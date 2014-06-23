package gw.lang.enhancements
uses gw.test.TestClass
uses java.io.File
uses java.util.ArrayList
uses java.io.FileNotFoundException
uses java.lang.NullPointerException
uses gw.lang.parser.EvaluationException

class CoreFileEnhancementTest extends TestClass
{
  function testReadAndWrite() {
    var file = File.createTempFile( "tmp", "txt" )
    file.write( "Это is a test" )
    assertEquals( "Это is a test", file.read() ) 
  }
  
  function testReadAndWriteBytes() {
    var file = File.createTempFile( "tmp", "txt" )
    var arr = new byte[]{1, 2, 3}
    file.writeBytes( arr )
    var arr2 = file.readBytes()
    assertEquals( arr.length, arr2.length )
    for( b in arr index i ) {
      assertEquals( b, arr2[i] )
    }
  }

  function testCopyTo() {
    var f = File.createTempFile( "tmp", "txt" )
    var contents = "Это - Тест"
    f.write( contents )
    assertEquals( contents, f.read() )
    
    var otherFile = File.createTempFile( "tmp", "txt" )
    assertEquals( "", otherFile.read() )
    f.copyTo( otherFile )
    assertEquals( contents, otherFile.read() )    
  }
  
  function testContainsText() {
    var file = File.createTempFile( "tmp", "txt" )
    file.write( "Это is a test" )
    assertTrue( file.containsText( "Это" ))
    assertTrue( file.containsText( "is" ))
    assertTrue( file.containsText( "a" ))
    assertTrue( file.containsText( "test" ))
    assertTrue( file.containsText( ".*test" ))
    assertTrue( file.containsText( "test.*" ))
     
    assertFalse( file.containsText( "Zod" ) )
    assertFalse( file.containsText( ".*Zod" ) )
    assertFalse( file.containsText( "Zod.*" ) )
    assertFalse( file.containsText( ".*Zod.*" ) )
  }

  function testDeleteRecursively() {
    var f = File.createTempFile( "foo", ".tmp" )
    f.deleteRecursively()
    assertFalse( f.exists() ) 
  }

  function testExtensionProperty() {
    var f = File.createTempFile( "foo", ".tmp" )
    assertEquals( "tmp", f.Extension )
    f = File.createTempFile( "foo", ".baz" )
    assertEquals( "baz", f.Extension )
  }

  function testEachLine() {
    var f = File.createTempFile( "foo", ".tmp" )
    f.write( "this\nis\na\ntest" )
    var strs = {}
    f.eachLine( \ s -> strs.add( s ) )
    assertEquals( {"this", "is", "a", "test"}, strs )
  }
  
  function testDiffersFrom() {
    var f1 = File.createTempFile( "foo", ".tmp" )
    var f2 = File.createTempFile( "foo", ".tmp" )
    var f3 = File.createTempFile( "foo", ".tmp" )

    // these contents should vary in length
    f1.write( "foo" )
    f2.write( "foobar" )
    f3.write( "doh foobar" )

    assertDiffers( f1, f2, f3, 1, 2, 3 )

    // these contents should not vary in length
    f1.write( "foo" )
    f2.write( "bar" )
    f3.write( "doh" )
    
    assertDiffers( f1, f2, f3, 1, 2, 3 )

    //make all the files the same  
    f2.write( "foo" )
    f3.write( "foo" )

    assertDiffers( f1, f2, f3, 1, 1, 1 )

    f2.delete()
    
    try
    {
      f1.differsFrom( f2 )
      fail( "Expected FileNotFoundException" )
    }
    catch ( e : FileNotFoundException )
    {
      // good
    }

    try
    {
      f2.differsFrom( f1 )
      fail( "Expected FileNotFoundException" )
    }
    catch ( e : FileNotFoundException )    
    {
      // good
    }

    try
    {
      f2.differsFrom( f2 )
      fail( "Expected FileNotFoundException" )
    }
    catch ( e : FileNotFoundException )
    {
      // good
    }
    
    try
    {
      f2.differsFrom( null )
      fail( "Expected NullPointerException" )
    }
    catch ( e : NullPointerException )
    {
      // good
    }
      
  }
  
  private function assertDiffers( f1 : File, f2 : File, f3 : File, i1 : int, i2 : int, i3 : int )
  {
    assertDiffers( f1, f2, i1, i2 )
    assertDiffers( f2, f3, i2, i3 )
    assertDiffers( f1, f3, i1, i3 )
  }
  
  private function assertDiffers( f1 : File, f2 : File, i1 : int, i2 : int )
  {
    assertEquals( i1 != i2, f1.differsFrom( f2 ) )
    assertEquals( i1 != i2, f2.differsFrom( f1 ) )
  }
  
  function testUsingTempFile() {
    var f1 : File
    File.usingTempFile( \ f -> {
      assertTrue( f.exists() )
      assertTrue( f.File )
      assertFalse( f.Directory )
      f.write( "This is a test" )
      assertEquals( "This is a test", f.read())
      f1 = f 
    })
    assertFalse( f1.exists() )
  }
 
  function testFileExtension () {
    for( prefix in { "", "./", "../", "asdf/", "asdf/asdf/", "/asdf/", "/asdf/asdf/"} ) {
      assertEquals( "", new File(prefix + "").Extension ) 
      assertEquals( "", new File(prefix + ".").Extension ) 
      assertEquals( "", new File(prefix + "..").Extension )
      assertEquals( "foo", new File(prefix + "foo.foo").Extension )
      assertEquals( "foo", new File(prefix + ".foo").Extension )
      assertEquals( "bar", new File(prefix + "foo.foo.bar").Extension )
    }
  }

  function testNameSansExtension () {
    for( prefix in { "", "./", "../", "asdf/", "asdf/asdf/", "/asdf/", "/asdf/asdf/"} ) {
      assertEquals( ".", new File(prefix + ".").NameSansExtension ) 
      assertEquals( "..", new File(prefix + "..").NameSansExtension )
      assertEquals( "foo", new File(prefix + "foo.foo").NameSansExtension )
      assertEquals( "", new File(prefix + ".foo").NameSansExtension )
      assertEquals( "foo.foo", new File(prefix + "foo.foo.bar").NameSansExtension )
    }
  }
  
}
