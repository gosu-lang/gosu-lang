package editor.settings;

import editor.util.Experiment;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class Settings
{
  /**
   * Reset Experiment-specific settings to defaults.
   */
  public static Map<String, ISettings> makeDefaultSettings( Experiment experiment )
  {
    Map<String, ISettings> settings = new TreeMap<>();

    CompilerSettings compilerSettings = new CompilerSettings();
    compilerSettings.resetToDefaultSettings( experiment );
    settings.put( compilerSettings.getPath(), compilerSettings );

    return settings;
  }

  /**
   * Reset Gosu Lab application-level settings to defaults.
   */
  public static Map<String, ISettings> makeDefaultSettings()
  {
    Map<String, ISettings> settings = new TreeMap<>();

    AppearanceSettings appearanceSettings = new AppearanceSettings();
    appearanceSettings.resetToDefaultSettings( null );
    settings.put( appearanceSettings.getPath(), appearanceSettings );

    return settings;
  }

  /**
   * Assumes settings map is ordered top-down in tree order
   */
  public static Map<String, ISettings> mergeSettings( Map<String, ISettings> old, Experiment experiment )
  {
    Map<String, ISettings> defaultSettings = makeDefaultSettings( experiment );
    old.keySet().forEach( key -> defaultSettings.put( key, old.get( key ) ) );
    return defaultSettings;
  }

  public static Map<String, ISettings> mergeSettings( Map<String, ISettings> old )
  {
    Map<String, ISettings> defaultSettings = makeDefaultSettings();
    old.keySet().forEach( key -> defaultSettings.put( key, old.get( key ) ) );
    return defaultSettings;
  }
}
