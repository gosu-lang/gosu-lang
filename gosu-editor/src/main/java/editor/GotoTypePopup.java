package editor;

import editor.search.StudioUtilities;
import editor.util.Project;
import gw.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;


public class GotoTypePopup extends AbstractGotoPopup<String>
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
      IType type = TypeSystem.getByFullNameIfValid( strQualifedType );
      if( type == null )
      {
        return;
      }
      IFile[] sourceFiles = type.getSourceFiles();
      if( sourceFiles != null && sourceFiles.length > 0 )
      {
        IFile sourceFile = sourceFiles[0];
        if( sourceFile.isJavaFile() )
        {
          RunMe.getEditorFrame().openFile( sourceFile.toJavaFile() );
        }
      }
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

  protected List<String> initializeData()
  {
    Set<String> allGosuTypes = TypeSystem.getTypeLoader( GosuClassTypeLoader.class ).getAllTypeNames();
    Project project = RunMe.getEditorFrame().getGosuPanel().getProjectView().getProject();
    List<String> allTypes = filterGosuClassFromProjectsInResources( allGosuTypes, project );
    Collections.sort( allTypes, ( o1, o2 ) -> getRelativeTypeName( o1 ).compareToIgnoreCase( getRelativeTypeName( o2 ) ) );
    return allTypes;
  }

  private List<String> filterGosuClassFromProjectsInResources( Set<String> allGosuTypes, Project project )
  {
    String projectPath = project.getProjectDir().getAbsolutePath();
    List<String> relativeSrcPaths = project.getSourcePath().stream().map( srcPath ->
      (srcPath.startsWith( projectPath )
       ? srcPath.substring( projectPath.length() + 1 )
       : srcPath).replace( File.separatorChar, '.' ) ).collect( Collectors.toList() );
    return allGosuTypes.stream().filter( s -> !relativeSrcPaths.stream().anyMatch( s::contains ) ).collect( Collectors.toList() );
  }

  protected AbstractPopupListModel<String> reconstructModel( String strPrefix )
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

  private static class TypeModel extends AbstractPopupListModel<String>
  {
    private List<String> _allTypes;

    public TypeModel( List<String> allTypes )
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
