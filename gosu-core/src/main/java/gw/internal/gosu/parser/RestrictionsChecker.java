package gw.internal.gosu.parser;

import gw.internal.gosu.dynamic.DynamicType;
import gw.internal.gosu.parser.expressions.BlockType;
import gw.internal.gosu.parser.expressions.LocalVarDeclaration;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.function.IBlock;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.restrictions.LanguageFeature;
import gw.lang.parser.restrictions.Restrictions;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.ITypeOfExpression;
import gw.lang.parser.restrictions.Result;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

import static gw.lang.parser.resources.Res.MSG_ANY;

class RestrictionsChecker
{
  private static final RestrictionsChecker instance = new RestrictionsChecker();

  private RestrictionsChecker()
  {
  }

  static RestrictionsChecker instance()
  {
    return instance;
  }

  void enforceRestrictions( IParsedElement pe )
  {
    IGosuClass gosuClass = pe.getGosuClass();
    if( !(gosuClass instanceof IGosuProgramInternal) )
    {
      // only enforcing restrictions on GosuPrograms
      // e.g., from GosuProgramParser.parseExpressionOrProgram(...)
      return;
    }

    Restrictions restrictions = ((IGosuProgramInternal)gosuClass).getRestrictions();
    if( restrictions == null )
    {
      // no restrictions
      return;
    }

    visit( pe, e -> {
      if( e instanceof TypeLiteral )
      {
        checkTypeLiterals( (TypeLiteral)e, restrictions );
      }
      else if( e instanceof VarStatement )
      {
        checkVarStatements( (VarStatement)e, restrictions );
      }
      else if( e instanceof LocalVarDeclaration )
      {
        checkLocalVarDeclaration( (LocalVarDeclaration)e, restrictions );
      }
      else if( e instanceof IMemberAccessExpression )
      {
        checkMemberAccessExpression( (IMemberAccessExpression)e, restrictions );
      }
      else if( e instanceof INewExpression )
      {
        checkNewExpression( (INewExpression)e, restrictions );
      }
      else if( e instanceof IEvalExpression )
      {
        checkEvalExpression( (IEvalExpression)e, restrictions );
      }
      else if( e instanceof ITypeOfExpression )
      {
        checkTypeOfExpression( (ITypeOfExpression)e, restrictions );
      }
      else if( e instanceof IFeatureLiteralExpression )
      {
        checkFeatureLiteralExpression( (IFeatureLiteralExpression)e, restrictions );
      }
    }, new LinkedHashSet<>() );
  }

  private void checkTypeLiterals( TypeLiteral tl, Restrictions restrictions )
  {
    MetaType type = tl.getType();
    if( type != null )
    {
      IType t = type.getType();
      if( t != null && !(t instanceof IErrorType) && !t.isPrimitive() && !isInSamePackage( t, tl.getGosuClass() ) )
      {
        Result result = checkType( t, restrictions );
        if( !result.isAllowed() )
        {
          tl.addParseException( MSG_ANY, result.getMessage() );
        }
      }
    }
  }

  private void visit( IParsedElement pe, Consumer<IParsedElement> consumer, Set<IParsedElement> visited )
  {
    if( visited.stream().anyMatch( e -> e.getClass() == pe.getClass() && e.getLineNum() == pe.getLineNum() && e.getColumn() == pe.getColumn() ) )
    {
      // programs copy top-level var declarations, so we need to avoid visiting them twice
      return;
    }
    visited.add( pe );

    for( IParseTree e : pe.getLocation().getChildren() )
    {
      visit( e.getParsedElement(), consumer, visited );
    }
    consumer.accept( pe );
  }

  private void checkVarStatements( VarStatement vs, Restrictions restrictions )
  {
    if( vs.getTypeLiteral() == null )
    {
      Result result = checkType( vs.getType(), restrictions );
      if( !result.isAllowed() )
      {
        vs.addParseException( MSG_ANY, result.getMessage() );
      }
    }
  }

  private void checkLocalVarDeclaration( LocalVarDeclaration localToStmt, Restrictions restrictions )
  {
    if( localToStmt.getTypeLiteral() == null )
    {
      Result result = checkType( localToStmt.getType(), restrictions );
      if( !result.isAllowed() )
      {
        localToStmt.addParseException( MSG_ANY, result.getMessage() );
      }
    }
  }

