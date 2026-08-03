package gw.lang.gosuc.simple;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.config.IMemoryMonitor;
import gw.config.IPlatformHelper;
import gw.config.Registry;
import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.GosuShop;
import gw.lang.IIncrementalCompilationManager;
import gw.lang.gosuc.GosucDependency;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.cli.CommandLineOptions;
import gw.lang.init.GosuInitialization;
import gw.lang.javac.SourceJavaFileObject;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IModule;
import gw.util.PathUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import manifold.internal.javac.IJavaParser;
import manifold.internal.javac.InMemoryClassJavaFileObject;
import manifold.util.NecessaryEvilUtil;


import static gw.lang.gosuc.simple.ICompilerDriver.ERROR;
import static gw.lang.gosuc.simple.ICompilerDriver.WARNING;

public class GosuCompiler implements IGosuCompiler
{
  // Use GosuClassTypeLoader.ALL_EXTS for Gosu files (.gs, .gsx, .gst, .gsp, .gr, .grs) plus .java
  private static final String[] SOURCE_EXTS = buildSourceExtensions();

  private static String[] buildSourceExtensions() {
    List<String> exts = new ArrayList<>( Arrays.asList( gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS ) );
    exts.add( ".java" );
    return exts.toArray( new String[0] );
  }

  private GosuInitialization _gosuInitialization;
  private File _compilingSourceFile;
  private IIncrementalCompilationManager _incrementalManager;

  @Override
  public boolean compile(CommandLineOptions options, ICompilerDriver driver) {
    List<String> allSourceFiles = getSourceFiles(options);
    if (!options.isIncremental()) {
      // Normal compilation - compile all sources
      return compileFilteredSources(allSourceFiles, options, driver);
    }

    // Extract source roots from sourcepath for FQCN computation
    List<String> sourceRoots = new ArrayList<>();
    String sourcepath = options.getSourcepath();
    if (sourcepath != null && !sourcepath.isEmpty()) {
      for (StringTokenizer tok = new StringTokenizer(sourcepath, File.pathSeparator); tok.hasMoreTokens(); ) {
        sourceRoots.add(tok.nextToken());
      }
    }

    _incrementalManager = GosuShop.createIncrementalCompilationManager(options.getDependencyFile(), sourceRoots,
            options.getLocalJavaTypes(), allSourceFiles, options.isVerbose());

    if (!new File(options.getDependencyFile()).exists()) {
      // First incremental compilation: compile all source files to build initial dependency file.
      if (options.isVerbose()) {
        System.out.println("Initial incremental compilation: compiling all " + allSourceFiles.size() + " source files");
      }
      boolean thresholdExceeded = compileFilteredSources(allSourceFiles, options, driver);
      // Don't persist the graph on a threshold abort: the compile stopped early, so
      // what was tracked is partial. Leave the (absent) dep file so the next run rebuilds from scratch.
      if (!driver.hasErrors() && !thresholdExceeded) {
        _incrementalManager.updateDependencyFile(Collections.emptySet(), Collections.emptySet());
      }
      return thresholdExceeded;
    }

    return compileGosuIncrementally(options, driver);
  }

