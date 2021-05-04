/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html.markup;

import java.io.IOException;
import java.io.Writer;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletConstants;

/**
 * Class for generating a comment for HTML pages of javadoc output.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Bhavesh Patel
 */
@Deprecated
public class Comment extends Content {

    private String commentText;

    /**
     * Constructor to construct a Comment object.
     *
     * @param comment comment text for the comment
     */
    public Comment(String comment) {
        commentText = nullCheck(comment);
    }

    /**
     * This method is not supported by the class.
     *
     * @param content content that needs to be added
     * @throws DocletAbortException this method will always throw a
     *                              DocletAbortException because it
     *                              is not supported.
     */
    public void addContent(Content content) {
        throw new DocletAbortException("not supported");
    }

    /**
     * This method is not supported by the class.
     *
     * @param stringContent string content that needs to be added
     * @throws DocletAbortException this method will always throw a
     *                              DocletAbortException because it
     *                              is not supported.
     */
    public void addContent(String stringContent) {
        throw new DocletAbortException("not supported");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return commentText.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean write(Writer out, boolean atNewline) throws IOException {
        if (!atNewline)
            out.write( DocletConstants.NL);
        out.write("<!-- ");
        out.write(commentText);
        out.write(" -->" + DocletConstants.NL);
        return true;
    }
}
