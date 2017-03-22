/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;
import gw.testharness.DoNotVerifyResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class VerifyAllPureGosuResourcesTest extends TestClass
{
  @Override
  public void beforeTestClass()
  {
    TypeSystem.refresh( true );
  }

  @Override
  public void afterTestClass()
  {
    TypeSystem.refresh( true );
  }

  public void testAllResourcesAreValid() {
    List<String> names = TypeSystem.getAllTypeNames().stream().map( Object::toString ).sorted().collect( Collectors.toList() );
    List<String> badTypes = new ArrayList<>();
    for( String name : names )
    {
      if( name.contains( "Errant_" ) ||
          name.endsWith( ".PLACEHOLDER" ) )
      {
        continue;
      }

      try
      {
        IType type = TypeSystem.getByFullName( name );
        if( !type.getTypeInfo().hasAnnotation( TypeSystem.get( DoNotVerifyResource.class ) ) )
        {
          if( !type.isValid() )
          {
            System.out.println( "Parse Error in " + name );
            if( type instanceof IGosuClass )
            {
              System.out.println( "-------------------------------------------" );
              //noinspection ThrowableResultOfMethodCallIgnored
              ((IGosuClass)type).getParseResultsException().getParseExceptions().forEach( e -> System.out.println( "  " + e.getConsoleMessage() ) );
              System.out.println( "-------------------------------------------" );
            }
            badTypes.add( name );
          }
          else if( type instanceof IGosuClass )
          {
            // Compile gosu classes

            try
            {
              ((IGosuClass)type).getBackingClass();
            }
            catch( Throwable e )
            {
              System.out.println( "Catastrophe while compiling " + name );
              System.out.println( "-------------------------------------------" );
              System.out.println( "  " + e.getMessage() );
              System.out.println( "-------------------------------------------" );

              badTypes.add( name );
            }
          }
        }
      }
      catch( Exception e )
      {
        System.out.println( "Catastrophe while parsing " + name );
        System.out.println( "-------------------------------------------" );
        System.out.println( "  " + e.getMessage() );
        System.out.println( "-------------------------------------------" );

        badTypes.add( name );
      }
    }
    System.out.println( "\n\n" );
    if( !badTypes.isEmpty() )
    {
      System.out.println( "Found " +  badTypes.size() + " bad types:" );
      for( String badType : badTypes )
      {
        System.out.println( "    * " + badType );
      }
    }
    assertTrue( badTypes.isEmpty() );
  }
}
