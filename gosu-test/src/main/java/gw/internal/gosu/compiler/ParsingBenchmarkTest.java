/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;


import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;

import java.util.HashMap;
import java.util.Set;

/**
 Total Classes: 1085
 START: Type system warm up...
 Parsing in full cold: 46.667s
 FINISHED: Type system warm up

 START: Type system refresh...
 FINISHED: Type system refresh
 START: Header parsing...
 FINISHED: Header parsing: 1.632s
 START: Declaration parsing...
 FINISHED: Declaration parsing: 2.607s
 START: Definition parsing...
 Finished: Definition parsing: 23.821s

 */
public class ParsingBenchmarkTest extends ByteCodeTestBase
{
  public void testParsingBenchmark() throws Exception
  {
//    ITypeLoader loader = TypeSystem.getTypeLoader( GosuClassTypeLoader.class );
//    Set<? extends CharSequence> allTypeNames = loader.getAllTypeNames();
//
//    System.out.println( "Total Classes: " + allTypeNames.size() );
//
//    //
//    // Full
//    //
//    TypeSystem.refresh();
//    long t = System.currentTimeMillis();
//    System.out.println( "START: Type system warm up..." );
//    for( CharSequence n : allTypeNames )
//    {
//      if( n.toString().contains( "Errant" ) )
//      {
//        continue;
//      }
//      IType type = TypeSystem.getByFullName( n.toString() );
//      type.isValid();
//    }
//
//    long delta = System.currentTimeMillis() - t;
//    System.out.println( "Parsing in full cold: " + delta / 1000.0 + "s" );
//    System.out.println( "FINISHED: Type system warm up\n" );
//
//
//    System.out.println( "START: Type system refresh..." );
//    TypeSystem.refresh();
//    System.out.println( "FINISHED: Type system refresh" );
//
//    //
//    // Header
//    //
//    t = System.currentTimeMillis();
//    System.out.println( "START: Header parsing..." );
//    for( CharSequence n : allTypeNames )
//    {
//      if( n.toString().contains( "Errant" ) )
//      {
//        continue;
//      }
//      IType type = TypeSystem.getByFullName( n.toString() );
//      ((IGosuClassInternal)type).compileHeaderIfNeeded();
//    }
//
//    delta = System.currentTimeMillis() - t;
//    System.out.println( "FINISHED: Header parsing: " + delta / 1000.0 + "s" );
//
//    //
//    // Declaration
//    //
//    t = System.currentTimeMillis();
//    System.out.println( "START: Declaration parsing..." );
//    for( CharSequence n : allTypeNames )
//    {
//      if( n.toString().contains( "Errant" ) )
//      {
//        continue;
//      }
//      IType type = TypeSystem.getByFullName( n.toString() );
//      ((IGosuClassInternal)type).compileDeclarationsIfNeeded();
//    }
//
//    delta = System.currentTimeMillis() - t;
//    System.out.println( "FINISHED: Declaration parsing: " + delta / 1000.0 + "s" );
//
//    //
//    // Declaration
//    //
//    t = System.currentTimeMillis();
//    System.out.println( "START: Definition parsing..." );
//    for( CharSequence n : allTypeNames )
//    {
//      if( n.toString().contains( "Errant" ) )
//      {
//        continue;
//      }
//      IType type = TypeSystem.getByFullName( n.toString() );
//      type.isValid();
//    }
//
//    delta = System.currentTimeMillis() - t;
//    System.out.println( "Finished: Definition parsing: " + delta / 1000.0 + "s" );
//
////    //
////    // Full
////    //
////    TypeSystem.refresh();
////    t = System.currentTimeMillis();
////    System.out.println( "START: Full parsing..." );
////    for( CharSequence n : allTypeNames )
////    {
////      if( n.toString().contains( "Errant" ) )
////      {
////        continue;
////      }
////      IType type = TypeSystem.getByFullName( n.toString() );
////      type.isValid();
////    }
////
////    delta = System.currentTimeMillis() - t;
////    System.out.println( "FINISHED: Full parsing: " + delta / 1000.0 + "s" );
  }
}
