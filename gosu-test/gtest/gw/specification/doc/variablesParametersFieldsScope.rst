.. _variablesParametersFieldsScope:

***************************************
Variables, Parameters, Fields and Scope
***************************************

.. index:: variable, parameter, field

A *variable* is declared inside a method, constructor, or block statement 
(see XXX).
The variable can be used only in that method (or constructor or block 
statement), and only after its declaration.

A *parameter* is a special kind of variable: it is declared in the parameter 
list of a method or constructor, and is given a value when the method or 
constructor is called. The parameter can be used only in that method or 
constructor.

A *field* is declared inside a class, but not inside a method or constructor of 
the class. It can be used anywhere in the class, also textually before its 
declaration.


Values Bound to Variables, Parameters, or Fields
================================================

A variable, parameter, or field of primitive type holds a *value* of that type, 
such as the boolean ``false``, the integer ``17``, or the floating-point 
number ``1.7``. A variable, parameter, or field of reference type ``t``
either has the special value ``null`` or holds a reference to an object or 
array. If it is an object, then the run-time class of that object must be ``t`` 
or a subclass of ``t`` or a reference type that is compatible with ``t``.


Variable Declarations
=====================

.. index:: variable declaration

The purpose of a variable is to hold a value during the execution of a block 
statement (or method or constructor). A *local variable-declaration* has the 
form

.. productionlist:: local variable
    LocalVarStatement : "var" `id` `OptionalType` ["=" `Expression`] .
    OptionalType : [":" `TypeLiteral` | `BlockTypeLiteral`] .


If a variable is declared ``final``, then it must be initialized or assigned at 
most once at run-time (exactly once if it is ever used): it is a 
*named constant*. However, if the variable has reference type, then the object
or array pointed to by the variable may still be modified.
A variable may be initialized in its declaration by providing an initializer 
*expression*. 
If a variable declaration has initializer *expression*, the variable's type may 
be omitted. The initializer *expression* cannot be the literal ``null`` or refer
to the declared variable itself. (see Type Inference)
The declared variable is given the type inferred for the *expression* as if that
type had been explicitly stated; subsequent uses of, or assignments to, the 
variable do not affect this type inference process. The variable remains 
statically typed as if the type had been explicitly given.
Execution of the variable declaration will reserve space for the variable, then
evaluate the initializer, if any, and store the resulting value in the variable.
Like a field (see XXX), a variable is given a *default initial value* when 
declared, depending on its type ``t``. If ``t`` is a primitive type, the 
variable is initialized  to 0 (when ``t`` is ``byte``, ``char``, ``short``, 
``int`` or ``long``) or 0.0 (when ``t`` is ``float`` or ``double``) or 
``false`` (when ``t`` is ``boolean``). If ``t`` is a reference type the variable
is initialized to ``null``.


Scope of Variables, Parameters, and Fields
==========================================

.. index:: scope

The *scope* of a name is that part of the program in which the name is visible.
The scope of a variable extends from just after its declaration to the end of 
the innermost enclosing block statement. The scope of a method or constructor 
parameter is the entire method or constructor body. For statements that may 
declare a new variable (``for``, ``using``, ``try-catch-finally``) the scope of 
that variable is the entire  body of the statement.
For example the variable ``x`` declared in a ``for`` statement 
``for (var x in ...) body``.
The ``switch`` statement (see XXX) creates a new scope for each ``case`` 
clause.
Within the scope of a variable or parameter ``x``, one cannot redeclare ``x``.


