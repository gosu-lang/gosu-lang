uses java.io.File

uses gw.lang.cli.CommandLineAccess
uses gw.gosudoc.GSDocHTMLWriter
uses gw.gosudoc.cli.GosuDocArgs

print("Starting GosuDoc...")
print("Args: ${CommandLineAccess.RawArgs.join( " " )}")

var args = new GosuDocArgs()
CommandLineAccess.initialize( args, true )

var writer = new GSDocHTMLWriter() {:Output = new File(args.Out) }
writer.write()