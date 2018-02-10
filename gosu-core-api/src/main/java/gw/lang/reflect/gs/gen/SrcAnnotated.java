package gw.lang.reflect.gs.gen;

import gw.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 */
public abstract class SrcAnnotated<T extends SrcAnnotated<T>> extends SrcElement
{
  private List<SrcAnnotationExpression> _annotations = new ArrayList<>();
  private int _modifiers;
  private String _name;
  private List<SrcParameter> _parameters = new ArrayList<>();

  public SrcAnnotated() {}
  public SrcAnnotated( SrcAnnotated owner )
  {
    super( owner );
  }

  public T annotation( SrcAnnotationExpression anno )
  {
    _annotations.add( anno );
    return (T)this;
  }

  public T modifiers( int modifiers )
  {
    _modifiers = modifiers;
    return (T)this;
  }

  public T name( String simpleName )
  {
    _name = simpleName;
    return (T)this;
  }

  public T addParam( SrcParameter param )
  {
    _parameters.add( param );
    return (T)this;
  }
  public T addParam( String name, Class type )
  {
    _parameters.add( new SrcParameter( name, type ) );
    return (T)this;
  }
  public T addParam( String name, String type )
  {
    _parameters.add( new SrcParameter( name, type ) );
    return (T)this;
  }
  public T addParam( String name, SrcType type )
  {
    _parameters.add( new SrcParameter( name, type ) );
    return (T)this;
  }

  public List<SrcAnnotationExpression> getAnnotations()
  {
    return _annotations;
  }

  public int getModifiers()
  {
    return _modifiers;
  }

  public String getSimpleName()
  {
    return _name;
  }

  public List<SrcParameter> getParameters()
  {
    return _parameters;
  }

  protected void renderAnnotations( StringBuilder sb, int indent, boolean sameLine )
  {
    for( SrcAnnotationExpression anno : _annotations )
    {
      anno.render( sb, indent, sameLine );
    }
  }

  protected String renderParameters( StringBuilder sb )
  {
    sb.append( '(' );
    for( int i = 0; i < _parameters.size(); i++ )
    {
      if( i > 0 )
      {
        sb.append( ", " );
      }
      SrcParameter param = _parameters.get( i );
      param.render( sb, 0 );
    }
    sb.append( ')' );
    return "";
  }

  public StringBuilder renderArgumenets( StringBuilder sb, List<SrcArgument> arguments, int indent, boolean sameLine )
  {
    sb.append( '(' );
    for( int i = 0; i < arguments.size(); i++ )
    {
      if( i > 0 )
      {
        sb.append( ", " );
      }
      SrcArgument arg = arguments.get( i );
      arg.render( sb, 0 );
    }
    sb.append( ')' ).append( sameLine ? "" : "\n" );
    return sb;
  }

  protected String renderModifiers( StringBuilder sb, boolean isDefault, int defModifier )
  {
    return renderModifiers( sb, _modifiers, isDefault, defModifier );
  }
  protected String renderModifiers( StringBuilder sb, int modifiers, boolean isDefault, int defModifier )
  {
    if( (modifiers & Modifier.PUBLIC) != 0 )
    {
      sb.append( "public " );
    }
    else if( (modifiers & Modifier.PROTECTED) != 0 )
    {
      sb.append( "protected " );
    }
    else if( (modifiers & Modifier.PRIVATE) != 0 )
    {
      sb.append( "private " );
    }
    else if( (modifiers & Modifier.INTERNAL) != 0 )
    {
      sb.append( "internal " );
    }
    else if( defModifier != 0 )
    {
      renderModifiers( sb, defModifier, false, 0 );
    }

    // Canonical order
    if( (modifiers & Modifier.ABSTRACT) != 0 )
    {
      sb.append( "abstract " );
    }
    if( (modifiers & Modifier.OVERRIDE) != 0 )
    {
      sb.append( "override " );
    }
    if( (modifiers & Modifier.STATIC) != 0 )
    {
      sb.append( "static " );
    }
    if( (modifiers & Modifier.FINAL) != 0 )
    {
      sb.append( "final " );
    }
    if( (modifiers & Modifier.TRANSIENT) != 0 )
    {
      sb.append( "transient " );
    }
    if( (modifiers & Modifier.VOLATILE) != 0 )
    {
      sb.append( "volatile " );
    }
    return "";
  }
}
