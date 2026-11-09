package gw.lang.parser.restrictions;

import gw.lang.parser.AstProcessor;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IInitializerAssignment;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IPropertyAccessIdentifier;
import gw.lang.parser.expressions.IStaticTypeOfExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeOfExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IDynamicType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IMethodInfoDelegate;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static gw.lang.parser.resources.Res.MSG_ANY;

class RestrictionsChecker implements AstProcessor
{
  private final Restrictions _restrictions;
  private final Set<String> _reported;
  private IGosuClass _programClass;

  RestrictionsChecker( Restrictions restrictions )
  {
    _restrictions = restrictions;
    _reported = new LinkedHashSet<>();
  }

  @Override
  public void process( IParsedElement root )
  {
    _programClass = root.getGosuClass();
    visit( root );
  }

  void visit( IParsedElement pe )
  {
    for( IParseTree e : pe.getLocation().getChildren() )
    {
      visit( e.getParsedElement() );
    }
    check( pe );
  }

  private void check( IParsedElement e )
  {
    if( e instanceof ITypeLiteralExpression )
    {
      checkTypeLiteral( (ITypeLiteralExpression)e );
    }
    else if( e instanceof IVarStatement )
    {
      checkVarStatement( (IVarStatement)e );
    }
    else if( e instanceof ILocalVarDeclaration )
    {
      checkLocalVarDeclaration( (ILocalVarDeclaration)e );
    }
    else if( e instanceof IMemberAccessExpression )
    {
      checkMemberAccessExpression( (IMemberAccessExpression)e );
    }
    else if( e instanceof IMethodCallExpression )
    {
      checkMethodCallExpression( (IMethodCallExpression)e );
    }
    else if( e instanceof IPropertyAccessIdentifier )
    {
      checkPropertyAccessIdentifier( (IPropertyAccessIdentifier)e );
    }
    else if( e instanceof IInitializerAssignment )
    {
      checkInitializerAssignment( (IInitializerAssignment)e );
    }
    else if( e instanceof INewExpression )
    {
      checkNewExpression( (INewExpression)e );
    }
    else if( e instanceof IEvalExpression )
    {
      checkFeature( e, LanguageFeature.EVAL );
    }
    else if( e instanceof ITypeOfExpression ||
             e instanceof IStaticTypeOfExpression )
    {
      checkFeature( e, LanguageFeature.TYPEOF );
    }
    else if( e instanceof IFeatureLiteralExpression )
    {
      checkFeature( e, LanguageFeature.FEATURE_LITERAL );
    }
  }

  /**
   * Adds the issue unless the same message was already reported at the same position. Programs split top-level var
   * statements (as a field and as an assignment statement in the program's function), and a type reference may be
   * reported both as a type literal and as the declaring type of a member access e.g., Foo.staticMethod().
   */
  private void report( IParsedElement e, Result result )
  {
    if( _reported.add( e.getLineNum() + ":" + e.getColumn() + ":" + result.getMessage() ) )
    {
      e.addParseException( MSG_ANY, result.getMessage() );
    }
  }

  private void checkFeature( IParsedElement e, LanguageFeature feature )
  {
    Result result = _restrictions.checkFeature( feature );
    if( !result.isAllowed() )
    {
      report( e, result );
    }
  }

  private void checkTypeLiteral( ITypeLiteralExpression tl )
  {
    IMetaType type = tl.getType();
    if( type != null && type.getType() != null )
    {
      Result result = checkType( type.getType() );
      if( !result.isAllowed() )
      {
        report( tl, result );
      }
    }
  }

  private void checkVarStatement( IVarStatement vs )
  {
    if( vs.getTypeLiteral() == null )
    {
      // inferred type
      Result result = checkType( vs.getType() );
      if( !result.isAllowed() )
      {
        report( vs, result );
      }
    }
  }

  private void checkLocalVarDeclaration( ILocalVarDeclaration localToStmt )
  {
    if( localToStmt.getTypeLiteral() == null )
    {
      // inferred type
      Result result = checkType( localToStmt.getType() );
      if( !result.isAllowed() )
      {
        report( localToStmt, result );
      }
    }
  }

  private void checkMemberAccessExpression( IMemberAccessExpression expr )
  {
    if( expr.getType() instanceof INamespaceType )
    {
      // namespace access is handled as package/type access
      return;
    }

    if( expr.getType() instanceof IDynamicType )
    {
      Result result = _restrictions.checkFeature( LanguageFeature.DYNAMIC );
      if( !result.isAllowed() )
      {
        // dynamic type use not allowed
        report( expr, result );
        return;
      }
    }

    if( expr instanceof IFieldAccessExpression &&
        checkAssociativeArrayAccessExpression( (IFieldAccessExpression)expr ) )
    {
      // associate array access not allowed
      return;
    }

    if( expr instanceof IBeanMethodCallExpression )
    {
      IMethodInfo mi = sourceOf( ((IBeanMethodCallExpression)expr).getMethodDescriptor() );
      checkMember( expr, expr.getRootType(), mi, Collections.singletonList( expr.getMemberName() ) );
    }
    else
    {
      IMemberAssignmentStatement assignment = expr.getParent() instanceof IMemberAssignmentStatement &&
                                              ((IMemberAssignmentStatement)expr.getParent()).getMemberAccess() == expr
                                              ? (IMemberAssignmentStatement)expr.getParent()
                                              : null;
      checkMember( expr, expr.getRootType(), sourceOf( expr.getPropertyInfo() ),
                   propertyMemberNames( expr.getMemberName(),
                                        assignment == null || assignment.isCompoundStatement(), assignment != null ) );
    }
  }

