/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ITemplateType;
import gw.test.TestClass;
import gw.testharness.Disabled;
import gw.testharness.DoNotVerifyResource;
import gw.util.GosuStringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 */
public class VerifyAllPureGosuResourcesTest extends TestClass
{
  @Disabled(assignee = "cgross", reason = "Ugh.  Finds pl resources on the path in th...")
  public void testAllResourcesAreValid() {
    ArrayList<String> names = new ArrayList<String>();
    names.addAll( (Set)TypeSystem.getAllTypeNames() );
    Collections.sort( names );
    List<String> badTypes = new ArrayList<String>();
    for( String name : names )
    {
      try
      {
        IType iType = TypeSystem.getByFullName( name );
        if( !iType.isValid() && !iType.getTypeInfo().hasAnnotation( TypeSystem.get( DoNotVerifyResource.class ) ) && !iType.getRelativeName().startsWith( "Errant_" ) )
        {
          System.out.println( "Error in " + name );
          if( iType instanceof IGosuClass )
          {
            System.out.println( "-------------------------------------------" );
            ParseResultsException resultsException = ((IGosuClass)iType).getParseResultsException();
            System.out.println( "  " + GosuStringUtil.join( GosuStringUtil.split( resultsException.getFeedback(), "\n" ), "    " ) );
            System.out.println( "-------------------------------------------" );
          }
          badTypes.add( name );
        }
      }
      catch( Exception e )
      {
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
