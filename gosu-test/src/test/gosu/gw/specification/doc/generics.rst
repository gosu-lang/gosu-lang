.. _generics:

*************************
Generic Types and Methods
*************************

.. index:: generics

Generic types and methods provide a way to strengthen type checking at
compile-time while at the same time making programs more expressive, reusable
and readable. The ability to have generic types and method is also known as
parametric polymorphism.

Generic Types, Type Parameters, and Type Instances
==================================================

.. index:: generic type, type parameter, type instance

A *generic class* declaration ``class C<T1, ... , Tn> { ... }`` has one or more
*type parameters* ``T1, ... , Tn``. The body of the declaration is an ordinary
class body in which the type parameters ``Ti`` can be used almost as if they
were ordinary types; see section XXX. A generic class is also called
parametrized class.

A generic class ``C<T1>`` is not itself a class. Rather, it is a mechanism or
template from which classes such as C<Integer> or C<String> or even
C<C<String>>, and so on, can be generated, by replacing the type parameter
``T1`` by a type expression ``t1``, such as Integer or String or C<String>. The
resulting classes are called *type instances*. The type ``t1`` used to replace
the type parameter ``T1`` can be any reference type expression: a class, an
array type, an interface, ..., or it can itself be a type instance. However, it
cannot be a primitive type such as ``int``, nor the pseudo-type ``void``.

Generic interfaces (and structures) can be declared also, and type instances
can be created from them. Again, a generic interface (or structure) is not an
interface (or structure), but a type instance of a generic interface (or
structure) is an interface (or structure).

Generic methods can be declared by specifying type parameters on the method
declaration in addition to any type parameters specified on the enclosing class
or interface or structure type.

Generic Classes
===============

.. index:: generic class

A declaration of a *generic class* ``C<T1, ... , Tn>`` may have this form:

    *class-modifiers* ``class C<T1, ... , Tn>`` *class-base-clause* *class-body*

The ``T1, ... , Tn`` are *type parameters*. The *class-modifiers*, *class-body* 
and *class-base-clause* are as for a non-generic class declaration (section 
XXX). 

In addition, each type parameter ``Ti`` may have constraints ``c``\ :sub:`1`, 
``c``\ :sub:`2`, ..., ``c``\ :sub:`n` in which case its entry in the parameter 
list is written ``Ti extends`` ``c``\ :sub:`1` ``&`` ``c``\ :sub:`2` ``&`` ... 
``&`` ``c``\ :sub:`n` instead of just ``Ti``; see XXX. 

The type parameters ``T1, ... , Tn`` may be used wherever a type is expected in 
the *class-base-clause* and in non-static members of the *class-body*, and so 
may the type parameters of any enclosing generic class, if the present class is 
a non-static member class. See XXX for details. 

A generic class ``C<T1, ... , Tn>`` in itself is not a class. However, each 
*type instance* ``C<t1, ... , tn>`` is a class, just like a class declared by 
replacing each type parameter ``Ti`` with the corresponding type ``ti`` in the 
*class-body*. A type ``ti`` that is substituted for a type parameter ``Ti`` in a 
type instance can be any reference type or it can itself be a type instance. 
However, it cannot be a primitive type nor the pseudo-type ``void``. 

All type instances of a generic class ``C<T1, ... , Tn>`` are represented by the 
same *raw type* ``C`` at run-time. All type instances of a generic class ``C<T1, 
... , Tn>`` share the same static fields (if any) declared in the *class-body*. 
As a consequence, the type parameters of the class cannot be used in any static 
members. 

A type instance ``C<t1, ... , tn>`` is accessible when all its parts are 
accessible. Thus if the generic class ``C<T1, ... , Tn>`` or any of the type 
arguments ``t1, ... , tn`` is private, then the type instance is private also. 

A scope can have only one class, generic or not, with the same name C, 
regardless of its number of type parameters. 

A generic class declaration is illegal if there are types ``t1, ... , tn`` such 
that the type instance ``C<t1, ... , tn>`` would contain two or more method 
declarations with the same signature. 

The usual conversion rules hold for generic classes and generic interfaces. When 
generic class ``C<T1>`` is declared to be a subclass of generic class ``B<T1>`` 
or is declared to implement interface ``I<T1>``, then the type instance 
``C<t1>`` is a subtype of the type instances ``B<t1>`` and ``I<t1>``: an 
expression of type ``C<t1>`` can be used wherever a value of type ``B<t1>`` or 
``I<t1>`` is expected. 

However, generic classes and interfaces are covariant in their type parameters. 
Hence if ``t11`` is a subtype of ``t12``, the type instance ``C<t11>`` *is* a 
subtype of the type instance ``C<t12>``. 


Constraints on Type Parameters
==============================

.. index:: generic constraint

.. |greaterthanorequal| unicode:: U+2265 .. greater than or equal
.. |lessthanorequal| unicode:: U+2264 .. less than or equal

A type parameter of a generic class ``C<T1, ... , Tn>`` may have type parameter 
constraints. The constraints on a type parameter are given in-line in the type
parameter list by a *constraint-clause* of this form:

    ``Ti extends`` ``c``\ :sub:`1` ``&`` ``c``\ :sub:`2` ``&`` ... ``&`` ``c``\ :sub:`n`

In the constraints clause, ``Ti`` is one of the type parameters ``T1, ... , Tn`` 
each ``c``\ :sub:`j` is a *constraint* on ``Ti``, and ``n`` |greaterthanorequal| 
1. A *constraint* ``c`` must be a type expression: an interface or a structure 
or a class type or one of the preceding type parameters ``Tj`` where ``1`` 
|lessthanorequal| ``j`` |lessthanorequal| ``i-1``.

