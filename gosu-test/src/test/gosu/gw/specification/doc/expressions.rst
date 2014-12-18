.. _expressions:

***********
Expressions
***********

.. index:: expressions

An expression is evaluated to obtain a value (such as ``117``). In addition, 
evaluation of an expression may change the computer's *state*: the values of 
variables, fields, and array elements, the contents of files, and so on. More 
precisely, evaluation of an expression either 

* terminates normally, producing a value; or 
* terminates abruptly by throwing an exception; or 
* does not terminate at all (for instance, because it calls a method that does 
  not terminate). 

Expressions are built from *literals* (anonymous constants), variables, fields, 
operators, method calls, array accesses, conditional expressions, the ``new`` 
operator, and so on; see the table of expression forms below. 

One must distinguish the *compile-time type of an expression* from the *run-time 
type of its value*. An expression has a compile-time type inferred by the 
compiler. When this is a reference type ``t``, and the run-time value of the 
expression is an object ``o``, then the run-time type of ``o`` is a class that 
is implicitly convertible to ``t`` but not necessarily equal to ``t``. For 
instance, the expression ``"foo" as Object`` has compile-time type Object, but 
its run-time value is an object of class String, a subclass of Object. 


Table of Expression Forms
=========================


The table of expression forms shows the form, meaning, associativity, argument
(operand) types, and result types for expressions. The expressions are grouped
according to precedence, as indicated by the horizontal rules, from high
precedence to low precedence. Higher-precedence forms are evaluated before
lower-precedence forms. Parentheses may be used to emphasize or force a
particular order of evaluation.

When an operator (such as ``+``) is left-associative, a sequence ``e1 + e2 +
e3`` of operators is evaluated as if parenthesized ``(e1 + e2) + e3``. When an
operator (such as ``=``) is right-associative, a sequence ``e1 = e2 = e3`` of
operators is evaluated as if parenthesized ``e1 = (e2 = e3)``.

In the argument type and result type columns of the table, *integer* stands for
any of ``char``, ``byte``, ``short``, ``int``, ``long`` or ``BigInteger`` (TODO
Add dimensiona and maybe more?), or their boxed forms Character, Byte, Short,
Integer or Long (section XXX); and *numeric* stands for integer or ``float`` or
``double`` or ``BigDecimal``, or their boxed forms Float or Double. The type
*boolean* stands for ``boolean`` or its boxed form Boolean.

For an operator with one integer or numeric operand, the *promotion type* is
``double`` if the operand has type ``double``; it is ``float`` if the operand
has type ``float``; it is ``long`` if the operand has type ``long``; otherwise
it is ``int`` (that is, if the operand has type ``byte``, ``char``, ``short``,
or ``int``). TODO add BigXXX to the mix and dimensions

For an operator with two integer or numeric operands (except the shift
operators; section XXX), the promotion type is ``double`` if any operand has
type ``double``; otherwise, it is ``float`` if any operand has type ``float``;
otherwise, it is ``long`` if any operand has type ``long``; otherwise it is
``int``.

Before the operation is performed, the operands are promoted, that is,
converted to the promotion type by a widening type conversion (section XXX).

If the result type is given as numeric also, it equals the promotion type. For
example, ``10 / 3`` has type ``int``, whereas ``10 / 3.0`` has type ``double``,
and ``c + 1 as byte`` has type ``int`` when ``c`` has type ``char``.




TODO TABLE

Arithmetic Operators
====================

.. index:: arithmetic operators

Integer division ``el/e2`` truncates, that is, rounds toward zero, so ``10/3`` 
is ``3``, and ``(-10)/3`` is -3. The integer remainder ``x%y`` equals 
``x-(x/y)*y`` when ``y`` is non-zero; it has the same sign as ``x``. Integer 
division or remainder by zero throws the exception ArithmeticException. Integer 
overflow does not throw an exception but wraps around. That is, the result is 
truncated by discarding any higher-order bits that do not fit in the type. Thus,
in the ``int`` type, the expression ``2147483647+1`` evaluates to -2147483648, 
and the expression ``-2147483648-1`` evaluates to 2147483647. 

