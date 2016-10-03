package editor;

import editor.util.HTMLEscapeUtil;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IHasArguments;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParameterInfoPopup extends JPopupMenu
{

  private JPanel _pane = new JPanel();
  private GosuEditor _editor;
  private EditorKeyListener _editorKeyListener;
  private CaretListener _editorCaretListener;
  private UndoableEditListener _docListener;
  private JPanel _labelContainer;
  private int _iArgPosition;


  public ParameterInfoPopup( GosuEditor editor )
  {
    super();
    _editor = editor;
    initLayout();
  }

  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    GridBagLayout gridBag = new GridBagLayout();
    _pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    IParameterInfo[][] paramInfoLists = getParamInfoLists( _editor.getFunctionCallAtCaret() );
    if( paramInfoLists == null || paramInfoLists.length == 0 )
    {
      EventQueue.invokeLater(
        new Runnable()
        {
          @Override
          public void run()
          {
            setVisible( false );
          }
        } );
      return;
    }
    _labelContainer = new JPanel();
    _labelContainer.setLayout( new BoxLayout( _labelContainer, BoxLayout.Y_AXIS ) );
    _labelContainer.setBackground( Scheme.active().getTooltipBackground() );
    _labelContainer.setBorder( BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
    int iArgIndex = getArgIndex();
    for( int i = 0; i < paramInfoLists.length; i++ )
    {
      IParameterInfo[] paramInfoList = paramInfoLists[i];
      addParameterListLabel( paramInfoList, _labelContainer, i != paramInfoLists.length - 1, iArgIndex );
    }
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    _pane.add( _labelContainer, c );

    //
    // The Symbol list
    //

    add( _pane );

    if( _editor != null )
    {
      _editorKeyListener = new EditorKeyListener();
      _editorCaretListener = new EditorCaretListener();
      _docListener = new UndoableEditListener()
      {
        @Override
        public void undoableEditHappened( UndoableEditEvent e )
        {
          filterDisplay();
        }
      };
    }
  }

  private int getArgIndex()
  {
    IParsedElement function = _editor.getFunctionCallAtCaret();
    if( function == null )
    {
      return -1;
    }
    int iCaretPos = _editor.getEditor().getCaretPosition();
    List args = function.getLocation().getChildren();
    int iNonArgLocations = 0;
    for( int i = args.size() - 1; i >= 0; i-- )
    {
      IParseTree location = (IParseTree)args.get( i );
      if( (function instanceof IBeanMethodCallExpression &&
           ((IBeanMethodCallExpression)function).getRootExpression().getLocation().contains( location )) ||
          (function instanceof INewExpression && !Arrays.stream( ((INewExpression)function).getArgs() ).anyMatch( e -> e.equals( location.getParsedElement() ) )) )
      {
        iNonArgLocations++;
      }
      else if( location.getExtent() + 1 >= iCaretPos )
      {
        return (args.size() - 1) - i - iNonArgLocations;
      }
    }
    return Math.max( 0, args.size() - 1 - iNonArgLocations );
  }

  private void addParameterListLabel( IParameterInfo[] paramList, JPanel container, boolean bBorder, int iArgIndex )
  {
    JLabel label = new JLabel();
    label.setBorder( bBorder ? BorderFactory.createMatteBorder( 0, 0, 1, 0, Scheme.active().getControlShadow() ) : null );
    label.setFont( _editor.getEditor().getFont() );
    label.setText( makeParamInfoContent( paramList, iArgIndex ) );
    container.add( label );
  }

  private String makeParamInfoContent( IParameterInfo[] paramInfoList, int iArgIndex )
  {
    String strContent = "<html>";
    if( paramInfoList == null || paramInfoList.length == 0 )
    {
      strContent += "&lt;" + "no parameters" + "&gt;";
    }
    else
    {
      boolean bUseName = false;
      for( int j = 0; j < paramInfoList.length; j++ )
      {
        IParameterInfo pi = paramInfoList[j];
        if( pi.getName() != null && pi.getName().length() > 0 && !pi.getName().equalsIgnoreCase( pi.getFeatureType().getRelativeName() ) )
        {
          bUseName = true;
          break;
        }
      }

      for( int j = 0; j < paramInfoList.length; j++ )
      {
        IParameterInfo pi = paramInfoList[j];
        boolean bBlock = pi.getFeatureType() instanceof IBlockType;
        String strTypeName = bBlock
                             ? (pi.getName() + ((IBlockType)pi.getFeatureType()).getRelativeNameSansBlock())
                             : pi.getFeatureType().getRelativeName();
        strTypeName = HTMLEscapeUtil.escape( strTypeName );
        if( !bBlock &&
            bUseName &&
            pi.getName() != null &&
            pi.getName().length() > 0 )
        {
          if( j == iArgIndex )
          {
            strContent += "<b><i>" + pi.getName() + "</i></b>";
          }
          else
          {
            strContent += "<i>" + pi.getName() + "</i>";
          }
          strContent += ": ";
        }
        if( j == iArgIndex )
        {
          strContent += "<b>" + strTypeName + "</b>";
        }
        else
        {
          strContent += strTypeName;
        }
        if( j < paramInfoList.length - 1 )
        {
          strContent += ", ";
        }
      }
    }
    return strContent + "</html>";
  }

  private IParameterInfo[][] getParamInfoLists( IParsedElement parsedElement )
  {
    if( parsedElement instanceof IHasArguments )
    {
      int iArgPosition = ((IHasArguments)parsedElement).getArgPosition();
      if( _editor.getEditor().getCaretPosition() < iArgPosition )
      {
        // Caret must be positioned within the arg-list
        return getParamInfoLists( _editor.findFunction( parsedElement.getParent() ) );
      }
      _iArgPosition = iArgPosition;

      List<IParameterInfo[]> paramInfoLists = new ArrayList<IParameterInfo[]>();
      if( parsedElement instanceof IMethodCallExpression )
      {
        IFunctionSymbol fs = ((IMethodCallExpression)parsedElement).getFunctionSymbol();
        if( fs != null )
        {
          IFunctionType funcType = (IFunctionType)fs.getType();
          if( funcType.getMethodInfo() != null )
          {
            return new IParameterInfo[][]{funcType.getMethodInfo().getParameters()};
          }
          else if( funcType.getScriptPart() != null && funcType.getScriptPart().getContainingType() instanceof IGosuClass )
          {
            IGosuClass type = (IGosuClass)funcType.getScriptPart().getContainingType();
            String strName = funcType.getName();
            List methods = type.getTypeInfo().getMethods( type );
            for( Object method : methods )
            {
              IMethodInfo mi = (IMethodInfo)method;
              if( mi.getDisplayName().equalsIgnoreCase( strName ) && mi.isVisible( _editor.getScriptabilityModifier() ) )
              {
                paramInfoLists.add( mi.getParameters() );
              }
            }
          }
          else
          {
            Map<String, List<IFunctionSymbol>> dfsDecls = _editor.getParser().getDfsDecls();
            List<IFunctionSymbol> dynamicFunctionSymbols = dfsDecls.get( funcType.getName() );
            if( dynamicFunctionSymbols != null )
            {
              for( IFunctionSymbol symbol : dynamicFunctionSymbols )
              {
                assert symbol instanceof IDynamicFunctionSymbol;
                IDynamicFunctionSymbol dfs = (IDynamicFunctionSymbol)symbol;
                IParameterInfo[] params = new IParameterInfo[dfs.getArgs().size()];
                for( int i = 0; i < dfs.getArgs().size(); i++ )
                {
                  final ISymbol arg = dfs.getArgs().get( i );
                  params[i] = new ParameterInfoStub( arg );
                }
                paramInfoLists.add( params );
              }
            }
          }
        }
        return paramInfoLists.toArray( new IParameterInfo[0][0] );
      }
      if( parsedElement instanceof IBeanMethodCallExpression )
      {
        IMethodInfo mi = ((IBeanMethodCallExpression)parsedElement).getMethodDescriptor();
        if( mi != null )
        {
          String strName = mi.getDisplayName();
          IType type = mi.getOwnersType();
          List methods = type.getTypeInfo().getMethods();
          for( int i = 0; i < methods.size(); i++ )
          {
            mi = (IMethodInfo)methods.get( i );
            if( mi.getDisplayName().equalsIgnoreCase( strName ) && mi.isVisible( _editor.getScriptabilityModifier() ) )
            {
              paramInfoLists.add( mi.getParameters() );
            }
          }
        }
        return paramInfoLists.toArray( new IParameterInfo[0][0] );
      }
      if( parsedElement instanceof INewExpression )
      {
        INewExpression newExpression = (INewExpression)parsedElement;
        if( !(newExpression.getType() instanceof IErrorType) )
        {
          IType type = newExpression.getType();
          List<? extends IConstructorInfo> ctors = type.getTypeInfo().getConstructors();
          for( IConstructorInfo ctor : ctors )
          {
            if( ctor.isVisible( _editor.getScriptabilityModifier() ) )
            {
              paramInfoLists.add( ctor.getParameters() );
            }
          }
          return paramInfoLists.toArray( new IParameterInfo[0][0] );
        }
      }
    }
    return null;
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( _editor == null )
    {
      return;
    }

    if( bVisible )
    {
      registerListeners();
    }
    else
    {
      unregisterListeners();
      _editor.getEditor().requestFocus();
    }
  }

  void registerListeners()
  {
    unregisterListeners();

    _editor.getEditor().addKeyListener( _editorKeyListener );
    _editor.getEditor().addCaretListener( _editorCaretListener );
    _editor.getEditor().getDocument().addUndoableEditListener( _docListener );
  }

  void unregisterListeners()
  {
    _editor.getEditor().getDocument().removeUndoableEditListener( _docListener );
    _editor.getEditor().removeCaretListener( _editorCaretListener );
    _editor.getEditor().removeKeyListener( _editorKeyListener );
  }

  void filterDisplay()
  {
    filterDisplay( null );
  }

  void filterDisplay( String strWholePath )
  {
  }

  public void display( int iPosition ) throws BadLocationException
  {
    if( _iArgPosition != 0 )
    {
      iPosition = _iArgPosition;
    }
    Rectangle rcCaretBounds = _editor.getEditor().modelToView( iPosition );
    if( rcCaretBounds == null )
    { // the editor doesn't have a positive size - most likely we're in a test
      show( null, 0, 0 );
    }
    else
    {
      show( _editor.getEditor(), rcCaretBounds.x, rcCaretBounds.y - getPreferredHeight() );
    }
  }

  public int getPreferredHeight()
  {
    if( _labelContainer == null )
    {
      return 0;
    }

    _labelContainer.doLayout();
    return _labelContainer.getPreferredSize().height + 2;
  }

  public static ParameterInfoPopup invoke( final GosuEditor gsEditor, int iPosition )
  {
    try
    {
      ParameterInfoPopup parameterInfoPopup = new ParameterInfoPopup( gsEditor );
      parameterInfoPopup.display( iPosition );

      EventQueue.invokeLater(
        new Runnable()
        {
          @Override
          public void run()
          {
            gsEditor.getEditor().requestFocus();
            gsEditor.getEditor().repaint();
          }
        } );
      return parameterInfoPopup;
    }
    catch( BadLocationException e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
    }
    return null;
  }

  String getLabelAt( int index )
  {
    return ((JLabel)_labelContainer.getComponent( index )).getText();
  }

  class EditorCaretListener implements CaretListener
  {
    @Override
    public void caretUpdate( CaretEvent e )
    {
      EventQueue.invokeLater(
        new Runnable()
        {
          @Override
          public void run()
          {
            ParameterInfoPopup.invoke( _editor, _editor.getEditor().getCaretPosition() );
          }
        } );
    }
  }

  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_ENTER ||
          e.getKeyCode() == KeyEvent.VK_SPACE ||
          e.getKeyCode() == KeyEvent.VK_TAB )
      {
        setVisible( false );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );
        e.consume();
      }
    }
  }

  private static class ParameterInfoStub implements IParameterInfo
  {
    private final ISymbol _arg;

    public ParameterInfoStub( ISymbol arg )
    {
      _arg = arg;
    }

    @Override
    public String getName()
    {
      return _arg.getName();
    }

    @Override
    public IType getFeatureType()
    {
      return _arg.getType();
    }

    @Override
    public IFeatureInfo getContainer()
    {
      return null;
    }

    @Override
    public IType getOwnersType()
    {
      return null;
    }

    @Override
    public String getDisplayName()
    {
      return null;
    }

    @Override
    public String getDescription()
    {
      return null;
    }

  }
}
