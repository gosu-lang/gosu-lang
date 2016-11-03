package editor.settings;

import editor.LabDarkScheme;
import editor.LabFrame;
import editor.util.Experiment;
import java.util.function.Consumer;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 */
public class AppearanceSettings extends AbstractSettings<AppearanceSettingsParameters>
{
  public static final String PATH = "Appearance";

  public AppearanceSettings()
  {
    super( null, PATH, PATH );
  }

  @Override
  public AppearanceSettingsParameters makeDefaultParameters( Experiment experiment )
  {
    AppearanceSettingsParameters params = new AppearanceSettingsParameters();
    params.setTheme( LabDarkScheme.NAME );
    return params;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public Icon getIcon()
  {
    return null;
  }

  @Override
  public JComponent makePanel( AppearanceSettingsParameters params, Consumer<AppearanceSettingsParameters> changeListener )
  {
    return new AppearanceSettingsPanel( params, changeListener );
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

  public static String getTheme()
  {
    AppearanceSettings settings = (AppearanceSettings)LabFrame.getSettings().get( AppearanceSettings.PATH );
    return settings.getParams().getTheme();
  }
}
