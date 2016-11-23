package editor.search;

import editor.GosuEditor;
import editor.LabFrame;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.exceptions.ParseIssue;
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
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IConstructorStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class UsageTarget
{
  private final IParsedElement _pe;
  private final IFeatureInfo _selectedFi;
  private final IFeatureInfo _rootFi;
  private final SearchElement _targetPe;

  public UsageTarget( IParsedElement pe, IFeatureInfo selectedFi )
  {
    _pe = pe;
    _selectedFi = selectedFi;
    _rootFi = findRootFeatureInfo();
    _targetPe = findTarget( _rootFi, pe );
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

  public SearchElement getTargetElement()
  {
    return _targetPe;
  }

  public static UsageTarget makeTargetFromCaret()
  {
    GosuEditor editor = getCurrentGosuEditor();
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

    return makeTarget( pe );
  }

  public static UsageTarget makeTarget( IParsedElement pe )
  {
    if( pe == null )
    {
      return null;
    }
    while( pe.getLocation().getLength() == 0 )
    {
      pe = pe.getParent();
    }

    try
    {
      IFeatureInfo fi = findFeatureInfoFor( pe );
      if( fi == null )
      {
        return null;
      }
      return new UsageTarget( pe, fi );
    }
    catch( RuntimeException e )
    {
      if( e.getCause() instanceof ParseIssue )
      {
        return null;
      }
      throw e;
    }
  }

  private static GosuEditor getCurrentGosuEditor()
  {
    return LabFrame.instance().getGosuPanel().getCurrentGosuEditor();
  }

  private static IFeatureInfo findFeatureInfoFor( IParsedElement pe )
  {
    if( pe instanceof IIdentifierExpression )
    {
      if( pe instanceof IPropertyAccessIdentifier ||
          pe instanceof IPropertyAsMethodCallIdentifier )
      {
        IFeatureInfo pi = ((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
        SearchElement target = findTarget( pi, pe );
        if( target != null )
        {
          return findFeatureInfoFor( (IParsedElement)target.getElement() );
        }
        return ((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
      }
      else
      {
        ISymbol symbol = ((IIdentifierExpression)pe).getSymbol();
        if( symbol instanceof IDynamicSymbol )
        {
          IGosuClass gsClass = symbol.getGosuClass();
          if( gsClass != null )
          {
            IPropertyInfo pi = gsClass.getTypeInfo().getProperty( gsClass, symbol.getDisplayName() );
            if( pi != null )
            {
              SearchElement target = findTarget( pi, pe );
              if( target != null )
              {
                return findFeatureInfoFor( (IParsedElement)target.getElement() );
              }
            }
          }
        }
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
        SearchElement targetPe = findTarget( memberAccess.getPropertyInfo(), pe );
        if( targetPe != null )
        {
          return findFeatureInfoFor( (IParsedElement)targetPe.getElement() );
        }
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

        IType type = typeLiteral.getType().getType();
        while( type.isArray() )
        {
          type = type.getComponentType();
        }
        return type.getTypeInfo();
      }
    }
    return null;
  }

  private static SearchElement findTarget( IFeatureInfo fi, IParsedElement ref )
  {
    if( !(fi.getOwnersType() instanceof IFileRepositoryBasedType) )
    {
      return null;
    }
    IFileRepositoryBasedType declaringType = (IFileRepositoryBasedType)fi.getOwnersType();

    if( fi instanceof ITypeInfo )
    {
      IType type = fi.getOwnersType();
      if( type instanceof IGosuClass )
      {
        return new SearchElement( ((IGosuClass)type).getClassStatement().getClassDeclaration() );
      }
      else
      {
        ILocationInfo loc = fi.getLocationInfo();
        if( loc != null && loc != ILocationInfo.EMPTY )
        {
          return new SearchElement( fi, declaringType, loc.getOffset() );
        }
        else
        {
          return new SearchElement( fi, declaringType, 0 );
        }
      }
    }
    if( fi instanceof IMethodInfo )
    {
      if( declaringType instanceof IGosuClass )
      {
        IFunctionStatement fs = ((IGosuClass)declaringType).getFunctionStatement( (IMethodInfo)fi );
        return new SearchElement( fs.getLocation().getDeepestLocation( fs.getNameOffset( null ), true ).getParsedElement() );
      }
      else
      {
        ILocationInfo loc = fi.getLocationInfo();
        if( loc != null && loc != ILocationInfo.EMPTY )
        {
          return new SearchElement( fi, declaringType, loc.getOffset() );
        }
      }
    }
    else if( fi instanceof IPropertyInfo )
    {
      if( fi instanceof IGosuVarPropertyInfo )
      {
        int offset = ((IGosuVarPropertyInfo)fi).getOffset();
        return new SearchElement( ((IGosuClass)declaringType).getClassStatement().getLocation().getDeepestLocation( offset, true ).getParsedElement() );
      }
      else if( fi instanceof IGosuPropertyInfo )
      {
        boolean bSetter;
        if( ref instanceof IIdentifierExpression )
        {
          IParsedElement parent = ref.getParent();
          bSetter = parent instanceof IAssignmentStatement && ((IAssignmentStatement)parent).getIdentifier() == ref;
        }
        else
        {
          IParsedElement parent = ref.getParent();
          bSetter = parent instanceof IMemberAssignmentStatement && (((IMemberAssignmentStatement)parent).getMemberAccess() == ref ||
                                                                     ((IMemberAssignmentStatement)parent).getRootExpression() == ref);
        }
        IMethodInfo mi = bSetter
                         ? (IMethodInfo)((IGosuPropertyInfo)fi).getDps().getSetterDfs().getMethodOrConstructorInfo()
                         : (IMethodInfo)((IGosuPropertyInfo)fi).getDps().getGetterDfs().getMethodOrConstructorInfo();
        IFunctionStatement fs = ((IGosuClass)mi.getOwnersType()).getFunctionStatement( mi );
        if( fs == null )
        {
          return new SearchElement( ((IGosuClass)declaringType).getPropertyDeclaration( fi.getDisplayName() ) );
        }
        else
        {
          return new SearchElement( fs.getLocation().getDeepestLocation( fs.getNameOffset( null ), true ).getParsedElement() );
        }
      }
      else
      {
        ILocationInfo loc = fi.getLocationInfo();
        if( loc != null && loc != ILocationInfo.EMPTY )
        {
          return new SearchElement( fi, declaringType, loc.getOffset() );
        }
      }
    }
    else if( fi instanceof IConstructorInfo )
    {
      if( ((IConstructorInfo)fi).isDefault() )
      {
        return findTarget( fi.getContainer(), ref );
      }
      if( declaringType instanceof IGosuClass )
      {
        IFunctionStatement fs = ((IGosuClass)declaringType).getConstructorStatement( (IConstructorInfo)fi );
        return new SearchElement( fs == null ? null : fs.getLocation().getDeepestLocation( fs.getNameOffset( null ), true ).getParsedElement() );
      }
      else
      {
        ILocationInfo loc = fi.getLocationInfo();
        if( loc != null && loc != ILocationInfo.EMPTY )
        {
          return new SearchElement( fi, declaringType, loc.getOffset() );
        }
      }
    }
    else if( fi instanceof LocalVarFeatureInfo )
    {
      return new SearchElement( findLocalDeclaration( ((LocalVarFeatureInfo)fi).getSymbol(), ref ) );
    }
    return null;
  }

  private static IParsedElement findLocalDeclaration( ISymbol symbol, IParsedElement pe )
  {
    int offset;
    List<ILocalVarDeclaration> res = new ArrayList<>();
    IParsedElement root = getRootParsedElement( pe );
    root.getContainedParsedElementsByType( ILocalVarDeclaration.class, res );
    for( ILocalVarDeclaration varDecl : res )
    {
      if( symbol == varDecl.getSymbol() || sameFunctionSameIndex( pe, symbol, varDecl ) )
      {
        offset = varDecl.getNameOffset( (String)varDecl.getLocalVarName() );
        return findDeepestViablePe( offset, varDecl );
      }
    }
    List<IVarStatement> v = new ArrayList<>();
    root.getContainedParsedElementsByType( IVarStatement.class, v );
    for( IVarStatement fs : v )
    {
      if( symbol.equals( fs.getSymbol() ) )
      {
        offset = fs.getNameOffset( fs.getIdentifierName() );
        return findDeepestViablePe( offset, fs );
      }
    }
    return null;
  }

  private static boolean sameFunctionSameIndex( IParsedElement pe, ISymbol symbol, ILocalVarDeclaration varDecl )
  {
    if( findFunctionStmt( pe ) == findFunctionStmt( varDecl ) )
    {
      ISymbol declSymbol = varDecl.getSymbol();
      return symbol.equals( declSymbol ) && symbol.getIndex() == declSymbol.getIndex();
    }
    return false;
  }

  private static IFunctionStatement findFunctionStmt( IParsedElement pe )
  {
    if( pe instanceof IFunctionStatement )
    {
      return (IFunctionStatement)pe;
    }

    return findFunctionStmt( pe.getParent() );
  }

  private static IParsedElement findDeepestViablePe( int offset, IParsedElement pe )
  {
    IParseTree deepestLocation = pe.getLocation().getDeepestLocation( offset, true );
    IParsedElement localPe = deepestLocation.getParsedElement();
    while( localPe.getLocation().getLength() == 0 )
    {
      localPe = localPe.getParent();
    }
    return localPe;
  }

  private static IParsedElement getRootParsedElement( IParsedElement pe )
  {
    IParseTree location = pe.getLocation();
    if( location == null )
    {
      return null;
    }
    IParsedElement parsedElement = location.getParsedElement();
    while( parsedElement.getParent() != null )
    {
      parsedElement = parsedElement.getParent();
    }
    return parsedElement;
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
