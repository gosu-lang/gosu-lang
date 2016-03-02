package editor.search;

import editor.IScriptEditor;
import editor.SimpleDocumentFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 */
public class SearchLocalDialog2 extends BaseLocalSearchDialog
{

  private IScriptEditor _scriptSource;
  private Map<IScriptEditor, Integer> _mapReplaceAllOffset;

  public SearchLocalDialog2( IScriptEditor scriptSource, boolean bReplace )
  {
    this( scriptSource, scriptSource.getSelectedText(), bReplace );
  }

  public SearchLocalDialog2( IScriptEditor scriptSource, String searchText, boolean bReplace )
  {
    super( true, bReplace, searchText, scriptSource.getUndoManager() );
    _scriptSource = scriptSource;
  }

  @Override
  protected SearchLocation performFind( String strText, boolean backwards )
  {
    IScriptEditor scriptSource = getScriptSource();
    SearchLocation location;
    int iCaretPos;
    if( isReplaceMode() )
    {
      iCaretPos = scriptSource.getEditor().getSelectionStart();
    }
    else
    {
      iCaretPos = scriptSource.getEditor().getCaretPosition();
    }
    setAllModeOffset( scriptSource, iCaretPos );
    location = findInSection( scriptSource, strText, iCaretPos, backwards );
    return location;
  }

  SearchLocation findInSection( IScriptEditor section, String strText, int caretPosition, boolean backwards )
  {
    return findInSection( section, strText, caretPosition, false, null, backwards );
  }

  SearchLocation findInSection( IScriptEditor section, String strText, int iOffset, boolean bForcePrompt, Point messageLocation, boolean backwards )
  {
    JTextComponent editor = section.getEditor();
    int iDocLength = editor.getDocument().getLength();
    String strSource;
    try
    {
      strSource = editor.getDocument().getText( 0, iDocLength );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
    List found = findInSource( strSource, strText, iOffset, backwards );
    if( found != null && found.size() > 0 )
    {
      SearchLocation location = (SearchLocation)found.get( 0 );
      if( location != null )
      {
        if( hasAllModeRecycledFromTop() )
        {
          // Don't replace text if it's location is after the location from
          // which ReplaceAll was initiated, otherwise perpetual replacing will ensue.
          int iAllModeOffset = getAllModeOffset( section );
          if( iAllModeOffset >= 0 && location._iOffset >= iAllModeOffset )
          {
            return null;
          }
        }

        editor.requestFocus();
        editor.select( location._iOffset, location._iOffset + location._iLength );
        if( isReplaceMode() )
        {
          String strReplace = location.getReplacementText( getDialogReplaceText() );
          strReplace = strReplace == null ? "" : strReplace;
          int iRet = isAllMode()
                     ? 0
                     : MessageDisplay.displayConfirmation( "Replace this_occurrence ",
                                                           MessageBox.CUSTOM, new String[]{"Replace", "Skip", "All", MessageBox.CANCEL}, messageLocation );
          if( iRet == 0 || iRet == 2 )
          {
            DocumentFilter documentFilter = ((AbstractDocument)editor.getDocument()).getDocumentFilter();
            if( documentFilter instanceof SimpleDocumentFilter && !((SimpleDocumentFilter)documentFilter).acceptEdit( "" ) )
            {
              return location;
            }
          }
          switch( iRet )
          {
            case 0: // Replace
              startUndoIfNecessary();
              if( editor.isEditable() )
              {
                editor.replaceSelection( strReplace );
                int iAllModeOffset = getAllModeOffset( section );
                if( location._iOffset < iAllModeOffset )
                {
                  setAllModeOffset( section, iAllModeOffset - strText.length() + strReplace.length() );
                }
              }
              // Fall thru...
            case 1: // Skip
              setStartedReplacing( true );
              return findInSection( section, strText, section.getEditor().getCaretPosition(), bForcePrompt, MessageDisplay.getLastMessageLocation(), backwards );
            case 2: // All
              setStartedReplacing( true );
              startUndoIfNecessary();
              setAllMode( true );
              if( editor.isEditable() )
              {
                editor.replaceSelection( strReplace );
                int iAllModeOffset = getAllModeOffset( section );
                if( location._iOffset < iAllModeOffset )
                {
                  setAllModeOffset( section, iAllModeOffset - strText.length() + strReplace.length() );
                }
              }
              return findInSection( section, strText, location._iOffset + strReplace.length(), bForcePrompt, MessageDisplay.getLastMessageLocation(), backwards );
            case 3: // Cancel
              break;
          }
        }
        return location;
      }
    }

    if( isReplaceMode() && hasAllModeOffset() )
    {
      // Start replacing from from top in AllMode.
      if( !hasAllModeRecycledFromTop() )
      {
        setAllModeRecyclingFromTop( true );
        return findInSection( _scriptSource, strText, 0, false, messageLocation, backwards );
      }
      else
      {
        return null;
      }
    }

    if( bForcePrompt || iOffset > 0 )
    {
      int iRet = MessageDisplay.displayConfirmation( "Search from the top of the file?", JOptionPane.YES_NO_OPTION );
      if( iRet == JOptionPane.YES_OPTION )
      {
        return findInSection( _scriptSource, strText, backwards ? strSource.length() : 0, false, MessageDisplay.getLastMessageLocation(), backwards );
      }
      else
      {
        setDisplayNotFound( false );
      }
    }
    return null;
  }

  private IScriptEditor getScriptSource()
  {
    return _scriptSource;
  }

  private int getAllModeOffset( IScriptEditor section )
  {
    if( _mapReplaceAllOffset == null )
    {
      return -1;
    }

    Integer offset = _mapReplaceAllOffset.get( section );
    if( offset == null )
    {
      return -1;
    }
    return offset;
  }

  private void setAllModeOffset( IScriptEditor section, int iOffset )
  {
    if( _mapReplaceAllOffset == null )
    {
      _mapReplaceAllOffset = new HashMap<IScriptEditor, Integer>();
    }
    _mapReplaceAllOffset.put( section, iOffset );
  }

  private boolean hasAllModeOffset()
  {
    return _mapReplaceAllOffset != null && !_mapReplaceAllOffset.isEmpty();
  }

}