  private void checkMemberAccessExpression( IMemberAccessExpression expr, Restrictions restrictions )
  {
    if( expr.getType() instanceof NamespaceType )
    {
      // namespace access is handled as package/type access
      return;
    }

    if( expr.getType() instanceof DynamicType )
    {
      Result result = restrictions.checkFeature( LanguageFeature.DYNAMIC );
      if( !result.isAllowed() )
      {
        // dynamic type use not allowed
        expr.addParseException( MSG_ANY, result.getMessage() );
        return;
      }
    }

    if( expr instanceof IFieldAccessExpression &&
        checkAssociativeArrayAccessExpression( (IFieldAccessExpression)expr, restrictions ) )
    {
      // associate array access not allowed
      return;
    }

    IType declaringType;
    if( expr instanceof IBeanMethodCallExpression )
    {
      IMethodInfo mi = ((IBeanMethodCallExpression)expr).getMethodDescriptor();
      declaringType = mi == null ? null : mi.getOwnersType();
    }
    else
    {
      IPropertyInfo pi = expr.getPropertyInfo();
      declaringType = pi == null ? null : pi.getOwnersType();
    }

    if( declaringType instanceof MetaType )
    {
      declaringType = ((MetaType)declaringType).getType();
    }

    if( declaringType != null && !(declaringType instanceof IErrorType) && !declaringType.isPrimitive() &&
        !isInSamePackage( declaringType, expr.getGosuClass() ) )
    {
      Result result = restrictions.checkMember( declaringType.getName(), expr.getMemberName() );
      if( !result.isAllowed() )
      {
        // access to member is denied
        expr.addParseException( MSG_ANY, result.getMessage() );
      }
    }
  }

  private void checkNewExpression( INewExpression expr, Restrictions restrictions )
  {
    IConstructorInfo constructor = expr.getConstructor();
    if( constructor == null )
    {
      return;
    }

    IType type = constructor.getOwnersType();
    if( type == null || type instanceof IErrorType )
    {
      return;
    }

    Result result = checkType( type, restrictions );
    if( !result.isAllowed() )
    {
      // sb covered by TypeLiteral check
      return;
    }

    result = restrictions.checkMember( type.getName(), "<init>" );
    if( !result.isAllowed() )
    {
      // dynamic type use not allowed
      expr.addParseException( MSG_ANY, result.getMessage() );
    }
  }

  private void checkEvalExpression( IEvalExpression expr, Restrictions restrictions )
  {
    Result result = restrictions.checkFeature( LanguageFeature.EVAL );
    if( !result.isAllowed() )
    {
      expr.addParseException( MSG_ANY, result.getMessage() );
    }
  }
  
  private void checkTypeOfExpression( ITypeOfExpression expr, Restrictions restrictions )
  {
    Result result = restrictions.checkFeature( LanguageFeature.TYPEOF );
    if( !result.isAllowed() )
    {
      expr.addParseException( MSG_ANY, result.getMessage() );
    }
  }

  private void checkFeatureLiteralExpression( IFeatureLiteralExpression expr, Restrictions restrictions )
  {
    Result result = restrictions.checkFeature( LanguageFeature.FEATURE_LITERAL );
    if( !result.isAllowed() )
    {
      expr.addParseException( MSG_ANY, result.getMessage() );
    }
  }

  private boolean checkAssociativeArrayAccessExpression( IFieldAccessExpression expr, Restrictions restrictions )
  {
    IExpression memberExpression = expr.getMemberExpression();
    if( memberExpression == null )
    {
      return false;
    }

    Result result = restrictions.checkFeature( LanguageFeature.DYNAMIC );
    if( !result.isAllowed() )
    {
      expr.addParseException( MSG_ANY, result.getMessage() );
      return true;
    }
    return false;
  }

  private static Result checkType( IType type, Restrictions restrictions )
  {
    if( type instanceof IBlockType )
    {
      return restrictions.checkFeature( LanguageFeature.LAMBDA );
    }
    return restrictions.checkType( type.getName() );
  }

  private boolean isInSamePackage( IType t, IGosuClass gosuClass )
  {
    return t.getNamespace().equals( gosuClass.getNamespace() );
  }
}
