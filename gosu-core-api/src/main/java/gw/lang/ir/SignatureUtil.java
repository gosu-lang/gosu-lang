package gw.lang.ir;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.FunctionClassUtil;
import java.util.ArrayList;
import java.util.Arrays;

public class SignatureUtil
{
  static public void visitType( SignatureVisitor sv, IType type, boolean[] bGeneric )
  {
    if( type instanceof ITypeVariableType )
    {
      sv.visitTypeVariable( type.getRelativeName() );
    }
    else if( type instanceof IFunctionType )
    {
      IJavaType arityType = getParamterizedArityType( (IFunctionType)type );
      visitType( sv, arityType, bGeneric );
    }
    else if( !TypeSystem.isBytecodeType( type ) )
    {
      sv.visitClassType( Object.class.getName().replace( '.', '/' ) );
      sv.visitEnd();
    }
    else if( type.isArray() )
    {
      SignatureVisitor arrSv = sv.visitArrayType();
      visitType( arrSv, type.getComponentType(), bGeneric );
    }
    else if( type.isPrimitive() )
    {
      String name = type.getName();
      char c;
      if( name.equals( "boolean" ) )
      {
        c = 'Z';
      }
      else if( name.equals( "long" ) )
      {
        c = 'J';
      }
      else
      {
        c = Character.toUpperCase( name.charAt( 0 ) );
      }
      sv.visitBaseType( c );
    }
    else
    {
      IType rawType = type.getGenericType() == null ? type : type.getGenericType();
      String rawName = rawType.isPrimitive() ? rawType.getName() : processName( rawType );
      sv.visitClassType( rawName );
      if( type.isParameterizedType() )
      {
        bGeneric[0] = true;
        for( IType param : type.getTypeParameters() )
        {
          sv.visitTypeArgument( '=' );
          visitType( sv, param, bGeneric );
        }
      }
      if( !rawType.isPrimitive() )
      {
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

  private static IJavaType getParamterizedArityType( IFunctionType funcType )
  {
    boolean hasReturn = funcType.getReturnType() != JavaTypes.pVOID();
    IJavaType arityType = FunctionClassUtil.getFunctionInterfaceForArity( hasReturn, funcType.getParameterTypes().length );
    if( arityType.isGenericType() )
    {
      IType returnType = funcType.getReturnType();
      ArrayList<IType> typeParams = new ArrayList<>();
      if( hasReturn )
      {
        typeParams.add( returnType );
      }
      typeParams.addAll( Arrays.asList( funcType.getParameterTypes() ) );
      arityType = (IJavaType)arityType.getParameterizedType( typeParams.toArray( new IType[typeParams.size()] ) );
    }
    return arityType;
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

  public static void visitGenericType( SignatureVisitor sw, IType funcType, boolean[] bGeneric )
  {
    if( funcType.isGenericType() ) {
      bGeneric[0] = true;
      for( IGenericTypeVariable tv: funcType.getGenericTypeVariables() ) {
        sw.visitFormalTypeParameter( tv.getName() );
        IType boundingType = tv.getBoundingType();
        if( boundingType != null ) {
          IType[] types;
          if( boundingType instanceof ICompoundType ) {
            types = ((ICompoundType) boundingType).getTypes().toArray(new IType[0]);
          } else {
            types = new IType[] {boundingType};
          }
          SignatureVisitor sv;
          for(int i = types.length-1; i >= 0 ; i--) {
            if( types[i].isInterface() ) {
              sv = sw.visitInterfaceBound();
            }
            else {
              sv = sw.visitClassBound();
            }
            SignatureUtil.visitType( sv, SignatureUtil.getPureGenericType(types[i]), bGeneric );
          }
        }
        else {
          SignatureVisitor sv = sw.visitClassBound();
          SignatureUtil.visitType( sv, JavaTypes.OBJECT(), bGeneric );
        }
      }
    }
  }
}