The type expression may be a type instance and may involve any of the parameters 
``T1, ... , Tn``. 

Only the first constraint ``c``\ :sub:`1` can be a class type or a type 
parameter ``Tj``; the following ones must be interfaces. If the first constraint 
is a type parameter ``Tj``, then that must be the only constraint. 

The types ``t1, ... , tn`` used when creating a generic type instance ``C<t1, 
... , tn>`` must satisfy the *constraints*: if type parameter ``Ti`` is replaced 
by type ``ti`` throughout its *constraint-clause*, where the resulting 
constraints is ``ti extends`` ``c``\ :sub:`1` ``&`` ``c``\ :sub:`2` ``&`` ... 
``&`` ``c``\ :sub:`n`, it must hold that ``ti`` is a subtype of ``c``\ :sub:`1` 
and of ``c``\ :sub:`2` and so on up to ``c``\ :sub:`n`. 

How Can Type Parameters Be Used?
================================

Within the body ``{ ... }`` of a generic class ``class C<T1, ... , Tn> { .. }``
or generic interface or generic structure or generic enhancement, a type
parameter ``Ti`` may be used almost as if it were a public type:

- One can use type parameter ``Ti`` in the return type, variable types and 
  parameter types of non-static methods and their local inner classes, and in 
  the type and initializer of non-static fields and non-static constructors. In 
  these contexts, ``Ti`` can be used in type instances ``C1<..., Ti, ...>`` of 
  generic types ``C1``.
- One can use type parameter ``Ti`` for the same purposes in non-static member 
  classes, but not in static member classes nor in member interfaces.
- One can use ``new Ti[10]`` to create a new array whose element type is 
  ``Ti``; ``o typeis Ti`` to test whether ``o`` is an instance of ``Ti``; ``e 
  as Ti`` for type casts; ``Ti.Type`` to obtain the canonical object 
  representing the type ``Ti``; ``new Ti()`` to create an instance of ``Ti``; 
  and ``Ti`` in a ``typeof`` expression as ``typeof Ti`` or ``typeof C<T1>``. 
- One cannot call static methods on a type parameter ``Ti``, as in ``Ti.m()``, 
  or otherwise refer to the static members of a type parameter


Generic Interfaces
==================

.. index:: generic interface

A declaration of a *generic interface* ``I<T1, ..., Tn>`` has this form:

    *interface-modifiers* ``interface`` ``I<T1, ..., Tn>`` *extends-clause*  *interface-body*

The ``T1, ..., Tn`` are type parameters as for generic classes (section XXX), 
and *interface-modifiers*, *extends-clause* and *interface-body* are as for 
non-generic interfaces (section XXX). Each type parameter ``Ti`` may have type 
parameter constraints just as for a generic class. 

A type instance of the generic interface has form ``I<T1, ..., Tn>`` where the 
``t1, ..., tn`` are types. The types ``t1, ..., tn`` must satisfy the parameter 
constraints, if any, on the generic interface ``I<T1, ..., Tn>`` as described in 
section XXX. 

A generic interface is a subinterface of the interfaces mentioned in its 
*extends-clause*. Like a generic class, a generic interface is covariant in its 
type parameters. That is, ``I<String>`` is a subtype of ``I<Object>`` as 
``String`` is a subtype of ``Object``. 


Generic Methods
================

.. index:: generic method

A generic method is a method that takes one or more type parameters. A generic 
method may be declared inside a generic or non-generic class or interface or 
structure or enhancement.

A declaration of a generic method ``m<T1, ..., Tn>`` has this form:

    *method-modifiers* ``function`` ``m`` ``<T1, ..., Tn>`` ``(`` *formal-list* ``)``  ``:`` *returntype*  *method-body*
    
The *method-modifiers*, *formal-list*, *returntype* and *method-body* are as for 
non-generic methods (section XXX). Each type parameter ``Ti`` may have type 
parameter constraints just as for a generic class. 

The type parameters ``T1, ..., Tn`` may be used as types in the *returntype*, 
*formal-list* and *method-body*; as may the type parameters of any enclosing 
generic class if the method is non-static.

Generic methods of the same name ``m`` are not distinguished by their number of 
generic type parameters, and a generic method is not distinguished from a 
non-generic method of the same name. 

If a generic method overrides a generic method declared in a superclass or 
implements a generic method described in an interface, then it must have the 
same parameter constraints as those methods. The names of the type parameters 
are not significant, only their ordinal positions in the type parameter list 
``T1, ..., Tn``. 

A call of a generic method can be written without type arguments as in 
``o.m(...)``, or with explicit generic type arguments as in ``o.m<t1, ..., 
tn>()``. In the former case, the compiler will attempt to infer the appropriate 
type arguments ``t1, ..., tn`` automatically. 

Explicit generic type arguments can be given in the following syntactic forms of
a method call:

    ``o.m<t1, ..., tn>(`` *actual-list* ``)``
    
    ``super.m<t1, ..., tn>(`` *actual-list* ``)``
    
    ``C.m<t1, ..., tn>(`` *actual-list* ``)``

Note that to give a type arguments to a static method ``m`` in class ``C``, one 
must explicitly prefix the method call with the class name. Similarly, to give 
type arguments to an instance method in the current object, one mist explicitly 
prefix the method call with the current object reference. In any case, either 
none or all type arguments must be given. 




