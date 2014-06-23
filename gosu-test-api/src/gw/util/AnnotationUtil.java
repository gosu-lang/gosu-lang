/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationUtil {
  public static Map<IType, List<IAnnotationInfo>> map(List<IAnnotationInfo> annotations) {
    HashMap<IType, List<IAnnotationInfo>> map = new HashMap<IType, List<IAnnotationInfo>>();
    for (IAnnotationInfo annotation : annotations) {
      List<IAnnotationInfo> infoList = map.get(annotation.getType());
      if (infoList == null) {
        infoList = new ArrayList<IAnnotationInfo>();
        map.put(annotation.getType(), infoList);
      }
      infoList.add(annotation);
    }
    return map;
  }
}
