package gw.lang.enhancements

uses java.io.InputStream
uses gw.util.StreamUtil
uses java.io.InputStreamReader

enhancement CoreInputStreamEnhancement : InputStream {
  property get TextContent(): String {
    using( var reader = new InputStreamReader( this ) ) {
      return StreamUtil.getContent( reader )
    }
  }
}