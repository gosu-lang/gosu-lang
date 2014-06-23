/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

// TODO: We can do more with Http_Headers in the future, such as allowing to remove headers, clear all headers with a certain name,
// replace headers with a certain name, access existing headers, etc. Keep in mind that HTTP header names are case-insensitive (RFC 2616)
public class HttpHeaders {

  private LinkedHashMap<String, Pair<String, String>> _headers = new LinkedHashMap<String, Pair<String, String>>();

  public void setHeader( String name, String value ) {
    String ucName = name.toUpperCase();
    _headers.put( ucName, new Pair<String,String>( name, value ) );
  }
  
  public List<String> getHeaderNames() {
    ArrayList<String> ret = new ArrayList<String>();
    for ( Pair<String, String> pair : _headers.values() ) {
      ret.add( pair.getFirst() );
    }
    return Collections.unmodifiableList( ret );
  }
  
  public String getHeader( String name ) {
    Pair<String, String> ret = _headers.get( name.toUpperCase() );
    return ret == null ? null : ret.getSecond();
  }

  @Override
  public String toString() {
    return _headers.values().toString();
  }
  
}