  private boolean compileGosuIncrementally(CommandLineOptions options, ICompilerDriver driver) {
    // Get changed and removed type FQCNs from CLI
    Set<String> changedTypes = options.getChangedTypes();
    Set<String> removedTypes = options.getRemovedTypes();
    Set<String> localJavaTypes = options.getLocalJavaTypes();
    // Calculate types that need recompilation (returns FQCNs)
    Set<String> typeFqcnsToCompile = new HashSet<>();
    Set<String> sourceFilesCompiled = new HashSet<>();

    Set<String> visited = new HashSet<>();
    Queue<String> worklist = new ArrayDeque<>();

    // TODO: Non-transactional: deletion happens before the compiler runs. If compile then fails, the deleted
    // outputs are gone with no rollback. A future stash-and-restore step would close this gap.
    deleteClassAndSourceFiles(removedTypes, options.getDestDir(), options.isVerbose());
    deleteClasses(changedTypes, options.getDestDir(), options.isVerbose());

    // Seed the worklist with the union of changed and removed types.
    for (String changedType : changedTypes) {
      if (!visited.contains(changedType)) {
        visited.add(changedType);
        worklist.add(changedType);
      }
    }
    for (String removedType : removedTypes) {
      if (!visited.contains(removedType)) {
        visited.add(removedType);
        worklist.add(removedType);
      }
    }

    /*
      Note that the typeDependencies[X] give you all the types that consume/refer to X: if X is modified all types in
      typeDependencies[X] must be recompiled.
      This map reflects the status of the previously compiled .class files. The changedTypes/removedTypes are
      referring to source code changes, not yet reflected on the .class files.
      Given that source files X, Y, Z just changed, the below BFS tracks down the types whose .class are stale and need
      to be recompiled.
      Once the toRecompile files are recompiled, _incrementalManager.updateDependencyFile updates the dependency file to
      reflect the modified dependencies in changedTypes/removedTypes and synchronize with the new .class file on disk.
    */
    boolean thresholdExceeded = false;
    while (!worklist.isEmpty() && !thresholdExceeded) {
      String type = worklist.remove();

      // Only add if it's a Gosu type (not a known local Java type, java types are already compiled) and
      // it is not a removed type (no file to compile).
      if (!localJavaTypes.contains(type) && !removedTypes.contains(type)) {
        typeFqcnsToCompile.add(type);
        String sourceFile = _incrementalManager.getGosuFilePathFromFqcn(type);
        if (sourceFile == null) {
          // TODO: This should only happen if the dep graph carries stale entries in pathological situations, ex:
          //  - a previously-recorded Gosu consumer whose .gs file was deleted outside the Gradle change-set Gradle
          //    reported.
          //  - a dep file that survived an aborted compile.

          // Handle stale inner classes.
          // This can happen when Outer.gs contains an Outer class and an Inner one. When we delete Outer.gs and do an
          // incremental compilation, we still find a stale producer Outer$Inner in dep file.
          // See testOuterSourceRemovalRecordsExpectedDepFileAndDeletesStaleClassFiles().
          if (!type.contains("$")) {
            // TODO Probably it will be better to do a full rebuild instead of failing hard, but for now we want to debug
            // this failure when it happens.
            throw new IllegalStateException("Could not find source file for type " + type);
          }
        } else if (!sourceFilesCompiled.contains(sourceFile)) {
          sourceFilesCompiled.add(sourceFile);
          thresholdExceeded = compileGosuSource(options, driver, sourceFile);
        }
      }

      Set<String> consumers = _incrementalManager.getConsumersFor(type);
      for (String consumer : consumers) {
        if (!visited.contains(consumer)) {
          visited.add(consumer);
          worklist.add(consumer);
        }
      }
    }

    if (options.isVerbose()) {
      System.out.println("Incremental compilation: recompiled " + sourceFilesCompiled.size() + " source files:");
      for (String fqcn : sourceFilesCompiled) {
        System.out.println("  - " + fqcn);
      }
    }

    // Skip the dep-file write on any threshold abort: the loop exited early, so
    // typeFqcnsToCompile is partial and persisting it would corrupt the graph. Leaves the prior dep file intact.
    if (!driver.hasErrors() && !thresholdExceeded) {
      File destDir = new File(options.getDestDir());
      Set<String> effectivelyRemoved = new HashSet<>(removedTypes);
      for (String fqcn : typeFqcnsToCompile) {
        if (fqcn.contains("$")) {
          // If FQCN is an inner class and that inner class was deleted (the outer is part of removedTypes or the outer
          // source code no longer contains the inner), we need to add it to effectivelyRemoved so
          // that updateDependencyFile will correctly remove the inner class from the dep file as consumer and
          // producer.
          if (!classFileFor(destDir, fqcn).exists()) {
            effectivelyRemoved.add(fqcn);
          }
        }
      }
      _incrementalManager.updateDependencyFile(typeFqcnsToCompile, effectivelyRemoved);
    }
    return thresholdExceeded;
  }

  private boolean compileFilteredSources( List<String> sourceFiles, CommandLineOptions options, ICompilerDriver driver )
  {
    List<String> gosuFiles = new ArrayList<>();
    List<String> javaFiles = new ArrayList<>();
    for( String fileName : sourceFiles )
    {
      if( fileName.toLowerCase().endsWith( ".java" ) )
      {
        javaFiles.add( fileName );
      }
      else
      {
        gosuFiles.add( fileName );
      }
    }

    boolean thresholdExceeded = false;

    if( !gosuFiles.isEmpty() )
    {
      thresholdExceeded = compileGosuSources( options, driver, gosuFiles );
    }

    if( !javaFiles.isEmpty() )
    {
      thresholdExceeded = compileJavaSources( options, driver, javaFiles );
    }

    return thresholdExceeded;
  }

