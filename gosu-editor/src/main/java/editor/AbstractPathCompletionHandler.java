package editor;


import editor.util.TextComponentUtil;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;

/**
 */
public abstract class AbstractPathCompletionHandler implements IPathCompletionHandler
{
  private GosuEditor _gsEditor;


  @Override
  public GosuEditor getGosuEditor()
  {
    return _gsEditor;
  }

  @Override
  public void setGosuEditor( GosuEditor gsEditor )
  {
    _gsEditor = gsEditor;
  }

  protected void displayPathCompletionBeanInfoPopup( BeanInfoPopup popup )
  {
    getGosuEditor().setBeanInfoPopup( popup );
    getGosuEditor().displayPathCompletionBeanInfoPopup( false );
  }

  protected void displayFeaturePathCompletionBeanInfoPopup( BeanInfoPopup popup )
  {
    getGosuEditor().setBeanInfoPopup( popup );
    getGosuEditor().displayPathCompletionBeanInfoPopup( true );
  }

  protected String getPathAtCaret()
  {
    return TextComponentUtil.getWordAtCaret( _gsEditor.getEditor() );
  }

  protected String getWordAtCaret()
  {
    return getLastWordInPath( getPathAtCaret() );
  }

  protected String getSingleNameAtCaret()
  {
    String strMemberPath = TextComponentUtil.getWordBeforeCaret( _gsEditor.getEditor() );
    if( strMemberPath != null && strMemberPath.length() > 0 )
    {
      int iIndex = strMemberPath.lastIndexOf( '.' );
      if( iIndex >= 0 )
      {
        strMemberPath = strMemberPath.substring( iIndex + 1 );
      }
      char cFirst = strMemberPath.length() == 0 ? 0 : strMemberPath.charAt( 0 );
      if( !Character.isJavaIdentifierPart( cFirst ) && cFirst != '.' )
      {
        strMemberPath = " ";
      }
    }
    //If there is space at the end of the word, return a null name
    if( strMemberPath.endsWith( " " ) )
    {
      return "";
    }
    return strMemberPath;
  }

  protected String getWordBeforeCaret()
  {
    return getLastWordInPath( getSingleNameAtCaret() );
  }

  protected String getMemberPathFromPath( String strPath )
  {
    if( strPath == null )
    {
      return null;
    }

    int iDotIndex = strPath.indexOf( '.' );
    if( iDotIndex < 0 )
    {
      return null;
    }

    return strPath.substring( iDotIndex + 1 );
  }

  private String getLastWordInPath( String strMemberPath )
  {
    if( strMemberPath != null )
    {
      int iIndex = strMemberPath.lastIndexOf( '.' );
      if( iIndex >= 0 )
      {
        return iIndex + 1 >= strMemberPath.length() ? "" : strMemberPath.substring( iIndex + 1 );
      }
    }
    return strMemberPath;
  }

  protected boolean isWordAtCaret( ISymbolTable transientSymTable )
  {
    String strMemberPath = getSingleNameAtCaret();
    String strBeanName = getRootBeanFromPath( strMemberPath );

    return
      strBeanName == null ||
      strBeanName.length() == 0 ||
      !Character.isJavaIdentifierPart( strBeanName.charAt( 0 ) );
  }

  protected ISymbol getSymbolAtCaret( ISymbolTable transientSymTable )
  {
    String strMemberPath = getSingleNameAtCaret();
    String strBeanName = getRootBeanFromPath( strMemberPath );

    boolean bHasWordAtCaret = isWordAtCaret( transientSymTable );
    ISymbol sym = null;
    if( !bHasWordAtCaret )
    {
      sym = getGosuEditor().getSymbolTable().getSymbol( strBeanName );
      if( sym == null )
      {
        if( transientSymTable != null )
        {
          sym = transientSymTable.getSymbol( strBeanName );
        }
      }
    }
    return sym;
  }

  protected String getRootBeanFromPath( String strPath )
  {
    int iDotIndex = strPath.indexOf( '.' );
    if( iDotIndex <= 0 )
    {
      return strPath;
    }

    return strPath.substring( 0, iDotIndex );
  }
}
