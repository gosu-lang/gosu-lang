package editor;

import gw.lang.parser.exceptions.ParseException;
import gw.lang.reflect.IMetaType;

/**
 */
public class MetaInfoPopup extends BeanInfoPopup
{
  public MetaInfoPopup( IMetaType type, String strMemberPath, boolean bConstrainByLastPathElement, GosuEditor editor )
    throws ParseException
  {
    super( type, strMemberPath, bConstrainByLastPathElement, editor, null );
  }

  public MetaInfoPopup( PackageType type, String strMemberPath, boolean bConstrainByLastPathElement, GosuEditor editor )
    throws ParseException
  {
    super( type, strMemberPath, bConstrainByLastPathElement, editor, null );
  }

  public Boolean isForStaticAccess()
  {
    return (_classes != null && _classes.length == 1 &&
            (_classes[0] instanceof PackageType || _classes[0] instanceof IMetaType)) ||
           _strMemberPath == null ||
           _strMemberPath.length() == 0;
  }
}
