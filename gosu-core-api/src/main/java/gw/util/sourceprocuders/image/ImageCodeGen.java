package gw.util.sourceprocuders.image;

import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.gen.SrcClass;
import gw.lang.reflect.gs.gen.SrcConstructor;
import gw.lang.reflect.gs.gen.SrcField;
import gw.lang.reflect.gs.gen.SrcMethod;
import gw.lang.reflect.gs.gen.SrcParameter;
import gw.lang.reflect.gs.gen.SrcRawStatement;
import gw.lang.reflect.gs.gen.SrcStatementBlock;
import gw.lang.reflect.gs.gen.SrcType;
import gw.util.GosuClassUtil;
import gw.util.GosuEscapeUtil;
import gw.util.GosuExceptionUtil;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 */
public class ImageCodeGen
{
  private final String _fqn;
  private final String _url;

  public ImageCodeGen( String url, String topLevelFqn )
  {
    _url = url;
    _fqn = topLevelFqn;
  }

  public SrcClass make()
  {
    try
    {
      return new SrcClass( _fqn, SrcClass.Kind.Class ).uses( URL.class )
        .superClass( new SrcType( ImageIcon.class ) )
        .addField( new SrcField( "INSTANCE", GosuClassUtil.getShortClassName( _fqn ) ).modifiers( Modifier.STATIC ) )
        .addConstructor( new SrcConstructor()
                           .addParam( new SrcParameter( "url" )
                                        .type( URL.class ) )
                           .modifiers( Modifier.PRIVATE )
                           .body( new SrcStatementBlock<SrcConstructor>()
                                    .addStatement( new SrcRawStatement()
                                                     .rawText( "super( url )" ) )
                                    .addStatement( new SrcRawStatement()
                                                     .rawText( "INSTANCE = this" ) ) ) )
        .addMethod( new SrcMethod().modifiers( Modifier.STATIC )
                      .name( "get" )
                      .returns( GosuClassUtil.getShortClassName( _fqn ) )
                      .body( new SrcStatementBlock<SrcMethod>()
                               .addStatement( new SrcRawStatement().rawText( "return INSTANCE != null ? INSTANCE : new " + _fqn + "(new URL(\"" + GosuEscapeUtil.escapeForGosuStringLiteral( _url ) + "\"))" ) ) ) );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }
}
