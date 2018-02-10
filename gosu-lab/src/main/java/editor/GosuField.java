package editor;

import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IScriptabilityModifier;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

/**
 */
public class GosuField extends GosuEditor implements ClipboardOwner
{
  private static volatile AtomicUndoManager _dummyUndoMgr;

  public GosuField( ISymbolTable symTable, AtomicUndoManager undoMgr )
  {
    this( symTable, normalizeUndoManager( undoMgr ), true, true );
  }

  public GosuField( ISymbolTable symTable, AtomicUndoManager undoMgr, boolean bEmptyTextOk, boolean bAccessAll )
  {
    this( symTable, normalizeUndoManager( undoMgr ), ScriptabilityModifiers.SCRIPTABLE,
          new DefaultContextMenuHandler(), false, bEmptyTextOk, bAccessAll );
  }

  public GosuField( ISymbolTable symTable,
                    AtomicUndoManager undoMgr,
                    IScriptabilityModifier scriptabilityConstraint,
                    IContextMenuHandler<IScriptEditor> contextMenuHandler,
                    boolean bStatement, boolean bEmptyTextOk, boolean bAccessAll )
  {
    super( symTable, null, normalizeUndoManager( undoMgr ), scriptabilityConstraint, contextMenuHandler, null, bStatement, bEmptyTextOk );
    ((AbstractDocument)getEditor().getDocument()).setDocumentFilter( new GSFieldDocumentFilter() );
    setAcceptUses( false );
    setAccessAll( bAccessAll );
  }

  @Override
  public void read( IScriptPartId partId, String strSource ) throws IOException
  {
    super.read( partId, strSource );
    ((AbstractDocument)getEditor().getDocument()).setDocumentFilter( new GSFieldDocumentFilter() );
  }

  @Override
  protected void configureLayout( ILineInfoManager lineInfoRenderer )
  {
    super.configureLayout( lineInfoRenderer );

    JViewport viewport = new JViewport()
    {
      @Override
      public Dimension getPreferredSize()
      {
        Dimension dim = getEditor().getPreferredSize();
        return new Dimension( 1, dim.height );
      }

      @Override
      public Dimension getMinimumSize()
      {
        return getPreferredSize();
      }
    };
    viewport.setView( getEditor() );
    add( viewport, BorderLayout.CENTER );
  }

  @Override
  protected void handleParseException( final ParseResultsException e, boolean bForceCodeCompletion )
  {
    super.handleParseException( e, bForceCodeCompletion );
    if( e != null )
    {
      if( !e.getParseExceptions().isEmpty() )
      {
        getEditor().setBackground( GosuEditorKit.getStylePreferences().getBackground( GosuStyleContext.FIELD_ERROR ) );
      }
      else
      {
        getEditor().setBackground( GosuEditorKit.getStylePreferences().getBackground( GosuStyleContext.FIELD_WARNING ) );
      }
    }
  }

  @Override
  protected void clearParseException()
  {
    super.clearParseException();
    EventQueue.invokeLater( () -> getEditor().setBackground(
      ((GosuStyleContext)getEditor().getEditorKit().getViewFactory()).getBackground( GosuStyleContext.DEFAULT ) ) );
  }

  public void clipCut( Clipboard c )
  {
    try
    {
      getUndoManager().beginUndoAtom( "Cut" );

      clipCopy( c );
      delete();
    }
    finally
    {
      getUndoManager().endUndoAtom();
    }
  }

  public void clipCopy( Clipboard c )
  {
    try
    {
      Transferable contents = getClipCopyContents();
      if( contents == null )
      {
        return;
      }

      c.setContents( contents, this );

    }
    catch( Exception e )
    {
      EditorUtilities.handleUncaughtException( e );
    }
  }

  public void clipPaste( Clipboard c, boolean asGosu )
  {
    Transferable t = c.getContents( this );
    if( t == null )
    {
      return;
    }

    if( t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
    {
      try
      {
        String strContents = (String)t.getTransferData( DataFlavor.stringFlavor );
        getEditor().replaceSelection( strContents );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private Transferable getClipCopyContents()
  {
    Transferable contents = null;
    String strSelection = getExpandedSelection();
    if( strSelection != null && strSelection.length() > 0 )
    {
      contents = new StringSelection( strSelection );
    }
    return contents;
  }

  public void lostOwnership( Clipboard clipboard, Transferable contents )
  {
  }

  class GSFieldDocumentFilter extends DocumentFilter
  {
    @Override
    public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr ) throws BadLocationException
    {
      if( getHeight() < getFontMetrics( getFont() ).getHeight()*2 )
      {
        super.insertString( fb, offset, string.replace( '\n', ';' ), attr );
      }
      else
      {
        super.insertString( fb, offset, string, attr );
      }
    }

    @Override
    public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
    {
      if( getHeight() < getFontMetrics( getFont() ).getHeight()*2 )
      {
        super.replace( fb, offset, length, text.replace( '\n', ';' ), attrs );
      }
      else
      {
        super.replace( fb, offset, length, text, attrs );
      }
    }
  }

  private static AtomicUndoManager normalizeUndoManager( AtomicUndoManager candidate )
  {
    return candidate != null ? candidate : getDummyUndoManager();
  }

  private static AtomicUndoManager getDummyUndoManager()
  {
    if( _dummyUndoMgr == null )
    {
      AtomicUndoManager dummy = new AtomicUndoManager( 1 );
      dummy.setPaused( true );
      _dummyUndoMgr = dummy;
    }
    return _dummyUndoMgr;
  }

}