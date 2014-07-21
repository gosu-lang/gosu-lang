package gw.internal.gosu.typeinfo;

import gw.config.CommonServices;
import gw.internal.gosu.parser.DefaultEntityAccess;
import gw.internal.gosu.parser.ExtendedTypeData;
import gw.internal.gosu.parser.ExtendedTypeDataFactory;
import gw.lang.parser.resources.Strings;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;

public class LocalizingEntityAccess extends DefaultEntityAccess {

  private static IEntityAccess _oldAccess;

  public static void install()
  {
    _oldAccess = CommonServices.getEntityAccess();
    CommonServices.getKernel().redefineService_Privileged(IEntityAccess.class, new LocalizingEntityAccess());
  }

  public static void uninstall()
  {
    CommonServices.getKernel().redefineService_Privileged(IEntityAccess.class, _oldAccess);
    _oldAccess = null;

  }

  @Override
  public String getLocalizedTypeName(IType type) {
    return super.getLocalizedTypeName(type).toUpperCase();
  }

  @Override
  public String getLocalizedTypeInfoName(IType type) {
    return super.getLocalizedTypeInfoName(type).toLowerCase();
  }
}
