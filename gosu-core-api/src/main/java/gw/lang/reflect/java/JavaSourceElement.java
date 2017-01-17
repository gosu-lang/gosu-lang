package gw.lang.reflect.java;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.LocationInfo;
import java.net.MalformedURLException;
import java.util.StringTokenizer;

/**
 */
public abstract class JavaSourceElement
{
  private LocationInfo _location;

  public abstract Tree getTree();
  public abstract IJavaClassInfo getEnclosingClass();
  public IJavaClassInfo getDeclaringClass()
  {
    return getEnclosingClass();
  }

  public ILocationInfo getLocationInfo()
  {
    if( _location != null )
    {
      return _location;
    }

    int startPos = TreeInfo.getStartPos( (JCTree)getTree() );
    int endPos = TreeInfo.getEndPos( (JCTree)getTree(), null );
    try
    {
      _location = new LocationInfo( startPos, endPos - startPos, -1, -1, getDeclaringClass().getSourceFileHandle().getFile().toURI().toURL() );
      return _location;
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }
  }

  protected IJavaClassInfo findInnerSourceType( IJavaClassInfo topLevelType, String fqnInner )
  {
    String innerSuffix = fqnInner.substring( topLevelType.getName().length() + 1 );
    StringTokenizer tokenizer = new StringTokenizer( innerSuffix, "." );
    IJavaClassInfo innerType = topLevelType;
    outer:
    while( tokenizer.hasMoreTokens() )
    {
      String innerName = tokenizer.nextToken();
      for( IJavaClassInfo innerClass: innerType.getDeclaredClasses() )
      {
        if( innerClass.getSimpleName().equals( innerName ) )
        {
          innerType = innerClass;
          continue outer;
        }
      }
      throw new IllegalStateException( "Should have found inner type" );
    }
    return innerType;
  }
}
