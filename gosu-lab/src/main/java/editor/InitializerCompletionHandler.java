package editor;

import editor.util.EditorUtilities;
import editor.util.TextComponentUtil;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IInitializerAssignment;
import gw.lang.parser.expressions.IObjectInitializerExpression;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.IFeatureFilter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;

public class InitializerCompletionHandler extends AbstractPathCompletionHandler
{
  @Override
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    final GosuEditor gsEditor = getGosuEditor();
    IParsedElement parsedElement = null;

    if( isInitializerStart( getGosuEditor().getExpressionAtCaret() ) )
    {
      parsedElement = getGosuEditor().getExpressionAtCaret();
    }

    if( isDoubleColonAtCaret() )
    {
      parsedElement = getGosuEditor().getDeepestLocationAtCaret().getParsedElement();
    }

    if( parsedElement != null )
    {
      IObjectInitializerExpression oie = findObjectInitializerExpression( parsedElement );
      IType type = oie.getType();
      String strMemberPath = getSingleNameAtCaret();
      strMemberPath = strMemberPath != null ? strMemberPath.trim() : null;

      BeanInfoPopup valuePopup;
      try
      {
        valuePopup = new BeanInfoPopup( type, strMemberPath, true, gsEditor, new IFeatureFilter()
        {
          @Override
          public boolean acceptFeature( IType beanType, IFeatureInfo fi )
          {
            if( fi instanceof IPropertyInfo )
            {
              IPropertyInfo pi = (IPropertyInfo)fi;
              if( pi.isWritable( null ) ||
                  JavaTypes.COLLECTION().isAssignableFrom( pi.getFeatureType() ) ||
                  JavaTypes.MAP().isAssignableFrom( pi.getFeatureType() ) )
              {
                return true;
              }
            }
            return false;
          }
        } );
      }
      catch( ParseException e )
      {
        return false;
      }

      valuePopup.addNodeChangeListener(
        new ChangeListener()
        {
          @Override
          public void stateChanged( ChangeEvent e )
          {
            BeanTree beanTree = (BeanTree)e.getSource();
            GosuEditorPane editor = gsEditor.getEditor();
            editor.select( TextComponentUtil.findCharacterPositionOnLine( editor.getCaretPosition() - 1, editor.getText(), ':', TextComponentUtil.Direction.BACKWARD ) + 1, editor.getCaretPosition() );
            editor.replaceSelection( beanTree.getBeanNode().getName() + " = " );
            editor.requestFocus();
            EditorUtilities.fixSwingFocusBugWhenPopupCloses( gsEditor );
            editor.repaint();
          }
        } );
      gsEditor.setValuePopup( valuePopup );
      gsEditor.displayValuePopup();
      return true;

    }

    return false;
  }

  private boolean isDoubleColonAtCaret()
  {
    if( ":".equals( TextComponentUtil.getWordBeforeCaret( getGosuEditor().getEditor() ) ) )
    {
      return getGosuEditor().getDeepestLocationAtCaret() != null &&
             getGosuEditor().getDeepestLocationAtCaret().getParsedElement() instanceof IInitializerAssignment &&
             getGosuEditor().getExpressionAtCaret() == null;
    }
    return false;
  }

  private IObjectInitializerExpression findObjectInitializerExpression( IParsedElement parsedElement )
  {
    if( parsedElement instanceof IObjectInitializerExpression )
    {
      return (IObjectInitializerExpression)parsedElement;
    }
    else if( parsedElement.getParent() != null )
    {
      return findObjectInitializerExpression( parsedElement.getParent() );
    }
    else
    {
      return null;
    }
  }

  public static boolean isInitializerStart( IParsedElement parsedElement )
  {
    if( parsedElement instanceof IInitializerAssignment )
    {
      IInitializerAssignment iia = (IInitializerAssignment)parsedElement;
      if( parsedElement instanceof IIdentifierExpression )
      {
        return true;
      }
      else
      {
        List<IParseIssue> exceptions = iia.getParseExceptions();
        for( IParseIssue exception : exceptions )
        {
          if( exception.getMessageKey().equals( Res.MSG_EQUALS_FOR_INITIALIZER_EXPR ) )
          {
            return true;
          }
        }
        return false;
      }
    }
    else
    {
      return parsedElement != null && isInitializerStart( parsedElement.getParent() );
    }
  }

}