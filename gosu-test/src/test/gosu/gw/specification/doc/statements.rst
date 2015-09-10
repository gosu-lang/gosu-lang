.. _statements:

***********
Statements
***********

.. index:: statements

A *statement* may change the computer's *state*: the value of variables,
fields, array elements, the contents of files, and so on. More precisely,
execution of a statement either

* terminates normally (meaning execution will continue with the next statement,
  if any); or
* terminates abruptly by throwing an exception; or
* exits by executing a ``return`` statement (if inside a method or
  constructor); or
* exits a switch or loop by executing a ``break`` statement (if inside a switch
  or loop); or
* exits the current iteration of a loop and starts a new iteration by executing
  a ``continue`` statement (if inside a loop); or
* does not terminate at all, for instance, by executing ``while (true) {}``.

Expression Statements
=====================

.. index:: expression statements

An *expression statement* is an *expression* optionally followed by a semicolon:

``expression`` [``;``]

It is executed by evaluating the *expression* and ignoring its value. The only
forms of *expression* that may be legally used in this way are method call
expressions (section XXX) and object creation expressions (section XXX). For
example, a method call statement is a method call expression followed by
semicolon. The value returned by the method, if any, is discarded; the method
is executed only for its side effect.

Block Statements
================

.. index:: block statements

A *block-statement* is a sequence of zero or more *statements* or
*variable-declarations*, in any order, enclosed in braces::

    {
      statements
      variable-declarations
    }

Within a block, a variable can be used only after its declaration.

The Empty Statement
===================

.. index:: empty statement

An *empty statement* consists of a semicolon only. It is equivalent to the
block statement ``{ }`` that contains no statements or declarations, and it has
no effect at all: ``;``


Choice Statements
=================

The ``if-else`` Statement
-------------------------

.. index:: if statement

An ``if-else`` statement has the form::

    if (condition)
      truebranch
    [else
      falsebranch]

The *condition* must have type ``boolean`` or ``Boolean``, and *truebranch* and
*falsebranch* are statements. If *condition* evaluates to ``true``, then
*truebranch* is executed; otherwise, if the optional else clause is present,
*falsebranch* is executed.

If the *condition* is a type test of the form ``e typeis T``, any occurrence of
``e`` in *truebranch* will be implicitly guarded by a cast: ``e as T``.

The ``switch`` Statement
------------------------

.. index:: switch statement

A ``switch`` statement has the form::

  switch (expr) {
    case c1: branch1
    case c2: branch2
    ...
    case cn: branchn
    default: branch
  }

The *expr* can be any expression.
*c*\ :sub:`1`, ..., *c*\ :sub:`n` can be compile-time *constant* expressions
(including enum values) or they can be any expression.
No two *constants* may have the same value. Each *c*\ :sub:`i` must have a
type compatible the type of *expr*.

Each *branch* is preceded by one or more *case* clauses and is a possibly empty
sequence of statements, usually terminated by ``break`` or ``return``
(if inside a method or constructor) or ``continue`` (inside a loop). There can
be at most one *default* clause, placed last inside the *switch* statement.

If *expr* is a typeof expression (``e typeof T``) and for a *branch*\ :sub:`i`
its *c*\ :sub:`i` is a type literal expression then any occurrence of
``e`` in *branch*\ :sub:`i` will be implicitly guarded by a cast: ``e as T``.

The *switch* statement is executed as follows: The *expr* is evaluated to obtain
a value ``v``. If ``v`` equals one of the *c*\ :sub:`1`, ..., *c*\ :sub:`n`,
then the corresponding *branch* is executed. If ``v`` does not equal any of the
*c*\ :sub:`1`, ..., *c*\ :sub:`n`, then the *branch* following ``default`` is
executed; if there is no ``default`` clause, nothing is executed. If a *branch*
is not exited by ``break`` or ``return`` or ``continue``, then execution
continues with the next *branch* in the switch regardless of the ``case``
clauses, until a *branch* exits or the switch ends.



Assignment Statements
=====================

.. index:: assignment statements

In the *assignment expression* ``x = e``, the type of ``e`` must be implicitly
convertible to the type of ``x`` (see XXX). The type of the expression ``x =
e`` is the same as the type of ``x``. The assignment is executed by evaluating
expression ``x``, then evaluating expression ``e`` and implicitly converting
the value to the type of ``x``(if necessary), and finally storing the result in
variable ``x``. The value of the expression ``x = e`` is the value that was
stored in ``x``.

The left-hand side ``x`` may be a local variable or parameter, or a field, or a
property or an element access expression (see XXX). When ``e`` is a
compile-time constant of type ``byte``, ``char``, ``short``, or ``int``, and
``x`` has type ``byte``, ``char``, or ``short``, a narrowing conversion is done
automatically, provided the value of ``e`` is within the range representable in
``x`` (section xxx).

