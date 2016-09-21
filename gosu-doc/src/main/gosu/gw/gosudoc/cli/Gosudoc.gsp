uses gw.config.CommonServices
uses gw.gosudoc.cli.CommandLineOptions
uses gw.gosudoc.GSDocHTMLWriter
uses gw.lang.init.ClasspathToGosuPathEntryUtil
uses gw.lang.init.GosuInitialization
uses gw.lang.reflect.TypeSystem
uses gw.internal.ext.com.beust.jcommander.JCommander
uses gw.internal.ext.com.beust.jcommander.ParameterException

uses java.io.File
uses java.net.URLClassLoader
uses java.net.URL

var options = new CommandLineOptions()
var args = Gosu.RawArgs.toTypedArray()
var help : JCommander

try {
  help = new JCommander(options, args)
} catch (e : ParameterException) {
  print("Error: " + e.Message)
  System.exit(1)
}

if(args.length == 0 or options.Help) {
  //dump the summary when gosudoc is called w/o any args
  help.setProgramName("gosudoc")
  help.usage()
  print("In addition, the @<filename> syntax may be used to read the above options from a file.")
  System.exit(0)
}

var writer = new GSDocHTMLWriter()

injectClasspathIntoLoader(options.Classpath)

writer.InputDirs = options.InputDirs
writer.Output = options.Output
writer.Filters = options.Filters
writer.ExternalDocs = options.ExternalDocs
writer.Verbose = options.Verbose

writer.write()

/**
 * get options.Classpath and inject into type system
 */
private function injectClasspathIntoLoader( classpath: List<File> )
{
  for( entry in classpath )
  {
    try
    {
      var addURL = (URLClassLoader as Class).getDeclaredMethod( "addURL", { URL } )
      addURL.setAccessible( true )
      addURL.invoke( CommonServices.EntityAccess.PluginClassLoader, { entry.toURI().toURL() } )
    }
    catch( e : Exception )
    {
      throw new RuntimeException( e )
    }
  }

  //reinit gosu
  try
  {
    GosuInitialization.instance( TypeSystem.ExecutionEnvironment ).reinitializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ), {} )
  }
  catch( e : Exception )
  {
    e.printStackTrace()
  }
  TypeSystem.refresh( true )
}