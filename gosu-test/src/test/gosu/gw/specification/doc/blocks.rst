.. _blocks:

*****************
Block Expressions
*****************

.. index:: block expression

A *block expression* is an expression that evaluates to an anonymous function.  This is similar to lambda expressions
as popularized by Lisp, Scheme and other functional programming languages.  Block expressions are useful for abstracting
algorithms by parameterizing an operation within a larger algorithm.  In Java, anonymous inner classes often satisfy
the same requirement.  As an example, Java has a Comparator_ class that provides an abstraction for comparing two
objects with each other for ordering purposes when passed to the ``Collections.sort()`` method.

.. _Comparator: http://docs.oracle.com/javase/7/docs/api/java/util/Comparator.html

.. productionlist:: blockExpr
  blockExpr : "\" [parameterDeclarationList] "->" (expression | statementBlock) .

The parameter declaration list for blocks is special in that, in some contexts, the types of the parameters can
be omitted for brevity.

Blocks have their own kind of type, the block type, within the Gosu type system, written in the form
``block(`` T\ :sub:`param-1` ``, ...,`` T\ :sub:`param-n` ``):`` T\ :sub:`return`, where T\ :sub:`param-n` is the type of the
n-th parameter of the block and T\ :sub:`return` is the return type of the block.  When an identifier is followed
by a type declaration, and that type declaration is a block type, the ``block`` keyword can be omitted:

``var example:(int):int``

The block's body can consist of either a simple expression or a statement block.  If the body is an expression, the
return type of the block is the type of the expression.  If the body is a statement list, it must have consistent
return points: either all reachable paths must return an expression value, or all reachable paths must contain
an empty return statement or have no return statement.  If the statement list contains return statements with
expressions, the return type of the block is the least upper bound of all return statement types, otherwise
the return type is ``void``.

Block type `A` is considered assignable to block type `B` if three requirements are met:

* `A`'s return type is *covariant* with `B`'s return type **or** `B`'s return type is `void`
* `A` and `B` have the same number of parameters
* Each parameter type T\ :sub:`param-n` of `A` is *contravariant* with the parameter type T\ :sub:`param-n` of type `B`.

Blocks have full access to all local variables available at the point of block, as well as the current ``this``
reference and all class variables .  A local variable that is referenced by a block is considered
*captured* and, because blocks can be returned from a function, will survive the return of a function.  This data is
referred to as the *closure* of the block.

If a loop variable falls within the closure of a block, a new instance of the variable is captured in each iteration
of the loop (i.e. a new closure is created on each iteration.)

Blocks can be *coerced* into Java interfaces which have one and only one method beyond the methods defined on ``Object``
if the block is compatible with the method.