/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.config.CommonServices;
import gw.config.Registry;
import gw.lang.cli.CommandLineAccess;
import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.launch.*;
import gw.lang.mode.GosuMode;
import gw.lang.mode.IGosuMode;
import gw.lang.mode.RequiresInit;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.OSPlatform;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.StringTokenizer;

public class Gosu implements IGosuLaunch
{
  public static final IStringArgKey ARGKEY_FQN = Launch.factory().createArgKeyBuilder("loads a Gosu program based on a fully qualified name", "FQN")
          .withLongSwitch("fqn")
          .hideFromHelp()
          .build();
  public static final IBooleanArgKey ARGKEY_INTERACTIVE = Launch.factory().createArgKeyBuilder("starts an interactive Gosu shell")
          .withShortSwitch('i')
          .withLongSwitch("interactive")
          .build();
  public static final IBooleanArgKey ARGKEY_VERIFY = Launch.factory().createArgKeyBuilder("verifies the Gosu source")
          .withLongSwitch("verify")
          .build();
  public static final IBooleanArgKey ARGKEY_VERSION = Launch.factory().createArgKeyBuilder("displays the version of Gosu")
          .withLongSwitch("version")
          .build();
  public static final IBooleanArgKey ARGKEY_HELP = Launch.factory().createArgKeyBuilder("displays this command-line help")
          .withShortSwitch('h')
          .withLongSwitch("help")
          .build();
  private static List<? extends IArgKey> getArgKeys() {
    return Arrays.asList(
            LaunchArgs.FILE_PROGRAM_SOURCE,
            LaunchArgs.URL_PROGRAM_SOURCE,
            LaunchArgs.EVAL_PROGRAM_SOURCE,
            LaunchArgs.CLASSPATH,
            LaunchArgs.DEFAULT_PROGRAM_FILE,
            LaunchArgs.USE_TOOLS_JAR,
            ARGKEY_FQN,
            ARGKEY_INTERACTIVE,
            ARGKEY_VERIFY,
            ARGKEY_VERSION,
            ARGKEY_HELP
    );
  }

  private static List<File> _classpath;

  public static void main( String... args ) {
    IArgInfo argInfo = Launch.factory().createArgInfo( args );
    new Gosu().start( argInfo );
  }

  @Override
  public int start( IArgInfo argInfo )
  {
    try
    {
      String cpValue = argInfo.consumeArg( LaunchArgs.CLASSPATH );

      IGosuMode mode = getMode( argInfo );

      argInfo.processUnknownArgs();
      if (argInfo.getErrorMessage() != null) {
        System.err.println(argInfo.getErrorMessage());
        showHelp();
        System.exit(1);
      }

      if ( mode.getClass().isAnnotationPresent( RequiresInit.class ) ) {
        List<File> classpath = makeClasspath(cpValue);
        if (argInfo.getProgramSource() != null && argInfo.getProgramSource().getFile() != null) {
          classpath.addAll( initRegistry( argInfo.getProgramSource().getFile() ) );
        }
        init(classpath);
      }

      return mode.run();
    }
    catch( Throwable t )
    {
      t.printStackTrace( System.err );
      return 2;
    }
  }

  private static List<File> makeClasspath( String cpValue )
  {
    ArrayList<File> cp = new ArrayList<File>();
    if ( cpValue != null ) {
      StringTokenizer st = new StringTokenizer( cpValue, ",", false );
      while( st.hasMoreTokens() )
      {
        String s = st.nextToken();
        if( ( s.contains( ":" ) && !OSPlatform.isWindows()) || s.contains( ";" ) )
        {
          System.err.println( "WARNING: The Gosu classpath argument should be comma separated to avoid system dependencies.\n" +
                              "It appears you are passing in a system-dependent path delimiter" );
        }
        cp.add(new File(s));
      }
    }
    return cp;
  }

