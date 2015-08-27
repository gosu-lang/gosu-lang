package gw.internal.gosu.parser;

import gw.lang.parser.IParserState;
import gw.util.concurrent.LocklessLazyVar;

/**
*/
public class LazyLightweightParserState implements IParserState
{
  private LocklessLazyVar<IParserState> _lazyState;

  public LazyLightweightParserState( SourceCodeTokenizer tokenizer, int offsetShift, int lineShift )
  {
    int mark = tokenizer.mark();
    _lazyState = new LocklessLazyVar<IParserState>()
    {
      @Override
      protected LightweightParserState init()
      {
        int newMark = tokenizer.mark();
        try
        {
          tokenizer.restoreToMark( mark );
          return new LightweightParserState( tokenizer, offsetShift, lineShift );
        }
        finally
        {
          tokenizer.restoreToMark( newMark );
        }
      }
    };
  }

  @Override
  public int getLineNumber()
  {
    return _lazyState.get().getLineNumber();
  }

  @Override
  public int getTokenColumn()
  {
    return _lazyState.get().getTokenColumn();
  }

  @Override
  public String getSource()
  {
    return _lazyState.get().getSource();
  }

  @Override
  public int getTokenStart()
  {
    return _lazyState.get().getTokenStart();
  }

  @Override
  public int getTokenEnd()
  {
    return _lazyState.get().getTokenEnd();
  }

  @Override
  public int getLineOffset()
  {
    return _lazyState.get().getLineOffset();
  }

  @Override
  public IParserState cloneWithNewTokenStartAndTokenEnd( int newTokenStart, int newLength )
  {
    return _lazyState.get().cloneWithNewTokenStartAndTokenEnd( newTokenStart, newLength );
  }
}
