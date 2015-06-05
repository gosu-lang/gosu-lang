package gw.gosudoc;

import gw.lang.Gosu;
import gw.lang.cli.CommandLineAccess;
import gw.lang.reflect.ReflectUtil;

import java.io.IOException;
import java.util.Arrays;

public class GosuDoc
{
  public static void main( String[] args ) throws IOException
  {
    Gosu.bootstrapGosuWhenInitiatedViaClassfile();
    Object gosuDocArgs = ReflectUtil.construct( "gw.gosudoc.cli.GosuDocArgs" );
    CommandLineAccess.setRawArgs( Arrays.asList(args) );
    CommandLineAccess.initialize( gosuDocArgs, true );
    ReflectUtil.construct( "gw.gosudoc.GSDocHTMLWriter", gosuDocArgs );
  }
}