  private List<String> getSourceFiles( CommandLineOptions options )
  {
    List<String> sourceFiles = options.getSourceFiles();
    if( !sourceFiles.isEmpty() )
    {
      return sourceFiles;
    }

    String sourcepath = options.getSourcepath();
    if( sourcepath.isEmpty() )
    {
      return Collections.emptyList();
    }

    sourceFiles = new ArrayList<>();
    for( StringTokenizer tok = new StringTokenizer( File.pathSeparator ); tok.hasMoreTokens(); )
    {
      String path = tok.nextToken();
      Path sourcePath = PathUtil.create( path );
      addToSources( sourcePath, sourceFiles );
    }

    return sourceFiles;
  }

  private void addToSources( Path sourcePath, List<String> sourceFiles )
  {
    if( !PathUtil.exists( sourcePath ) )
    {
      return;
    }

    if( Files.isDirectory( sourcePath ) )
    {
      for( Path child : PathUtil.listFiles( sourcePath ) )
      {
        addToSources( child, sourceFiles );
      }
    }
    else
    {
      String absolutePathName = PathUtil.getAbsolutePathName( sourcePath );
      if( isSourceFile( absolutePathName ) )
      {
        sourceFiles.add( absolutePathName );
      }
    }
  }

  private boolean isSourceFile( String absolutePathName )
  {
    return Arrays.stream( SOURCE_EXTS ).anyMatch( e -> absolutePathName.toLowerCase().endsWith( e ) );
  }

  private boolean compileGosuSource( CommandLineOptions options, ICompilerDriver driver, String gosuFile ) {
    File file = new File( gosuFile );

    if( options.isVerbose() )
    {
      System.out.println( "gosuc: about to compile file: " + file );
    }

    compile( file, driver );

    if( driver.getErrors().size() > options.getMaxErrs() )
    {
      System.out.printf( "\nError threshold of %d exceeded; aborting compilation.", options.getMaxErrs() );
      return true;
    }
    if( !options.isNoWarn() && driver.getWarnings().size() > options.getMaxWarns() )
    {
      System.out.printf( "\nWarning threshold of %d exceeded; aborting compilation.", options.getMaxWarns() );
      return true;
    }
    return false;
  }

  private boolean compileGosuSources( CommandLineOptions options, ICompilerDriver driver, List<String> gosuFiles )
  {
    for( String fileName : gosuFiles )
    {
      if (compileGosuSource(options, driver, fileName)) {
        return true;
      }
    }
    return false;
  }

  private boolean compileJavaSources( CommandLineOptions options, ICompilerDriver driver, List<String> javaFiles )
  {
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    List<JavaFileObject> sourceFiles = javaFiles.stream().map( SourceJavaFileObject::new ).collect( Collectors.toList() );
    Collection<InMemoryClassJavaFileObject> files = javaParser.compile( sourceFiles, makeJavacOptions( options ), errorHandler );
    errorHandler.getDiagnostics().forEach( driver::sendCompileIssue );
    createJavaOutputFiles( files, driver );
    if( driver.getErrors().size() > options.getMaxErrs() )
    {
      System.out.printf( "\nError threshold of %d exceeded; aborting compilation.", options.getMaxErrs() );
      return true;
    }
    if( !options.isNoWarn() && driver.getWarnings().size() > options.getMaxWarns() )
    {
      System.out.printf( "\nWarning threshold of %d exceeded; aborting compilation.", options.getMaxWarns() );
      return true;
    }
    return false;
  }

  private List<String> makeJavacOptions( CommandLineOptions options )
  {
    ArrayList<String> javacOpts = new ArrayList<>();
    javacOpts.add( "-g" );
    javacOpts.add( "-source" );
    javacOpts.add( "8" );
    javacOpts.add( "-proc:none" );
    javacOpts.add( "-Xlint:unchecked" );
    javacOpts.add( "-parameters" );
    if( options.isVerbose() )
    {
      javacOpts.add( "-verbose" );
    }
    if( options.isNoWarn() )
    {
      javacOpts.add( "-nowarn" );
    }
    return javacOpts;
  }

