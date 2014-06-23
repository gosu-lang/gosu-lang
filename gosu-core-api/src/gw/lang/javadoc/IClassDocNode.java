/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaMethodDescriptor;

import java.lang.reflect.Type;
import java.util.List;

@UnstableAPI
public interface IClassDocNode {

  String getDescription();

  void setDescription( String value );

  String getDeprecated();

  boolean isDeprecated();

  void setDeprecated( String value );

  List<IMethodNode> getMethods();

  void addMethod( IMethodNode method );

  IMethodNode getMethod( IJavaMethodDescriptor name );

  IMethodNode getMethod( String name, Type[] parameterTypes );

  List<IConstructorNode> getConstructors();

  void addConstructor( IConstructorNode ctor );

  List<IVarNode> getVars();

  void addVar( IVarNode var );

  IVarNode getVar( String name );

  IConstructorNode getConstructor( IJavaClassConstructor ctor );

}
