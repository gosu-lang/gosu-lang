package editor;


import editor.util.Experiment;
import gw.util.PathUtil;
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
/*
  To enable "Mark Errors For Gosu Language Test"
  -Dspec=true
 */
public class RunMe
{
  public static void main( String[] args ) throws Exception
  {
    launchEditor();
  }

  public static void launchEditor() throws Exception
  {
    EventQueue.invokeLater(
      () -> {
        LabFrame.loadSettings();
        SplashScreen.instance().setFeedbackText( "Initializing..." );
        LabFrame.create();
        reinitializeGosu( null ); // this is so we can use Gosu to write Gosu Lab :) (right now we are only using the Json stuff)
        LabFrame.instance().checkForUpdate( LabFrame.instance().getGosuPanel() );
        LabFrame.instance().restoreState( LabFrame.instance().loadRecentExperiment( LabFrame.instance().getGosuPanel() ) );
        SettleModalEventQueue.instance().run();
        SplashScreen.instance().dispose();
        LabFrame.instance().showMe();
      } );
  }

  public static void reinitializeGosu( Experiment experiment )
  {
    CommonServices.getKernel().redefineService_Privileged( IPlatformHelper.class, new GosuEditorPlatformHelper() );

    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment();
    GosuInitialization gosuInitialization = GosuInitialization.instance( execEnv );
    GosucModule gosucModule = new GosucModule( IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME,
                                               experiment == null ? Collections.emptyList() : experiment.getSourcePath(),
                                               deriveClasspath( experiment ),
                                               deriveBackingSourcePath( experiment ),
                                               "",
                                               Collections.<GosucDependency>emptyList(),
                                               Collections.<String>emptyList() );
    gosuInitialization.reinitializeSimpleIde( gosucModule );
  }

  private static List<String> deriveClasspath( Experiment experiment )
  {
    List<String> classpath = new ArrayList<>();
    List<String> sourcePath = experiment == null ? Collections.emptyList() : experiment.getSourcePath();
    for( String path: sourcePath )
    {
      if( !path.toLowerCase().startsWith( PathUtil.getAbsolutePathName( experiment.getExperimentDir() ).toLowerCase() + File.separator ) )
      {
        classpath.add( path );
      }
    }
    List<String> collect = Gosu.deriveClasspathFrom( LabFrame.class ).stream().map( File::getAbsolutePath ).collect( Collectors.toList() );
    classpath.addAll( collect );
    return classpath;
  }

  private static List<String> deriveBackingSourcePath( Experiment experiment )
  {
    List<String> backingSource = new ArrayList<>();
    List<String> sourcePath = experiment == null ? Collections.emptyList() : experiment.getBackingSourcePath();
    for( String path: sourcePath )
    {
      if( !path.toLowerCase().startsWith( PathUtil.getAbsolutePathName( experiment.getExperimentDir() ).toLowerCase() + File.separator ) )
      {
        backingSource.add( path );
      }
    }
    List<String> collect = Gosu.findJreSourcePath();
    backingSource.addAll( collect );
    backingSource = new ArrayList<>( new HashSet<>( collect ) );

    return backingSource;
  }
}
