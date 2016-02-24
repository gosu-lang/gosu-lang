package gw.lang.enhancements

uses java.io.InputStream
uses gw.util.StreamUtil

enhancement CoreInputStreamEnhancement : InputStream {
  property get TextContent(): String {
    using( var reader = StreamUtil.getInputStreamReader( this ) ) {
      return StreamUtil.getContent( reader )
    }
  }
}