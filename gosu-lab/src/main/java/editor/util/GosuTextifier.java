package editor.util;

import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.Opcodes;
//import gw.internal.ext.org.objectweb.asm.util.Textifiable;
import gw.internal.ext.org.objectweb.asm.util.Textifier;
import manifold.util.ReflectUtil;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class GosuTextifier extends Textifier
{
  public GosuTextifier()
  {
    super( Opcodes.ASM7 );
  }

  @Override
  public void visitAttribute( final Attribute attr )
  {
    stringBuilder.setLength( 0 );
    stringBuilder.append( tab ).append( "ATTRIBUTE " );
    appendDescriptor( -1, attr.type );

//    if( attr instanceof Textifiable )
//    {
//      ((Textifiable)attr).textify( new StringBuffer( stringBuilder ), null );
//    }
//    else
//    {
      byte[] data;
      try
      {
        Class<Attribute> aClass = Attribute.class;

        Field[] fields = aClass.getDeclaredFields();
        ReflectUtil.setAccessible( fields[1] );
        data = (byte[])fields[1].get( attr );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
      if( data.length > 0 )
      {
        stringBuilder.append( " : " );
        stringBuilder.append( new String( data, StandardCharsets.US_ASCII ) );
      }
      stringBuilder.append( '\n' );
//    }
    text.add( stringBuilder.toString() );
  }


}
