package editor.shipit;

import editor.FileTree;
import editor.util.IProgressCallback;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

/**
 */
public class Compiler
{
  public boolean compileTree( FileTree tree, ICompileConsumer consumer, IProgressCallback progress )
  {
    if( tree.isFile() )
    {
      IType type = tree.getType();
      progress.incrementProgress( type != null ? type.getName() : "" );
      if( type instanceof IGosuClass )
      {
        if( !compile( (IGosuClass)type, consumer ) )
        {
          return false;
        }
      }
    }
    else if( tree.isDirectory() )
    {
      for( FileTree file: tree.getChildren() )
      {
        if( !compileTree( file, consumer, progress ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  public boolean compile( IGosuClass gsClass, ICompileConsumer consumer )
  {
    // Parse
    parseImpl( gsClass );
    //noinspection ThrowableResultOfMethodCallIgnored
    ParseResultsException parseException = gsClass.getParseResultsException();
    if( parseException != null && !parseException.hasOnlyParseWarnings() )
    {
      return consumer.accept( new CompiledClass( gsClass, null, parseException ) );
    }

    // Compile
    try
    {
      return compileClass( gsClass, consumer );
    }
    catch( Exception e )
    {
      return consumer.accept( new CompiledClass( gsClass, null, e ) );
    }
  }

  private boolean compileClass( IGosuClass gsClass, ICompileConsumer consumer )
  {
    byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( gsClass );
    if( !consumer.accept( new CompiledClass( gsClass, bytes, gsClass.getParseResultsException() ) ) )
    {
      return false;
    }
    for( IGosuClass innerClass : gsClass.getInnerClasses() )
    {
      if( !compileClass( innerClass, consumer ) )
      {
        return false;
      }
    }
    return true;
  }

  protected void parseImpl( IGosuClass gsClass )
  {
    TypeSystem.lock();
    try
    {
      if( gsClass != null )
      {
        TypeSystem.refresh( (ITypeRef)gsClass );
        gsClass.setCreateEditorParser( false );
        gsClass.isValid(); // force compilation
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }
}

