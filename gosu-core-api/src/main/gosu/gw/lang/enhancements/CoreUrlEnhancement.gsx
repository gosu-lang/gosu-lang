package gw.lang.enhancements

uses java.net.URL
uses java.net.URLEncoder
uses java.net.HttpURLConnection
uses java.io.InputStreamReader
uses java.io.BufferedReader
uses manifold.rt.api.Bindings
uses gw.util.StreamUtil
uses gw.lang.reflect.json.Json

enhancement CoreUrlEnhancement : URL {
  /**
   * Make a JSON-friendly URL with the arguments from the Bindings.
   * <p>
   * If an argument is a Gosu Dynamic Expando or a manifold.rt.api.Bindings or a List,
   * it is transformed to JSON.  Otherwise, the argument is coerced to a String.  All
   * arguments are URL encoded.
   * <p>
   * Note the resulting URL is intended to be used for an http GET invocation via the
   * TextContent and JsonContent properties. Do not use the resulting URL for a POST
   * invocation, instead separately construct a URL and call postForTextContent() or
   * postForJsonContent().
   * <p>
   * @see #TextContent
   * @see #JsonContent
   * @see #postForTextContent(manifold.rt.api.Bindings)
   * @see #postForJsonContent(manifold.rt.api.Bindings)
   */
  static function makeUrl( url: String, arguments: Bindings ) : URL {
    if( arguments.size() > 0 ) {
      url += '?'
    }
    return new URL( url + makeArguments( arguments ) )
  }

  /**
   * Fetch the content of this URL as a String.  If this is an http URL,
   * fetches the content using the GET method.
   *
   * @return The full content of this URL coerced to a String.
   */
  property get TextContent(): String {
    using( var reader = StreamUtil.getInputStreamReader( this.openStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }

  /**
   * Fetch the content of this URL as a JSON object.  If this is an http URL,
   * fetches the content using the GET method.
   *
   * @return If the content of this URL is a JSON document, a JSON object reflecting the document.
   *
   * @see #TextContent
   * @see #postForJsonContent(manifold.rt.api.Bindings)
   */
  property get JsonContent(): Dynamic {
    return Json.fromJson( TextContent )
  }

  /**
   * Use http POST to pass arguments and get the full content of this URL as a String.
   * <p>
   * If an argument is a Gosu Dynamic Expando or a manifold.rt.api.Bindings or a List,
   * it is transformed to JSON.  Otherwise, the argument is coerced to a String.  All
   * arguments are URL encoded.
   *
   * @return The full content of this URL coerced to a String.
   *
   * @see #postForJsonContent(manifold.rt.api.Bindings)
   * @see #TextContent
   * @see #JsonContent
   */
  function postForTextContent( arguments : Bindings ) : String {
    var bytes = makeArguments( arguments ).getBytes( "UTF-8" )
    var conn = this.openConnection() as HttpURLConnection
    conn.RequestMethod = "POST"
    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" )
    conn.setRequestProperty( "Content-Length", String.valueOf( bytes.length ) )
    conn.DoOutput = true
    using( var writer = conn.OutputStream ) {
      writer.write( bytes )
    }
    using( var reader = StreamUtil.getInputStreamReader( conn.getInputStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }

  /**
   * Use http POST to pass arguments and get the full content of this URL as a JSON object.
   * <p>
   * If an argument is a Gosu Dynamic Expando or a manifold.rt.api.Bindings or a List,
   * it is transformed to JSON.  Otherwise, the argument is coerced to a String.  All
   * arguments are URL encoded.
   *
   * @return The full content of this URL's stream as a JSON object.
   *
   * @see #postForTextContent(manifold.rt.api.Bindings)
   * @see #TextContent
   * @see #JsonContent
   */
  function postForJsonContent( arguments: Bindings ) : Dynamic {
    return Json.fromJson( postForTextContent( arguments ) )
  }

  private static function makeArguments( arguments: Bindings ) : String {
    var sb = new StringBuilder()
    for( entry in arguments.entrySet() ) {
      if( sb.length() != 0 ) {
        sb.append( '&' )
      }
      sb.append( URLEncoder.encode( entry.Key, "UTF-8" ) )
      .append( '=' )
      .append( makeValue( entry.Value ) )
    }
    return sb.toString()
  }

  private static function makeValue( value: Object ) : String {
    if( value typeis Bindings ) {
      value = value.toJson()
    }
    else if( value typeis List ) {
      value = Bindings.listToJson( value )
    }
    return URLEncoder.encode( value as String, "UTF-8" )
  }
}