/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.util.GosuExceptionUtil;
import gw.util.GosuStringUtil;
import gw.util.StreamUtil;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.StringTokenizer;

public class GosuVersion implements Comparable<GosuVersion> {
  public static final String RESOURCE_PATH = "gw/lang/gosu-version.properties";

  private final int _major;
  private final int _minor;
  private final int _incremental;
  private final int _buildNum;
  private final String _qualifier;

  public static GosuVersion parse( Reader in )
  {
    try
    {
      Properties props = new Properties();
      props.load(in);
      String majorProp = (String) props.get("gosu.version.major");
      int dotIndex = majorProp.indexOf('.');
      if (dotIndex >= 0) {
        int major = Integer.parseInt(majorProp.substring(0, dotIndex));
        int minor = Integer.parseInt(majorProp.substring(dotIndex + 1));
        int incremental = Integer.parseInt((String) props.get("gosu.version.minor"));
        String qualifier = (String) props.get("gosu.version.aux");
        return new GosuVersion( major, minor, incremental, qualifier );
      }
      else {
        int major = Integer.parseInt(majorProp);
        int minor = Integer.parseInt( (String) props.get( "gosu.version.minor" ) );
        int incremental = Integer.parseInt((String) props.get("gosu.version.incremental"));
        int build = Integer.parseInt((String) props.get("gosu.version.build"));
        String qualifier = (String) props.get( "gosu.version.qualifier" );
        if (build > 0 && !GosuStringUtil.isEmpty(qualifier)) {
          throw new IllegalArgumentException("build number non-zero and qualifier non-empty, can only have one or the other");
        }
        if (build > 0) {
          return new GosuVersion( major, minor, incremental, build );
        }
        else {
          return new GosuVersion( major, minor, incremental, qualifier );
        }
      }
    }
    catch ( IOException e )
    {
      throw GosuExceptionUtil.forceThrow(e);
    }
    finally
    {
      try
      {
        StreamUtil.close(in);
      }
      catch (IOException e)
      {
        System.err.println("Could not close version properties stream");
      }
    }
  }

  public static GosuVersion parse( String version )
  {
    // copied from org.apache.maven.artifact.versioning.DefaultArtifactVersion;

    int majorVersion = 0;
    int minorVersion = 0;
    int incrementalVersion = 0;
    int buildNumber = 0;
    String qualifier = null;

    int index = version.indexOf( "-" );

    String part1;
    String part2 = null;

    if ( index < 0 )
    {
      part1 = version;
    }
    else
    {
      part1 = version.substring( 0, index );
      part2 = version.substring( index + 1 );
    }

    if ( part2 != null )
    {
      try
      {
        if ( ( part2.length() == 1 ) || !part2.startsWith( "0" ) )
        {
          buildNumber = Integer.valueOf( part2 );
        }
        else
        {
          qualifier = part2;
        }
      }
      catch ( NumberFormatException e )
      {
        qualifier = part2;
      }
    }

    if ( ( part1.indexOf( "." ) < 0 ) && !part1.startsWith( "0" ) )
    {
      try
      {
        majorVersion = Integer.valueOf( part1 );
      }
      catch ( NumberFormatException e )
      {
        // qualifier is the whole version, including "-"
        qualifier = version;
        buildNumber = 0;
      }
    }
    else
    {
      boolean fallback = false;

      StringTokenizer tok = new StringTokenizer( part1, "." );
      try
      {
        majorVersion = getNextIntegerToken( tok );
        if ( tok.hasMoreTokens() )
        {
          minorVersion = getNextIntegerToken( tok );
        }
        if ( tok.hasMoreTokens() )
        {
          incrementalVersion = getNextIntegerToken( tok );
        }
        if ( tok.hasMoreTokens() )
        {
          fallback = true;
        }

        // string tokenzier won't detect these and ignores them
        if ( part1.indexOf( ".." ) >= 0 || part1.startsWith( "." ) || part1.endsWith( "." ) )
        {
          fallback = true;
        }
      }
      catch ( NumberFormatException e )
      {
        fallback = true;
      }

      if ( fallback )
      {
        // qualifier is the whole version, including "-"
        qualifier = version;
        majorVersion = 0;
        minorVersion = 0;
        incrementalVersion = 0;
        buildNumber = 0;
      }
    }
    return new GosuVersion(majorVersion, minorVersion, incrementalVersion, buildNumber, qualifier);
  }

  private static Integer getNextIntegerToken( StringTokenizer tok )
  {
    String s = tok.nextToken();
    if ( ( s.length() > 1 ) && s.startsWith( "0" ) )
    {
      throw new NumberFormatException( "Number part has a leading 0: '" + s + "'" );
    }
    return Integer.valueOf( s );
  }

  public GosuVersion(int major, int minor) {
    this(major, minor, 0, 0);
  }

  public GosuVersion(int major, int minor, int incremental) {
    this(major, minor, incremental, 0);
  }

  public GosuVersion(int major, int minor, int incremental, int buildNum)
  {
    this(major, minor, incremental, buildNum, null);
  }

  public GosuVersion(int major, int minor, int incremental, String qualifier)
  {
    this(major, minor, incremental, 0, GosuStringUtil.isEmpty(qualifier) ? null : qualifier);
  }

  private GosuVersion(int major, int minor, int incremental, int buildNum, String qualifier)
  {
    _major = major;
    _minor = minor;
    _incremental = incremental;
    _buildNum = buildNum;
    _qualifier = qualifier;
  }

  public int getMajor()
  {
    return _major;
  }

  public int getMinor()
  {
    return _minor;
  }

  public int getIncremental() {
    return _incremental;
  }

  public int getBuildNum() {
    return _buildNum;
  }

  public String getQualifier()
  {
    return _qualifier;
  }

  @Override
  public int compareTo(GosuVersion that)
  {
    int cmp = _major - that.getMajor();
    if (cmp == 0) {
      cmp = _minor - that.getMinor();
      if (cmp == 0) {
        cmp = _incremental - that.getIncremental();
        if (cmp == 0) {
          cmp = _buildNum - that.getBuildNum();
        }
        if (cmp == 0) {
          if (_qualifier != null && that.getQualifier() != null) {
            cmp = _qualifier.compareTo(that.getQualifier());
          }
          else if (_qualifier != null && that.getQualifier() == null) {
            return Integer.MAX_VALUE;
          }
          else if (_qualifier == null && that.getQualifier() != null) {
            return Integer.MIN_VALUE;
          }
        }
      }
    }
    return cmp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GosuVersion that = (GosuVersion) o;

    if (_major != that.getMajor() ||
            _minor != that.getMinor() ||
            _incremental != that.getIncremental() ||
            _buildNum != that.getBuildNum()) {
      return false;
    }
    if (_qualifier != null ? !_qualifier.equals(that._qualifier) : that._qualifier != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _major;
    result = 31 * result + _minor;
    result = 31 * result + _incremental;
    result = 31 * result + _buildNum;
    result = 31 * result + (_qualifier != null ? _qualifier.hashCode() : 0);
    return result;
  }

  public String toMajorMinorString() {
    return _major + "." + _minor;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( _major ).append('.').append( _minor );
    if (_incremental > 0) {
      sb.append( '.' ).append( _incremental );
    }
    if ( _buildNum > 0 ) {
      sb.append( '-' ).append(_buildNum);
    }
    else if ( _qualifier != null ) {
      sb.append( '-' ).append( _qualifier );
    }
    return sb.toString();
  }
}
