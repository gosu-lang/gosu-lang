package gw.lang.ir;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

public class SignatureUtil {

  static public void visitType( SignatureVisitor sv, IType type, boolean[] bGeneric ) {
    if( type instanceof ITypeVariableType) {
      sv.visitTypeVariable( type.getRelativeName() );
    }
    else if( !TypeSystem.isBytecodeType(type) ) {
      sv.visitClassType( Object.class.getName().replace( '.', '/' ) );
      sv.visitEnd();
    }
    else if( type.isArray() ) {
      SignatureVisitor arrSv = sv.visitArrayType();
      visitType( arrSv, type.getComponentType(), bGeneric );
      arrSv.visitEnd();
    } else if( type.isPrimitive() ) {
      String name = type.getName();
      char c;
      if( name.equals("boolean") ) {
        c = 'Z';
      } else if( name.equals("long") ) {
        c = 'J';
      } else {
        c = Character.toUpperCase(name.charAt(0));
      }
      sv.visitBaseType(c);
    }
    else {
      IType rawType = type.getGenericType() == null ? type : type.getGenericType();
      String rawName = rawType.isPrimitive() ? rawType.getName() : processName( rawType );
      sv.visitClassType( rawName );
      if( type.isParameterizedType() ) {
        bGeneric[0] = true;
        for( IType param: type.getTypeParameters() ) {
          sv.visitTypeArgument( '=' );
          visitType( sv, param, bGeneric );
        }
      }
      if( !rawType.isPrimitive() ) {
        sv.visitEnd();
      }
    }
  }

  public static IType getPureGenericType(IType type) {
    while( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    return type;
  }

  private static String processName( IType type ) {
    String name = makeJavaName( type );
    if( name.length() > IGosuClass.PROXY_PREFIX.length() && name.startsWith( IGosuClass.PROXY_PREFIX ) ) {
      name = IGosuClass.ProxyUtil.getNameSansProxy( name );
    }
    return name.replace( '.', '/' );
  }

  private static String makeJavaName( IType type ) {
    IType enclosingType = type.getEnclosingType();
    if( enclosingType != null ) {
      return makeJavaName( enclosingType ) + '$' + getSimpleName( type.getRelativeName() );
    }
    return type.getName();
  }

  private static String getSimpleName( String name ) {
    int iDot = name.lastIndexOf( '.' );
    if( iDot >= 0 ) {
      return name.substring( iDot + 1 );
    }
    return name;
  }
}
