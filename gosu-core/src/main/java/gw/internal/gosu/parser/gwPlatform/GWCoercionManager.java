package gw.internal.gosu.parser.gwPlatform;

import gw.config.CommonServices;
import gw.lang.parser.ICoercer;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.coercers.IdentityCoercer;
import gw.lang.reflect.IEnumConstant;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cgross
 */
public class GWCoercionManager extends StandardCoercionManager {

  public ICoercer getCoercerInternal(IType lhsType, IType rhsType, boolean runtime) {
    lhsType = TypeSystem.getTypeFromJavaBackedType(lhsType);
    rhsType = TypeSystem.getTypeFromJavaBackedType(rhsType);

    if (TypeSystem.isDeleted(lhsType) || TypeSystem.isDeleted(rhsType)) {
      return null;
    }

    ICoercer coercer = super.getCoercerInternal(lhsType, rhsType, runtime);
    if (coercer != null) {
      return coercer;
    }

    //TODO-dp this coercion needs to be upgraded and removed
    //=============================================================================
    //  IEntityType <- MetaType<IEntityType>
    //=============================================================================
    IType entityType = TypeSystem.getByFullNameIfValid("gw.entity.IEntityType");
    if (entityType != null && entityType.equals(lhsType) &&
            rhsType instanceof IMetaType && CommonServices.getEntityAccess().isEntityClass(((IMetaType) rhsType).getType())) {
      return IdentityCoercer.instance();
    }

    return null;
  }

  public String makeStringFrom(Object obj) {
    if (obj == null) {
      return null;
    }

    if (obj instanceof String) {
      return (String) obj;
    }

    if (obj instanceof Number) {
      // Avoid appending a ".0" to numeric values with no decimal.

      if (obj instanceof BigDecimal) {
        BigDecimal bd = (BigDecimal) obj;
        BigDecimal bdi = new BigDecimal(bd.toBigInteger());
        obj = bd.compareTo(bdi) == 0 ? bdi : bd;
      } else if (obj instanceof Float ||
              obj instanceof Double) {
        Number number = (Number) obj;
        long lValue = number.longValue();
        if (lValue == number.doubleValue()) {
          return String.valueOf(lValue);
        }
      }
      return obj.toString();
    }

    if (obj instanceof Date) {
      return CommonServices.getCoercionManager().formatTime((Date) obj, "yyyy-MM-dd");
    }

    if (CommonServices.getEntityAccess().isDomainInstance(obj)) {
      return CommonServices.getEntityAccess().makeStringFrom(obj);
    }

    if (obj instanceof IEnumConstant) {
      return ((IEnumConstant) obj).getCode();
    }

    return obj.toString();
  }

  @Override
  public BigDecimal makeBigDecimalFrom(Object obj) {
    if (obj instanceof String) {
      //!!! temporary hack: interpret empty String double values or dashes as zeros.
      //!!! Ben?
      String strValue = (String) obj;
      if (strValue.length() == 0 || strValue.equals("-")) {
        return IGosuParser.BIGD_ZERO;
      } else // hack to support an insane currency coercion we used to support
      {    // I'm sure this will be as temporary as the hack above, put in years ago
        return new BigDecimal(parseNumber(strValue).toString());
      }
    }
    return super.makeBigDecimalFrom(obj);
  }

}
