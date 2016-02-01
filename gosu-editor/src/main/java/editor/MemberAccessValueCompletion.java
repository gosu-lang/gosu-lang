package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.util.IFeatureFilter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 */
public class MemberAccessValueCompletion extends AbstractParseExceptionResolver
{
  public void resolve( ParseException e )
  {
    // ignore
  }

  public boolean handleCompleteValue()
  {
    IParseTree currentLocation = getDeepestLocationToComplete();
    IParsedElement element = currentLocation.getParsedElement();
    if( !(element instanceof IFieldAccessExpression) )
    {
      return false;
    }
    IFieldAccessExpression ma = (IFieldAccessExpression)element;
    try
    {
      String strLastMember = ma.getMemberName();
      IFeatureFilter filter = null;
      BeanInfoPopup popup = new BeanInfoPopup( ma.getRootType(), strLastMember, false, getGosuEditor(), filter );
      popup.setSelection( strLastMember, true );
      popup.addNodeChangeListener(
        new ChangeListener()
        {
          public void stateChanged( ChangeEvent e )
          {
            BeanTree beanInfoSelection = (BeanTree)e.getSource();
            String strRef = beanInfoSelection.makePath( false );
            TextComponentUtil.replaceWordAtClosestDot( getEditor(), strRef );
            getEditor().requestFocus();
            getGosuEditor().fixSwingFocusBugWhenPopupCloses();
            getEditor().repaint();
          }
        } );
      getGosuEditor().setBeanInfoPopup( popup );
      getGosuEditor().displayBeanInfoPopup( currentLocation.getOffset() + currentLocation.getLength() );
    }
    catch( ParseException pe )
    {
      editor.util.EditorUtilities.handleUncaughtException( pe );
    }
    return true;
  }
}
