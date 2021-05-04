/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import gw.gosudoc.com.sun.javadoc.ThrowsTag;

/**
 * Represents a @throws or @exception documentation tag.
 * Parses and holds the exception name and exception comment.
 * The exception name my be the name of a type variable.
 * Note: @exception is a backwards compatible synonymy for @throws.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 * @author Atul M Dambalkar
 * @see gw.gosudoc.com.sun.tools.javadoc.main.ExecutableMemberDocImpl#throwsTags()
 *
 */
@Deprecated
class ThrowsTagImpl extends gw.gosudoc.com.sun.tools.javadoc.main.TagImpl implements ThrowsTag
{

    private final String exceptionName;
    private final String exceptionComment;

    /**
     * Cached inline tags.
     */
    private gw.gosudoc.com.sun.javadoc.Tag[] inlineTags;

    ThrowsTagImpl( DocImpl holder, String name, String text) {
        super(holder, name, text);
        String[] sa = divideAtWhite();
        exceptionName = sa[0];
        exceptionComment = sa[1];
    }

    /**
     * Return the exception name.
     */
    public String exceptionName() {
        return exceptionName;
    }

    /**
     * Return the exception comment.
     */
    public String exceptionComment() {
        return exceptionComment;
    }

    /**
     * Return the exception as a ClassDocImpl.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc exception() {
        gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl exceptionClass;
        if (!(holder instanceof gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)) {
            exceptionClass = null;
        } else {
            ExecutableMemberDocImpl emd = (ExecutableMemberDocImpl)holder;
            gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl con = (ClassDocImpl)emd.containingClass();
            exceptionClass = (ClassDocImpl)con.findClass(exceptionName);
        }
        return exceptionClass;
    }

    /**
     * Return the type that represents the exception.
     * This may be a <code>ClassDoc</code> or a <code>TypeVariable</code>.
     */
    public gw.gosudoc.com.sun.javadoc.Type exceptionType() {
        //###(gj) TypeVariable not yet supported.
        return exception();
    }


    /**
     * Return the kind of this tag.  Always "@throws" for instances
     * of ThrowsTagImpl.
     */
    @Override
    public String kind() {
        return "@throws";
    }

    /**
     * For the exception comment with embedded @link tags return the array of
     * TagImpls consisting of SeeTagImpl(s) and text containing TagImpl(s).
     *
     * @return TagImpl[] Array of tags with inline SeeTagImpls.
     * @see gw.gosudoc.com.sun.tools.javadoc.main.TagImpl#inlineTags()
     * @see gw.gosudoc.com.sun.tools.javadoc.main.ParamTagImpl#inlineTags()
     */
    @Override
    public gw.gosudoc.com.sun.javadoc.Tag[] inlineTags() {
        if (inlineTags == null) {
            inlineTags = gw.gosudoc.com.sun.tools.javadoc.main.Comment.getInlineTags(holder, exceptionComment());
        }
        return inlineTags;
    }
}
