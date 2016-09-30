package editor.run;


import editor.util.EditorUtilities;

import javax.swing.*;

/**
 */
public class ProgramRunConfig extends FqnRunConfig<ProgramRunConfigParameters>
{
  public ProgramRunConfig( ProgramRunConfigParameters params )
  {
    super( params );
  }

  // this if for reading from json
  @SuppressWarnings("UnusedDeclaration")
  public ProgramRunConfig()
  {
  }

  @Override
  public Icon getIcon()
  {
    return EditorUtilities.loadIcon( "images/program.png" );
  }

  @Override
  public JComponent makePanel( ProgramRunConfigParameters params )
  {
    return new ProgramConfigPanel( params );
  }
}
