package gw.lang.init;

import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.lang.Gosu;
import gw.lang.GosuShop;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import manifold.api.host.IModule;
import manifold.api.host.RefreshKind;
import manifold.api.type.ClassType;
import manifold.api.type.ContributorKind;
import manifold.api.type.ISourceKind;
import manifold.api.type.ITypeManifold;
import manifold.api.type.TypeName;
import manifold.util.ManExceptionUtil;

public class GosuTypeManifold implements ITypeManifold
{
  private IModule _module;

  @Override
  public void init( IModule module )
  {
    _module = module;
    // Initialize Gosu TypeSystem if not already initialized
    if( Gosu.bootstrapGosuWhenInitiatedViaClassfile() )
    {
      // Configure Gosu's source paths from Manifold's module
      configureGosuSourcePaths( module );
    }
  }

  private void configureGosuSourcePaths( IModule manifoldModule )
  {
    try
    {
      // Get source paths from Manifold module
      List<manifold.api.fs.IDirectory> manifoldSourcePaths = manifoldModule.getSourcePath();
      System.err.println( "[GosuTypeManifold] Manifold source paths: " + manifoldSourcePaths );

      // Convert Manifold IDirectory to Gosu IDirectory via File
      FileFactory fileFactory = FileFactory.instance();
      List<IDirectory> gosuSourcePaths = manifoldSourcePaths.stream()
        .map( manifold.api.fs.IResource::toJavaFile )
        .map( fileFactory::getIDirectory )
        .collect( Collectors.toList() );

      System.err.println( "[GosuTypeManifold] Converted Gosu source paths: " + gosuSourcePaths );

      // Configure Gosu's global module with these source paths
      gw.lang.reflect.module.IModule gosuModule = TypeSystem.getGlobalModule();
      List<IDirectory> existingSourcePaths = gosuModule.getSourcePath();

      System.err.println( "[GosuTypeManifold] Existing Gosu source paths: " + existingSourcePaths );

      // Merge with existing source paths (avoid duplicates)
      List<IDirectory> mergedPaths = new ArrayList<>( existingSourcePaths );
      for( IDirectory dir : gosuSourcePaths )
      {
        if( !mergedPaths.contains( dir ) )
        {
          mergedPaths.add( dir );
        }
      }

      System.err.println( "[GosuTypeManifold] Merged source paths: " + mergedPaths );
      gosuModule.setSourcePath( mergedPaths );
      System.err.println( "[GosuTypeManifold] Successfully configured Gosu source paths" );
    }
    catch( Exception e )
    {
      // Log but don't fail - fallback to classpath-based discovery
      System.err.println( "Warning: Failed to configure Gosu source paths from Manifold module: " + e.getMessage() );
      e.printStackTrace();
    }
  }

  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public ISourceKind getSourceKind()
  {
    return ISourceKind.Java;
  }

  @Override
  public ContributorKind getContributorKind()
  {
    return ContributorKind.Primary;
  }

  @Override
  public boolean isType( String fqn )
  {
    boolean result = findGosuClass( fqn ) instanceof IGosuClass;
    if( fqn != null && fqn.startsWith( "com.example" ) )
    {
      System.err.println( "[GosuTypeManifold] isType(" + fqn + ") = " + result );
    }
    return result;
  }

  @Override
  public boolean isTopLevelType( String fqn )
  {
    if( !isType( fqn ) )
    {
      return false;
    }

    IType type = findGosuClass( fqn );
    return type != null && type.getEnclosingType() == null;
  }

  @Override
  public boolean isPackage( String pkg )
  {
    return TypeSystem.getNamespace( pkg ) != null;
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    IType type = findGosuClass( fqn );
    return type == null ? null : ClassType.JavaClass;
  }

  @Override
  public String getPackage( String fqn )
  {
    IType type = findGosuClass( fqn );
    return type == null ? null : type.getNamespace();
  }

  @Override
  public String contribute( JavaFileManager.Location location, String fqn, boolean b, String s1, DiagnosticListener<JavaFileObject> diagnosticListener )
  {
    System.err.println( "[GosuTypeManifold] contribute() called for: " + fqn );
    IGosuClass gsClass = (IGosuClass)findGosuClass( fqn );
    if( gsClass == null )
    {
      System.err.println( "[GosuTypeManifold] WARNING: findGosuClass returned null for: " + fqn );
      return null;
    }
    System.err.println( "[GosuTypeManifold] Generating Java stub for: " + fqn );
    String stub = GosuShop.generateJavaStub( gsClass );
    System.err.println( "[GosuTypeManifold] Generated stub length: " + (stub == null ? "null" : stub.length()) );
    return stub;
  }

  @Override
  public Collection<String> getAllTypeNames()
  {
    //## todo: is this ok?  don't want to invoke if unnecessary
    return Collections.emptyList();
  }

