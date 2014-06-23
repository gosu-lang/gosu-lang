.. _runningGosu:

*************************************************
Running Gosu: Compilation, Loading, and Execution
*************************************************

.. index:: compilation, execution

Before a Gosu program can be executed, it must be compiled and loaded. The 
compiler checks that the Gosu program is *legal*: that the program conforms to 
the Gosu syntax or grammar, that operators (such as +) are applied operands 
(such as 5 and x) of the correct type, and so on. If so, the compiler generates 
so-called class files. Execution may then be started by loading the needed 
*class files*. Thus running a Gosu program involves three stages: *compilation* 
(checks that the program is well-formed), *loading* (loads and initializes 
classes), and *execution* (runs the program code).

TODO:

* add information about gosuc, type system?
