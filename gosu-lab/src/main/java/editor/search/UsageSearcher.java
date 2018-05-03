package editor.search;

import editor.FileTree;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.LabFrame;
import editor.NodeKind;
import editor.util.EditorUtilities;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IToken;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IPropertyAccessIdentifier;
import gw.lang.parser.expressions.IPropertyAsMethodCallIdentifier;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuClass;
import manifold.api.templ.DisableStringLiteralTemplates;

import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class UsageSearcher extends AbstractSearcher
{
  private final UsageTarget _target;
  private final boolean _searchText;
  private boolean _includeMemberUsage;
  private SearchTree _results;

  public UsageSearcher( UsageTarget target, boolean searchText, boolean includeMemberUsage )
  {
    _target = target;
    _searchText = searchText;
    _includeMemberUsage = includeMemberUsage;
  }

  boolean includeMemberUsage()
  {
    return _includeMemberUsage;
  }

  @Override
  public boolean search( FileTree tree, SearchTree results )
  {
    if( isExcluded( tree ) )
    {
      return false;
    }

    IType type = tree.getType();
    if( _searchText && !(type instanceof IGosuClass) )
    {
      return maybeSearchForText( tree, results );
    }

    List<SearchLocation> locations = findUsage( (IGosuClass)type );
    if( locations.isEmpty() )
    {
      return false;
    }

    SearchTree searchTree = getOrMakePath( tree, results );

    for( SearchLocation loc : locations )
    {
      SearchTree.SearchTreeNode node = new SearchTree.SearchTreeNode( tree, loc );
      SearchTree res = new SearchTree( NodeKind.Info, node );
      if( searchTree.getTree() != null )
      {
        EditorUtilities.invokeInDispatchThread( () -> {
          searchTree.addViaModel( res );
          ((DefaultTreeModel)searchTree.getTree().getModel()).nodeChanged( _results );
        } );
      }
      else
      {
        // no tree when searching locally (highlighting usages in editor)
        results.insert( res, res.getChildCount() );
      }
    }
    return true;
  }

  List<SearchLocation> findUsage( IGosuClass gsClass )
  {
    gsClass.isValid();
    IClassStatement classStmt = gsClass.getClassStatement();
    if( classStmt != null )
    {
      IParsedElement pe;
      IClassFileStatement classFileStmt = classStmt.getClassFileStatement();
      if( classFileStmt != null )
      {
        pe = classFileStmt;
      }
      else
      {
        pe = classStmt;
      }
      return findUsage( pe );
    }
    return Collections.emptyList();
  }

  private List<SearchLocation> findUsage( IParsedElement pe )
  {
    //noinspection unchecked
    final List<SearchLocation>[] locations = new List[]{Collections.emptyList()};
    IFeatureInfo rootFi = _target.getRootFeatureInfo();
    if( rootFi instanceof ITypeInfo )
    {
      IType type = rootFi.getOwnersType();
      pe.visit( elem -> locations[0] = findUsage( elem, type, locations[0] ) );
    }
    else if( rootFi instanceof IPropertyInfo )
    {
      pe.visit( elem -> locations[0] = findUsage( elem, (IPropertyInfo)rootFi, locations[0] ) );
    }
    else if( rootFi instanceof IMethodInfo )
    {
      pe.visit( elem -> locations[0] = findUsage( elem, (IMethodInfo)rootFi, locations[0] ) );
    }
    else if( rootFi instanceof IConstructorInfo )
    {
      pe.visit( elem -> locations[0] = findUsage( elem, (IConstructorInfo)rootFi, locations[0] ) );
    }
    else if( rootFi instanceof LocalVarFeatureInfo )
    {
      pe.visit( elem -> locations[0] = findUsage( elem, (LocalVarFeatureInfo)rootFi, locations[0] ) );
    }
    else
    {
      throw new IllegalStateException();
    }
    return locations[0];
  }

  private List<SearchLocation> findUsage( IParsedElement pe, IConstructorInfo findCi, List<SearchLocation> locations )
  {
    if( pe instanceof INewExpression )
    {
      IConstructorInfo ci = ((INewExpression)pe).getConstructor();
      if( ci == findCi )
      {
        return addSearchLocation( pe, locations );
      }

      if( ci == null )
      {
        return locations;
      }

      IType declaringType = ci.getOwnersType();
      if( declaringType == findCi.getOwnersType() )
      {
        ITypeInfo typeInfo = declaringType.getTypeInfo();
        if( typeInfo instanceof IRelativeTypeInfo )
        {
          IConstructorInfo latestCi = ((IRelativeTypeInfo)typeInfo).getConstructor( declaringType, FeatureUtil.getParamTypes( ci.getParameters() ) );
          if( latestCi != null && FeatureUtil.constructorInfosEqual(
            FeatureUtil.findRootConstructorInfo( latestCi ), findCi ) )
          {
            return addSearchLocation( pe, locations );
          }
        }
      }
    }
    return locations;
  }

  private List<SearchLocation> findUsage( IParsedElement pe, IMethodInfo findMi, List<SearchLocation> locations )
  {
    if( pe instanceof IMethodCallExpression )
    {
      IFunctionSymbol funcSym = ((IMethodCallExpression)pe).getFunctionSymbol();
      if( funcSym == null || !findMi.getDisplayName().equals( funcSym.getDisplayName() ) )
      {
        return locations;
      }

      IFunctionType functionType = ((IMethodCallExpression)pe).getFunctionType();
      if( functionType != null )
      {
        IFeatureInfo mi = functionType.getMethodOrConstructorInfo();
        if( mi != null && FeatureUtil.methodInfosEqual( FeatureUtil.findRootMethodInfo( (IMethodInfo)mi ), findMi ) )
        {
          locations = addSearchLocation( findNameToken( findMi.getDisplayName(), pe ), locations );
        }
      }
    }
    else if( pe instanceof IBeanMethodCallExpression )
    {
      if( !findMi.getDisplayName().equals( ((IBeanMethodCallExpression)pe).getMemberName() ) )
      {
        return locations;
      }

      IFunctionType functionType = ((IBeanMethodCallExpression)pe).getFunctionType();
      if( functionType != null )
      {
        IFeatureInfo mi = functionType.getMethodOrConstructorInfo();
        if( mi instanceof IMethodInfo && FeatureUtil.methodInfosEqual( FeatureUtil.findRootMethodInfo( (IMethodInfo)mi ), findMi ) )
        {
          locations = addSearchLocation( findNameToken( ((IBeanMethodCallExpression)pe).getMemberName(), pe ), locations );
        }
      }
    }
    else if( findMi.getName().startsWith( "@" ) )
    {
      String propertyName = findMi.getDisplayName().substring( 1 );

      if( pe instanceof IMemberAccessExpression )
      {
        if( ((IMemberAccessExpression)pe).getType() instanceof INamespaceType )
        {
          return locations;
        }

        if( !propertyName.equals( ((IMemberAccessExpression)pe).getMemberName() ) )
        {
          return locations;
        }

        boolean bSetter = findMi.getParameters().length > 0;

        IGosuClass gsClass = (IGosuClass)findMi.getOwnersType();
        IPropertyInfo findPi = gsClass.getTypeInfo().getProperty( gsClass, propertyName );

        IPropertyInfo pi;
        try
        {
          pi = ((IMemberAccessExpression)pe).getPropertyInfo();
          if( pi != null && propertyInfosEqual( pi, findPi ) )
          {
            IParsedElement parent = pe.getParent();
            if( bSetter )
            {
              if( parent instanceof IMemberAssignmentStatement && (((IMemberAssignmentStatement)parent).getMemberAccess() == pe || ((IMemberAssignmentStatement)parent).getRootExpression() == pe) )
              {
                locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
              }
            }
            else
            {
              if( !(parent instanceof IMemberAssignmentStatement && (((IMemberAssignmentStatement)parent).getMemberAccess() == pe || ((IMemberAssignmentStatement)parent).getRootExpression() == pe)) )
              {
                locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
              }
            }
          }
        }
        catch( Exception e )
        {
          return locations;
        }
      }
      else if( pe instanceof IIdentifierExpression )
      {
        if( pe instanceof IPropertyAccessIdentifier ||
            pe instanceof IPropertyAsMethodCallIdentifier )
        {
          boolean bSetter = findMi.getParameters().length > 0;

          IGosuClass gsClass = (IGosuClass)findMi.getOwnersType();
          IPropertyInfo findPi = gsClass.getTypeInfo().getProperty( gsClass, propertyName );

          IPropertyInfo pi = (IPropertyInfo)((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
          if( pi != null && propertyInfosEqual( pi, findPi ) )
          {
            IParsedElement parent = pe.getParent();
            if( bSetter )
            {
              if( parent instanceof IAssignmentStatement && ((IAssignmentStatement)parent).getIdentifier() == pe )
              {
                locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
              }
            }
            else
            {
              if( !(parent instanceof IAssignmentStatement) || ((IAssignmentStatement)parent).getIdentifier() != pe )
              {
                locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
              }
            }
          }
        }
        else
        {
          ISymbol symbol = ((IIdentifierExpression)pe).getSymbol();
          if( symbol instanceof IDynamicSymbol )
          {
            boolean bSetter = findMi.getParameters().length > 0;

            IGosuClass gsClass = (IGosuClass)findMi.getOwnersType();
            IPropertyInfo findPi = gsClass.getTypeInfo().getProperty( gsClass, propertyName );

            IType type = symbol.getScriptPart().getContainingType();
            if( type == findPi.getOwnersType() && symbol.getName().equals( findPi.getName() ) )
            {
              IParsedElement parent = pe.getParent();
              if( bSetter )
              {
                if( parent instanceof IAssignmentStatement && ((IAssignmentStatement)parent).getIdentifier() == pe )
                {
                  locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
                }
              }
              else
              {
                if( !(parent instanceof IAssignmentStatement) || ((IAssignmentStatement)parent).getIdentifier() != pe )
                {
                  locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
                }
              }
            }
          }
        }
      }
    }
    return locations;
  }

  private List<SearchLocation> findUsage( IParsedElement pe, IPropertyInfo findPi, List<SearchLocation> locations )
  {
    if( pe instanceof IMemberAccessExpression )
    {
      if( ((IMemberAccessExpression)pe).getType() instanceof INamespaceType )
      {
        return locations;
      }

      if( !findPi.getName().equals( ((IMemberAccessExpression)pe).getMemberName() ) )
      {
        return locations;
      }

      IPropertyInfo pi;
      try
      {
        pi = ((IMemberAccessExpression)pe).getPropertyInfo();
        if( pi != null && propertyInfosEqual( pi, findPi ) )
        {
          locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
        }
      }
      catch( Exception e )
      {
        return locations;
      }
    }
    else if( pe instanceof IIdentifierExpression )
    {
      if( pe instanceof IPropertyAccessIdentifier ||
          pe instanceof IPropertyAsMethodCallIdentifier )
      {
        IPropertyInfo pi = (IPropertyInfo)((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
        if( pi != null && propertyInfosEqual( pi, findPi ) )
        {
          locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
        }
      }
      else
      {
        ISymbol symbol = ((IIdentifierExpression)pe).getSymbol();
        if( symbol instanceof IDynamicSymbol )
        {
          IType type = symbol.getScriptPart().getContainingType();
          if( type == findPi.getOwnersType() && symbol.getName().equals( findPi.getName() ) )
          {
            locations = addSearchLocation( findNameToken( findPi.getDisplayName(), pe ), locations );
          }
        }
      }
    }
    return locations;
  }

  private boolean propertyInfosEqual( IPropertyInfo pi, IPropertyInfo findPi )
  {
    if( pi == findPi )
    {
      return true;
    }

    while( pi instanceof IPropertyInfoDelegate )
    {
      pi = ((IPropertyInfoDelegate)pi).getSource();
    }

    String name = pi.getName();
    return name != null && name.equals( findPi.getName() ) &&
           findPi.getOwnersType().isAssignableFrom( pi.getOwnersType() );
  }

  private IToken findNameToken( String name, IParsedElement pe )
  {
    for( IToken token: pe.getTokens() )
    {
      int tt = token.getType();
      if( (tt == ISourceCodeTokenizer.TT_WORD ||
           tt == ISourceCodeTokenizer.TT_KEYWORD) &&
          token.getStringValue().contains( name ) )
      {
        return token;
      }
    }
    throw new IllegalStateException();
  }

  private List<SearchLocation> findUsage( IParsedElement pe, LocalVarFeatureInfo findLocal, List<SearchLocation> locations )
  {
    if( pe instanceof IIdentifierExpression )
    {
      LocalVarFeatureInfo local = new LocalVarFeatureInfo( (IIdentifierExpression)pe );
      if( local.equals( findLocal ) )
      {
        locations = addSearchLocation( pe, locations );
      }
    }
    return locations;
  }

  private List<SearchLocation> findUsage( IParsedElement pe, IType type, List<SearchLocation> locations )
  {
    if( pe instanceof ITypeLiteralExpression )
    {

      IType t = ((ITypeLiteralExpression)pe).getType().getType();
      while( t.isArray() )
      {
        t = t.getComponentType();
      }
      if( t == type )
      {
        locations = addSearchLocation( pe, locations );
      }
    }
    else if( pe instanceof INewExpression )
    {
      ITypeLiteralExpression typeLiteral = ((INewExpression)pe).getTypeLiteral();
      if( typeLiteral == null )
      {
        IConstructorInfo ctor = ((INewExpression)pe).getConstructor();
        if( ctor != null && ctor.getType() == type )
        {
          locations = addSearchLocation( pe, locations );
        }
        else if( ((INewExpression)pe).getType() == type )
        {
          locations = addSearchLocation( pe, locations );
        }
      }
    }
    else if( pe instanceof IVarStatement )
    {
      IVarStatement varStmt = (IVarStatement)pe;
      if( varStmt.getTypeLiteral() == null )
      {
        IType varType = varStmt.getType();
        if( varType != null && referencesType( varType, type ) )
        {
          locations = addZeroLengthSearchLocation( pe, locations );
        }
      }
    }
    else if( includeMemberUsage() )
    {
      if( pe instanceof IMethodCallExpression )
      {
        IFunctionType functionType = ((IMethodCallExpression)pe).getFunctionType();
        if( functionType != null )
        {
          IType declaringType = functionType.getEnclosingType();
          if( declaringType != null && encloses( type, declaringType ) )
          {
            locations = addSearchLocation( pe, locations );
          }
        }
      }
      else if( pe instanceof IBeanMethodCallExpression )
      {
        IFunctionType functionType = ((IBeanMethodCallExpression)pe).getFunctionType();
        if( functionType != null )
        {
          IType declaringType = functionType.getEnclosingType();
          IExpression rootExpression = ((IBeanMethodCallExpression)pe).getRootExpression();
          if( rootExpression != null && rootExpression instanceof ITypeLiteralExpression )
          {
            IType typeLiteral = ((ITypeLiteralExpression)rootExpression).getType().getType();
            if( typeLiteral != type && encloses( type, declaringType ) )
            {
              locations = addSearchLocation( pe, locations );
            }
          }
          else if( declaringType != null && encloses( type, declaringType ) )
          {
            locations = addSearchLocation( pe, locations );
          }
        }
      }
      else if( pe instanceof IMemberAccessExpression )
      {
        if( ((IMemberAccessExpression)pe).getType() instanceof INamespaceType )
        {
          return locations;
        }

        IPropertyInfo pi;
        try
        {
          pi = ((IMemberAccessExpression)pe).getPropertyInfo();
        }
        catch( Exception e )
        {
          return locations;
        }

        if( pi != null )
        {
          locations = getSearchLocations( pe, type, locations, pi );
        }
      }
      else if( pe instanceof IIdentifierExpression )
      {
        if( pe instanceof IPropertyAccessIdentifier ||
            pe instanceof IPropertyAsMethodCallIdentifier )
        {
          IFeatureInfo pi = ((IDynamicPropertySymbol)((IIdentifierExpression)pe).getSymbol()).getPropertyInfo();
          if( pi != null )
          {
            locations = getSearchLocations( pe, type, locations, pi );
          }
        }
      }
    }
    return locations;
  }

  private List<SearchLocation> getSearchLocations( IParsedElement pe, IType type, List<SearchLocation> locations, IFeatureInfo pi )
  {
    IType declaringType = pi.getOwnersType();
    IExpression rootExpression = ((IMemberAccessExpression)pe).getRootExpression();
    if( rootExpression != null && rootExpression instanceof ITypeLiteralExpression )
    {
      IType typeLiteral = ((ITypeLiteralExpression)rootExpression).getType().getType();
      if( typeLiteral != type && encloses( type, declaringType ) )
      {
        locations = addSearchLocation( pe, locations );
      }
    }
    else if( declaringType != null && encloses( type, declaringType ) )
    {
      locations = addSearchLocation( pe, locations );
    }
    return locations;
  }

  private boolean referencesType( IType ref, IType type )
  {
    if( ref == type )
    {
      return true;
    }

    if( ref.isArray() )
    {
      return referencesType( ref.getComponentType(), type );
    }

    if( ref.isParameterizedType() )
    {
      for( IType param : ref.getTypeParameters() )
      {
        if( referencesType( param, type ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  private boolean encloses( IType type, IType inner )
  {
    return inner != null && (inner == type || encloses( type, inner.getEnclosingType() ));
  }

  private List<SearchLocation> addZeroLengthSearchLocation( IParsedElement elem, List<SearchLocation> locations )
  {
    IParseTree parseTree = elem.getLocation();
    SearchLocation loc = makeZeroLengthSearchLocation( parseTree );
    if( locations.isEmpty() )
    {
      locations = new ArrayList<>();
    }
    locations.add( loc );
    return locations;
  }

  private List<SearchLocation> addSearchLocation( IParsedElement elem, List<SearchLocation> locations )
  {
    IParseTree parseTree = elem.getLocation();
    SearchLocation loc = makeSearchLocation( parseTree );
    if( locations.isEmpty() )
    {
      locations = new ArrayList<>();
    }
    locations.add( loc );
    return locations;
  }

  private List<SearchLocation> addSearchLocation( IToken token, List<SearchLocation> locations )
  {
    SearchLocation loc = makeSearchLocation( token );
    if( locations.isEmpty() )
    {
      locations = new ArrayList<>();
    }
    locations.add( loc );
    return locations;
  }

  private SearchLocation makeZeroLengthSearchLocation( IParseTree parseTree )
  {
    SearchLocation loc = makeSearchLocation( parseTree );
    loc._iLength = 0;
    return loc;
  }
  public static SearchLocation makeSearchLocation( IParseTree parseTree )
  {
    SearchLocation loc = new SearchLocation();
    loc._iOffset = parseTree.getOffset();
    loc._iLength = parseTree.getLength();
    loc._iLineOffset = 0;
    loc._iLine = parseTree.getLineNum();
    loc._iColumn = parseTree.getColumn();
    return loc;
  }

  private SearchLocation makeSearchLocation( IToken token )
  {
    SearchLocation loc = new SearchLocation();
    loc._iOffset = token.getTokenStart();
    loc._iLength = token.getTokenEnd() - token.getTokenStart();
    loc._iLineOffset = 0;
    loc._iLine = token.getLine();
    loc._iColumn = token.getTokenColumn();
    return loc;
  }

  private SearchLocation makeSearchLocation( SearchElement target )
  {
    SearchLocation loc = new SearchLocation();
    loc._iOffset = target.getOffset();
    loc._iLength = target.getLength();
    loc._iLineOffset = 0;
    loc._iLine = target.getLine();
    loc._iColumn = target.getColumn();
    return loc;
  }

  private boolean maybeSearchForText( FileTree tree, SearchTree results )
  {
    if( !(_target instanceof ITypeInfo) )
    {
      return false;
    }

    IType type = _target.getSelectedFeatureInfo().getOwnersType();
    if( type instanceof IErrorType )
    {
      return false;
    }

    return new TextSearcher( type.getName(), true, true, false ).search( tree, results );
  }

  @DisableStringLiteralTemplates
  public void search( FileTree tree )
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    gosuPanel.showSearches( false );
    SearchPanel searchPanel = gosuPanel.showSearches( true );
    //searchPanel.setReplacePattern( (String)_cbReplace.getSelectedItem() );

    _results = new SearchTree( "<html><b>$count</b>&nbsp;usages&nbsp;of&nbsp;<b>'" + _target.getRootFeatureInfo().getName() + "'</b>&nbsp;in&nbsp;" + tree.getName(), NodeKind.Directory, SearchTree.empty() );
    searchPanel.add( _results );

    doSearch( tree );

    EventQueue.invokeLater( () -> selectFirstMatch( _results ) );
  }

  public void headlessSearch( FileTree tree )
  {
    _results = new SearchTree( _target.getRootFeatureInfo().getName() + " : " + tree.getName(), NodeKind.Directory, SearchTree.empty() );

    doSearch( tree );
  }

  private void doSearch( FileTree tree )
  {
    boolean[] bFinished = {false};
    ProgressFeedback.runWithProgress( "Searching...",
      progress -> {
          progress.setLength( tree.getTotalFiles() );
          searchTree( tree, _results, ft -> ft.getType() instanceof IGosuClass, progress );
          bFinished[0] = true;
        } );
    new ModalEventQueue( () -> !bFinished[0] ).run();
  }

  public List<SearchLocation> searchLocal()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    FileTree tree = FileTreeUtil.find( gosuPanel.getCurrentFile(), gosuPanel.getCurrentEditor().getParsedClass().getName() );
    IType type = tree.getType();
    SearchTree results = new SearchTree( "root", NodeKind.Directory, SearchTree.empty() );
    searchTree( tree, results, ft -> ft.getType() instanceof IGosuClass, null );
    List<SearchLocation> locations = findLocations( results, new ArrayList<>() );
    SearchElement target = _target.getTargetElement();
    if( target != null && getOuterMostEnclosingType( target.getEnclosingType() ) == getOuterMostEnclosingType( type ) )
    {
      locations.add( makeSearchLocation( target ) );
    }
    return locations;
  }

  private IType getOuterMostEnclosingType( IType innerClass )
  {
    IType outerMost = innerClass;
    while( outerMost != null && outerMost.getEnclosingType() != null )
    {
      outerMost = outerMost.getEnclosingType();
    }
    return outerMost;
  }

  private List<SearchLocation> findLocations( SearchTree tree, List<SearchLocation> locations )
  {
    if( tree == null )
    {
      return locations;
    }

    SearchTree.SearchTreeNode node = tree.getNode();
    if( node != null && node.getLocation() != null )
    {
      locations.add( node.getLocation() );
    }
    else
    {
      for( int i = 0; i < tree.getChildCount(); i++ )
      {
        findLocations( tree.getChildAt( i ), locations );
      }
    }

    return locations;
  }

//  private void addReplaceInfo( SearchPanel searchPanel )
//  {
//    if( _bReplace )
//    {
//      String text = (String)_cbReplace.getSelectedItem();
//      SearchTree results = new SearchTree( "<html>Replace occurrences with <b>'" + text + "'</b>", NodeKind.Info, SearchTree.empty() );
//      searchPanel.add( results );
//    }
//  }

  private void selectFirstMatch( SearchTree results )
  {
    if( results.getChildCount() == 0 )
    {
      results.select();
    }
    else
    {
      selectFirstMatch( results.getChildAt( 0 ) );
    }
  }

}