The floating-point remainder ``x%y`` roughly equals ``x-((x/y) as int)*y`` when 
``y`` is non-zero. Floating-point division by zero and floating-point overflow 
do not throw exceptions but produce special IEEE754 values (of type ``float`` or 
``double``) such as ``Infinity`` or ``NaN`` ("not a number"). 

Logical Operators
=================

.. index:: logical operators

.. |lessthanorequal| unicode:: U+2264 .. less than or equal

The operators ``==`` and ``!=`` require the operand types to be compatible: one 
must be implicitly convertible to the other, possibly after an unboxing 
operation. 

Two values ``v0`` and ``v1`` are equal (by ``==``) if  

* ``v0 === v1``.
* ``v0`` and ``v1`` have both primitive types and the same value after 
  conversion to their common supertype. For instance, 10 and 10.0 are equal.
* ``v0`` and ``v1`` have the same reference type ``t`` and ``t`` is a subtype of
  Number or IDimension, and ``t`` is a subtype of Comparable and 
  ``v0.compareTo(v1) == 0``.
* ``v0`` and ``v1`` have the same reference type ``t`` and 
  ``v0.equals(v1) == true``
  
* ``v0`` has a String type and ``v0.equals(v1.toString()) == true``.
* ``v0`` has array type ``t0``, ``v1`` has array type ``t1`` *and* 

  * ``t0`` and ``t1`` are the same primitive type or have a subtype 
    relationship *and*
  * ``v0[i].equals(v1[i]) == true`` holds for every *i* in 0 |lessthanorequal| 
    *i* < *L* . 

Two values of reference type are identical (by ``===``) if both are ``null``, or 
both are references to the same object or array, created by the same boxing 
operation or execution of the ``new``-operator. 

The logical operators ``&&`` and ``||`` perform *shortcut evaluation*: if ``e1`` 
evaluates to ``true`` in ``el && e2``, then ``e2`` is evaluated to obtain the 
value of the expression; otherwise ``e2`` is ignored and the value of the 
expression is ``false``. Conversely, if ``e1`` evaluates to ``false`` in ``e1 || 
e2``, then ``e2`` is evaluated to obtain the value of the expression; otherwise 
``e2`` is ignored and the value of the expression is ``true``. 

The logical negation operator ``!e`` evaluates its argument to true or false and
returns false or true.

Bitwise Operators and Shift Operators 
===================================== 

.. index:: bitwise operators, shift operators 

The operators ``~`` (bitwise complement, or one's complement) and ``&`` (bitwise 
and) and ``^`` (bitwise exclusive-or) and ``|`` (bitwise or) may be used on 
operands of integer type. The operators work in parallel on all bits of the 2's 
complement representation of the operands. Thus ``~n`` equals ``(-n) - 1``. 

The ``<<`` and ``>>`` and ``>>>`` shift the bits of the 2's complement 
representation of the first argument. The two operands are promoted (section 
XXX) separately, and the result type is the promotion type (``int`` or ``long``) 
of the first argument. Thus the shift operation is always performed on a 32-bit 
(``int``) or a 64-bit (``long``) value. In the former case, the length of the 
shift is between 0 and 31 as determined by the five least significant bits of 
the second argument; in the latter case, it is between 0 and 63 as determined by 
the six least significant bits of the second argument. The left shift ``n<<s`` 
equals ``n*2*2*`` ... ``*2`` where there are ``s`` multiplications. The signed 
right shift ``n >> s`` of a non-negative ``n`` equals ``n/2/2/``...``/2`` where 
there are ``s`` divisions; the signed right shift of a negative ``n`` equals 
``~((~n)>>s)``. The unsigned right shift ``n>>>s`` of a non-negative ``n`` 
equals ``n>>s``; the signed right shift of a negative n equals ``(n>>s) + 
(2<<~s)`` if n has type ``int``, and ``(n>>s) + (2L<<~s)`` if it has type 
``long``, where ``2L`` is the ``long`` constant with value 2. 


Conditional Expressions
=======================

.. index:: conditional expression

The *conditional expression* ``e1 ? e2 : e3`` is legal if ``e1`` has type 
``boolean`` or Boolean. The type of the conditional expression is the least 
common super-type of ``e2`` and ``e3`` possibly after boxing operations. In
particular if the type of ``e2`` and ``e3`` is primitive or boxed and a widening
conversion is possible, the resulting type will be the widened primitive type.

