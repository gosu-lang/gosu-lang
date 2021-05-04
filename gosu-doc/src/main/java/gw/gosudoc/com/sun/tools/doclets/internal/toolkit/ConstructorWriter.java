/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit;

import java.io.*;

import gw.gosudoc.com.sun.javadoc.ConstructorDoc;

/**
 * The interface for writing constructor output.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 * @since 1.5
 */

@Deprecated
public interface ConstructorWriter {

    /**
     * Get the constructor details tree header.
     *
     * @param classDoc the class being documented
     * @param memberDetailsTree the content tree representing member details
     * @return content tree for the constructor details header
     */
    public Content getConstructorDetailsTreeHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                                    Content memberDetailsTree);

    /**
     * Get the constructor documentation tree header.
     *
     * @param constructor the constructor being documented
     * @param constructorDetailsTree the content tree representing constructor details
     * @return content tree for the constructor documentation header
     */
    public Content getConstructorDocTreeHeader( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor,
                                                Content constructorDetailsTree);

    /**
     * Get the signature for the given constructor.
     *
     * @param constructor the constructor being documented
     * @return content tree for the constructor signature
     */
    public Content getSignature( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor);

    /**
     * Add the deprecated output for the given constructor.
     *
     * @param constructor the constructor being documented
     * @param constructorDocTree content tree to which the deprecated information will be added
     */
    public void addDeprecated( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor, Content constructorDocTree);

    /**
     * Add the comments for the given constructor.
     *
     * @param constructor the constructor being documented
     * @param constructorDocTree the content tree to which the comments will be added
     */
    public void addComments( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor, Content constructorDocTree);

    /**
     * Add the tags for the given constructor.
     *
     * @param constructor the constructor being documented
     * @param constructorDocTree the content tree to which the tags will be added
     */
    public void addTags( ConstructorDoc constructor, Content constructorDocTree);

    /**
     * Get the constructor details tree.
     *
     * @param memberDetailsTree the content tree representing member details
     * @return content tree for the constructor details
     */
    public Content getConstructorDetails(Content memberDetailsTree);

    /**
     * Get the constructor documentation.
     *
     * @param constructorDocTree the content tree representing constructor documentation
     * @param isLastContent true if the content to be added is the last content
     * @return content tree for the constructor documentation
     */
    public Content getConstructorDoc(Content constructorDocTree, boolean isLastContent);

    /**
     * Let the writer know whether a non public constructor was found.
     *
     * @param foundNonPubConstructor true if we found a non public constructor.
     */
    public void setFoundNonPubConstructor(boolean foundNonPubConstructor);

    /**
     * Close the writer.
     */
    public void close() throws IOException;
}
