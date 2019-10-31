The Gosu Language
=================

Gosu is a pragmatic programming language for the JVM. It has been designed with Java developers in mind by providing a set of features that allow them to be more productive without sacrificing the benefits of Java's simple syntax and type-safety. Gosu is an object oriented language with a sprinkle of functional programming features.

**Gosu features**:

* Open Type System
* Advanced type inference
* Program files (scripts containing statements, functions, and classes at the same level)
* Structural typing (similar TypeScript's interfaces)
* Extension methods (aka enhancements)
* Java interoperability
* Lambda expressions
* Classes/Interfaces/Enums
* Generics (reified, covariant type parameters and no wildcards)
* Composition (with the delegate keyword)
* Properties
* Null Safety (supports operator ?. etc.)
* Named Arguments and Default Parameter Values
* A powerful for each statement with user-defined intervals types
* Member Literals
* Object Initializers
* Classpath Statement and Shebang (useful when you use Gosu as a scripting language)
* ...and more

Build instructions
------------------
You need Java JDK 1.8 and Maven 3.x to build Gosu.
Set the JAVA_HOME environment variable to JDK 1.8's home.

Clone the project from GitHub and run:

    mvn compile

To execute tests:

    mvn test

To change the version number:

    mvn -B release:update-versions -DdevelopmentVersion=1-someValue-SNAPSHOT

Quickstart
----------

* Download the latest Gosu distribution [here](http://gosu-lang.github.io/downloads.html).
* Set the JAVA_HOME environment variable to JDK 1.8's home, if needed.
* Unzip the distribution zip, go to the bin folder and double click on gosu.cmd (or gosu if you are using Linux/Mac).

Support
-------

Need help getting your project off the ground? Want to discuss features in the next release, among both users and the Gosu team? Participate in our discussion forum or report a bug:

* [Issue tracking](https://github.com/gosu-lang/gosu-lang/issues "Issues")

License
-------

Gosu is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0.txt "License").


Enjoy!
