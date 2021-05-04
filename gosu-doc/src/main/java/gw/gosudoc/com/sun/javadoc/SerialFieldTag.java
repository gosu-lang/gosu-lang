/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.javadoc;

/**
 * Documents a Serializable field defined by an ObjectStreamField.
 * <pre>
 * The class parses and stores the three serialField tag parameters:
 *
 * - field name
 * - field type name
 *      (fully-qualified or visible from the current import context)
 * - description of the valid values for the field

 * </pre>
 * This tag is only allowed in the javadoc for the special member
 * serialPersistentFields.
 *
 * @author Joe Fialli
 *
 * @see java.io.ObjectStreamField
 *
 * @deprecated
 *   The declarations in this package have been superseded by those
 *   in the package {@code jdk.javadoc.doclet}.
 *   For more information, see the <i>Migration Guide</i> in the documentation for that package.
 */
@Deprecated
public interface SerialFieldTag extends Tag, Comparable<Object> {

    /**
     * Return the serializable field name.
     *
     * @return the serializable field name.
     */
    public String fieldName();

    /**
     * Return the field type string.
     *
     * @return the field type string.
     */
    public String fieldType();

    /**
     * Return the ClassDoc for field type.
     *
     * @return null if no ClassDoc for field type is visible from
     *         containingClass context.
     */
    public ClassDoc fieldTypeDoc();

    /**
     * Return the field comment. If there is no serialField comment, return
     * javadoc comment of corresponding FieldDoc.
     *
     * @return the field comment. If there is no serialField comment, return
     *         javadoc comment of corresponding FieldDoc.
     */
    public String description();

    /**
     * Compares this Object with the specified Object for order.  Returns a
     * negative integer, zero, or a positive integer as this Object is less
     * than, equal to, or greater than the given Object.
     * <p>
     * Included to make SerialFieldTag items java.lang.Comparable.
     *
     * @param   obj the {@code Object} to be compared.
     * @return  a negative integer, zero, or a positive integer as this Object
     *          is less than, equal to, or greater than the given Object.
     * @exception ClassCastException the specified Object's type prevents it
     *            from being compared to this Object.
     * @since 1.2
     */
    public int compareTo(Object obj);
}
