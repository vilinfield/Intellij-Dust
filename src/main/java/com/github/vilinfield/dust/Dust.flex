package com.github.vilinfield.dust;

import com.github.vilinfield.dust.psi.DustTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.Stack;

%%

%class DustLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}
//%debug

%{
  private static final String[] checks = {"\"","(",")","=","\\s", ";"};
  private static final String[] tagSymbols = {"#","?","^","@","<", ">", "+", "/", ":", "!"};

  private Stack<Integer> lexStateStack = new Stack<java.lang.Integer>();

  private void pushState(int state) {
    lexStateStack.push(yystate());
    yybegin(state);
  }

  private void popState() {
    if (lexStateStack.empty()) {
      yybegin(YYINITIAL);
    } else {
      yybegin(lexStateStack.pop());
    }
  }
%}

CRLF= \n|\r|\r\n
WS= {CRLF} | [\ \t\f]

SECTION=\{\#
EXISTANCE=\{\?
NOT_EXISTANCE=\{\^
HELPER=\{@
PARTIAL=\{>
INLINE_PARTIAL=\{<
BLOCK=\{\+
CLOSE=\{\/
ELSE=\{:

LD=\{
RD=\}
SLASH_RD=\/\}
LB=\[
RB=\]

EQUAL==
PIPE=\|
PERIOD_SPACE=\.{WS}
PERIOD_NOSPACE=\.
COLON=:

STRING=\"((\\.)|[^\"])*\"
STRING_SINGLE='((\\.)|[^'])*'

IDENTIFIER=[a-zA-Z_$][\-a-zA-Z_0-9]*

%state DUST_TAG
%state DUST_ATTR
%state DUST_ATTR_STRING_SINGLE
%state DUST_ATTR_STRING_DOUBLE
%state COMMENT

%%

{WS}+                                 { return TokenType.WHITE_SPACE; }

<COMMENT> {
  "{!" ~"!}"     { popState(); return DustTypes.COMMENT; }
}

{SECTION}                             { pushState(DUST_TAG); return DustTypes.SECTION; }
{EXISTANCE}                           { pushState(DUST_TAG); return DustTypes.EXISTANCE; }
{NOT_EXISTANCE}                       { pushState(DUST_TAG); return DustTypes.NOT_EXISTANCE; }
{HELPER}                              { pushState(DUST_TAG); return DustTypes.HELPER; }
{PARTIAL}                             { pushState(DUST_TAG); return DustTypes.PARTIAL; }
{INLINE_PARTIAL}                      { pushState(DUST_TAG); return DustTypes.INLINE_PARTIAL; }
{BLOCK}                               { pushState(DUST_TAG); return DustTypes.BLOCK; }
{CLOSE}                               { pushState(DUST_TAG); return DustTypes.CLOSE; }
{ELSE}                                { pushState(DUST_TAG); return DustTypes.ELSE; }

{LD}{WS}+                             { return DustTypes.HTML; }
{LD}{CRLF}+                           { return DustTypes.HTML; }

<YYINITIAL> {
{LD}~{RD}                             {
  boolean isDustLD = true;

  if (yylength() > 2) {
    boolean isAllWhitespace = true;
    CharSequence cSeq = yytext();
    int size = cSeq.length();

    for (int i = 1; i < size - 1; i++) {
      if (!Character.isWhitespace(cSeq.charAt(i))) { isAllWhitespace = false; break; }
    }

    if (!isAllWhitespace) {
      while (yylength() > 1) {
          String c = yytext().subSequence(yylength() - 1, yylength()).toString();
          for (int i = 0; i < checks.length; i++) {
            if (c.equals(checks[i])) isDustLD = false;
          }
          if (yylength() == 2) {
            if (c.equals("#")) {
              pushState(DUST_TAG); return DustTypes.SECTION;
            } else if (c.equals("?")) {
              pushState(DUST_TAG); return DustTypes.EXISTANCE;
            } else if (c.equals("^")) {
              pushState(DUST_TAG); return DustTypes.NOT_EXISTANCE;
            } else if (c.equals("@")) {
              pushState(DUST_TAG); return DustTypes.HELPER;
            } else if (c.equals(">")) {
              pushState(DUST_TAG); return DustTypes.PARTIAL;
            } else if (c.equals("<")) {
              pushState(DUST_TAG); return DustTypes.INLINE_PARTIAL;
            } else if (c.equals("+")) {
              pushState(DUST_TAG); return DustTypes.BLOCK;
            } else if (c.equals("/")) {
              pushState(DUST_TAG); return DustTypes.CLOSE;
            } else if (c.equals(":")) {
              pushState(DUST_TAG); return DustTypes.ELSE;
            } else if (c.equals("!")) {
              pushState(COMMENT); yypushback(2); return DustTypes.COMMENT;
            } else if (c.equals(" ")) {
              return DustTypes.HTML;
            } else if (c.equals("\n")) {
              return DustTypes.HTML;
            }
          }
          yypushback(1);
      }
    } else {
      isDustLD = false;
    }
  } else {
    isDustLD = false;
  }


  if (isDustLD) {
    pushState(DUST_TAG); return DustTypes.LD;
  } else {
    return DustTypes.HTML;
  }
}
!([^]*"{"[^]*)                         { return DustTypes.HTML; }
}

{LD}                                  { pushState(DUST_TAG); return DustTypes.LD; }

<DUST_TAG> {
  {STRING_SINGLE}                       { return DustTypes.STRING_SINGLE; }
  {STRING}                              { return DustTypes.STRING; }

  {RD}                                  { popState(); return DustTypes.RD; }
  {SLASH_RD}                            { popState(); return DustTypes.SLASH_RD; }

  {EQUAL} / ['\"]                       { pushState(DUST_ATTR); return DustTypes.EQUAL; }
  {EQUAL}                               { return DustTypes.EQUAL; }
  {PIPE}                                { return DustTypes.PIPE; }
  {PERIOD_SPACE}                        { return DustTypes.PERIOD_SPACE; }
  {PERIOD_NOSPACE}                      { return DustTypes.PERIOD_NOSPACE; }
  {COLON}                               { return DustTypes.COLON; }
  {LB}                                  { return DustTypes.LB; }
  {RB}                                  { return DustTypes.RB; }

  {IDENTIFIER}+                         { return DustTypes.IDENTIFIER; }
  [0-9]+\.?[0-9]+                       { return DustTypes.NUMBER; }
  [0-9]+                                { return DustTypes.NUMBER; }
}

<DUST_ATTR> {
  "\""                        { pushState(DUST_ATTR_STRING_DOUBLE); return DustTypes.STRING_START; }
  "\'"                        { pushState(DUST_ATTR_STRING_SINGLE); return DustTypes.STRING_START; }
}

<DUST_ATTR_STRING_SINGLE> {
  ((\\.)|[^'{}])+                     { return DustTypes.STRING; }
  "'"                         { popState();popState(); return DustTypes.STRING_END; }
}

<DUST_ATTR_STRING_DOUBLE> {
  ((\\.)|[^\"{}])+                    { return DustTypes.STRING; }
  "\""                        { popState();popState(); return DustTypes.STRING_END; }
}

{CRLF}                                { return DustTypes.CRLF; }

{WS}+                                 { return TokenType.WHITE_SPACE; }

.                                     { return DustTypes.HTML; }