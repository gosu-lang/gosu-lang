.. _types:

*****
Types
*****

.. index:: type

A *type* is a set of values and operations on them. A type is either a primitive
type or a reference type.


.. _primitiveTypes:

Primitive Types
===============

.. index:: primitive type

A *primitive type* is either ``boolean`` or one of the *numeric types* ``char``,
``byte``, ``short``, ``int``, ``long``, ``float``, and ``double``. The primitive
types, example literals (that is, constants), size in bits (where 8 bits equals 
1 byte), and value range, are shown in the following table:


.. |plusminus| unicode:: U+00B1 .. plus minus

   
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
| Type       | Kind           | Example Literals        | Size  | Range                                                                    | Wrapper  | 
+============+================+=========================+=======+==========================================================================+==========+
|``boolean`` | logical        | true, false             | 1     |                                                                          | Boolean  |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``char``    | integer        | ' ', '0', 'A', ...      | 16    |  \\u0000 ... \\uFFFF (unsigned)                                          | Character|
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``byte``    | integer        | 0, -1, 117, ...         | 8     |  max = 2\ :sup:`8-1`-1                                                   | Byte     |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``short``   | integer        | 0, -1, 117, ...         | 16    |  max = 2\ :sup:`16-1`-1                                                  | Short    |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``int``     | integer        | 0, -1, 117, ...         | 32    |  max = 2\ :sup:`32-1`-1                                                  | Integer  |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``long``    | integer        | 0L, -1L, 117L, ...      | 64    |  max = 2\ :sup:`64-1`-1                                                  | Long     |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``float``   | floating point | -1.0f, 0.49f, 3E8f, ... | 32    |  |plusminus| 10\ :sup:`-38`... |plusminus| 10\ :sup:`38`, sigdig 6-7     | Float    |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+
|``double``  | floating point | -1.0, 0.49, 3E8, ...    | 64    |  |plusminus| 10\ :sup:`-308`... |plusminus| 10\ :sup:`308`, sigdig 15-16 | Double   |
+------------+----------------+-------------------------+-------+--------------------------------------------------------------------------+----------+

The integer types are exact within their range. They use signed 2's complement 
representation (except for ``char``), so when the most positive number in a type
is *max*, then the most negative number is *-max-1*. The floating-point types 
are inexact and follow IEEE754, with the number of significant digits indicated 
by "sigdig." 

Integer literals (of type ``byte``, ``char``, ``short``, ``int``, or ``long``) 
may be written in three different bases:

===========    ====    ================  ===================== 
Notation       Base    Distinction       Example
===========    ====    ================  ===================== 
Decimal        10      No prefix         123, -127
Hexadecimal    16      Leading 0x or 0X  0x7b, -0X7F
Binary         2       Leading 0b or 0B  0b1111011, -0B1111111
===========    ====    ================  ===================== 

Number literals can have a suffix that specify the type of the constant as 
described by the following table. If a number literal has no suffix then its 
type can be inferred.

================   ================  =======
Suffix             Type              Example
================   ================  =======
``b`` or ``B``     ``byte``          18b 
``s`` or ``S``     ``short``         17s
``l`` or ``L``     ``long``          16L
``bi`` or ``BI``   ``BigInteger``    15bi
``f`` or ``F``     ``float``         1.5f 
``d`` or ``D``     ``double``        1.0d
``bd`` or ``BD``    ``BigDecimal``   1.0bd
================   ================  =======

The production rules defining a number literal are the followings:

.. productionlist:: number literal
    NumberLiteral : "NaN" | "Infinity" | `HexLiteral` | `BinLiteral` | `IntOrFloatPointLiteral` .
    BinLiteral : ("0b" | "0B") "0" | "1" {"0" | "1"} [`IntegerTypeSuffix`] .
    HexLiteral : ("0x" | "0X") `HexDigit` {`HexDigit`} ["s" | "S" | "l" | "L"] .
    IntOrFloatPointLiteral : "." `Digit` {`Digit`} [`FloatTypeSuffix`] |
                           : `Digit` {`Digit`} ["." {`Digit`} [`Exponent`] [`FloatTypeSuffix`] | `Exponent` [`FloatTypeSuffix`] | `FloatTypeSuffix` | `IntegerTypeSuffix` ] .
    HexDigit : `Digit` | "a".."f" | "A".."F" .
    IntegerTypeSuffix : ("l" | "L" | "s" | "S" | "bi" | "BI" | "b" | "B") .
    Exponent : ("e" | "E") ["+" | "-"] `Digit` {`Digit`} .
    FloatTypeSuffix : ("f" | "F" | "d" | "D" | "bd" | "BD") .

