uses java.awt.EventQueue
uses javax.swing.UIManager

EventQueue.invokeLater( \-> {
  UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() )
  new BoardFrame().Visible = true
} )
