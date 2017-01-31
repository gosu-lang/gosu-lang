package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.util.GosuObjectUtil;
import gw.util.IFeatureFilter;

/**
 */
public class FeaturePathCompletionHandler extends AbstractPathCompletionHandler
{
  @Override
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    boolean bDotAtCaret = GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforeCaret( getGosuEditor().getEditor() ), "#" );
    IExpression exprAtCaret;
    if( bDotAtCaret )
    {
      exprAtCaret = getGosuEditor().getExpressionContainingCharacterBeforeCaret();
    }
    else
    {
      exprAtCaret = getGosuEditor().getExpressionAtCaret();
    }

    String strMemberPath = getSingleNameAtCaret();
    strMemberPath = strMemberPath == null ? "" : strMemberPath.trim();
    if( bDotAtCaret && strMemberPath.length() > 0 )
    {
      strMemberPath += '#';
    }
    boolean bFilterByLastPathElement = strMemberPath.length() == 0 || !bDotAtCaret;
    if( exprAtCaret instanceof IFeatureLiteralExpression )
    {
      IFeatureLiteralExpression memberExpr = (IFeatureLiteralExpression)exprAtCaret;
      IType rootType = memberExpr.getRootType();
      if( rootType instanceof IErrorType || rootType instanceof INamespaceType )
      {
        return false;
      }

      IFeatureFilter filter = null;
      if( getGosuEditor().getScriptabilityModifier() != null )
      {
        filter = new ScriptabilityFilter( getGosuEditor().getScriptabilityModifier() );
      }
      try
      {
//        strMemberPath = strMemberPath.length() == 0 || memberExpr.getMemberName() == null
//                        ? "#"
//                        :  memberExpr.getMemberName();
        BeanInfoPopup beanInfoPopup = new BeanInfoPopup( memberExpr.getRootType(),
                                                         strMemberPath,
                                                         bFilterByLastPathElement,
                                                         getGosuEditor(),
                                                         filter,
                                                         false,
                                                         true );
        displayFeaturePathCompletionBeanInfoPopup( beanInfoPopup );
        return true;
      }
      catch( ParseException e )
      {
        return false;
      }
    }
    return false;
  }
}
