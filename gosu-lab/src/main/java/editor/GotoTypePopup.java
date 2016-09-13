package editor;

import editor.search.StudioUtilities;
import editor.util.Experiment;
import gw.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
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
    Component host = RunMe.getEditorFrame().getRootPane();
    valuePopup.show( host, 0, 0 );
  }

  public static void display( JTextComponent host, Consumer<String> consumer )
  {
    GotoTypePopup valuePopup = new GotoTypePopup( "" );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        consumer.accept( strQualifedType );
      } );
    valuePopup.show( StudioUtilities.rootPaneForComponent( host ), 0, 0 );
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

  protected GotoTypePopup( String title, String strPrefix )
  {
    super( DEFAULT_WAIT_TIME, ROW_COUNT, title, strPrefix, true, true );
  }

  protected List<String> initializeData()
  {
    List<String> allTypes = new ArrayList<>( RunMe.getEditorFrame().getGosuPanel().getTypeNamesCache().getAllTypeNames( null ) );
    Experiment experiment = RunMe.getEditorFrame().getGosuPanel().getExperimentView().getExperiment();
    allTypes = filterTypes( allTypes, experiment );
    Collections.sort( allTypes, ( o1, o2 ) -> getRelativeTypeName( o1 ).compareToIgnoreCase( getRelativeTypeName( o2 ) ) );
    return allTypes;
  }

  protected List<String> filterTypes( List<String> allGosuTypes, Experiment experiment )
  {
    return filterGosuClassFromExperimentsInResources( allGosuTypes, experiment );
  }

  private List<String> filterGosuClassFromExperimentsInResources( List<String> allGosuTypes, Experiment experiment )
  {
    String experimentPath = experiment.getExperimentDir().getAbsolutePath();
    List<String> relativeSrcPaths = experiment.getSourcePath().stream().map( srcPath ->
      (srcPath.startsWith( experimentPath )
       ? srcPath.substring( experimentPath.length() + 1 )
       : srcPath).replace( File.separatorChar, '.' ) ).collect( Collectors.toList() );
    return allGosuTypes.stream()
           .filter( s -> !relativeSrcPaths.stream().anyMatch( s::contains ) )
           .filter( s -> !s.startsWith( "gw.internal" ) )
           .collect( Collectors.toList() );
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

  static class TypeModel extends AbstractPopupListModel<String>
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
