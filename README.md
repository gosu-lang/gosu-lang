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


Build instructions
------------------
You need Java JDK 1.8 and Maven 3.x to build Gosu.
Set the JAVA_HOME environment variable to JDK 1.8's home.

Clone the project from GitHub and run:

    mvn compile


Quick Install
-------------

* Download the latest Gosu distribution [here](http://gosu-lang.github.io/downloads.html).
* Set the JAVA_HOME environment variable to JDK 1.8's home, if needed.
* Unzip/extract the distribution and run gosu-1.X/bin/gosu from the command line.

Support
-------

Need help getting your project off the ground? Want to discuss features in the next release, among both users and the Gosu team? Participate in our discussion forum or report a bug:

* [Forum](http://groups.google.com/group/gosu-lang "Forum")
* [Issue tracking](https://github.com/gosu-lang/gosu-lang/issues "Issues")

License
-------

Gosu is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0.txt "License").

Enjoy!
