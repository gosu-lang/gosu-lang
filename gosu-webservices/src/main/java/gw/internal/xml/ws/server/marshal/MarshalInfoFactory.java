package gw.internal.xml.ws.server.marshal;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IType;

public interface MarshalInfoFactory {
  MarshalInfo createMarshalInfoForType(IType type, boolean component, WsiServiceInfo serviceInfo);
}
