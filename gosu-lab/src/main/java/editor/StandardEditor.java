package editor;

import editor.plugin.typeloader.ITypeFactory;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import gw.fs.IFile;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IType;
import gw.lang.IIssueContainer;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuStringUtil;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

public class StandardEditor extends EditorHost
{
  private EditorScrollPane _scroller;
  private DocumentHandler _docHandler;
  private EditorHostTextPane _editor;
  private ParserFeedbackPanel _panelFeedback;
  private ITypeFactory _factory;
  private IType _type;

  public StandardEditor( ILineInfoManager lineInfoRenderer, IType type )
  {
    super( new AtomicUndoManager( 1000 ) );
    _docHandler = new DocumentHandler();
    _type = type;
    if( lineInfoRenderer != null )
    {
      lineInfoRenderer.setEditor( this );
    }
    configUi( lineInfoRenderer );
  }

  private void configUi( ILineInfoManager lineInfoRenderer )
  {
    setLayout( new BorderLayout() );

    _editor = new EditorHostTextPane( this );
    ToolTipManager.sharedInstance().registerComponent( _editor );
    ToolTipManager.sharedInstance().setDismissDelay( 60000 );
    _editor.setFont( new Font( EditorUtilities.getFontFamilyOrDefault( "Consolas", "Monospaced" ), Font.PLAIN, 12 ) );
    setBorder( UIManager.getBorder( "TextField.border" ) );
    _editor.setMargin( new Insets( 3, 3, 3, 3 ) );
    _editor.setForeground( Scheme.active().getCodeWindowText() );
    _editor.setBackground( Scheme.active().getCodeWindow() );
    _editor.putClientProperty( "caretWidth", 2 );
    SimpleAttributeSet sas = new SimpleAttributeSet();
    StyleConstants.setLineSpacing( sas, -.2f );
    //_editor.setParagraphAttributes( sas, false );
    _editor.setCaretColor( Scheme.active().getCodeWindowText() );

    configureEditorKit( _type );

    ScrollableEditorRootPane editorRootScroller = new ScrollableEditorRootPane( _editor );
    editorRootScroller.setContentPane( _editor );
    editorRootScroller.setBorder( null );

    _scroller = new EditorScrollPane( lineInfoRenderer, _editor, editorRootScroller );
    _scroller.setBorder( null );
    JViewport vp = _scroller.getViewport();
    vp.setScrollMode( JViewport.BLIT_SCROLL_MODE );

    add( _scroller, BorderLayout.CENTER );

    JLabel label = new JLabel( "" );
    label.setFont( label.getFont().deriveFont( Font.BOLD ) );
    label.setBorder( new EmptyBorder( 0, 4 + GosuEditor.MIN_LINENUMBER_WIDTH, 0, 0 ) );
    add( label, BorderLayout.NORTH );

    _panelFeedback = new ParserFeedbackPanel();
    add( BorderLayout.EAST, _panelFeedback );

    _editor.addKeyListener( new EditorKeyHandler() );
    _editor.addMouseWheelListener( new ScriptEditorMouseWheelHandler( this ) );
    MouseInEditorHandler mouseInEditorHandler = new MouseInEditorHandler( this );
    _editor.addMouseListener( mouseInEditorHandler );
    _editor.addMouseMotionListener( mouseInEditorHandler );
    _editor.addMouseListener( new MouseAdapter()
    {
      @Override
      public void mouseClicked( MouseEvent e )
      {
        setCompleteCode( false );
      }

      @Override
      public void mousePressed( MouseEvent e )
      {
        setCompleteCode( false );
      }

      @Override
      public void mouseReleased( MouseEvent e )
      {
        setCompleteCode( false );
      }
    } );

    addDocumentListener();

    addKeyHandlers();
  }

  @Override
  protected void handleDot( ISymbolTable transientSymTable )
  {

  }

  @Override
  public ISymbolTable getSymbolTableAtCursor()
  {
    return null;
  }

  @Override
  public void gotoDeclaration()
  {

  }

