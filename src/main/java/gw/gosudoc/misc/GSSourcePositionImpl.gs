package gw.gosudoc.misc

uses com.sun.javadoc.SourcePosition

uses java.io.File

class GSSourcePositionImpl implements SourcePosition{

  var _file : File as File
  var _line : int as Line
  var _column : int as Column
  
  override function file(): File{
    return _file
  }

  override function line(): int{
    return _line
  }

  override function column(): int{
    return _column
  }

  override function toString() : String {
    return "${_file.Name}:${Line} column ${Column}"
  }
}