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

The if-else Statement
---------------------

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

The switch Statement
--------------------

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

In the assignment expression x = e, the type of e must be a subtype of the type of x. The type of the
expression is the same as the type of x. The assignment is executed by evaluating expression x and
then e, and storing e's value in variable x, after a widening conversion (section 11.12) if necessary.
When e is a compile-time constant of type byte, char, short, or int, and x has type byte, char, or
short, a narrowing conversion is done automatically, provided the value of e is within the range
representable in x (section 5.1). The value of the expression x = e is that of x after the assignment.
The assignment operator is right-associative, so the multiple assignment x = y = e has the same
meaning as x = (y = e), that is, evaluate the expression e, assign its value to y, and then to x.
When e has reference type (object type or array type), only a reference to the object or array is stored in
x. Thus the assignment x = e does not copy the object or array (example 41).
When x and e have the same type, the compound assignment x += e is equivalent to x = x + e;
however, x is evaluated only once, so in a[i++] += e the variable i is incremented only once. When
the type of x is t, different from the type of e, then x += e is equivalent to x = (t) (x + e), in
which the intermediate result (x + e) is converted to type t (section 11.12); again x is evaluated only
once. The other compound assignment operators -=, ``*=``, and so on, are similar.
Since assignment associates to the right, and the value of sum += e is that of sum after the
assignment, one can write ps[i] = sum += e to first increment sum by e and then store the result in
ps[i] (example 30).


Increment and decrement statements
==================================

.. index:: increment statement, decrement statement

The value of the postincrement expression x++ is that of x, and its effect is to increment x by 1; and
similarly for postdecrement x--. The value of the preincrement expression ++x is that of x+1, and its
effect is to increment x by 1; and similarly for predecrement --x.
