/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

/**
 * !!DEPRECATED!!
 * <p>
 * Warning. An impl of this interface is <i>not</i> a real Java annotation.
 * This is simply a marker interface to indicate that a normal class serves as
 * a quasi-annotation; it can be used to annotate Gosu features wherever a Java
 * annotation can be used. However, because it is a normal class and does not
 * conform with the rules and structure of a Java Annotation, it is only bound
 * to its target feature in terms of Gosu <i>TypeInfo</i> -- it is <i>never</i>
 * compiled into the target feature's bytecode as an Annotation. But because
 * Gosu source and source-based TypeInfo are available at runtime, an
 * IAnnotation is also available at runtime.  As a consequence an IAnnotation
 * has no use for the @Retention modifier, it is always retained.
 * <p>
 * A word of caution. An IAnnotation type is just a normal class that isn't
 * bound to the strict compile-time constant limitations of a conventional Java
 * Annotation. An IAnnotation can be constructed outside the normal execution
 * environment of your project or application, say, in an IDE. Both the
 * IAnnotation implementation and the users of it must take care not to execute
 * code intended for runtime and avoid loading classes that are directly part of
 * a project's source. Otherwise compilation performance can be significantly
 * affected and/or result in nasty compile-time errors.
 * <p>
 * Note, this class remains part of the Gosu API for legacy purposes only.
 * Gosu's Java-compatible 'annotation' types should be implemented instead.
 *
 * @deprecated Implement a standard Gosu 'annotation' class
 */
public interface IAnnotation {
}
