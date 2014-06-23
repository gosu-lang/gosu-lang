/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.xml.XmlElement;

import java.util.concurrent.TimeUnit;

@SuppressWarnings( { "UnusedDeclaration" } )
public interface AsyncResponseInternal<T, E extends XmlElement> {

  E getRequestEnvelope();
  void setRequestEnvelope( E requestEnvelope );
  E getResponseEnvelope();
  void setResponseEnvelope( E responseEnvelope );
  T get();
  T get( long timeout, TimeUnit unit );
  void start();
  void run();

}
