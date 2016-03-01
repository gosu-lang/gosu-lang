package editor;

/**
 */
public interface IIntelliTextModel
{
  /**
   * Returns a string to insert into a text editor that is representative of the
   * given element in the model.
   */
  public String getInsertionTextFrom( Object element );

  /**
   * Returns the display name for the element.
   */
  public String getDisplayText( Object element );

  /**
   * Returns a displayable string for the type of elements in the model.
   * e.g., "ActivityPattern", "LossCause", "DateTime", anything.
   */
  public String getTypeName();

  /**
   * Returns the element in the model corresponding with the string value.
   * Returns null if there is no corresponding element.
   */
  public Object parseElement( String strValue );

  public String getFilterPrefix();

  public IIntelliTextModel getFilteredModel( String strPrefix );
}