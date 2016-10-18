package editor.search;

/**
 */
public enum SearchScope
{
  CurrentFile( "Current file" ),
  OpenFiles( "Open files" ),
  SelectedFiles( "Selected files" ),
  PreviousSearchFiles( "Files in previous search" );

  private final String _label;

  SearchScope( String label )
  {
    _label = label;
  }

  public String getLabel()
  {
    return _label;
  }

  public String toString()
  {
    return _label;
  }
}
