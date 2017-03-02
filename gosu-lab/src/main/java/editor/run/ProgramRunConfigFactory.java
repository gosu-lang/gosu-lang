package editor.run;

import editor.util.EditorUtilities;

import javax.swing.*;

/**
 */
public class ProgramRunConfigFactory implements IRunConfigFactory<ProgramRunConfig, ProgramRunConfigParameters>
{
  private static ProgramRunConfigFactory INSTANCE = new ProgramRunConfigFactory();

  public static ProgramRunConfigFactory instance()
  {
    return INSTANCE;
  }

  @Override
  public String getName()
  {
    return "Program";
  }

  @Override
  public ProgramRunConfigParameters makeParameters()
  {
    return new ProgramRunConfigParameters();
  }

  @Override
  public ProgramRunConfig newRunConfig( ProgramRunConfigParameters params )
  {
    return new ProgramRunConfig( params );
  }

  @Override
  public Icon getIcon()
  {
    return EditorUtilities.loadIcon( "images/program.png" );
  }
}
