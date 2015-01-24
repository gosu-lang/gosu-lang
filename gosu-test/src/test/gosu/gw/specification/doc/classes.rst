.. _classes:

*******
Classes
*******

.. index:: classes

Class Declarations and Class Bodies
===================================

A *class-declaration* of class ``C`` has the form

.. productionlist:: class
    GClass : "class" `id` `typeVariableDefs` ["extends" `classOrInterfaceType`] ["implements" `classOrInterfaceType` {"," `classOrInterfaceType`}] `classBody` .


A declaration of class ``C`` introduces a new reference type ``C``. The 
*class body* may contain declarations of fields, constructors, methods, 
properties, delegates, nested classes, nested interfaces, nested structure 
and nested enums. A class declaration may take type parameters and be 
generic (See XXX). The declarations in a class may appear in any order. 


A field, method, property, delegate, nested class, nested interface, nested 
structure, or nested enum is called a *member* of the class. A member may be 
declared ``static`` (with the exception of delegates). A non-static member 
is also called an *instance member*. The scope of a member is the entire 
class body, except where shadowed by a variable or parameter or by a member 
of a nested class or interface. Fields, properties and delegates cannot be 
shadowed. There can be no two nested classes or interfaces or structures or 
delegates or enum types with the same name, and no two fields with the same 
name, but a field, a method and a class (or interface or structure or enum 
type) may have the same name. By *static code* we mean expressions and 
statements in static field initializers and static methods. By *non-static 
code* we mean expressions and statements in constructors, non-static field 
initializers, and non-static methods. Non-static code is executed inside a 
*current object*, which can be referred to as ``this`` (section XXX). Static 
code cannot refer to non-static members or to ``this``, only to static 
members. 


Top-Level Classes, Nested Classes and Member Classes
====================================================

A *top-level class* is a class declared outside any other class or interface 
or structure declaration. A *nested class* is a class declared inside 
another class or interface or structure. A non-static nested class is called 
an *inner class*, because an object of the inner class will contain a 
reference to an object of the enclosing class. See also section XXX. 

Class Modifiers
===============

.. index:: modifiers

For a top-level class, the *class-modifiers* may be a list of ``public`` or 
``internal`` and at most one of ``abstract`` and ``final``. For a nested class, 
they may be a list of ``static``, and at most one of ``abstract`` and ``final``,
and at most one of ``private``, ``internal``, ``protected``, and ``public``. 


The Class Modifiers  ``public``, ``final``, ``abstract``
========================================================

If a top-level class ``C`` is declared ``public``, then it is accessible 
outside its package (chapter XXX). By default a top-level class is ``public`` so 
that modifier can be omitted. If a class ``C`` is declared ``internal``, then it 
is only accessible within its package. If a class ``C`` is declared ``final``, 
one cannot declare subclasses of ``C`` and hence cannot override any methods 
declared in ``C``. This is useful for preventing rogue subclasses from violating 
data representation invariants. 

If a class ``C`` is declared ``abstract``, then it cannot be instantiated, 
but non-abstract subclasses of ``C`` can be instantiated. An abstract class 
may declare constructors, to be executed when instantiating non-abstract 
subclasses. An abstract class may declare abstract and non-abstract methods; 
a non-abstract class cannot declare abstract methods. A class cannot be both 
abstract and final, because no objects could be created of that class. 

Subclasses, Superclasses, Class Hierarchy, Inheritance, and Overriding 
====================================================================== 

.. index:: inheritance, overriding 

A class ``C`` may be declared as a *subclass* of class ``B`` by an 
*extends-clause* of the form 

  ``class C extends B {`` ... ``}`` 

Class ``C`` is a subclass and hence a subtype (section XXX) of ``B`` and its 
supertypes. It inherits all methods and fields (even private ones, although they 
are not accessible in class ``C``), but not the constructors, from ``B``. 

Class B is called the *immediate superclass* of ``C``. A class can have at most 
one immediate superclass. The predefined class Object is a superclass of all 
other classes and has no superclass, so the classes form a *class hierarchy* in 
which every class is a descendant of its immediate superclass, except Object, 
which is at the top. The very first action of a constructor in ``C`` may be an 
explicit call to a constructor in superclass ``B``, like this: 

  ``super(`` *actual-list* ``)`` 

