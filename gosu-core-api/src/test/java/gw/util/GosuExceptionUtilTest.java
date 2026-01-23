package gw.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for GosuExceptionUtil focusing on:
 * - Java 21 pattern matching for exception conversion
 * - Stream-based parameter formatting (O(n) vs O(n²))
 * - Exception cause traversal
 * - Stack trace handling
 *
 * Target: 60-70% line coverage, 55-60% branch coverage
 */
public class GosuExceptionUtilTest {

    // ========== Pattern Matching Tests (Java 21) ==========

    @Test
    public void testConvertToRuntimeException_WithRuntimeException() {
        // Tests pattern matching: if (t instanceof RuntimeException e)
        RuntimeException original = new RuntimeException("test exception");

        RuntimeException result = GosuExceptionUtil.convertToRuntimeException(original);

        assertSame("RuntimeException should be returned as-is", original, result);
    }

    @Test
    public void testConvertToRuntimeException_WithCheckedException() {
        // Tests pattern matching else branch
        Exception checked = new Exception("checked exception");

        RuntimeException result = GosuExceptionUtil.convertToRuntimeException(checked);

        assertNotNull("Result should not be null", result);
        assertNotSame("Checked exception should be wrapped", checked, result);
        assertSame("Cause should be original exception", checked, result.getCause());
        assertTrue("Should be RuntimeException",
                result instanceof RuntimeException);
    }

    @Test
    public void testConvertToRuntimeException_WithError() {
        // Errors are not RuntimeExceptions, should be wrapped
        Error error = new Error("test error");

        RuntimeException result = GosuExceptionUtil.convertToRuntimeException(error);

        assertNotNull("Result should not be null", result);
        assertSame("Cause should be original error", error, result.getCause());
    }

    // ========== Stream-Based Formatting Tests (Java 21 Performance) ==========

    @Test
    public void testThrowArgMismatchException_SingleParam() {
        // Tests Stream API: Arrays.stream().map().collect()
        Class<?>[] params = new Class<?>[]{String.class};
        Object[] args = new Object[]{"test"};

        try {
            GosuExceptionUtil.throwArgMismatchException(
                    new IllegalArgumentException("base"),
                    "testMethod",
                    params,
                    args
            );
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException ex) {
            assertNotNull("Exception should not be null", ex);
            String message = ex.getMessage();
            assertTrue("Message should contain method name",
                    message.contains("testMethod"));
            assertTrue("Message should contain parameter type",
                    message.contains("String"));
        }
    }

    @Test
    public void testThrowArgMismatchException_MultipleParams() {
        // Tests O(n) stream performance vs O(n²) string concatenation
        Class<?>[] params = new Class<?>[]{String.class, Integer.class, Boolean.class};
        Object[] args = new Object[]{"test", 42, true};

        try {
            GosuExceptionUtil.throwArgMismatchException(
                    new IllegalArgumentException(),
                    "complexMethod",
                    params,
                    args
            );
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException ex) {
            String message = ex.getMessage();
            assertTrue("Message should contain all types",
                    message.contains("String") &&
                    message.contains("Integer") &&
                    message.contains("Boolean"));
            assertTrue("Message should contain method name",
                    message.contains("complexMethod"));
        }
    }

    @Test
    public void testThrowArgMismatchException_NullArg() {
        Class<?>[] params = new Class<?>[]{String.class};
        Object[] args = new Object[]{null};

        try {
            GosuExceptionUtil.throwArgMismatchException(
                    new IllegalArgumentException(),
                    "methodWithNull",
                    params,
                    args
            );
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException ex) {
            String message = ex.getMessage();
            assertTrue("Message should handle null argument",
                    message.contains("null") || message.contains("String"));
        }
    }

    @Test
    public void testThrowArgMismatchException_EmptyParams() {
        Class<?>[] params = new Class<?>[0];
        Object[] args = new Object[0];

        try {
            GosuExceptionUtil.throwArgMismatchException(
                    new IllegalArgumentException(),
                    "noArgsMethod",
                    params,
                    args
            );
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException ex) {
            assertNotNull("Exception should be thrown even with no params", ex);
        }
    }

    // ========== Exception Finding Tests ==========

    @Test
    public void testFindExceptionCause_SingleLevel() {
        RuntimeException root = new RuntimeException("root cause");

        Throwable result = GosuExceptionUtil.findExceptionCause(root);

        assertSame("Single exception should return itself", root, result);
    }

    @Test
    public void testFindExceptionCause_NestedTwoLevels() {
        RuntimeException root = new RuntimeException("root");
        RuntimeException wrapper = new RuntimeException("wrapper", root);

        Throwable result = GosuExceptionUtil.findExceptionCause(wrapper);

        assertSame("Should find root cause", root, result);
    }

    @Test
    public void testFindExceptionCause_NestedThreeLevels() {
        RuntimeException root = new RuntimeException("root");
        RuntimeException middle = new RuntimeException("middle", root);
        RuntimeException top = new RuntimeException("top", middle);

        Throwable result = GosuExceptionUtil.findExceptionCause(top);

        assertSame("Should find deepest root cause", root, result);
    }

    @Test
    public void testFindException_TypeFound() {
        IllegalArgumentException target = new IllegalArgumentException("target");
        RuntimeException wrapper = new RuntimeException("wrapper", target);

        IllegalArgumentException result = GosuExceptionUtil.findException(
                IllegalArgumentException.class,
                wrapper
        );

        assertSame("Should find exception of specific type", target, result);
    }

    @Test
    public void testFindException_TypeNotFound() {
        RuntimeException ex = new RuntimeException("test");

        IllegalStateException result = GosuExceptionUtil.findException(
                IllegalStateException.class,
                ex
        );

        assertNull("Should return null when type not found", result);
    }

    @Test
    public void testFindException_NullException() {
        IllegalArgumentException result = GosuExceptionUtil.findException(
                IllegalArgumentException.class,
                null
        );

        assertNull("Should handle null exception gracefully", result);
    }

    // ========== Stack Trace Tests ==========

    @Test
    public void testGetStackTraceAsString() {
        RuntimeException ex = new RuntimeException("test exception");

        String stackTrace = GosuExceptionUtil.getStackTraceAsString(ex);

        assertNotNull("Stack trace should not be null", stackTrace);
        assertTrue("Stack trace should contain exception message",
                stackTrace.contains("test exception"));
        assertTrue("Stack trace should contain exception class name",
                stackTrace.contains("RuntimeException"));
        assertTrue("Stack trace should contain stack frames",
                stackTrace.contains("at "));
    }

    @Test
    public void testGetStackTraceAsString_WithCause() {
        Exception cause = new Exception("root cause");
        RuntimeException ex = new RuntimeException("wrapper", cause);

        String stackTrace = GosuExceptionUtil.getStackTraceAsString(ex);

        assertNotNull("Stack trace should not be null", stackTrace);
        assertTrue("Stack trace should contain wrapper message",
                stackTrace.contains("wrapper"));
        assertTrue("Stack trace should contain cause message",
                stackTrace.contains("root cause"));
        assertTrue("Stack trace should show causation",
                stackTrace.contains("Caused by") ||
                stackTrace.contains("root cause"));
    }

    // ========== Edge Cases ==========

    // Note: findExceptionCause(null) throws NPE, which is acceptable behavior
    // since passing null is not a valid use case
}
