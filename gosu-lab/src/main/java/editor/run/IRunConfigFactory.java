package editor.run;

import javax.swing.*;

/**
 */
public interface IRunConfigFactory<T extends IRunConfig, P extends IRunConfigParameters>
{
  String getName();

  Icon getIcon();

  P makeParameters();

  T newRunConfig( P p );
}