  @Override
  public Collection<TypeName> getTypeNames( String pkg )
  {
    System.err.println( "[GosuTypeManifold] getTypeNames called for package: " + pkg );
    Collection<manifold.api.type.TypeName> typeNames = (Collection)GosuClassTypeLoader.getDefaultClassLoader().getTypeNames( pkg );

    System.err.println( "[GosuTypeManifold] Found " + (typeNames == null ? "null" : typeNames.size()) + " type names" );

    // Wrap type names to fix module reference
    if( typeNames != null && !typeNames.isEmpty() )
    {
      List<manifold.api.type.TypeName> wrapped = new ArrayList<>();
      for( manifold.api.type.TypeName tn : typeNames )
      {
        if( tn != null && tn.name != null )
        {
          // Create new TypeName with correct module reference
          manifold.api.type.TypeName fixedTn = new manifold.api.type.TypeName( tn.name, _module, tn.kind, tn.visibility );
          wrapped.add( fixedTn );
          System.err.println( "[GosuTypeManifold]   TypeName: name=" + tn.name + ", kind=" + tn.kind + ", module=" + (_module == null ? "null" : "non-null") );
        }
        else
        {
          System.err.println( "[GosuTypeManifold] WARNING: Skipped TypeName with null name: " + tn );
        }
      }
      return wrapped;
    }

    System.err.println( "[GosuTypeManifold] Returning empty list" );
    return Collections.emptyList();
  }

  @Override
  public List<IFile> findFilesForType( String fqn )
  {
    if( fqn != null && fqn.startsWith( "com.example" ) )
    {
      System.err.println( "[GosuTypeManifold] findFilesForType(" + fqn + ") called" );
    }

    IGosuClass gsClass = (IGosuClass)findGosuClass( fqn );
    if( gsClass == null )
    {
      if( fqn != null && fqn.startsWith( "com.example" ) )
      {
        System.err.println( "[GosuTypeManifold] findGosuClass returned null" );
      }
      return Collections.emptyList();
    }

    ISourceFileHandle sfh = gsClass.getSourceFileHandle();
    if( sfh == null )
    {
      if( fqn != null && fqn.startsWith( "com.example" ) )
      {
        System.err.println( "[GosuTypeManifold] getSourceFileHandle() returned null" );
      }
      return Collections.emptyList();
    }

    gw.fs.IFile file = sfh.getFile();
    if( file == null )
    {
      if( fqn != null && fqn.startsWith( "com.example" ) )
      {
        System.err.println( "[GosuTypeManifold] sfh.getFile() returned null" );
      }
      return Collections.emptyList();
    }

    URL url;
    try
    {
      url = file.toURI().toURL();
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }

    IFile manifoldFile = GosuRuntimeManifoldHost.get().getFileSystem().getIFile( url );
    if( fqn != null && fqn.startsWith( "com.example" ) )
    {
      System.err.println( "[GosuTypeManifold] Returning file: " + (manifoldFile == null ? "null" : manifoldFile.getName()) );
    }
    return Collections.singletonList( manifoldFile );
  }

  @Override
  public void clear()
  {

  }

  @Override
  public boolean handlesFileExtension( String extension )
  {
    // Check if the extension (without dot) matches any Gosu extension
    boolean result = Arrays.stream( GosuClassTypeLoader.ALL_EXTS ).anyMatch( ext -> ext.equalsIgnoreCase( "." + extension ) );
    System.err.println( "[GosuTypeManifold] handlesFileExtension(" + extension + ") = " + result );
    return result;
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    // Get extension and ensure it has a leading dot for comparison
    String rawExt = file.getExtension();
    final String fileExt = (rawExt != null && !rawExt.startsWith( "." )) ? "." + rawExt : rawExt;
    boolean result = Arrays.stream( GosuClassTypeLoader.ALL_EXTS ).anyMatch( ext -> ext.equalsIgnoreCase( fileExt ) );
    System.err.println( "[GosuTypeManifold] handlesFile(" + file.getName() + ") extension=" + fileExt + ", result=" + result );
    return result;
  }

  @Override
  public String[] getTypesForFile( IFile iFile )
  {
    try
    {
      // Convert Manifold IFile to Gosu IFile
      gw.fs.IFile gosuFile = FileFactory.instance().getIFile( iFile.toJavaFile() );

      // Get the type name from the file path
      // For a file like "src/main/java/com/example/Foo.gs", we want "com.example.Foo"
      String fileName = gosuFile.getName();
      if( fileName.lastIndexOf( '.' ) > 0 )
      {
        fileName = fileName.substring( 0, fileName.lastIndexOf( '.' ) );
      }

      // Get the package/namespace from the file's directory structure
      // Try to load the type and get its FQN
      IType type = TypeSystem.getByFullNameIfValidNoJava( fileName );
      if( type != null )
      {
        System.err.println( "[GosuTypeManifold] getTypesForFile(" + iFile.getName() + ") returning: " + type.getName() );
        return new String[] { type.getName() };
      }

      // Fallback: try to infer from source paths
      for( gw.lang.reflect.module.IModule module : TypeSystem.getExecutionEnvironment().getModules() )
      {
        for( IDirectory sourceDir : module.getSourcePath() )
        {
          String sourcePath = sourceDir.toJavaFile().getAbsolutePath();
          String filePath = gosuFile.toJavaFile().getAbsolutePath();

          if( filePath.startsWith( sourcePath ) )
          {
            // Convert file path to FQN
            String relativePath = filePath.substring( sourcePath.length() + 1 );
            String fqn = relativePath.replace( '/', '.' ).replace( '\\', '.' );
            if( fqn.lastIndexOf( '.' ) > 0 )
            {
              fqn = fqn.substring( 0, fqn.lastIndexOf( '.' ) );
            }

            // Verify the type exists
            type = TypeSystem.getByFullNameIfValidNoJava( fqn );
            if( type != null )
            {
              System.err.println( "[GosuTypeManifold] getTypesForFile(" + iFile.getName() + ") inferred FQN: " + fqn );
              return new String[] { fqn };
            }
          }
        }
      }

      System.err.println( "[GosuTypeManifold] getTypesForFile(" + iFile.getName() + ") - could not determine type" );
    }
    catch( Exception e )
    {
      System.err.println( "[GosuTypeManifold] ERROR in getTypesForFile: " + e.getMessage() );
      e.printStackTrace();
    }

    return new String[0];
  }

