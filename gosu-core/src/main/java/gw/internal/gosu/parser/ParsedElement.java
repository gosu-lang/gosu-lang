/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.AnnotationExpression;
import gw.internal.gosu.parser.expressions.Program;
import gw.internal.gosu.parser.statements.ClassFileStatement;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.NoOpStatement;
import gw.internal.gosu.parser.statements.UsesStatement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.IToken;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.util.GosuObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * ParsedElement is the root class for all non-terminal elements represented in
 * a parse tree i.e., all non-terminal expressions and statements derive either
 * directly or indirectly from this class.
 */
public abstract class ParsedElement implements IParsedElement
{
  private static final List<IParseTree> EMPTY_PARSETREE_LIST = Collections.emptyList();

  public static final String UNDEF_MODULE = "[undefined-module]";
  public static final String UNDEF_FUNCTION = "[undefined-function]";
  public static final String UNDEF_FILE = "undefined-module.gs";

  private ParseTree _location;
  private IParsedElement _parent;
  private int _iLineNum;
  private int _iColumn;
  private LikelyNullFields _lnf;
  private IGosuProgramInternal _gosuProgram;
  protected List<IToken> _tokens;

  private static class LikelyNullFields
  {
    private List<IParseIssue> _parseExceptions = Collections.emptyList();
    private List<IParseIssue> _parseWarnings = Collections.emptyList();
    private Map<String, IParsedElementWithAtLeastOneDeclaration> _declaringStatements;
    private boolean _bSynthetic;
  }

  ParsedElement()
  {
    _tokens = new ArrayList<IToken>( 2 );
  }

  public IGosuProgramInternal getGosuProgram()
  {
    return _gosuProgram;
  }
  public void setGosuProgram( IGosuProgramInternal gosuProgram )
  {
    _gosuProgram = gosuProgram;
    
  }

  public void addExceptionsFrom( IParsedElement elem )
  {
    List<IParseIssue> exceptions = elem.getParseExceptions();
    if( !exceptions.isEmpty() )
    {
      maybeInitLikelyNullFields();

      if( _lnf._parseExceptions == Collections.EMPTY_LIST )
      {
        _lnf._parseExceptions = new ArrayList<IParseIssue>( exceptions.size() );
      }
      _lnf._parseExceptions.addAll( exceptions );
    }

    List<IParseIssue> warnings = elem.getParseWarnings();
    if( !warnings.isEmpty() )
    {
      maybeInitLikelyNullFields();

      if( _lnf._parseWarnings == Collections.EMPTY_LIST )
      {
        _lnf._parseWarnings = new ArrayList<IParseIssue>( warnings.size() );
      }
      _lnf._parseWarnings.addAll( warnings );
    }
  }

  private void maybeInitLikelyNullFields()
  {
    if( _lnf == null )
    {
      _lnf = new LikelyNullFields();
    }
  }

  /**
   * @return The location of this parsed element within the source.
   */
  public ParseTree getLocation()
  {
    return _location;
  }

  /**
   * Specifiy the location of this parsed element within the source.
   *
   * @param location The location of this parsed element within the source.
   */
  public void setLocation( IParseTree location )
  {
    _location = (ParseTree)location;
  }

  public ParseTree initLocation( int offset, int length, int lineNumber, int iColumn, IScriptPartId scriptPart )
  {
    _iLineNum = lineNumber;
    _iColumn = iColumn;
    if( _location == null )
    {
      _location = new ParseTree( this, offset, length, scriptPart );
    }
    else
    {
      _location.initLocation( this, offset, length );
    }

    return _location;
  }

  @Override
  public void visit( Consumer<IParsedElement> visitor )
  {
    ParseTree location = getLocation();
    if( location != null )
    {
      int count = location.getChildCount();
      if( count > 0 )
      {
        for( int i = 0; i < count; i++ )
        {
          ParseTree child = location.getChild( i );
          ParsedElement pe = child.getParsedElement();
          if( pe != null )
          {
            pe.visit( visitor );
          }
        }
      }
    }
    visitor.accept( this );
  }

  public void initEmptyParseTree()
  {
    initLocation( -1, -1, -1, -1, null );
  }