  @Override
  public boolean compile( File sourceFile, ICompilerDriver driver )
  {
    _compilingSourceFile = sourceFile;

    IType type = getType( _compilingSourceFile );
    if( type == null )
    {
      driver.sendCompileIssue( _compilingSourceFile, ERROR, 0, 0, 0, "Cannot find type in the Gosu Type System." );
      return false;
    }

    if( isCompilable( type ) )
    {
      try
      {
        if( type.isValid() )
        {
          createGosuOutputFiles( (IGosuClass)type, driver );
        }
      }
      catch( CompilerDriverException ex )
      {
        driver.sendCompileIssue( _compilingSourceFile, ERROR, 0, 0, 0, ex.getMessage() );
        return false;
      }
      // output warnings and errors - whether the type was valid or not
      IParsedElement classElement = ((IGosuClass)type).getClassStatement();
      IClassFileStatement classFileStatement = ((IClassStatement)classElement).getClassFileStatement();
      classElement = classFileStatement == null ? classElement : classFileStatement;
      ExecutionMode mode = CommonServices.getPlatformHelper().getExecutionMode();
      for( IParseIssue issue : classElement.getParseIssues() )
      {
        int category = issue instanceof ParseWarning ? WARNING : ERROR;
        String message = mode == ExecutionMode.IDE ? issue.getUIMessage() : issue.getConsoleMessage();
        driver.sendCompileIssue( _compilingSourceFile, category, issue.getTokenStart(), issue.getLine(), issue.getColumn(), message );
      }
    }

    return true;
  }

  /**
   * Delete each type's outputs from {@code destDir}: the {@code <fqcn>.class} file, its nested
   * (inner / anonymous / block) {@code <fqcn>$*.class} files, and the source-file copy (any known
   * Gosu extension).
   *
   * <p>Called before incremental compile for removed types: the type is gone, so all of its outputs
   * -- top-level, nested classes, and the copied source -- must be cleared. Nested deletion is done
   * by {@link #deleteClassFile}'s {@code <fqcn>$*.class} glob.
   *
   * <p>No-op if {@code fqcns} is empty or {@code destDir} is null / blank.
   *
   * @param fqcns    FQCNs whose outputs should be deleted
   * @param destDir  output directory (e.g. {@code build/classes/gosu/main})
   * @param verbose  if true, log each deletion
   */
  private void deleteClassAndSourceFiles(Set<String> fqcns, String destDir, boolean verbose )
  {
    if( !fqcns.isEmpty() && destDir != null && !destDir.isEmpty())
    {
      File dest = new File( destDir );
      for( String fqcn : fqcns )
      {
        deleteClassFile( fqcn, dest, verbose );
        deleteSourceFile(fqcn, dest, verbose);
      }
    }
  }

  /**
   * Delete each type's outputs from {@code destDir}: the {@code <fqcn>.class}
   * file.
   *
   * Nested compiled units (inner / anonymous /
   * block .class files) are also deleted to prevent stale classes on disk.
   * <p>No-op if {@code fqcns} is empty or {@code destDir} is null / blank.
   *
   * @param fqcns    FQCNs whose outputs should be deleted
   * @param destDir  output directory (e.g. {@code build/classes/gosu/main})
   * @param verbose  if true, log each deletion
   */
  private void deleteClasses(Set<String> fqcns, String destDir, boolean verbose )
  {
    if( !fqcns.isEmpty() && destDir != null && !destDir.isEmpty())
    {
      File dest = new File( destDir );
      for( String fqcn : fqcns )
      {
        deleteClassFile( fqcn, dest, verbose );
      }
    }
  }


  /**
   * Resolve the {@code .class} file path for {@code fqcn} under {@code outputDir}.
   * Example: {@code outputDir=build/classes/gosu/main} and
   * {@code fqcn=com.example.Foo} -> {@code build/classes/gosu/main/com/example/Foo.class}.
   */
  private static File classFileFor(File outputDir, String fqcn) {
    return new File(outputDir, fqcn.replace('.', File.separatorChar) + ".class");
  }