A superclass constructor call may appear only at the very beginning of a 
constructor body.
If a constructor ``constructor(``...\ ``)`` in subclass ``C`` does not 
explicitly call ``super(``...\ ``)`` as its first action, then it implicitly 
calls the argumentless *default constructor* ``constructor(``...\ ``)`` in 
superclass ``B`` as its first action, as if by ``super()``. In this case, ``B`` 
must have a non-private argumentless constructor ``constructor()``. Conversely, 
if there is no argumentless constructor ``constructor()`` in B, then 
``constructor(``...\ ``)`` in ``C`` must use ``super(``...\ ``)`` to explicitly 
call some other constructor in ``B``. 

The declaration of ``C`` may *override* a non-final non-static method ``m`` 
inherited from ``B`` by declaring a new non-static method ``m`` with the exact 
same signature. An overridden ``B``-method ``m`` can be referred to as 
``super.m`` inside ``C``'s constructors and non-static methods. 

The overriding method ``m`` in ``C``: 

* must be at least as accessible (section XXX) as the overridden method in 
  ``B``;
* must have the same signature(disregarding ``final``) as the overridden
  method in ``B``; and must have a return type that is a subtype of that of the
  overridden method in ``B``;

The declaration of ``C`` may *hide* a static method ``m`` inherited from ``B`` 
by declaring a new static method ``m`` with the exact same signature. 


Field Declarations in Classes
=============================

.. index:: field declaration 

The purpose of a *field* is to hold a value inside an object (if non-static) or 
a class (if static). A field must be declared in a class declaration. A 
*field-declaration* has the form 

  *field-modifiers* ``var`` fieldname ``:`` [ *type* ] [ ``=`` *initializer* ] 

The *field-modifiers* may be a list of the modifiers ``static``, ``final``, 
``transient`` (section XXX) and at most one of the access modifiers ``private``, 
``protected``, ``internal``, and ``public`` (section XXX). 

If a field ``f`` in class ``C`` is declared ``static``, then ``f`` is associated 
with the class ``C`` and can be referred to independently of any objects of 
class ``C``. The field can be referred to as ``C.f`` or ``o.f``, where ``o`` is 
an expression of type ``C``, or, in the declaration of ``C``, as ``f``. If a 
field ``f`` in class ``C`` is not declared ``static``, then ``f`` is associated 
with an *object* (also called *instance*) of class ``C``, and every instance has 
its own copy of the field. The field can be referred to as ``o.f``, where ``o`` 
is an expression of type ``C``, or, in non-static code in the declaration of 
``C``, as ``f``. 

If a field ``f`` in class ``C`` is declared ``final``, the field cannot be 
modified after initialization. If ``f`` has reference type and points to an
object or array, the object's fields or the array's elements may still be 
modified. The initialization must happen either in the declaration, or, if the 
field is non-static, precisely once in every constructor in class ``C``. A 
*field initializer* may be an expression or an object initializer (section XXX). 
A static field initializer can refer only to static members of ``C`` (chapter 
XXX). 

A field is given a *default initial value* depending on its type ``t``. If ``t`` 
is a primitive type, the field is initialized to 0 (when ``t`` is ``byte``, 
``char``, ``short``, ``int`` or ``long``) or 0.0 (when ``t`` is ``float`` or 
``double``) or ``false`` (when ``t`` is ``boolean``). If ``t`` is a reference 
type the field is initialized to ``null``. 

Static fields are initialized when the class is loaded. First all static fields 
are given their default initial values, then the static field initializers are 
executed, in order of appearance in the class declaration. 
Non-static fields are initialized when a constructor is called, at which time 
all static fields have been initialized already (section XXX). 

If a class ``C`` declares a non-static and non-private field ``f``, no subclass
of ``C`` can have a field with the same identifier ``f`` (section XXX).

TODO
----

Explain the full syntax and the properties ()the type is optional only for 
private fields. Add also property get/set in Method Declarations

.. productionlist:: field
    fieldDefn : "var" `id` `optionalType` ["as" ["readonly"] `id`] ["=" `expression`] .


The Member Access Modifiers  ``private``, ``protected``, ``internal``, ``public``
=================================================================================

.. index:: member access modifiers

