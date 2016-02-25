/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.JavaTypes;


/**
 */
public class MethodNode extends BeanInfoNode
{
  private IMethodInfo _mi;


  public MethodNode( IMethodInfo mi )
  {
    super( new FunctionType( mi ) );

    _mi = mi;
  }

  @Override
  public String getDisplayName()
  {
    return _mi.getDisplayName() + getParameterDisplay() + " : " + getTypeName( getReturnType() );
  }

  private IType getReturnType()
  {
    IType retType = ((IFunctionType)getType()).getReturnType();
    if( retType != JavaTypes.pVOID() && _mi instanceof IJavaMethodInfo )
    {
      retType = _mi.getReturnType();
    }
    return retType;
  }

  /** */
  @Override
  public String getName()
  {
    return _mi.getDisplayName() + getParameterDisplay();
  }

  @Override
  public IFeatureInfo getFeatureInfo()
  {
    return _mi;
  }

  @Override
  protected int getTypePriority()
  {
    return SECONDARY;
  }

  /** */
  public IMethodInfo getMethodDescriptor()
  {
    return _mi;
  }

  /** */
  public String getParameterDisplay()
  {
    IParameterInfo[] pd = _mi.getParameters();
    if( pd == null || pd.length == 0 )
    {
      return TypeInfoUtil.getTypeVarList( _mi, true ) + "()";
    }

    StringBuilder sbParams = new StringBuilder();
    sbParams.append( TypeInfoUtil.getTypeVarList( _mi, true ) ).append( "(" );
    for( int i = 0; i < pd.length; i++ )
    {
      String strName = pd[i].getName();
      sbParams.append( i == 0 ? "" : ", " ).append( strName );
      boolean bBlock = pd[i].getFeatureType() instanceof IBlockType;
      String strType = bBlock
                       ? ((IBlockType)pd[i].getFeatureType()).getRelativeNameSansBlock()
                       : pd[i].getFeatureType().getRelativeName();
      if( bBlock )
      {
        sbParams.append( strType );
      }
      else
      {
        sbParams.append( " : " ).append( strType );
      }
    }
    sbParams.append( ")" );

    return sbParams.toString();
  }
}
