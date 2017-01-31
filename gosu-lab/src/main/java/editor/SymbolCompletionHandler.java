package editor;

import editor.util.EditorUtilities;
import editor.util.TextComponentUtil;
import gw.lang.GosuShop;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.GosuTypes;
import gw.util.GosuObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class SymbolCompletionHandler extends AbstractPathCompletionHandler
{
  @Override
  public boolean handleCompletePath( ISymbolTable transientSymTable )
  {
    final GosuEditor gsEditor = getGosuEditor();
    boolean bDotAtCaret = GosuObjectUtil.equals( TextComponentUtil.getWordAtCaret( gsEditor.getEditor() ), "." ) ||
                          GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforeCaret( gsEditor.getEditor() ), "." ) ||
                          GosuObjectUtil.equals( TextComponentUtil.getWordAtCaret( gsEditor.getEditor() ), ":" ) ||
                          GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforeCaret( gsEditor.getEditor() ), ":" );
    if( bDotAtCaret )
    {
      return false;
    }
    IParseTree locationAtCaret = gsEditor.getDeepestLocationAtCaret();
    if( locationAtCaret != null &&
        InitializerCompletionHandler.isInitializerStart( locationAtCaret.getParsedElement() ) )
    {
      return false;
    }

    String strMemberPath = getSingleNameAtCaret();
    strMemberPath = strMemberPath != null ? strMemberPath.trim() : null;

    String wordBeforeCaret = TextComponentUtil.getPartialWordBeforeCaret( getGosuEditor().getEditor() );
    boolean isAtAsPosition = wordBeforeCaret != null && wordBeforeCaret.trim().equals( Keyword.KW_as.toString() );
    if( gsEditor.isAltDown() || isAtAsPosition )
    {
      return displayTypesPopup( strMemberPath );
    }

    //noinspection unchecked
    List<ISymbol> listSymbols = new ArrayList<ISymbol>( transientSymTable.getSymbols().values() );
    filterUnwantedSymbols( listSymbols );
    IType expectedType = addValuesForType( listSymbols );

    ISymbol[] symbols = listSymbols.toArray( new ISymbol[listSymbols.size()] );
    SymbolPopup valuePopup = new SymbolPopup( symbols, strMemberPath, gsEditor, expectedType );
    valuePopup.addNodeChangeListener(
      e -> {
        String word = (String)e.getSource();
        if( word.endsWith( "()" ) )
        {
          TextComponentUtil.replaceWordAtCaretDynamicAndRemoveEmptyParens( gsEditor.getEditor(),
                                                                           word,
                                                                           gsEditor.getReplaceWordCallback(),
                                                                           true,
                                                                           valuePopup.isReplaceWholeWord() );
        }
        else
        {
          TextComponentUtil.replaceWordAtCaretDynamic( gsEditor.getEditor(),
                                                       word,
                                                       gsEditor.getReplaceWordCallback(),
                                                       true,
                                                       valuePopup.isReplaceWholeWord() );
        }
        gsEditor.getEditor().requestFocus();
        EditorUtilities.fixSwingFocusBugWhenPopupCloses( gsEditor );
        gsEditor.getEditor().repaint();
      } );
    gsEditor.setCompletionPopup( valuePopup );
    gsEditor.displayCompletionPopup( gsEditor.getEditor().getCaretPosition() );
    if( !valuePopup.isShowing() && !valuePopup.wasAutoDismissed() )
    {
      return displayTypesPopup( strMemberPath );
    }

    return false;
  }

  private IType addValuesForType( List<ISymbol> listSymbols )
  {
    IType expectedType = getGosuEditor().findExpectedTypeErrorAtCaret();
    if( expectedType != null && expectedType.isEnum() )
    {
      int[] i = {0};
      ((IEnumType)expectedType).getEnumConstants().forEach( e -> listSymbols.add( i[0]++, GosuShop.createSymbol( e, expectedType, null ) ) );
    }
    return expectedType;
  }

  private void filterUnwantedSymbols( Collection<ISymbol> listSymbols )
  {
    List<ISymbol> deleteSyms = new ArrayList<>();
    for( ISymbol s : listSymbols )
    {
      if( s.getType() == GosuTypes.DEF_CTOR_TYPE() || (s instanceof IDynamicFunctionSymbol && ((IDynamicFunctionSymbol)s).isConstructor()) )
      {
        // Filter constructor symbols (only applicable when editing a gs class)
        deleteSyms.add( s );
      }
      else if( s instanceof IDynamicFunctionSymbol && s.getName().startsWith( "@" ) )
      {
        // This is the getter or setter for a property, and the DPS will be in the list already
        deleteSyms.add( s );
      }
    }
    listSymbols.removeAll( deleteSyms );
  }

  boolean displayTypesPopup( String strPrefix )
  {
    TypePopup popup = new TypePopup( strPrefix, getGosuEditor(), isAnnotationsOnly() );
    getGosuEditor().setCompletionPopup( popup );
    final boolean addUsesAutomatically = getGosuEditor().acceptsUses();
    popup.addNodeChangeListener(
      e -> {
        String strQualifedType = (String)e.getSource();
        String strRelativeType = TypePopup.getRelativeTypeName( strQualifedType );
        String strPartialType = strQualifedType;
        if( strPartialType.contains( TextComponentUtil.getWordAtCaret( getGosuEditor().getEditor() ) ) )
        {
          strPartialType = strPartialType.substring( strPartialType.indexOf( TextComponentUtil.getWordAtCaret( getGosuEditor().getEditor() ) ) );
        }

        if( addUsesAutomatically )
        {
          // Need to do this before we replace word because replace word causes
          // reparse, which would leave usesStmts in incomplete state.
          getGosuEditor().addToUses( strQualifedType );
        }

        //noinspection StatementWithEmptyBody
        if( addUsesAutomatically &&
            GosuObjectUtil.equals( TextComponentUtil.getPartialWordBeforeCaret( getGosuEditor().getEditor() ), strRelativeType ) )
        {
          //no need to replace the text, since it is already there
        }
        else
        {
          TextComponentUtil.replaceWordAtCaretDynamic(
            getGosuEditor().getEditor(),
            addUsesAutomatically ? strRelativeType : strPartialType,
            getGosuEditor().getReplaceWordCallback(), false, popup.isReplaceWholeWord() );
        }


        getGosuEditor().getEditor().requestFocus();
        EditorUtilities.fixSwingFocusBugWhenPopupCloses( getGosuEditor() );
        getGosuEditor().getEditor().repaint();
      } );
    getGosuEditor().displayCompletionPopup( getGosuEditor().getEditor().getCaretPosition() );
    return true;
  }

  protected boolean isAnnotationsOnly()
  {
    return false;
  }
}