A member that is not a field (i.e. method or nested class or nested interface or 
nested structure or nested enum) is always accessible in the class in which it 
is declared, except where shadowed by a variable or parameter or field (of a 
nested class). The *access modifiers* ``private``, ``internal``, ``protected``, 
and ``public`` determine where else the member is accessible. 

If a member is declared ``private`` in top-level class ``C`` or a nested class 
within ``C``, it is accessible in ``C`` and its nested classes, but not in their 
subclasses outside ``C`` nor in other classes. If a member in class ``C`` is 
declared ``protected``, it is accessible in all classes in the same package 
(chapter XXX) as ``C`` and in subclasses of ``C``, but not in non-subclasses in 
other packages. If a member in class ``C`` is declared ``internal``, it is 
accessible only in classes within the same package as ``C``, not in classes in 
other packages. If a member in class ``C`` is declared ``public``, it is 
accessible in all classes, including classes in other packages. If a member in 
class ``C`` is not declared ``private``, ``protected``, ``internal``, or
``public``, it has *default access*. The default access for fields is equivalent 
to ``private``, for any other member is equivalent to ``public``. Thus, in order
of increasing accessibility, we have ``private`` access, ``internal`` access, 
``protected`` access, and ``public`` access. 


Method Declarations
===================

.. index:: method

A *method* must be declared inside a class. A *method-declaration* declaring 
method ``m`` has the form
  
  *method-modifiers* ``function`` ``m(`` *formal-list* ``)``  [``:`` *return-type* ] *method-body*
  
The *formal-list* is a comma-separated list of zero or more *formal parameter 
declarations*, of the form

  *parameter-modifier*  *parameter-name* [ ``:`` ``type`` [ ``=`` constant ] | ``=`` constant]
  
The *parameter-modifier* may be ``final``, meaning that the parameter cannot be 
modified inside the method, or absent. The ``type`` is any type. The 
*parameter-name* must be distinct name . A formal parameter is an initialized 
variable; its scope is the *method-body*. For generic methods with type 
parameters, see XXX. A parameter may be made optional by giving a default value 
for it. In calls, the argument corresponding to an optional parameter may be 
left out, which is convenient for rarely used arguments and can reduce the 
temptation to introduce a large number of overloads. The default value must be a 
compile-time constant. An optional parameter must follow all required 
parameters. The ``type`` of a default parameter can omitted and it will be 
inferred from the default value. 

The method name ``m`` together with the list t\ :sub:`1`,..., t\ :sub:`n` of 
declared parameter types in the *formal-list* determine the *method signature* 
``m``\ (t\ :sub:`1`,..., t\ :sub:`n`), where any generic types in t\ 
:sub:`1`,..., t\ :sub:`n` are replaced by the underlying non-generic raw types 
(see XXX ). The *return-type* is not part of the method signature. 

A class may declare more than one method with the same *method-name*, provided 
they have different signatures(after replacing generic types by raw types). This
is called *overloading* of the *method-name*. A method with default parameters
cannot be overloaded. 

The *method-body* is a *block-statement* (section XXX) and thus may contain 
statements as well as declarations of variables. In particular, the 
*method-body* may contain ``return`` statements. If the *return-type* is 
``void``, the method does not return a value, and no ``return`` statement in the 
*method-body* can have an expression argument. If the *return-type* is not 
``void`` but a type, the method must return a value: it must not be possible for 
execution to reach the end of *method-body* without executing a ``return`` 
statement. Moreover, every ``return`` statement must have an expression argument 
whose type is a subtype of the ``return-type``. 

The *method-modifiers* may be either ``abstract`` or ``static`` or ``final`` 
(section XXX), and at most one of the access modifiers ``private``, 
``protected``, ``internal``, and ``public`` (section XXX). 

If a method ``m`` in class ``C`` is declared ``static``, then ``m`` is 
associated with the class ``C``; it can be referred to without any object. The 
method may be called as ``C.m(...)`` or as ``o.m(...)``, where ``o`` is an 
expression whose type is a subtype of ``C``, or, inside methods, constructors 
and field initializers in ``C``, simply as ``m(...)``. A static method can refer 
only to static fields and methods of the class. 

If a method ``m`` in class ``C`` is not declared ``static``, then ``m`` is 
associated with an object (instance) of class ``C``. Outside the class, the 
method must be called as ``o.m(...)``, where ``o`` is an object of class ``C`` 
or a subclass, or, inside non-static methods, and non-static field initializers 
in ``C``, simply as ``m(...)``. A non-static method can refer to all fields and 
methods of class ``C``, whether they are static or not. 

