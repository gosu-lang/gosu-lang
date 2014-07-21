package gw.internal.gosu.typeinfo;

import gw.config.CommonServices;
import gw.internal.gosu.parser.DefaultEntityAccess;
import gw.internal.gosu.parser.ExtendedTypeData;
import gw.internal.gosu.parser.ExtendedTypeDataFactory;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.java.IJavaType;

public class ExtendedEntityAccess extends DefaultEntityAccess implements ExtendedTypeDataFactory {

  private static IEntityAccess _oldAccess;

  public static void install()
  {
    _oldAccess = CommonServices.getEntityAccess();
    CommonServices.getKernel().redefineService_Privileged(IEntityAccess.class, new ExtendedEntityAccess());
  }

  public static void uninstall()
  {
    CommonServices.getKernel().redefineService_Privileged(IEntityAccess.class, _oldAccess);
    _oldAccess = null;

  }

  @Override
  public ExtendedTypeDataFactory getExtendedTypeDataFactory(String typeName) {
    return this;
  }

  @Override
  public ExtendedTypeData newTypeData(String name) {
    return ExtendedTypeData.newInstance(new IExtraTypeData() {
      @Override
      public String getExtraData() {
        return "ExtraData";
      }
    }, IExtraTypeData.class);
  }

  @Override
  public ExtendedTypeData newPropertyData(IJavaType type, String name) {
    return ExtendedTypeData.newInstance(new IExtraPropertyData() {
      @Override
      public String getExtraPropertyData() {
        return "ExtraPropertyData";
      }
    }, IExtraPropertyData.class);
  }
}
