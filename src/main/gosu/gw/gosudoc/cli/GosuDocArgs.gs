package gw.gosudoc.cli

uses gw.gosudoc.GSDocHTMLWriter
uses gw.lang.cli.*
uses gw.lang.reflect.ReflectUtil

uses java.io.File

class GosuDocArgs {

  @LongName( "in" ) @ShortName( "i" ) @ArgOptional
  var _in : String as In

  @LongName( "out" ) @ShortName( "o" ) @ArgOptional
  var _out : String as Out

  @LongName( "filters" ) @ShortName( "f" ) @ArgOptional
  var _filters : String as FiltersString

  @LongName( "externalDocs" ) @ShortName( "e" ) @ArgOptional
  var _externalDocs : String as ExternalDocs

  function init( writer : GSDocHTMLWriter ){
    writer.InputDirs = parseInputDirs()
    writer.Output = parseOutpuDir()
    writer.Filters = parseFilters()
    writer.ExternalDocs = _externalDocs.split( "," ).toList()
  }

  private function parseInputDirs() : List<File> {
    if(_in == null) {
      return { new File(".") }
    } else {
      return _in.split( "[;:]" ).map( \ s -> new File(s) ).toList()
    }
  }

  private function parseOutpuDir() : File {
    if(_out == null) {
      return new File(".")
    } else {
      return new File(_out)
    }
  }

  private function parseFilters() : List {
    if(_filters == null or _filters.isEmpty()) {
      return {}
    } else {
      return _filters.split( "," ).map( \ elt -> ReflectUtil.construct( elt, {} ) ).toList()
    }
  }

}