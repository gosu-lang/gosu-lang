package editor;


import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.SettleModalEventQueue;
import gw.config.CommonServices;
import gw.config.IPlatformHelper;
import gw.lang.Gosu;
import gw.lang.gosuc.GosucDependency;
import gw.lang.gosuc.GosucModule;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IExecutionEnvironment;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RunMe
{
  private static BasicGosuEditor _gosuEditor;
  private static GosuInitialization _gosuInitialization;

  public static void main( String[] args ) throws Exception
  {
    launchEditor();
  }

  public static BasicGosuEditor getEditorFrame()
  {
    return _gosuEditor;
  }

  public static void launchEditor() throws Exception
  {
    EventQueue.invokeLater(
      () -> {
        SplashScreen.instance().setFeedbackText( "Initializing..." );
        _gosuEditor = BasicGosuEditor.create();
        _gosuEditor.restoreState( EditorUtilities.loadRecentExperiment( _gosuEditor.getGosuPanel() ) );
        SettleModalEventQueue.instance().run();
        SplashScreen.instance().dispose();
        _gosuEditor.showMe();
      } );
  }

  public static void reinitializeGosu( Experiment experiment )
  {
    CommonServices.getKernel().redefineService_Privileged( IPlatformHelper.class, new GosuEditorPlatformHelper() );

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
    _gosuInitialization = GosuInitialization.instance( execEnv );
    GosucModule gosucModule = new GosucModule(
      IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME, experiment.getSourcePath(), deriveClasspath( experiment ),
      "", Collections.<GosucDependency>emptyList(), Collections.<String>emptyList() );
    _gosuInitialization.reinitializeSimpleIde( gosucModule );
  }

  private static List<String> deriveClasspath( Experiment experiment )
  {
    List<String> classpath = new ArrayList<>();
    List<String> sourcePath = experiment.getSourcePath();
    for( String path: sourcePath )
    {
      if( !path.toLowerCase().startsWith( experiment.getExperimentDir().getAbsolutePath().toLowerCase() + File.separator ) )
      {
        classpath.add( path );
      }
    }
    List<String> collect = Gosu.deriveClasspathFrom( RunMe.class ).stream().map( File::getAbsolutePath ).collect( Collectors.toList() );
    classpath.addAll( collect );
    return classpath;
  }
}
