.. _enchancements:

************
Enhancements
************

.. index:: enhancements


Overview
=====================

.. index:: overview

An enhancement in Gosu defines non-virtual instance and static methods and properties to be added to another type.  An
enhancement is defined in a file ending in the ``.gsx`` suffix.

Enhancement Declarations
===================================

An *enhancement-declaration* with name ``E`` that is enhancing the type ``C`` has the form

.. productionlist:: enhancement
    gEnhancement = "enhancement" id typeVariableDefs ":" classOrInterfaceType {"[" "]"} enhancementBody .

A declaration of enhancement ``E`` on type ``C``` introduces a new reference type ``E``. The
*enhancement body* may contain declarations of methods, properties, nested classes, nested interfaces,
nested structures  and nested enums.  Each instance method and property defined in ``E`` will become available on
the type ``C`` as a non-virtual instance method.  Each static method and property defined in ``E`` will become available
for invocation statically on type ``C``.

All enhancements must be defined as **top-level**: they cannot be defined inside other type declarations.

Enhancement Feature Invocation
===================================

Given an instance method ``m()`` defined on enhancement ``E`` that enhances type ``C``, and an instance of ``C``
named ``o``, the expression ``o.m()`` is syntactic sugar for ``E.m(o)``.

Given a static method ``m()`` defined on enhancement ``E`` that enhances type ``C``, the expression
``C.m()`` is syntactic sugar for ``E.m()``.

Enhancement feature invocation is resolved **statically** at compile time.

Enhancement Bodies
===================================

Enhancement method and property bodies of enhancement ``E`` compile as if they belong to the enhanced type ``C``, with
a few restrictions.  The ``this`` keyword in non-static features is of type ``C``, rather than ``E``, but only
public and, if applicable, protected features are available in the enhancement.  Since enhancement features
are non-virtual, they cannot be used to implement explicit interfaces, nor can they be used to implement virtual
overloading between multiple classes.

The declarations in an enhancement may appear in  any order.

Enhancements and Generics
===================================

An enhancement declaration may take type parameters and enhance generic types.

Generic type variables are expressions in Gosu.  In enhancements, the value of a generic type variable will be the
**statically resolved**: the value will be the statically determined value of the type variable *at the call site of
the enhancement feature invocation*.

Enhancements may also be applied to parameterized types (e.g. ``List<String``)