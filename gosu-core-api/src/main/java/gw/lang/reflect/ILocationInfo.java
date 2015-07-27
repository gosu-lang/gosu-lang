package gw.lang.reflect;

import java.net.URL;

/**
 */
public interface ILocationInfo
{
  boolean hasLocation();

  /**
   * The offset of the feature in its top-level container
   */
  int getOffset();

  /**
   * The length of the feature's text in its top-level container
   */
  int getTextLength();

  /**
   * One-based line number of this feature in its top-level container
   */
  int getLine();

  int getColumn();

  URL getFileUrl();

  static ILocationInfo EMPTY =
    new ILocationInfo()
    {
      @Override
      public boolean hasLocation()
      {
        return false;
      }

      @Override
      public int getOffset()
      {
        return -1;
      }

      @Override
      public int getTextLength()
      {
        return -1;
      }

      @Override
      public int getLine()
      {
        return -1;
      }

      @Override
      public int getColumn()
      {
        return -1;
      }

      @Override
      public URL getFileUrl()
      {
        return null;
      }
    };
}