  // Note this is a giant hack, we need to instead get the type name from the psiClass
  private static String makeFqn( File file )
  {
    String path = file.getAbsolutePath();
    int iIndex = path.indexOf( "src" + File.separatorChar );
    String fqn = path.substring( iIndex + 4 ).replace( File.separatorChar, '.' );
    return fqn.substring( 0, fqn.lastIndexOf( '.' ) );
  }

  private static void printVerificationResults()
  {
    List<IVerificationResults> lst = verifyAllGosu(true, false);
    if( lst.size() > 0 )
    {
      for( IVerificationResults result : lst )
      {
        System.out.print( result.getTypeName() );
        System.out.println( ":" );
        System.out.println( result.getFeedback() );
      }
    }
    else
    {
      System.out.println( "No verification issues were found" );
    }
  }

  public static void setClasspath( List<File> classpath )
  {
    removeDups( classpath );

    if( classpath.equals( _classpath ) )
    {
      return;
    }

    _classpath = classpath;
    ClassLoader loader = TypeSystem.getCurrentModule() == null
                         // Can be null if called before the exec environment is setup, so assume the future parent of the module loader is the plugin loader
                         ? CommonServices.getEntityAccess().getPluginClassLoader()
                         : TypeSystem.getGosuClassLoader().getActualLoader();
    if( loader instanceof URLClassLoader )
    {
      for( File entry : classpath )
      {
        try
        {
          Method addURL = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
          addURL.setAccessible( true );
          addURL.invoke( loader, entry.toURI().toURL() );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
    }

    reinitGosu( classpath );
    TypeSystem.refresh( true );
  }

  private static void reinitGosu( List<File> classpath )
  {
    try
    {
      GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).reinitializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  private static void removeDups( List<File> classpath )
  {
    for( int i = classpath.size()-1; i >= 0; i-- )
    {
      File f = classpath.get( i );
      classpath.remove( i );
      if( !classpath.contains( f ) )
      {
        classpath.add( i, f );
      }
    }
  }

  /**
   * Initializes Gosu using the classpath derived from the current classloader and system classpath.
   */
  public static void init()
  {
    init( null );
  }

  public static void init( List<File> classpath )
  {

    List<File> combined = new ArrayList<File>();
    combined.addAll( deriveClasspathFrom( Gosu.class ) );
    if( classpath != null )
    {
      combined.addAll( classpath );
    }
    setClasspath(combined);
  }

  private static List<File> initRegistry(File sourceFile)
  {
    if( isProgramLikeResource( sourceFile ) )
    {
      File possibleReg = new File( sourceFile.getParentFile(), "registry.xml" );
      if( possibleReg.exists() )
      {
        try
        {
          Registry.setLocation( possibleReg.toURI().toURL() );
          ArrayList<File> files = new ArrayList<File>();
          List<String> entries = Registry.instance().getClasspathEntries();
          if( entries != null )
          {
            for( String entry : entries )
            {
              files.addAll(resolveFilesForPath( sourceFile, entry ) );
            }
          }
          return files;
        }
        catch( MalformedURLException e )
        {
          //ignore
        }
      }
    }

    // default
    Registry.setLocation( Gosu.class.getResource( "shell/registry.xml" ) );
    return Collections.emptyList();
  }

  private static boolean isProgramLikeResource(File sourceFile) {
    return sourceFile != null;
  }

  public static boolean bootstrapGosuWhenInitiatedViaClassfile()
  {
    if( GosuInitialization.isAnythingInitialized() &&
        GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).isInitialized() )
    {
      return false;
    }
    init();
    return true;
  }

  public static List<IVerificationResults> verifyAllGosu( boolean includeWarnings, boolean log )
  {
    List<String> sortedNames = getAllGosuTypeNames();
    ArrayList<IVerificationResults> errors = new ArrayList<IVerificationResults>();
    int count = 0;
    int i = 0;
    int cutoff = sortedNames.size() / 10;
    DecimalFormat decimalFormat = new DecimalFormat( "#0.0" );
    if( log )
    {
      System.out.println( "Verifying" );
    }
    for( Object o : sortedNames )
    {
      i++;
      count++;
      if( i > cutoff )
      {
        i = 0;
        if( log )
        {
          double v = (double)count * 100.0;
          System.out.println( decimalFormat.format( v / (double) sortedNames.size()) + "% done." );
        }
      }
      verifyType( includeWarnings, errors, o, (CharSequence)o );
    }
    return errors;
  }

  private static List<String> getAllGosuTypeNames()
  {
    List<String> sortedNames = new ArrayList<String>();
    for( CharSequence c : TypeSystem.getTypeLoader( GosuClassTypeLoader.class ).getAllTypeNames() )
    {
      String name = c.toString();
      if( !name.startsWith( "gw." ) )
      {
        sortedNames.add( name );
      }
    }
    return sortedNames;
  }

  private static void verifyType( boolean includeWarnings, ArrayList<IVerificationResults> errors, Object o, CharSequence typeName )
  {
    try
    {
      IType type = TypeSystem.getByFullNameIfValid( o.toString() );
      if( type instanceof ITemplateType )
      {
        ITemplateGenerator generator = ((ITemplateType) type).getTemplateGenerator();
        try {
          generator.verify(GosuParserFactory.createParser(null));
        } catch ( ParseResultsException e) {
          errors.add( new GosuTypeVerificationResults( typeName.toString(), e.getParseIssues() ) );
        }
      }
      else if( type instanceof IGosuClass )
      {
        boolean valid = type.isValid();
        List<IParseIssue> parseIssues = ((IGosuClass)type).getClassStatement().getParseIssues();
        if( parseIssues.size() > 0 && (!valid || includeWarnings) )
        {
          errors.add( new GosuTypeVerificationResults( typeName.toString(), parseIssues ) );
        }
      }
    }
    catch( Throwable e )
    {
      errors.add( new ExceptionTypeVerificationResults( typeName.toString(), e.getMessage() ) );
    }
  }

  static void showHelp() {
    showHelp(new PrintWriter(StreamUtil.getOutputStreamWriter(System.out)));
  }

  static void showHelp(PrintWriter out)
  {
    out.println("Usage:");
    out.println("        gosu [options] [program [args...]]" );
    out.println();
    out.println("Options:");

    IArgKeyList keys = Launch.factory().createArgKeyList();
    for (IArgKey key : getArgKeys()) {
      keys.register(key);
    }
    keys.printHelp(out);
  }

  private static List<File> resolveFilesForPath( File programFile, String strPath )
  {
    File file = null;
    //resolve relative paths relative to the executable, rather than the current working directory
    if( strPath.startsWith( "." ) )
    {
      String path = null;
      try
      {
        path = programFile.getCanonicalFile().getParentFile().getAbsolutePath() + File.separator + strPath;
        file = new File( path );
      }
      catch( IOException e )
      {
        throw new RuntimeException( "Could not resolve file relative to the executable with path \"" + path + "\"", e );
      }
    }

    //If it was not an obvious relative path, attempt to resolve it as an absolute path
    if( file == null )
    {
      file = new File( strPath );

      //If it is not an absolute path, try it as a (non-obvious) relative path
      if( !file.exists() )
      {
        String path = null;
        try
        {
          path = programFile.getCanonicalFile().getParentFile().getAbsolutePath() + File.separator + strPath;
          file = new File( path );
        }
        catch( IOException e )
        {
          throw new RuntimeException( "Could not resolve file relative to the executable with path \"" + path + "\"", e );
        }
      }
    }

    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
      //ignore
    }

    if( file.exists() && file.isDirectory() )
    {
      File[] files = file.listFiles( new FilenameFilter()
      {
        public boolean accept( File dir, String name )
        {
          return name.endsWith( ".jar" );
        }
      } );

      ArrayList<File> returnFiles = new ArrayList<File>();
      returnFiles.add( file );
      returnFiles.addAll( Arrays.asList( files ) );
      return returnFiles;
    }
    else
    {
      return file.exists() ? Arrays.asList( file ) : Collections.<File>emptyList();
    }
  }

  public static List<File> deriveClasspathFrom( Class clazz )
  {
    List<File> ll = new LinkedList<File>();
    ClassLoader loader = clazz.getClassLoader();
    while( loader != null )
    {
      if( loader instanceof URLClassLoader )
      {
        for( URL url : ((URLClassLoader)loader).getURLs() )
        {
          try
          {
            File file = new File( url.toURI() );
            if( file.exists() )
            {
              ll.add( file );
            }
          }
          catch( Exception e )
          {
            //ignore
          }
        }
      }
      loader = loader.getParent();
    }
    return ll;
  }

  public static List<File> getClasspath()
  {
    return _classpath;
  }

  public static GosuVersion getVersion()
  {
    InputStream in = Gosu.class.getClassLoader().getResourceAsStream(GosuVersion.RESOURCE_PATH);
    Reader reader = StreamUtil.getInputStreamReader(in);
    return GosuVersion.parse(reader);
  }

  public interface IVerificationResults
  {
    public String getFeedback();
    public String getTypeName();
  }

  private static class GosuTypeVerificationResults implements IVerificationResults
  {
    private final String _typeName;
    private final List<IParseIssue> _parseIssues;

    public GosuTypeVerificationResults( String typeName, List<IParseIssue> parseIssues )
    {
      _typeName = typeName;
      _parseIssues = parseIssues;
    }

    @Override
    public String getFeedback()
    {
      StringBuilder builder = new StringBuilder();
      List<ParseWarning> warnings = getWarnings();
      if( warnings.size() > 0 )
      {
        builder.append( "Warnings :\n\n" );
        for( ParseWarning warning : warnings )
        {
          builder.append( warning.getConsoleMessage() );
          builder.append( "\n" );
        }
      }
      List<ParseException> errors = getErrors();
      if( errors.size() > 0 )
      {
        builder.append( "Errors :\n\n" );
        for( ParseException error : errors )
        {
          builder.append( error.getConsoleMessage() );
          builder.append( "\n" );
        }
      }
      builder.append( "\n" );
      return builder.toString();
    }

    @Override
    public String getTypeName()
    {
      return _typeName;
    }

    public List<ParseWarning> getWarnings()
    {
      ArrayList<ParseWarning> warningArrayList = new ArrayList<ParseWarning>();
      for( IParseIssue parseIssue : _parseIssues )
      {
        if( parseIssue instanceof ParseWarning )
        {
          warningArrayList.add( (ParseWarning)parseIssue );
        }
      }
      return warningArrayList;
    }

    public List<ParseException> getErrors()
    {
      ArrayList<ParseException> warningArrayList = new ArrayList<ParseException>();
      for( IParseIssue parseIssue : _parseIssues )
      {
        if( parseIssue instanceof ParseException )
        {
          warningArrayList.add( (ParseException)parseIssue );
        }
      }
      return warningArrayList;
    }
  }

  private static class ExceptionTypeVerificationResults implements IVerificationResults
  {
    private final String _typeName;
    private final String _msg;

    public ExceptionTypeVerificationResults( String typeName, String msg )
    {
      _typeName = typeName;
      _msg = msg;
    }

    @Override
    public String getFeedback()
    {
      return _msg;
    }

    @Override
    public String getTypeName()
    {
      return _typeName;
    }
  }

  static IGosuMode getMode(IArgInfo argInfo) {

    List<IGosuMode> modes = new ArrayList<IGosuMode>();
    modes.add(new VersionMode());
    modes.add(new HelpMode());
    modes.add(new VerifyMode());
    modes.add(new ExecuteMode());

    ServiceLoader<IGosuMode> gosuModeLoader = ServiceLoader.load(IGosuMode.class);
    for (IGosuMode mode : gosuModeLoader) {
      modes.add(mode);
    }

    Collections.sort(modes);

    IGosuMode selectedMode = null;
    for (IGosuMode mode : modes) {
      mode.setArgInfo(argInfo);
      if (mode.accept()) {
        selectedMode = mode;
        break;
      }
    }
    if (selectedMode == null) {
      selectedMode = modes.get(0);
    }
    return selectedMode;
  }

  static class VersionMode extends GosuMode {
    @Override
    public int getPriority() {
      return GOSU_MODE_PRIORITY_VERSION;
    }

    @Override
    public boolean accept() {
      return _argInfo.consumeArg(ARGKEY_VERSION);
    }

    @Override
    public int run() {
      System.out.println(getVersion());
      return 0;
    }
  }

  static class HelpMode extends GosuMode {
    @Override
    public int getPriority() {
      return GOSU_MODE_PRIORITY_HELP;
    }

    @Override
    public boolean accept() {
      return _argInfo.consumeArg(ARGKEY_HELP);
    }

    @Override
    public int run() {
      showHelp();
      return 0;
    }
  }

  @RequiresInit
  static class VerifyMode extends GosuMode {
    @Override
    public int getPriority() {
      return GOSU_MODE_PRIORITY_VERIFY;
    }

    @Override
    public boolean accept() {
      return _argInfo.consumeArg(ARGKEY_VERIFY);
    }

    @Override
    public int run() {
      printVerificationResults();
      return 0;
    }
  }

  @RequiresInit
  public static class ExecuteMode extends GosuMode {
    private String _fqn = null;

    @Override
    public int getPriority() {
      return GOSU_MODE_PRIORITY_EXECUTE;
    }

    @Override
    public boolean accept() {
      _fqn = _argInfo.consumeArg(ARGKEY_FQN);
      return (_fqn != null && !_fqn.isEmpty()) || _argInfo.getProgramSource() != null;
    }

    public List<String> getProgramArgs() {
      return _argInfo.getArgsList();
    }

    public String getFqn() {
      return _fqn;
    }

    @Override
    public int run() throws Exception
    {
      if( _fqn != null && !_fqn.isEmpty() )
      {
        return runWithType();
      }
      else
      {
        return runWithFile();
      }
    }

    private int runWithType() throws IOException, ParseResultsException
    {
      CommandLineAccess.setRawArgs( _argInfo.getArgsList() );
      IGosuProgram program = (IGosuProgram)TypeSystem.getByFullName( _fqn );
      program.getProgramInstance().evaluate( null );
      return 0;
    }

    private int runWithFile() throws IOException, ParseResultsException
    {
      CommandLineAccess.setCurrentProgram( _argInfo.getProgramSource().getFile() );
      // set remaining arguments as arguments to the Gosu program
      CommandLineAccess.setRawArgs( _argInfo.getArgsList() );
      byte[] bytes = StreamUtil.getContent( _argInfo.getProgramSource().openInputStream() );
      String content = StreamUtil.toString( bytes );
      IFileContext ctx;
      if( _argInfo.getProgramSource().getFile() != null )
      {
        File file = _argInfo.getProgramSource().getFile();
        ctx = new ProgramFileContext( file, _fqn != null ? _fqn : makeFqn( file ) );
      }
      else
      {
        ctx = new IFileContext() {
          public String getClassName() {
            return _fqn != null ? _fqn : "program.TestProgram";
          }

          public String getFilePath() {
            return "TestProgram.gsp";
          }

          public String getContextString() {
            return null;
          }
        };
      }
      IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
      ParserOptions options = new ParserOptions().withFileContext( ctx );
      IParseResult result = programParser.parseExpressionOrProgram( content, new StandardSymbolTable( true ), options );
      IGosuProgram program = result.getProgram();
      Object ret = program.getProgramInstance().evaluate(null); // evaluate it
      IType expressionType = result.getType();
      if( expressionType != null && !JavaTypes.pVOID().equals(expressionType) )
      {
        GosuShop.print( ret );
      }
      return 0;
    }
  }
}
