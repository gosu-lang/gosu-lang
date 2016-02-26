package gw.lang.reflect.json;

/**
 */
enum JsonSimpleType implements IJsonType
{
  String,
  Boolean,
  Character,
  Byte,
  Short,
  Integer,
  Long,
  Float,
  Double,
  BigDecimal,
  BigInteger;

  @Override
  public String getName()
  {
    return super.name();
  }

  @Override
  public IJsonParentType getParent()
  {
    return null;
  }

  public static JsonSimpleType get( Object jsonObj )
  {
    if( jsonObj == null )
    {
      return null;
    }

    return valueOf( JsonSimpleType.class, jsonObj.getClass().getSimpleName() );
  }
}
