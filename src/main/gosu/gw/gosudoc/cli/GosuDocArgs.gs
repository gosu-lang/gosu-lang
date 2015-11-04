package gw.gosudoc.cli

uses gw.gosudoc.GSDocHTMLWriter
uses gw.lang.cli.*
uses gw.lang.reflect.ReflectUtil
uses gw.util.GosuStringUtil

uses java.io.File

class GosuDocArgs {

  private construct() {} //prevent instantiation
  
  @LongName( "in" ) @ShortName( "i" ) @ArgOptional
  static var _in : String as In

  @LongName( "out" ) @ShortName( "o" ) @ArgOptional
  static var _out : String as Out

  @LongName( "filters" ) @ShortName( "f" ) @ArgOptional
  static var _filters : String as FiltersString

  @LongName( "externalDocs" ) @ShortName( "e" ) @ArgOptional
  static var _externalDocs : String as ExternalDocs

  static function init( writer : GSDocHTMLWriter ) {
    writer.InputDirs = parseInputDirs()
    writer.Output = parseOutpuDir()
    writer.Filters = parseFilters()
    writer.ExternalDocs = parseExternalDocs()
  }

  static private function parseInputDirs() : List<File> {
    if(_in == null) {
      return { new File(".") }
    } else {
      return _in.split( "[;:]" ).map( \ s -> new File(s) ).toList()
    }
  }

  static private function parseOutpuDir() : File {
    if(_out == null) {
      return new File(".")
    } else {
      return new File(_out)
    }
  }

  static private function parseFilters() : List {
    if(GosuStringUtil.isBlank(_filters)) {
      return {}
    } else {
      return _filters.split( "," ).map( \ elt -> ReflectUtil.construct( elt, {} ) ).toList()
    }
  }

  static private function parseExternalDocs() : List<String> {
    if(GosuStringUtil.isBlank(_externalDocs)) {
      return {}
    } else {
      return _externalDocs.split(",").toList()
    }
  }
  
}