/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.objectsize;

import java.lang.reflect.Field;

public class DefaultObjectSizeFilter implements IObjectSizeFilter {

    public boolean skipField(Field field) {
        return field.getType().equals(Class.class);
    }

    public boolean skipObject(Object obj) {
        return obj.getClass().equals(Class.class);
    }
}