  public boolean hasParseIssues()
  {
    return hasParseExceptions() || hasParseWarnings();
  }

  public List<IParseIssue> getParseIssues()
  {
    List<IParseIssue> issues = new ArrayList<IParseIssue>();
    getParseExceptions( issues );
    getParseWarnings( issues );
    if( issues.isEmpty() )
    {
      issues = Collections.emptyList();
    }
    else
    {
      ((ArrayList)issues).trimToSize();
    }
    return issues;
  }

  @Override
  public List<IParseIssue> getImmediateParseIssues()
  {
    ArrayList<IParseIssue> issues = null;
    if( _lnf != null )
    {
      if( _lnf._parseExceptions != null && !_lnf._parseExceptions.isEmpty() )
      {
        if( issues == null )
        {
          issues = new ArrayList<IParseIssue>();
        }
        issues.addAll( _lnf._parseExceptions );
      }
      if( _lnf._parseWarnings != null && !_lnf._parseWarnings.isEmpty() )
      {
        if( issues == null )
        {
          issues = new ArrayList<IParseIssue>();
        }
        for( IParseIssue exc : _lnf._parseWarnings )
        {
          if( !isSuppressed( exc ) )
          {
            issues.add( exc );
          }
        }
      }
    }
    return issues != null ? issues : Collections.<IParseIssue>emptyList();
  }