When ``e`` has reference type (object type or array type), only a reference to
the object or array is stored in ``x``. Thus the assignment ``x = e`` does not
copy the object or array.

A *compound assignment* has the form ``x += e``, and is legal in two cases.
Either ``x + e`` must be implicitly convertible to the type of ``x``, in which
case the compound assignment ``x += e`` is equivalent to ``x = x + e``.
Otherwise ``x + e`` must be explicitly convertible to the type ``t`` of ``x``,
and ``e`` must be implicitly convertible to ``t``, in which case the compound
assignment ``x += e`` is equivalent to ``x = (x + e) as t``. In both cases,
``x`` is evaluated only once. The other compound assignment operators ``+=``,
``-=``, ``*=``, ``/=``, ``&=``, ``&&=``, ``|=``, ``||=``, ``^=``, ``%=``, are
similar.

The increment statement ``x++`` has the effect to increment ``x`` by ``1``; and
similarly for decrement ``x--``. They are a special case of *compound
assignment*.

Loop Statements
===============

The ``while`` Statement
-----------------------

.. index:: while statement

A ``while`` statement has the form

    ``while`` ``(`` *condition* ``)`` *body*

where *condition* is an expression of type ``boolean`` or Boolean, and ``body``
is a statement. It is executed as follows:

1. The *condition* is evaluated. If it is ``false``, the loop terminates.
2. If it is ``true``, then

  a. The *body* is executed.
  b. Execution continues at (1).

