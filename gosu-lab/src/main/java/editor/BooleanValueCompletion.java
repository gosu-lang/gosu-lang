package editor;

import editor.util.TextComponentUtil;
import gw.config.CommonServices;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 */
public class BooleanValueCompletion extends AbstractParseExceptionResolver
{
  @Override
  public boolean canResolve( ParseException pe )
  {
    if( !super.canResolve( pe ) )
    {
      return false;
    }
    IType typeExpected = getExpectedType( pe );
    return typeExpected == JavaTypes.BOOLEAN() ||
           typeExpected == JavaTypes.pBOOLEAN();
  }

  @Override
  public void resolve( ParseException pe )
  {
    StringPopup popup = new StringPopup( "true", JavaTypes.BOOLEAN().getRelativeName(), getGosuEditor().getEditor() );

    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          TextComponentUtil.replaceWordAtCaretDynamic( getEditor(), e.getSource().toString(),
                                                       getGosuEditor().getReplaceWordCallback(), false );
          getEditor().requestFocus();
          getGosuEditor().fixSwingFocusBugWhenPopupCloses();
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, false );
  }

  @Override
  public boolean handleCompleteValue()
  {
    IType typeExpected = getTypeExpected();
    if( typeExpected == null )
    {
      return false;
    }
    if( typeExpected != JavaTypes.BOOLEAN() &&
        typeExpected != JavaTypes.pBOOLEAN() )
    {
      return false;
    }
    final IParseTree currentLocation = getDeepestLocationToComplete();

    Boolean bValue = Boolean.TRUE;
    IParsedElement element = currentLocation.getParsedElement();
    if( element instanceof ILiteralExpression )
    {
      bValue = (Boolean)CommonServices.getCoercionManager().convertValue( ((ILiteralExpression)element).evaluate(), TypeSystem.get( Boolean.class ) );
    }

    StringPopup popup = new StringPopup( bValue == null ? "true" : bValue.toString(), JavaTypes.BOOLEAN().getRelativeName(), getGosuEditor().getEditor() );
    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          if( isEditorAtPossibleArgPosition( currentLocation.getParsedElement(), BooleanValueCompletion.this.getEditor() ) )
          {
            insertTextAtCaret( e.getSource().toString() );
          }
          else
          {
            getGosuEditor().replaceLocation( currentLocation, e.getSource().toString() );
          }
          getEditor().requestFocus();
          getGosuEditor().fixSwingFocusBugWhenPopupCloses();
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, true );
    return true;
  }
}
