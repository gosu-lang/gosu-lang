package gw.internal.gosu.util;

import gw.lang.reflect.gs.IGosuProgram;
import gw.test.TestClass;
public class SFDR_4560_Test extends TestClass {
    public void testCacheWorks() {
        String value1 = notStatic("blah.") + "Foo" + ".blah";
        String value2 = notStatic("blah.") + "Foo" + ".blah";
        assertNotSame(value1, value2);
        String result1 = StringPool.get( value1 );
        String result2 = StringPool.get( value2 );
        assertSame(result1, result2);
    }

    public void testCacheFilters() {
        String value1 = notStatic("blah.") + IGosuProgram.NAME_PREFIX + ".blah";
        String value2 = notStatic("blah.") + IGosuProgram.NAME_PREFIX + ".blah";
        assertNotSame(value1, value2);
        String result1 = StringPool.get( value1 );
        String result2 = StringPool.get( value2 );
        assertNotSame(result1, result2);
    }

    private String notStatic(String s) {
        return s;
    }
}
