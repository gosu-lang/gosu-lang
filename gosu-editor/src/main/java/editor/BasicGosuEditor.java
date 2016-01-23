package editor;

import editor.util.Project;
import gw.config.CommonServices;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.module.IFileSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class BasicGosuEditor extends JFrame implements IGosuEditor
{
  private GosuPanel _panel;

  public BasicGosuEditor() throws HeadlessException
  {
    super( "Gosu Editor" );
    configUI();
    setInitialSize();
    addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          exit();
        }
      } );
  }

  public void exit()
  {
    if( _panel.saveIfDirty() )
    {
      System.exit( 0 );
    }
  }

  private void configUI()
  {
    setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
    setIconImage( editor.util.EditorUtilities.loadIcon( "images/Guidewire_icon_16.gif" ).getImage() );
    _panel = new GosuPanel( this );
    Container contentPane = getContentPane();
    contentPane.setLayout( new BorderLayout() );
    contentPane.add( _panel, BorderLayout.CENTER );
  }

  private void setInitialSize()
  {
    GraphicsConfiguration configuration = getRootPane().getGraphicsConfiguration();
    Rectangle bounds = configuration.getBounds();
    int width = Math.max( 700, (int)((double)bounds.width * .7) );
    int height = Math.max( 700, (int)((double)bounds.height * .7) );
    setSize( width, height );
    setLocationRelativeTo( null );
    _panel.setSplitPosition( 60 );
  }

  public void reset()
  {
    if( EventQueue.isDispatchThread() )
    {
      resetNow();
      return;
    }

    try
    {
      EventQueue.invokeAndWait(
        new Runnable()
        {
          public void run()
          {
            resetNow();
          }
        } );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private void resetNow()
  {
    _panel.clearTabs();
  }

  public void openInitialFile( IScriptPartId partId, File file )
  {
    _panel.openInitialFile( partId, file );
  }

  public void openFile( File anySourceFile )
  {
    _panel.openFile( anySourceFile );
  }

  @Override
  public void showMe()
  {
    setVisible( true );

    GosuEventQueue.instance().run();
  }

  public GosuPanel getGosuPanel()
  {
    return _panel;
  }

  public void restoreState( Project project )
  {
    _panel.restoreProjectState( project );
  }

  public void selectTab( Object contentId )
  {
    _panel.selectTab( (File)contentId );
  }

  public void closeTab( Object contentId )
  {
    _panel.closeTab( (File)contentId );
  }

  public static BasicGosuEditor create()
  {
    GosuWindowsLAF.setLookAndFeel();
    CommonServices.getFileSystem().setCachingMode( IFileSystem.CachingMode.NO_CACHING );
    return new BasicGosuEditor();
  }
}
