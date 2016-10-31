package editor.settings;

import gw.lang.reflect.json.IJsonIO;

/**
 */
public interface ISettingsParameters<T extends ISettingsParameters<T>> extends IJsonIO
{
  void copy( T to );
}
