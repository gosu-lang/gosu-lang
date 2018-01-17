/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.objectsize;

import gw.util.perf.InvocationCounter;

import gw.util.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public class ObjectSizeUtil {
  public static boolean VERBOSE = true;
  private static final String INDENT = "  ";
  private static final IObjectSizeFilter DEFAULT_FILTER = new DefaultObjectSizeFilter();
  private static DecimalFormat decimalFormat = new DecimalFormat("##.#");
  
  private static final Map primitiveSizes = new IdentityHashMap() {
    {
      put(boolean.class, new Integer(1));
      put(byte.class, new Integer(1));
      put(char.class, new Integer(2));
      put(short.class, new Integer(2));
      put(int.class, new Integer(4));
      put(float.class, new Integer(4));
      put(double.class, new Integer(8));
      put(long.class, new Integer(8));
    }
  };

  public static int getFieldSize(Class clazz) {
    Integer i = (Integer) primitiveSizes.get(clazz);
    return i != null ? i.intValue() : getPointerSize();
  }

  public static int getPointerSize() {
    return 4;
  }

  /**
   * Calculates full size of object iterating over its hierarchy graph.
   * 
   * @param obj object to calculate size of
   * @param filter the filter used to ignore fields or objects
   * @param maxObjects the max numbers of objects to traverse
   * @return object size
   * @throws Exception 
   */
  public static ObjectSize deepSizeOf(Object obj, IObjectSizeFilter filter, int maxObjects) {
    Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
    Stack<ObjectEntry> stack = new Stack<ObjectEntry>();
    InvocationCounter sizeHistogram = new InvocationCounter(false);
    
    long result = internalSizeOf(new ObjectEntry(obj, "", ""), stack, visited, filter, "");
    sizeHistogram.recordInvocation(obj.getClass().getName(), (int)result);
    int n = 1;
    while (!stack.isEmpty()) {
      ObjectEntry entry = stack.pop();
      long size = internalSizeOf(entry, stack, visited, filter, entry.indent);
      result += size;
      n++;
      sizeHistogram.recordInvocation(entry.object.getClass().getName(), (int)size);
      if (n >= maxObjects) {
        return new ObjectSize(result, false);
      }
    }
    visited.clear();
    
    if (VERBOSE) {
      System.out.println();      
      System.out.println("-------------------------------------------------");      
      sizeHistogram.print();
    }

    return new ObjectSize(result, true);
  }

  public static ObjectSize deepSizeOf(Object obj)  {
    try {
      return deepSizeOf(obj, DEFAULT_FILTER, Integer.MAX_VALUE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean skipObject(Object obj, Map<Object, Object> visited,IObjectSizeFilter filter) {
    return obj == null || visited.containsKey(obj) || filter.skipObject(obj);
  }

  private static boolean skipField(Field field, IObjectSizeFilter filter) {
    return filter.skipField(field);
  }

  private static long getArrayShallowSize(Object obj) {
    long result = 16;
    int length = Array.getLength(obj);
    if (length != 0) {
      Class<?> arrayElementClazz = obj.getClass().getComponentType();
      if (arrayElementClazz.isPrimitive()) {
        result += length * getFieldSize(arrayElementClazz);
      } else {
        result += length * getPointerSize();
      }
    }
    return result;
  }

  private static long internalSizeOf(ObjectEntry entry, Stack<ObjectEntry> stack, Map<Object, Object> visited, IObjectSizeFilter filter, String indent) {
    Object obj = entry.object;
    if (skipObject(obj, visited, filter)) {
      return 0;
    }
    visited.put(obj, null);

    long result = 0;
    Class<?> clazz = obj.getClass();
    if (clazz.isArray()) {
      result += getArrayShallowSize(obj);
      // process all array elements but skip primitive type array
      if (clazz.getName().length() != 2) {
        int length = Array.getLength(obj);
        for (int i = length - 1; i >= 0; i--) {
          Object o = Array.get(obj, i);
          if (!skipObject(o, visited, filter)) {
            stack.add(new ObjectEntry(o, indent + INDENT, "[" + i + "]: "));
          }
        }
      }
    } else {
      result = 8;
      // process all fields of the object
      while (clazz != null) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
          if (!Modifier.isStatic(fields[i].getModifiers())) {
            Field field = fields[i];
            result += getFieldSize(field.getType());
            if (!field.getType().isPrimitive() && !skipField(field, filter)) {
              field.setAccessible(true);
              try {
                // objects to be estimated are put to stack
                Object objectToAdd = field.get(obj);
                if (!skipObject(objectToAdd, visited, filter)) {
                  stack.add(new ObjectEntry(objectToAdd, indent + INDENT, field.getName() + ": "));
                }
              } catch (IllegalAccessException ex) {
                assert false;
              }
            }
          }
        }
        clazz = clazz.getSuperclass();
      }
    }

    result = roundUpToNearestEightBytes(result);
    if (VERBOSE ) {
      String extra = "";
      if (obj instanceof HashMap) {
        try {
          Method m = obj.getClass().getDeclaredMethod("capacity");
          m.setAccessible(true);
          int capacity = (Integer) m.invoke(obj);
          extra = " (" + decimalFormat.format(100.0 * ((HashMap)obj).size()/capacity) + "%) ";
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      if (obj instanceof ArrayList) {
        try {
          Field f = obj.getClass().getDeclaredField("elementData");
          f.setAccessible(true);
          int capacity = Array.getLength(f.get(obj));
          if (capacity == 0) {
            extra = " (empty) ";
          } else {
            extra = " (" + decimalFormat.format(100.0 * ((ArrayList)obj).size()/capacity) + "%) ";
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      System.out.println(indent + entry.info + obj.getClass().getName() + extra + " - " + result);
    }
    return result;
  }

  private static long roundUpToNearestEightBytes(long result) {
    if ((result % 8) != 0) {
      result += 8 - (result % 8);
    }
    return result;
  }

  private static class ObjectEntry {
    public Object object;
    public String indent;
    public String info;

    public ObjectEntry(Object object, String indent, String info) {
      this.object = object;
      this.indent = indent;
      this.info = info;
    }
  }

  public static void main(String[] args) throws Exception {
    String[] s = new String[11*1024*1024];
    System.out.println(ObjectSizeUtil.deepSizeOf(s));
  }
}
