package gw.lang.enhancements

uses java.net.URL
uses gw.util.StreamUtil
uses java.io.InputStreamReader
uses dynamic.Dynamic
uses gw.lang.reflect.json.Json

enhancement CoreUrlEnhancement : URL {
  property get TextContent(): String {
    using( var reader = new InputStreamReader( this.openStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }

  property get JsonContent(): Dynamic {
    return Json.fromJson( TextContent )
  }
}