package editor;

import editor.util.EditorUtilities;
import editor.util.SourceFileCreator;
import gw.lang.reflect.gs.ClassType;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 */
public class NewFilePopup extends JPopupMenu
{
  public NewFilePopup()
  {
    addMenuItems( this );
  }

  public static void addMenuItems( JComponent popup )
  {
    addNewItem( popup, "Class", "images/class.png", () -> SourceFileCreator.instance().create( ClassType.Class ) );
    addNewItem( popup, "Enum", "images/enum.png", () -> SourceFileCreator.instance().create( ClassType.Enum ) );
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "Interface", "images/interface.png", () -> SourceFileCreator.instance().create( ClassType.Interface ) );
    addNewItem( popup, "Structure", "images/structure.png", () -> SourceFileCreator.instance().create( ClassType.Structure ) );
    addNewItem( popup, "Annotation", "images/annotation.png", () -> SourceFileCreator.instance().create( ClassType.Annotation ) );
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "Program", "images/program.png", () -> SourceFileCreator.instance().create( ClassType.Program ) );
    addNewItem( popup, "Template", "images/template.png", () -> SourceFileCreator.instance().create( ClassType.Template ) );
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "Enhancement", "images/Enhancement.png", () -> SourceFileCreator.instance().create( ClassType.Enhancement ) );
    addCustomTypes();
  }

  private static void addCustomTypes()
  {
    //## todo: iterate TypeLoaders and add type creators
  }

  private static void addNewItem( JComponent popup, String name, String icon, Runnable action )
  {
    JMenuItem item = new JMenuItem(       
      new AbstractAction( name, EditorUtilities.loadIcon( icon ) )
      {
        public void actionPerformed( ActionEvent e )
        {
          action.run();
        }
      } );
    popup.add( item );
  }
}
