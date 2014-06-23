package gw.internal.gosu.compiler.sample.statement.classes

uses java.awt.Rectangle

class GosuShape extends JavaClass<Rectangle>
{
  function foo( rc : Rectangle ) : Rectangle
  {
    modify( rc )
    return access()
  }

  function bar( rc : Rectangle ) : Rectangle
  {
    return involvesMethodTv( rc, "whatever" )
  }
}