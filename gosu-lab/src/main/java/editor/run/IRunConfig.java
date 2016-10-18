package editor.run;

import gw.lang.reflect.json.IJsonIO;

import javax.swing.*;
import java.util.function.Consumer;

/**
 */
public interface IRunConfig<T extends IRunConfigParameters<T>> extends IJsonIO
{
  T getParams();
  void setParams( T params );

  default String getName()
  {
    return getParams().getName();
  }

  boolean isValid();
  Icon getIcon();

  JComponent makePanel( T params, Consumer<T> changeListener );

  boolean isRunnable();
  IProcessRunner run();

  boolean isDebuggable();
  IProcessRunner debug();
}
