/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.AnnotationInfoFactoryImpl;
import gw.internal.gosu.parser.FieldJavaClassField;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.TypeUsesMap;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.ICompileTimeConstantValue;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.util.GosuExceptionUtil;

import java.lang.annotation.Annotation;
import gw.util.Array;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/**
 */
public class CompileTimeExpressionParser
{
  public static IExpression parse(String text, IJavaClassInfo enclosingType, IType resultType) {
    if( text.endsWith( ".class" ) ) {
      text = text.substring( 0, text.lastIndexOf( ".class" ) );
    }
    ITypeUsesMap usesMap = null;
    List<String> staticImports = null;
    IJavaClassInfo outerMostEnclosingType = TypeLord.getOuterMostEnclosingClass(enclosingType);
    if (outerMostEnclosingType instanceof JavaSourceType) {
      usesMap = ((JavaSourceType) outerMostEnclosingType).getTypeUsesMap().copy();
      staticImports = ((JavaSourceType)outerMostEnclosingType).getStaticImports();
      addInnerClassNames( enclosingType, usesMap );
    }
    else {
      usesMap = new TypeUsesMap();
      staticImports = Collections.emptyList();
    }

    usesMap.addToDefaultTypeUses("gw.lang.");

    TypeSystem.pushIncludeAll();
    addEnclosingPackages(usesMap, enclosingType );
    try {
      GosuParser scriptParser = (GosuParser)GosuParserFactory.createParser( text );
      maybePushEnumTypes( scriptParser.getSymbolTable(), resultType );
      pushLocalConstants( scriptParser.getSymbolTable(), enclosingType );
      pushStaticImports( scriptParser.getSymbolTable(), staticImports, enclosingType );
      scriptParser.setTypeUsesMap( usesMap );
      IExpression expr = scriptParser.parseExpOrProgram( new TypelessScriptPartId("compile-time annotation eval" ), resultType, false, false );
      return expr;
    }
    catch (ParseResultsException e) {
      throw GosuExceptionUtil.forceThrow( e );
    }
    finally {
      TypeSystem.popIncludeAll();
    }
  }

  private static void addInnerClassNames( IJavaClassInfo enclosingType, ITypeUsesMap usesMap ) {
    if( enclosingType == null ) {
      return;
    }
    usesMap.addToTypeUses( enclosingType.getName() );
    for( IJavaClassInfo jci : enclosingType.getDeclaredClasses() ) {
      usesMap.addToTypeUses( jci.getName() );
    }
    addInnerClassNames( enclosingType.getEnclosingClass(), usesMap );
  }

  private static void pushLocalConstants(ISymbolTable symbolTable, IJavaClassInfo enclosingClass) {
    for(IJavaClassField field : enclosingClass.getDeclaredFields() ) {
      symbolTable.putSymbol( new CompileTimeFieldSymbol( field ) );
    }
  }

  private static void pushStaticImports(ISymbolTable symbolTable, List<String> staticImports, IJavaClassInfo enclosingType) {
    for( String imp : staticImports ) {
      IJavaClassInfo javaClassInfo = TypeSystem.getJavaClassInfo(imp, enclosingType.getModule());
      if (javaClassInfo != null) {
        for(IJavaClassField field : javaClassInfo.getFields() ) {
          symbolTable.putSymbol( new CompileTimeFieldSymbol( field ) );
        }
      } else {
        int endIndex = imp.lastIndexOf('.');
        String typeName = imp.substring(0, endIndex);
        String fieldName = imp.substring(endIndex + 1);
        javaClassInfo = TypeSystem.getJavaClassInfo(typeName, enclosingType.getModule());
        for(IJavaClassField field : javaClassInfo.getFields() ) {
          if (field.getName().equals(fieldName)) {
            symbolTable.putSymbol( new CompileTimeFieldSymbol( field ) );
          }
        }
      }
    }
  }

  private static void maybePushEnumTypes(ISymbolTable symbolTable, IType returnType) {
    if( !returnType.isEnum() ) {
      return;
    }
    for(IPropertyInfo pi : returnType.getTypeInfo().getProperties() ) {
      if( pi.isStatic() && pi.isPublic() ) {
        symbolTable.putSymbol( new Symbol(pi.getName(), pi.getFeatureType(), null) );
      }
    }
  }

  private static void addEnclosingPackages(ITypeUsesMap map, IJavaClassInfo type) {
    map.addToDefaultTypeUses( type.getNamespace() + ".");
    if (type.getEnclosingType() != null) {
      addEnclosingPackages(map, type.getEnclosingClass());
    }
  }

  public static class CompileTimeFieldSymbol extends Symbol implements ICompileTimeConstantValue {
    private IJavaClassField _field;

    public CompileTimeFieldSymbol( IJavaClassField field ) {
      super( field.getName(), field.getType().getJavaType(), null );
      _field = field;
    }

    public IJavaClassField getField() {
      return _field;
    }

    public boolean isCompileTimeConstantValue() {
       return Modifier.isStatic( getField().getModifiers() ) &&
              Modifier.isFinal( getField().getModifiers() );
     }

    @Override
    public ISymbol getLightWeightReference() {
      return this;
    }

    @Override
     public Object doCompileTimeEvaluation() {
       IJavaClassField field = getField();
       if( field instanceof JavaSourceField ) {
         String rhs = ((JavaSourceField)field).getRhs();
         IExpression expr = CompileTimeExpressionParser.parse(rhs, field.getEnclosingClass(), field.getType().getJavaType());
         return expr.evaluate();
       }
       else if( field instanceof FieldJavaClassField ) {
         try {
           Object value = ((FieldJavaClassField)field).get( null );
           return convertValueToInfoFriendlyValue( value, _field.getEnclosingClass().getJavaType().getTypeInfo() );
         }
         catch( IllegalAccessException e ) {
           throw new RuntimeException( e );
         }
       }
       else {
         throw new IllegalStateException( "Unexpected field type: " + field );
       }
     }
  }

  public static Object convertValueToInfoFriendlyValue( Object value, IFeatureInfo enclosingType ) {
    if( value == null ) {
      return null;
    }
    if( value instanceof Enum ) {
      return ((Enum)value).name();
    }
    if( value.getClass().isArray() && !IType.class.isAssignableFrom( value.getClass().getComponentType() ) ) {
      Object arrayValue = null;
      for( int i = 0; i < Array.getLength( value ); i++ ) {
        //noinspection RedundantCast
        Object elemValue = convertValueToInfoFriendlyValue( Array.get( value, i ), enclosingType );
        if( arrayValue == null ) {
          arrayValue = Array.newInstance( elemValue.getClass(), Array.getLength( value ) );
        }
        Array.set( arrayValue, i, elemValue );
      }
      if( arrayValue == null ) {
        arrayValue = value;
      }
      return arrayValue;
    }
    if( value instanceof Class ) {
      return TypeSystem.get( (Class)value );
    }
    if( value instanceof Annotation ) {
      return AnnotationInfoFactoryImpl.instance().createJavaAnnotation( (Annotation)value, enclosingType );
    }
    return value;
  }
}