  @Override
  public RefreshKind refreshedFile( IFile iFile, String[] strings, RefreshKind refreshKind )
  {
    return null;
  }

  @Override
  public boolean isSelfCompile( String fqn )
  {
    if( fqn != null && fqn.startsWith( "com.example" ) )
    {
      System.err.println( "[GosuTypeManifold] isSelfCompile(" + fqn + ") = true" );
    }
    return true;
  }

  @Override
  public byte[] compile( String fqn )
  {
    System.err.println( "[GosuTypeManifold] compile() called for: " + fqn );
    try
    {
      IType type = findGosuClass( fqn );
      if( type == null )
      {
        System.err.println( "[GosuTypeManifold] WARNING: findGosuClass returned null for: " + fqn );
        return null;
      }
      System.err.println( "[GosuTypeManifold] Compiling: " + fqn );
      byte[] bytecode = type.compile();
      System.err.println( "[GosuTypeManifold] Compiled bytecode length: " + (bytecode == null ? "null" : bytecode.length) );

      // Copy source file to output directory (like legacy gosuc)
      copySourceFileToOutput( fqn, (IGosuClass)type );

      return bytecode;
    }
    catch( Exception e )
    {
      System.err.println( "[GosuTypeManifold] ERROR compiling " + fqn + ": " + e.getMessage() );
      e.printStackTrace();
      // wrap in an IOException since ClassLoaders conventionally handle this
      throw ManExceptionUtil.unchecked( new IOException( "Error compiling '" + fqn + "'", e ) );
    }
  }

  private void copySourceFileToOutput( String fqn, IGosuClass gsClass )
  {
    try
    {
      // Get source file
      ISourceFileHandle sfh = gsClass.getSourceFileHandle();
      if( sfh == null )
      {
        System.err.println( "[GosuTypeManifold] WARNING: No source file handle for: " + fqn );
        return;
      }

      gw.fs.IFile sourceFile = sfh.getFile();
      if( sourceFile == null || !sourceFile.exists() )
      {
        System.err.println( "[GosuTypeManifold] WARNING: Source file not found for: " + fqn );
        return;
      }

      // Get output path from Manifold module
      List<manifold.api.fs.IDirectory> outputPaths = _module.getOutputPath();
      if( outputPaths == null || outputPaths.isEmpty() )
      {
        System.err.println( "[GosuTypeManifold] WARNING: No output path configured" );
        return;
      }

      // Use first output path (typically build/classes/java/main)
      manifold.api.fs.IDirectory outputDir = outputPaths.get( 0 );

      // Construct target path: outputDir + package path + filename
      String packagePath = fqn.substring( 0, fqn.lastIndexOf( '.' ) ).replace( '.', '/' );
      manifold.api.fs.IDirectory packageDir = outputDir.dir( packagePath );

      // Create package directory if needed
      packageDir.mkdir();

      // Copy source file
      manifold.api.fs.IFile targetFile = packageDir.file( sourceFile.getName() );
      java.io.InputStream in = sourceFile.openInputStream();
      java.io.OutputStream out = targetFile.openOutputStream();
      byte[] buffer = new byte[8192];
      int bytesRead;
      while( (bytesRead = in.read( buffer )) != -1 )
      {
        out.write( buffer, 0, bytesRead );
      }
      in.close();
      out.close();

      System.err.println( "[GosuTypeManifold] Copied source file to: " + targetFile.toJavaFile().getAbsolutePath() );
    }
    catch( Exception e )
    {
      // Log but don't fail compilation if source copy fails
      System.err.println( "[GosuTypeManifold] WARNING: Failed to copy source file for " + fqn + ": " + e.getMessage() );
      e.printStackTrace();
    }
  }

  private IType findGosuClass( String fqn )
  {
    if( fqn != null && fqn.endsWith( "package-info" ) )
    {
      // do not search for package-info classes, it is unnecessary in this context and otherwise risks deadlock with
      // other tooling e.g., JAXB
      return null;
    }

    return TypeSystem.getByFullNameIfValidNoJava( fqn );
  }
}
