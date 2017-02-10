package editor;

import editor.util.LabButton;
import editor.util.LabCheckbox;
import editor.util.TextComponentUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.util.GosuRefactorUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Provides the extract-variable refactor functionality for a Gosu editor
 *
 * @author cgross
 */
public class ExtractVariablePopup extends JDialog
{
  private IdentifierTextField _varName;
  private GosuEditor _gosuEditor;
  private LabCheckbox _replaceAll;
  private JButton _okBtn;
  private static final String CLOSE = "_close";
  private static final String OK = "_ok";
  private JButton _cancelBtn;

  public ExtractVariablePopup() throws HeadlessException
  {
    super( LabFrame.instance(), "Extract Variable", true );

    setLayout( new BorderLayout() );

    getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), CLOSE );
    getRootPane().getActionMap().put( CLOSE, new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        _cancelBtn.doClick();
      }
    } );

    getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), OK );
    getRootPane().getActionMap().put( OK, new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        _okBtn.doClick();
      }
    } );

    JPanel mainPanel = new JPanel( new GridBagLayout() );
    mainPanel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

    GridBagConstraints c = new GridBagConstraints();
    int iX = 0;
    int iY = 0;

    _varName = new IdentifierTextField();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = iX++;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 4 );
    JLabel nameLabel = new JLabel( "Name:" );
    nameLabel.setDisplayedMnemonic( 'N' );
    nameLabel.setLabelFor( _varName );
    mainPanel.add( nameLabel, c );

    _varName.getDocument().addDocumentListener( new DocumentListener()
    {
      @Override
      public void insertUpdate( DocumentEvent e )
      {
        _okBtn.setEnabled( _varName.getText().length() > 0 );
      }

      @Override
      public void removeUpdate( DocumentEvent e )
      {
        _okBtn.setEnabled( _varName.getText().length() > 0 );
      }

      @Override
      public void changedUpdate( DocumentEvent e )
      {
      }
    } );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = iX;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    mainPanel.add( _varName, c );

    _replaceAll = new LabCheckbox();

    _replaceAll = new LabCheckbox( "Replace All Occurrences" );
