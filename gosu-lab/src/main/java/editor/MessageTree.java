package editor;

import editor.util.EditorUtilities;
import gw.lang.parser.IParseIssue;
import gw.lang.reflect.IType;

import javax.swing.*;
import java.awt.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

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
    return LabFrame.instance().getGosuPanel().getMessagesPanel().getTree();
  }

  public static IssueNode makeIssueMessage( IParseIssue issue )
  {
    return new IssueNode( issue );
  }

  public static IssueNode makeIssueMessage( Diagnostic<? extends JavaFileObject> issue, IType type )
  {
    return new IssueNode( issue, type );
  }

  public static IssueNode empty()
  {
    return IssueNode.EMPTY;
  }

  static class IssueNode implements ITreeNode
  {
    private static final IssueNode EMPTY = new IssueNode();

    private final int _line;
    private final int _column;
    private final String _fqn;

    private IssueNode()
    {
      _line = -1;
      _column = -1;
      _fqn = null;
    }

    public IssueNode( IParseIssue issue )
    {
      _line = issue.getLine();
      _column = issue.getColumn();
      _fqn =  getOuterMostEnclosingClass( issue.getSource().getGosuClass() ).getName();
    }

    public IssueNode( Diagnostic<? extends JavaFileObject> issue, IType type )
    {
      _line = (int)issue.getLineNumber();
      _column = (int)issue.getColumnNumber();
      _fqn =  getOuterMostEnclosingClass( type ).getName();
    }

    @Override
    public boolean hasTarget()
    {
      return _fqn != null;
    }

    @Override
    public void jumpToTarget()
    {
      LabFrame.instance().getGosuPanel().openType( _fqn, true );
      EventQueue.invokeLater( () -> LabFrame.instance().getGosuPanel().getCurrentEditor().gotoLine( _line, _column ) );
    }

    private IType getOuterMostEnclosingClass( IType innerClass )
    {
      IType outerMost = innerClass;
      while( outerMost != null && outerMost.getEnclosingType() != null )
      {
        outerMost = outerMost.getEnclosingType();
      }
      return outerMost;
    }

  }
}
