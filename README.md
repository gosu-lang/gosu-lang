The Gosu Language
=================

Gosu is a pragmatic programming language for the JVM.
It has been designed with Java developers in mind by providing a set of features that allow them to be more productive without sacrificing the benefits of static typing.
Gosu is an object oriented language with a sprinkle of functional programming features.
It was nominated Language of the Month in a Dr Dobb's [article](http://www.drdobbs.com/open-source/language-of-the-month-gosu/231001429# "Dr Dobb's article") in 2011.

**Gosu features**:

* Open Type System (similar to F# type providers)
* Type inference
* Dynamic typing (similar to C#'s dynamic type)
* Structural typing (similar Go's interfaces)
* Enhancements  (similar to Extension Methods in C#)
* Full Java interoperability
* Blocks (also called closures or lambda expressions)
* Classes/Interfaces/Enums
* Generics (reified, covariant type parameters and no wildcards)
* Composition (with the delegate keyword)
* Properties (similar to C#)
* Null Safety (with the Groovy-like operator ?. and many more)
* Named Arguments and Default Parameter Values
* A powerful for each statement with user-defined intervals types
* Feature Literals (to refer to a method on a Class)
* Object Initializers
* Classpath Statement and Shebang (useful when you use Gosu as a scripting language)
* ...and more


The Open Type System
--------------------
Excerpt from the article [What Differentiates Gosu From Other Languages?](http://devblog.guidewire.com/2012/02/27/what-differentiates-gosu-from-other-languages/ "Link"):

"The idea itself is simple: programmatic expansion of the type system.
The only language with a similar feature is F#, the idea was independently developed around the same time.
The enormous achievement with Gosu is that we have unified two otherwise discrete concepts: data and static types. Programmers can now work directly with raw data as first-class types and do it statically leveraging all the productivity features offered by modern, high quality IDEs.
The open type system breathes life into the otherwise dead world of raw data. This is the world we struggle with as programmers.  This is where we waste precious time and resources reading and deciphering and parsing and mapping and reinventing, and we do this over and over. The cracks and crevices between data and our world of objects is where the bad bugs lie. With Gosu suddenly data has the potential to be directly accessible as typed objects, be it XML, WSDL, properties, SQL, data markets, business rules, or you name it. It's the sort of thing that dynamic languages are incredibly good at but which, in a statically-typed language, has historically required reams of ugly code generation."

You can read more about the Open Type System in these articles:

* [Gosu's Secret Sauce: The Open Type System](http://devblog.guidewire.com/2010/11/18/gosus-secret-sauce-the-open-type-system/ "Link")
* [The Open Type System vs. Code Gen](http://devblog.guidewire.com/2011/05/23/the-open-type-system-vs-code-ge/ "Link")
* [Pragmatic Type Systems](http://devblog.guidewire.com/2008/07/25/pragmatic-type-systems/ "Link")
* [Why Gosu?](http://devblog.guidewire.com/2010/11/11/why-gosu/ "Link")


IntelliJ IDEA plugin
--------------------

The IntelliJ plugin for Gosu has been in development for a while now.
It has a rich number of features and it is pretty stable now.
The plugin enables you to:

* Create Gosu classes, programs, etc.
* Syntax highlighting and error highlighting
* Converting Java code to Gosu code ("Paste Java as Gosu" action)
* Go to Declaration (including Quick Definition lookup);
* Code completion
* Parameter information
* Find Usages
* Rename and Move refactoring
* Code formatter
* Code inspections and intentions
* Structure View (including the File Structure Popup)
* Hierarchy view
* Go to Class, Go to Symbol
* Brace matching
* Code folding
* Javadoc
* Launch Gosu programs directly via IJ run configurations
* Debug Gosu
* Gosu scratchpad
* Incremental Gosu compiler
* Custom typeloader support
* Hyperlink to Gosu from stack traces

Build instructions
------------------
You need Java JDK 1.7 and Maven 3.0 to build Gosu and the IntelliJ IDEA plugin.

Simply clone the project from GitHub and run:

    mvn compile


Quick Install
-------------

The Gosu Plugin for IntelliJ is the recommended way to use Gosu.
The plugin is hosted on the IntelliJ IDEA Plugin Repository and you can download it directly from within IntelliJ IDEA.
For complete installation instructions and important information about the IntelliJ IDEA plugins, refer to the [plugin page](http://gosu-lang.org/intellij.html "Plugin").



Command Line Shell
------------------

If you run the Gosu command-line tool without any arguments, Gosu an interactive shell for writing and running lines of Gosu.
For more information, refer to the Gosu shell section in the [documentation](http://gosu-lang.org/doc/index.html "Docs").


Support
-------

Need help getting your project off the ground? Want to discuss features in the next release, among both users and the Gosu team? Participate in our discussion forum or report a bug:

* [Forum](http://groups.google.com/group/gosu-lang "Forum")
* [Issue tracking](https://github.com/gosu-lang/gosu/issues "Issues")

License
-------

The Gosu and the IntelliJ IDEA plugin are released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0.txt "License").

Enjoy!
