package gw.lang.enhancements

uses java.net.URL
uses gw.util.StreamUtil
uses dynamic.Dynamic
uses gw.lang.reflect.json.Json

enhancement CoreUrlEnhancement : URL {
  property get TextContent(): String {
    using( var reader = StreamUtil.getInputStreamReader( this.openStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }

  property get JsonContent(): Dynamic {
    return Json.fromJson( TextContent )
  }
}