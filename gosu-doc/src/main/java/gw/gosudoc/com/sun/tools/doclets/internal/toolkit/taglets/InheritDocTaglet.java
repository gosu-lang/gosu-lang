/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * An inline Taglet representing the <b>inheritDoc</b> tag. This tag should only
 * be used with a method.  It is used to inherit documentation from overriden
 * and implemented methods.
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
public class InheritDocTaglet extends BaseInlineTaglet {

    /**
     * The inline tag that would appear in the documentation if
     * the writer wanted documentation to be inherited.
     */
    public static final String INHERIT_DOC_INLINE_TAG = "{@inheritDoc}";

    /**
     * Construct a new InheritDocTaglet.
     */
    public InheritDocTaglet () {
        name = "inheritDoc";
    }

    /**
     * Will return false because this inline tag may
     * not appear in Fields.
     * @return false
     */
    public boolean inField() {
        return false;
    }

    /**
     * Will return false because this inline tag may
     * not appear in Constructors.
     * @return false
     */
    public boolean inConstructor() {
        return false;
    }

    /**
     * Will return false because this inline tag may
     * not appear in Overview.
     * @return false
     */
    public boolean inOverview() {
        return false;
    }

    /**
     * Will return false because this inline tag may
     * not appear in Packages.
     * @return false
     */
    public boolean inPackage() {
        return false;
    }

    /**
     * Will return true because this inline tag may
     * appear in Type (Class).
     * @return true
     */
    public boolean inType() {
        return true;
    }

    /**
     * Given a <code>MethodDoc</code> item, a <code>Tag</code> in the
     * <code>MethodDoc</code> item and a String, replace all occurrences
     * of @inheritDoc with documentation from it's superclass or superinterface.
     *
     * @param writer the writer that is writing the output.
     * @param ped the {@link gw.gosudoc.com.sun.javadoc.ProgramElementDoc} that we are documenting.
     * @param holderTag the tag that holds the inheritDoc tag or null for type
     * (class) docs.
     * @param isFirstSentence true if we only want to inherit the first sentence.
     */
    private Content retrieveInheritedDocumentation( TagletWriter writer,
                                                    gw.gosudoc.com.sun.javadoc.ProgramElementDoc ped, gw.gosudoc.com.sun.javadoc.Tag holderTag, boolean isFirstSentence) {
        Content replacement = writer.getOutputInstance();

        Configuration configuration = writer.configuration();
        Taglet inheritableTaglet = holderTag == null ?
            null : configuration.tagletManager.getTaglet(holderTag.name());
        if (inheritableTaglet != null &&
            !(inheritableTaglet instanceof InheritableTaglet)) {
                String message = ped.name() +
                    ((ped instanceof gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)
                        ? ((gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)ped).flatSignature()
                        : "");
                //This tag does not support inheritence.
                configuration.message.warning(ped.position(),
                "doclet.noInheritedDoc", message);
         }
        DocFinder.Output inheritedDoc =
            DocFinder.search(configuration, new DocFinder.Input(ped,
                (InheritableTaglet) inheritableTaglet, holderTag,
                isFirstSentence, true));
        if (inheritedDoc.isValidInheritDocTag) {
            if (inheritedDoc.inlineTags.length > 0) {
                replacement = writer.commentTagsToOutput(inheritedDoc.holderTag,
                    inheritedDoc.holder, inheritedDoc.inlineTags, isFirstSentence);
            }
        } else {
            String message = ped.name() +
                    ((ped instanceof gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)
                        ? ((gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)ped).flatSignature()
                        : "");
            configuration.message.warning(ped.position(),
                "doclet.noInheritedDoc", message);
        }
        return replacement;
    }

    /**
     * Given the <code>Tag</code> representation of this custom
     * tag, return its string representation, which is output
     * to the generated page.
     * @param tag the <code>Tag</code> representation of this custom tag.
     * @param tagletWriter the taglet writer for output.
     * @return the Content representation of this <code>Tag</code>.
     */
    public Content getTagletOutput( Tag tag, TagletWriter tagletWriter) {
        if (! (tag.holder() instanceof gw.gosudoc.com.sun.javadoc.ProgramElementDoc)) {
            return tagletWriter.getOutputInstance();
        }
        return tag.name().equals("@inheritDoc") ?
                retrieveInheritedDocumentation(tagletWriter, (gw.gosudoc.com.sun.javadoc.ProgramElementDoc) tag.holder(), null, tagletWriter.isFirstSentence) :
                retrieveInheritedDocumentation(tagletWriter, (gw.gosudoc.com.sun.javadoc.ProgramElementDoc) tag.holder(), tag, tagletWriter.isFirstSentence);
    }
}
