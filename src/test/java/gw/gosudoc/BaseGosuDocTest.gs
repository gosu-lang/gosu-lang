package gw.gosudoc

uses com.example.bootstrap.Example
uses org.junit.BeforeClass

uses java.io.File
uses java.nio.file.Files

class BaseGosuDocTest {

  static var _OUT_DIR : File = null

  @BeforeClass
  static function initGosuDoc() {
    if(_OUT_DIR == null) {
      using ( BaseGosuDocTest as IMonitorLock) {
        if(_OUT_DIR == null){
          _OUT_DIR = genGosuDocForTests()
        }
      }
    }
  }


  /* ----------------------------------------------------------------------------
   * Init logic: generate GosuDoc for all classes in the com/example package
   * ---------------------------------------------------------------------------- */
  private static function genGosuDocForTests() : File {

    var out = Files.createTempDirectory( null, {} ).toFile()

    var htmlGenerator = new GSDocHTMLWriter() {
      :Output = out,
      :InputDirs = {new File('target/test-classes/com/example')}
    }

    htmlGenerator.write()

    return out
  }

  /* ----------------------------------------------------------------------------
   * Test Helpers
   * ---------------------------------------------------------------------------- */
  function gosuDocForType( example: Type ): File {
    return gosuDocForString( example.Name, example.Namespace )
  }

  function gosuDocForString( name: String, pkg : String = null ): File {
    if(pkg == null) {
      pkg = name.substring( name.lastIndexOf( '.' ) )
    }
    var fileName = pkg.split( "\\." ).join( File.separator ) + File.separator + name + ".html"
    var file = new File(_OUT_DIR, fileName )
    if(file.exists()) {
      return file
    } else {
      return null
    }
  }

}