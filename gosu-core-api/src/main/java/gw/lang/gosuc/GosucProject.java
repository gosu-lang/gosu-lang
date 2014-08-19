/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.CommonServices;
import gw.config.IMemoryMonitor;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.IProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Grammar for gosuc project file format:
 * <pre>
 * project-name {
 *   'sdk' {
 *     sdk-path-list
 *   }
 *   'modules' {
 *     module-list
 *   }
 *   'global-loaders' {
 *     global-loaders-list
 *   }
 *   root-dir : quoted-path
 *   app-name : quoted-name
 * }
 *
 * global-loaders-list = quoted-class-name[, global-loaders-list] | null
 *
 * quoted-class-name = '"'java-class-name'"'
 *
 * module-list = module[ module-list] | null
 *
 * module =
 *   module-name {
 *     'sourcepath' {
 *       sourcepath-list
 *     }
 *     'classpath' {
 *       classpath-lise
 *     }
 *     'outpath' {
 *       output-path-list
 *     }
 *     'deps' {
 *       deps-list
 *     }
 *   }
 *
 * sourcepath-list -> quoted-path[, sourcepath-list] | null
 *
 * classpath-list -> path[, classpath-list] | null
 *
 * quoted-path -> '"'path'"'
 *
 * deps-list -> dep[ dep-list] | null
 *
 * dep -> module-name[: export]
 * </pre>
 */
public class GosucProject implements IProject {
  private String _name;
  private GosucSdk _sdk;
  private List<GosucModule> _modules;
  private List<String> _globalLoaders;


  public GosucProject( String name, GosucSdk sdk, List<GosucModule> modules, List<String> globalLoaders ) {
    _name = name;
    _sdk = sdk;
    _modules = modules;
    _globalLoaders = globalLoaders;
  }

  public GosucProject() {
    this( TypeSystem.getExecutionEnvironment() );
  }
  public GosucProject( IExecutionEnvironment execEnv ) {
    _name = makeLegalName( execEnv.getProject().getName() );
    assignSdk( execEnv );
    assignModules( execEnv );
    assignGlobalLoadersFromProvider();
  }

