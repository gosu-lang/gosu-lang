package editor;

import editor.debugger.BreakpointManager;
import editor.debugger.Debugger;
import editor.run.IRunConfig;
import editor.util.EditorUtilities;
import editor.util.LabToolbarButton;
import editor.util.SettleModalEventQueue;
import editor.util.ToolBar;
import gw.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.PrintStream;

/**
 */
public class SystemPanel extends ClearablePanel
{
  private JTextPane _outputPanel;
  private EditorScrollPane _scroller;
  private PrintStream _out;
  private PrintStream _err;
  private PrintStream _sysOut;
  private PrintStream _sysErr;

  public SystemPanel()
  {
    super();
    BatchDocument document = new BatchDocument();
    document.addDocumentListener( new DocListener() );
    _outputPanel = new JTextPane( document );
    _out = new TextComponentWriter( Color.WHITE, document, System.out );
    _err = new TextComponentWriter( Color.PINK, document, System.err );
    configureUI();
  }

  public void configureUI()
  {
    setLayout( new BorderLayout() );

    _outputPanel.setFont( new Font( "monospaced", Font.PLAIN, 14 ) );
    _outputPanel.setBorder( new EmptyBorder( 3, 3, 0, 0 ) );
    _outputPanel.setMargin( new Insets( 10, 10, 10, 10 ) );
    _outputPanel.setForeground( new Color( 92, 225, 92 ) );
    _outputPanel.setBackground( new Color( 20, 20, 20 ) );
    SimpleAttributeSet sas = new SimpleAttributeSet();
    StyleConstants.setLineSpacing( sas, -.2f );
    _outputPanel.setParagraphAttributes( sas, false );
    _outputPanel.setEditable( false );
    _outputPanel.setCaretColor( Color.white );

    ScrollableEditorRootPane editorRootScroller = new ScrollableEditorRootPane( _outputPanel );
    editorRootScroller.setContentPane( _outputPanel );
    editorRootScroller.setBorder( null );

    _scroller = new EditorScrollPane( null, _outputPanel, editorRootScroller );
    _scroller.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, Scheme.active().getScrollbarBorderColor() ) );
    JViewport vp = _scroller.getViewport();
    vp.setScrollMode( JViewport.BLIT_SCROLL_MODE );

    add( _scroller, BorderLayout.CENTER );

    JLabel label = new JLabel( "" );
    label.setFont( label.getFont().deriveFont( Font.BOLD ) );
    label.setBorder( new EmptyBorder( 0, 4 + GosuEditor.MIN_LINENUMBER_WIDTH, 0, 0 ) );
    add( label, BorderLayout.NORTH );

    add( makeRunToolbar(), BorderLayout.WEST );

    MouseHandler ml = new MouseHandler();
    _outputPanel.addMouseMotionListener( ml );
    _outputPanel.addMouseListener( ml );
    _outputPanel.addMouseWheelListener( ml );
  }

  private JComponent makeRunToolbar()
  {
    JPanel toolbarPanel = new JPanel( new BorderLayout() );
    toolbarPanel.setBackground( Scheme.active().getMenu() );
    toolbarPanel.setBorder( BorderFactory.createEmptyBorder( 1, 2, 1, 2 ) );

    ToolBar toolbar = new ToolBar( JToolBar.VERTICAL );

    LabToolbarButton item;
    item = new LabToolbarButton( new CommonMenus.ClearAndRunActionHandler( () -> getGosuPanel().getRunConfig() ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getGosuPanel().getRunConfig();
      return rc == null ? "Run..." : "Run '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    item = new LabToolbarButton( new CommonMenus.ClearAndDebugActionHandler( () -> getGosuPanel().getRunConfig() ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getGosuPanel().getRunConfig();
      return rc == null ? "Debug..." : "Debug '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    item = new LabToolbarButton( new CommonMenus.StopActionHandler( this::getGosuPanel ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.PauseActionHandler( this::getDebugger ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.ResumeActionHandler( this::getDebugger ) );
    toolbar.add( item );

    toolbar.addSeparator();

    item = new LabToolbarButton( new CommonMenus.ViewBreakpointsActionHandler( () -> null ) );
    toolbar.add( item );
    ToggleToolBarButton titem = new ToggleToolBarButton( new CommonMenus.MuteBreakpointsActionHandler( this::getBreakpointManager ) );
    toolbar.add( titem );

    toolbarPanel.add( toolbar, BorderLayout.CENTER );
    return toolbarPanel;
  }

  public JTextPane getOutputPanel()
  {
    return _outputPanel;
  }

  public void addNotify()
  {
    super.addNotify();
    _sysOut = System.out;
    _sysErr = System.err;
    System.setOut( _out );
    System.setErr( _err );
  }

  public void removeNotify()
  {
    super.removeNotify();
    System.setOut( _sysOut );
    System.setErr( _sysErr );
  }

  /**
   * @param strOut
   */
  public void println( String strOut )
  {
    _out.println( strOut );
  }

  /**
   * @param strOut
   */
  public void setText( String strOut )
  {
    _outputPanel.setText( strOut );
  }

  @Override
  public void clear()
  {
    _outputPanel.setText( "" );
    scrollRectToVisible( new Rectangle( 0, 0, 0, 0 ) );
  }

  @Override
  public void dispose()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();

    gosuPanel.killProcess();
    gosuPanel.showConsole( false );
  }

  private class MouseHandler extends MouseAdapter
  {
    private SourceFileAttribute _link;

    @Override
    public void mousePressed( MouseEvent e )
    {
      _link = getLinkAtCursor( e );
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
      if( _link == getLinkAtCursor( e ) )
      {
        gotoLink( _link );
      }
      _link = null;
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {
      SourceFileAttribute link = getLinkAtCursor( e );
      if( link != null )
      {
        _outputPanel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
      }
      else
      {
        _outputPanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
      }
    }

    private SourceFileAttribute getLinkAtCursor( MouseEvent e )
    {
      int caret = _outputPanel.viewToModel( e.getPoint() );
      Element elem = getElementAt( caret );
      if( elem != null )
      {
        return (SourceFileAttribute)((AbstractDocument.LeafElement)elem).getAttribute( HTML.Tag.A );
      }

      return null;
    }

    private void gotoLink( SourceFileAttribute link )
    {
      if( link != null )
      {
        IType type = TypeSystem.getByFullNameIfValid( link.getFqn() );
        if( type instanceof IGosuClass )
        {
          IFile sourceFile = ((IGosuClass)type).getSourceFileHandle().getFile();
          if( sourceFile != null && sourceFile.isJavaFile() )
          {
            getGosuPanel().openFile( sourceFile.toJavaFile(), true );
            SettleModalEventQueue.instance().run();
          }
          GosuEditorPane editor = getGosuPanel().getCurrentGosuEditor().getEditor();
          Element root = editor.getDocument().getDefaultRootElement();
          int startOfLineOffset = root.getElement( link.getLine() - 1 ).getStartOffset();
          editor.setCaretPosition( startOfLineOffset );
          editor.requestFocus();
        }
      }
    }

    public void mouseWheelMoved( MouseWheelEvent e )
    {
      // For high-resolution pointing devices, the events sometimes come in with 0 rotation, which we
      // want to ignore as it indicates a small incremental scroll.
      if( e.getWheelRotation() == 0 )
      {
        return;
      }

      if( (e.getModifiers() & EditorUtilities.CONTROL_KEY_MASK) == 0 )
      {
        forward( e );
        return;
      }

      int iInc = e.getWheelRotation() < 0 ? -1 : 1;

      Font font = _outputPanel.getFont();
      int iSize = font.getSize() + iInc;
      if( iSize < 4 || iSize > 72 )
      {
        return;
      }
      _outputPanel.setFont( font.deriveFont( (float)iSize ) );
      _scroller.getAdviceColumn().revalidate();
      _scroller.getAdviceColumn().repaint();
    }

    /**
     * For some reason the parent does not get mouse wheel
     */
    private void forward( MouseWheelEvent e )
    {
      e = new MouseWheelEvent( e.getComponent().getParent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation() );
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( e );
    }

    protected Element getElementAt( int offset )
    {
      return getElementAt( _outputPanel.getDocument().getDefaultRootElement(), offset );
    }
    private Element getElementAt( Element parent, int offset )
    {
      if( parent.isLeaf() )
      {
        return parent;
      }
      return getElementAt( parent.getElement( parent.getElementIndex( offset ) ), offset );
    }
  }

  private BreakpointManager getBreakpointManager()
  {
    return getGosuPanel() == null ? null : getGosuPanel().getBreakpointManager();
  }

  private Debugger getDebugger()
  {
    return getGosuPanel() == null ? null : getGosuPanel().getDebugger();
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  private class DocListener implements DocumentListener
  {
    @Override
    public void insertUpdate( DocumentEvent e )
    {
      maintainForegroundColor();
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {

    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
      maintainForegroundColor();
    }

    private void maintainForegroundColor()
    {
      // Ensure text typed into the console uses the foreground color.  If we don't set it here, the
      // editor sometimes reuses a color from wherever the user may have moved the cursor in the console
      // and pressed Enter.
      _outputPanel.getInputAttributes();
      SimpleAttributeSet sas = new SimpleAttributeSet();
      StyleConstants.setForeground( sas, _outputPanel.getForeground() );
      _outputPanel.setCharacterAttributes( sas, false );
    }
  }
}