/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.data.Public_Super_Class;
import gw.data.Public_TopLevel_Class;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.test.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 *
 * From The Java Specification
<pre>
6.5.5 Meaning of Type Names
The meaning of a name classified as a TypeName is determined as follows.

6.5.5.1 Simple Type Names
If a type name consists of a single Identifier, then the identifier must occur in the scope of a declaration of a type with this name, or a compile-time error occurs.
It is possible that the identifier occurs within the scope of more than one type with that name, in which case the type denoted by the name is determined as follows:


If the simple type name occurs within the scope of a visible local class declaration (§14.3) with that name, then the simple type name denotes that local class type.
Otherwise, if the simple type name occurs within the scope of exactly one visible member type (§8.5, §9.5), then the simple type name denotes that member type.
Otherwise, if the simple type name occurs within the scope of more than one visible member type, then the name is ambiguous as a type name; a compile-time error occurs.
Otherwise, if a type with that name is declared in the current compilation unit (§7.3), either by a single-type-import declaration (§7.5.1) or by a declaration of a class or interface type (§7.6), then the simple type name denotes that type.
Otherwise, if a type with that name is declared in another compilation unit (§7.3) of the package (§7.1) containing the identifier, then the identifier denotes that type.
Otherwise, if a type of that name is declared by exactly one type-import-on-demand declaration (§7.5.2) of the compilation unit containing the identifier, then the simple type name denotes that type.
Otherwise, if a type of that name is declared by more than one type-import-on-demand declaration of the compilation unit, then the name is ambiguous as a type name; a compile-time error occurs.
Otherwise, the name is undefined as a type name; a compile-time error occurs.
This order for considering type declarations is designed to choose the most explicit of two or more applicable type declarations.

6.5.5.2 Qualified Type Names
If a type name is of the form Q.Id, then Q must be either a type name or a package name. If Id names exactly one type that is a member of the type or package denoted by Q, then the qualified type name denotes that type. If Id does not name a member type (§8.5, §9.5) within Q, or the member type named Id within Q is not accessible (§6.6), or Id names more than one member type within Q, then a compile-time error occurs.
The example:

package wnj.test;
class Test {
	public static void main(String[] args) {
		java.util.Date date =
			new java.util.Date(System.currentTimeMillis());
		System.out.println(date.toLocaleString());
	}
}

produced the following output the first time it was run:

Sun Jan 21 22:56:29 1996

In this example the name java.util.Date must denote a type, so we first use the procedure recursively to determine if java.util is an accessible type or a package, which it is, and then look to see if the type Date is accessible in this package.
 </pre>
 */
public class JavaSourceClassTest extends TestClass {

  public void testSimple() throws Exception {
    IModule mod = TypeSystem.getExecutionEnvironment().getModule( "myModule" );

    //Public_TopLevel_Class
    JavaSourceType javaClassInfo = (JavaSourceType)TypeSystem.getJavaClassInfo( Public_TopLevel_Class.class.getName(), mod );
    verifyMembersOnClass( Public_TopLevel_Class.class, javaClassInfo );

    // Public_TopLevel_Class.Public_Inner_Class
    javaClassInfo = (JavaSourceType)javaClassInfo.getDeclaredClasses()[0];
    verifyMembersOnClass( Public_TopLevel_Class.Public_Inner_Class.class, javaClassInfo );
  }

  public void verifyMembersOnClass( Class cls, JavaSourceType javaClassInfo ) throws Exception
  {
    for( IJavaClassField field : javaClassInfo.getDeclaredFields() ) {
      IJavaClassInfo fieldType = field.getType();
      assertNotNull( "Field type not found for field: " + field.getName() + " on " + cls.getSimpleName(), field.getType() );
      Class<?> fieldClass = null;
      try {
        fieldClass = Public_TopLevel_Class.class.getDeclaredField( field.getName() ).getType();
      }
      catch( NoSuchFieldException nsfe ) {
        fail( "No field found on Class: " + field.getName() );
      }
      assertEquals( "Field: " + field.getName(), fieldClass.getName().replace( '$', '.' ), fieldType.getName() );
    }

    for( IJavaClassMethod method : javaClassInfo.getDeclaredMethods() ) {
      IJavaClassInfo returnType = method.getReturnClassInfo();
      Method javaMethod = cls.getDeclaredMethod( method.getName() );
      Class<?> returnClass = javaMethod.getReturnType();
      assertEquals( "Method: " + method.getName(), returnClass.getName().replace( '$', '.' ), returnType.getName() );
      int i = 0;
      for( IJavaClassInfo paramType : method.getParameterTypes() ) {
        for( Class paramClass : javaMethod.getParameterTypes() ) {
          assertEquals( "Method: " + javaMethod + " param: " + (i++), paramClass.getName().replace( '$', '.' ), paramType.getName() );
        }
      }
    }

    int i = 0;
    for( IJavaClassConstructor ctor : javaClassInfo.getDeclaredConstructors() ) {
      Constructor<?> javaCtor = cls.getDeclaredConstructors()[i++];
      for( IJavaClassInfo paramType : ctor.getParameterTypes() ) {
        int j = 0;
        for( Class paramClass : javaCtor.getParameterTypes() ) {
          assertEquals( "Constructor: " + javaCtor + " param: " + (j++), paramClass.getName().replace( '$', '.' ), paramType.getName() );
        }
      }
    }
  }

  public void testAnother() {
    IJavaType type = (IJavaType)TypeSystem.getByFullNameIfValid( Public_Super_Class.class.getName() );
    assertTrue( type.isValid() );
  }

}
