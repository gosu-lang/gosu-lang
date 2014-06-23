/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRClass;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.SyntheticIRType;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.reflect.IType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

@UnstableAPI
public class IRClassBuilder extends IRFeatureBuilder<IRClassBuilder> {

  private IRType _thisType;
  private String _name;
  private IRType _superType;
  private List<IRMethodStatement> _methods = new ArrayList<IRMethodStatement>();
  private List<IRFieldDecl> _fields = new ArrayList<IRFieldDecl>();
  private List<IRType> _interfaces = new ArrayList<IRType>();

  public IRClassBuilder( String name, Class superType ) {
    _name = name;
    _superType = getIRType( superType );
    String relativeName = name.substring( name.lastIndexOf('.') + 1 );
    _thisType = new SyntheticIRType( superType, name, relativeName );
    // Set up some defaults
    this._public();
  }

  public IRType getThisType() {
    return _thisType;
  }

  public List<IRFieldDecl> getFields() {
    return _fields;
  }

  public List<IRMethodStatement> getMethods() {
    return _methods;
  }

  public IRType getSuperType() {
    return _superType;
  }

  public IRMethodBuilder createMethod() {
    return new IRMethodBuilder(this);
  }
  
  public IRMethodBuilder createConstructor() {
    return new IRMethodBuilder(this).name("<init>").returns(IRTypeConstants.pVOID());
  }

  public void addDefaultConstructor() {
    new IRMethodBuilder(this)._public().name("<init>").returns(IRTypeConstants.pVOID()).body(
            IRBuilderMethods._superInit(),
            IRBuilderMethods._return()
    );
  }

  public IRFieldBuilder createField() {
    return new IRFieldBuilder(this);
  }

  public IRClassBuilder withField( IRFieldDecl field ) {
    _fields.add( field );
    return this;
  }

  public IRClassBuilder withMethod( IRMethodStatement method ) {
    _methods.add( method );
    return this;
  }

  public IRClassBuilder withInterface( Class type ) {
    return withInterface( getIRType( type ) );
  }

  public IRClassBuilder withInterface( IJavaClassInfo type ) {
    return withInterface( getIRType( type ) );
  }

  public IRClassBuilder withInterface( IType type ) {
    return withInterface( getIRType( type ) );
  }

  public IRClassBuilder withInterface( IRType type ) {
    _interfaces.add( type );
    return this;
  }

  public IRClassBuilder asAbstract() {
    _modifiers |= Modifier.ABSTRACT;
    return this;
  }

  public IRClassBuilder asInterface() {
    asAbstract();
    _modifiers |= Modifier.INTERFACE;
    return this;
  }

  public IRClass build() {
    IRClass irClass = new IRClass();
    irClass.setName( _name );
    irClass.setSuperType( _superType );
    irClass.setThisType( _thisType );
    irClass.setModifiers( _modifiers );

    for ( IRType iface : _interfaces ) {
      irClass.addInterface( iface );
    }

    for ( IRFieldDecl field : _fields ) {
      irClass.addField( field );
    }

    for ( IRMethodStatement method : _methods ) {
      irClass.addMethod( method );
    }

    return irClass;
  }

  public Class define( ClassLoader classLoader ) {
    IRClass irClass = build();
    byte[] bytes = GosuShop.getIRClassCompiler().compile(irClass, false);
    return defineClassInLoader( classLoader, irClass.getName(), bytes );
  }

  private Class defineClassInLoader( ClassLoader classLoader, String name, byte[] bytes ) {
    try {
      Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
      method.setAccessible(true);
      return (Class) method.invoke(classLoader, name, bytes, 0, bytes.length);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
