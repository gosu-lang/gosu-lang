package editor.util;

import javax.swing.*;

/**
 */
public class LabCheckbox extends JCheckBox
{
  public LabCheckbox()
  {
    super();
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( Icon icon )
  {
    super( icon );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( Icon icon, boolean selected )
  {
    super( icon, selected );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( String text )
  {
    super( text );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( Action a )
  {
    super( a );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( String text, boolean selected )
  {
    super( text, selected );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( String text, Icon icon )
  {
    super( text, icon );
    setBorderPaintedFlat( true );
  }

  public LabCheckbox( String text, Icon icon, boolean selected )
  {
    super( text, icon, selected );
    setBorderPaintedFlat( true );
  }
}
