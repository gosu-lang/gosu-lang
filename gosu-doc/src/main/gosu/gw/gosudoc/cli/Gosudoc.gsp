uses gw.gosudoc.cli.CommandLineOptions
uses gw.gosudoc.GSDocHTMLWriter
uses gw.internal.ext.com.beust.jcommander.JCommander
uses gw.internal.ext.com.beust.jcommander.ParameterException

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

writer.InputDirs = options.InputDirs
writer.Output = options.Output
writer.Filters = options.Filters
writer.ExternalDocs = options.ExternalDocs
writer.Verbose = options.Verbose

writer.write()