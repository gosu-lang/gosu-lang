.. _interfaces:

**********
Interfaces
**********

.. index:: interfaces

Interface Declarations
======================

An *interface* describes fields and methods but does not generally implement 
them, unless a default implementation is provided. An *interface-declaration* 
may contain field descriptions, method descriptions, method-declarations, class 
declarations, and interface declarations, in any order.


    *interface-modifiers* ``interface`` ``I`` *extends-clause* ``{``

        *field-descriptions*

        *method-descriptions*

        *method-declarations*

        *class-declarations*

        *interface-declarations*

    ``}``


An interface may be declared at toplevel or inside a class or interface but not 
inside a method or constructor. At toplevel, the *interface-modifiers* may be 
``public`` or absent. A public interface is accessible also outside its package. 
Inside a class or interface, the *interface-modifiers* may be ``static`` (always 
implicitly understood) and at most one of *public*, *protected*, or *private*. 
An interface declaration may take type parameters and be generic (see XXX).

A *field-description* in an interface declares a named constant and must have 
the form

    *field-desc-modifiers* ``var`` ``f`` `:` *type* ``=`` *initializer*

where *field-desc-modifiers* is a list of ``static``, ``final``, ``private`` and 
``public``. All the modifiers but ``private`` need not to be given explicitly, 
as they are implicitly understood. 

A *method-description* for method ``m`` must have the form

    *method-desc-modifiers* ``function`` ``m`` ``(`` *formal-list* ``)`` *return-type*

where *method-desc-modifiers* is a list of ``abstract`` and ``public``, none of 
which need be given explicitly.

A *method-declaration* inside an interface is always implicitly ``public``. 
It is possible to declare static methods.

A *class-declaration* inside an interface is always implicitly ``static`` and 
``public``.

The *extends-clause* may be absent or have the form

    ``extends`` ``I1, I2, ...``

where ``I1, I2, ...`` is a nonempty list of interface names. If the 
*extends-clause* is present, then interface ``I`` describes all those members 
described by ``I1, I2, ...``, and interface ``I`` is a *subinterface* (and hence 
subtype) of ``I1, I2, ...``. Interface ``I`` can describe additional fields and 
methods, can override non-static methods but cannot shadow inherited fields. 
 
It is a compile-time error if a default method is override-equivalent with a 
non-private method of the class ``Object``, because any class implementing the 
interface will inherit its own implementation of the method.


Classes Implementing Interfaces
===============================

A class ``C`` may be declared to implement one or more interfaces by an 
*implements-clause*:

    ``class`` ``C`` ``implements`` ``I1, I2, ...`` *class-body*

In this case, ``C`` is a subtype of ``I1``, ``I2``, and so on, and ``C`` must 
declare all the non default methods described by ``I1, I2, ...`` with exactly 
the prescribed signatures and return types. A class may implement any number of 
interfaces. Fields, classes, and interfaces declared in ``I1, I2,...`` can be 
used in class ``C``. A class does not inherit static methods from its 
superinterfaces. 

Because inheritance is possible from multiple interfaces, the same default 
method can be inherited from different paths. Since each inherited default 
method provides a different implementation, the following rules apply to 
determine the declaration to use: 

- Classes always win. A declaration in the class or a superclass takes priority 
  over any default method declaration.
- Otherwise, the method with the same signature in the most specific 
  default-providing interface is selected. 

If a class ``C`` overrides a default method ``m`` in a interface ``I1``, it can 
call that method using the following syntax:

    ``super[`` ``I1`` ``]`` ``.`` ``m()``