Just after the ``while`` loop, the negation of *condition* must hold(unless
the loop is exited by ```break``.). This fact provides a useful information
about program's state after the loop.

When a *loop invariant* -- a property that always holds at the beginning and
end of the loop body -- is known as well, then one can combine it with the
negation of the *condition* to get precise information about the program's
state after the ``while`` loop. This often helps understanding short but subtle
loops.

The ``do-while`` Statement
--------------------------

.. index:: do-while statement

A ``do-while`` statement has the form

    ``do`` *body* ``while`` ``(`` *condition* ``)``

where *condition* is an expression of ttype ``boolean`` or Boolean, and ``body``
is a statement. The *body* is executed at least once, because the ``do-while``
statement is executed as follows:

  1. The *body* is executed.
  2. The *condition* is evaluated. If it is ``false``, the loop terminates.
  3. If it is ``true``, then execution continues at (1).

Hence the ``do-while`` statement above is equivalent to the following statement
using ``while``:

    *body* ``while`` ``(`` *condition* ``)`` *body*

The ``For`` Statement
---------------------

.. index:: for statement

A ``for`` statement has one of the following forms:

    ``for`` ``(`` [``var``] *x* ``in`` *expression* ``index`` *i* ``)`` *body*

    ``for`` ``(`` [``var``] *x* ``in`` *expression* ``iterator`` *iter* ``)`` *body*

    ``for`` ``(`` [``var``] *x* ``in`` *expression* ``index`` *i*  ``iterator`` *iter* ``)`` *body*


The *expression* must have one of the following types:

- any array type, ``T[]``
- ``Iterable<T>``
- ``Iterator<T>``
- ``String``

and *x* is a new variable local to the loop *body* of inferred type ``T`` (or
``String`` if *expression* is of type ``String``). *body* is  a statement.

First the *expression* is evaluated to obtain an ``Iterator``. Then the *body*
is evaluated for each element produced by the iterator with variable ``x`` bound
to that element.

The ``var`` keyword is optional. If the ``index`` keyword is present it must
be followed by a variable name *i* (of type ``int``) that will be bound to the
index of the current iteration. If the *expression*'s type is an ``Iterable``
and the ``iterator`` keyword is present it must be followed by a variable name
*iter* that will be bound to the *expression*'s ``Iterator``.

The following special shorthand version of the for loop can be used when the
local variable ``x`` is not needed in the *body* of the loop

    ``for`` ``(`` *expression*  [``index`` *i*] ``)`` *body*

Only the ``index`` keyword can be used in this form.

Returns, Exits, and Exceptions
==============================

The ``return`` Statement
------------------------

.. index:: return statement

The simplest form of a ``return`` statement, without an expression argument, is

   ``return``

That form of ``return`` statement must occur inside the body of a method or
block whose return type is ``void``, in the body of a constructor or in a
``property set``, but not in a ``property get``. Execution of the
``return`` statement exits the method or constructor and continues execution at
the place from which it was called.

Alternatively, a ``return`` statement may have an expression argument:

    ``return`` *expression*

That form of ``return`` statement must occur inside the body of a method or
block whose return type is non-``void``, in the a ``property get``, but not in
a constructor or in a ``property set``. The type of the *expression* must be
implicitly convertible to the return type of the enclosing function. The
``return`` statement is executed as follows. First the *expression* is evaluated
to some value ``v``. Then it exits the method and continues execution at the
method call expression that called the method; the value of that expression will
be v.

The ``break`` Statement
-----------------------

.. index:: break statement

A ``break`` statement is legal only inside a ``switch`` or ``loop``, and has the
form

    ``break``

Executing ``break`` exits the innermost enclosing ``switch`` or loop, and
continues execution after that ``switch`` or loop.

The ``continue`` Statement
--------------------------

.. index:: continue statement

A ``continue`` statement is legal only inside a loop, and has the form

    ``continue``

Executing ``continue`` terminates the current iteration of the innermost
enclosing loop, and continues the execution at  with the next element
(in ``for`` loops) or the *condition* (in ``while`` and ``do-while`` loops).

The ``throw`` Statement
------------------------

.. index:: throw statement

A ``throw`` statement has the form

    ``throw`` *expression*

where the type of the *expression* must be a subtype of class Throwable or a
``String``. The ``throw`` statement is executed as follows: The *expression* is
evaluated to obtain an exception object ``v``, if *expression* is a ``String``
a new RuntimeException will be created having that string as argument. If it is
``null``, then a NullPointerException is thrown; otherwise the exception object
``v`` is thrown. Thus a thrown exception is never ``null``. In any case, the
enclosing block statement terminates abruptly.

The thrown exception may be caught in a dynamically enclosing ``try-catch``
statement (see XXX). If the exception is not caught, then the entire program
execution will be aborted, and information from the exception will be printed
on the console.


The ``try-catch-finally`` Statement
-----------------------------------

.. index:: try-catch-finally statement

A ``try-catch`` statement is used to catch (particular) exceptions thrown by the
execution of a block of code. It has the following form:

    ``try`` body

    ``catch`` ``(`` [var] ``x1 : E1`` ``)`` catchbody\ :sub:`1`

    ``catch`` ``(`` [var] ``x2 : E2`` ``)`` catchbody\ :sub:`2`

    ``...``

    ``finally`` finallybody

where ``E1, E2, ...`` are names of exception types, ``x1, x2, ...`` are
variable names, and *body*, *catchbody*\ :sub:`i`, and *finallybody* are
*block-statements* (section XXX). There can be zero or more ``catch`` clauses,
and the ``finally`` clause may be absent, but at least one ``catch`` or
``finally`` clause must be present. The ``var`` keyword in the ``catch`` clause
is optional.

We say that ``Ei`` matches exception type ``E`` if ``E`` is a subtype of ``Ei``
(possibly equal to ``Ei``). The ``try-catch-finally`` statement is executed by
executing the *body*. If the execution of the *body* terminates normally, or
exits by ``return`` or ``break`` or ``continue`` (when inside a method or
constructor or switch or loop), then the ``catch`` clauses are ignored. If the
*body* terminates abruptly by throwing exception ``e`` of class ``E``, then the
first matching ``Ei`` (if any) is located, variable ``xi`` is bound to ``e``,
and the corresponding *catchbody*\ :sub:`i` is executed. The *catchbody*\
:sub:`i` may terminate normally, or loop infinitely, or exit by executing
``return`` or ``break`` or ``continue``, or throw an exception (possibly
``xi``); if there is no ``finally`` clause, this determines how the entire
``try-catch`` statement terminates. A thrown exception ``e`` is never ``null``
(section XXX), so ``xi`` is guaranteed not to be ``null`` either. If there is
no matching ``Ei``, then the entire ``try-catch`` statement terminates abruptly
with exception ``e``.

If there is a ``finally`` clause, then *finallybody* will be executed
regardless of whether the execution of *body* terminated normally, regardless
of whether *body* exited by executing ``return`` or ``break`` or ``continue``,
regardless of whether any exception thrown by *body* was caught by a ``catch``
clause, and regardless of whether the ``catch`` clause exited by executing
``return`` or ``break`` or ``continue`` or by throwing an exception. If
execution of *finallybody* terminates normally, then the entire
``try-catch-finally`` terminates as determined by *body* (or *catchbody*\
:sub:`i` , if one was executed and terminated abruptly or exited). If execution
of *finallybody* terminates abruptly, then that determines how the entire
``try-catch-finally`` terminates.

The ``using`` Statement
=======================

.. index:: using statement

The purpose of the ``using`` statement is to release a resource *res*, such as
a file handle, lock or database connection, after its use. It may have one of
the forms:

    ``using`` ``(`` ``var`` *res* [``:`` *type*] ``=`` *initializer*  ... ``)`` ``{`` *body* ``}`` [ ``finally`` ``{`` *finally-body* ``}`` ]
    
    ``using`` ``(`` *expression* ``)`` ``{`` *body* ``}`` [ ``finally`` ``{`` *finally-body* ``}`` ]
    
The first form declares a variable *res* to have type *type*, or if *type* 
is missing, the inferred type of *initializer*. There can be multiple declared 
resources.

*type* must be one of the following types:

- Lock
- Closeable
- IReentrant
- IDisposable
- IMonitorLock
- Any type with method ``dispose()``
- Any type with method ``close()``
- Any type with method ``lock()`` and ``unlock()``

The *initializer* is evaluated and its result assigned to *res* (if ``res``'s 
*type* has a ``lock`` or ``enter`` method, it will be called on *res*), then 
the *body* is executed, and finally the "clean-up" method ``dispose`` or 
``close`` or ``unlock`` or ``exit`` (depending on ``res``'s *type*) is called 
on *res* regardless of whether *body* terminates normally, throws an exception, 
or exits by ``return`` or ``break`` or ``continue``. If ``finally`` is present, 
*finally-body* will be executed after the call to the "clean-up" method. 
Resource variables like *res* are implicitly *final* and they are local to the 
``using`` statement. The second form of the ``using`` statement has an 
*expression* in place of the variable list and the "clean-up" method is called 
on the value of the *expression*. It behaves as the first form otherwise. 

The ``assert`` Statement
========================

.. index:: assert statement

A ``assert`` statement has one of the following forms:

    ``assert`` *boolean-expression*
    ``assert`` *boolean-expression* : *expression*

The *boolean-expression* must have type ``boolean`` or Boolean.

Under ordinary execution of a program, an ``assert`` statement has no effect at
all. However, assertions may be enabled at run-time by specifying the option
*-ea* or *-enableassertions* when executing a program.

When assertions are enabled at run-time, every execution of the ``assert``
statement will evaluate the *boolean-expression*. If the result is ``true``,
program execution continues normally. If the result is ``false``, the assertion
fails and an AssertionError will be thrown; moreover, in the second form of the
``assert`` statement, the *expression* will be evaluated and its value will be
passed to the appropriate AssertionError contructor. Thus the value of
*expression* will be reported along with the exception in case of assertion
failure. This simplifies troubleshooting in a malfunctioning program.

An AssertionError sgnals the failure of a fundamental assumption in the program
and should not be caught by a ``try-catch`` statement in the program; it should
be allowed to propagate to the toplevel.

An ``assert`` statement can serve two purposes: to document the programmer's
assumption about the state at a certain point in the program, and to check (at
runtime) that that assumption holds (provided the program is executed using the
*enableassertions* option).

One may put an ``assert`` statement after a particular complicated piece of
code, to check that it has achieved what it was supposed to; or in a class that
has a data representation invariant, one may assert the invariant at the end of
every method that could modify the state of the current object.

One should not use ``assert`` statements to check the validity of user input or
the arguments of public methods or constructors, because the check will be
performed only if assertions are enabled at run-time. Instead use ordinary
``if`` statements and handle the error.


The ``eval`` Statement
======================

.. index:: eval statement

An ``eval`` statement has the form:

    ``eval`` ``(`` *gosu-source* ``)``
    
Where *gosu-source* is any expression of type Object.

After converting *gosu-source* to String, ``eval`` will execute it at runtime 
and return the result of the evaluation. The statements or expressions in 
*gosu-source* can access all the variables available in the ``eval``'s context.


The ``uses`` statement
======================

.. index:: uses statement

Gosu source files may be organized in *packages*. Every source file in package 
``p`` must begin with the declaration ``package p`` and must be stored in a 
subdirectory called ``p``. A class declared in a source file with no 
``package`` declaration belongs to the anonymous *default package*. A source 
file not belonging to package ``p`` may refer to class ``C`` from package ``p`` 
by using the qualified name ``p.C``, in which the class name ``C`` is prefixed 
by the package name. To avoid using the package name prefix, the source file 
may begin with an ``import`` declaration (possibly following a ``package`` 
declaration) of one of these two forms: 

  ``import p.C`` 
  
  ``import p.*`` 

The first form allows ``C`` to be used unqualified, without the package name, 
and the second one allows all accessible types (classes, interfaces ...) in
package ``p`` to be used unqualified. The Java class library packages
``java.lang`` and ``java.util`` are implicitly imported into all source
files, as if by ``uses java.lang.*`` and ``uses java.util.*``


