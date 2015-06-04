package gw.gosudoc;

import gw.lang.Gosu;
import gw.lang.cli.CommandLineAccess;
import gw.lang.reflect.ReflectUtil;

import java.io.IOException;

public class GosuDoc
{
  public static void main( String[] args ) throws IOException
  {
    Gosu.init();
    Object gosuDocArgs = ReflectUtil.construct( "gw.gosudoc.cli.GosuDocArgs" );
    CommandLineAccess.initialize( gosuDocArgs, true );
    ReflectUtil.construct( "gw.gosudoc.GSDocHTMLWriter", gosuDocArgs );
  }
}