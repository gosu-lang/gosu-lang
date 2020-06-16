/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import gw.fs.IFile;
import gw.util.cache.FqnCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import manifold.internal.javac.ClassSymbols;
import manifold.internal.javac.JavacPlugin;
import manifold.rt.api.util.Pair;


import static gw.lang.reflect.java.asm.AsmClass.Origin.ASM;
import static gw.lang.reflect.java.asm.AsmClass.Origin.Javac;

/**
 */
public class AsmClassLoader {
  private Object _module;
  private FqnCache<AsmClass> _cache;

  public AsmClassLoader( Object module ) {
    _module = module;
    _cache = new FqnCache<>();
  }

  public AsmClass findClassUsingAsm( String fqn, IFile file ) {
    AsmClass asmClass = getAsmClass( fqn );
    if( asmClass == null ) {
      try
      {
        asmClass = new AsmClass( _module, ASM );
        _cache.add( fqn, asmClass );
        asmClass.init( getContent( file.openInputStream() ) );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
    return asmClass;
  }

  public AsmClass findClassUsingJavac( String fqn ) {
    AsmClass asmClass = getAsmClass( fqn );
    if( asmClass == null ) {
      asmClass = new AsmClass( _module, Javac );
      _cache.add( fqn, asmClass );
      ClassSymbols classSymbols = ClassSymbols.instance( JavacPlugin.instance().getHost().getSingleModule() );
      BasicJavacTask javacTask = JavacPlugin.instance().getJavacTask();
      Pair<Symbol.ClassSymbol, JCTree.JCCompilationUnit> classSym = classSymbols.getClassSymbol( javacTask, fqn );
      if( classSym == null )
      {
        _cache.remove( fqn );
        return null;
      }
      asmClass.init( classSym.getFirst() );
    }
    return asmClass;
  }

  private AsmClass getAsmClass( String fqn )
  {
    AsmClass asmClass = _cache.get( fqn );
    if( asmClass == null && fqn.indexOf( '$' ) > 0 ) {
      asmClass = _cache.get( fqn.replace( '$', '.' ) );
    }
    return asmClass;
  }

  public AsmClass findClassUsingAsm( String fqn, File file ) {
    AsmClass asmClass = getAsmClass( fqn );
    if( asmClass == null ) {
      try
      {
        asmClass = new AsmClass( _module, ASM );
        _cache.add( fqn, asmClass );
        asmClass.init( getContent( new FileInputStream( file ) ) );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
    return asmClass;
  }

  private static byte[] getContent( InputStream is ) {
    byte[] buf = new byte[1024];
    ExposedByteArrayOutputStream out = new ExposedByteArrayOutputStream();
    while( true ) {
      int count;
      try {
        count = is.read( buf );
      }
      catch( IOException e ) {
        throw new RuntimeException( e );
      }
      if( count < 0 ) {
        break;
      }
      out.write( buf, 0, count );
    }
    try {
      out.flush();
      is.close();
      return out.getByteArray();
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public static class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
    public ExposedByteArrayOutputStream() {
      super( 1024 );
    }

    public byte[] getByteArray() {
      return buf;
    }
  }

}