If a method ``m`` in class ``C`` is declared ``final``, it cannot be overridden 
(redefined) in subclasses. 

If a method ``m`` in class ``C`` is declared abstract, class ``C`` must itself 
be ``abstract`` (and so cannot be instantiated). 

An abstract declaration has this form, without a method body

  ``abstract`` *method-modifiers* ``function`` ``m(`` *formal-list* ``)``  [``:`` *return-type* ] 

Constructor Declarations
========================

.. index:: constructor

The purpose of a constructor in class ``C`` is to initialize new objects 
(instances) of the class. A *constructor-declaration* in class ``C`` has the 
form

  constructor-modifiers ``construct(`` *formal-list* ``)`` *constructor-body*
  
The *constructor-modifiers* may be a list of at most one of ``private``, 
``internal``, ``protected``, and ``public`` (section XXX); a constructor cannot 
be ``abstract``, ``final``, ``transient``, or ``static``. A constructor has no 
return type. 

Constructors may be overloaded in the same way as methods: the *constructor 
signature* (a list of the parameter types in *formal-list*) is used to 
distinguish constructors in the same class. A constructor may call another 
overloaded constructor in the same class using the syntax: 

  ``this(`` *actual-list* ``)``
  
but a constructor may not call itself, directly or indirectly. A call 
``this(...)`` to another constructor, if present, must be the very first action 
of a constructor, preceding any declaration or statement. 

The *constructor-body* is a *block-statement* (section XXX) and so may contain 
statements as well as declarations of variables. The *constructor-body* may 
contain ``return`` statements, but no ``return`` statement can take an 
expression argument. 

A class that does not explicitly declare a constructor implicitly declares a 
public, argumentless *default constructor* whose only (implicit) action is to 
call the superclass constructor (section XXX): 

  [``public``] ``construct()`` ``{ super() }``
 
When ``new`` creates a new object in memory (section XXX), the object's 
non-static fields are given default initial values according to their type. Then 
a constructor is called to further initialize the object, and the following 
happens: First, some superclass constructor is called (explicitly or implicitly) 
exactly once, then the non-static field initializers are executed once in order
of appearance in the class declaration, and finally the constructor body (except
the explicit superclass constructor call, if any) is executed. The call to a
superclass constructor will cause a call to a constructor in its superclass, and
so on, until reaching ``Object()``.


Nested Classes, Member Classes and Inner Classes
================================================

.. index:: nested classes, member classes, inner classes

A non-static nested class, that is, a non-static member class ``NMC`` is called 
an *inner class*. An object of an inner class always contains a reference to an 
object of the enclosing class ``C``, called the *enclosing object*. That object 
can be referred to as ``outer``, so a non-static member ``x`` of the enclosing 
object can be referred to as ``outer.x``. ``outer`` is an implicit private
member of the inner class. An inner class cannot have static members.

A static nested class, that is, a static member class ``SMC``, has no 
enclosing object and cannot refer to non-static members of the enclosing class 
``C``. This is the standard restriction on static members of a class (section 
XXX). A static member class may itself have static as well as non-static 
members. 


Anonymous Classes
=================

.. index:: anonymous classes

An *anonymous class* is a special kind of inner class; it must be declared 
inside a method, constructor or field initializer. An anonymous class can be 
declared, and exactly one instance created, using the special expression syntax 

  ``new`` ``C(`` *actual-list* ``)`` *class-body*
  
where ``C`` is a class name. This creates an anonymous subclass of class ``C``, 
with the given *class-body* (section XXX). Moreover, it creates an object of 
that anonymous subclass and calls the appropriate ``C`` constructor with the 
arguments in *actual-list*, as if by ``super(`` *actual-list* ``)``.

An anonymous class can declare at most one constructor. The arguments in
the *actual-list* must be compatible with the formal parameters of the
declared constructor.

When ``I`` is an interface name, the similar expression syntax

  ``new`` ``I()`` *class-body*
  
creates an anonymous class, with the given *class-body* (section XXX), that must 
implement the interface ``I``, and also creates an object of that anonymous 
class. Note that the parameter list after ``I`` must be empty. An anonymous
class implementing the interface ``I`` can declare at most one default
constructor.

