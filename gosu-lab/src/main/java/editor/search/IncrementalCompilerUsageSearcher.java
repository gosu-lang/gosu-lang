package editor.search;

import editor.FileTree;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class IncrementalCompilerUsageSearcher extends UsageSearcher
{
  private Set<IType> _types;

  public IncrementalCompilerUsageSearcher( IType type )
  {
    super( new UsageTarget( null, type.getTypeInfo() ), false, false );
    _types = new HashSet<>();
    _types.add( type );
  }

  public Set<IType> getTypes()
  {
    return _types;
  }

  @Override
  public boolean search( FileTree tree, SearchTree results )
  {
    if( isExcluded( tree ) )
    {
      return false;
    }

    IType type = tree.getType();

    List<SearchLocation> locations = findUsage( (IGosuClass)type );
    if( locations.isEmpty() )
    {
      return false;
    }
    _types.add( type );
    return true;
  }
}
