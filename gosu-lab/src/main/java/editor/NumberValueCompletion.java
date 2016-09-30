package editor;

import editor.util.EditorUtilities;
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
public class NumberValueCompletion extends AbstractParseExceptionResolver
{
  @Override
  public boolean canResolve( ParseException pe )
  {
    if( !super.canResolve( pe ) )
    {
      return false;
    }
    IType typeExpected = getExpectedType( pe );
    return typeExpected != null && TypeSystem.isNumericType( typeExpected );
  }

  @Override
  public void resolve( ParseException pe )
  {
    IType baseType = getBaseType( pe );
    StringPopup popup = new StringPopup( "0", baseType == null ? JavaTypes.NUMBER().getRelativeName() : baseType.getRelativeName(), getGosuEditor().getEditor() );

    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          if( isEditorAtPossibleArgPosition( getDeepestLocationToComplete().getParsedElement(), NumberValueCompletion.this.getEditor() ) )
          {
            insertTextAtCaret( e.getSource().toString() );
          }
          else
          {
            TextComponentUtil.replaceWordAtCaretDynamic( getEditor(), (String)e.getSource(),
                                                         getGosuEditor().getReplaceWordCallback(), false );
          }
          getEditor().requestFocus();
          EditorUtilities.fixSwingFocusBugWhenPopupCloses( getGosuEditor() );
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
    if( !TypeSystem.isNumericType( typeExpected ) )
    {
      return false;
    }
    final IParseTree currentLocation = getDeepestLocationToComplete();

    Double dValue = null;
    IParsedElement element = currentLocation.getParsedElement();
    if( element instanceof ILiteralExpression )
    {
      dValue = (Double)CommonServices.getCoercionManager().convertValue( ((ILiteralExpression)element).evaluate(), TypeSystem.get( Double.class ) );
    }
    StringPopup popup = new StringPopup( dValue == null ? "0" : dValue.toString(), JavaTypes.NUMBER().getRelativeName(), getGosuEditor().getEditor() );
    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          getGosuEditor().replaceLocation( currentLocation, e.getSource().toString() );
          getEditor().requestFocus();
          EditorUtilities.fixSwingFocusBugWhenPopupCloses( getGosuEditor() );
          getEditor().repaint();
        }
      } );
    showValueCompletionPopup( popup, true );
    return true;
  }
}
