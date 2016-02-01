package editor;

import editor.util.TextComponentUtil;
import gw.config.CommonServices;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 */
public class EnumerationValueCompletion extends AbstractParseExceptionResolver
{

  @Override
  public boolean canResolve( ParseException pe )
  {
    if( !super.canResolve( pe ) )
    {
      return false;
    }
    IType typeExpected = getExpectedType( pe );
    return typeExpected != null && typeExpected.isEnum();
  }

  @Override
  public void resolve( ParseException pe )
  {
    IType typeExpected = getExpectedType( pe );

    EnumerationListPopup popup = new EnumerationListPopup( (IEnumType)typeExpected, getEditor() );
    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          if( isEditorAtPossibleArgPosition( getDeepestLocationToComplete().getParsedElement(), EnumerationValueCompletion.this.getEditor() ) )
          {
            insertTextAtCaret( e.getSource().toString() );
          }
          else
          {
            TextComponentUtil.replaceWordAtCaretDynamic( getEditor(), (String)e.getSource(),
                                                         getGosuEditor().getReplaceWordCallback(), false );
          }
          getEditor().requestFocus();
          getGosuEditor().fixSwingFocusBugWhenPopupCloses();
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, true );
  }

  @Override
  public boolean handleCompleteValue()
  {
    IType typeExpected = getTypeExpected();
    if( typeExpected == null )
    {
      return false;
    }
    if( !typeExpected.isEnum() )
    {
      return false;
    }

    final IParseTree currentLocation = getDeepestLocationToComplete();
    IParsedElement element = currentLocation.getParsedElement();

    EnumerationListPopup popup = new EnumerationListPopup( (IEnumType)typeExpected, getEditor() );

    Object value = null;
    if( element instanceof ILiteralExpression )
    {
      try
      {
        value = CommonServices.getCoercionManager().convertValue( ((ILiteralExpression)element).evaluate(), typeExpected );
      }
      catch( Exception e )
      {
        // the typeliteral is not a valid typekey
      }
    }
    if( value != null )
    {
      popup.setValue( value );
    }
    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          getGosuEditor().replaceLocation( currentLocation, (String)e.getSource() );
          getEditor().requestFocus();
          getGosuEditor().fixSwingFocusBugWhenPopupCloses();
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, true );
    return true;
  }

}
