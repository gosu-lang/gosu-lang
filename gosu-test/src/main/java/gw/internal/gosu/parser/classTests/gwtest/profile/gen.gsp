uses java.io.File


var dir = new File( "C:/eng/pl/carbon/active/entity/platform/gosu-test/src/gw/internal/gosu/parser/classTests/gwtest/profile/lots" )
dir.mkdirs()
for( i in 0..|10000 )
{
  var gsFile = new File( dir, "Class_" + i + ".gs" )
  var source = 
    "package gw.internal.gosu.parser.classTests.gwtest.profile.lots\n" +
    "\n" +
    "uses java.lang.Runnable\n" +
    "\n" +
    "class Class_" + i + "{\n" +
     "  var _field1 : String as Field1\n" +
     "  var _field2 : int as Field2\n" +
     "  var _field3 : double as readonly Field3\n" +
     "\n" +
     "  construct() {\n" +
     "    _field1 = \"hello\"\n" + 
     "    _field2 = 1\n" + 
     "    _field3 = 2 as double\n" + 
     "  }\n" +
     "  function foo() : String {\n" + 
     "    return Field1\n" + 
     "  }\n" + 
     "\n" +      
     "  property get Runner() : Runnable {\n" + 
     "    return new InnerClass()\n" + 
     "  }\n" + 
     "\n" +      
     "  class InnerClass implements Runnable {\n" +      
     "    override void run() {\n" +
     "    }\n" +      
     "  }\n" +      
     "}\n"
   gsFile.write( source )
   print( i )
}
