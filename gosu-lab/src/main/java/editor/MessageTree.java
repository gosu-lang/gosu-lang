package editor;

import editor.util.EditorUtilities;
import gw.lang.parser.IParseIssue;
import gw.lang.reflect.gs.IGosuClass;

import javax.swing.*;
import java.awt.*;

/**
 */
public class MessageTree extends AbstractTree<MessageTree, MessageTree.IssueNode>
{
  public MessageTree( JTree tree )
  {
    super( tree );
  }
  public MessageTree( String text, NodeKind kind, IssueNode data )
  {
    super( text, kind, data );
  }

  public Icon getIcon()
  {
    return findIcon();
  }

  private Icon findIcon()
  {
    return hasFailures()
           ? EditorUtilities.loadIcon( "images/failure.png" )
           : hasErrors()
             ? EditorUtilities.loadIcon( "images/error.png" )
             : hasWarnings()
               ? EditorUtilities.loadIcon( "images/warning.png" )
               : EditorUtilities.loadIcon( "images/info.png" );
  }

  public JTree getTree()
  {
    return RunMe.getEditorFrame().getGosuPanel().getMessagesPanel().getTree();
  }

  public static IssueNode makeIssueMessage( IParseIssue issue )
  {
    return new IssueNode( issue );
  }

  public static IssueNode empty()
  {
    return new IssueNode( null ) {
      @Override
      public boolean hasTarget()
      {
        return false;
      }
    };
  }

  static class IssueNode implements ITreeNode
  {
    private final IParseIssue _issue;

    public IssueNode( IParseIssue issue )
    {
      _issue = issue;
    }

    @Override
    public boolean hasTarget()
    {
      return true;
    }

    @Override
    public void jumpToTarget()
    {
      IGosuClass gsClass = _issue.getSource().getGosuClass();
      gsClass = getOuterMostEnclosingClass( gsClass );

      RunMe.getEditorFrame().getGosuPanel().openType( gsClass.getName(), true );
      EventQueue.invokeLater( () ->
                                RunMe.getEditorFrame().getGosuPanel().getCurrentEditor().gotoLine( _issue.getLine(), _issue.getColumn() ) );
    }

    private IGosuClass getOuterMostEnclosingClass( IGosuClass innerClass )
    {
      IGosuClass outerMost = innerClass;
      while( outerMost != null && outerMost.getEnclosingType() != null )
      {
        outerMost = (IGosuClass)outerMost.getEnclosingType();
      }
      return outerMost;
    }

  }
}
