package editor;

import java.util.ArrayList;
import java.util.List;

public class RangeModel extends PopupListModel
{

  protected List _model;
  private boolean _bSortByName = true;

  public RangeModel()
  {
    super();
    init();
  }

  private RangeModel( RangeModel source )
  {
    super();
    init();
    _bSortByName = source._bSortByName;
    _model = source._model;
  }

  protected void init()
  {
    _model = new ArrayList();
  }

  public String getInsertionTextFrom( Object element )
  {
    return element.toString();
  }

  public String getDisplayText( Object element )
  {
    return element.toString();
  }

  public String getTypeName()
  {
    return "All Items in List";
  }

  public Object parseElement( String strValue )
  {
    return strValue;
  }

  public void setSortByName( boolean bSortByName )
  {
    _bSortByName = bSortByName;
  }

  public PopupListModel createSubset( List subset )
  {
    RangeModel copy = new RangeModel( this );
    copy._model = subset;
    return copy;
  }

  public List getModel()
  {
    if( shouldResetModel() )
    {
      resetModel();
    }
    return _model;
  }

  public void resetModel()
  {
    throw new RuntimeException( "resetModel method not supported." );
  }

  public List getUnfilteredModel()
  {
    return getModel();
  }

  public boolean shouldResetModel()
  {
    return false;
  }
}