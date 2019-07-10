package gw.lang.ir;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

public class SignatureUtil {
  static public void visitIrType( SignatureVisitor sv, IRType type ) {
    if( type.isArray() ) {
      SignatureVisitor arrSv = sv.visitArrayType();
      visitIrType( arrSv, type.getComponentType() );
    } else if( type.isPrimitive() ) {
      sv.visitBaseType(type.getDescriptor().charAt( 0 ));
    }
    else {
      sv.visitClassType( type.getSlashName() );
      sv.visitEnd();
    }
  }

  static public void visitType( SignatureVisitor sv, IType type, boolean[] bGeneric ) {
    if( type instanceof ITypeVariableType) {
      sv.visitTypeVariable( type.getRelativeName() );
    }
    else if( !TypeSystem.isBytecodeType(type) ) {
      if( type instanceof IFunctionType ) {
        type = TypeSystem.getFunctionalInterface( (IFunctionType)type );
        visitType( sv, type, bGeneric );
      }
      else {
        sv.visitClassType( Object.class.getName().replace( '.', '/' ) );
        sv.visitEnd();
      }
    }
    else if( type.isArray() ) {
      SignatureVisitor arrSv = sv.visitArrayType();
      visitType( arrSv, type.getComponentType(), bGeneric );
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
      IType rawType = makeRawType( type );
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

  private static IType makeRawType( IType type ) {
    IType ret;
    if( type instanceof ICompoundType ) {
      ret = makeRawType( ((ICompoundType)type).getTypes().iterator().next() );
    }
    else {
      ret = type.getGenericType() == null ? type : type.getGenericType();
    }
    return ret;
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

  public static String getSimpleName( String name ) {
    int iDot = name.lastIndexOf( '.' );
    if( iDot >= 0 ) {
      return name.substring( iDot + 1 );
    }
    return name;
  }
}
