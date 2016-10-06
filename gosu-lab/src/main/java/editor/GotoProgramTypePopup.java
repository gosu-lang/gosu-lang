package editor;

import editor.search.StudioUtilities;
import editor.util.Experiment;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class GotoProgramTypePopup extends GotoTypePopup
{
  public static void display()
  {
    GotoProgramTypePopup valuePopup = new GotoProgramTypePopup( "" );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        doGoTo( strQualifedType );
      } );
    Component host = RunMe.getEditorFrame().getRootPane();
    valuePopup.show( host, 0, 0 );
  }

  public static void display( JTextComponent host, String prefix, Consumer<String> consumer )
  {
    GotoProgramTypePopup valuePopup = new GotoProgramTypePopup( prefix );
    valuePopup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        consumer.accept( strQualifedType );
        host.requestFocus();
      } );
    valuePopup.getNameField().setText( prefix );
    valuePopup.show( StudioUtilities.rootPaneForComponent( host ), 0, 0 );
  }

  public GotoProgramTypePopup( String strPrefix )
  {
    super( "Enter a program name", strPrefix );
  }

  @Override
  protected List<String> initializeData()
  {
    FileTree root = FileTreeUtil.getRoot();
    List<String> progs = new ArrayList<>();
    findProgs( root, progs );
    return progs;
  }

  private void findProgs( FileTree root, List<String> progs )
  {
    if( root.getName().toLowerCase().endsWith( ".gsp" ) )
    {
      progs.add( root.getType().getName() );
    }
    else if( root.isDirectory() )
    {
      for( FileTree tree : root.getChildren() )
      {
        findProgs( tree, progs );
      }
    }
  }

  protected List<String> filterTypes( List<String> types, Experiment experiment )
  {
    return types;
  }

  protected AbstractPopupListModel<String> reconstructModel( String strPrefix )
  {
    if( strPrefix == null || strPrefix.isEmpty() )
    {
      // Show all programs
      return new TypeModel( getInitializedAllData() );
    }
    // Filter by prefix
    return super.reconstructModel( strPrefix );
  }

}
