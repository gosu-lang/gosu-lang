package editor;

import editor.util.EditorUtilities;

import java.nio.file.Path;
import gw.util.PathUtil;
import javax.swing.*;


/**
 */
public class EditorTabHistoryHandler implements ITabHistoryHandler
{
  @Override
  public ITabHistoryContext makeTabContext( EditorHost tab )
  {

    return new EditorTabContext( tab );
  }

  @Override
  public void selectTab( ITabHistoryContext tabContext )
  {
    if( tabContext == null )
    {
      return;
    }
    LabFrame.instance().selectTab( tabContext.getContentId() );
  }

  @Override
  public void closeTab( ITabHistoryContext tabContext )
  {
    if( tabContext == null )
    {
      return;
    }
    LabFrame.instance().closeTab( tabContext.getContentId() );
  }

  static class EditorTabContext implements ITabHistoryContext
  {
    private Path _contentId;
    //private Icon _icon;

    public EditorTabContext( EditorHost editor )
    {
      _contentId = (Path)editor.getClientProperty( "_file" );
      //_icon = view.getIcon( BeanInfo.ICON_COLOR_16x16 );
    }

    public String getDisplayName()
    {
      return PathUtil.getName( _contentId );
    }

    @Override
    public Icon getIcon( int iTypeFlags )
    {
      return EditorUtilities.findIcon( _contentId );
    }

    public boolean represents( EditorHost editor )
    {
      return editor != null && _contentId.equals( editor.getClientProperty( "_file" ) );
    }

    @Override
    public boolean equals( ITabHistoryContext other )
    {
      return equals( (Object)other );
    }

    @Override
    public boolean equals( Object o )
    {
      if( this == o )
      {
        return true;
      }
      if( o == null || getClass() != o.getClass() )
      {
        return false;
      }

      final EditorTabContext that = (EditorTabContext)o;

      return _contentId.equals( that._contentId );
    }

    @Override
    public int hashCode()
    {
      int result;
      result = _contentId.hashCode();
      return result;
    }

    @Override
    public Path getContentId()
    {
      return _contentId;
    }
  }
}
