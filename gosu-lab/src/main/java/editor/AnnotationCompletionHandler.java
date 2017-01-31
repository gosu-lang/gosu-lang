package editor;


import editor.util.TextComponentUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.util.GosuObjectUtil;

/**
 */
public class AnnotationCompletionHandler extends SymbolCompletionHandler
{
  @Override
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    IExpression exprAtCaret = getGosuEditor().getExpressionAtCaret();
    if( exprAtCaret == null )
    {
      return false;
    }
    String wordAtCaret = TextComponentUtil.getWordAtCaret( getGosuEditor().getEditor() );
    boolean bAtAtCaret = "@".equals( wordAtCaret ) || GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforePos( getGosuEditor().getEditor(), getGosuEditor().getPositionAtStartOfExpressionAtCaret() ), "@" );
    if( !bAtAtCaret )
    {
      return false;
    }
    String prefix = "";
    if( exprAtCaret instanceof ITypeLiteralExpression )
    {
      ITypeLiteralExpression typeAtCaret = (ITypeLiteralExpression)exprAtCaret;
      if( typeAtCaret.getLocation().getLength() > 0 )
      {
        prefix = typeAtCaret.toString();
      }
    }
    return displayTypesPopup( prefix );
  }

  @Override
  protected boolean isAnnotationsOnly()
  {
    return true;
  }
}