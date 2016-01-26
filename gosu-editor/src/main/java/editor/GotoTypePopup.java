package editor;

import editor.search.StudioUtilities;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;


public class GotoTypePopup extends AbstractGotoPopup<CharSequence>
{
  private static final int DEFAULT_WAIT_TIME = 0;
  private static final int ROW_COUNT = 10;

  public static void display()
  {
    GotoTypePopup valuePopup = new GotoTypePopup( "" );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        doGoTo( strQualifedType );
      } );
    Component host = ((JFrame)RunMe.getEditorFrame()).getRootPane();
    valuePopup.show( host, 0, 0 );
  }

  public static void doGoTo( String strQualifedType )
  {
    StudioUtilities.showWaitCursor( true );
    try
    {
      File sourceFile;
      IType type = TypeSystem.getByFullNameIfValid( strQualifedType );
      URL resource;
      if( type instanceof IGosuEnhancement )
      {
        resource = TypeSystem.getGosuClassLoader().getActualLoader().getResource( strQualifedType.replace( '.', '/' ) + GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT );
      }
      else if( type instanceof ITemplateType )
      {
        resource = TypeSystem.getGosuClassLoader().getActualLoader().getResource( strQualifedType.replace( '.', '/' ) + GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT );
      }
      else if( type instanceof IGosuProgram )
      {
        resource = TypeSystem.getGosuClassLoader().getActualLoader().getResource( strQualifedType.replace( '.', '/' ) + GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT );
      }
      else if( type instanceof IGosuClass )
      {
        resource = TypeSystem.getGosuClassLoader().getActualLoader().getResource( strQualifedType.replace( '.', '/' ) + GosuClassTypeLoader.GOSU_CLASS_FILE_EXT );
      }
      else
      {
        return;
      }
      //noinspection ConstantConditions
      sourceFile = new File( URLDecoder.decode( resource.getFile(), "UTF-8" ) ).getAbsoluteFile();

      RunMe.getEditorFrame().openFile( sourceFile );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      StudioUtilities.showWaitCursor( false );
    }
  }

  public GotoTypePopup( String strPrefix )
  {
    super( DEFAULT_WAIT_TIME, ROW_COUNT, "Enter type name", strPrefix, true, true );
  }

  protected ArrayList<CharSequence> initializeData()
  {
    Set<String> allStudioLoadableTypes = TypeSystem.getTypeLoader( GosuClassTypeLoader.class ).getAllTypeNames();
    ArrayList<CharSequence> allTypes = new ArrayList<CharSequence>( allStudioLoadableTypes );
    Collections.sort( allTypes, new Comparator<CharSequence>()
    {
      public int compare( CharSequence o1, CharSequence o2 )
      {
        return getRelativeTypeName( (String)o1 ).compareToIgnoreCase( getRelativeTypeName( (String)o2 ) );
      }
    } );
    return allTypes;
  }

  protected TypeModel reconstructModel( String strPrefix )
  {
    return new TypeModel( StudioUtilities.filterStrings( getInitializedAllData(), strPrefix ) );
  }

  private static String getRelativeTypeName( String strType )
  {
    int iIndex = strType.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      return strType.substring( iIndex + 1 );
    }
    return strType;
  }

  protected TypeCellRenderer constructCellRenderer()
  {
    return new TypeCellRenderer();
  }

  protected void handleEdit()
  {
    filterDisplay( escape( _nameField.getText() ), true );
  }

  private String escape( String text )
  {
    text = text.replace( '.', '~' );
    return text;
  }

  private static class TypeModel extends AbstractPopupListModel<CharSequence>
  {
    private java.util.List<String> _allTypes;

    public TypeModel( java.util.List<String> allTypes )
    {
      _allTypes = allTypes;
    }

    public int getSize()
    {
      return _allTypes.size();
    }

    public String getElementAt( int i )
    {
      return _allTypes.get( i );
    }
  }
}
