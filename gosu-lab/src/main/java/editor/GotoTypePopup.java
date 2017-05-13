package editor;

import editor.util.EditorUtilities;
import editor.util.Experiment;
import gw.util.PathUtil;
import manifold.api.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.Set;
import java.util.TreeSet;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
    Component host = LabFrame.instance().getRootPane();
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
    valuePopup.show( EditorUtilities.rootPaneForComponent( host ), 0, 0 );
  }

  public static void doGoTo( String strQualifedType )
  {
    EditorUtilities.showWaitCursor( true );
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
        //## todo: maybe support multiple files here?
        IFile sourceFile = sourceFiles[0];
        LabFrame.instance().openFile( PathUtil.create( sourceFile.toURI() ) );
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      EditorUtilities.showWaitCursor( false );
    }
  }

  public GotoTypePopup( String strPrefix )
  {
    super( DEFAULT_WAIT_TIME, ROW_COUNT, "Enter type name", strPrefix, true, true, true );
  }

  protected GotoTypePopup( String title, String strPrefix )
  {
    super( DEFAULT_WAIT_TIME, ROW_COUNT, title, strPrefix, true, true, true );
  }

//  protected List<String> initializeData()
//  {
//    List<String> allTypes = new ArrayList<>( LabFrame.instance().getGosuPanel().getTypeNamesCache().getAllTypeNames( null ) );
//    Experiment experiment = LabFrame.instance().getGosuPanel().getExperimentView().getExperiment();
//    allTypes = filterTypes( allTypes, experiment );
//    Collections.sort( allTypes, ( o1, o2 ) -> getRelativeTypeName( o1 ).compareToIgnoreCase( getRelativeTypeName( o2 ) ) );
//    return allTypes;
//  }

  protected List<String> initializeData()
  {
    Set<String> allTypes;
    if( !isExternalTypes() )
    {
      allTypes = FileTreeUtil.getAllExperimentTypes();
    }
    else
    {
      allTypes = LabFrame.instance().getGosuPanel().getTypeNamesCache().getAllTypeNames( null );
    }
    Experiment experiment = LabFrame.instance().getGosuPanel().getExperimentView().getExperiment();
    allTypes = filterTypes( allTypes, experiment );

    ArrayList<String> sortedList = new ArrayList<>( allTypes );
    sortedList.sort( ( o1, o2) -> getRelativeTypeName( o1 ).compareToIgnoreCase( getRelativeTypeName( o2 ) ) );
    return sortedList;
  }

  protected Set<String> filterTypes( Set<String> allGosuTypes, Experiment experiment )
  {
    return filterGosuClassFromExperimentsInResources( allGosuTypes, experiment );
  }

  private Set<String> filterGosuClassFromExperimentsInResources( Set<String> allGosuTypes, Experiment experiment )
  {
    String experimentPath = PathUtil.getAbsolutePathName( experiment.getExperimentDir() );
    List<String> relativeSrcPaths = experiment.getSourcePath().stream().map( srcPath ->
      (srcPath.startsWith( experimentPath )
       ? srcPath.substring( experimentPath.length() + 1 )
       : srcPath).replace( File.separatorChar, '.' ) ).collect( Collectors.toList() );
    return allGosuTypes.stream()
           .filter( fqn -> relativeSrcPaths.stream().noneMatch( fqn::contains ) )
           .filter( fqn -> !fqn.startsWith( "gw.internal" ) )
           .collect( Collectors.toSet() );
  }

  protected AbstractPopupListModel<String> reconstructModel( String strPrefix )
  {
    return new TypeModel( filterStrings( getInitializedAllData(), strPrefix ) );
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
    return new TypeCellRenderer( _list );
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

  public static List<String> filterStrings( Collection<? extends CharSequence> collection, String filter )
  {
    Set<String> set = _filterStrings( collection, filter, false );
    set.addAll( _filterStrings( collection, filter, true ) );
    return new ArrayList<>( set );
  }
  private static Set<String> _filterStrings( Collection<? extends CharSequence> collection, String filter, boolean forceLower )
  {
    if( filter == null )
    {
      filter = "";
    }
    int iDotIndex = filter.lastIndexOf( '.' );
    if( iDotIndex >= 0 )
    {
      filter = filter.substring( iDotIndex + 1 );
    }

    TreeSet<String> filteredTypes = new TreeSet<>();
    if( filter.length() > 0 )
    {
      int iFlags = 0;
      if( !forceLower &&
          filter.length() > 0 && filter.indexOf( '*' ) < 0 &&
          Character.isUpperCase( filter.charAt( 0 ) ) )
      {
        filter = camelCasePrefix( filter );
      }
      else
      {
        iFlags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
      }

      // Replace all wildcard '*' chars with the regex ".*" expression
      filter = filter.replaceAll( "\\*", "\\.\\*" );

      // A '#' char indicates that the proper regex syntax has already been embedded in the string;
      // we only need to replace the '#' with '*'
      filter = filter.replaceAll( "\\#", "\\*" );

      boolean bHasDot = filter.indexOf( '~' ) >= 0;
      filter = filter.replaceAll( "~", "(\\\\.|" + '\u2024' + ")" );

      boolean exactMatch = filter.endsWith( " " );
      filter = filter.trim();

      // Match the expression string followed by any number of chars
      try
      {
        Pattern pattern = Pattern.compile( '^' + filter + (exactMatch ? "" : (filter.startsWith( ".*" ) ? "" : ".*")), iFlags );
        for( CharSequence cs : collection )
        {
          String strType = cs.toString();
          String strName = bHasDot ? strType : getRelativeTypeName( strType );
          boolean shouldAdd = exactMatch ? pattern.matcher( strName ).matches() : pattern.matcher( strName ).find();
          if( shouldAdd )
          {
            filteredTypes.add( strType );
          }
        }
      }
      catch( PatternSyntaxException e )
      {
        // Skip
      }
    }
    return filteredTypes;
  }

  private static String camelCasePrefix( String strPrefix )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = strPrefix.length() - 1; i >= 0; i-- )
    {
      char c = strPrefix.charAt( i );
      sb.insert( 0, c );
      if( i != 0 && Character.isUpperCase( c ) )
      {
        // Each uppercase char in the prefix match all but uppercase chars preceding it e.g.,
        // "AcT" matches any string starting with "Ac" followed by any number of non-uppercase chars followed by "T".
        // It's the same as the regex "Ac[^A-Z]*T"
        sb.insert( 0, "[^A-Z]#" ); // see note in filter method re # char
      }
    }
    return sb.toString();
  }
}
