/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.javadoc;

/**
 * This interface provides error, warning and notice printing.
 *
 * @since 1.2
 * @author Robert Field
 *
 * @deprecated
 *   The declarations in this package have been superseded by those
 *   in the package {@code jdk.javadoc.doclet}.
 *   For more information, see the <i>Migration Guide</i> in the documentation for that package.
 */
@Deprecated
public interface DocErrorReporter {

    /**
     * Print error message and increment error count.
     *
     * @param msg message to print
     */
    void printError(String msg);

    /**
     * Print an error message and increment error count.
     *
     * @param pos the position item where the error occurs
     * @param msg message to print
     * @since 1.4
     */
    void printError( gw.gosudoc.com.sun.javadoc.SourcePosition pos, String msg);

    /**
     * Print warning message and increment warning count.
     *
     * @param msg message to print
     */
    void printWarning(String msg);

    /**
     * Print warning message and increment warning count.
     *
     * @param pos the position item where the warning occurs
     * @param msg message to print
     * @since 1.4
     */
    void printWarning( gw.gosudoc.com.sun.javadoc.SourcePosition pos, String msg);

    /**
     * Print a message.
     *
     * @param msg message to print
     */
    void printNotice(String msg);

    /**
     * Print a message.
     *
     * @param pos the position item where the message occurs
     * @param msg message to print
     * @since 1.4
     */
    void printNotice( SourcePosition pos, String msg);
}