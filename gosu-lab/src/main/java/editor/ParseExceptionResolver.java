package editor;

import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IHasArguments;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import java.awt.Point;
import javax.swing.text.JTextComponent;

/**
 */
public class ParseExceptionResolver
{
  public static IType resolvePossibleContextTypesFromEmptyMethodCalls( IExpression expression, JTextComponent editor )
  {
    IType[] paramTypes = null;
    if( expression instanceof IMethodCallExpression )
    {
      IMethodCallExpression mce = (IMethodCallExpression)expression;
      if( mce.getArgs() == null || mce.getArgs().length == 0 )
      {
        if( isEditorAtPossibleArgPosition( mce, editor ) )
        {
          IFunctionSymbol symbol = mce.getFunctionSymbol();
          if( symbol != null )
          {
            IFunctionType funType = (FunctionType)symbol.getType();
            if( funType != null && funType.getParameterTypes() != null && funType.getParameterTypes().length > 0 )
            {
              paramTypes = funType.getParameterTypes();
            }
          }
        }
      }
    }
    if( expression instanceof IBeanMethodCallExpression )
    {
      IBeanMethodCallExpression bmce = (IBeanMethodCallExpression)expression;
      if( bmce.getArgs() == null || bmce.getArgs().length == 0 )
      {
        if( isEditorAtPossibleArgPosition( bmce, editor ) )
        {
          IFunctionType funType = bmce.getFunctionType();
          if( funType != null && funType.getParameterTypes() != null && funType.getParameterTypes().length > 0 )
          {
            paramTypes = funType.getParameterTypes();
          }
        }
      }
    }
    return paramTypes != null && paramTypes.length > 0 ? paramTypes[0] : null;
  }

  public static boolean isEditorAtPossibleArgPosition( IParsedElement e, JTextComponent editor )
  {
    boolean returnVal = false;
    if( e instanceof IHasArguments )
    {
      Point argPosition = findMethodArgPoint( e, editor );
      if( argPosition != null )
      {
        int position = editor.getCaretPosition();
        if( position > argPosition.x && position <= argPosition.y )
        {
          returnVal = true;
        }
      }
    }
    return returnVal;
  }

  private static Point findMethodArgPoint( IParsedElement mce, JTextComponent editor )
  {
    int start = mce.getLocation().getOffset();
    int end = mce.getLocation().getExtent();
    try
    {
      while( !"(".equals( editor.getText( start, 1 ) ) && start < end )
      {
        start++;
      }
      while( !")".equals( editor.getText( end, 1 ) ) && end > start )
      {
        end--;
      }
      return new Point( start, end );
    }
    catch( Exception e )
    {
      //ignore
    }
    return null;
  }
}
