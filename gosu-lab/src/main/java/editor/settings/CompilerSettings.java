package editor.settings;

import editor.LabFrame;
import editor.util.Experiment;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 */
public class CompilerSettings extends AbstractSettings<CompilerSettingsParameters>
{
  public static final String PATH = "Compiler";

  public CompilerSettings()
  {
    super( null, PATH, PATH );
  }

  @Override
  public CompilerSettingsParameters makeDefaultParameters( Experiment experiment )
  {
    CompilerSettingsParameters params = new CompilerSettingsParameters();
    params.setSourceBased( true );
    params.setOuputPath( experiment.getExperimentDir().getAbsolutePath() + File.separatorChar + "classes" );
    return params;
  }

  @Override
  public boolean isValid()
  {
    return getParams().isSourceBased() || new File( getParams().getOutputPath() ).isDirectory();
  }

  @Override
  public Icon getIcon()
  {
    return null;
  }

  @Override
  public JComponent makePanel( CompilerSettingsParameters params, Consumer<CompilerSettingsParameters> changeListener )
  {
    return new CompilerSettingsPanel( params, changeListener );
  }

  @Override
  public boolean isExperimentSetting()
  {
    return true;
  }

  @Override
  public boolean isIdeSetting()
  {
    return false;
  }

  public static boolean isStaticCompile()
  {
    Experiment experiment = LabFrame.instance().getGosuPanel().getExperiment();
    CompilerSettings settings = (CompilerSettings)experiment.getSettings().get( CompilerSettings.PATH );
    return !settings.getParams().isSourceBased();
  }

  public static File getCompilerOutputDir()
  {
    if( !isStaticCompile() )
    {
      return null;
    }

    Experiment experiment = LabFrame.instance().getGosuPanel().getExperiment();
    CompilerSettings settings = (CompilerSettings)experiment.getSettings().get( CompilerSettings.PATH );
    return new File( settings.getParams().getOutputPath() );
  }
}
