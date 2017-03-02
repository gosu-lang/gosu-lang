package editor;

import editor.util.EditorUtilities;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

/**
 */
public class BeanMethodCallValueCompletion extends AbstractParseExceptionResolver
{
  public void resolve( ParseException e )
  {
    // ignore
  }

  public boolean handleCompleteValue()
  {
    final IParseTree currentLocation = getDeepestLocationToComplete();
    IParsedElement element = currentLocation.getParsedElement();
    if( !(element instanceof IBeanMethodCallExpression) )
    {
      return false;
    }
    final IBeanMethodCallExpression ma = (IBeanMethodCallExpression)element;
    try
    {
      String strLastMember = ma.getMemberName();
      BeanInfoPopup popup = new BeanInfoPopup( ma.getRootType(), strLastMember, getGosuEditor() );
      popup.setSelection( strLastMember, true );
      popup.addNodeChangeListener(
        new ChangeListener()
        {
          public void stateChanged( ChangeEvent e )
          {
            BeanTree beanInfoSelection = (BeanTree)e.getSource();
            String strRef = beanInfoSelection.makePath( false );

            try
            {
              String strRoot = getGosuEditor().getEditor().getText( currentLocation.getOffset(), ma.getRootExpression().getLocation().getLength() );
              getGosuEditor().replaceLocation( currentLocation, strRoot + '.' + strRef );
            }
            catch( BadLocationException e1 )
            {
              throw new RuntimeException( e1 );
            }
            getEditor().requestFocus();
            EditorUtilities.fixSwingFocusBugWhenPopupCloses( getGosuEditor() );
            getEditor().repaint();
          }
        } );
      getGosuEditor().setBeanInfoPopup( popup );
      getGosuEditor().displayBeanInfoPopup( currentLocation.getOffset() );
    }
    catch( ParseException pe )
    {
      editor.util.EditorUtilities.handleUncaughtException( pe );
    }
    return true;
  }
}