.. _referenceTypes:

Reference Types
===============

.. index:: reference type

A *reference type* is either a class type defined by a class declaration 
(see XXX), or an interface type defined by an interface declaration (see XXX), 
or an array type (see XXX), or an enum type (see XXX), or a block type 
(see XXX), or a structural type (see XXX). A value of reference type is either 
``null`` or a reference to an object or array or enum or block. The special 
value ``null`` denotes "no object." The literal ``null``, denoting the ``null``
value, can have any reference type.

Array Types
===========

.. index:: array type

An *array type* has the form ``t[]``, where ``t`` is any type. An array type 
``t[]`` is a reference type. Hence a value of array type ``t[]`` is either 
``null``, or is a reference to an array whose element type is precisely ``t``
(when ``t`` is a primitive type), or is a subtype of ``t`` (when ``t`` is a 
reference type).

Block Types
===========

.. index:: block type

A *block type* has the form ``block(`` T\ :sub:`param-1` ``, ...,`` T\ :sub:`param-n` ``):`` T\ :sub:`return` and
describes a Function-like thing, with parameters and a return type.  T\ :sub:`param-n` is the type of the
n-th parameter of the block type and T\ :sub:`return` is the return type of the block type.  Parameter and return types
may be of any type. Block types are reference types.

Block types do not support formal inheritance but do support assignability via coercion.  A block type
``block`` :sub:`1` ``(`` T\ :sub:`param-1-1` ``, ...,`` T\ :sub:`param-1-n` ``):`` T\ :sub:`return-1` is said to be *coercible* to
``block`` :sub:`2` ``(`` T\ :sub:`param-2-1` ``, ...,`` T\ :sub:`param-2-n` ``):`` T\ :sub:`return-2` if:

* Both blocks have the same number of parameter types (n)
* For each parameter type T\ :sub:`param-1-n`, the type T\ :sub:`param-2-n` is coercible to it (contravariance including coercion)
* type T\ :sub:`return-1` is coercible to type T\ :sub:`return-2` (covariance including coercion)


Subtypes and Compatibility
==========================

.. index:: subtype, compatibility, structural

A type ``t1`` may be a *subtype* of a type ``t2``, in which case ``t2`` is a 
*supertype* of ``t1``. Intuitively this means that any value ``v1`` of type 
``t1`` can be used where a value of type ``t2`` is expected. When ``t1`` and 
``t2`` are reference types, ``t1`` must provide at least the functionality 
(methods, fields and properties) provided by ``t2``. In particular, any value 
``v1`` of type ``t1`` may be bound to a variable or field or parameter ``x2`` 
of type ``t2``, e.g., by the assignment ``x2 = v1`` or by parameter passing. 
 
The following rules determine when a type ``t1`` is a subtype of a type ``t2``:

* Every type is a subtype of itself.
* If ``t1`` is a subtype of ``t2``, and ``t2`` is a subtype of ``t3,`` then 
  ``t1`` is a subtype of ``t3``.
* If ``t1`` and ``t2`` are primitive types, and there is a widening (W or L) 
  conversion from ``t1`` to ``t2`` according to the 
  :ref:`conversionsPrimitiveTypes`, then ``t1`` is a subtype of ``t2``.
* If ``t1`` and ``t2`` are classes, then ``t1`` is a subtype of ``t2`` if ``t1``
  is a subclass of ``t2``.
* If ``t1`` and ``t2`` are interfaces, then ``t1`` is a subtype of ``t2`` if 
  ``t1`` is a subinterface of ``t2``.
* If ``t1`` is a class and ``t2`` is an interface, then ``t1`` is a subtype of 
  ``t2`` provided that ``t1`` (is a subclass of a class that) implements ``t2``
  or implements a subinterface of ``t2``.
* Array type ``t1[]`` is a subtype of array type ``t2[]`` if reference type 
  ``t1`` is a subtype of reference type ``t2``.
* Any reference type ``t``, including any array type, is also a subtype of 
  predefined class ``Object``.

