package gw.internal.gosu.parser;

import gw.util.GosuExceptionUtil;

public class GosuCompilationFailure extends RuntimeException
{
  public GosuCompilationFailure( Throwable t, IGosuClassInternal gsClass, int ln )
  {
    super( "Compilation failure. " +
           GosuExceptionUtil.findExceptionCause( t ).getClass().getTypeName() +
           ":[" + gsClass.getName() + ":" + ln + "]", t );
  }
}
