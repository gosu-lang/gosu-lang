.. _enums:

**********
Enum Types
**********

.. index:: enum

An enum type is used to declare distinct enum values; an enum type is a
reference type. An *enum-type-declaration* is a specialized form of class
declaration that begins with a list of enum value declarations:

    *enum-modifiers* ``enum`` ``t`` *implements-clause* ``{``

        *enum-value-list*  [ ``;`` ]

        *field-declarations*

        *constructor-declarations*

        *method-declarations*

        *class-declarations*

        *interface-declarations*

        *structure-declarations*

        *enum-declarations*

    ``}``

The declaration of fields, method, nested types are as for ordinary classes;
these declarations may appear in any order. In fact, the enum type ``t`` is
implemented as a class and is a reference type, and there is exactly one instance
(object) of that implementation class for each declared enum value.

The *enum-modifiers* control the accessibility of the enum type and follow the
same rules as class access modifiers. The modifiers ``abstract`` and ``final``
cannot be used. An enum type may be declared to implement any number of
interfaces, but cannot be declared to have a superclass; it implicitly has the
superclass java.lang.Enum<t>. An enum type is implicitly final and cannot be
used as a superclass. A nested enum type is implicitly static (the ``static``
modifier is allowed but is implicitly understood and not required) and cannot
refer to instance fields of an enclosing type.

An enum declaration can declare private constructors only, and an enum value
cannot be explicitly created using ``new t (`` *actual list* ``)``. Instead enum
values are created by the *enum-value-list*, which is a (possibly empty)
comma-separated list of enum value declarations. An enum value declaration has
the form *enum-value* or *enum-value* ``(`` *actual list* ``)``. The first one
corresponds to a call to the enum type's argumentless constructor and the second
one to call to the constructor overload appropriate for the enum value's
*actual-list*.

A declared enum value has the type ``t`` of the enclosing enum type, and is
similar to a public static final field. The *ordinal value* of an enum value is
given by its position in the *enum-value-list*; the first one is zero. There are
not predefined conversions between enum values and integers, and no numeric
operations such as (``+``), nor comparison such as ('<'), on enum values. A
value of an enum type always equals a declared *enum-value*.

Outside an enum declaration, an enum value can be written in its fully
qualified form, such as ``Month.Jan``, or it can also be written in unqualified
form, such as ``Jan`` when the enum type can be inferred.