  private String makeLegalName( CharSequence name ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < name.length(); i++ ) {
      char c = name.charAt( i );
      if( Character.isLetterOrDigit( c ) ||
          c == '-' ||
          c == '_' ) {
        sb.append( c );
      }
      else {
        sb.append( '_' );
      }
    }
    return sb.toString();
  }

  /**
   * Subclasse override to initialize/start dependencies
   */
  public void startDependencies() {
  }

  private void assignModules( IExecutionEnvironment execEnv ) {
    List<? extends IModule> modules = execEnv.getModules();
    List<GosucModule> gosucModules = new ArrayList<GosucModule>();
    for( IModule module: modules ) {
      if( module != execEnv.getGlobalModule() &&
          module != execEnv.getJreModule() )
      {
        gosucModules.add( makeModule( module ) );
      }
    }
    _modules = gosucModules;
  }

  private GosucModule makeModule( IModule module ) {
    final IDirectory outputPath = module.getOutputPath();
    return new GosucModule( module.getName(),
                            GosucUtil.makeStringPaths( module.getSourcePath() ),
                            GosucUtil.makeStringPaths( module.getJavaClassPath() ),
                            outputPath != null ? outputPath.getPath().getPathString() : null,
                            makeDependencies( module.getDependencies() ),
                            GosucUtil.makeStringPaths( module.getExcludedPaths() ));
  }

  private List<GosucDependency> makeDependencies( List<Dependency> dependencies ) {
    List<GosucDependency> deps = new ArrayList<GosucDependency>();
    for( Dependency d: dependencies ) {
      if( !d.getModule().getName().equals( "_jre_module_" ) ) {
        deps.add( new GosucDependency( d.getModule().getName(), d.isExported() ) );
      }
    }
    return deps;
  }

  private void assignSdk( IExecutionEnvironment execEnv ) {
    List<String> classpath = GosucUtil.makeStringPaths( execEnv.getJreModule().getJavaClassPath() );
    _sdk = new GosucSdk( classpath );
  }

  private void assignGlobalLoadersFromProvider() {
    List<Class<? extends ITypeLoader>> loaderClasses = CommonServices.getGlobalLoaderProvider().getGlobalLoaderTypes();
    List<String> globalLoaders = new ArrayList<String>();
    for( Class<? extends ITypeLoader> cls: loaderClasses ) {
      globalLoaders.add( cls.getName() );
    }
    _globalLoaders = globalLoaders;
  }

  public String getName() {
    return _name;
  }

  public GosucSdk getSdk() {
    return _sdk;
  }

  public List<GosucModule> getModules() {
    return _modules;
  }

  public List<String> getGlobalLoaders() {
    return _globalLoaders;
  }

  public String write() {
    return getName() + " {\n" +
             GosucUtil.indent( getSdk().write() ) +
             GosucUtil.indent( writeModules() ) +
             GosucUtil.indent( writeGlobalLoaders() ) +
             GosucUtil.indent( writeCustom() ) +
           "\n}\n";
  }

  protected String writeCustom() {
    return "";
  }

  private String writeGlobalLoaders() {
    StringBuilder sb = new StringBuilder();
    sb.append( "\nglobal-loaders {\n");
    for( String path : getGlobalLoaders() ) {
      sb.append( "  " ).append( "\"" ).append( path ).append( "\",\n" );
    }
    sb.append( "}\n" );
    return sb.toString();
  }

  private String writeModules() {
    StringBuilder sb = new StringBuilder();
    sb.append( "\nmodules {\n" );
    for( GosucModule mod: getModules() ) {
      sb.append( GosucUtil.indent( mod.write() ) ).append( "\n" );
    }
    sb.append( "}" );
    return sb.toString();
  }

  public static GosucProject parse( GosucProjectParser parser, ICustomParser custParser ) {
    IToken t = parser.getTokenizer().getCurrentToken();
    parser.verify( parser.match( null, ISourceCodeTokenizer.TT_WORD, false ), "Expecting name of project" );
    String name = t.getStringValue();
    parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin proejct definition" );
    GosucSdk sdk = GosucSdk.parse( parser );

    parser.verify( parser.matchWord( "modules", false ), "Expecting 'modules' keyword" );
    parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin module list" );
    List<GosucModule> modules = parseModuleList( parser );
    parser.verify( parser.match( null, '}', false ), "Expecting '}' to close module list" );

    List<String> globalLoaders = Collections.emptyList();
    if( parser.matchWord( "global-loaders", false ) ) {
      parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin global loader list" );
      globalLoaders = parseGlobalLoaderList( parser );
      parser.verify( parser.match( null, '}', false ), "Expecting '}' to close global loader list" );
    }

    if( custParser != null ) {
      custParser.parse( parser );
    }

    parser.verify( parser.match( null, '}', false ), "Expecting '}' to close project definition" );
    return custParser == null
           ? new GosucProject( name, sdk, modules, globalLoaders )
           : custParser.createProject( name, sdk, modules, globalLoaders );
  }

  private static List<GosucModule> parseModuleList( GosucProjectParser parser ) {
    List<GosucModule> modules = new ArrayList<GosucModule>();
    while( parser.match( null, ISourceCodeTokenizer.TT_WORD, true ) ) {
      modules.add( GosucModule.parse( parser ) );
    }
    return modules;
  }

  private static List<String> parseGlobalLoaderList( GosucProjectParser parser ) {
    List<String> paths = new ArrayList<String>();
    for( IToken t = parser.getTokenizer().getCurrentToken(); parser.match( null, '"', false ); t = parser.getTokenizer().getCurrentToken() ) {
      paths.add( t.getStringValue() );
      if( !parser.match( null, ',', false ) ) {
        break;
      }
    }
    return paths;
  }

  @Override
  public Object getNativeProject() {
    return this;
  }

  @Override
  public boolean isDisposed() {
    return false;
  }

  @Override
  public boolean isHeadless() {
    return true;
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null || getClass() != o.getClass() ) {
      return false;
    }

    GosucProject that = (GosucProject)o;

    if( !_globalLoaders.equals( that._globalLoaders ) ) {
      return false;
    }
    if( !_modules.equals( that._modules ) ) {
      return false;
    }
    if( !_name.equals( that._name ) ) {
      return false;
    }
    return _sdk.equals( that._sdk );
  }

  @Override
  public int hashCode() {
    int result = _name.hashCode();
    result = 31 * result + _sdk.hashCode();
    result = 31 * result + _modules.hashCode();
    result = 31 * result + _globalLoaders.hashCode();
    return result;
  }

  public Collection<? extends CharSequence> getAllDefinedTypes() {
    List<String> types = new ArrayList<String>();
    for( GosucModule module: _modules ) {
      if( module instanceof IJreModule || module == TypeSystem.getGlobalModule() ) {
        continue;
      }
      for( String srcRoot: module.getAllSourceRoots() ) {
        File rootFile = new File( srcRoot );
        if( !rootFile.exists() ) {
          throw new RuntimeException( srcRoot + " is not a valid source root" );
        }
        addTypesForFile( types, rootFile );
      }
    }
    return types;
  }

  private void addTypesForFile( List<String> types, File rootFile ) {
    for( File f: rootFile.listFiles() ) {
      if( f.isDirectory() ) {
        addTypesForFile( types, f );
      }
      else {
        IFile file = CommonServices.getFileSystem().getIFile( f );
        String[] typesForFile = TypeSystem.getTypesForFile( TypeSystem.getGlobalModule(), file );
        if( typesForFile.length > 0 ) {
          for( String type: typesForFile ) {
            if( !type.startsWith( "default." ) ) {
              types.add( type );
            }
          }
        }
      }
    }
  }

  public IMemoryMonitor getMemoryMonitor() {
    return null;
  }

  @Override
  public boolean isShadowMode() {
    return false;
  }
}
