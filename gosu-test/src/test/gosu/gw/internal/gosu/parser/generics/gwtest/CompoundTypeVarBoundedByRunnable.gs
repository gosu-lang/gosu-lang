package gw.internal.gosu.parser.generics.gwtest

uses java.io.Closeable

class CompoundTypeVarBoundedByRunnable<T extends Runnable>
{
  function narrow( item : T )
  {
    if( item typeis Closeable )
    {
      item.run()
    }
  }
}