No primitive type is a subtype of a reference type and no reference type is a 
subtype of a primitive type. 
   
`t1`` is  *nominally compatible* with a type ``t2`` if ``t1`` is a subtype  of
``t2``.

The following rules determine when a type ``t1`` is  *structurally compatible* 
with a type ``t2``:

* If ``t1`` is nominally  compatible with ``t2``, then ``t1`` is structurally 
  compatible with ``t2``.
* If ``t1`` is a type and ``t2`` is a structure, then ``t1`` is structurally 
  compatible with ``t2`` if ``t1`` provides the same functionality 
  (methods, fields and properties) provided by ``t2``. 
* If ``t1`` and ``t2`` are structures, then ``t1`` is structurally compatible 
  with ``t2``  if ``t1`` extends ``t2``.
* If ``t1`` is a class and ``t2`` is a structure, then ``t1`` is 
  structurally compatible with ``t2`` provided that ``t1`` (is a subclass of a
  class that) implements ``t2`` or implements a (sub)interface that 
  extends ``t2``.
* If ``t1`` is a interface and ``t2`` is a structure, then ``t1`` is 
  structurally compatible with ``t2`` if ``t1`` extends ``t2`` or a
  (sub)interface that extends ``t2``.

  
The following rules determine when a reference type ``t1`` is *compatible*
with a reference type ``t2``:

* If ``t1`` is *nominally compatible* with ``t2``.
* If ``t1`` is *structurally compatible* with ``t2``.
* if ``t1`` or ``t2`` is a reference type, and there is a conversion from ``t1``
  to ``t2`` according to the  :ref:`conversionsReferenceTypes`

Type Conversion
===============

.. index:: conversion

For a given type ``ts`` there may exist an implicit or explicit standard 
*conversion* of a value of type ``ts`` into a value of another type ``tt``. If 
there is an *implicit conversion* from type ``ts`` to type ``tt``, then an 
expression of type ``ts`` can be used wherever an expression of type ``tt`` is 
expected. In particular, any value ``v`` of type ``ts`` may be bound to a 
variable or field or parameter ``x`` of type ``tt``, for instance, by the
assignment ``x = v``.

If there is an *explicit conversion* from ``ts`` to ``tt``, then a cast 
expression can be used (see XXX). 

.. _boxing:

Boxing: Wrapping Primitive Types As Reference Types
---------------------------------------------------

.. index:: boxing

For every primitive type there is a corresponding wrapper class, which is a 
reference type. The wrapper classes are listed in the previous 
:ref:`table<primitiveTypes>`. An object of a wrapper class contains a single
value of the corresponding primitive type.

A wrapper class must be used when a value of primitive type is passed to 
a method that expects a reference type, or stored in a variable or field of 
reference type. For instance, to store an ``int`` in a collection one must wrap 
it as an Integer object.

The conversion from primitive type to wrapper class is called *boxing*, and the 
opposite conversion is called *unboxing*. Boxing and unboxing are performed 
automatically when needed. Boxing and unboxing may also be performed explicitly
using operations such as ``new Integer(i)`` to box the integer ``i``, and 
``o.intValue()`` or ``o as int`` to unbox the Integer object ``o``. If ``o`` is
``null``, then unboxing of ``o`` will fail at run-time by throwing 
NullPointerException. Because of automatic unboxing, a Boolean value may be used
in conditional statements (``if``, ``while`` and ``do-while``) and in logical 
operators (such as ``!``, ``&&`` and so on); and Integer and other integer type
wrapper classes may be used in ``switch`` statements. 

A boxed value can be unboxed only to a value of the boxed type, or to a 
supertype. Thus an Integer object can be unboxed to an ``int`` or a ``long`` 
because ``long`` is a supertype of ``int``, but not to a ``char`` or ``byte`` or
``short``. The wrapper classes Byte, Short, Integer, Long, Float, and Double
have the common superclass Number. 

.. _conversionsReferenceTypes:

Conversion involving Reference Types
------------------------------------

If ``ts`` and ``tt`` are types, then a standard *implicit conversion* from 
``ts`` to ``tt`` exists in these cases:

* there is a boxing/unboxing conversion between ``ts`` and ``tt``.
* ``ts`` is a integer primitive type or Character and ``tt`` is BigInteger. 
* ``ts`` is a primitive type (but not ``boolean``) or Number or IDimension or 
  Character and ``tt`` is BigDecimal. 
* ``ts`` is a Java interface with one method ``m`` and ``tt`` is a block type 
  that is compatible with the formal parameters and return type of ``m``; and
  vice versa when ``ts`` is a block and ``tt`` is an interface.
* ``ts`` is a subtype of FeatureReference<R, T> and ``tt`` is the block type T.
* ``ts`` is a ``char`` or ``Character`` type and `tt`` is the String type.

