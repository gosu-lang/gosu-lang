uses javax.swing.UIManager
uses java.awt.EventQueue

EventQueue.invokeLater( \-> {
  UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() )
  new BoardFrame().Visible = true
} )
