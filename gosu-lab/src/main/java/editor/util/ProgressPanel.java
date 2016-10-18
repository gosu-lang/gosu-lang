package editor.util;

import editor.Scheme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple progress meter style modal window to display during lengthy
 * operations.
 * <p>
 * A typical use case for this style of progress feedback involves a worker
 * thread responsible for performing some potentially long operation. The worker
 * thread calls back to the UI thread via <code>IProgressCallback</code>. The UI
 * thread's implementation of <code>IProgressCallback</code> must ensure its
 * method calls execute in the UI thread (aka AWT Dispatch Thread);
 * EventQueue.invokeLater() facilitates this requirement. The UI thread calls
 * <code>ProgressPanel.updateProgress()</code> in response to
 * <code>IProgressCallback.updateProgress()</code>.
 * <p>
 * Most of the time you should use <code>ProgressFeedback.runWithProgress()</code>
 * instead of using this class directly.
 */
public class ProgressPanel extends JPanel
{
  private JProgressBar _progress;
  private JLabel _labelProgress;
  private ActionListener _abortListener;

  /**
   * Construct a ProgressPanel with specified number of units, notice text, and abort action listener.
   *
   * @param iLength       The number units the operation is expected to complete.
   * @param strNotice     A text message to display indicating what the operation is doing at a high level.
   * @param abortListener An action listener to notify if the user aborts the operation.
   */
  public ProgressPanel( int iLength, String strNotice, ActionListener abortListener )
  {
    this( iLength, strNotice, abortListener, false );
  }

  public ProgressPanel( int iLength, String strNotice, ActionListener abortListener, boolean bProLife )
  {
    _abortListener = abortListener;
    configUI( strNotice, iLength, bProLife );
  }

  public void setLength( int iLength )
  {
    _progress.setValue( 0 );
    _progress.setMaximum( iLength < 0 ? 0 : iLength );
    _progress.setIndeterminate( iLength < 0 );
  }

  public int getLength()
  {
    return _progress.getMaximum();
  }

  /**
   * @param strNotice
   * @param iLength
   */
  protected void configUI( String strNotice, int iLength, boolean bProLife )
  {
    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    setBorder( border );
    setLayout( new BorderLayout() );
    setBackground( Scheme.active().getWindow() );

    JPanel panelCenter = new JPanel( new BorderLayout() );
    panelCenter.setBackground( Scheme.active().getWindow() );
    panelCenter.setForeground( UIManager.getColor( "windowText" ) );
    panelCenter.setBorder( new javax.swing.border.EmptyBorder( 10, 10, 10, 10 ) );
    panelCenter.setOpaque( true );

    JLabel labelNotice = new JLabel( strNotice );
    labelNotice.setFont( labelNotice.getFont().deriveFont( Font.BOLD ) );
    labelNotice.setBorder( new javax.swing.border.EmptyBorder( 0, 0, 7, 0 ) );
    labelNotice.setBackground( Scheme.active().getWindow() );
    labelNotice.setForeground( UIManager.getColor( "windowText" ) );
    labelNotice.setOpaque( true );
    panelCenter.add( BorderLayout.NORTH, labelNotice );

    add( BorderLayout.NORTH, panelCenter );

    final JButton btnAbort = new JButton( "Abort" );
    btnAbort.setMnemonic( 'A' );
    btnAbort.setFocusable( false );
    btnAbort.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

    JPanel panelCtrl = new JPanel( new BorderLayout() );
    _progress = new JProgressBar( 0, iLength < 0 ? 0 : iLength );
    _progress.setBorder( BorderFactory.createEmptyBorder() );
    _progress.setUI( new LabProgressBarUI() );
    if( iLength < 0 )
    {
      _progress.setIndeterminate( true );
    }
    _progress.setBackground( Scheme.active().getWindow() );
    JPanel progressPanel = new JPanel( new BorderLayout() );
    progressPanel.add( BorderLayout.CENTER, _progress );
    progressPanel.setBackground( Scheme.active().getWindow() );
    progressPanel.setBorder( BorderFactory.createEmptyBorder( 0, 10, 15, 10 ) );
    _labelProgress = new JLabel();
    _labelProgress.setBackground( Scheme.active().getWindow() );
    _labelProgress.setForeground( UIManager.getColor( "windowText" ) );
    _labelProgress.setOpaque( true );
    progressPanel.add( BorderLayout.SOUTH, _labelProgress );
    panelCtrl.add( BorderLayout.NORTH, progressPanel );

    JPanel panelButtons = new JPanel( new FlowLayout() );
    panelButtons.setBackground( Scheme.active().getWindow() );
    panelButtons.add( btnAbort );
    panelCtrl.add( BorderLayout.SOUTH, panelButtons );
    add( BorderLayout.SOUTH, panelCtrl );

    btnAbort.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        // dispose();
        _abortListener.actionPerformed( e );
        btnAbort.setEnabled( false );
        btnAbort.setText( "Aborting..." );
      }
    } );
    if( bProLife )
    {
      btnAbort.setVisible( false );
    }
  }

  /**
   * @param iProgress
   */
  public void updateProgress( int iProgress, String strProgress )
  {
    _progress.setValue( iProgress );
    updateProgress( strProgress );
  }

  public void updateProgress( String strProgress )
  {
    _labelProgress.setText( strProgress );
    revalidate();
    //_progress.paintImmediately( 0, 0, _progress.getWidth(), _progress.getHeight() );
  }

  public int getProgess()
  {
    return _progress.getValue();
  }

}