  /**
   * A call with no qualifier: a method of the enclosing class, inherited or not, or a statically imported method
   */
  private void checkMethodCallExpression( IMethodCallExpression expr )
  {
    IMethodInfo mi = expr.getFunctionType() == null ? null : sourceOf( expr.getFunctionType().getMethodInfo() );
    if( mi != null )
    {
      checkMember( expr, null, mi, Collections.singletonList( mi.getDisplayName() ) );
    }
  }

  private void checkPropertyAccessIdentifier( IPropertyAccessIdentifier expr )
  {
    ISymbol symbol = expr.getSymbol();
    IFeatureInfo fi = symbol instanceof IDynamicPropertySymbol ? ((IDynamicPropertySymbol)symbol).getPropertyInfo() : null;
    if( fi instanceof IPropertyInfo )
    {
      IAssignmentStatement assignment = expr.getParent() instanceof IAssignmentStatement &&
                                        ((IAssignmentStatement)expr.getParent()).getIdentifier() == expr
                                        ? (IAssignmentStatement)expr.getParent()
                                        : null;
      checkMember( expr, null, sourceOf( (IPropertyInfo)fi ),
                   propertyMemberNames( symbol.getName(),
                                        assignment == null || assignment.isCompoundStatement(), assignment != null ) );
    }
  }

  /**
   * `:P = value` in an object initializer writes property P
   */
  private void checkInitializerAssignment( IInitializerAssignment expr )
  {
    checkMember( expr, expr.getOwnerType(), sourceOf( expr.getPropertyInfo() ),
                 propertyMemberNames( expr.getPropertyName(), false, true ) );
  }

  /**
   * @param receiverType The static type of the qualifier, null if there is none
   */
  private void checkMember( IParsedElement e, IType receiverType, IFeatureInfo member, List<String> memberNames )
  {
    if( member == null || memberNames.isEmpty() )
    {
      return;
    }
    IType declaringType = resolve( member.getOwnersType() );
    if( declaringType == null || declaringType instanceof IErrorType )
    {
      return;
    }

    if( declaringType instanceof IGosuEnhancement )
    {
      declaringType = ((IGosuEnhancement)declaringType).getEnhancedType();
    }

    boolean bStatic = member instanceof IMethodInfo ? ((IMethodInfo)member).isStatic() : ((IPropertyInfo)member).isStatic();
    Result result = checkType( declaringType );
    if( result.isAllowed() )
    {
      // A member rule on the receiver's type or the declaring type matches, so the user need not know which type
      // declares the member. For an instance member a rule on any of their supertypes also matches, since that covers
      // overrides of the member. A static member is not overridden; a subtype's static of the same name is unrelated.
      Set<IType> hierarchy = new LinkedHashSet<>();
      addHierarchy( resolve( receiverType ), hierarchy, !bStatic );
      addHierarchy( declaringType, hierarchy, !bStatic );
      if( !bStatic )
      {
        // Gosu classes extending Object and interfaces have no supertype
        hierarchy.add( JavaTypes.OBJECT() );
      }
      for( IType type : hierarchy )
      {
        result = checkMemberOnly( type, memberNames );
        if( !result.isAllowed() )
        {
          break;
        }
      }
    }

    if( !result.isAllowed() )
    {
      // access to member is denied
      report( e, result );
    }
  }

  /**
   * The owner of an inherited static member's delegate is the subtype it is accessed on, not the declaring type.
   * (A meta type's delegates to the instance members of IType are static, but their sources are not.)
   */
  private static IMethodInfo sourceOf( IMethodInfo mi )
  {
    while( mi instanceof IMethodInfoDelegate && mi.isStatic() && ((IMethodInfoDelegate)mi).getSource().isStatic() )
    {
      mi = ((IMethodInfoDelegate)mi).getSource();
    }
    return mi;
  }

  private static IPropertyInfo sourceOf( IPropertyInfo pi )
  {
    while( pi instanceof IPropertyInfoDelegate && pi.isStatic() && ((IPropertyInfoDelegate)pi).getSource().isStatic() )
    {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }
    return pi;
  }

