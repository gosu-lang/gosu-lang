package editor;

import editor.util.EditorUtilities;
import editor.util.Experiment;
import gw.config.CommonServices;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.module.IFileSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BasicGosuEditor extends JFrame implements IGosuEditor
{
  private GosuPanel _panel;
  private Rectangle _restoreBounds;
  private List<String> _experiments = Collections.emptyList();

  public BasicGosuEditor() throws HeadlessException
  {
    super( "Gosu Editor" );
    RunMe.setEditorFrame( this );
    configUI();
    setInitialSize();
    addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          exit();
        }

        @Override
        public void windowActivated( WindowEvent e )
        {
          EventQueue.invokeLater( ()-> {
            GosuEditor currentEditor = _panel.getCurrentEditor();
            if( currentEditor != null )
            {
              currentEditor.parse();
            }
          } );
        }
        @Override
        public void windowDeactivated( WindowEvent e )
        {
          EventQueue.invokeLater( _panel::saveIfDirty );
        }
      } );
    addComponentListener(
      new ComponentAdapter()
      {
        @Override
        public void componentResized( ComponentEvent e )
        {
          if( (getExtendedState() & Frame.MAXIMIZED_BOTH) != MAXIMIZED_BOTH )
          {
            _restoreBounds = getBounds();
          }
        }

        @Override
        public void componentMoved( ComponentEvent e )
        {
          if( (getExtendedState() & Frame.MAXIMIZED_BOTH) != MAXIMIZED_BOTH )
          {
            _restoreBounds = getBounds();
          }
        }
      });
  }

  public void exit()
  {
    EditorUtilities.saveLayoutState( _panel.getExperimentView().getExperiment() );

    if( _panel.saveIfDirty() )
    {
      getGosuPanel().killProcess();

      System.exit( 0 );
    }
  }

  private void configUI()
  {
    setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
    setIconImage( EditorUtilities.loadLabIcon().getImage() );
    _panel = new GosuPanel( this );
    Container contentPane = getContentPane();
    contentPane.setLayout( new BorderLayout() );
    contentPane.add( _panel, BorderLayout.CENTER );
  }

  private void setInitialSize()
  {
    _panel.setEditorSplitPosition( 20 );
    _panel.setExperimentSplitPosition( 75 );
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
      EventQueue.invokeAndWait( this::resetNow );
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
    _panel.openFile( anySourceFile, true );
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

  public void restoreState( Experiment experiment )
  {
    _panel.restoreExperimentState( experiment );
  }

  public void selectTab( Object contentId )
  {
    _panel.selectTab( (File)contentId );
  }

  public void closeTab( Object contentId )
  {
    _panel.closeTab( (File)contentId );
  }

  public Rectangle getRestoreBounds()
  {
    return new Rectangle( _restoreBounds );
  }
  public void setRestoreBounds( Rectangle restoreBounds )
  {
    _restoreBounds = restoreBounds;
  }

  public List<String> getExperiments()
  {
    return _experiments;
  }
  public void setExperiments( List<String> experiments )
  {
    _experiments = experiments;
    for( Iterator<String> iter = experiments.iterator(); iter.hasNext(); )
    {
      String exp = iter.next();
      if( !new File( exp ).exists() )
      {
        iter.remove();
      }
    }
  }
  public void addExperiment( Experiment exp )
  {
    String dir = exp.getExperimentDir().getAbsolutePath();
    if( _experiments.isEmpty() )
    {
      _experiments = new ArrayList<>();
      _experiments.add( dir );
    }
    else
    {
      if( _experiments.contains( dir ) )
      {
        _experiments.remove( dir );
      }
      _experiments.add( 0, dir );
    }
  }

  public static BasicGosuEditor create()
  {
    GosuLabLAF.setLookAndFeel();
    CommonServices.getFileSystem().setCachingMode( IFileSystem.CachingMode.NO_CACHING );
    return new BasicGosuEditor();
  }

  //## todo: dynamically update Gosu Lab
  public void checkForUpdate( GosuPanel gosuPanel )
  {
    try
    {
      File userFile = EditorUtilities.getUserFile( gosuPanel );
      if( !userFile.exists() || EditorUtilities.getVersion( gosuPanel ) < 1 )
      {
        deleteDir( EditorUtilities.getUserGosuEditorDir() );
      }
    }
    catch( Exception e )
    {
      deleteDir( EditorUtilities.getUserGosuEditorDir() );
    }
  }

  private static void deleteDir( File fileOrDirectory )
  {
    if( fileOrDirectory.isDirectory() )
    {
      for( File child : fileOrDirectory.listFiles() )
      {
        deleteDir( child );
      }
    }
    //noinspection ResultOfMethodCallIgnored
    fileOrDirectory.delete();
  }

}
