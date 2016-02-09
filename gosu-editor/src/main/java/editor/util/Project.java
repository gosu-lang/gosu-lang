package editor.util;

import editor.FileWatcher;
import editor.GosuPanel;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import gw.lang.reflect.module.IProject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 */
public class Project implements IProject
{
  private String _name;
  private List<String> _sourcePath;
  private File _projectDir;
  private List<String> _openFiles;
  private String _activeFile;
  private String _recentProgram;
  private GosuPanel _gosuPanel;

  public Project( String name, File dir, GosuPanel gosuPanel )
  {
    _name = name;
    _gosuPanel = gosuPanel;
    _sourcePath = Arrays.asList( new File( dir, getRelativeGosuSourcePath() ).getAbsolutePath() );
    _projectDir = dir;
    //noinspection ResultOfMethodCallIgnored
    _projectDir.mkdirs();
    _openFiles = Collections.emptyList();
  }

  public Project( File dir, GosuPanel gosuPanel )
  {
    _name = dir.getName();
    _projectDir = dir;
    _gosuPanel = gosuPanel;
    load();
    FileWatcher.instance( this );
  }

  public String getName()
  {
    return _name;
  }

  @Override
  public Object getNativeProject()
  {
    return this;
  }

  @Override
  public boolean isDisposed()
  {
    return false;
  }

  @Override
  public boolean isHeadless()
  {
    return false;
  }

  @Override
  public boolean isShadowMode()
  {
    return false;
  }

  public void setName( String name )
  {
    _name = name;
  }

  public List<String> getSourcePath()
  {
    return _sourcePath;
  }
  public void setSourcePath( List<String> classpath )
  {
    _sourcePath = classpath;
  }

  public File getProjectDir()
  {
    return _projectDir;
  }
  public void setProjectDir( File projectDir )
  {
    _projectDir = projectDir;
  }

  public List<String> getOpenFiles()
  {
    return _openFiles;
  }

  public String getActiveFile()
  {
    return _activeFile;
  }

  public GosuPanel getGosuPanel()
  {
    return _gosuPanel;
  }

  public File getOrMakeProjectFile()
  {
    File projectDir = getProjectDir();
    //noinspection ResultOfMethodCallIgnored
    projectDir.mkdirs();
    File project = EditorUtilities.findProjectFile(projectDir);
    if( project != null )
    {
      return project;
    }
    project = new File( projectDir.getAbsolutePath() + File.separator + projectDir.getName() + ".prj" );
    //noinspection ResultOfMethodCallIgnored
    try( FileWriter writer = new FileWriter( project ) )
    {
      Properties props = new Properties();
      props.put( "Name", getName() );
      if( getSourcePath() != null )
      {
        for( int i = 0; i < getSourcePath().size(); i++ )
        {
          String sourcePath = getSourcePath().get( i );
          props.put( "Classpath.Entry" + i, sourcePath );
          File sourceDir = new File( sourcePath );
          sourceDir.mkdirs();
        }
      }
      props.store( writer, "Gosu Project: " + getName() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return project;
  }

  public void save( TabPane tabPane )
  {
    File userFile = getOrMakeProjectFile();

    Properties props = new Properties();
    props.put( "Name", getName() );
    ITab selectedTab = tabPane.getSelectedTab();
    if( selectedTab != null )
    {
      props.put( "Tab.Active", makeProjectRelativePathWithSlashes( (File)tabPane.getSelectedTab().getContentPane().getClientProperty( "_file" ) ) );
      for( int i = 0; i < tabPane.getTabCount(); i++ )
      {
        File file = (File)tabPane.getTabAt( i ).getContentPane().getClientProperty( "_file" );
        props.put( "Tab.Open." + ((char)(i + 'A')), makeProjectRelativePathWithSlashes( file ) );
      }
    }

    for( int i = 0; i < getSourcePath().size(); i++ )
    {
      String path = getSourcePath().get( i );
      String relativePath = makeProjectRelativePathWithSlashes( new File( path ) );
      props.put( "Classpath.Entry" + i, relativePath == null ? path : relativePath );
    }

    if( getRecentProgram() != null )
    {
      props.put( "Recent.Program", getRecentProgram() );
    }

    try
    {
      FileWriter fw = new FileWriter( userFile );
      props.store( fw, "Gosu Project" );
      fw.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public String makeProjectRelativePath( File file )
  {
    String absProjectDir = getProjectDir().getAbsolutePath();
    String absFile = file.getAbsolutePath();
    if( !absFile.startsWith( absProjectDir ) )
    {
      return absFile;
    }
    return absFile.substring( absProjectDir.length() + 1 );
  }
  public String makeProjectRelativePathWithSlashes( File file )
  {
    return makeProjectRelativePath( file ).replace( '\\', '/' );
  }

  private void load()
  {
    Properties props = new Properties();
    try
    {
      System.setProperty( "user.dir", getProjectDir().getAbsolutePath() );
      FileReader reader = new FileReader( getOrMakeProjectFile() );
      props.load( reader );
      reader.close();

      setName( props.getProperty( "Name", getProjectDir().getName() ) );

      Set<String> keys = props.stringPropertyNames();
      //noinspection SuspiciousToArrayCall
      String[] sortedKeys = keys.toArray( new String[keys.size()] );
      Arrays.sort( sortedKeys );
      ArrayList<String> sourcePath = new ArrayList<>();
      for( String cpEntry : sortedKeys )
      {
        if( cpEntry.startsWith( "Classpath.Entry" ) )
        {
          File file = new File( props.getProperty( cpEntry ) ).getAbsoluteFile();
          if( file.exists() )
          {
            sourcePath.add( file.getAbsolutePath() );
          }
        }
      }
      _sourcePath = sourcePath;
      if( _sourcePath.isEmpty() )
      {
        File srcPath = new File( getProjectDir(), getRelativeGosuSourcePath() );
        _sourcePath.add( srcPath.getAbsolutePath() );
      }

      List<String> openFiles = new ArrayList<>();
      for( String strTab : sortedKeys )
      {
        if( strTab.startsWith( "Tab.Open" ) )
        {
          File file = new File( props.getProperty( strTab ) ).getAbsoluteFile();
          if( file.isFile() )
          {
            openFiles.add( file.getAbsolutePath() );
          }
        }
      }
      _openFiles = openFiles;
      _activeFile = props.getProperty( "Tab.Active" );
      if( _activeFile != null && !_activeFile.isEmpty() )
      {
        _activeFile = new File( _activeFile ).getAbsolutePath();
      }

      _recentProgram = props.getProperty( "Recent.Program" );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public File getOrMakeUntitledProgram()
  {
    File srcDir = new File( getSourcePath().get( 0 ) );
    //noinspection ResultOfMethodCallIgnored
    srcDir.mkdirs();
    File file = new File( srcDir, "Untitled.gsp" );
    try
    {
      //noinspection ResultOfMethodCallIgnored
      file.createNewFile();
      return file;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static String getRelativeGosuSourcePath()
  {
    return "src" + File.separator + "main" + File.separator + "gosu";
  }

  public String getRecentProgram()
  {
    return _recentProgram;
  }
  public void setRecentProgram( String name )
  {
    _recentProgram = name;
  }
}
