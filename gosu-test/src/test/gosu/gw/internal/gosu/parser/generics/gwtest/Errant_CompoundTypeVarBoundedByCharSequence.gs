package gw.internal.gosu.parser.generics.gwtest

uses java.io.Closeable

class Errant_CompoundTypeVarBoundedByCharSequence<T extends CharSequence>
{
  function narrow( item : T )
  {
    if( item typeis Closeable )
    {
      item.run()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
  }
}
