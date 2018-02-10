package gw.specContrib.typemanifolds;

import gw.lang.reflect.ClassLazyTypeResolver;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import junit.framework.TestCase;

public class JavaAccessGosu extends TestCase
{
  public void testGosuAccessViaStub()
  {
    assertEquals( MyGosu._static_string, "_static_string" );
    assertEquals( MyGosu._static_int, 5 );
    assertEquals( MyGosu._static_stingArray[0], "a" );
    assertEquals( MyGosu._static_stingArray[1], "b" );
    assertEquals( MyGosu._static_stringList, Arrays.asList( "b", "c" ) );
    assertEquals( MyGosu._static_stringMap, map( "hi", MyGosu._static_stringMap.get( "hi" ) ) );
  
    assertEquals( MyGosu._static_internal, "_static_internal" );
    assertEquals( MyGosu._static_protected, "_static_protected" );
    assertEquals( MyGosu._static_public, "_static_public" );
    assertEquals( MyGosu._static_private_final, "_static_private_final" );

    assertEquals( MyGosu.getStatic_NewVarProp(), "Static_NewVarProp" );
  
    assertEquals( MyGosu.getStatic_OldVarProp(), "Static_OldVarProp" );
  
    assertEquals( MyGosu.getStatic_NewPropBoth(), "Static_NewPropBoth" );
    MyGosu.setStatic_NewPropBoth( "setStatic_NewPropBoth" );
    assertEquals( MyGosu.getStatic_NewPropBoth(), "setStatic_NewPropBoth" );
   
    assertEquals( MyGosu.getStatic_NewPropGet(), "Static_NewPropGet" );
    MyGosu.setStatic_NewPropSet( "setStatic_NewPropSet" );
  
    assertEquals( MyGosu.static_publicFunction(), "static_publicFunction" );
    assertEquals( MyGosu.static_protectedFunction(), "static_protectedFunction" );
    assertEquals( MyGosu.static_internalFunction(), "static_internalFunction" );
    //assertEquals( MyGosu.static_privateFunction(), "static_internalFunction" );
  
    assertEquals( new MyGosu.Static_PublicInnerClass<String, String, String>().hi( "hi" ), "hi" );
    assertEquals( new MyGosu.Static_PublicInnerClass<String, String, String>( ClassLazyTypeResolver.String, ClassLazyTypeResolver.String, ClassLazyTypeResolver.String ).hi( "hi" ), "hi" );
    assertEquals( new MyGosu.Static_ProtectedInnerClass<String, String, String>().one( "hi" ), "hi" );
    assertEquals( new MyGosu.Static_InternalInnerClass<String, String, String>().two( "hi" ), "hi" );

    MyGosu mg = new MyGosu();

    assertEquals( mg._string, "_string" );
    assertEquals( mg._int, 6 );
    assertEquals( mg._stingArray[0], "x" );
    assertEquals( mg._stingArray[1], "y" );
    assertEquals( mg._stringList, Arrays.asList( "g", "s" ) );
    assertEquals( mg._stringMap, map( "one", 1 ) ); 
  
    assertEquals( mg._internal, "_internal" ); 
    assertEquals( mg._protected, "_protected" );
    assertEquals( mg._public, "_public" );
  
    assertEquals( mg.getNewVarProp(), "NewVarProp" );
  
    assertEquals( mg.getOldVarProp(), "OldVarProp" );
  
    assertEquals( mg.getNewPropBoth(), "NewPropBoth" );
    mg.setNewPropBoth( "setNewPropBoth" );
    assertEquals( mg.getNewPropBoth(), "setNewPropBoth" );
    
    assertEquals( mg.getNewPropGet(), "NewPropGet" );
  
    assertEquals( mg.publicFunction(), "publicFunction" );
    assertEquals( mg.protectedFunction(), "protectedFunction" );
    assertEquals( mg.internalFunction(), "internalFunction" );
  
    assertEquals( mg.allTheThings( null ), null );
  
    assertNotNull( new MyGosu.PublicStructure() { public void foo( String s, int t ) {} } );

    mg.run();

    assertEquals( Arrays.asList( "hi" ), mg.generic( "hi" ) );

    new Outer();
  }

  static class Outer extends MyGosu
  {
    class ExtendsGosu extends MyGosu.Protected_InnerClass<String, StringBuilder, Object>
    {

    }
  }

  public static <K, V> Map<K, V> map( K key, V value )
  {
    Map<K,V> map = new HashMap<>();
    map.put( key, value );
    return map;
  }
}