.. _structures:

**********
Structures
**********

.. index:: structures

Structure Declarations
======================

A *structure* is a special kind of *interface* type. Like an interface,
*structure-declaration* may contain field descriptions, method descriptions,
method-declarations, class declarations, interface declarations, and structure
declarations, in any order.


    *structure-modifiers* ``structure`` ``S`` *extends-clause* ``{``

        *field-descriptions*

        *method-descriptions*

        *method-declarations*

        *class-declarations*

        *interface-declarations*

        *structure-declarations*

    ``}``



A *structure* follows the same rules of an *interface* but has some additional
properties.

A class ``C`` may be declared to *nominally* implement one or more structures by
an *implements-clause*: ``implements`` ``S1, S2, ...``; this is analogous to
interfaces. Also, a class ``C`` can *structurally* implement one or more
structures by omitting the *implements-clause* and declaring all the non default
methods described by ``S1, S2, ...`` with *contravariant* argument types and
*covariant* return types.

It follows that any class *structurally* implements the empty structure:
``structure {}``

A structure ``S`` can extend one or more structures or interfaces by an
*extends-clause* : ``extends`` ``S1, I2, ...``. This indicates that ``S`` is
a flat collections of all the methods from ``S``, ``S1``, ``I2``, ``...`` .

An interface ``I`` can extend one or more structures or interfaces by an
*extends-clause* : ``extends`` ``S1, I2, ...``. In this case all the structures
in the *extends-clause* are treated as regular interfaces maintaining
*nominal compatibility*.

Given an expression ``e`` of type ``T`` and structure ``s`` of type ``S``, ``e``
is assignable to ``s`` if:

* ``e`` is ``null``.
* ``T`` is *structurally compatible* to ``S`` (see "Type Conversion").

Any structure can be assigned to a variable of type ``Object``.
