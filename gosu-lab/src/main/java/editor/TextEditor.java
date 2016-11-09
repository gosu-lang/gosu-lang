package editor;

import editor.plugin.typeloader.ITypeFactory;
import editor.undo.AtomicUndoManager;
import gw.lang.reflect.IType;
import gw.util.GosuStringUtil;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

/**
 */
public class TextEditor extends EditorHost
{
  private EditorScrollPane _scroller;
  private DocumentHandler _docHandler;
  private JTextPane _editor;
  private GosuEditorFeedbackPanel _panelFeedback;
  private ITypeFactory _factory;
  private IType _type;

  public TextEditor( IType type )
  {
    super( new AtomicUndoManager( 1000 ) );
    _docHandler = new DocumentHandler();
    _type = type;
    configUi();
  }

  private void configUi()
  {
    setLayout( new BorderLayout() );

    _editor = new JTextPane();
    _editor.setFont( new Font( "monospaced", Font.PLAIN, 12 ) );
    setBorder( UIManager.getBorder( "TextField.border" ) );
    _editor.setMargin( new Insets( 3, 3, 3, 3 ) );
    _editor.setForeground( Scheme.active().getCodeWindowText() );
    _editor.setBackground( Scheme.active().getCodeWindow() );
    SimpleAttributeSet sas = new SimpleAttributeSet();
    StyleConstants.setLineSpacing( sas, -.2f );
    _editor.setParagraphAttributes( sas, false );
    _editor.setCaretColor( Scheme.active().getCodeWindowText() );

    configureEditorKit( _type );

    ScrollableEditorRootPane editorRootScroller = new ScrollableEditorRootPane( _editor );
    editorRootScroller.setContentPane( _editor );
    editorRootScroller.setBorder( null );

    _scroller = new EditorScrollPane( null, _editor, editorRootScroller );
    _scroller.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, Scheme.active().getScrollbarBorderColor() ) );
    JViewport vp = _scroller.getViewport();
    vp.setScrollMode( JViewport.BLIT_SCROLL_MODE );

    add( _scroller, BorderLayout.CENTER );

    JLabel label = new JLabel( "" );
    label.setFont( label.getFont().deriveFont( Font.BOLD ) );
    label.setBorder( new EmptyBorder( 0, 4 + GosuEditor.MIN_LINENUMBER_WIDTH, 0, 0 ) );
    add( label, BorderLayout.NORTH );

    _panelFeedback = new GosuEditorFeedbackPanel();
    add( BorderLayout.EAST, _panelFeedback );

    addDocumentListener();

    addKeyHandlers();
  }

  private void configureEditorKit( IType type )
  {
    if( type == null )
    {
      return;
    }

    _factory = type.getTypeLoader().getInterface( ITypeFactory.class );
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
  public JTextComponent getEditor()
  {
    return _editor;
  }

  @Override
  public EditorScrollPane getScroller()
  {
    return _scroller;
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
  public void parse()
  {
    if( _factory != null )
    {
      _factory.parse( _type, this );
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
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      parse();
    }

  }

}
