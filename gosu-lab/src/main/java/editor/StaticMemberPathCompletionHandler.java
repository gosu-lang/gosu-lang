package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.util.GosuObjectUtil;

/**
 */
public class StaticMemberPathCompletionHandler extends AbstractPathCompletionHandler
{
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    IExpression exprAtCaret = getGosuEditor().getExpressionAtCaret();
    String strMemberPath = getSingleNameAtCaret();
    if( strMemberPath == null )
    {
      return false;
    }

    boolean bDotAtCaret = GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforeCaret( getGosuEditor().getEditor() ), "." );
    boolean bFilterByLastPathElement = !bDotAtCaret;
    if( exprAtCaret instanceof IMemberAccessExpression )
    {
      IMemberAccessExpression memberExpr = (IMemberAccessExpression)exprAtCaret;
      IType rootType = memberExpr.getRootType();
      if( !(rootType instanceof IErrorType) && !(rootType instanceof IMetaType) )
      {
        return false;
      }

      try
      {
        StringBuilder sbMemberPath = new StringBuilder( strMemberPath );
        if( memberExpr.getRootType() instanceof IMetaType )
        {
          BeanInfoPopup beanInfoPopup = new MetaInfoPopup( (IMetaType)memberExpr.getRootType(),
                                                           sbMemberPath.toString(),
                                                           bFilterByLastPathElement,
                                                           getGosuEditor() );
          displayPathCompletionBeanInfoPopup( beanInfoPopup );
          return true;
        }
        return false;
      }
      catch( ParseException e )
      {
        return false;
      }
    }
    return false;
  }
}
