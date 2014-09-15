package gw.internal.xml.ws.server.marshal;

import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IType;

public interface MarshalInfoFactory {
  MarshalInfo createMarshalInfoForType(IType type, boolean component, WsiServiceInfo serviceInfo);
}
