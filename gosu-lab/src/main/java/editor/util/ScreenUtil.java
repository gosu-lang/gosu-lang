package editor.util;

import java.awt.*;

/**
 */
public interface ScreenUtil
{
  public static void convertToPercentageOfScreenWidth( Rectangle rcBounds )
  {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    rcBounds.x = (int)(((float)rcBounds.x / dim.width) * 10000);
    rcBounds.y = (int)(((float)rcBounds.y / dim.height) * 10000);
    rcBounds.width = (int)(((float)rcBounds.width / dim.width) * 10000);
    rcBounds.height = (int)(((float)rcBounds.height / dim.height) * 10000);
  }

  public static void convertFromPercentageOfScreenWidth( Rectangle rcBounds )
  {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    rcBounds.x = (int)(((float)rcBounds.x / 10000) * dim.width);
    rcBounds.y = (int)(((float)rcBounds.y / 10000) * dim.height);
    rcBounds.width = (int)(((float)rcBounds.width / 10000) * dim.width);
    rcBounds.height = (int)(((float)rcBounds.height / 10000) * dim.height);
  }
}