If ``e2`` is a string literal and is convertible to the type of ``e3`` and
``e3`` is not a literal expression then the type of the conditional expression
is the type of ``e3``; likewise if ``e3`` is a string literal.

The conditional expression is evaluated by first evaluating ``e1``. If ``e1``
evaluates to ``true``, then ``e2`` is evaluated; otherwise ``e3`` is evaluated. 
The resulting value is the value of the conditional expression.

The *conditional expression* ``e1 :? e2`` is shorthand notation for
``e1 != null ? e1 : e2`` where the type of ``e2`` is a reference type. 


Object Creation Expressions
===========================

.. index:: object creation expression

The *object creation expression*

``new`` *[* ``C`` *]* ``(`` *actual-list* ``)``

creates a new object of class ``C`` and then calls that constructor in class 
``C`` whose signature matches the arguments in *actual-list*. The type ``C`` may 
be omitted and the type of the expression will be inferred. 

The *actual-list* is evaluated from left to right to obtain a list of argument 
values. These argument values are bound to the constructor's parameters, an 
object of the class is created in the memory, the non-static fields are given 
default initial values according to their type, a superclass constructor is 
called explicitly or implicitly, all non-static field initializers are executed 
in order of appearance, and finally the constructor body is executed to 
initialize the object. The value of the constructor call expression is the newly 
created object, whose class is ``C``. 

Object Initializers
===================

.. index:: object initializers

An object initializer immediately follows a constructor call to initialize the 
fields or properties of the newly created object. Thus its purpose is similar to 
that of an array initialization (section XXX). If the constructor takes no 
arguments, the complete syntax, including the constructor call, is this: 

``new`` ``C`` ``{`` ``:``\ x1 ``=`` e1\ ``,`` ...\ ``,`` ``:``\ xn ``=`` en\ ``}``     

If the constructor takes arguments, the complete syntax is this:

``new`` ``C(`` *actual-list* ``)`` ``{`` ``:``\ x1 ``=`` e1\ ``,`` ...\ ``,`` ``:``\ xn ``=`` en\ ``}``  

In either case, the *xi* must name visible fields or properties of the object, 
and each *ei* must be an expression. The *ei* cannot refer to the newly created 
object. At run-time the constructor is called, each expression *ei* is evaluated 
from left to right, and its value is assigned to the corresponding filed or 
property *xi*. 

Collection And Map Initializers
===============================

.. index:: collection initializers, map initializers

A *collection initializer* immediately follow a constructor call and adds items 
(en) to the new collection. The syntax is:

[``new`` ``C(`` [*actual-list*] ``)`` ] ``{`` e1\ ``,`` ...\ ``,`` en\ ``}``

The type ``C`` must be compatible with Collection.  The new expression ``new`` ``C(`` [*actual-list*] ``)``
may be omitted and type type will be inferred.

A *map initializer* immediately follow a constructor call and puts key-value 
pairs (kn->en) to the new map. The syntax is:

[``new`` ``C(`` [*actual-list*] ``)`` ] ``{`` k1 ``->`` e1\ ``,`` ...\ ``,`` kn ``->`` en\ ``}``

The type ``C`` must be compatible with Map. The new expression ``new`` ``C(`` [*actual-list*] ``)``
may be omitted and type type will be inferred.


Type Expressions 
================

.. index:: type expression, typeis, typeof

The type test ``e typeis t`` is evaluated by evaluating ``e`` to a value ``v``.
If ``v`` is not ``null`` and ``a`` is a reference to an object of class ``C``, 
where ``C`` is nominally or structurally compatible with ``t``, the result is 
``true``; otherwise ``false``.

The expression ``typeof e`` is evaluated by evaluating ``e`` and returning its
run-time type.


Field Access Expressions
========================

.. index:: field access expression, property access expression

A *field* access must have one of these three forms:

- ``f``
- ``C.f``
- ``o.f``

where ``C`` is a class and ``o`` an expression of reference type.

A field access ``f`` must refer to a static or non-static field declared in or
inherited by a class whose declaration encloses the field access expression.
The class declaring the field is the target class ``TC``.

A field access ``C.f`` must refer to a static field in class ``C`` or a
superclass of ``C``. That class is the target class ``TC``.

