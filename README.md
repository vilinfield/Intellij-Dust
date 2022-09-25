# Dust Plugin for Intellij

[Dust.js](https://github.com/linkedin/dustjs) Template Support.

Provides syntax highlighting for the Dust.js Templating Language, goto declaration on dust partial tags and support
within standard HTML documents.

Forked from [https://github.com/yifanz/Intellij-Dust](https://github.com/yifanz/Intellij-Dust)
which is no longer maintained.

## Install

Plugin is available from the [Jetbrains plugin repository](https://plugins.jetbrains.com/plugin/16458-dust-js).

### Install From Repository (recommended)

1. Search for and install the plugin: File -> Settings -> Plugins -> Browse repositories.

### Manual Install

1. Download the latest zip file (or jar for versions < 0.4.2) from [GitHub Releases](https://github.com/vilinfield/Intellij-Dust/releases).
2. Go to File -> Settings -> Plugins -> Install plugin from disk.

### After Installation

1. Associate plugin with your dust file extension: File -> Settings -> Editor -> File Types -> Dust template (*.dust and *.tl are associated by default).
2. Customize appearance: File -> Settings -> Editor -> Color Scheme -> Dust (optional, inherits from defaults).

## Developer Notes

1. Open the project with Intellij. The project is already setup to be a Gradle IntelliJ Plugin and should have the
   build settings already configured. The only dependency is a Java 11 SDK which may need to be configured for your system.
2. Install the [Grammar-Kit](https://plugins.jetbrains.com/plugin/?id=6606) plugin.
3. You will need Grammar-Kit to generate the parser source files from Dust.bnf and JFlex to generate the DustLexer
   from Dust.flex. Since the generated sources are not checked into version control, you need to remember to generate
   the lexer/parser before compiling using the generateParser and generateLexer Gradle tasks.
4. Build the plugin and prepare it for deployment using the buildPlugin Gradle task.
5. (Optional) Install [PSI Viewer](https://plugins.jetbrains.com/plugin/?id=227) plugin which lets you see the parse
   tree graphically.

## Release Notes

**Version 0.4.2**

* Improve compatability with newer Intellij releases

**Version 0.4.1**

* Fix range must be inside element being annotated exception

**Version 0.4.0**

* Maintainer change from Yi-Fan Zhang (yifanz) to Victor Linfield (vilinfield)
* Add support for newer versions of Intellij > 2020.3
* Inherit color scheme from defaults
* Update colorscheme page demo
* Add *.dust as a default file type alongside *.tl
* Change file icon to an SVG matching the default icons for other template languages

**Version 0.3.8**

* Allow hyphen inside of identifiers

**Version 0.3.7**

* Use system dependent file separator when resolving partial declarations
* Fixed parsing errors with single period path expressions
* Allow multiple colon tags

**Version 0.3.6**

* Allow path expression inside subscript
* Fix bugs in subscript tokenizer rule

**Version 0.3.5**

* Allow self-closing section tags in grammar

**Version 0.3.4**

* Fixed bug with parsing numeric key tag

**Version 0.3.3**

* Fixed bug with using current context and numbers as attribute values

**Version 0.3.2**

* Fixed bug in comment parsing

**Version 0.3.1**

* Fixed compatibility issues with Intellij 11 and set it as the minimum supported version
* Fixed bugs in left curly brace and identifier token patterns in lexer

**Version 0.3**

* Added closing tag auto-completion
* Added goto declaration shortcut "Ctrl+b" on dust partial tag references
* Fix parsing error on self-closing block tags
* Fix brace matcher bug when key tags are used in attribute strings
* Remove redundant HTML pattern rules in Dust lexer

**Version 0.2**

* Added Dust brace match highlighting
* Added "Ctrl+/" shortcut for Dust comments
* Fix syntax highlighting for subscript operator in tags (e.g. {#section[0]}...{/section[0]})
* Added TODO highlighting in comments

**Version 0.1.2**

* Fixes syntax highlighting when javascript is present in the template

**Version 0.1.1**

* Provides syntax highlighting for dust templates
* Enabled for all Jetbrains IDEs
