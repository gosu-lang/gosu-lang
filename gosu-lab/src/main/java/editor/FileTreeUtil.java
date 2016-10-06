package editor;

import javax.swing.tree.TreeModel;

/**
 */
public class FileTreeUtil
{
  public static FileTree find( String fqn )
  {
    TreeModel model = getExperimentView().getTree().getModel();
    FileTree root = (FileTree)model.getRoot();
    return root.find( fqn );
  }

  public static ExperimentView getExperimentView()
  {
    return getGosuPanel() == null ? null : getGosuPanel().getExperimentView();
  }

  public static GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  public static FileTree getRoot()
  {
    ExperimentView experimentView = getExperimentView();
    if( experimentView == null )
    {
      return null;
    }
    TreeModel model = experimentView.getTree().getModel();
    return (FileTree)model.getRoot();
  }
}