A field access ``o.f``, where expression ``o`` has type ``C``, must refer to a
static or non-static field in class ``C`` or a superclass of ``C``. That class
is the target class ``TC``. To evaluate the field access, the expression ``o``
is evaluated to obtain an object. If the field is static, the object is ignored
and the value of ``o.f`` is the ``TC``-field ``f``. If the field is non-static,
the value of ``o`` must be non-``null`` and the value of ``o.f`` is the value
of the ``TC``-field ``f`` in object ``o``. It is informative to contrast a
non-static field access and a non-static method call (section XXX):

* In a non-static field access ``o.f``, the field referred to is determined by
  the compile-time *type* of the object expression ``o``.
* In a non-static call to a non-private method ``o.m(``...\ ``)``, the method
  called is determined by the run-time *class* of the target object: the object
  to which ``o`` evaluates.


The Current Object Reference ``this``
=====================================

The name ``this`` may be used in non-static code to refer to the current object
(section XXX). When non-static code in a given object is executed, the object
reference ``this`` refers to the object as a whole. Hence, when ``f`` is a
field and ``m`` is a method (declared in the innermost enclosing class), then
``this.f`` means the same as ``f``, and ``this.m(``...\ ``)`` means the same as
``m(``...\ ``)``.


Property Access Expressions
===========================

A property get-access must have one of these three forms:

- ``P``
- ``C.P``
- ``o.P``

where ``C`` is a class and ``o`` an expression of reference type. In the first
case, ``P`` must be a static or instance property declared in an enclosing
class. In the second case, ``P`` must be a static property declared in class
``C``. In the third case, ``P`` must be an instance property declared in the
type of ``o``, where ``o`` is a value. Property declarations are described in
section XXX.

In each case, the type of the property get-access expression is the declared
type of the property ``P``. A property get-access is evaluated by evaluating
``o``, if present, and then executing the body of the get-accessor. The value
of the expression is the value returned by the ``return``-statement that
terminates the execution of the get-accessor’s body. Such a
``return``-statement will eventually be executed, provided the get-accessor
terminates normally; see section XXX. If ``o`` is present but evaluates to
``null``, NullPointerException is thrown.

A *property set-access* must have one of these three forms:

- ``P =`` *expression*
- ``C.P =`` *expression*
- ``o.P =`` *expression*

where ``C`` is a class and ``o`` an expression of reference type. Each case
must satisfy the same requirements as for get-access above. In each case, the
type of the entire expression is the declared type of the property ``P``. The
type of the right-hand side *expression* must be implicitly convertible to the
declared type of the property. A property set-access is evaluated by evaluating
``o``, if present, and then evaluating *expression* to a value which is
implicitly converted to obtain a value ``v`` of the declared type of ``P``.
Then parameter ``value`` is bound to ``v`` and the body of the set-accessor is
executed. If ``o`` is present but evaluates to ``null``, NullPointerException
is thrown. The value of the property set-access expression is the value passed
to the set-accessor of ``P``.

A read-write property ``P`` may be used in a compound assignment such as ``o.P
*= 2`` or with increment and decrement operators as in ``o.P++``. First the
get-accessor is called to get the value of ``P``, and then the set-accessor is
called to set it. The expression ``o`` is evaluated only once.

PROPERTY ACCESS
?.
\*.
super.a property


Method Call Expressions
=======================

.. index:: method call expression

A method call expression, or method invocation, must have one of these five forms:

  ``m(`` *actual-list* ``)``
  ``super.m(`` *actual-list* ``)``
  ``C.m(`` *actual-list* ``)``
  ``C.super.m (`` *actual-list* ``)``
  ``o.m(`` *actual-list* ``)``

where ``m`` is a method name, ``C`` is a class name, and ``o`` is an expression of reference type. The *actual-list*
is a possibly empty comma-separated list of expressions, called the *arguments* or *actual parameters*.
The *call signature* is csig = m(t\ :sub:`1`, ..., t\ :sub:`n`), where (t\ :sub:`1`, ..., t\ :sub:`n`) is the list of types
of the *n* arguments in the *actual-list*.

