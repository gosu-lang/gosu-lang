/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public abstract class IRElement {
  private IRElement _parent;
  private int _iLineNumber;
  private boolean _bImplicit;

  protected IRElement() {
    _iLineNumber = -1;
  }

  public IRElement getParent() {
    return _parent;
  }

  public void setParent( IRElement parent ) {
    _parent = parent;
  }

  protected void setParentToThis( IRElement element ) {
    if (element != null) {
      element.setParent( this );
    }
  }

  public boolean isImplicit() {
    return _bImplicit || (getParent() != null && getParent().isImplicit());
  }
  public void setImplicit( boolean bImplicit ) {
    _bImplicit = bImplicit;
  }

  public int getLineNumber() {
    if( !isImplicit() ) {
      return _iLineNumber;
    }
    return -1;
  }
  public void setLineNumber( int iLineNumber ) {
    if( !isImplicit() ) {
      _iLineNumber = iLineNumber;
    }
  }

  protected IRType maybeEraseStructuralType( IRType type ) {
    return maybeEraseStructuralType( null, type );
  }
  protected IRType maybeEraseStructuralType( IRType ownersType, IRType type ) {
    if( ownersType == null ? type.isStructural() : type.isStructuralAndErased( ownersType ) ) {
      return GosuShop.getIRTypeResolver().getDescriptor( Object.class );
    }
    return type;
  }

  protected List<IRType> maybeEraseStructuralTypes( IRType ownersType, List<IRType> types ) {
    List<IRType> altTypes = null;
    for( IRType csr: types ) {
      if( csr.isStructuralAndErased( ownersType ) ) {
        IRType type = GosuShop.getIRTypeResolver().getDescriptor( Object.class );
        if( altTypes == null ) {
          altTypes = new ArrayList<IRType>( types );
        }
        altTypes.set( types.indexOf( csr ), type );
      }
    }
    return altTypes == null ? types : altTypes;
  }

  protected List<IRSymbol> maybeEraseStructuralSymbolTypes( List<IRSymbol> parameters ) {
    for( IRSymbol sym: parameters ) {
      IRType type = sym.getType();
      if( type.isStructural() ) {
        type = GosuShop.getIRTypeResolver().getDescriptor( Object.class );
        sym.setType( type );
      }
    }
    return parameters;
  }
}
