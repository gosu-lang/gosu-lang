package editor.util;

import java.awt.*;
import java.awt.image.RGBImageFilter;


/**
 * An image filter that replaces occurrences of default System Colors to current System Color
 * settings. Following are default ISC.S_STANDARD System Colors:
 * <ul>
 * <li> standard surface    = rgb(192,192,192)
 * <li> standard shadow     = rgb(128,128,128)
 * <li> standard highlight  = rgb(255,255,255) to current System Color settings.
 * </ul>
 * <p/>
 * For a fast, easy way to create a new System Color image use the
 * <a href="sterling.ei.client.api.util.Utility.html#createSystemColorImage"><b>Utility.createSystemColorImage</b></a>
 * utility method.
 */
public class SystemColorFilter extends RGBImageFilter
{
  private Color _clrFace;
  private Color _clrHighlight;
  private Color _clrShadow;

  private float _fFudgeFactor = .05f;

  /**
   *
   */
  public SystemColorFilter()
  {
    canFilterIndexColorModel = true;

    _clrFace = new Color( 204, 204, 203 );
    _clrHighlight = new Color( 255, 255, 255 );
    _clrShadow = new Color( 153, 153, 153 );
  }

  /**
   *
   * @param clrFace
   * @param clrHighlight
   * @param clrShadow
   * @param fFudgeFactor
   */
  public SystemColorFilter( Color clrFace, Color clrHighlight, Color clrShadow, float fFudgeFactor )
  {
    canFilterIndexColorModel = true;

    _clrFace = clrFace;
    _clrHighlight = clrHighlight;
    _clrShadow = clrShadow;

    _fFudgeFactor = fFudgeFactor;
  }

  /**
   *
   * @param x
   * @param y
   * @param rgb
   * @return
   */
  public int filterRGB( int x, int y, int rgb )
  {
    int r = (rgb & 0xff0000) >> 16;
    int g = (rgb & 0xff00) >> 8;
    int b = (rgb & 0xff);

    if( (r >= _clrFace.getRed() - _fFudgeFactor * _clrFace.getRed() && r <= _clrFace.getRed() + _fFudgeFactor * _clrFace.getRed()) &&
        (g >= _clrFace.getGreen() - _fFudgeFactor * _clrFace.getGreen() && g <= _clrFace.getGreen() + _fFudgeFactor * _clrFace.getGreen()) &&
        (b >= _clrFace.getBlue() - _fFudgeFactor * _clrFace.getBlue() && b <= _clrFace.getBlue() + _fFudgeFactor * _clrFace.getBlue()) )
    {
      r = editor.util.EditorUtilities.CONTROL.getRed();
      g = editor.util.EditorUtilities.CONTROL.getGreen();
      b = editor.util.EditorUtilities.CONTROL.getBlue();
    }
    else if( (r >= _clrHighlight.getRed() - _fFudgeFactor * _clrHighlight.getRed() && r <= _clrHighlight.getRed() + _fFudgeFactor * _clrHighlight.getRed()) &&
             (g >= _clrHighlight.getGreen() - _fFudgeFactor * _clrHighlight.getGreen() && g <= _clrHighlight.getGreen() + _fFudgeFactor * _clrHighlight.getGreen()) &&
             (b >= _clrHighlight.getBlue() - _fFudgeFactor * _clrHighlight.getBlue() && b <= _clrHighlight.getBlue() + _fFudgeFactor * _clrHighlight.getBlue()) )
    {
      r = editor.util.EditorUtilities.CONTROL_HIGHLIGHT.getRed();
      g = editor.util.EditorUtilities.CONTROL_HIGHLIGHT.getGreen();
      b = editor.util.EditorUtilities.CONTROL_HIGHLIGHT.getBlue();
    }
    else if( (r >= _clrShadow.getRed() - _fFudgeFactor * _clrShadow.getRed() && r <= _clrShadow.getRed() + _fFudgeFactor * _clrShadow.getRed()) &&
             (g >= _clrShadow.getGreen() - _fFudgeFactor * _clrShadow.getGreen() && g <= _clrShadow.getGreen() + _fFudgeFactor * _clrShadow.getGreen()) &&
             (b >= _clrShadow.getBlue() - _fFudgeFactor * _clrShadow.getBlue() && b <= _clrShadow.getBlue() + _fFudgeFactor * _clrShadow.getBlue()) )
    {
      r = editor.util.EditorUtilities.CONTROL_SHADOW.getRed();
      g = editor.util.EditorUtilities.CONTROL_SHADOW.getGreen();
      b = editor.util.EditorUtilities.CONTROL_SHADOW.getBlue();
    }

    return (rgb & 0xff000000) + (r << 16) + (g << 8) + b;
  }
}