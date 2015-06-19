.. _featureLiterals:

****************
Feature Literals
****************

.. index:: feature literals


Introduction
============

"Feature Literals" are a programming language feature of Gosu that allow users to refer to
features using a statically verified literal syntax, inspired by the Javadoc @link syntax.
This feature allows developers to make reflective references in a compile-time verified
manner.

Syntax
======

Feature literals use the ``#`` operator, with the following syntax:

  ``featureLiteral`` = ``rootExpression`` *#* ``featureReference``

  ``featureReference`` = (``id`` | *construct*) ``typeArguments`` ``optionalArguments``

Kinds of Feature Literal
========================

Feature Literals have a set of parsing rules that are dependent on the parse structure of the left hand side,
termed rootExpression, above.  The parse structure of rootExpression determines what TypeInformation the
featureReference is resolved against.  There are four different cases:

``rootExpression`` is a TypeLiteral
-----------------------------------

If ``rootExpression`` consists of a type literal, the valid features are those of the type represented by the feature literal.
As an example, if the root expression is String, then the acceptable features to resolve against are from java.lang.String
Feature literals of this type are known simply as "Feature Literals".

``rootExpression`` is a Property FeatureLiteral
-----------------------------------------------

If ``rootExpression`` consists of an existing Property FeatureLiteral, bound or unbound, the valid features are those of the
property type of the root property literal feature. As an example, if the root expression is ``Contact#Address``, and the
return type of the property ``Address`` is ``T`` then the acceptable features to resolve against are from the type ``T``.
Property literals of this type are known as "Property Literal Chains".

``rootExpression`` is any other non-blank expression
----------------------------------------------------

If ``rootExpression`` is of any other type of non-blank expression, the valid features are those of the type of the root
expression.  As an example, if the root expression is ``"A String"``, then the acceptable features to resolve
against are from ``java.lang.String``.  Feature literals of this type are known as "Bound Feature Literals" because they
are bound to the instance value on the right hand side.

The feature literal is a relative Expression
--------------------------------------------

