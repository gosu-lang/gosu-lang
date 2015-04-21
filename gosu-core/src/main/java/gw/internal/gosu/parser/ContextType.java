/*
 * Copyright 2014. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ContextType {

  private enum CommonType { OBJECT, pBOOLEAN, pINT }

  public static final ContextType EMPTY = new ContextType( (IType)null, false );
  public static final ContextType OBJECT_FALSE = new CommonContextType( CommonType.OBJECT, false );
  public static final ContextType pBOOLEAN_FALSE = new CommonContextType( CommonType.pBOOLEAN, false );
  public static final ContextType pINT_FALSE = new CommonContextType( CommonType.pINT, false );

  private IType _type;
  private boolean _bMethodScoring;

  private ContextType( boolean bScoring ) {
    _bMethodScoring = bScoring;
  }

  public ContextType( IType type ) {
    this( type, false );
  }
  public ContextType( IType type, boolean bScoring ) {
    _type = type;
    _bMethodScoring = bScoring;
  }

  public boolean isMethodScoring() {
    return _bMethodScoring;
  }

  public IType getType() {
    return _type;
  }

  public IType getAlternateType() {
    return null;
  }

  private static class CommonContextType extends ContextType {
    private CommonType _ctype;

    private CommonContextType( CommonType ctype, boolean bScoring ) {
      super( bScoring );
      _ctype = ctype;
    }

    @Override
    public IType getType() {
      switch( _ctype ) {
        case OBJECT:
          return JavaTypes.OBJECT();
        case pBOOLEAN:
          return JavaTypes.pBOOLEAN();
        case pINT:
          return JavaTypes.pINT();
        default:
          throw new IllegalStateException();
      }
    }
  }

  public static ContextType makeBlockContexType( IType ctxType, IBlockType ctxTypeWithTypeVars, boolean bScoring ) {
    return new BlockContextType( ctxType, ctxTypeWithTypeVars, bScoring );
  }

  private static class BlockContextType extends ContextType {
    private IBlockType _ctxTypeWithTypeVars;

    private BlockContextType( IType ctxType, IBlockType ctxTypeWithTypeVars, boolean bScoring ) {
      super( ctxType, bScoring );
      _ctxTypeWithTypeVars = ctxTypeWithTypeVars;
    }

    public IBlockType getAlternateType() {
      return _ctxTypeWithTypeVars;
    }
  }
}