If ``ts`` and ``tt`` are types, then a standard *explicit conversion* from 
``ts`` to ``tt`` exists in these cases:

* ``ts`` is any type and ``tt`` is the String type.
* ``ts`` is a reference type and ``tt`` is the IMonitorLock and the conversion 
  happens in the resource list of the ``using`` statement.
* ``ts`` is the Type<T> type and ``tt`` is the Class<T> type.
* ``ts`` is a primitive type (but not ``boolean``) or Number or IDimension or 
  Character and ``tt`` is BigInteger. 



.. _conversionsPrimitiveTypes:

Conversion between Primitive Types
----------------------------------

A *type conversion* between primitive types converts a value from one type to 
another. A *widening* conversion converts from a type to a supertype (or the 
type itself). A *narrowing* conversion converts from a type to another type. A 
narrowing conversion requires an explicit *type cast* (see XXX), except in an 
assignment ``x = e`` or initialization where ``e`` is a compile-time integer 
constant (see XXX). 

The legal type conversion between primitive types are shown in 
:ref:`conversionsPrimitiveTypes`. A type cast between primitive types never
fails at run-time.

In the following table the letter C marks a narrowing conversion that requires 
a type cast ``e as t`` (see XXX); W marks a widening conversion that preserves 
the value; and L marks a widening conversion that may cause a loss of precision.
A narrowing integer conversion discards those (most significant) bits that 
cannot be represented in the smaller integer type. Conversion from an integer 
type to a floating point type (``float`` or ``double``) produces a floating 
point approximation of the integer value. Conversion from a floating point type 
to an integer type discards the fractional part of the number; that is it rounds
toward zero. When converting a too-large floating point number to a ``long`` or
``int``, the result is the best approximation (that is, the type's largest 
positive or the largest negative representable number); conversion to ``byte``
or ``short`` or ``char`` is done by converting to ``int`` and then to the 
requested type.


+------------+-------------------------------------------------------------------------------------------------------+
|            |                                             To Type                                                   |
|            +------------+------------+------------+------------+------------+------------+------------+------------+
| From Type  |``boolean`` |``char``    | ``byte``   | ``short``  | ``int``    | ``long``   | ``float``  | ``double`` |
+============+============+============+============+============+============+============+============+============+
|``boolean`` | W          | C          | C          | C          | C          | C          | C          | C          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``char``    | C          | W          | C          | C          | W          | W          | W          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``byte``    | C          | C          | W          | W          | W          | W          | W          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``short``   | C          | C          | C          | W          | W          | W          | W          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``int``     | C          | C          | C          | C          | W          | W          | L          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``long``    | C          | C          | C          | C          | C          | W          | L          | L          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``float``   | C          | C          | C          | C          | C          | C          | W          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+
|``double``  | C          | C          | C          | C          | C          | C          | C          | W          |
+------------+------------+------------+------------+------------+------------+------------+------------+------------+

Signatures and Subsumption
==========================

.. index:: signature, subsumption

A *signature* has form m(t\ :sub:`1`, ..., t\ :sub:`n`), where *m* is a method 
or constructor or function name, and (t\ :sub:`1`, ..., t\ :sub:`n`) is a list 
of non-generic types; (See exampleXXX). We say that a signature sig\ :sub:`1` = 
m(t\ :sub:`1`, ..., t\ :sub:`n`) *subsumes* signature sig\ :sub:`2` =
m(u\ :sub:`1`, ..., u\ :sub:`n`) if each u\ :sub:`i` is a subtype of 
t\ :sub:`i` . We also say that sig\ :sub:`2` is *more specific* than 
sig\ :sub:`1`. Note that the method name *m* and the number *n* of types must be
the same in the two signatures. Since every type t\ :sub:`i` is a subtype of 
itself, every signature subsumes itself. In a collection of signatures there may
be one that is subsumed by all others; such a signature is called the 
*most specific* signature.

EBNF
====

TODO