//    _replaceAll.setMnemonic( 'A' );
//    _replaceAll.setDisplayedMnemonicIndex( 8 );
//    _replaceAll.setSelected( true );
//    c.anchor = GridBagConstraints.WEST;
//    c.fill = GridBagConstraints.NONE;
//    c.gridx = iX = 0;
//    c.gridy = iY;
//    c.gridwidth = GridBagConstraints.REMAINDER;
//    c.gridheight = 1;
//    c.weightx = 0;
//    c.weighty = 0;
//    c.insets = new Insets( 0, 0, 10, 0 );
//    add( _replaceAll, c );

    add( mainPanel, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    FlowLayout layout = new FlowLayout( FlowLayout.RIGHT );
    layout.setVgap( 0 );
    buttonPanel.setLayout( layout );
    _okBtn = new LabButton( new AbstractAction( "OK" )
             {
               @Override
               public void actionPerformed( ActionEvent e )
               {
                 extractVariable( _gosuEditor, _varName.getText(), _replaceAll.isSelected() );
                 setVisible( false );
               }
             } );
    _okBtn.setEnabled( false );

    _cancelBtn = new LabButton( new AbstractAction( "Cancel" )
                 {
                   @Override
                   public void actionPerformed( ActionEvent e )
                   {
                     ExtractVariablePopup.this.setVisible( false );
                   }
                 } );
    buttonPanel.add( _okBtn );
    buttonPanel.add( _cancelBtn );

    add( buttonPanel, BorderLayout.SOUTH );

    setSize( 240, 140 );
  }

  static void extractVariable( GosuEditor gosuEditor, String varName, boolean replaceAll )
  {

    IParsedElement rootScopeElement = findValidRootScopeElement( gosuEditor );
    if( rootScopeElement == null )
    {
      throw new IllegalStateException( "Unable to extract a variable outside of a function or program" );
    }

    if( !verifyExpressionSelected( gosuEditor ) )
    {
      throw new IllegalStateException( "Unable to extract a variable unless an expression is selected" );
    }

    gosuEditor.getUndoManager().beginUndoAtom( "Extract Variable" );

    try
    {
      String entireProgram = gosuEditor.getText();

      Document document = gosuEditor.getEditor().getDocument();

      int start = gosuEditor.getEditor().getSelectionStart();
      int end = gosuEditor.getEditor().getSelectionEnd() - 1;
      IParseTree deepestLocation = gosuEditor.getDeepestLocationSpanning( start, end );
      int varInsertionPoint = deepestLocation.getOffset();
      String selectedText = document.getText( varInsertionPoint, deepestLocation.getLength() );
      boolean standaloneExpression = deepestLocation.getParsedElement().getParent() instanceof INoOpStatement;

      if( replaceAll )
      {
//        //find all matches below the bounding element
//        List<IParsedElement> matches = GosuRefactorUtil.findAllMatchingExpressionsWithinScope(
//          selectedText,
//          entireProgram,
//          rootScopeElement,
//          deepestLocation.getParsedElement() );
//
//        //Find the dominating element for the matching elements
//        IParsedElement dominatingElement = GosuRefactorUtil.findDominatingElement( matches );
//
//        //insert just before this element
//        varInsertionPoint = dominatingElement.getLocation().getOffset();
//
//        //shift offset is how far we must shift back the replacedment due to the
//        //delta between the replaced text and the variable name
//        int shiftOffset = selectedText.length() - varName.length();
//        int offset = 0;
//
//        //For each match, replace
//        for( IParsedElement elt : matches )
//        {
//          int eltStart = elt.getLocation().getOffset();
//          int position = eltStart - offset;
//          replaceStringInDocument( document, position, selectedText, varName );
//          offset += shiftOffset;
//        }
      }
      else
      {
        if( standaloneExpression )
        {
          document.remove( varInsertionPoint, selectedText.length() );
        }
        else
        {
          //If we are not replacing all, simply replace the single match
          replaceStringInDocument( document, varInsertionPoint, selectedText, varName );
        }
      }

      String varString = "";
      while( !(deepestLocation.getParsedElement() instanceof IStatement) )
      {
        deepestLocation = deepestLocation.getParent();
      }
      int lineStart;
      if( !standaloneExpression )
      {
        lineStart = TextComponentUtil.getLineStart( entireProgram, deepestLocation.getOffset() );
        int i = lineStart;
        while( Character.isWhitespace( entireProgram.charAt( i ) ) )
        {
          varString += entireProgram.charAt( i );
          i++;
        }
      }
      else
      {
        lineStart = varInsertionPoint;
      }

      varString += "var " + varName + " = " + selectedText + (standaloneExpression ? "" : "\n");
      document.insertString( lineStart, varString, null );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      gosuEditor.getUndoManager().endUndoAtom();
      gosuEditor.parse();
    }
  }

  private static void replaceStringInDocument( Document document, int position, String textToReplace, String replacementText )
    throws BadLocationException
  {
    document.remove( position, textToReplace.length() );
    document.insertString( position, replacementText, null );
  }

  private static IParsedElement findValidRootScopeElement( GosuEditor gosuEditor )
  {
    int start = gosuEditor.getEditor().getSelectionStart();
    IParsedElement rootContainerElement = GosuRefactorUtil.boundingParent( gosuEditor.getParser().getLocations(),
                                                                           start,
                                                                           IProgram.class,
                                                                           IClassFileStatement.class,
                                                                           IFunctionStatement.class,
                                                                           IPropertyStatement.class );
    if( rootContainerElement == null )
    {
      return null;
    }
    else if( rootContainerElement instanceof IProgram ||
             rootContainerElement instanceof IClassFileStatement )
    { //IClassFileStatement indicates a program
      return rootContainerElement;
    }
    else
    {
      ArrayList<IStatement> stmtList = new ArrayList<IStatement>();
      boolean eltFound = rootContainerElement.getContainedParsedElementsByType( IStatement.class, stmtList );

      if( eltFound )
      {
        IStatement list = stmtList.get( 0 );
        if( list.getLocation().contains( start ) )
        {
          return list;
        }
      }

      return null;
    }
  }

  public void showNow( GosuEditor gosuEditor )
  {
    _gosuEditor = gosuEditor;

    IParsedElement rootScopeElement = findValidRootScopeElement( _gosuEditor );
    if( rootScopeElement == null )
    {
      editor.util.EditorUtilities.displayError( "Unable to extract a variable" );
      return;
    }

    boolean validExprSelected = verifyExpressionSelected( _gosuEditor );
    if( !validExprSelected )
    {
      editor.util.EditorUtilities.displayError( "Cannot extract a variable unless a valid expression is selected" );
      return;
    }

    editor.util.EditorUtilities.centerWindowInFrame( this, SwingUtilities.getWindowAncestor( gosuEditor ) );
    setVisible( true );
  }

  private static boolean verifyExpressionSelected( GosuEditor gosuEditor )
  {
    int start = gosuEditor.getEditor().getSelectionStart();
    int end = gosuEditor.getEditor().getSelectionEnd() - 1;
    IParseTree deepestLocation = gosuEditor.getDeepestLocationSpanning( start, end );

    if( deepestLocation.getParsedElement() instanceof IExpression )
    {
      if( deepestLocation.getOffset() == start &&
          deepestLocation.getExtent() == end )
      {
        return true;
      }

      try
      {
        if( deepestLocation.getOffset() == start &&
            deepestLocation.getExtent() == end - 1 &&
            gosuEditor.getEditor().getText( end, 1 ).equals( ";" ) )
        {
          return true;
        }
      }
      catch( BadLocationException e )
      {
        //ignore
      }
    }

    return false;
  }

}
