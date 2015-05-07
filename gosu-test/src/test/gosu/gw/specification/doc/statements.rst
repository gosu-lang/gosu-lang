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
assignment*

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

A ``break`` statement is legal only inside a ``switch`` or ``loop``, and has the
form

    ``break``

Executing ``break`` exits the innermost enclosing ``switch`` or loop, and
continues execution after that ``switch`` or loop.

The ``continue`` Statement
--------------------------

A ``continue`` statement is legal only inside a loop, and has the form

    ``continue``

Executing ``continue`` terminates the current iteration of the innermost
enclosing loop, and continues the execution at  with the next element
(in ``for`` loops) or the *condition* (in ``while`` and ``do-while`` loops).
