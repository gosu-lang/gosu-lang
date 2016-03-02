package editor;

import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IEnumValue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class EnumerationPopupListModel extends PopupListModel
{
  private List _model;
  private IEnumType _enumAccess;

  public EnumerationPopupListModel( IEnumType enumAccess )
  {
    super();
    init( enumAccess );
  }

  private EnumerationPopupListModel( EnumerationPopupListModel source )
  {
    super();
    init( source._enumAccess );

    _model = source._model;
  }

  private void init( IEnumType classTypeKey )
  {
    _enumAccess = classTypeKey;
    _model = getTypeCodes();
  }


  public String getInsertionTextFrom( Object element )
  {
    IEnumValue val = (IEnumValue)element;
    String str = val.getCode();
    return str;
  }

  public String getDisplayText( Object element )
  {
    return ((IEnumValue)element).getCode();
  }

  public PopupListModel createSubset( List subset )
  {
    EnumerationPopupListModel copy = new EnumerationPopupListModel( this );
    copy._model = subset;
    return copy;
  }

  public List getModel()
  {
    return _model;
  }

  public List getUnfilteredModel()
  {
    return getTypeCodes();
  }

  public String getTypeName()
  {
    return _enumAccess.getDisplayName();
  }

  public Object parseElement( String strValue )
  {
    return _enumAccess.getEnumValue( strValue );
  }

  public IIntelliTextModel getFilteredModel( String strPrefix )
  {
    PopupListModel filteredModel = (PopupListModel)super.getFilteredModel( strPrefix );
    Collections.sort( filteredModel.getModelUpdatedOrFilteredByPredicate(), new EnumeratorComparator() );
    return filteredModel;
  }

  private List getTypeCodes()
  {
    List<IEnumValue> typecodes = _enumAccess.getEnumValues();
    Collections.sort( typecodes, new EnumeratorComparator() );
    return typecodes;
  }


  private static class EnumeratorComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      String code1 = ((IEnumValue)o1).getCode();
      String code2 = ((IEnumValue)o2).getCode();
      return code1.compareToIgnoreCase( code2 );
    }
  }

}