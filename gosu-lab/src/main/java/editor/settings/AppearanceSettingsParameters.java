package editor.settings;

/**
 */
public class AppearanceSettingsParameters extends AbstractSettingsParameters<AppearanceSettingsParameters>
{
  private String _theme;


  public AppearanceSettingsParameters()
  {
  }

  public String getTheme()
  {
    return _theme;
  }
  public void setTheme( String theme )
  {
    _theme = theme;
  }

  @Override
  public void copy( AppearanceSettingsParameters to )
  {
    to.setTheme( getTheme() );
  }
}
