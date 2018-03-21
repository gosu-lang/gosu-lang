package editor.settings;

import editor.LabFrame;
import editor.util.Experiment;
import java.nio.file.Path;
import gw.util.PathUtil;
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
    params.setOuputPath( "classes" );
    return params;
  }

  @Override
  public boolean isValid()
  {
    return getParams().isSourceBased() || PathUtil.isDirectory( PathUtil.create( getParams().getOutputPath() ) );
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
    CompilerSettings settings = getCompilerSettings();
    return !settings.getParams().isSourceBased();
  }

  public static Path getCompilerOutputDir()
  {
    if( !isStaticCompile() )
    {
      return null;
    }

    CompilerSettings settings = getCompilerSettings();
    return PathUtil.create( settings.getParams().getOutputPath() );
  }

  private static CompilerSettings getCompilerSettings()
  {
    Experiment experiment = LabFrame.instance().getGosuPanel().getExperiment();
    return (CompilerSettings)experiment.getSettings().get( CompilerSettings.PATH );
  }
}
