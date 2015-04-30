package gw.gosudoc

uses com.sun.tools.doclets.formats.html.HtmlDoclet
uses gw.gosudoc.doc.GSRootDocImpl

uses java.io.File

class GSDocHTMLWriter {

  var _output: File as Output
  var _config: File as ConfigFile

  function write(){

    // Init output directory
    Output.mkdirs()
    if( not Output.Directory ){
      throw "Destination directory must be a valid directory path"
    }

    // Create Javadoc Data Structure
    var rootDoc = new GSRootDocImpl(Output)
    if(ConfigFile != null) {
      rootDoc.initWithPropertiesFile( ConfigFile )
    }
    rootDoc.printNotice( "Using configuration file ${ConfigFile}" )
    rootDoc.genDocs()
    rootDoc.printNotice( "Finished loading types:  now generating GosuDoc HTML to: ${Output.AbsolutePath}" )

    // Generate HTML
    var doclet = new HtmlDoclet()
    doclet.configuration.charset = "utf-8"
    doclet.start( doclet, rootDoc )
  }

}