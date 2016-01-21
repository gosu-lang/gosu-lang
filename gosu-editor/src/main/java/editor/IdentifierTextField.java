package editor;

import editor.util.TextComponentUtil;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 */
public class IdentifierTextField extends JTextField
{
  private boolean _bAcceptDot;
  private boolean _bAcceptUnderscore;


  public IdentifierTextField()
  {
    this( false, true );
  }

  public IdentifierTextField( boolean bAcceptDot )
  {
    this( bAcceptDot, true );
  }

  public IdentifierTextField( boolean bAcceptDot, boolean bAcceptUnderscore )
  {
    super();
    _bAcceptDot = bAcceptDot;
    _bAcceptUnderscore = bAcceptUnderscore;
  }

  @Override
  protected Document createDefaultModel()
  {
    return new IdentifierDocument();
  }

  private class IdentifierDocument extends PlainDocument
  {
    @Override
    public void insertString( int iOffset, String str, AttributeSet a ) throws BadLocationException
    {
      if( str == null || str.length() == 0 )
      {
        return;
      }

      String strText = IdentifierTextField.this.getText();
      StringBuffer strbText = new StringBuffer( strText == null ? "" : strText );

      if( iOffset <= strbText.length() )
      {
        strbText.insert( iOffset, str );
      }
      else
      {
        strbText.append( str );
      }

      if( !TextComponentUtil.isValidIdentifier( strbText, _bAcceptDot ) || strbText.toString().contains( "$" ) )
      {
        String validID = TextComponentUtil.makeValidIdentifier( strbText.toString(), _bAcceptDot, _bAcceptUnderscore );
        if( !_bAcceptUnderscore )
        {
          validID = validID.replace( "$", "" );
        }
        str = validID.substring( iOffset, iOffset + (validID.length() - strText.length()) );
      }

      super.insertString( iOffset, str, a );
    }
  }

}
