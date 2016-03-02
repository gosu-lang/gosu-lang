package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.util.GosuObjectUtil;

/**
 */
public class PackageCompletionHandler extends AbstractPathCompletionHandler
{
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    IExpression exprAtCaret = getGosuEditor().getExpressionAtCaret();
    if( exprAtCaret == null )
    {
      return false;
    }
    if( exprAtCaret instanceof ITypeLiteralExpression && exprAtCaret.getParent() instanceof INewExpression )
    {
      IParseTree location = exprAtCaret.getLocation();
      if( location != null )
      {
        IParseTree prevSibling = location.getPreviousSibling();
        if( prevSibling != null )
        {
          IParsedElement prevElement = prevSibling.getParsedElement();
          if( prevElement instanceof IExpression )
          {
            exprAtCaret = (IExpression)prevElement;
          }
        }
      }
    }
    String strMemberPath = getSingleNameAtCaret();
    boolean bDotAtCaret = GosuObjectUtil.equals( TextComponentUtil.getWordBeforeCaret( getGosuEditor().getEditor() ), "." );
    boolean bFilterByLastPathElement = strMemberPath == null || !bDotAtCaret;
    IType rootType;
    IMemberAccessExpression memberExpr;
    PackageType type = null;
    StringBuffer sbFilter = new StringBuffer();
    if( exprAtCaret instanceof IMemberAccessExpression )
    {
      memberExpr = (IMemberAccessExpression)exprAtCaret;
      rootType = memberExpr.getRootType();
      if( !(rootType instanceof IErrorType) && !(rootType instanceof INamespaceType) )
      {
        return false;
      }
      type = PackageTypeLoader.instance().getType( rootType.getName() );
      if( type == null )
      {
        strMemberPath = strMemberPath == null || strMemberPath.length() == 0 || memberExpr.getMemberName() == null
                        ? ""
                        : memberExpr.getMemberName();
        strMemberPath = memberExpr.getRootExpression().toString() + '.' + strMemberPath;
        type = getParentPackageType( strMemberPath, sbFilter );
      }
      else
      {
        sbFilter.append( strMemberPath );
      }
    }
    else if( exprAtCaret instanceof ITypeLiteralExpression && exprAtCaret.getParent() instanceof IUsesStatement )
    {
      type = getParentPackageType( ((ITypeLiteralExpression)exprAtCaret).getType().getRelativeName(), sbFilter );
    }
    try
    {
      if( type != null )
      {
        BeanInfoPopup beanInfoPopup = new MetaInfoPopup( type, sbFilter.toString(), bFilterByLastPathElement, getGosuEditor() );
        displayPathCompletionBeanInfoPopup( beanInfoPopup );
        return true;
      }
    }
    catch( ParseException e )
    {
      return false;
    }

    return false;
  }

  private PackageType getParentPackageType( String strMemberPath, StringBuffer sbOutFilter )
  {
    int iIndex = strMemberPath.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      sbOutFilter.append( strMemberPath.substring( iIndex + 1 ) );
      strMemberPath = strMemberPath.substring( 0, iIndex );
      return PackageTypeLoader.instance().getType( strMemberPath );
    }
    return null;
  }
}
