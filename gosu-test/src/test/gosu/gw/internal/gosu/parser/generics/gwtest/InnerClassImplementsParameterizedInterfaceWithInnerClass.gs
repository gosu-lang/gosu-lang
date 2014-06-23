package gw.internal.gosu.parser.generics.gwtest

uses javax.swing.JList
uses java.util.Collection
uses javax.swing.AbstractListModel
uses java.util.ArrayList
uses java.util.Iterator
uses java.lang.Iterable
uses java.lang.StringBuilder

class InnerClassImplementsParameterizedInterfaceWithInnerClass
{
  var _model : StockListModel as Model

  construct()
  {
    _model = new StockListModel()
  }

  private class StockListModel extends AbstractListModel implements Iterable<StockItem>
  {
    var _data =
      new ArrayList<StockItem>()
      {
        new StockItem( "goog" ),
        new StockItem( "java" ),
        new StockItem( "msft" ),
        new StockItem( "zero" ),
        new StockItem( "ibm" )
      }.sortBy( \ si -> si.Symbol.toLowerCase() )

    property get Size() : int
    {
      return _data.Count
    }

    function getElementAt( i : int ) : Object
    {
      return _data.get( i )
    }

    function update()
    {
      for( si in this )
      {
        si.update()
      }
    }

    function iterator() : Iterator<StockItem>
    {
      return _data.iterator()
    }
  }

  private static class StockItem
  {
    var _strSymbol : String as Symbol

    construct( strSymbol : String )
    {
      _strSymbol = strSymbol
    }

    function update()
    {
      _strSymbol = _strSymbol + _strSymbol
    }
  }
}
