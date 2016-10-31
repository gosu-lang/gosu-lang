package editor.settings;

/**
 */
public class CompilerSettingsParameters extends AbstractSettingsParameters<CompilerSettingsParameters>
{
  private boolean _sourceBased;
  private String _ouputPath;


  public CompilerSettingsParameters()
  {
  }

  public boolean isSourceBased()
  {
    return _sourceBased;
  }
  public void setSourceBased( boolean sourceBased )
  {
    _sourceBased = sourceBased;
  }

  public String getOutputPath()
  {
    return _ouputPath;
  }
  public void setOuputPath( String outputPath )
  {
    _ouputPath = outputPath;
  }

  @Override
  public void copy( CompilerSettingsParameters to )
  {
    to.setSourceBased( isSourceBased() );
    to.setOuputPath( getOutputPath() );
  }
}
