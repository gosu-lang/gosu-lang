package editor;

import gw.lang.GosuShop;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 * Checks that the source is the name of a type which is assignable to a given type.
 *
 * @author pdalbora
 */
public class TypeNameValidator implements IGosuValidator
{

  private final IType _expectedType;
  private final ITypeUsesMap _typeUsesMap;

  public TypeNameValidator( IType expectedType )
  {
    this( expectedType, null );
  }

  public TypeNameValidator( IType expectedType, ITypeUsesMap typeUsesMap )
  {
    _expectedType = expectedType;
    _typeUsesMap = typeUsesMap;
  }

  @Override
  public void validate( IParsedElement rootParsedElement, String scriptSrc )
  {
    IType actualType = getType( scriptSrc );

    if( actualType == null )
    {
      rootParsedElement.addParseException(
        new ParseException(
          GosuShop.createStandardParserState( rootParsedElement, scriptSrc, true ),
          Res.MSG_INVALID_TYPE,
          scriptSrc ) );
    }
    else if( !_expectedType.isAssignableFrom( actualType ) )
    {
      rootParsedElement.addParseException(
        new ParseException(
          GosuShop.createStandardParserState( rootParsedElement, scriptSrc, true ),
          Res.MSG_TYPE_MISMATCH,
          _expectedType.getDisplayName(),
          actualType.getDisplayName() ) );
    }
  }

  private IType getType( String name )
  {
    IType type;
    try
    {
      type = TypeSystem.getByFullName( name );
    }
    catch( Exception e )
    {
      if( _typeUsesMap != null )
      {
        type = _typeUsesMap.resolveType( name );
      }
      else
      {
        type = null;
      }
    }
    return type;
  }
}
