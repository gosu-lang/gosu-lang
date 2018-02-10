package editor;

import editor.plugin.typeloader.ITypeFactory;
import editor.util.EditorUtilities;
import editor.util.SmartMenuItem;
import editor.util.SourceFileCreator;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import manifold.api.type.ClassType;

import java.awt.EventQueue;
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
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "Namespace", "images/folder.png", () -> SourceFileCreator.instance().createNamespace() );
    popup.add( new JPopupMenu.Separator() );
    EventQueue.invokeLater( () -> addCustomTypes( popup ) );
  }

  private static void addCustomTypes( JComponent popup )
  {
    for( ITypeLoader tl: TypeSystem.getAllTypeLoaders() )
    {
      for( ITypeFactory factory: tl.getInterface( ITypeFactory.class ) )
      {
        if( factory != null && factory.canCreate() )
        {
          addNewItem( popup, factory.getName(), factory.getIcon(), () -> SourceFileCreator.instance().create( factory ) );
        }
      }
    }
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "Java Class", "images/javaclass.png", () -> SourceFileCreator.instance().create( ClassType.JavaClass ) );
    popup.add( new JPopupMenu.Separator() );
    addNewItem( popup, "File", "images/FileText.png", () -> SourceFileCreator.instance().createTextFile() );
  }

  private static void addNewItem( JComponent popup, String name, String icon, Runnable action )
  {
    JMenuItem item = new SmartMenuItem(
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
