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
public class StringValueCompletion extends AbstractParseExceptionResolver
{
  @Override
  public boolean canResolve( ParseException pe )
  {
    if( !super.canResolve( pe ) )
    {
      return false;
    }
    IType typeExpected = getExpectedType( pe );
    return typeExpected == JavaTypes.STRING();
  }

  @Override
  public void resolve( ParseException pe )
  {
    StringPopup popup = new StringPopup( "Text", JavaTypes.STRING().getRelativeName(), getGosuEditor().getEditor() );

    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          String s = "\"" + e.getSource().toString() + "\"";
          if( isEditorAtPossibleArgPosition( getDeepestLocationToComplete().getParsedElement(), StringValueCompletion.this.getEditor() ) )
          {
            insertTextAtCaret( s );
          }
          else
          {
            TextComponentUtil.replaceWordAtCaretDynamic( getEditor(), s,
                                                         getGosuEditor().getReplaceWordCallback(), false );
          }
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
    if( typeExpected != JavaTypes.STRING() )
    {
      return false;
    }
    final IParseTree currentLocation = getDeepestLocationToComplete();

    String strValue = null;
    IParsedElement element = currentLocation.getParsedElement();
    if( element instanceof ILiteralExpression )
    {
      strValue = (String)CommonServices.getCoercionManager().convertValue( ((ILiteralExpression)element).evaluate(), TypeSystem.get( String.class ) );
    }
    if( strValue == null )
    {
      strValue = "Text";
    }
    StringPopup popup = new StringPopup( strValue, JavaTypes.STRING().getRelativeName(), getGosuEditor().getEditor() );

    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          getGosuEditor().replaceLocation( currentLocation, "\"" + e.getSource().toString() + "\"" );
          getEditor().requestFocus();
          getGosuEditor().fixSwingFocusBugWhenPopupCloses();
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, true );
    return true;
  }
}