Determining what method is actually called by a method call is complicated because (1) method names
may be overloaded, each version of the method having a distinct signature; (2) methods may be
overridden, that is, reimplemented in subclasses; (3) methods that are both non-static and nonprivate
are called by dynamic dispatch, given a target object; and (4) a method call in a nested class may call a
method declared in some enclosing class.
Section 11.11.1 describes argument evaluation and parameter passing, assuming the simple case
where it is clear which method ``m`` is being called. Section 11.11.2 describes how to determine which
method is being called in the general case.

Method Call: Parameter Passing
------------------------------

.. index:: parameter passing

This section considers the evaluation of a method call ``m(`` *actual-list* ``)`` when it is clear which method ``m`` is
called, and focuses on the parameter passing mechanism.
The call is evaluated by evaluating the expressions in the *actual-list* from left to right to obtain the
argument values. These argument values are then bound to the corresponding parameters in the
method's *formal-list*, in order of appearance. A widening conversion (section 11.12) occurs if the type of
an argument expression is a subtype of the method's corresponding parameter type.

Java uses *call-by-value* to bind argument values to formal parameters, so the formal parameter holds a
copy of the argument value. Thus if the method changes the value of a formal parameter, this change
does not affect the argument. For an argument of reference type, the parameter holds a copy of the
object reference or array reference, and hence the parameter refers to the same object or array as the
actual argument expression. Thus if the method changes that object or array, the changes will be visible
after the method returns (example 49).
A non-static method must be called with a target object, for example as ``o.m(`` *actual-list* ``)``, where the
target object is the value of ``o``, or as ``m(`` *actual-list* ``)``, where the target object is the current object reference
``thi``s. In either case, during execution of the method body, ``this`` will be bound to the target object.
A static method is not called with a target object, and it is illegal to use the identifier ``this`` inside the
body of a static method.
When the argument values have been bound to the formal parameters, the method body is executed.
The value of the method call expression is the value returned by the method if its return type is non-
``void``; otherwise the method call expression has no value. When the method returns, all parameters
and local variables in the method are discarded.

Method Call: Determining Which Method Is Called
-----------------------------------------------

In general, methods may be overloaded as well as overridden. The overloading is resolved at compile-
time by finding the most specific applicable and accessible method signature for the call. Overriding (for
non-static methods) is handled at run-time by searching the class hierarchy upwards starting with the
class of the object on which the method is called.

At Compile-Time: Determine the Target Type and Signature
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Find the target type ``TC``. If the method call has the form ``m(`` *actual-list* ``)``, the target type ``TC`` is the innermost
enclosing class containing a method called ``m`` that is visible (not shadowed by a method ``m``, regardless of
signature, in an intervening class). If the method call has the form ``super.m(`` *actual-list* ``)``, the target type
``TC`` is the superclass of the innermost enclosing class. If the method call has the form
``C.super.m(`` *actual-list* ``)``, the target type ``TC`` is the superclass of the enclosing class ``C``. If the method call
has the form C.m(actual-list), then ``TC`` is ``C``. If the method call has the form ``o.m(`` *actual-list* ``)``, then ``TC`` is the
type of the expression ``o``.
Find the target signature tsig. A method in class ``TC`` is applicable if its signature subsumes the call
signature csig (section 5.5). Whether a method is accessible is determined by its access modifiers
(section 9.7). Consider the collection of methods in ``TC`` that are both applicable and accessible. The call
is illegal (method unknown) if there is no such method. The call is illegal (ambiguous) if there is more
than one method whose extended signature m(T, u\ :sub:`1`, ..., u\ :sub:`n`) is most specific, that is, one whose extended
signature is subsumed by all the others. Thus if the call is legal, there is exactly one most specific
extended signature; from that we obtain the target signature tsig = m(u\ :sub:`1`, ..., u\ :sub:`n`) .
Determine whether the called method is static. If the method call has the form ``C.m(`` *actual-list* ``)``, the
called method must be static. If the method call has the form ``m(`` *actual-list* ``)`` or ``o.m(`` *actual-list* ``)`` or
``super.m(`` *actual-list* ``)`` or ``C.super.m(`` *actual-list* ``)``, we use the target type ``TC`` and the signature tsig to
determine whether the called method is static or non-static.

