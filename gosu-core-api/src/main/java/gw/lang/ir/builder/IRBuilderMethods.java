/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.ir.IRType;
import gw.lang.ir.builder.expression.IRClassLiteralBuilder;
import gw.lang.ir.builder.expression.IRCompositeExpressionBuilder;
import gw.lang.ir.builder.expression.IRFieldGetExpressionBuilder;
import gw.lang.ir.builder.expression.IRIdentifierExpressionBuilder;
import gw.lang.ir.builder.expression.IRMethodCallExpressionBuilder;
import gw.lang.ir.builder.expression.IRNewArrayExpressionBuilder;
import gw.lang.ir.builder.expression.IRNewExpressionBuilder;
import gw.lang.ir.builder.expression.IRNullLiteralBuilder;
import gw.lang.ir.builder.expression.IRNumericLiteralBuilder;
import gw.lang.ir.builder.expression.IRStringLiteralBuilder;
import gw.lang.ir.builder.statement.IRArrayStoreStatementBuilder;
import gw.lang.ir.builder.statement.IRAssignmentStatementBuilder;
import gw.lang.ir.builder.statement.IRFieldSetStatementBuilder;
import gw.lang.ir.builder.statement.IRIfStatementBuilder;
import gw.lang.ir.builder.statement.IRReturnStatementBuilder;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UnstableAPI
public class IRBuilderMethods {

  // ==========================================================================
  // ------------------------ Expression Builders -----------------------------
  // ==========================================================================

  // ------------------------ Get Field Expression ----------------------------

  public static IRFieldGetExpressionBuilder field(String name) {
    return new IRFieldGetExpressionBuilder( _this(), name );
  }

  // ------------------------ Identifier Expression ---------------------------

  public static IRIdentifierExpressionBuilder var(String name) {
    return new IRIdentifierExpressionBuilder(new IRSymbolBuilder(name));
  }

  public static IRIdentifierExpressionBuilder var(IRSymbolBuilder symbol) {
    return new IRIdentifierExpressionBuilder( symbol );
  }

  // ------------------------ Literal Expressions -----------------------------

  public static IRNumericLiteralBuilder literal( Number value ) {
    return new IRNumericLiteralBuilder( value );
  }

  public static IRClassLiteralBuilder literal( Class cls ) {
    return new IRClassLiteralBuilder( getIRType( cls ) );
  }

  public static IRExpressionBuilder literal(String value) {
    return new IRStringLiteralBuilder(value);
  }

  // ------------------------ Method Calls ------------------------------------

  public static IRMethodCallExpressionBuilder call( String name, IRExpressionBuilder... args ) {
    return call( _this(), name, args );
  }

  public static IRMethodCallExpressionBuilder call( IJavaClassMethod method, IRExpressionBuilder... args ) {
    return call( _this(), method, args );
  }

  public static IRMethodCallExpressionBuilder callStatic( IJavaClassMethod method, IRExpressionBuilder... args ) {
    return call( null, method, args );
  }

  public static IRMethodCallExpressionBuilder call( IRExpressionBuilder root, String name, IRExpressionBuilder... args ) {
    return new IRMethodCallExpressionBuilder( root, name, Arrays.asList(args) );
  }

  public static IRMethodCallExpressionBuilder call( IRExpressionBuilder root, IJavaClassMethod method, IRExpressionBuilder... args ) {
    return new IRMethodCallExpressionBuilder( root, method, Arrays.asList(args) );
  }

  // ------------------------ New Array Creation ------------------------------

  public static IRCompositeExpressionBuilder newArray( Class componentType, List<IRExpressionBuilder> values ) {
    return newArray( getIRType( componentType ), values );
  }

  public static IRCompositeExpressionBuilder newArray( IType componentType, List<IRExpressionBuilder> values ) {
    return newArray( getIRType( componentType ), values );
  }

  public static IRCompositeExpressionBuilder newArray( IJavaClassInfo componentType, List<IRExpressionBuilder> values ) {
    return newArray( getIRType( componentType ), values );
  }

  public static IRCompositeExpressionBuilder newArray( IRType componentType, List<IRExpressionBuilder> values ) {
    List<IRStatementBuilder> statements = new ArrayList<IRStatementBuilder>();
    IRSymbolBuilder tempArray = new IRTempSymbolBuilder( componentType.getArrayType() );
    statements.add(assign(tempArray, newArray(componentType, literal(values.size()))));
    for (int i = 0; i < values.size(); i++) {
      statements.add( arrayStore( var(tempArray), literal(i), values.get(i) ) );
    }
    return new IRCompositeExpressionBuilder( statements, var( tempArray ) );
  }

