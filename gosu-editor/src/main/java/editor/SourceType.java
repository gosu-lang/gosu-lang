package editor;


/**
 */
public final class SourceType
{
  public static final SourceType PROGRAM;
  public static final SourceType CLASS;
  public static final SourceType LIBRARY;
  public static final SourceType STATEMENT;
  public static final SourceType EXPRESSION;
  public static final SourceType TEMPLATE;
  public static final SourceType ANY;

  private static final SourceType[] SCRIPT_TYPES =
    new SourceType[]
      {
        PROGRAM = new SourceType(),
        CLASS = new SourceType(),
        LIBRARY = new SourceType(),
        STATEMENT = new SourceType(),
        EXPRESSION = new SourceType(),
        TEMPLATE = new SourceType(),
        ANY = new SourceType(),
      };

  private static int g_iIdSource;
  private int _iId;

  private SourceType()
  {
    _iId = g_iIdSource++;
  }

  public int getId()
  {
    return _iId;
  }

  Object readResolve()
  {
    return SCRIPT_TYPES[getId()];
  }

  public static SourceType[] getSourceTypes()
  {
    SourceType[] copy = new SourceType[SCRIPT_TYPES.length];
    System.arraycopy( SCRIPT_TYPES, 0, copy, 0, copy.length );
    return copy;
  }
}