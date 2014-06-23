/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import java.util.ArrayList;
import java.util.List;

public class DftreeNode {

  private String _name;
  private List<DftreeNode> _children;
  private String _targetPath;

  public DftreeNode( String name ) {
    _name = name;
  }

  public String getName() {
    return _name;
  }

  public void setName( String name ) {
    this._name = name;
  }

  public String getTargetPath() {
    return _targetPath;
  }

  public void setTargetPath( String targetPath ) {
    this._targetPath = targetPath;
  }

  public List<DftreeNode> getChildren() {
    return _children;
  }

  public void addChild( DftreeNode child ) {
    if ( _children == null ) {
      _children = new ArrayList<DftreeNode>();
    }
    _children.add( child );
  }

}
