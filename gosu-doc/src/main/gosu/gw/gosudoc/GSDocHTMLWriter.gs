package gw.gosudoc

//uses com.sun.tools.javadoc.main.DocletInvoker
//uses com.sun.tools.javac.util.Context
//uses jdk.javadoc.internal.doclets.formats.html.HtmlDoclet
//uses com.sun.tools.javadoc.main.Messager
uses gw.gosudoc.doc.GSRootDocImpl

uses java.io.File
uses java.io.PrintWriter
uses java.nio.charset.StandardCharsets

class GSDocHTMLWriter {

  var _inputDirs: List<File> as InputDirs = {}
  var _output: File as Output
  var _filters: List as Filters = {}
  var _externalDocs : List<String> as ExternalDocs = {}
  var _verbose : Boolean as Verbose

  function write(){
    // Init output directory
    Output.mkdirs()
    if( not Output.Directory ){
      throw "Destination directory must be a valid directory path"
    }
    // Create Javadoc Data Structure
    var rootDoc = new GSRootDocImpl(InputDirs, Output, Filters, ExternalDocs, Verbose)
    rootDoc.printNotice( "Generating Documentation" )
    rootDoc.genDocs()
    rootDoc.printNotice( "Finished loading types:  now generating GosuDoc HTML to: ${Output.AbsolutePath}" )


/* todo: https://guidewirejira.atlassian.net/browse/ISGOSU-197
    // Generate HTML
    var doclet = new HtmlDoclet()
    doclet.Configuration.charset = StandardCharsets.UTF_8.toString()
    var context = new Context();
    Messager.preRegister( context, "Gosu API Javadocs" )
    var messenger = Messager.instance0( context )
    var docWriter = new DocletInvoker( messenger, com.sun.tools.doclets.standard.Standard as Class, true, false );
    docWriter.start( rootDoc )
*/
  }

}