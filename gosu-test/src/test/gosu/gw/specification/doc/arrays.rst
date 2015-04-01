.. _arrays:

******
Arrays
******

.. index:: array

.. |greaterthanorequal| unicode:: U+2265 .. greater than or equal

An *array* is an indexed collection of variables, called *elements*. An
array has a given *length* *l* |greaterthanorequal| 0 and a given *element type*
``t``. The elements are indexed by the integers 0, 1,...,\ *l*-1. The value
of an expression of array type ``u[]`` is either ``null`` or a reference to
an array whose element type ``t`` is a subtype of ``u``. If ``u`` is a
primitive type, then ``t`` must equal ``u``.


.. _arrayCreationAndAccess:

Array Creation and Access
=========================

.. index:: array creation

A new array of length *l* with element type ``t`` is created (allocated) using
an *array creation expression*:

``new t[l]``

where *l* is an expression of type ``int``. If type ``t`` is a primitive
type, all elements of the new array are initialized to 0 (when ``t`` is
``byte``, ``char``, ``short``, ``int``, or ``long``) or 0.0 (when ``t`` is
``float`` or ``double``) or ``false`` (when ``t`` is ``boolean``). If ``t``
is a reference type, all elements are initialized to ``null``. If *l* is
negative, then the exception NegativeArraySizeException is thrown. Let ``a`` be
a reference of array type ``u[]``, to an array with length *l* and element
type ``t``. Then

* ``a.length`` of type ``int`` is the length  *l* of ``a``, that is, the number
  of elements in ``a``.
* The *array access* expression ``a[i]`` denotes element number ``i`` of ``a``,
  counting from 0; this expression has type ``u``. The integer expression ``i``
  is called the *array index*. If the value of ``i`` is less than 0 or greater
  than or equal to ``a.length``, then exception ArrayIndexOutOfBoundsException
  is thrown.
* When ``t`` is a reference type, every array element assignment ``a[i] = e``
  checks that the value of ``e`` is ``null`` or a reference to an object whose
  class ``C`` is a subtype of the element type ``t``. If this is not the case,
  then the exception ArrayStoreException is thrown. This check is made before
  every array element assignment at run-time, but only for reference types.
  
  
Array Initializers
==================

.. index:: array initializer

A variable or field of array type may be initialized at declaration, using 
an existing array or an *array initializer* for the initial value. An array 
initializer is a comma-separated list of zero or more expressions enclosed 
in braces ``{`` ... ``}``: 

``var x : t[] = {`` expression, ..., expression ``}``

The type of each *expression* must be a subtype of ``t``. Evaluation of the 
initializer causes a distinct new array, whose length equals the number of 
expressions, to be allocated. Then the expressions are evaluated from left 
to right and their values are stored in the array, and finally the array is 
bound to ``x``. Hence ``x`` cannot occur in the *expressions*: it has not 
been initialized when they are evaluated.

Array initializers may also be used in connection with array creation 
expressions:

``new t[] {`` expression, ..., expression ``}``

Multidimensional arrays can have nested initializers. Note that there are no 
array constants: a new distinct array is created every time an array initializer
is evaluated.  


Multidimensional Arrays
=======================

.. index:: array multidimensional

.. |cross| unicode:: U+00D7 .. cross

The types of multidimensional arrays are written ``t[][]``, ``t[][][]``, and 
so on. A rectangular *n*-dimensional array of size *l*\ :sub:`1` |cross| 
*l*\ :sub:`2` |cross| ... |cross| *l*\ :sub:`n` is created (allocated) using 
the array creation expression 
 
``new t[``\ *l*\ :sub:`1`\ ``]``\ ``[``\ *l*\ :sub:`2`\ ``]``...\ ``[``\ *l*\ :sub:`n`\ ``]``

A multidimensional array a of type ``t[][]`` is in fact a one-dimensional 
array of arrays; its component arrays have type ``t[]``. Hence a 
multidimensional array need not be rectangular, and one need not create all 
the dimensions at once. To create only the first *k* dimensions of size *l*\ 
:sub:`1` |cross| *l*\ :sub:`2` |cross| ... |cross| *l*\ :sub:`k` of an 
*n*-dimensional array, leave the *(n - k)* last brackets empty: 

``new t[``\ *l*\ :sub:`1`\ ``]``\ ``[``\ *l*\ :sub:`2`\ ``]``...\ ``[``\ *l*\ :sub:`k`\ ``]``\ ``[]``...\ ``[]``

To access an element of an *n*-dimensional array ``a``, use *n* index 
expressions: ``a[``\ *i*\ :sub:`1`\ ``]``\ ``[``\ *i*\ :sub:`2`\ ``]``...
\ ``[``\ *i*\ :sub:`n`\ ``]``.
