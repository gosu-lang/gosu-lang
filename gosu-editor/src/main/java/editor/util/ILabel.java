package editor.util;

import javax.swing.*;

/**
 */
public interface ILabel
{
  public static final int OPEN = 1;

  /**
   * A human readable name, typically displayed as a caption.
   */
  public String getDisplayName();

  /**
   * Supplies an icon for this label
   *
   * @param iTypeFlags one flag specifying the requested icon's type
   *
   * @return an icon for this label
   *
   * @see java.beans.BeanInfo#ICON_COLOR_16x16
   * @see java.beans.BeanInfo#ICON_MONO_16x16
   * @see java.beans.BeanInfo#ICON_COLOR_32x32
   * @see java.beans.BeanInfo#ICON_MONO_32x32
   * @see #OPEN
   */
  public Icon getIcon( int iTypeFlags );
}
