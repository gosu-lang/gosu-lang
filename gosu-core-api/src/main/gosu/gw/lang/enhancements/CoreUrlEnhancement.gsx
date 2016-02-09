package gw.lang.enhancements

uses java.net.URL
uses gw.util.StreamUtil
uses java.io.InputStreamReader

enhancement CoreUrlEnhancement : URL {
  property get TextContent(): String {
    using( var reader = new InputStreamReader( this.openStream() ) ) {
      return StreamUtil.getContent( reader )
    }
  }
}