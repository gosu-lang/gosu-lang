package editor.util;

import editor.FileWatcher;
import editor.GosuPanel;
import editor.LabFrame;
import editor.run.FqnRunConfig;
import editor.run.ProgramRunConfigFactory;
import editor.run.ProgramRunConfigParameters;
import editor.settings.CompilerSettings;
import editor.settings.CompilerSettingsParameters;
import editor.settings.ISettings;
import editor.settings.Settings;
import gw.lang.Gosu;
import manifold.internal.javac.IJavaParser;
import gw.lang.parser.GosuParserFactory;
import gw.lang.reflect.IType;
import gw.lang.reflect.json.IJsonIO;
import editor.run.IRunConfig;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import gw.lang.reflect.Expando;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.module.IProject;

import java.nio.file.Files;
import java.nio.file.Path;
import gw.util.PathUtil;
import java.io.File;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;
import javax.script.Bindings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 */
public class Experiment implements IProject
{
  private String _name;
  private List<String> _sourcePath;
  private List<String> _backingSourcePath;
  private Path _experimentDir;
  private List<String> _openFiles;
  private String _activeFile;
  private IRunConfig _mruRunConfig;
  private List<IRunConfig> _runConfigs;
  private ISettings _mruSettings;
  private Map<String, ISettings> _settings;
  private GosuPanel _gosuPanel;

  public Experiment( String name, Path dir, GosuPanel gosuPanel )
  {
    _name = name;
    _gosuPanel = gosuPanel;
    _sourcePath = Collections.singletonList( PathUtil.getAbsolutePathName( PathUtil.create( dir, getRelativeGosuSourcePath() ) ) );
    _backingSourcePath = Gosu.findJreSources();
    _experimentDir = dir;
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( _experimentDir );
    _openFiles = Collections.emptyList();
    _runConfigs = Collections.emptyList();
    _settings = Settings.makeDefaultSettings( this );
    addSettingsListeners();
  }