  /**
   * Delete the .class file and any inner/anonymous outputs for the given type.
   *
   * @param fqcn The fully-qualified class name of the type to clean up
   * @param outputDir The output directory containing compiled .class files
   * @param verbose Whether to log deletion operations
   */
  private void deleteClassFile(String fqcn, File outputDir, boolean verbose )
  {
    File mainClassFile = classFileFor(outputDir, fqcn);

    if( verbose )
    {
      System.out.println( "Attempting to delete class file for removed type: " + fqcn );
      System.out.println( "  Output dir: " + outputDir );
      System.out.println( "  Target file: " + mainClassFile );
      System.out.println( "  File exists: " + mainClassFile.exists() );
    }

    if( mainClassFile.exists() )
    {
      if( mainClassFile.delete() )
      {
        if( verbose )
        {
          System.out.println( "Deleted stale class file: " + mainClassFile );
        }
      }
      else
      {
        System.err.println( "Warning: Failed to delete class file: " + mainClassFile );
      }
    }
    else if( verbose )
    {
      System.out.println( "Class file does not exist (may have already been deleted): " + mainClassFile );
    }

    // Delete inner/anonymous classes (Foo$*.class)
    File parentDir = mainClassFile.getParentFile();
    if( parentDir != null && parentDir.exists() )
    {
      String className = mainClassFile.getName().replace( ".class", "" );
      File[] innerClasses = parentDir.listFiles( (dir, name) ->
        name.startsWith( className + "$" ) && name.endsWith( ".class" )
      );

      if( innerClasses != null )
      {
        for( File innerClass : innerClasses )
        {
          if( innerClass.delete() )
          {
            if( verbose )
            {
              System.out.println( "Deleted stale inner class file: " + innerClass );
            }
          }
          else
          {
            System.err.println( "Warning: Failed to delete inner class file: " + innerClass );
          }
        }
      }
    }
  }

  /**
   * Deletes source file for the given type from the output directory.
   * Attempts to delete source files for all known Gosu file extensions.
   *
   * Since there's no way to determine which extension a FQCN originally had
   * (could be .gs, .gr, .grs, .gst, .gsp, or .gsx), we attempt deletion for
   * all known Gosu extensions. File.delete() is safe for non-existent files.
   *
   * @param fqcn The fully qualified name of the type
   * @param outputDir The output directory containing source files
   * @param verbose Whether to log deletion operations
   */
  private void deleteSourceFile(String fqcn, File outputDir, boolean verbose) {
    // Use official list from GosuClassTypeLoader: .gs, .gsx, .gsp, .gst, .gr, .grs
    String[] extensions = gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS;

    for (String extension : extensions) {
      deleteSourceFile(fqcn, extension, outputDir, verbose);
    }
  }

  /**
   * Deletes a specific source file from the output directory.
   *
   * @param fqcn The fully qualified name of the type
   * @param extension The file extension (e.g., ".gs", ".gr", etc.)
   * @param outputDir The output directory
   * @param verbose Whether to log deletion operations
   */
  private void deleteSourceFile(String fqcn, String extension, File outputDir, boolean verbose) {
    String relativePath = fqcn.replace('.', File.separatorChar);
    File sourceFile = new File(outputDir, relativePath + extension);

    if (sourceFile.exists()) {
      if (sourceFile.delete()) {
        if (verbose) {
          System.out.println("Deleted stale source file: " + sourceFile);
        }
      } else {
        System.err.println("Warning: Failed to delete source file: " + sourceFile);
      }
    }
  }

  private IType getType( File file )
  {
    IFile ifile = FileFactory.instance().getIFile( file );
    IModule module = TypeSystem.getGlobalModule();
    String[] typesForFile = TypeSystem.getTypesForFile( module, ifile );
    if( typesForFile.length != 0 )
    {
      return TypeSystem.getByFullNameIfValid( typesForFile[0], module );
    }
    return null;
  }

  private boolean isCompilable( IType type )
  {
    IType doNotVerifyAnnotation = TypeSystem.getByFullNameIfValid( "gw.testharness.DoNotVerifyResource" );
    return type instanceof IGosuClass && !type.getTypeInfo().hasAnnotation( doNotVerifyAnnotation );
  }

