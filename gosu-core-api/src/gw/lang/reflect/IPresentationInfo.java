/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IPresentationInfo
{
  public final static FeatureCategory CATEGORY_BEHAVIOR = new FeatureCategory("Behavior", 0, true);
  public final static FeatureCategory CATEGORY_DESIGN = new FeatureCategory("Design", 1, true);

  /**
   * @return True if the this feature can be presented for editing e.g., in a
   *         property editor.
   */
  public boolean isPresentable();

  /**
   * @return True if this feature is required, and should be called out as such in
   *         a property editor.
   */
  public boolean isRequired();

  /**
   * @return The id of the category under which this feature belongs. Feature
   *         categories facilitate grouping of features for presentation purposes
   *         e.g., in a property editor.
   */
  public FeatureCategory getCategory();

  /**
   * @return True if this feature can be edited in the company of other objects
   *         with this feature. For instance, if this is a font it's likely that it's
   *         ok to edit this font's value along with other objects that are selected so
   *         as to set all their fonts to the same value with one change. Conversely,
   *         if this is a value that probably pertains to just this object, say a
   *         chunk of Gosu, it's probably best to return true here so that the
   *         feature is not editable while other object are selected.
   */
  public boolean isMultiEdit();

  /**
   * @return True if the feature editor for this feature can auto-update while
   *         the user modifies the value. For instance, if this feature is a simple
   *         label text value, auto-update means that the value changes automatically
   *         as the user types. Otherwise, if auto-update is false, the user must
   *         explicitly commit the changes when he is finished typing e.g., via an OK
   *         button.
   */
  public boolean isAutoUpdate();

  /**
   * @return True if this property is considered the primary property for this
   *         object, false otherwise.  Editors may use this information to implement
   *         editing behavior e.g. supporting jumping to the primary property when
   *         editing.
   */
  public boolean isPrimary();

  /**
   * @return A regular expression string specifying which characters are allowed
   *         in the value of this property, or null if no such restriction exists.
   */
  public String getCharacterMask();

  String getDefaultValue();

  public boolean isMultiLine();

  public static final class Default implements IPresentationInfo
  {
    public static final Default GET = new Default();

    private Default()
    {
    }

    public boolean isPresentable()
    {
      return true;
    }

    public boolean isRequired()
    {
      return false;
    }

    public FeatureCategory getCategory()
    {
      return CATEGORY_BEHAVIOR;
    }

    public boolean isMultiEdit()
    {
      return true;
    }

    public boolean isAutoUpdate()
    {
      return true;
    }

    public boolean isPrimary()
    {
      return false;
    }

    public String getCharacterMask()
    {
      return null;
    }

    public String getDefaultValue()
    {
      return null;
    }

    public boolean isMultiLine() {
      return false;
    }
  }
}
