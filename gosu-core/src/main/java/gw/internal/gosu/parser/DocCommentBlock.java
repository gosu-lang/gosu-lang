/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;


import gw.lang.Param;
import gw.lang.Returns;
import gw.lang.Throws;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.util.GosuStringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocCommentBlock
{
  private List<DocAnnotationData> _annotationData = null;
  private StringBuilder _desc;
  private ITypeUsesMap _typeUsesMap;
  private ICompilableType _ownersType;
  private String _rawComment;
  private boolean computeAnnotationData = false;


  public void setRawComment(String rawComment) {
    _rawComment = rawComment;
    computeAnnotationData = true;
  }

  private void addLine( String line )
  {
    String strippedLine = stripLeadingCommentAndWhiteSpace( line );
    DocAnnotationData annotationData = maybeStartDocAnnotation( strippedLine );
    if( annotationData != null )
    {
      if( _annotationData == null )
      {
        _annotationData = new ArrayList<DocAnnotationData>();
      }
      _annotationData.add( annotationData );
    }
    else if( _annotationData == null )
    {
      if( _desc == null )
      {
        _desc = new StringBuilder();
        _desc.append( strippedLine );
      }
      else
      {
        _desc.append( "\n" );
        _desc.append( strippedLine );
      }
    }
    else
    {
      _annotationData.get( _annotationData.size() - 1 ).getArg().append("\n").append( strippedLine );
    }
  }

  public String getDescription()
  {
    if(computeAnnotationData)
    {
      computeAnnotationData();
      computeAnnotationData = false;
    }
    return GosuStringUtil.strip( _desc == null ? "" : _desc.toString() );
  }

  public List<IGosuAnnotation> getAnnotations()
  {
    if(computeAnnotationData)
    {
      computeAnnotationData();
      computeAnnotationData = false;
    }
    if( _annotationData == null )
    {
      return Collections.emptyList();
    }
    ArrayList<IGosuAnnotation> lst = new ArrayList<IGosuAnnotation>( _annotationData.size() );
    for( DocAnnotationData annotation : _annotationData )
    {
      IGosuAnnotation gosuAnnotation = annotation.makeAnnotation( _ownersType, _typeUsesMap );
      if( gosuAnnotation != null )
      {
        lst.add( gosuAnnotation );
      }
    }
    return lst;
  }

  private void computeAnnotationData() {
    final String[] lines = _rawComment.substring( 2, _rawComment.length()-2 ).split( "\n" );
    for(String line : lines)
    {
      addLine( line );
    }
  }

  private DocAnnotationData maybeStartDocAnnotation( String line )
  {
    Class annotationClass = null;
    // from http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/javadoc.html#javadoctags
    if( line.startsWith( "@deprecated" ) )
    {
      annotationClass = gw.lang.Deprecated.class;
    }
    else if( line.startsWith( "@exception" ) )
    {
      annotationClass = Throws.class;
    }
    else if( line.startsWith( "@param" ) )
    {
      annotationClass = Param.class;
    }
    else if( line.startsWith( "@return" ) )
    {
      annotationClass = Returns.class;
    }
    else if( line.startsWith( "@throws" ) )
    {
      annotationClass = Throws.class;
    }

    if( annotationClass != null )
    {
      int i = line.indexOf( " " );
      return new DocAnnotationData( annotationClass, i == -1 ? "" : line.substring( i ) );
    }
    else
    {
      return null;
    }
  }

  private String stripLeadingCommentAndWhiteSpace( String line )
  {
    String s = "";
    for( int i = 0; i < line.length(); i++ )
    {
      if( !isBlankOrCommentChar( line.charAt( i ) ) )
      {
        s = line.substring( i );
        break;
      }
    }
    return s.trim();
  }

  private boolean isBlankOrCommentChar( char c )
  {
    return c == ' ' || c == '\t' || c == '*';
  }

  public void setOwnersTypes( ICompilableType ownersType )
  {
    _ownersType = ownersType;
    _typeUsesMap = _ownersType == null ? null : _ownersType.getTypeUsesMap();
  }

  private static class DocAnnotationData
  {
    private Class _class;
    private StringBuilder _arg;

    public StringBuilder getArg()
    {
      return _arg;
    }

    DocAnnotationData( Class type, String arg )
    {
      _class = type;
      _arg = new StringBuilder( arg );
    }

    public IGosuAnnotation makeAnnotation( ICompilableType ownersType, ITypeUsesMap typeUses )
    {
      String[] args = makeArgs();
      if( _class == Throws.class )
      {
        try
        {
          IType iType = TypeSystem.getByRelativeName( args[0], typeUses );
          args[0] = iType.getName();
        }
        catch( ClassNotFoundException e )
        {
          return null;
        }
      }
      return new GosuDocAnnotation( ownersType, TypeSystem.get( _class ), args );
    }

    private String[] makeArgs()
    {
      String arg = GosuStringUtil.strip( GosuStringUtil.strip( _arg.toString() ) );
      if( _class == Throws.class || _class == Param.class )
      {
        return splitFirstArg( arg );
      }
      else
      {
        return new String[]{arg};
      }
    }

    private String[] splitFirstArg( String arg )
    {
      String[] args = arg.split("\\s+", 2);
      if (args.length == 1) { // no description
        args = new String[] { arg, "" };
      }
      return args;
    }
  }

  @Override
  public String toString()
  {
    return "DocCommentBlock(" + getDescription() + ")";
  }
}
