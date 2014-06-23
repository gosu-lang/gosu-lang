/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import java.util.HashSet;
import java.util.Set;

public class MarshalContext {
  public Set<Object> getSeen() {
    return seen;
  }

  Set<Object> seen = new HashSet<Object>();
}
