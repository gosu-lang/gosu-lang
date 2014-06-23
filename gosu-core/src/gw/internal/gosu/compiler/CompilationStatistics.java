/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.parser.IBlockClassInternal;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.ICompilableType;

public class CompilationStatistics
{
  long _totalBytes;
  long _blockBytes;
  long _programBytes;
  long _otherBytes;

  long _total;
  long _block;
  long _program;
  long _other;

  long _totalBytesPermanent;
  long _blockBytesPermanent;
  long _programBytesPermanent;
  long _otherBytesPermanent;

  long _totalPermanent;
  long _blockPermanent;
  long _programPermanent;
  long _otherPermanent;

  private static CompilationStatistics INSTANCE = new CompilationStatistics();

  private CompilationStatistics(){}

  public static CompilationStatistics instance() {
    return INSTANCE;
  }


  public void collectStats( ICompilableType gsClass, byte[] classBytes, boolean singleServingClassLoader )
  {
    if( !BytecodeOptions.collectCompilationStatistics() )
    {
      return;
    }
    long length = classBytes.length;
    if( gsClass instanceof IBlockClassInternal )
    {
      _blockBytes += length;
      _block++;
      if( !singleServingClassLoader )
      {
        _blockBytesPermanent += length;
        _blockPermanent++;
      }
    }
    else if( gsClass.getName().startsWith( IGosuProgram.PACKAGE ) )
    {
      _programBytes += length;
      _program++;
      if( !singleServingClassLoader )
      {
        _programBytesPermanent += length;
        _programPermanent++;
      }
    }
    else
    {
      _otherBytes += length;
      _other++;
      if( !singleServingClassLoader )
      {
        _otherBytesPermanent += length;
        _otherPermanent++;
      }
    }
    _totalBytes += length;
    _total++;
    if( !singleServingClassLoader ) {
      _totalBytesPermanent += length;
      _totalPermanent++;
    }

    System.out.println( "    * Class : " + gsClass.getName() + ", size : " + length );
    System.out.println( "    * Defined in SingleServingClassLoader : " + singleServingClassLoader );
    System.out.println( "      * Bytes = total : " + _totalBytes + ", blocks : " + _blockBytes + ", programs : " + _programBytes + ", other : " + _otherBytes );
    System.out.println( "      * Count = total : " + _total + ", blocks : " + _block + ", programs : " + _program + ", other : " + _other );
    System.out.println( "      * Bytes (Permanent) = total : " + _totalBytesPermanent + ", blocks : " + _blockBytesPermanent + ", programs : " + _programBytesPermanent + ", other : " + _otherBytesPermanent );
    System.out.println( "      * Count (Permanent) = total : " + _totalPermanent + ", blocks : " + _blockPermanent + ", programs : " + _programPermanent + ", other : " + _otherPermanent );
  }
}