  private void createGosuOutputFiles( IGosuClass gsClass, ICompilerDriver driver )
  {
    IDirectory moduleOutputDirectory = TypeSystem.getGlobalModule().getOutputPath();
    if( moduleOutputDirectory == null )
    {
      throw new RuntimeException( "Can't make class file, no output path defined." );
    }

    final String outRelativePath = gsClass.getName().replace( '.', File.separatorChar ) + ".class";
    File child = new File( moduleOutputDirectory.getPath().getFileSystemPathString() );
    mkdirs( child );
    try
    {
      for( StringTokenizer tokenizer = new StringTokenizer( outRelativePath, File.separator + "/" ); tokenizer.hasMoreTokens(); )
      {
        String token = tokenizer.nextToken();
        child = new File( child, token );
        if( !child.exists() )
        {
          if( token.endsWith( ".class" ) )
          {
            createNewFile( child );
          }
          else
          {
            mkDir( child );
          }
        }
      }
      populateGosuClassFile( child, gsClass, driver );
      maybeCopySourceFile( child.getParentFile(), gsClass, _compilingSourceFile, driver );
    }
    catch( Throwable e )
    {
      driver.sendCompileIssue( _compilingSourceFile, ERROR, 0, 0, 0, combine( "Cannot create .class files.", getStackTrace( e ) ) );
    }
  }

  private void createJavaOutputFiles( Collection<InMemoryClassJavaFileObject> compiledJavaFiles, ICompilerDriver driver )
  {
    IDirectory moduleOutputDirectory = TypeSystem.getGlobalModule().getOutputPath();
    if( moduleOutputDirectory == null )
    {
      throw new RuntimeException( "Can't make class file, no output path defined." );
    }

    compiledJavaFiles = compiledJavaFiles.stream().filter( e -> TypeSystem.getByFullNameIfValid( e.getClassName().replace( '$', '.' ) ) instanceof IJavaType ).collect( Collectors.toList() );

    for( InMemoryClassJavaFileObject compiledJavaFile: compiledJavaFiles )
    {
      final String outRelativePath = compiledJavaFile.getClassName().replace( '.', File.separatorChar ) + ".class";
      File child = new File( moduleOutputDirectory.getPath().getFileSystemPathString() );
      mkdirs( child );
      try
      {
        for( StringTokenizer tokenizer = new StringTokenizer( outRelativePath, File.separator + "/" ); tokenizer.hasMoreTokens(); )
        {
          String token = tokenizer.nextToken();
          child = new File( child, token );
          if( !child.exists() )
          {
            if( token.endsWith( ".class" ) )
            {
              createNewFile( child );
            }
            else
            {
              mkDir( child );
            }
          }
        }
        populateJavaClassFile( child, compiledJavaFile.getBytes(), driver );
      }
      catch( Throwable e )
      {
        driver.sendCompileIssue( _compilingSourceFile, ERROR, 0, 0, 0, combine( "Cannot create .class files.", getStackTrace( e ) ) );
      }
    }
  }

  public static String getStackTrace( Throwable e )
  {
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace( new PrintWriter( stringWriter ) );
    return stringWriter.toString();
  }

  private String combine( String message1, String message2 )
  {
    if( message1 == null )
    {
      message1 = "";
    }
    else
    {
      message1 = message1 + "\n";
    }
    return message1 + message2;
  }

  private boolean mkDir( File file )
  {
    return file.mkdir();
  }

  private boolean mkdirs( File file )
  {
    return file.mkdirs();
  }

  private boolean createNewFile( File file ) throws IOException
  {
    return file.createNewFile();
  }

  private void maybeCopySourceFile( File parent, IGosuClass gsClass, File sourceFile, ICompilerDriver driver )
  {
    ISourceFileHandle sfh = gsClass.getSourceFileHandle();
    IFile srcFile = sfh.getFile();
    if( srcFile != null )
    {
      File file = new File( srcFile.getPath().getFileSystemPathString() );
      if( file.isFile() )
      {
        try
        {
          File destFile = new File( parent, file.getName() );
          copyFile( file, destFile );
          driver.registerOutput( _compilingSourceFile, destFile );
        }
        catch( IOException e )
        {
          e.printStackTrace();
          driver.sendCompileIssue( sourceFile, ERROR, 0, 0, 0, "Cannot copy source file to output folder." );
        }
      }
    }
  }

