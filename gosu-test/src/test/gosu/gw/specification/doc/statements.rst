.. _statements:

***********
Statements
***********

.. index:: statements


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