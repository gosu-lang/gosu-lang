/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Primitives {
  private static final Map<String, Class> PRIMITIVE_MAP = new HashMap<String, Class>();
  private static final Map<String, String> PRIMITIVE_ARRAY_MAP = new HashMap<String, String>();

  static {
    PRIMITIVE_MAP.put("boolean", boolean.class);
    PRIMITIVE_MAP.put("short", short.class);
    PRIMITIVE_MAP.put("int", int.class);
    PRIMITIVE_MAP.put("double", double.class);
    PRIMITIVE_MAP.put("float", float.class);
    PRIMITIVE_MAP.put("char", char.class);
    PRIMITIVE_MAP.put("byte", byte.class);
    PRIMITIVE_MAP.put("long", long.class);
    PRIMITIVE_MAP.put("void", void.class);

    PRIMITIVE_ARRAY_MAP.put("boolean", boolean[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("short", short[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("int", int[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("double", double[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("float", float[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("char", char[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("byte", byte[].class.getName());
    PRIMITIVE_ARRAY_MAP.put("long", long[].class.getName());
  }

  public static boolean isPrimitive(String type) {
    return PRIMITIVE_MAP.containsKey(type);
  }

  public static final Class get(String type) {
    return PRIMITIVE_MAP.get(type);
  }

  public static final String getArraySignature(String type) {
    return PRIMITIVE_ARRAY_MAP.get(type);
  }

  public static Collection<? extends String> getAllPrimitiveNames() {
    return PRIMITIVE_MAP.keySet();
  }
}
