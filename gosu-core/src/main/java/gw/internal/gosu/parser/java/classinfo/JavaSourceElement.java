package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.LocationInfo;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import java.net.MalformedURLException;

/**
 */
public abstract class JavaSourceElement implements IJavaAnnotatedElement
{
  private LocationInfo _location;

  protected abstract Tree getTree();

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
      _location = new LocationInfo( startPos, endPos - startPos, -1, -1, getEnclosingClass().getSourceFileHandle().getFile().toURI().toURL() );
      return _location;
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }
  }

}