  public Experiment( Path dir, GosuPanel gosuPanel )
  {
    _name = PathUtil.getName( dir );
    _gosuPanel = gosuPanel;
    _sourcePath = Collections.emptyList();
    _backingSourcePath = Gosu.findJreSources();
    _experimentDir = dir;
    _openFiles = Collections.emptyList();
    _runConfigs = Collections.emptyList();
    _settings = Settings.makeDefaultSettings( this );
    GosuParserFactory.getInterface( IJavaParser.class ).get( 0 ).clear();
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

  public List<String> getBackingSourcePath()
  {
    return _backingSourcePath;
  }
  public void setBackingSourcePath( List<String> backingSource )
  {
    _backingSourcePath = backingSource;
  }

  public Path getExperimentDir()
  {
    return _experimentDir;
  }
  @SuppressWarnings("UnusedDeclaration")
  public void setExperimentDir( Path experimentDir )
  {
    _experimentDir = experimentDir;
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

  private Path getExperimentFile()
  {
    Path experimentDir = getExperimentDir();
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( experimentDir );
    Path experiment = LabFrame.findExperimentFile( experimentDir );
    if( experiment == null )
    {
      experiment = PathUtil.create( PathUtil.getAbsolutePathName( experimentDir ) + File.separator + PathUtil.getName( experimentDir ) + ".prj" );
    }
    return experiment;
  }

  public Path getOrMakeExperimentFile()
  {
    Path experiment = getExperimentFile();
    if( !PathUtil.exists( experiment ) )
    {
      save();
    }
    return experiment;
  }

  public void save()
  {
    TabPane tabPane = getGosuPanel().getEditorTabPane();

    Path userFile = getExperimentFile();

    Expando bindings = new Expando();

    bindings.put( "Title", "Gosu Experiment" );
    bindings.put( "Version", LabFrame.VERSION );
    bindings.put( "Name", getName() );

    if( tabPane != null )
    {
      ITab selectedTab = tabPane.getSelectedTab();
      if( selectedTab != null )
      {
        bindings.put( "ActiveTab", makeExperimentRelativePathWithSlashes( (Path)tabPane.getSelectedTab().getContentPane().getClientProperty( "_file" ) ) );
        bindings.put( "Tabs", Arrays.stream( tabPane.getTabs() ).map( e -> {
          Path file = (Path)e.getContentPane().getClientProperty( "_file" );
          return makeExperimentRelativePathWithSlashes( file );
        } ).collect( Collectors.toList() ) );
      }
    }

    bindings.put( "SourcePath", getSourcePath().stream().map( path -> {
      String relativePath = makeExperimentRelativePathWithSlashes( PathUtil.create( path ) );
      path = relativePath == null ? path : relativePath;
      //noinspection ResultOfMethodCallIgnored
      PathUtil.mkdirs( PathUtil.create( path ) );
      return path;
    } ).collect( Collectors.toList() ) );

    bindings.put( "BackingSource", getBackingSourcePath().stream().map( path -> {
      String relativePath = makeExperimentRelativePathWithSlashes( PathUtil.create( path ) );
      path = relativePath == null ? path : relativePath;
      //noinspection ResultOfMethodCallIgnored
      PathUtil.mkdirs( PathUtil.create( path ) );
      return path;
    } ).collect( Collectors.toList() ) );

    IJsonIO.writeList( "RunConfigs", _runConfigs, bindings );
    bindings.put( "MruRunConfig", getMruRunConfig() == null ? null : getMruRunConfig().getName() );

    IJsonIO.writeList( "Settings", new ArrayList<>( _settings.values() ), bindings );
    bindings.put( "MruSettings", getMruSettings() == null ? null : getMruSettings().getName() );

    try( Writer fw = PathUtil.createWriter( userFile ) )
    {
      String json = (String)ReflectUtil.invokeMethod( bindings, "toJson" );
      fw.write( json );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public String makeExperimentRelativePath( Path file )
  {
    String absExperimentDir = PathUtil.getAbsolutePathName( getExperimentDir() );
    String absFile = PathUtil.getAbsolutePathName( file );
    if( !absFile.startsWith( absExperimentDir + File.separator ) )
    {
      return absFile;
    }
    return absFile.substring( absExperimentDir.length() + 1 );
  }
  public String makeExperimentRelativePathWithSlashes( Path file )
  {
    return makeExperimentRelativePath( file ).replace( '\\', '/' );
  }

  private void load()
  {
    try
    {
      System.setProperty( "user.dir", PathUtil.getAbsolutePathName( getExperimentDir() ) );
      Bindings bindings = (Bindings)ReflectUtil.getProperty( getOrMakeExperimentFile().toUri().toURL(), "JsonContent" );

      setName( (String)bindings.getOrDefault( "Name", PathUtil.getName( getExperimentDir() ) ) );

      //noinspection unchecked
      List<String> sourcePath = (List<String>)bindings.getOrDefault( "SourcePath", Collections.emptyList() );
      if( sourcePath.isEmpty() )
      {
        Path srcPath = PathUtil.create( getExperimentDir(), getRelativeGosuSourcePath() );
        sourcePath.add( PathUtil.getAbsolutePathName( srcPath ) );
        _sourcePath = sourcePath;
      }
      else
      {
        _sourcePath = sourcePath.stream().map( e -> PathUtil.getAbsolutePathName( PathUtil.create( e ) ) ).collect( Collectors.toList() );
      }

      //noinspection unchecked
      List<String> backingSource = (List<String>)bindings.getOrDefault( "BackingSource", Collections.emptyList() );
      if( backingSource.isEmpty() )
      {
        _backingSourcePath = Gosu.findJreSources();
      }
      else
      {
        _backingSourcePath = backingSource.stream().map( e -> PathUtil.getAbsolutePathName( PathUtil.create( e ) ) ).collect( Collectors.toList() );
      }

      //noinspection unchecked
      List<String> tabs = (List<String>)bindings.getOrDefault( "Tabs", Collections.emptyList() );
      List<String> openFiles = new ArrayList<>();
      for( String strTab : tabs )
      {
        Path file = PathUtil.getAbsolutePath( PathUtil.create( strTab ) );
        if( PathUtil.isFile( file ) )
        {
          openFiles.add( PathUtil.getAbsolutePathName( file ) );
        }
      }
      _openFiles = openFiles;

      _activeFile = (String)bindings.get( "ActiveTab" );
      if( _activeFile != null && !_activeFile.isEmpty() )
      {
        _activeFile = PathUtil.getAbsolutePathName( PathUtil.create( _activeFile ) );
      }

      //noinspection unchecked
      _runConfigs = IJsonIO.readList( "RunConfigs", bindings );
      _mruRunConfig = findRunConfig( rc -> rc.getName().equals( bindings.get( "MruRunConfig" ) ) );

      _settings = new TreeMap<>();
      List<ISettings> settingList = IJsonIO.readList( "Settings", bindings );
      settingList.forEach( setting -> _settings.put( setting.getPath(), setting ) );
      _settings = Settings.mergeSettings( _settings, this );
      addSettingsListeners();

      _mruSettings = findSettings( settings -> settings.getName().equals( bindings.get( "MruSettings" ) ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private void addSettingsListeners()
  {
    CompilerSettings compilerSettings = (CompilerSettings)_settings.get( CompilerSettings.PATH );
    compilerSettings.addChangeListener( (oldParams, newParams) -> {
      if( newParams.isSourceBased() && !oldParams.isSourceBased() ||
          !oldParams.getOutputPath().equals( newParams.getOutputPath() ) )
      {
        // clean .class files from output
        String outputPath = oldParams.getOutputPath();
        if( outputPath != null && !outputPath.isEmpty() )
        {
          Path path = PathUtil.create( outputPath );
          if( Files.isDirectory( path ) )
          {
            PathUtil.delete( path, true );
          }
        }
      }
    } );
  }

  public static String getRelativeGosuSourcePath()
  {
    return "src" + File.separator + "main" + File.separator + "gosu";
  }

  public IRunConfig getOrCreateRunConfig( IType type )
  {
    String fqn = type.getName();
    IRunConfig rc = findRunConfig( runConfig -> runConfig instanceof FqnRunConfig &&
                                                ((FqnRunConfig)runConfig).getFqn().equals( fqn ) );
    return rc == null
           ? EditorUtilities.isRunnable( type )
             ? ProgramRunConfigFactory.instance().newRunConfig( makeProgramParams( type.getRelativeName(), type.getName() ) )
             : null
           : rc;
  }

  private ProgramRunConfigParameters makeProgramParams( String name, String fqn )
  {
    ProgramRunConfigParameters params = ProgramRunConfigFactory.instance().makeParameters();
    params.setName( name );
    params.setFqn( fqn );
    return params;
  }

  public IRunConfig findRunConfig( Predicate<IRunConfig> matcher )
  {
    if( matcher == null )
    {
      return null;
    }

    for( IRunConfig runConfig: _runConfigs )
    {
      if( matcher.test( runConfig ) )
      {
        return runConfig;
      }
    }
    return null;
  }

  public IRunConfig getMruRunConfig()
  {
    return _mruRunConfig;
  }
  public void setMruRunConfig( IRunConfig runConfig )
  {
    _mruRunConfig = runConfig;
  }

  public List<IRunConfig> getRunConfigs()
  {
    return _runConfigs;
  }

  public void addRunConfig( IRunConfig runConfig )
  {
    if( _runConfigs.isEmpty() )
    {
      _runConfigs = new ArrayList<>();
    }

    int index = _runConfigs.indexOf( runConfig );
    if( index >= 0 )
    {
      runConfig = _runConfigs.get( index );
      _runConfigs.remove( index );
    }
    _runConfigs.add( 0, runConfig );

    setMruRunConfig( runConfig );

    save();
  }

  public boolean removeRunConfig( IRunConfig runConfig )
  {
    if( _runConfigs.isEmpty() )
    {
      return false;
    }

    return _runConfigs.remove( runConfig );
  }

  public Map<String, ISettings> getSettings()
  {
    return _settings;
  }

  public ISettings getMruSettings()
  {
    return _mruSettings;
  }
  public void setMruSettings( ISettings settings )
  {
    _mruSettings = settings;
  }
  
  public ISettings findSettings( Predicate<ISettings> matcher )
  {
    if( matcher == null )
    {
      return null;
    }

    for( ISettings settings: _settings.values() )
    {
      if( matcher.test( settings ) )
      {
        return settings;
      }
    }
    return null;
  }  
}
