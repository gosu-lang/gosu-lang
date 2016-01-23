package editor;

import editor.util.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 */
public class ProjectView extends JPanel
{
  private Project _project;
  private JTree _tree;

  public ProjectView()
  {
    setBorder( new EmptyBorder( 1, 2, 2, 2 ) );
  }

  public void load( Project project )
  {
    _project = project;
    DefaultTreeModel model = new DefaultTreeModel( new FileTree( getProject() ) );
    _tree = new JTree( model );
    JScrollPane scroller = new JScrollPane( _tree );
    setLayout( new BorderLayout() );
    add( scroller, BorderLayout.CENTER );
  }

  public JTree getTree()
  {
    return _tree;
  }

  public Project getProject()
  {
    return _project;
  }

}
