/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.internal.gosu.parser.IParameterizableType;
import gw.lang.parser.ICompilationState;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IConstructorStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.ICanBeAnnotation;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGosuClass extends ICompilableType, IEnumType, IEnhanceableType, Comparable, IHasInnerClass, IHasJavaClass, IParameterizableType, ICanBeAnnotation
{
  String PROXY_PREFIX = "_proxy_";
  String SUPER_PROXY_CLASS_PREFIX = "_java_";
  String ANONYMOUS_PREFIX = "AnonymouS_";

  IGosuClassTypeInfo getTypeInfo();

  IModifierInfo getModifierInfo();

  boolean isSubClass( IType gsSubType );

  boolean isStructure();

  boolean isCompiled();

  IGosuClass getInnerClass( CharSequence strTypeName );

  List<? extends IGosuClass> getInnerClasses();

  Map<CharSequence, ? extends IGosuClass> getInnerClassesMap();

  IClassStatement getClassStatementWithoutCompile();

  ICompilationState getCompilationState();

  boolean isCompilingHeader();

  boolean isHeaderCompiled();

  boolean isCompilingDeclarations();

  boolean isDeclarationsCompiled();

  boolean isDeclarationsBypassed();

  boolean isInnerDeclarationsCompiled();

  boolean isCompilingDefinitions();

  boolean isDefinitionsCompiled();

  boolean isTestClass();

  boolean hasError();

  boolean hasWarnings();

  ParseResultsException getParseResultsException();

  List<? extends IVarStatement> getMemberFields();

  List<? extends IDynamicFunctionSymbol> getMemberFunctions();

  Map<String, ? extends IVarStatement> getMemberFieldsMap();

  public IDynamicPropertySymbol getMemberProperty( String name );

  IType getEnclosingTypeReference();

  IFunctionStatement getFunctionStatement( IMethodInfo method );
  IConstructorStatement getConstructorStatement( IConstructorInfo ctor );
  INameInDeclaration getPropertyDeclaration( String name );

  IJavaType getJavaType();

  /**
   * WARNING:  This method is slow the first time it is called.  It will iterate over all types in the system
   * and find all matching subtypes
   *
   * @return all subtypes of this type
   */
  List<? extends IType> getSubtypes();

  /**
   * Only for use during type loading e.g., from GosuClassTypeLoader
   *
   * @param enclosingType the enclosing type
   */
  void setEnclosingType( IType enclosingType );

  void setNamespace( String strNamespace );

  boolean shouldKeepDebugInfo();

  void setCreateEditorParser( boolean bEditorParser );

  void unloadBackingClass();

  boolean hasBackingClass();

  public IType findProxiedClassInHierarchy();

  Map<CharSequence, ? extends IGosuClass> getKnownInnerClassesWithoutCompiling();

  public List<IGosuClass> getBlocks();

  String getSource();

  static class ProxyUtil
  {
    public static boolean isProxy( IType type )
    {
      if( type instanceof ICompoundType )
      {
        for( IType t : ((ICompoundType)type).getTypes() )
        {
          if( isProxy( t ) )
          {
            return true;
          }
        }
        return false;
      }
      return type != null && isProxyClass( type.getName() );
    }

    public static boolean isProxyClass( String strName )
    {
      return strName != null &&
             strName.length() > PROXY_PREFIX.length() && // must be Greater than
             strName.startsWith( PROXY_PREFIX );
    }

    public static boolean isProxyStart( String strName )
    {
      return strName != null &&
             strName.length() >= PROXY_PREFIX.length() &&
             strName.startsWith( PROXY_PREFIX );
    }

    public static IType getProxiedType( IType type )
    {
      if( type instanceof ICompoundType )
      {
        Set<IType> types = new HashSet<>();
        for( IType t : ((ICompoundType)type).getTypes() )
        {
          types.add( getProxiedType( t ) );
        }
        return TypeSystem.getCompoundType( types );
      }

      while( type.isParameterizedType() )
      {
        type = type.getGenericType();
      }

      if( isProxy( type ) )
      {
        return TypeSystem.getByFullName( getNameSansProxy( type ) );
      }
      else
      {
        return type;
      }
    }

    public static String getNameSansProxy( IType type )
    {
      return getNameSansProxy( type.getName() );
    }

    public static String getNameSansProxy( String name )
    {
      if( isProxyName( name ) )
      {
        return name.substring( IGosuClass.PROXY_PREFIX.length() + 1 );
      }

      return name;
    }

    private static boolean isProxyName( String name )
    {
      return name.startsWith( IGosuClass.PROXY_PREFIX ) && name.length() > IGosuClass.PROXY_PREFIX.length() + 1;
    }
  }

}