If a ``rootExpression`` does not exist (e.g. ``#exampleFunction()``) then the method is resolved against the current class
being parsed, as an unbound feature literal.  So, if ``exampleFunction()`` is defined in ``ExampleClass``, and the expression
``#foo()`` is parsed within the direct definition (i.e. not in a nested class, anonymous or named) of ``ExampleClass``,
the expression is equivalent to ``ExampleClass#foo()`` The above rules specify how the feature references on the RHS of
the feature literal, the featureReference, can be resolved.

In the next section we will consider the different types of features that can be resolved against.

Feature Literal Accessibility Rules
===================================

Feature Literals can reference any method, property or constructor of type type of the ``rootExpression``.
Feature Literals can refer to any such feature, regardless of access modifiers.  (i.e. private features are resolvable,
although IDEs may wish to hide and/or indicate caution in using them.)

Feature Literal Classifications
===============================

In addition to the types of Feature Literal outlined above, different sub-classifications of Feature Literals
exist, based on the type of feature they refer to and the form of their arguments, if any.  These are outlined below.

Method Feature Literals
-----------------------

Methods may be referred to by using the method argument list syntax, with an opening parenthesis and closing parenthesis.
Feature Literals of this form are referred to as a "Method Feature Literals".  The arguments of a Method Feature Literal
with the method name ``N`` may take one of three forms:

  - A comma separated list of TypeLiterals which must match exactly with the parameter types of method being matched,
    including all optional arguments.

  - A comma separated list of expressions which will resolve to a method against the rootExpression type using the
    standard argument parsing logic for method invocation.  Method Feature Literals of this kind are further described
    as "Method Feature Literals With Bound Parameters"

  - The special case of a method ``N`` with no overloading, in which case the arguments can be omitted entirely and it
    will be treated as the same as case #1 above.

Property Feature Literals
-------------------------

Properties can be referred to by simply using the property name.  Feature Literals of this form are referred to as
"Property Feature Literals".

Constructor Feature Literals
----------------------------

Constructors can be referred to by using the ``construct`` keyword and the argument resolution rules for Method Feature
Literals specified above.  Feature literals of this type are referred to as "Constructor Feature Literals" or
"Constructor Feature Literals With Bound Parameters", depending on the form of the literal.

Feature Literal Expression Typing
=================================

Preamble
--------

Let the "final root type" of an expression be defined thusly: If an expression ``E`` is of the form TypeLiteral,
its final root type is the type value of the evaluated expression ``E``.  Otherwise it is the type of ``E``.

Typing Table
------------

Feature literal expression typing consists of 12 cases:

Given a Feature Literal ``E`` of the form ``rootExpression#featureReference``, the type of ``E`` is as follows:

#) If the Feature Literal is a Method Feature Literal with no bound parameters and refers to a static method
   ``M``, then ``E`` is ``gw.lang.reflect.features.MethodReference<R, T>``, where ``R`` is the final root type of
   the rootExpression and ``T`` is a block type matching the type signature of ``M``

#) If the Feature Literal is a Method Feature Literal with no bound parameters and refers to a non-static method ``M``,
   then ``E`` is ``gw.lang.reflect.features.MethodReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type with parameters that consist of ``R``, followed by the parameters of ``M``,
   and with a return type that is the same  as the return type of ``M``.

#) If the Feature Literal is a Method Feature Literal with bound parameters and refers to a static method ``M``,
   then ``E`` is ``gw.lang.reflect.features.MethodReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type with no parameters and with a return type that is the same as the
   return type of ``M``.

#) If the Feature Literal is a Method Feature Literal with bound parameters and refers to a non-static method ``M``,
   then ``E`` is ``gw.lang.reflect.features.MethodReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type with one parameter of type ``R`` and with a return type that is the same as
   the return type of ``M``.

#) If the Feature Literal is a Bound Method Feature Literal with no bound parameters and refers to a method ``M``, then
   ``E`` is ``gw.lang.reflect.features.BoundMethodReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type matching the type signature of ``M``.

#) If the Feature Literal is a Bound Method Feature Literal with bound parameters and refers to a method ``M``, then
   ``E`` is ``gw.lang.reflect.features.BoundMethodReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type with no parameters an a return type that is the same as the return type of
   ``M``

#) If the Feature Literal is a Property Feature Literal and refers to a property ``P``, then ``E`` is
   ``gw.lang.reflect.features.PropertyReference<R, T>``, where ``R`` is the type of the final root type of the
   rootExpression and ``T`` is the type of the property ``P``.

#) If the Feature Literal is a Bound Property Feature Literal and refers to a property ``P``, then ``E`` is
   ``gw.lang.reflect.features.BoundPropertyReference<R, T>``, where ``R`` is the final root type of the rootExpression
   and ``T`` is the type of the property ``P``.

#) If the Feature Literal is a Constructor Feature Literal with no bound parameters and refers to a constructor ``C``,
   then ``E`` is ``gw.lang.reflect.features.ConstructorReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type whose parameters match the constructor ``C`` and whose return type is ``R``.

#) If the Feature Literal is a Constructor Feature Literal with bound parameters and refers to a constructor ``C``,
   then ``E`` is ``gw.lang.reflect.features.ConstructorReference<R, T>``, where ``R`` is the final root type of the
   rootExpression and ``T`` is a block type with no parameters and whose return type is ``R``.

#) If the Feature Literal is a Property Literal Chain that begins with a Feature Literal ``FL1`` and finishes with a
   Property Feature Literal referencing property ``P2``, then ``E`` is
   ``gw.lang.reflect.features.PropertyChainReference<R, T>``, where ``R`` is the final root type of the
   rootExpression of ``FL1`` and ``T`` is the type of property ``P2``.

#) If the Feature Literal is a Bound Property Literal Chain that begins with an expression  Feature Literal ``FL1`` and
   finishes with a Property Feature Literal referencing  property ``P2``, then ``E`` is
   ``gw.lang.reflect.features.BoundPropertyChainReference<R, T>``, where ``R`` is the final root type of the
   rootExpression of ``FL1`` and ``T`` is the type of property ``P2``.

Generics & Feature Literals
===========================

Generics type variables interact with feature literals in two ways, depending on the kind of the type parameter in
question.

- Generic class type parameters are respected in feature literal expressions.

- Generic method type parameters are not usable in feature literal expressions, and are always set to their
  bounding type. There is no way to refer to a specific parameterized version of a generic method.
  Attempting to parameterize a generic method literal is an error.