  public static IRNewArrayExpressionBuilder newArray( Class rootType, int size ) {
    return newArray( getIRType( rootType ), literal( size ) );
  }

  public static IRNewArrayExpressionBuilder newArray( IType rootType, int size ) {
    return newArray( getIRType( rootType ), literal( size ) );
  }

  public static IRNewArrayExpressionBuilder newArray( IJavaClassInfo rootType, int size ) {
    return newArray( getIRType( rootType ), literal( size ) );
  }

  public static IRNewArrayExpressionBuilder newArray( IRType rootType, IRExpressionBuilder size ) {
    return new IRNewArrayExpressionBuilder( rootType, size );
  }

  // ------------------------ New Instance Creation ---------------------------

  public static IRNewExpressionBuilder _new( Class rootType, IRExpressionBuilder... values ) {
    return new IRNewExpressionBuilder( getIRType( rootType), Arrays.asList( values ) );
  }

  // ------------------------ Null Literal ------------------------------------

  public static IRNullLiteralBuilder _null() {
    return new IRNullLiteralBuilder();
  }

  // ------------------------ Pass Through Args -------------------------------

  public static List<IRExpressionBuilder> passArgs( IJavaClassMethod m ) {
    List<IRExpressionBuilder> args = new ArrayList<IRExpressionBuilder>();
    IJavaClassInfo[] paramTypes = m.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      args.add(var("arg" + i));
    }
    return args;
  }

  public static List<IRExpressionBuilder> passArgs( IJavaClassConstructor cons ) {
    List<IRExpressionBuilder> args = new ArrayList<IRExpressionBuilder>();
    IJavaClassInfo[] paramTypes = cons.getParameterTypes();
    for (int i = 0; i < paramTypes.length; i++) {
      args.add(var("arg" + i));
    }
    return args;
  }

  // ------------------------ This Expression ---------------------------------

  public static IRIdentifierExpressionBuilder _this() {
    return new IRIdentifierExpressionBuilder( new IRThisSymbolBuilder() );
  }


  // ==========================================================================
  // ------------------------ Statement Builders ------------------------------
  // ==========================================================================

  // ------------------------ Array Store Statement ---------------------------

  public static IRArrayStoreStatementBuilder arrayStore( IRExpressionBuilder target, IRExpressionBuilder index, IRExpressionBuilder value ) {
    IRArrayStoreStatementBuilder statement = new IRArrayStoreStatementBuilder( target, index, value );
    return statement;
  }

  // ------------------------ Assignment Statement ----------------------------

  public static IRAssignmentStatementBuilder assign(String name, IRExpressionBuilder value) {
    return new IRAssignmentStatementBuilder( new IRSymbolBuilder( name ), value );
  }

  public static IRAssignmentStatementBuilder assign(String name, IRType type, IRExpressionBuilder value) {
    return new IRAssignmentStatementBuilder( new IRSymbolBuilder( name, type ), value);
  }

  public static IRAssignmentStatementBuilder assign(IRSymbolBuilder symbol, IRExpressionBuilder value) {
    return new IRAssignmentStatementBuilder( symbol, value );
  }

  // ------------------------- If Statement -----------------------------------

  public static IRIfStatementBuilder _if(IRExpressionBuilder test) {
    return new IRIfStatementBuilder(test);
  }

  // ------------------------- Return Statement -------------------------------

  public static IRReturnStatementBuilder _return() {
    return new IRReturnStatementBuilder();
  }

  public static IRReturnStatementBuilder _return(IRExpressionBuilder value) {
    return new IRReturnStatementBuilder(value);
  }

  // ------------------------- Set Field Statement ----------------------------

  public static IRFieldSetStatementBuilder set(String name, IRExpressionBuilder value) {
    return new IRFieldSetStatementBuilder(_this(), name, value);
  }

  // ------------------------- Super method call ------------------------------

  public static IRMethodCallExpressionBuilder _superInit(List<IRExpressionBuilder> args) {
    return IRMethodCallExpressionBuilder.callSuperInit(args);
  }

  public static IRMethodCallExpressionBuilder _superInit(IRExpressionBuilder... args) {
    return IRMethodCallExpressionBuilder.callSuperInit(Arrays.asList(args));
  }

  // ==========================================================================
  // ------------------------ Other Helpers -----------------------------------
  // ==========================================================================

  public static IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  public static IRType getIRType( IType type ) {
    return GosuShop.getIRTypeResolver().getDescriptor( type );
  }

  public static IRType getIRType( IJavaClassInfo cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  public static List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }
}