  @Override
  public boolean canAddBreakpoint( int line )
  {
    return _factory.canAddBreakpoint( _type, line );
  }

  private void configureEditorKit( IType type )
  {
    if( type == null )
    {
      return;
    }

    List<ITypeFactory> factories = type.getTypeLoader().getInterface( ITypeFactory.class );
    if( factories.isEmpty() )
    {
      return;
    }

    for( ITypeFactory factory: factories )
    {
      if( factory.handlesType( type ) )
      {
        _factory = factory;
        break;
      }
    }

    if( _factory == null )
    {
      return;
    }

    StyledEditorKit kit = _factory.makeEditorKit();
    if( kit == null )
    {
      return;
    }

    String mimeType = "text/" + _factory.getFileExtension().substring( 1 );
    _editor.setEditorKitForContentType( mimeType, kit );
    _editor.setContentType( mimeType );
  }

  @Override
  public void refresh( String content )
  {
    content = GosuStringUtil.replace( content, "\r\n", "\n" );
    getEditor().setText( content );
    EventQueue.invokeLater( () -> getUndoManager().discardAllEdits() );
  }

  @Override
  public EditorHostTextPane getEditor()
  {
    return _editor;
  }

  @Override
  public EditorScrollPane getScroller()
  {
    return _scroller;
  }

  @Override
  public String getLineCommentDelimiter()
  {
    return "//";
  }

  @Override
  public int getOffsetOfDeepestStatementLocationAtPos( int caretPosition, boolean strict )
  {
    Element root = getDocument().getDefaultRootElement();
    int iElem = root.getElementIndex( caretPosition );
    if( iElem < 0 )
    {
      return -1;
    }
    Element elem = root.getElement( iElem );
    try
    {
      int lineStart = elem.getStartOffset();
      int lineEnd = elem.getEndOffset();
      String text = getDocument().getText( lineStart, lineEnd - lineStart );
      String trimmed = text.trim();
      int whitespace = text.indexOf( trimmed );
      if( whitespace == text.length() )
      {
        // all whitespace
        return -1;
      }
      // location of first non-whitespace char in line
      return lineStart + whitespace;
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String getTypeAtLine( int line )
  {
    if( _factory != null )
    {
      return _factory.getTypeAtOffset( _type, getLineOffset( line ) );
    }
    return null;
  }

  @Override
  public String getTooltipMessage( MouseEvent event )
  {
    if( _factory == null )
    {
      return null;
    }
    int iPos = getEditor().viewToModel( event.getPoint() );
    return _factory.getTooltipMessage( iPos, this );
  }

  @Override
  public IIssueContainer getIssues()
  {
    return _factory.getIssueContainer( this );
  }

  @Override
  public DocumentListener getDocHandler()
  {
    return _docHandler;
  }

  @Override
  public JComponent getFeedbackPanel()
  {
    return _panelFeedback;
  }

  @Override
  public void parse( String strText, boolean forceCodeCompletion, boolean changed )
  {
    if( _factory != null )
    {
      _factory.parse( _type, strText, forceCodeCompletion, changed, this );

      EventQueue.invokeLater( () -> _panelFeedback.update( this ) );
    }
  }

  /**
   */
  class DocumentHandler implements DocumentListener
  {
    @Override
    public void changedUpdate( DocumentEvent e )
    {
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
      parse();
      refreshTypes();
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      parse();
      refreshTypes();
    }

    private void refreshTypes()
    {
      EventQueue.invokeLater( () -> {
        TypeSystem.refresh( (ITypeRef)_type );

        IFile[] sourceFiles = _type.getSourceFiles();
        if( sourceFiles != null )
        {
          for( IFile file : sourceFiles )
          {
            String[] typesForFile = TypeSystem.getTypesForFile( TypeSystem.getCurrentModule(), file );
            if( typesForFile != null )
            {
              for( String fqn: typesForFile )
              {
                IType csr = TypeSystem.getByFullNameIfValid( fqn );
                if( csr != null )
                {
                  TypeSystem.refresh( (ITypeRef)csr );
                }
              }
            }
          }
        }
      } );
    }
  }
}
