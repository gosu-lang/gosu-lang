package editor.debugger;

import com.sun.jdi.InvocationException;
import com.sun.jdi.Location;
import com.sun.jdi.Value;
import editor.DefaultContextMenuHandler;
import editor.GosuClassLineInfoManager;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.IHandleCancel;
import editor.LabFrame;
import editor.Scheme;
import editor.VarTree;
import editor.search.StringUtil;
import editor.splitpane.CollapsibleSplitPane;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import gw.lang.parser.IParseTree;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.ContextSymbolTableUtil;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 */
public class EvaluateDialog extends JDialog implements IHandleCancel
{
  private final String _expr;
  private GosuEditor _fieldExpr;
  private JTree _varTree;

  private int _offset;
  private String _immediateClass;
  private String _fqn;

  public EvaluateDialog( String expr )
  {
    super( LabFrame.instance(), "Evaluate Script" );
    _expr = expr;
    configUi();
    setDefaultCloseOperation( DISPOSE_ON_CLOSE );
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
    contentPane.setBackground( Scheme.active().getMenu() );
    contentPane.setLayout( new BorderLayout() );

    CollapsibleSplitPane splitPane = new CollapsibleSplitPane( SwingConstants.VERTICAL, makeExpressionPanel(), makeResultPane() );
    add( splitPane, BorderLayout.CENTER );

    contentPane.add( splitPane, BorderLayout.CENTER );
    splitPane.setPosition( 6 );

    mapCancelKeystroke( "Close", this::close );

    setSize( 500, 500 );
    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  private JScrollPane makeResultPane()
  {
    _varTree = new JTree( makeEmptyModel() );
    _varTree.setBorder( null );
    _varTree.setBackground( Scheme.active().getWindow() );
    _varTree.setRootVisible( true );
    _varTree.setShowsRootHandles( true );
    _varTree.setRowHeight( 22 );
    _varTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _varTree.setVisibleRowCount( 20 );
    _varTree.setCellRenderer( new VarTreeCellRenderer( _varTree ) );
    return new JScrollPane( _varTree );
  }

  private JComponent makeExpressionPanel()
  {
    Location loc = getSuspendedLocation();
    _fieldExpr = createFieldExpr( loc );
    addKeyHandler( _fieldExpr );
    _fieldExpr.setAccessAll( true );
    try
    {
      _fieldExpr.read( new TypelessScriptPartId( "debugger condition" ), _expr );
      if( !_expr.isEmpty() )
      {
        _fieldExpr.getEditor().selectAll();
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    JPanel panel = new JPanel( new BorderLayout() );
    panel.setPreferredSize( new Dimension( 30, 30 ) );
    panel.add( _fieldExpr, BorderLayout.CENTER );
    return panel;
  }

  private GosuEditor createFieldExpr( Location loc )
  {
    if( loc == null )
    {
      return new GosuEditor( new StandardSymbolTable( true ),
                             new GosuClassLineInfoManager(), new AtomicUndoManager( 10000 ), ScriptabilityModifiers.SCRIPTABLE, new DefaultContextMenuHandler(), false, true );
    }
    else
    {
      String outermostTypeName = Debugger.getOutermostType( loc.declaringType() );
      IGosuClass outermostType = (IGosuClass)TypeSystem.getByFullNameIfValidNoJava( outermostTypeName );
      return new GosuEditor( ContextSymbolTableUtil.getSymbolTableAtOffset( outermostType, StringUtil.getLineOffset( outermostType.getSource(), loc.lineNumber() ) ),
                             new GosuClassLineInfoManager(), new AtomicUndoManager( 10000 ), ScriptabilityModifiers.SCRIPTABLE, new DefaultContextMenuHandler(), false, true );
    }
  }

  private Location getSuspendedLocation()
  {
    Debugger debugger = getGosuPanel().getDebugger();
    return debugger.isSuspended() ? debugger.getSuspendedLocation() : null;
  }

  private void updateResult()
  {
    String script = _fieldExpr.getText();
    DefaultTreeModel model;
    if( locate() && script != null && script.length() > 0 )
    {
      try
      {
        Value value = new DebuggerExpression( script, _fqn, _immediateClass, _offset ).evaluate( getGosuPanel().getDebugger() );
        model = new DefaultTreeModel( new VarTree( "Result", value == null ? "null" : value.type().name(), value ) );
      }
      catch( InvocationException e )
      {
        model = new DefaultTreeModel( new VarTree( "Result", e.exception().referenceType().name(), e.exception() ) );
      }
    }
    else
    {
      model = makeEmptyModel();
    }
    _varTree.setModel( model );
  }

  private DefaultTreeModel makeEmptyModel()
  {
    return new DefaultTreeModel( new VarTree( "Result", "void", null ) );
  }

  private boolean locate()
  {
    Location suspendedLoc = getSuspendedLocation();
    if( suspendedLoc == null )
    {
      return false;
    }

    _fqn = Debugger.getOutermostType( suspendedLoc.declaringType() );

    IGosuClass topLevelClass = (IGosuClass)TypeSystem.getByFullName( _fqn );
    _offset = StringUtil.getLineOffset( topLevelClass.getSource(), suspendedLoc.lineNumber() );
    topLevelClass.isValid();
    IParseTree loc = topLevelClass.getClassStatement().getLocation().getDeepestLocation( _offset, false );
    if( loc == null )
    {
      return false;
    }
    int i = 0;
    while( loc != null && loc.getOffset() < _offset )
    {
      loc = topLevelClass.getClassStatement().getLocation().getDeepestLocation( _offset + ++i, true );
    }
    if( loc == null )
    {
      return false;
    }
    _offset = loc.getOffset();
    _immediateClass = loc.getParsedElement().getGosuClass().getName();
    return true;
  }

  private void addKeyHandler( GosuEditor gosuEditor )
  {
    gosuEditor.getEditor().addKeyListener( new KeyAdapter() {
      @Override
      public void keyPressed( KeyEvent e )
      {
        if( e.getKeyCode() == KeyEvent.VK_ESCAPE && !gosuEditor.isIntellisensePopupShowing() )
        {
          close();
        }
        else if( e.getKeyCode() == KeyEvent.VK_ENTER )
        {
          if( (isOneLine() || e.isControlDown()) && !gosuEditor.isIntellisensePopupShowing() )
          {
            updateResult();
            e.consume();
          }
        }
      }

      private boolean isOneLine()
      {
        return _fieldExpr.getHeight() < _fieldExpr.getFontMetrics( _fieldExpr.getFont() ).getHeight()*2;
      }
    } );
  }

  protected void close()
  {
    dispose();
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }
}