/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.*;
import gw.lang.parser.template.StringEscaper;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.*;
import gw.lang.GosuShop;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GosuTemplateType extends GosuProgram implements IGosuTemplateInternal
{
  private TemplateGenerator _gen;
  private ModifierInfo _modifierInfo;

  public GosuTemplateType( String strNamespace, String strRelativeName,
                           GosuClassTypeLoader classTypeLoader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap,
                           ISymbolTable symTable )
  {
    super( strNamespace, strRelativeName, classTypeLoader, sourceFile, typeUsesMap, symTable );
    _modifierInfo = new ModifierInfo( Modifier.PUBLIC );
  }

  @Override
  protected void addProgramInterfaces()
  {
  }

  public IType getArrayType() {
    return TypeSystem.getOrCreateTypeReference(new DefaultArrayType(TypeSystem.getOrCreateTypeReference(this), TypeSystem.getJavaClassInfo(Object.class), getTypeLoader()));
  }

  public IType getSupertype()
  {
    if( getTemplateGenerator().getSuperType() != null )
    {
      return getTemplateGenerator().getSuperType();
    }
    return JavaTypes.OBJECT();
  }

  @Override
  public boolean isFinal()
  {
    return true;
  }

  @Override
  public boolean isGenericType()
  {
    return false;
  }

  @Override
  public ModifierInfo getModifierInfo()
  {
    return _modifierInfo;
  }

  public TemplateGenerator getTemplateGenerator()
  {
    if( _gen == null )
    {
      _gen = (TemplateGenerator)GosuShop.createSimpleTemplateHost().getTemplate( new StringReader( getSource() ), getName() );
    }
    return _gen;
  }

  @Override
  protected GosuParser getOrCreateParser(CompiledGosuClassSymbolTable symbolTable)
  {
    GosuParser parser = super.getOrCreateParser(symbolTable);
    if( parser.getTokenizerInstructor() == null )
    {
      setTokenizerInstructor( new TemplateTokenizerInstructor( parser.getTokenizer() ) );
    }
    return parser;
  }

  @Override
  public void addTemplateEntryPoints( ISymbolTable symTable, GosuClassParser parser )
  {
    IGosuClassInternal pThis = (IGosuClassInternal)getOrCreateTypeReference();
    IJavaType iface = JavaTypes.getGosuType(TemplateRenderer.class);
    IGosuClassInternal gsInterface = Util.getGosuClassFrom( iface );
    if( gsInterface != null )
    {
      for( IMethodInfo mi : gsInterface.getTypeInfo().getDeclaredMethods() )
      {
        if( mi.getDisplayName().startsWith( "render" ) )
        {
          GosuMethodInfo gmi = (GosuMethodInfo)mi;
          ReducedDynamicFunctionSymbol dfs = gmi.getDfs();

          mi = (IMethodInfo)dfs.getMethodOrConstructorInfo();
          String strMethodName = mi.getDisplayName();
          IType[] parameterTypes = ((IFunctionType)dfs.getType()).getParameterTypes();
          mi = ((IRelativeTypeInfo)iface.getTypeInfo()).getMethod( iface, strMethodName, parameterTypes );

          symTable.pushScope();
          TemplateRenderFunctionSymbol forwardFs;
          try
          {
            forwardFs = new TemplateRenderFunctionSymbol( pThis, symTable, dfs, mi, this, getParameterTypes( parameterTypes ) );
          }
          finally
          {
            symTable.popScope();
          }
          forwardFs.setModifiers( Modifier.setStatic( forwardFs.getModifiers(), true ) );
          parser.processFunctionSymbol( forwardFs, pThis );
        }
      }
    }
  }

  public IType[] getParameterTypes( IType[] delegateParamTypes )
  {
    ITemplateGenerator templateGenerator = getTemplateGenerator();
    List<ISymbol> params = templateGenerator.getParameters();
    List<IType> paramTypes = new ArrayList<IType>();
    if( delegateParamTypes.length >= 3 )
    {
      paramTypes.add( JavaTypes.getJreType( Writer.class ) );
      if( delegateParamTypes.length >= 4 )
      {
        paramTypes.add( JavaTypes.getJreType( StringEscaper.class ) );
      }
    }
    for( int i = 0; i < params.size(); i++ )
    {
      ISymbol symbol = params.get( i );
      paramTypes.add( symbol.getType() );
    }
    return paramTypes.toArray( new IType[paramTypes.size()] );
  }

  @Override
  public ClassType getClassType() {
    return ClassType.Template;
  }
}
