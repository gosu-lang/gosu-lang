/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.Predicate;
import org.fest.util.Arrays;

public class KnownBreakConditionPredicate implements Predicate<IAnnotationInfo> {
  @Override
  public boolean evaluate(IAnnotationInfo o) {
    return isKnownBreakCondition( o );
  }

  public static boolean isKnownBreakCondition(KnownBreakCondition kbCond) {
    for (Class<? extends IEnvironmentalCondition> conditionClass : kbCond.value()) {
      IEnvironmentalCondition condition;
      try {
        condition = conditionClass.newInstance();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      if (condition.isConditionMet()) {
        return true;
      }
    }
    return false;
  }
  public static boolean isKnownBreakCondition( IAnnotationInfo kbCond ) {
    Object values = kbCond.getFieldValue( "value" );
    if( !(values instanceof Object[]) ) {
      values = Arrays.array( values );
    }
    for (Object conditionClass : (Object[])values ) {
      IType type = conditionClass instanceof Class ? TypeSystem.get( (Class)conditionClass ) : (IType)conditionClass;
      IEnvironmentalCondition condition = (IEnvironmentalCondition)type.getTypeInfo().getConstructor().getConstructor().newInstance();
      if (condition.isConditionMet()) {
        return true;
      }
    }
    return false;
  }
}
