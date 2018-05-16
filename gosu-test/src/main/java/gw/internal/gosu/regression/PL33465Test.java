package gw.internal.gosu.regression;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

public class PL33465Test extends TestClass {
    public void testTemplateParserTextEndsWithPound() {
        IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.regression.Errant_PL33465TestClass");
        assertFalse(clazz.isValid());
    }

}