  public boolean hasParseExceptions()
  {
    if( _lnf != null && !_lnf._parseExceptions.isEmpty() )
    {
      return true;
    }

    ParseTree location = getLocation();
    List<IParseTree> children = location != null ? location.getChildren() : null;
    if (children != null) {
      //!! Don't use iterator here, it throws ConcurrentModificationExceptions if children is modified concurrently (and it allocates memory!)
      for( int i = 0; i < children.size(); i++ )
      {
        IParseTree child;
        try
        {
          child = children.get( i );
        }
        catch( Throwable t )
        {
          // The child list may be accessed concurrently, but we don't want to
          // incur the overhead of locking or copying and since we don't care if
          // the current list is overrun, we can safely return false (I think).
          return false;
        }
        if( child != null )
        {
          IParsedElement pe = child.getParsedElement();
          if( pe != null && pe.hasParseExceptions() )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean hasParseException( ResourceKey errKey )
  {
    for( IParseIssue err : getParseExceptions() )
    {
      if( err.getMessageKey() == errKey )
      {
        return true;
      }
    }
    return false;
  }

  public IParseIssue getImmediateParseIssue( ResourceKey errKey )
  {
    for( IParseIssue err : getImmediateParseIssues() )
    {
      if( err.getMessageKey() == errKey )
      {
        return err;
      }
    }
    return null;
  }

  public boolean hasImmediateParseIssue( ResourceKey errKey )
  {
    return getImmediateParseIssue( errKey ) != null;
  }

  public boolean hasParseWarning( ResourceKey errKey )
  {
    for (int i = 0; i < getParseWarnings().size(); i++) {
      IParseIssue warning = getParseWarnings().get( i );
      if (warning.getMessageKey() == errKey) {
        return true;
      }
    }
    return false;
  }

  public void addParseWarnings( List<IParseIssue> parseWarnings )
  {
    for (int i = 0; i < parseWarnings.size(); i++) {
      IParseIssue w = parseWarnings.get( i );
      addParseWarning(w);
    }
  }

  public void addParseExceptions( List<IParseIssue> parseExceptions )
  {
    for( IParseIssue w : parseExceptions )
    {
      addParseException( w );
    }
  }

  public void addParseIssues( List<IParseIssue> parseIssues )
  {
    for( IParseIssue p : parseIssues )
    {
      if( p instanceof ParseException )
      {
        addParseException( p );
      }
      else if( p instanceof ParseWarning )
      {
        addParseWarning( p );
      }
    }
  }

  public List<IParseIssue> getParseExceptions()
  {
    List<IParseIssue> list = new ArrayList<IParseIssue>();
    getParseExceptions( list );
    return list.isEmpty() ? Collections.<IParseIssue>emptyList() : list;
  }

  private void getParseExceptions( List<IParseIssue> allParseExceptions )
  {
    if( _lnf != null )
    {
      allParseExceptions.addAll( _lnf._parseExceptions );
    }

    ParseTree location = getLocation();
    List<IParseTree> children = location == null ? null : location.getChildren();
    if( children != null )
    {
      for( int i = 0; i < children.size(); i++ )
      {
        IParseTree child = children.get( i );
        IParsedElement pe = child.getParsedElement();
        if( pe != null )
        {
          ((ParsedElement)pe).getParseExceptions( allParseExceptions );
        }
      }
    }
  }

  public void addParseException( ResourceKey msgKey, Object... args )
  {
    String src = getSource();
    addParseException( new ParseException( new StandardParserState( this, src, false ), msgKey, args ) );
  }

  public void addParseException( IFullParserState parserState, ResourceKey msgKey, Object... args )
  {
    String src = getSource();
    addParseException( new ParseException( parserState, msgKey, args ) );
  }

  /**
   * Removes the specified parse exception or removes them all if the specified
   * exception is null.
   */
  public ParseException removeParseException( ResourceKey keyToRemove )
  {
    if( _lnf != null )
    {
      return (ParseException)removeParseIssue( keyToRemove, _lnf._parseExceptions );
    }
    return null;
  }

  public ParseWarning removeParseWarning( ResourceKey keyToRemove )
  {
    if( _lnf != null )
    {
      return (ParseWarning)removeParseIssue( keyToRemove, _lnf._parseWarnings );
    }
    return null;
  }

  public void removeParseWarningRecursively( ResourceKey keyToRemove )
  {
    //noinspection StatementWithEmptyBody,ThrowableResultOfMethodCallIgnored
    while( _lnf != null && removeParseWarning( keyToRemove ) != null );

    ParseTree location = getLocation();
    List<IParseTree> children = location == null ? null : location.getChildren();
    if( children != null )
    {
      for( int i = 0; i < children.size(); i++ )
      {
        IParseTree child = children.get( i );
        IParsedElement pe = child.getParsedElement();
        if( pe != null )
        {
          ((ParsedElement)pe).removeParseWarningRecursively( keyToRemove );
        }
      }
    }
  }

  private <E extends IParseIssue> E removeParseIssue( ResourceKey keyToRemove, List<E> issues )
  {
    E pe = null;
    if( _lnf != null && !issues.isEmpty() )
    {
      for( java.util.Iterator it = issues.iterator(); it.hasNext(); )
      {
        E parseIssue = (E)it.next();
        if( keyToRemove == null || keyToRemove.equals( parseIssue.getMessageKey() ) )
        {
          pe = parseIssue;
          it.remove();
        }
      }
    }
    return pe;
  }
  
  private String getSource()
  {
    String src = null;
    IParsedElement element = this;
    while( element != null )
    {
      if( element instanceof ClassStatement )
      {
        src = ((ClassStatement)element).getGosuClass().getSource();
        break;
      }
      else if( element instanceof Program )
      {
        src = element.toString();
        break;
      }
      element = element.getParent();
    }
    return src;
  }

  public IGosuClass getGosuClass()
  {
    IParsedElement parent = getParent();
    if( parent != null )
    {
      return parent.getGosuClass();
    }
    return null;
  }

  public void addParseWarning( ResourceKey msgKey, Object... args )
  {
    String src = getSource();
    addParseWarning( new ParseWarning( new StandardParserState( this, src, false ), msgKey, args ) );
  }

  public void addParseException( IParseIssue pe )
  {
    if( hasParseIssue( pe ) )
    {
      return;
    }

    maybeInitLikelyNullFields();

    if( _lnf._parseExceptions == Collections.EMPTY_LIST )
    {
      _lnf._parseExceptions = new ArrayList<IParseIssue>( 1 );
    }
    _lnf._parseExceptions.add( pe );
    ((ArrayList)_lnf._parseExceptions).trimToSize();
    ((ParseException)pe).setSource( this );
  }

  public void clearParseExceptions()
  {
    if( _lnf != null )
    {
      _lnf._parseExceptions = Collections.emptyList();
    }
    ParseTree location = getLocation();
    List<IParseTree> children = location == null ? EMPTY_PARSETREE_LIST : location.getChildren();
    if( !children.isEmpty() )
    {
      for( IParseTree child : children )
      {
        child.getParsedElement().clearParseExceptions();
      }
    }
  }

  public void clearParseWarnings()
  {
    if( _lnf != null )
    {
      _lnf._parseWarnings = Collections.emptyList();
    }
    ParseTree location = getLocation();
    List<IParseTree> children = location == null ? EMPTY_PARSETREE_LIST : location.getChildren();
    if( !children.isEmpty() )
    {
      for( IParseTree child : children )
      {
        child.getParsedElement().clearParseWarnings();
      }
    }
  }

  public boolean hasImmediateParseWarnings()
  {
    return _lnf != null && !_lnf._parseWarnings.isEmpty();
  }

  public boolean hasImmediateParseWarning( ResourceKey errKey )
  {
    if( _lnf == null || _lnf._parseWarnings.isEmpty() )
    {
      return false;
    }

    for( IParseIssue w : _lnf._parseWarnings )
    {
      if( w.getMessageKey().equals( errKey ) )
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasParseWarnings()
  {
    if( _lnf != null && !_lnf._parseWarnings.isEmpty() )
    {
      return true;
    }

    ParseTree location = getLocation();
    List<IParseTree> children = location != null ? location.getChildren() : EMPTY_PARSETREE_LIST;
    if( !children.isEmpty() )
    {
      for( IParseTree child : children )
      {
        IParsedElement pe = child.getParsedElement();
        if( pe != null && pe.hasParseWarnings() )
        {
          return true;
        }
      }
    }
    return false;
  }

  public List<IParseIssue> getParseWarnings()
  {
    List<IParseIssue> list = new ArrayList<IParseIssue>();
    getParseWarnings( list );
    return list.isEmpty() ? Collections.<IParseIssue>emptyList() : list;
  }

  private void getParseWarnings( List<IParseIssue> allWarnings )
  {
    if( _lnf != null )
    {
      for( IParseIssue exc : _lnf._parseWarnings )
      {
        if( !isSuppressed( exc ) )
        {
          allWarnings.add( exc );
        }
      }
    }

    ParseTree location = getLocation();
    List<IParseTree> children = location == null ? EMPTY_PARSETREE_LIST : location.getChildren();
    if( !children.isEmpty() )
    {
      for (int i = 0; i < children.size(); i++) {
        IParseTree child = children.get(i);
        IParsedElement pe = child.getParsedElement();
        if (pe != null) {
          ((ParsedElement) pe).getParseWarnings(allWarnings);
        }
      }
    }
  }

  public void addParseWarning( IParseIssue warning )
  {
    if( hasParseIssue( warning ) )
    {
      return;
    }

    if( getLocation() == null ||
        getLocation().getEnclosingType() == null ||
        CommonServices.getEntityAccess().shouldAddWarning( getLocation().getEnclosingType(), warning ) )
    {
      maybeInitLikelyNullFields();

      if( _lnf._parseWarnings == Collections.<IParseIssue>emptyList() )
      {
        _lnf._parseWarnings = new ArrayList<IParseIssue>( 1 );
      }
      _lnf._parseWarnings.add( warning );
      ((ArrayList)_lnf._parseWarnings).trimToSize();
      ((ParseIssue)warning).setSource( this );
    }
  }

  public boolean hasParseIssue( IParseIssue pi )
  {
    if( _lnf == null )
    {
      return false;
    }

    for( IParseIssue pw : getParseWarnings() )
    {
      if( GosuObjectUtil.equals( pw.getTokenStart(), pi.getTokenStart() ) &&
          pw.getMessageKey() == pi.getMessageKey() &&
          GosuObjectUtil.equals( pw.getConsoleMessage(), pi.getConsoleMessage() ) )
      {
        return true;
      }
    }

    for( IParseIssue pe : _lnf._parseExceptions )
    {
      if( pe.getTokenStart() != null && pe.getTokenStart().equals( pi.getTokenStart() ) &&
          pe.getMessageKey() == pi.getMessageKey() &&
          GosuObjectUtil.equals( pe.getPlainMessage(), pi.getPlainMessage() ) )
      {
        return true;
      }
    }

    return false;
  }

  public boolean isSuppressed( IParseIssue issue )
  {
    return issue instanceof IWarningSuppressor &&
           isSuppressed( (IWarningSuppressor)issue );
  }
  public boolean isSuppressed( IWarningSuppressor suppressor )
  {
    IModule mod = getGosuClass() == null ? null : getModule();
    if( mod != null ) {
      TypeSystem.pushModule( mod );
    }
    try
    {
      for( IGosuAnnotation anno: getAnnotations() )
      {
        if( anno.getType() == TypeSystem.get( SuppressWarnings.class ) )
        {
          IExpression annoExpr = anno.getExpression();
          if( annoExpr instanceof AnnotationExpression )
          {
            if( ((AnnotationExpression)annoExpr).getArgs() != null )
            {
              for( Expression expr : ((AnnotationExpression)annoExpr).getArgs() )
              {
                Object value = expr.evaluate();
                if( value instanceof String )
                {
                  if( suppressor.isSuppressed( (String)value ) )
                  {
                    return true;
                  }
                }
                else if( value instanceof Object[] )
                {
                  for( Object o: (Object[])value )
                  {
                    if( suppressor.isSuppressed( (String)o ) )
                    {
                      return true;
                    }
                  }
                }
                else if( value instanceof List )
                {
                  for( Object o: (List)value )
                  {
                    if( suppressor.isSuppressed( (String)o ) )
                    {
                      return true;
                    }
                  }
                }
              }
            }
          }
        }
      }
      ParsedElement parent = (ParsedElement)getParent();
      return parent != null && parent.isSuppressed( suppressor );
    }
    finally
    {
      if( mod != null )
      {
        TypeSystem.popModule( mod );
      }
    }
  }

  public List<IGosuAnnotation> getAnnotations()
  {
    return Collections.emptyList();
  }

  public boolean isCompileTimeConstant()
  {
    return false;
  }

  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------
  // -- Helper Methods --

  /**
   * Find all the parsed elements of a given type contained within this parsed
   * element.
   *
   * @param parsedElementType The type of parsed element to find.
   * @param listResults       A list of all the contained parsed elements matching the
   *                          specified type. Can be null if you are only interested in whether or not
   *                          parsedElementType exists in this element.
   *
   * @return True iff one or more parseElementType are found.
   */
  @SuppressWarnings("unchecked" )
  public <E extends IParsedElement> boolean getContainedParsedElementsByType( Class<E> parsedElementType, List<E> listResults )
  {
    return getContainedParsedElementsByTypes( (List<IParsedElement>)listResults, parsedElementType );
  }

  public boolean getContainedParsedElementsByTypes( List<IParsedElement> listResults, Class<? extends IParsedElement>... parsedElementTypes )
  {
    //noinspection unchecked
    return getContainedParsedElementsByTypesWithIgnoreSet( listResults, Collections.EMPTY_SET, parsedElementTypes );
  }

  public boolean getContainedParsedElementsByTypesWithIgnoreSet( List<IParsedElement> listResults,
                                                                 Set<Class<? extends IParsedElement>> ignoreSet,
                                                                 Class<? extends IParsedElement>... parsedElementTypes )
  {
    boolean isInstance = false;
    for( Class<? extends IParsedElement> parsedElementType : parsedElementTypes )
    {
      if( parsedElementType.isInstance( this ) )
      {
        isInstance = true;
        break;
      }
    }
    if( isInstance )
    {
      if( listResults != null )
      {
        listResults.add( this );
      }
      else
      {
        return true;
      }
    }
    
    boolean bIgnore = false;
    for( Class ignore : ignoreSet )
    {
      if( ignore.isInstance( this ) )
      {
    	bIgnore = true;
    	break;
      }
    }
    if( !bIgnore )
    {
      ParseTree location = getLocation();
      if (location != null) {
        List children = location.getChildren();
        if( !children.isEmpty() )
        {
          for( int i = 0; i < children.size(); i++ )
          {
            IParseTree child = (IParseTree)children.get( i );
            IParsedElement parsedElement = child.getParsedElement();
            if( parsedElement.getContainedParsedElementsByTypesWithIgnoreSet( listResults, ignoreSet, parsedElementTypes ) )
            {
              if( listResults == null )
              {
                return true;
              }
            }
          }
        }
      }
    }
    return listResults != null && !listResults.isEmpty();
  }

  public final Integer makeInteger( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    return CommonServices.getCoercionManager().makeIntegerFrom( obj );
  }

  public static Long makeLong( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    return CommonServices.getCoercionManager().makeLongFrom( obj );
  }

  /**
   * Just like makeDouble(), but creates a double primitive value instead of a
   * Double object.  Much more efficient if you don't need the object.
   *
   * @param obj Any double convertible object
   *
   * @return The double primitive value.
   */
  public static double makeDoubleValue( Object obj )
  {
    if( obj == null )
    {
      return Double.NaN;
    }

    return CommonServices.getCoercionManager().makePrimitiveDoubleFrom( obj );
  }

  /**
   * Just like makeFloat(), but creates a float primitive value instead of a
   * Float object.  Much more efficient if you don't need the object.
   *
   * @param obj Any float convertible object
   *
   * @return The float primitive value.
   */
  public static float makeFloatValue( Object obj )
  {
    if( obj == null )
    {
      return Float.NaN;
    }

    return CommonServices.getCoercionManager().makePrimitiveFloatFrom( obj );
  }

  public void compactParseTree() {
    if( _location != null )
    {
      _location.compactParseTree();
    }
  }

  public void clearParseTreeInformation()
  {
    if (shouldClearParseInfo()) {
      TypeSystem.lock();
      try
      {
        if( _location != null )
        {
          IParseTree loc = _location;
          IParseTree parent = loc.getParent();
          _location.clearParseTreeInformation();
          if( parent != null )
          {
            parent.removeChild(loc);
          }
        }
        _location = null;
        if( _lnf != null )
        {
          _lnf._declaringStatements = null;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  public IParsedElement getParent()
  {
    return _parent;
  }

  public void setParent( IParsedElement parent )
  {
    _parent = parent;
  }

  public int getLineNum()
  {
    return _iLineNum;
  }
  public void adjustLineNum( int offset )
  {
    _iLineNum += offset;
  }
  public void setLineNum( int iLineNum )
  {
    _iLineNum = iLineNum;
  }

  public int getColumn()
  {
    return _iColumn;
  }

  public void adjustColumn(int offset) {
    _iColumn += offset;
  }

  public String getFunctionName()
  {
    return getParent() == null ? UNDEF_FUNCTION : getParent().getFunctionName();
  }

  @Override
  public boolean isSynthetic()
  {
    return _lnf != null && _lnf._bSynthetic;
  }
  public void setSynthetic( boolean bSynthetic )
  {
    maybeInitLikelyNullFields();
    _lnf._bSynthetic = bSynthetic;
  }

  public IModule getModule() {
    IParsedElement parent = getParent();
    return parent == null ? null : parent.getModule();
  }
  
  public static IFeatureInfo getEnclosingFeatureInfo( Stack<IFeatureInfo> enclosingFeatureInfos )
  {
    if( enclosingFeatureInfos.empty() )
    {
      return null;
    }
    else
    {
      return enclosingFeatureInfos.peek();
    }
  }

  public static ITypeInfo getQualifyingEnclosingTypeInfo( Stack<IFeatureInfo> enclosingFeatureInfos )
  {
    if( enclosingFeatureInfos.empty() )
    {
      return null;
    }
    else
    {
      return (ITypeInfo)enclosingFeatureInfos.firstElement();
    }
  }

  public int findLineNumberOfDeclaration( String identifierName )
  {
    IParsedElementWithAtLeastOneDeclaration statement = findDeclaringStatement( this, identifierName );
    return statement.getLineNum();
  }

  public IParsedElementWithAtLeastOneDeclaration findDeclaringStatement( IParsedElement element, String identifierName )
  {
    IParsedElementWithAtLeastOneDeclaration declaringStatement;

    declaringStatement = _lnf == null || _lnf._declaringStatements == null
                          ? null
                          : _lnf._declaringStatements.get( identifierName );

    if( declaringStatement == null )
    {
      // More likely to be self
      declaringStatement = checkIfDeclaringStatement( element, identifierName );
      if( declaringStatement == null )
      {
        // Else check the children
        declaringStatement = findDeclaringStatementInChildren( element, identifierName );
        if( declaringStatement == null )
        {
          // if not found yet, go to parent
          IParsedElement parent = element.getParent();
          if( parent != null )
          {
            declaringStatement = findDeclaringStatement( parent, identifierName );
          }
          else
          {
            // We got to the root and couldn't find the declaring statement
            declaringStatement = null;
          }
        }
      }
      if( element == this &&
          declaringStatement != null )
      {
        maybeInitLikelyNullFields();
        _lnf._declaringStatements = new HashMap<String, IParsedElementWithAtLeastOneDeclaration>( 0 );
        _lnf._declaringStatements.put( identifierName, declaringStatement );
      }
    }
    return declaringStatement;
  }

  private static IParsedElementWithAtLeastOneDeclaration findDeclaringStatementInChildren( IParsedElement element, String identifierName )
  {
    IParsedElementWithAtLeastOneDeclaration declaringStatement;
    if( element.getLocation() == null )
    {
      return null;
    }
    List<IParseTree> children = element.getLocation().getChildren();
    for( IParseTree child : children )
    {
      IParsedElement parsedElement = child.getParsedElement();
      declaringStatement = checkIfDeclaringStatement( parsedElement, identifierName );
      if( declaringStatement != null )
      {
        return declaringStatement;
      }
      else if(( parsedElement instanceof ClassStatement ) || ( parsedElement instanceof ClassFileStatement ))
      {
        // If it's a class statement, check its children. Special case for inner classes.
        declaringStatement = findDeclaringStatementInChildren( parsedElement, identifierName );
        if( declaringStatement != null )
        {
          return declaringStatement;
        }
      }
    }
    return null;
  }

  private static IParsedElementWithAtLeastOneDeclaration checkIfDeclaringStatement( IParsedElement parsedElement, String identifierName )
  {
    if( parsedElement instanceof IParsedElementWithAtLeastOneDeclaration )
    {
      IParsedElementWithAtLeastOneDeclaration declarativeStatement = (IParsedElementWithAtLeastOneDeclaration)parsedElement;
      if( declarativeStatement.declares( identifierName ) )
      {
        return declarativeStatement;
      }
    }
    return null;
  }

  public IParsedElement findRootParsedElement()
  {
    IParsedElement parent = getParent();
    if( parent == null )
    {
      return this;
    }
    else
    {
      return parent.findRootParsedElement();
    }
  }

  /**
   * @param parsedElementClasses List of statement types to find
   *
   * @return The nearest ancestor statement that is any one of the given types. null if this element does not have
   *         an ancestor of any of the given types
   */
  public IParsedElement findAncestorParsedElementByType( Class... parsedElementClasses )
  {
    IParsedElement parent = getParent();
    while( (parent != null) && !(elementIsOneOfType( parent, parsedElementClasses )) )
    {
      parent = parent.getParent();
    }
    if( parent != null )
    {
      return parent;
    }
    else
    {
      return null;
    }
  }

  private static boolean elementIsOneOfType( IParsedElement element, Class[] parsedElementClasses )
  {
    for( Class statementClass : parsedElementClasses )
    {
      if( statementClass.isAssignableFrom( element.getClass() ) )
      {
        return true;
      }
    }
    return false;
  }

  public boolean shouldClearParseInfo() {
    return true;
  }

  public void assignTokens( List<Token> tokens )
  {
    // Note we don't sort the children because we need to let inner classes of
    // programs get first crack at consuming tokens so NoOpStatements inside
    // the synthetic evaluate method don't bogart them.
    List<IParseTree> children = getLocation().getChildren(); //getLocation().getChildrenSorted( getLocation() );
    for( IParseTree child : children )
    {
      ((ParsedElement)child.getParsedElement()).assignTokens( tokens );
    }
    assignTokensToJustMe( tokens );
  }

  protected void addToken( IToken token, IParseTree after )
  {
    token.setAfter( after );
    if( !containsToken( _tokens, token ) )
    {
      _tokens.add( token );
    }
  }

  public List<IToken> getTokens()
  {
    return _tokens;
  }

  private void assignTokensToJustMe( List<Token> tokens )
  {
    IParseTree parseTree = getLocation();
    int iStartOffset = -1;
    int iEndOffset = -1;
    IParseTree after = null;
    //int a = 0;
    int iTreeOffset = parseTree.getOffset();
    int iTreeEnd = parseTree.getExtent() + 1;
    boolean bZeroLengthTree = parseTree.getLength() == 0;
    int iStartIndex = binarySearchForFirstToken( tokens, iTreeOffset, iTreeEnd, bZeroLengthTree );
    //int iStartIndex = 0;
    if( iStartIndex < 0 ) {
      return;
    }
    int i;
    for( i = iStartIndex; i < tokens.size(); i++ )
    {
      IToken token = tokens.get( i );
      int iTokenStart = token.getTokenStart();
      int iTokenEnd = token.getTokenEnd();
      if( iTokenStart >= iTreeOffset && iTokenEnd <= iTreeEnd )
      {
        if( iStartOffset < 0 )
        {
          iStartOffset = iTokenStart;
        }
        iEndOffset = iTokenEnd;

        if( this instanceof NoOpStatement && !isDescendent( after ) )
        {
          break;
        }

        tokens.remove( i-- );
        if( !(token instanceof PositionToken) )
        {
          //a++;
          addToken( token, after );
        }
        else
        {
          after = ((PositionToken)token).getPos();
        }
      }
      else if( bZeroLengthTree &&
               iTreeOffset >= iTokenStart && iTreeOffset < iTokenEnd )
      {
        tokens.add( i, new PositionToken( parseTree, iTreeOffset, iTreeOffset ) );
        break;
      }
      else if( iStartOffset >= 0 && !tokens.isEmpty() )
      {
        tokens.add( i, new PositionToken( parseTree, iStartOffset, iEndOffset ) );
        break;
      }
    }
    //System.out.println( "#" + (i-iStartIndex) + ", " + a );
  }

  private int binarySearchForFirstToken( List<Token> tokens, int iTreeOffset, int iTreeEnd, boolean bZeroLengthTree ) {
    int iStart = 0;
    int iEnd = tokens.size() - 1;
    int iIndex;
    while( iStart <= iEnd ) {
      iIndex = (iStart + iEnd) / 2;
      IToken token = tokens.get( iIndex );
      int iTokenStart = token.getTokenStart();
      int iTokenEnd = token.getTokenEnd();
      if( !bZeroLengthTree && iTokenStart >= iTreeOffset && iTokenEnd <= iTreeEnd ) {
        // Found a token for the tree, now backtrack to the first token for the tree
        int iSaveIndex = iIndex;
        //int times = 0;
        if( iIndex > 0 ) {
          for( iIndex-=1;
               iIndex >= 0 && ((token = tokens.get( iIndex )) != null) && token.getTokenStart() >= iTreeOffset && token.getTokenEnd() <= iTreeEnd;
               iIndex-- ) {
            iSaveIndex = iIndex;
            //times++;
          }
        }
        //System.out.println( times );
        return iSaveIndex;
      }
      else if( bZeroLengthTree && iTreeOffset >= iTokenStart && iTreeOffset < iTokenEnd ) {
        return iIndex;
      }
      else if( iTokenStart < iTreeOffset ) {
        iStart = iIndex + 1;
      }
      else {
        iEnd = iIndex - 1;
      }
    }
    return -1;
  }

  private boolean containsToken( List<IToken> tokens, IToken target ) {
    int iStart = 0;
    int iEnd = tokens.size() - 1;
    int iOffset = target.getTokenStart();
    int iIndex;
    while( iStart <= iEnd ) {
      iIndex = (iStart + iEnd) / 2;
      IToken token = tokens.get( iIndex );
      if( token == target ) {
        return true;
      }
      else if( token.getTokenStart() < iOffset ) {
        iStart = iIndex + 1;
      }
      else {
        iEnd = iIndex - 1;
      }
    }
    return false;
  }

  private boolean isDescendent( IParseTree after )
  {
    if( after == null )
    {
      return true;
    }
    if( after.getParsedElement() == this )
    {
      return true;
    }
    if( after.getParent() == null )
    {
      return false;
    }
    return isDescendent( after.getParent() );
  }
}
