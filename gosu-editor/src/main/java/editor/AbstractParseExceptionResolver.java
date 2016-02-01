package editor;

import editor.util.EditorUtilities;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IHasArguments;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 */
public abstract class AbstractParseExceptionResolver implements IParseExceptionResolver, IValueCompletionHandler
{
  private GosuEditor _gsEditor;


  @Override
  public GosuEditor getGosuEditor()
  {
    return _gsEditor;
  }

  @Override
  public void setGosuEditor( GosuEditor gsEditor )
  {
    _gsEditor = gsEditor;
  }

  protected JTextComponent getEditor()
  {
    return getGosuEditor().getEditor();
  }

  @Override
  public boolean canResolve( ParseException pe )
  {
    if( pe == null )
    {
      return false;
    }

    IType typeExpected = getExpectedType( pe );
    if( typeExpected == null )
    {
      return false;
    }

    BeanInfoPopup beanInfoPopup = getGosuEditor().getBeanInfoPopup();
    if( beanInfoPopup != null )
    {
      if( beanInfoPopup.isVisible() )
      {
        // Don't stomp all over the beaninfo popup
        return false;
      }
      else
      {
        //## todo: this is a little bit disgusting
        getGosuEditor().setBeanInfoPopup( null );
      }
    }

    int iPos = getGosuEditor().getCaretPositionForValueCompletion( pe );
    JTextComponent editor = getGosuEditor().getEditor();
    int iCurrentPos = editor.getCaretPosition();
    try
    {
      Rectangle rcAdvice = editor.modelToView( iPos );
      Rectangle rcCurAdvice = editor.modelToView( iCurrentPos );
      if( rcAdvice == null || rcCurAdvice == null || rcAdvice.y != rcCurAdvice.y )
      {
        return false;
      }
    }
    catch( Exception e )
    {
      EditorUtilities.handleUncaughtException( "ElementModel change listener error.", e );
    }

    return true;
  }

  public IType getExpectedType( ParseException pe )
  {
    IType typeExpected = pe.getExpectedType();
    if( typeExpected == null )
    {
      typeExpected = resolvePossibleContextTypesFromEmptyMethodCalls( getGosuEditor().getExpressionAtCaret(), getEditor() );
    }
    return typeExpected;
  }

  protected IType getBaseType( ParseException pe )
  {
    IMemberAccessExpression ma = pe.getMemberAccessContext();
    IType baseType = null;

    if( ma != null )
    {
      baseType = ma.getRootType();
    }

    return baseType;
  }

  protected IParseTree getDeepestLocationToComplete()
  {
    // Get the deepest location at the caret.
    IParseTree deepest = getGosuEditor().getDeepestLocationAtCaret();
    if( deepest == null )
    {
      return null;
    }

    return deepest;
  }

  protected IType getTypeExpected()
  {
    IParseTree deepest = getDeepestLocationToComplete();
    if( deepest == null || !(deepest.getParsedElement() instanceof IExpression) )
    {
      return null;
    }
    IExpression expression = (IExpression)deepest.getParsedElement();
    IType typeExpected = expression.getContextType();

    if( typeExpected == null )
    {
      if( expression instanceof ILiteralExpression )
      {
        return expression.getType();
      }
      return resolvePossibleContextTypesFromEmptyMethodCalls( expression, getEditor() );
    }

    return typeExpected;
  }

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

  void insertTextAtCaret( String str )
  {
    GosuEditorPane editor = getGosuEditor().getEditor();
    try
    {
      editor.getDocument().insertString( editor.getCaretPosition(), str, null );
    }
    catch( BadLocationException e1 )
    {
      throw new RuntimeException( e1 );
    }
  }

  protected void showValueCompletionPopup( IValuePopup popup, boolean bFocusInEditor )
  {
    getGosuEditor().setValuePopup( popup );

    IParseTree currentLocation = getDeepestLocationToComplete();

    if( currentLocation != null &&
        currentLocation.getParsedElement() instanceof IExpression &&
        shouldPositionAtStartOfElement( currentLocation, getEditor() ) )
    {
      // Highlight/select the location to be visually specific about what is being acted on.
      getEditor().setCaretPosition( currentLocation.getOffset() );
      getEditor().moveCaretPosition( currentLocation.getOffset() + currentLocation.getLength() );
    }

    getGosuEditor().displayValuePopup( getEditor().getSelectionStart(), bFocusInEditor );
  }

  public static boolean shouldPositionAtStartOfElement( IParseTree currentLocation, JTextComponent editor )
  {
    return currentLocation != null &&
           !isEditorAtPossibleArgPosition( currentLocation.getParsedElement(), editor ) &&
           !isEmptyLocation( currentLocation );
  }

  private static boolean isEmptyLocation( IParseTree currentLocation )
  {
    return currentLocation.getLength() == 0;
  }

}
