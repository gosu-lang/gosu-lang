.. _dynamic:

************
Type Dynamic
************

.. index:: dynamic

If an expression has type ``dynamic.Dynamic``, then a method call, operator
application, delegate call, or field access involving that expression will be
resolved dynamically, at run-time, rather than at compile-time. This means that
almost any such expression will be accepted by the compiler but may fail with an
exception at run-time. An expression ``e`` of compile-time ``Dynamic``
will at run-time have some value ad it has a
*run-time type*, obtainable as ``typeof e``. The run-time type will never be
type ``Dynamic`` itself (but possibly ``Object``), and this type will be
used to resolve method calls, delegate calls, and operator applications.

If the receiver ``o`` or any argument expression ``e`` of a method call ``o.M(
... e .. )`` has type ``Dynamic``, then the resolution of method call target and the
resolution of method overloading is performed at run-time, not compile-time. If
no suitable method exists at run-time, then an exception will be thrown.

Likewise, if any operand of an operator has type ``Dynamic``, then the
operator resolution is performed at run-time. Thus in expressions such as ``e+o`` or ``o+e``, where ``o`` has
type ``Dynamic``, the relevant implementation of operator ``+`` will be
chosen at run-time. If no such operator exists, then an exception will be
thrown.

The expression ``e typeis t`` has type ``bool`` even when ``e`` has type
``Dynamic``.

An assignment ``x = e``, where ``e`` has type ``Dynamic`` and ``x`` does not, is evaluated
by evaluating ``e`` and then attempting an implicit conversion from the run-time
value to the type of ``x``. If this succeeds, the result is assigned to ``x``;
otherwise an exception is thrown.

Type ``Dynamic`` can be used as the declared type of method parameters and return
types, fields and local variables; it can be used as element type in arrays and as
type argument in generic types; and it can be used in casts.

Type ``Dynamic`` cannot be used as a base type for classes. Moreover, a class cannot implement an interface that has type argument
``Dynamic``, but it may well have a base class that has type argument ``Dynamic``.

Type ``Dynamic`` is indistinguishable from type ``Object`` in method signature
matching.