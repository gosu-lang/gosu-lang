package editor.debugger;

import editor.AbstractCloseDialog;
import editor.EditorScrollPane;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.RunMe;
import editor.Scheme;
import editor.util.EditorUtilities;
import editor.util.LabCheckbox;
import editor.util.SettleModalEventQueue;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class EditBreakpointsDialog extends AbstractCloseDialog
{
  private JList<Breakpoint> _listBreakpoints;
  private JButton _btnViewSource;
  private JButton _btnRemove;
  private JButton _btnGotoSource;

  public EditBreakpointsDialog( Breakpoint bp )
  {
    this();
    _listBreakpoints.setSelectedValue( bp, true );
  }

  public EditBreakpointsDialog() throws HeadlessException
  {
    super( "Edit Breakpoints" );
    configUi();
  }

  protected JComponent createCenterPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout( new GridBagLayout() );
    panel.setBorder( BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ),
      BorderFactory.createEmptyBorder( 8, 8, 8, 8  ) ) );

    GridBagConstraints c = new GridBagConstraints();

    createBreakpointsList();
    JScrollPane scroller = new JScrollPane( _listBreakpoints );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 2, 2, 0, 0 );
    panel.add( scroller, c );

    _btnGotoSource = new JButton( "Go To" );
    _btnGotoSource.setMnemonic( 'G' );
    _btnGotoSource.addActionListener( new GotoAction() );
    _btnGotoSource.setEnabled( false );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 4, 2, 0 );
    panel.add( _btnGotoSource, c );

    _btnViewSource = new JButton( "View Source" );
    _btnGotoSource.setMnemonic( 'V' );
    _btnViewSource.addActionListener( new ViewSourceAction() );
    _btnViewSource.setEnabled( false );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 4, 2, 0 );
    panel.add( _btnViewSource, c );

    _btnRemove = new JButton( "Remove" );
    _btnGotoSource.setMnemonic( 'R' );
    _btnRemove.addActionListener( new RemoveAction() );
    _btnRemove.setEnabled( false );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 4, 2, 0 );
    panel.add( _btnRemove, c );

    JPanel filler = new JPanel();
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.VERTICAL;
    c.gridx = 1;
    c.gridy = 3;
    c.gridwidth = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 0;
    c.weighty = 1;
    c.insets = new Insets( 2, 4, 2, 0 );
    panel.add( filler, c );

    return panel;
  }

  private void createBreakpointsList()
  {
    _listBreakpoints = new JList<>( makeBreakpointsModel() );
    _listBreakpoints.setCellRenderer( new BreakpointCellRenderer() );
    _listBreakpoints.getSelectionModel().addListSelectionListener(
      e -> {
        boolean bEnabled = _listBreakpoints.getSelectedIndex() >= 0;
        _btnGotoSource.setEnabled( bEnabled );
        _btnViewSource.setEnabled( bEnabled );
        _btnRemove.setEnabled( bEnabled );
      } );
    _listBreakpoints.addMouseListener(
      new MouseAdapter()
      {
        public void mouseClicked( MouseEvent e )
        {
          Breakpoint bp = _listBreakpoints.getSelectedValue();
          if( bp == null )
          {
            return;
          }
          BreakpointCellRenderer renderer = (BreakpointCellRenderer)_listBreakpoints.getCellRenderer();
          renderer.getListCellRendererComponent( _listBreakpoints, bp, _listBreakpoints.getSelectedIndex(), true, false );
          renderer.setSize( renderer.getPreferredSize() );
          renderer.doLayout();
          if( renderer._cbActive.contains( e.getX(), e.getY()%renderer.getHeight() ) )
          {
            bp.setActive( !bp.isActive() );
            _listBreakpoints.repaint();

            BreakpointManager bpm = getGosuPanel().getBreakpointManager();
            Breakpoint csr = bpm.getBreakpoint( bp );
            if( csr != null )
            {
              csr.setActive( bp.isActive() );
            }
            GosuEditor view = getGosuPanel().getGosuEditor();
            if( view != null )
            {
              java.util.List<? extends JComponent> columns = EditorUtilities.findDecendents( view.getComponent(), EditorScrollPane.AdviceColumn.class );
              for( JComponent column : columns )
              {
                column.repaint();
              }
            }
          }
        }
      } );
  }

  protected void setInitialSize()
  {
    setSize( 550, 400 );
  }

  private DefaultListModel<Breakpoint> makeBreakpointsModel()
  {
    BreakpointManager bpm = getGosuPanel().getBreakpointManager();
    DefaultListModel<Breakpoint> model = new DefaultListModel<>();
    bpm.getBreakpoints().forEach( model::addElement );
    return model;
  }

  protected void close()
  {
    super.close();
  }

  class BreakpointCellRenderer extends JPanel implements ListCellRenderer<Breakpoint>
  {
    private Breakpoint _bp;
    private LabCheckbox _cbActive;
    private DefaultListCellRenderer _label;

    public BreakpointCellRenderer()
    {
      super( new BorderLayout() );
      configUi();
    }

    public Dimension getPreferredSize()
    {
      Dimension dim = super.getPreferredSize();
      dim.height = _label.getPreferredSize().height;
      return dim;
    }

    private void configUi()
    {
      _cbActive = new LabCheckbox();
      _cbActive.setBorderPaintedFlat( true );
      _cbActive.setOpaque( false );

      add( _cbActive, BorderLayout.WEST );

      _label =
        new DefaultListCellRenderer()
        {
          public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
          {
            Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
            Breakpoint bp = (Breakpoint)value;
            setText( bp.getTitle() );
            setIcon( bp.isActive()
                     ? EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" )
                     : EditorUtilities.loadIcon( "images/disabled_breakpoint.png" ) );
            setEnabled( bp.isActive() );
            return c;
          }
        };
      add( _label, BorderLayout.CENTER );
    }

    void setBreakpoint( Breakpoint bp )
    {
      _bp = bp;
    }

    public Component getListCellRendererComponent( JList list, Breakpoint bp, int index, boolean isSelected, boolean cellHasFocus )
    {
      _label.getListCellRendererComponent( list, bp, index, isSelected, cellHasFocus );
      setBreakpoint( bp );
      _cbActive.setSelected( _bp.isActive() );
      if( isSelected )
      {
        setBackground( list.getSelectionBackground() );
        setForeground( list.getSelectionForeground() );
      }
      else
      {
        setBackground( list.getBackground() );
        setForeground( list.getForeground() );
      }
      return this;
    }
  }

  private class GotoAction extends ViewSourceAction
  {
    public void actionPerformed( ActionEvent e )
    {
      super.actionPerformed( e );
      close();
    }
  }

  private class ViewSourceAction extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      Breakpoint bp = _listBreakpoints.getSelectedValue();
      if( bp != null )
      {
        String strType = bp.getFqn();
        getGosuPanel().openType( strType, false );
        SettleModalEventQueue.instance().run();
        GosuEditor currentEditor = getGosuPanel().getCurrentEditor();
        if( currentEditor != null )
        {
          int iLine = bp.getLine();
          if( iLine >= 1 )
          {
            JTextComponent editor = currentEditor.getEditor();
            Element root = editor.getDocument().getRootElements()[0];
            iLine = root.getElementCount() < iLine ? root.getElementCount() : iLine;
            Element line = root.getElement( iLine - 1 );
            editor.setCaretPosition( line.getStartOffset() );
          }
        }
      }
    }
  }

  private GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  private class RemoveAction extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      int[] indexes = _listBreakpoints.getSelectedIndices();
      for( int i = indexes.length-1; i >= 0; i-- )
      {
        Breakpoint bp = (Breakpoint)((DefaultListModel)_listBreakpoints.getModel()).remove( indexes[i] );
        BreakpointManager bpm = getGosuPanel().getBreakpointManager();
        bpm.removeBreakpoint( bp );
      }
      if( indexes.length > 0 )
      {
        int iIndex = indexes[0];
        if( _listBreakpoints.getModel().getSize() > iIndex )
        {
          _listBreakpoints.setSelectedIndex( iIndex );
        }
        else if( iIndex != 0 && _listBreakpoints.getModel().getSize() == iIndex )
        {
          _listBreakpoints.setSelectedIndex( iIndex-1 );
        }
      }
    }
  }
}