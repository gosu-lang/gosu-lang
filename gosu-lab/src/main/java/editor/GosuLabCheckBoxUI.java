package editor;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class GosuLabCheckBoxUI extends GosuLabRadioButtonUI
{
  private static final GosuLabCheckBoxUI INSTANCE = new GosuLabCheckBoxUI();

  private static final String propertyPrefix = "CheckBox" + ".";

  private boolean defaults_initialized = false;

  public static ComponentUI createUI( JComponent c )
  {
    return INSTANCE;
  }


  public String getPropertyPrefix()
  {
    return propertyPrefix;
  }

  public void installDefaults( AbstractButton b )
  {
    super.installDefaults( b );
    if( !defaults_initialized )
    {
      icon = UIManager.getIcon( getPropertyPrefix() + "icon" );
      defaults_initialized = true;
    }
  }

  public void uninstallDefaults( AbstractButton b )
  {
    super.uninstallDefaults( b );
    defaults_initialized = false;
  }

}