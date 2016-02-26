package gw.lang.enhancements

uses java.net.URL
uses java.io.InputStreamReader
uses java.net.URLEncoder
uses javax.script.Bindings
uses gw.util.StreamUtil
uses gw.lang.reflect.json.Json

enhancement CoreUrlEnhancement : URL {
  /**
   * Make a JSON-compatible URL with the arguments from the Bindings. URL encodes
   * the arguments in UTF-8 and appends them to the list using standard URL query
   * delimiters.
   * <p>
   * If an argument is a Gosu Dynamic Expando or a javax.script.Bindings or a List,
   * it is transformed to JSON.  Otherwise, the argument is coerced to a String and
   * URL encoded.
   */
  static function makeUrl( url: String, arguments: Bindings ) : URL {
    var sb = new StringBuilder()
    for( entry in arguments.entrySet() ) {
      sb.append( sb.length() == 0 ? '?' : '&' )
      .append( entry.Key )
      .append( '=' )
      var value = entry.Value
      if( value typeis Bindings ) {
        value = value.toJson()
      }
      else if( value typeis List ) {
        value = Bindings.listToJson( value )
      }
      value = URLEncoder.encode( value as String, "UTF-8" )
      sb.append( value )
    }
    return new URL( url + sb )
  }

  /**
   * @return The full content of this URL's stream coerced to a String.
   */
  property get TextContent(): String {
    using( var reader = StreamUtil.getInputStreamReader( this.openStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }

  /**
   * @return If the content of this URL is a JSON document, a JSON object reflecting the document.
   *
   * @see gw.lang.reflect.json.Json#fromJson(String)
   */
  property get JsonContent(): Dynamic {
    return Json.fromJson( TextContent )
  }
}