uses java.util.regex.Pattern
uses java.nio.file.Files
uses com.sun.tools.doclets.formats.html.HtmlDoclet
uses gw.gosudoc.doc.GSRootDocImpl
uses java.io.File
uses gw.gosudoc.*

    // Init output directory
var out = Files.createTempDirectory(null, {}).toFile()

// Create Javadoc Data Structure
var rootDoc = new GSRootDocImpl(out);

rootDoc.EntryPoints.add( Pattern.compile( "^" + "com\\.example\\.bootstrap.*" + "$" ) )

rootDoc.genDocs()

// Generate HTML
var doclet = new HtmlDoclet()
doclet.configuration.charset = "utf-8"
doclet.start( doclet, rootDoc )
