package editor.search;

import editor.GosuEditor;
import editor.RunMe;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IPropertyAccessIdentifier;
import gw.lang.parser.expressions.IPropertyAsMethodCallIdentifier;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IConstructorStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

/**
 */
public class UsageTarget
{
  private final IParsedElement _pe;
  private final IFeatureInfo _selectedFi;
  private final IFeatureInfo _rootFi;

  public UsageTarget( IParsedElement pe, IFeatureInfo selectedFi )
  {
    _pe = pe;
    _selectedFi = selectedFi;
    _rootFi = findRootFeatureInfo();
  }

  private IFeatureInfo findRootFeatureInfo()
  {
    if( _selectedFi instanceof IMethodInfo )
    {
      return FeatureUtil.findRootMethodInfo( (IMethodInfo)_selectedFi );
    }
    else if( _selectedFi instanceof IPropertyInfo )
    {
      return FeatureUtil.findRootPropertyInfo( (IPropertyInfo)_selectedFi );
    }
    else if( _selectedFi instanceof IConstructorInfo )
    {
      return FeatureUtil.findRootConstructorInfo( (IConstructorInfo)_selectedFi );
    }
    return _selectedFi;
  }

  public IParsedElement getParsedElement()
  {
    return _pe;
  }

  public IFeatureInfo getSelectedFeatureInfo()
  {
    return _selectedFi;
  }

  public IFeatureInfo getRootFeatureInfo()
  {
    return _rootFi;
  }

  public static UsageTarget findInCurrentEditor()
  {
    GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().getCurrentEditor();
    if( editor == null )
    {
      return null;
    }

    IParseTree loc = editor.getDeepestLocationAtCaret();
    if( loc == null )
    {
      return null;
    }

    IParsedElement pe = loc.getParsedElement();
    if( pe == null )
    {
      return null;
    }

    IFeatureInfo fi = findFeatureInfoFor( pe );
    if( fi == null )
    {
      return null;
    }

    return new UsageTarget( pe, fi );
  }

  private static IFeatureInfo findFeatureInfoFor( IParsedElement pe )
  {
    if( pe instanceof IIdentifierExpression )
    {
      if( pe instanceof IPropertyAccessIdentifier ||
          pe instanceof IPropertyAsMethodCallIdentifier )
      {
        return ((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
      }
      else
      {
        return new LocalVarFeatureInfo( (IIdentifierExpression)pe );
      }
    }
    else if( pe instanceof ILocalVarDeclaration )
    {
      return new LocalVarFeatureInfo( (ILocalVarDeclaration)pe );
    }
    else if( pe instanceof IFieldAccessExpression )
    {
      IFieldAccessExpression memberAccess = (IFieldAccessExpression)pe;
      if( !(memberAccess.getType() instanceof INamespaceType) )
      {
        return memberAccess.getPropertyInfo();
      }
    }
    else if( pe instanceof IMethodCallExpression )
    {
      IMethodCallExpression methodCallExpression = (IMethodCallExpression)pe;
      IFunctionSymbol sym = methodCallExpression.getFunctionSymbol();
      if( sym instanceof IDynamicFunctionSymbol )
      {
        return ((IDynamicFunctionSymbol)sym).getMethodOrConstructorInfo();
      }
    }
    else if( pe instanceof IBeanMethodCallExpression )
    {
      IBeanMethodCallExpression beanMethodCallExpression = (IBeanMethodCallExpression)pe;
      return beanMethodCallExpression.getMethodDescriptor();
    }
    else if( pe instanceof INewExpression )
    {
      INewExpression newExpression = (INewExpression)pe;
      return newExpression.getConstructor();
    }
    else if( pe instanceof IClassDeclaration )
    {
      return pe.getGosuClass().getTypeInfo();
    }
    else if( pe instanceof INameInDeclaration )
    {
      IFeatureInfo fi = findFeatureInfo( (INameInDeclaration)pe );
      if( fi != null )
      {
        return fi;
      }
    }
    else if( pe instanceof IVarStatement && ((IVarStatement)pe).isFieldDeclaration() && ( (IVarStatement)pe ).getType().isEnum() )
    {
      IFeatureInfo fi = findFeatureInfo( pe, ((IVarStatement)pe).getIdentifierName() );
      if( fi != null )
      {
        return fi;
      }
    }
    else if( pe instanceof IConstructorStatement )
    {
      return ((IConstructorStatement)pe).getDynamicFunctionSymbol().getMethodOrConstructorInfo();
    }
    else if( pe instanceof ITypeVariableDefinition )
    {
      ITypeVariableDefinition typeVariableDefinition = (ITypeVariableDefinition)pe;
      return null; //## todo
    }
    else if( pe instanceof ITypeLiteralExpression )
    {
      if( pe.getParent() instanceof INewExpression && ((INewExpression)pe.getParent()).getTypeLiteral() == pe )
      {
        IConstructorInfo ctor = ((INewExpression)pe.getParent()).getConstructor();
        if( ctor != null )
        {
          return ctor;
        }
      }

      ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression)pe;
      if( isErrant( typeLiteral ) )
      {
        return null;
      }
      else
      {
        return typeLiteral.getType().getType().getTypeInfo();
      }
    }
    return null;
  }

  private static IFeatureInfo findFeatureInfo( INameInDeclaration pe )
  {
    final IParsedElement parent = pe.getParent();
    return findFeatureInfo( parent, pe.getName() );
  }

  private static IFeatureInfo findFeatureInfo( IParsedElement parent, String name )
  {
    if( parent instanceof IVarStatement )
    {
      IVarStatement varStatement = (IVarStatement)parent;
      if( varStatement.isFieldDeclaration() )
      {
        IGosuClass gsClass = varStatement.getSymbol().getGosuClass();
        return gsClass.getTypeInfo().getProperty( gsClass, name );
      }
      else
      {
        LocalVarFeatureInfo local = new LocalVarFeatureInfo( varStatement );
        if( local.getContainer() != null )
        {
          return local;
        }
      }
    }
    else if( parent instanceof ILocalVarDeclaration )
    {
      return new LocalVarFeatureInfo( (ILocalVarDeclaration)parent );
    }
    else if( parent instanceof IFunctionStatement )
    {
      return ((IFunctionStatement)parent).getDynamicFunctionSymbol().getMethodOrConstructorInfo();
    }
    return null;
  }

  private static boolean isErrant( ITypeLiteralExpression typeLiteral )
  {
    IType type = typeLiteral.getType();
    return ((type instanceof IMetaType) &&
            (((IMetaType)type).getType() instanceof IErrorType));
  }
}
