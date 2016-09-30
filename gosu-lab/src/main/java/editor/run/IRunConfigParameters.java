package editor.run;

import gw.lang.reflect.json.IJsonIO;

/**
 */
public interface IRunConfigParameters<T extends IRunConfigParameters<T>> extends IJsonIO
{
  String getName();
  void setName( String name );

  void copy( T to );
}
