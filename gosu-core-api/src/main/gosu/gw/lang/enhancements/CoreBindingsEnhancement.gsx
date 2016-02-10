package gw.lang.enhancements

uses javax.script.Bindings
uses dynamic.Dynamic
uses gw.lang.reflect.json.Json

enhancement CoreBindingsEnhancement : Bindings {
  /**
   * Makes a tree of structure types reflecting the Bindings.  The structure types are intended to be placed in a .gs file or inner type
   *<p>
   * A structure type contains a property member for each name/value pair in the Bindings.  A property has the same name as the key and follows these rules:
   * <ul>
   *   <li> If the type of the value is a "simple" type, such as a String or Integer, the type of the property matches the simple type exactly
   *   <li> Otherwise, if the value is a Bindings type, the property type is that of a child structure with the same name as the property and recursively follows these rules
   *   <li> Otherwise, if the value is a List, the property is a List parameterized with the component type, and the component type recursively follows these rules
   * </ul>
   */
  function toStructure( nameForStructure: String ) : String {
    return Json.makeStructureTypes( nameForStructure, this )
  }

  /**
   * Serializes this Bindings instance to a JSON formatted String
   */
  public function toJson() : String {
    var sb = new StringBuilder()
    toJson( sb, 0 )
    return sb.toString()
  }

  /**
   * Serializes this Bindings instance into a JSON formatted StringBuilder with the specified indent of spaces
   */
  public function toJson( sb: StringBuilder, indent: int ) : void {
    var iKey = 0
    indent( sb, indent )
    if( this.size() > 0 ) {
      sb.append(" {\n")
      for( key in this.keySet() ) {
        indent( sb, indent + 2 )
        sb.append( '\"' ).append( key ).append( '\"' ).append( ": " )
        var value : Object = this.get( key )
        if( value typeis Bindings ) {
          value.toJson(sb, indent + 2)
        }
        else if( value typeis List ) {
          handleJsonList( sb, indent, value )
        }
        else {
          appendGosuValue( sb, value )
        }
        appendCommaNewLine( sb, iKey < this.size() - 1 )
        iKey++
      }
    }
    indent( sb, indent )
    sb.append( "}\n" )
  }

  private function handleJsonList( sb: StringBuilder, indent: int, value: List ) {
    var length_0 : int = value.size()
    sb.append( '[' )
    if( length_0 > 0 ) {
      sb.append( "\n" )
      var iSize : int = value.size()
      var i = 0
      while( i < iSize ) {
        var comp : Object = value.get(i)
        if( comp typeis Bindings ) {
          comp.toJson(sb, indent + 4)
        }
        else if( comp typeis List ) {
          handleJsonList( sb, indent + 4, value )
        }
        else {
          indent(sb, indent + 4)
          appendGosuValue( sb, comp )
        }
        appendCommaNewLine( sb, i < iSize - 1 )
        i++
      }
    }
    indent( sb, indent + 2 )
    sb.append( "]" )
  }

  private function appendCommaNewLine( sb: StringBuilder, bComma: boolean ) {
    if( bComma ) {
      sb.append( ',' )
    }
    sb.append( "\n" )
  }

  private function indent( sb : StringBuilder, indent : int ) {
    var i = 0
    while (i < indent) {
      sb.append(' ')
      i++
    }
  }

  private function appendGosuValue( sb: StringBuilder, comp: Object) : StringBuilder {
    if( comp typeis String ) {
      sb.append( '\"' )
      sb.append( gw.util.GosuEscapeUtil.escapeForGosuStringLiteral( comp ) )
      sb.append( '\"' )
    }
    else if( comp typeis Integer ||
             comp typeis Long ||
             comp typeis Double ||
             comp typeis Float ||
             comp typeis Short ||
             comp typeis Character ||
             comp typeis Byte ) {
      sb.append( comp )
    }
    else if( comp == null ) {
      sb.append( "null" )
    }
    else {
      throw new IllegalStateException( "Unsupported expando type: " + comp.getClass() )
    }
    return sb
  }
}