  public void copyFile( File sourceFile, File destFile ) throws IOException
  {
    if( sourceFile.isDirectory() )
    {
      mkdirs( destFile );
      return;
    }

    if( !destFile.exists() )
    {
      mkdirs( destFile.getParentFile() );
      createNewFile( destFile );
    }

    try( FileChannel source = new FileInputStream( sourceFile ).getChannel();
         FileChannel destination = new FileOutputStream( destFile ).getChannel() )
    {
      destination.transferFrom( source, 0, source.size() );
    }
  }

  private void populateGosuClassFile( File outputFile, IGosuClass gosuClass, ICompilerDriver driver ) throws IOException
  {
    final byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( gosuClass );
    try( OutputStream out = new FileOutputStream( outputFile ) )
    {
      out.write( bytes );
      driver.registerOutput( _compilingSourceFile, outputFile );
    }
    // Track dependencies if incremental compilation is enabled.
    if( _incrementalManager != null )
    {
       _incrementalManager.trackDependencies(bytes, gosuClass);
    }
    for( IGosuClass innerClass : gosuClass.getInnerClasses() )
    {
      final String innerClassName = String.format( "%s$%s.class", outputFile.getName().substring( 0, outputFile.getName().lastIndexOf( '.' ) ), innerClass.getRelativeName() );
      File innerClassFile = new File( outputFile.getParent(), innerClassName );
      if( innerClassFile.isFile() )
      {
        createNewFile( innerClassFile );
      }
      populateGosuClassFile( innerClassFile, innerClass, driver );
    }
  }

  private void populateJavaClassFile( File outputFile, byte[] bytes, ICompilerDriver driver ) throws IOException
  {
    try( OutputStream out = new FileOutputStream( outputFile ) )
    {
      out.write( bytes );
      driver.registerOutput( _compilingSourceFile, outputFile );
    }
  }

  @Override
  public long initializeGosu( List<String> sourceFolders, List<String> classpath, List<String> backingSourcePath, String outputPath )
  {
    NecessaryEvilUtil.bypassJava9Security();

    final long start = System.currentTimeMillis();

    CommonServices.getKernel().redefineService_Privileged( IFileSystem.class, createFileSystemInstance() );
    CommonServices.getKernel().redefineService_Privileged( IMemoryMonitor.class, new CompilerMemoryMonitor() );
    CommonServices.getKernel().redefineService_Privileged( IPlatformHelper.class, new CompilerPlatformHelper() );

    if( "gw".equals( System.getProperty( "compiler.type" ) ) )
    {
      try
      {
        IEntityAccess access = (IEntityAccess)Class.forName( "gw.internal.gosu.parser.gwPlatform.GWEntityAccess" ).newInstance();
        ICoercionManager coercionManager = (ICoercionManager)Class.forName( "gw.internal.gosu.parser.gwPlatform.GWCoercionManager" ).newInstance();
        CommonServices.getKernel().redefineService_Privileged( IEntityAccess.class, access );
        CommonServices.getKernel().redefineService_Privileged( ICoercionManager.class, coercionManager );
        Registry.instance().setAllowEntityQueires( true );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
    _gosuInitialization = GosuInitialization.instance( execEnv );
    GosucModule gosucModule = new GosucModule( IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME,
                                               sourceFolders,
                                               classpath,
                                               backingSourcePath,
                                               outputPath,
                                               Collections.<GosucDependency>emptyList(),
                                               Collections.<String>emptyList() );
    _gosuInitialization.initializeCompiler( gosucModule );

    return System.currentTimeMillis() - start;
  }

  private static IFileSystem createFileSystemInstance()
  {
    try
    {
      Class<?> cls = Class.forName( "gw.internal.gosu.module.fs.FileSystemImpl" );
      Constructor m = cls.getConstructor( IFileSystem.CachingMode.class );
      return (IFileSystem)m.newInstance( IFileSystem.CachingMode.FULL_CACHING );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void uninitializeGosu()
  {
    TypeSystem.shutdown( TypeSystem.getExecutionEnvironment() );
    if( _gosuInitialization != null )
    {
      if( _gosuInitialization.isInitialized() )
      {
        _gosuInitialization.uninitializeCompiler();
      }
      _gosuInitialization = null;
    }
  }

  @Override
  public boolean isPathIgnored( String sourceFile )
  {
    return CommonServices.getPlatformHelper().isPathIgnored( sourceFile );
  }

}
