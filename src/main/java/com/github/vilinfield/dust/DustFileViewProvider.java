package com.github.vilinfield.dust;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.OuterLanguageElementType;
import com.intellij.util.IncorrectOperationException;
import com.github.vilinfield.dust.psi.DustPsiUtil;
import com.github.vilinfield.dust.psi.DustTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.coverage.gnu.trove.THashSet;

import java.util.Arrays;
import java.util.Set;

public class DustFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements TemplateLanguageFileViewProvider
{
    public static IElementType OUTER_TYPE = new OuterLanguageElementType("OUTER", DustLanguage.INSTANCE);
    private static final TemplateDataElementType templateDataElementType = new TemplateDataElementType("DUST_TEMPLATE_DATA", DustLanguage.INSTANCE, DustTypes.HTML, OUTER_TYPE);

    // Main language of the file (HTML).
    private final Language myTemplateDataLanguage;

    // Default constructor from parent.
    public DustFileViewProvider(PsiManager manager, VirtualFile file, boolean physical)
    {
        super(manager, file, physical);

        // Get the main language of the file.
        Language dataLang = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
        if (dataLang == null)
        {
            dataLang = HtmlFileType.INSTANCE.getLanguage();
        }

        // Some magic?
        if (dataLang instanceof TemplateLanguage)
        {
            myTemplateDataLanguage = PlainTextLanguage.INSTANCE;
        }
        else
        {
            myTemplateDataLanguage = LanguageSubstitutors.getInstance().substituteLanguage(dataLang, file, manager.getProject());
        }
    }

    // Constructor to be used by self.
    public DustFileViewProvider(PsiManager psiManager, VirtualFile virtualFile, boolean physical, Language myTemplateDataLanguage)
    {
        super(psiManager, virtualFile, physical);
        this.myTemplateDataLanguage = myTemplateDataLanguage;
    }


    @NotNull
    @Override
    public Language getBaseLanguage()
    {
        return DustLanguage.INSTANCE;
    }

    @NotNull
    @Override
    public Language getTemplateDataLanguage()
    {
        return myTemplateDataLanguage;
    }

    @NotNull
    @Override
    public Set<Language> getLanguages()
    {
        return new THashSet<>(Arrays.asList(DustLanguage.INSTANCE, myTemplateDataLanguage));
    }

    @NotNull
    @Override
    protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile virtualFile)
    {
        return new DustFileViewProvider(getManager(), virtualFile, false, myTemplateDataLanguage);
    }

    @Override
    protected PsiFile createFile(@NotNull Language lang)
    {
        // Creating file for main lang (HTML).
        if (lang == myTemplateDataLanguage)
        {
            PsiFileImpl file = (PsiFileImpl) LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
            file.setContentElementType(templateDataElementType);
            return file;
        }
        else if (lang == DustLanguage.INSTANCE)
        {
            return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
        }
        else
        {
            return null;
        }
    }

    /**
     * TODO This method is for just for supporting Intellij 11.
     *  Otherwise com.intellij.codeInsight.navigation.actions.GotoDeclarationAction lines 149-151 will throw
     *  nullpointerexception or assertion fail.
     */
    @Override
    @Nullable
    public PsiReference findReferenceAt(int offset)
    {
        for (final Language language : getLanguages())
        {
            final PsiElement psiRoot = getPsi(language);
            if (psiRoot != null)
            {
                final PsiElement element = psiRoot.getContainingFile().getViewProvider().findElementAt(offset, language);
                if (element != null && !(element instanceof OuterLanguageElement)
                        && DustPsiUtil.findParentPartialTagElement(element) != null)
                {

                    PsiElement[] foundElements = DustGotoDeclarationHandler.gotoReferences(element);
                    PsiReference fakeReference = null;
                    if (foundElements != null && foundElements.length > 0)
                    {
                        fakeReference = new PsiReference()
                        {
                            @NotNull
                            @Override
                            public PsiElement getElement()
                            {
                                return element;
                            }

                            @NotNull
                            @Override
                            public TextRange getRangeInElement()
                            {
                                TextRange range = element.getTextRange();
                                int start = range.getStartOffset();
                                return new TextRange(0, range.getEndOffset() - start);
                            }

                            @NotNull
                            @Override
                            public PsiElement resolve()
                            {
                                return element;
                            }

                            @NotNull
                            @Override
                            public String getCanonicalText()
                            {
                                return element.getText();
                            }

                            @Override
                            public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException
                            {
                                return null;
                            }

                            @Override
                            public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
                            {
                                return null;
                            }

                            @Override
                            public boolean isReferenceTo(@NotNull PsiElement element)
                            {
                                return false;
                            }

                            @NotNull
                            @Override
                            public Object[] getVariants()
                            {
                                return new Object[0];
                            }

                            @Override
                            public boolean isSoft()
                            {
                                return false;
                            }
                        };
                    }
                    return fakeReference;
                }
            }
        }
        return super.findReferenceAt(offset);
    }
}