  /**
   * A property is denied by a rule naming the property or the accessor method used: getX/isX to read, setX to write.
   */
  private static List<String> propertyMemberNames( String name, boolean bRead, boolean bWrite )
  {
    List<String> memberNames = new ArrayList<>();
    if( name == null || name.isEmpty() )
    {
      return memberNames;
    }
    memberNames.add( name );

    String capitalized = Character.toUpperCase( name.charAt( 0 ) ) + name.substring( 1 );
    if( bRead )
    {
      memberNames.add( "get" + capitalized );
      memberNames.add( "is" + capitalized );
    }
    if( bWrite )
    {
      memberNames.add( "set" + capitalized );
    }
    return memberNames;
  }

  private static void addHierarchy( IType type, Set<IType> hierarchy, boolean bSupertypes )
  {
    if( type == null || type instanceof IErrorType || type.isArray() )
    {
      return;
    }
    if( type instanceof ITypeVariableType )
    {
      addHierarchy( ((ITypeVariableType)type).getBoundingType(), hierarchy, bSupertypes );
      return;
    }
    if( type instanceof ICompoundType )
    {
      for( IType t : ((ICompoundType)type).getTypes() )
      {
        addHierarchy( t, hierarchy, bSupertypes );
      }
      return;
    }
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    if( hierarchy.add( type ) && bSupertypes )
    {
      addHierarchy( type.getSupertype(), hierarchy, true );
      for( IType iface : type.getInterfaces() )
      {
        addHierarchy( iface, hierarchy, true );
      }
    }
  }

  private Result checkMemberOnly(IType declaringType, List<String> memberNames )
  {
    for( String memberName : memberNames )
    {
      Result result = _restrictions.checkMemberOnly( declaringType.getName(), memberName );
      if( !result.isAllowed() )
      {
        return result;
      }
    }
    return Result.ALLOWED;
  }

  private void checkNewExpression( INewExpression expr )
  {
    IConstructorInfo constructor = expr.getConstructor();
    if( constructor == null )
    {
      return;
    }

    IType type = constructor.getOwnersType();
    while( type instanceof IGosuClass && ((IGosuClass)type).isAnonymous() && isOwnType( type ) )
    {
      // an anonymous class calls its super constructor
      type = type.getSupertype();
    }
    if( type == null || type instanceof IErrorType || isOwnType( type ) )
    {
      return;
    }

    Result result = checkType( type );
    if( !result.isAllowed() )
    {
      // reported by the TypeLiteral check
      return;
    }

    result = _restrictions.checkMemberOnly( type.getName(), "<init>" );
    if( !result.isAllowed() )
    {
      // constructor is denied
      report( expr, result );
    }
  }

  private boolean checkAssociativeArrayAccessExpression( IFieldAccessExpression expr )
  {
    IExpression memberExpression = expr.getMemberExpression();
    if( memberExpression == null )
    {
      return false;
    }

    Result result = _restrictions.checkFeature( LanguageFeature.DYNAMIC );
    if( !result.isAllowed() )
    {
      report( expr, result );
      return true;
    }
    return false;
  }

  private Result checkType( IType type )
  {
    type = unproxy( type );
    if( type == null || type instanceof IErrorType )
    {
      return Result.ALLOWED;
    }
    while( type.isArray() )
    {
      type = type.getComponentType();
    }
    if( type instanceof IGosuClass && ((IGosuClass)type).isStructure() )
    {
      Result result = _restrictions.checkFeature( LanguageFeature.STRUCTURAL );
      if( !result.isAllowed() )
      {
        return result;
      }
    }
    if( type.isPrimitive() || isOwnType( type ) )
    {
      return Result.ALLOWED;
    }
    if( type instanceof IBlockType ) // lambda use
    {
      return Result.ALLOWED;
    }
    if( type instanceof ITypeVariableType )
    {
      // a type variable is as restricted as its bound
      return checkType( ((ITypeVariableType)type).getBoundingType() );
    }
    if( type instanceof ICompoundType )
    {
      // e.g., the inferred type of `true ? 1 : "s"` is `Serializable & Comparable`; since members are checked
      // against their declaring types, the compound type is usable if any of its component types is allowed
      Result result = null;
      for( IType t : ((ICompoundType)type).getTypes() )
      {
        result = checkType( t );
        if( result.isAllowed() )
        {
          return result;
        }
      }
      return result == null ? Result.ALLOWED : result;
    }
    return _restrictions.checkType( type.getName() );
  }

  /**
   * True if the type is the program or a type declared in it, including anonymous classes
   */
  private boolean isOwnType( IType type )
  {
    while( type.getEnclosingType() != null )
    {
      type = type.getEnclosingType();
    }
    return type == _programClass;
  }

  private static IType unwrapMetaType( IType type )
  {
    return type instanceof IMetaType ? ((IMetaType)type).getType() : type;
  }

  /**
   * The type itself, unwrapped from a metatype or a Gosu proxy of a Java type
   */
  private static IType resolve( IType type )
  {
    return unproxy( unwrapMetaType( type ) );
  }

  /**
   * The Java type of a Gosu proxy, e.g., from a static import
   */
  private static IType unproxy( IType type )
  {
    return IGosuClass.ProxyUtil.isProxy( type ) ? IGosuClass.ProxyUtil.getProxiedType( type ) : type;
  }
}