At Run-Time: Determine the Target Object (If Nonstatic) and Execute the Method
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the method is *static*, no target object is needed: the method to call is the method with signature tsig in
class ``TC``. However, when ``m`` is static in a method call ``o.m(`` *actual-list* ``)``, the expression ``o`` must be
evaluated anyway, but its value is ignored.
If the method is *non-static*, determine the target object; it will be bound to the object reference ``this``
during execution of the method. In the case of ``m(`` *actual-list* ``)``, the target object is ``this`` (if ``TC`` is the
innermost class enclosing the method call), or ``TC.this`` (if ``TC`` is an outer class containing the method
call). In the case of ``super.m(`` *actual-list* ``)``, the target object is this. In the case of ``C.super.m(`` *actual-list* ``)``,
the target object is ``C.this``. In the case ``o.m(`` *actual-list* ``)``, the expression ``o`` must evaluate to an
object reference. If non-``null``, that object is the target object; otherwise the exception
NullPointerException is thrown. If the method is nonprivate, the class hierarchy is searched to determine
which method to call, starting with the class ``RTC`` of the target object. If a method with signature tsig is
not found in class ``RTC``, then the immediate superclass of ``RTC`` is searched, and so on. This search
procedure is called *dynamic dispatch*. If the method is private, it must be in the target class ``TC`` and no
search is needed.
When the method has been determined, arguments are evaluated and bound as described in section
11.11.1.

Named Arguments in Method Calls
-------------------------------

.. index:: named arguments

The parameter name corresponding to an argument expression in the *actual-list* of a method call (see XXX)
may be explicitly specified using a *named argument*, which has this form:

  ``:``\ *parametername* ``=`` *expression*
 
This means that the argument expressions may be given in any order, not just the order
in which they appear in the method's formal parameter list. Also, named arguments considerably improve code clarity when multiple arguments have the same type and there is no (universally agreed) natural order of the arguments.

The arguments expressions are evaluated from left to right. Named arguments expressions must appear after all unnamed arguments expressions.

In an instance method call ``o.M(`` *actual-list* ``)``, arguments names used in the *actual-list* are resolved against the parameter names given in the corresponding method ``M`` in the compile-time type of ``o``. That is, the parameter names in overriding methods ``M`` in subtypes of the compile-time type of ``o`` are ignored.


Type Cast Expressions and Type Conversion
=========================================

.. index:: type cast, type conversion

A *type conversion* converts a value from one type to another. A *widening* conversion converts from a
type to a supertype. A *narrowing* conversion converts from a type to another type. This requires an
explicit *type cast* (except in an assignment ``x = e`` or initialization where ``e`` is a compile-time integer
constant; see section 11.5).

Type Cast Between Primitive Types
---------------------------------

When ``e`` is an expression of primitive type and ``t`` is a primitive type, then a type cast of ``e`` to ``t`` is done
using the expression ``e as t``.

This expression, when legal, has type ``t``. The legal type casts between primitive types are shown in the
following table, where C marks a narrowing conversion that requires a type cast ``e as t``, W marks a
widening conversion that preserves the value, and L marks a widening conversion that may cause a
loss of precision.


TODO TABLE

A *narrowing* integer conversion discards those (most significant) bits that cannot be represented in the
smaller integer type. Conversion from an integer type to a floating-point type (``float`` or ``double``)
produces a floatingpoint approximation of the integer value. Conversion from a floating-point type to an
integer type discards the fractional part of the number; that is, it rounds toward zero. When converting a
too-large floating-point number to a ``long`` or ``int``, the result is the best approximation (that is, the type's
largest positive or the largest negative representable number); conversion to ``byte`` or ``short`` or ``char`` is
done by converting to ``int`` and then to the requested type. The primitive type ``boolean`` cannot be cast
to any other type. A type cast between primitive types never fails at run-time.


typeas 
Type Cast Between Reference Types
---------------------------------

When ``e`` is an expression of reference type and ``t`` is a reference type (class or interface or array type), a
type cast of ``e`` to ``t`` is done using the expression ``e as t``.

This expression has type ``t``. It is evaluated by evaluating ``e`` to a value ``v``. If ``v`` is ``null`` or is a reference to
an object or array whose class is a subtype of ``t``, then the type cast succeeds with result ``v``; otherwise
the exception ClassCastException is thrown. The type cast is illegal when it cannot possibly succeed at
run-time, for instance, when ``e`` has type ``Double`` and ``t`` is ``Boolean``: none of these classes is a subtype of
the other.
