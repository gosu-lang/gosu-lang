package editor.util;

import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.util.Textifiable;
import gw.internal.ext.org.objectweb.asm.util.Textifier;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class GosuTextifier extends Textifier
{
  public GosuTextifier()
  {
    super( Opcodes.ASM5 );
  }

  @Override
  public void visitAttribute( final Attribute attr )
  {
    buf.setLength( 0 );
    buf.append( tab ).append( "ATTRIBUTE " );
    appendDescriptor( -1, attr.type );

    if( attr instanceof Textifiable )
    {
      ((Textifiable)attr).textify( buf, null );
    }
    else
    {
      byte[] data = {};
      try
      {
        Class<Attribute> aClass = Attribute.class;

        Field[] fields = aClass.getDeclaredFields();
        fields[1].setAccessible( true );
        data = (byte[])fields[1].get( attr );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
      if( data.length > 0 )
      {
        buf.append( " : " );
        try
        {
          buf.append( new String( data, "US-ASCII" ) );
        }
        catch( UnsupportedEncodingException e )
        {
          buf.append( "unknown" );
        }
      }
      buf.append( '\n' );
    }
    text.add( buf.toString() );
  }


}
