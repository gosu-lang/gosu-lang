package editor;

import gw.util.Predicate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public abstract class PopupListModel extends AbstractListModel implements IIntelliTextModel
{
  private String _strFilterPrefix;
  private Predicate _filter;

  protected PopupListModel()
  {
    _filter = null;
  }

  protected PopupListModel( Predicate filter )
  {
    _filter = filter;
  }

  /**
   * Given the a subset of components in this model, creates a new model
   * containing just this subset.
   */
  public abstract PopupListModel createSubset( List subset );

  /**
   * Returns the underlying list.
   */
  public abstract List getModel();

  /**
   * Returns an unfiltered version of this model. If the model is not filtered,
   * this may return the same reference as getModel().
   */
  public abstract List getUnfilteredModel();

  public void setFilter( Predicate filter )
  {
    _filter = filter;
  }

  public Predicate getFilter()
  {
    return _filter;
  }

  @Override
  public int getSize()
  {
    return getModelUpdatedOrFilteredByPredicate().size();
  }

  @Override
  public Object getElementAt( int iIndex )
  {
    List modelFilteredByPredicate = getModelUpdatedOrFilteredByPredicate();
    return modelFilteredByPredicate.size() == 0 ? null : modelFilteredByPredicate.get( iIndex );
  }

  @Override
  public String getFilterPrefix()
  {
    return _strFilterPrefix;
  }

  @Override
  public IIntelliTextModel getFilteredModel( String strPrefix )
  {
    if( strPrefix != null && strPrefix.length() > 0 )
    {
      int iDotIndex = strPrefix.lastIndexOf( '.' );
      if( iDotIndex >= 0 )
      {
        strPrefix = strPrefix.substring( iDotIndex + 1 );
      }
    }

    _strFilterPrefix = strPrefix;

    List filteredList = new ArrayList();
    List model = getUnfilteredModel();
    for( int i = 0; i < model.size(); i++ )
    {
      Object element = model.get( i );
      String strElement = getDisplayText( element );
      if( strPrefix == null ||
          strPrefix.length() == 0 ||
          strElement.toLowerCase().startsWith( strPrefix.toLowerCase() ) )
      {
        filteredList.add( element );
      }
    }
    return createSubset( filteredList );
  }

  /**
   * Returns the underlying list, filtered by the predicate if it exists
   */
  public List getModelUpdatedOrFilteredByPredicate()
  {

    List model = getModel();

    if( _filter != null )
    {
      model = new ArrayList( model ); //duplicate because, insanely, ColUtil.filter is destructive
      for( Iterator it = model.iterator(); it.hasNext(); )
      {
        Object o = it.next();
        if( !_filter.evaluate( o ) )
        {
          it.remove();
        }
      }
    }

    return model;
  }

}