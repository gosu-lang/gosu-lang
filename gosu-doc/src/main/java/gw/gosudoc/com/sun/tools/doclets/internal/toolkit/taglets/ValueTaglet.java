/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;


/**
 * An inline Taglet representing the value tag. This tag should only be used with
 * constant fields that have a value.  It is used to access the value of constant
 * fields.  This inline tag has an optional field name parameter.  If the name is
 * specified, the constant value is retrieved from the specified field.  A link
 * is also created to the specified field.  If a name is not specified, the value
 * is retrieved for the field that the inline tag appears on.  The name is specifed
 * in the following format:  [fully qualified class name]#[constant field name].
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.4
 */

@Deprecated
public class ValueTaglet extends BaseInlineTaglet {

    /**
     * Construct a new ValueTaglet.
     */
    public ValueTaglet() {
        name = "value";
    }

    /**
     * Will return false because this inline tag may
     * only appear in Fields.
     * @return false since this is not a method.
     */
    public boolean inMethod() {
        return true;
    }

    /**
     * Will return false because this inline tag may
     * only appear in Fields.
     * @return false since this is not a method.
     */
    public boolean inConstructor() {
        return true;
    }

    /**
     * Will return false because this inline tag may
     * only appear in Fields.
     * @return false since this is not a method.
     */
    public boolean inOverview() {
        return true;
    }

    /**
     * Will return false because this inline tag may
     * only appear in Fields.
     * @return false since this is not a method.
     */
    public boolean inPackage() {
        return true;
    }

    /**
     * Will return false because this inline tag may
     * only appear in Fields.
     * @return false since this is not a method.
     */
    public boolean inType() {
        return true;
    }

    /**
     * Given the name of the field, return the corresponding FieldDoc. Return null
     * due to invalid use of value tag if the name is null or empty string and if
     * the value tag is not used on a field.
     *
     * @param config the current configuration of the doclet.
     * @param tag the value tag.
     * @param name the name of the field to search for.  The name should be in
     * {@code <qualified class name>#<field name>} format. If the class name is omitted,
     * it is assumed that the field is in the current class.
     *
     * @return the corresponding FieldDoc. If the name is null or empty string,
     * return field that the value tag was used in. Return null if the name is null
     * or empty string and if the value tag is not used on a field.
     */
    private gw.gosudoc.com.sun.javadoc.FieldDoc getFieldDoc( Configuration config, gw.gosudoc.com.sun.javadoc.Tag tag, String name) {
        if (name == null || name.length() == 0) {
            //Base case: no label.
            if (tag.holder() instanceof gw.gosudoc.com.sun.javadoc.FieldDoc ) {
                return (gw.gosudoc.com.sun.javadoc.FieldDoc) tag.holder();
            } else {
                // If the value tag does not specify a parameter which is a valid field and
                // it is not used within the comments of a valid field, return null.
                 return null;
            }
        }
        StringTokenizer st = new StringTokenizer(name, "#");
        String memberName = null;
        gw.gosudoc.com.sun.javadoc.ClassDoc cd = null;
        if (st.countTokens() == 1) {
            //Case 2:  @value in same class.
            gw.gosudoc.com.sun.javadoc.Doc holder = tag.holder();
            if (holder instanceof gw.gosudoc.com.sun.javadoc.MemberDoc ) {
                cd = ((gw.gosudoc.com.sun.javadoc.MemberDoc) holder).containingClass();
            } else if (holder instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
                cd = (gw.gosudoc.com.sun.javadoc.ClassDoc) holder;
            }
            memberName = st.nextToken();
        } else {
            //Case 3: @value in different class.
            cd = config.root.classNamed(st.nextToken());
            memberName = st.nextToken();
        }
        if (cd == null) {
            return null;
        }
        for ( gw.gosudoc.com.sun.javadoc.FieldDoc field : cd.fields()) {
            if (field.name().equals(memberName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( Tag tag, TagletWriter writer) {
        gw.gosudoc.com.sun.javadoc.FieldDoc field = getFieldDoc(
            writer.configuration(), tag, tag.text());
        if (field == null) {
            if (tag.text().isEmpty()) {
                //Invalid use of @value
                writer.getMsgRetriever().warning(tag.holder().position(),
                        "doclet.value_tag_invalid_use");
            } else {
                //Reference is unknown.
                writer.getMsgRetriever().warning(tag.holder().position(),
                        "doclet.value_tag_invalid_reference", tag.text());
            }
        } else if (field.constantValue() != null) {
            return writer.valueTagOutput(field,
                field.constantValueExpression(),
                ! field.equals(tag.holder()));
        } else {
            //Referenced field is not a constant.
            writer.getMsgRetriever().warning(tag.holder().position(),
                "doclet.value_tag_invalid_constant", field.name());
        }
        return writer.getOutputInstance();
    }
}
