/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.ThrowsTag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * A taglet that represents the @throws tag.
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
public class ThrowsTaglet extends BaseExecutableMemberTaglet
    implements InheritableTaglet {

    public ThrowsTaglet() {
        name = "throws";
    }

    /**
     * {@inheritDoc}
     */
    public void inherit( DocFinder.Input input, DocFinder.Output output) {
        gw.gosudoc.com.sun.javadoc.ClassDoc exception;
        if (input.tagId == null) {
            gw.gosudoc.com.sun.javadoc.ThrowsTag throwsTag = (gw.gosudoc.com.sun.javadoc.ThrowsTag) input.tag;
            exception = throwsTag.exception();
            input.tagId = exception == null ?
                throwsTag.exceptionName() :
                throwsTag.exception().qualifiedName();
        } else {
            exception = input.element.containingClass().findClass(input.tagId);
        }

        for ( gw.gosudoc.com.sun.javadoc.ThrowsTag tag : ((gw.gosudoc.com.sun.javadoc.MethodDoc)input.element).throwsTags()) {
            if (input.tagId.equals(tag.exceptionName()) ||
                (tag.exception() != null &&
                 (input.tagId.equals(tag.exception().qualifiedName())))) {
                output.holder = input.element;
                output.holderTag = tag;
                output.inlineTags = input.isFirstSentence ?
                                    tag.firstSentenceTags() : tag.inlineTags();
                output.tagList.add(tag);
            } else if (exception != null && tag.exception() != null &&
                     tag.exception().subclassOf(exception)) {
                output.tagList.add(tag);
            }
        }
    }

    /**
     * Add links for exceptions that are declared but not documented.
     */
    private Content linkToUndocumentedDeclaredExceptions(
      gw.gosudoc.com.sun.javadoc.Type[] declaredExceptionTypes, Set<String> alreadyDocumented,
      TagletWriter writer) {
        Content result = writer.getOutputInstance();
        //Add links to the exceptions declared but not documented.
        for ( gw.gosudoc.com.sun.javadoc.Type declaredExceptionType : declaredExceptionTypes) {
            if (declaredExceptionType.asClassDoc() != null &&
                !alreadyDocumented.contains(
                        declaredExceptionType.asClassDoc().name()) &&
                !alreadyDocumented.contains(
                        declaredExceptionType.asClassDoc().qualifiedName())) {
                if (alreadyDocumented.size() == 0) {
                    result.addContent(writer.getThrowsHeader());
                }
                result.addContent(writer.throwsTagOutput(declaredExceptionType));
                alreadyDocumented.add(declaredExceptionType.asClassDoc().name());
            }
        }
        return result;
    }

    /**
     * Inherit throws documentation for exceptions that were declared but not
     * documented.
     */
    private Content inheritThrowsDocumentation( gw.gosudoc.com.sun.javadoc.Doc holder,
                                                gw.gosudoc.com.sun.javadoc.Type[] declaredExceptionTypes, Set<String> alreadyDocumented,
                                                TagletWriter writer) {
        Content result = writer.getOutputInstance();
        if (holder instanceof gw.gosudoc.com.sun.javadoc.MethodDoc ) {
            Set<gw.gosudoc.com.sun.javadoc.Tag> declaredExceptionTags = new LinkedHashSet<>();
            for ( gw.gosudoc.com.sun.javadoc.Type declaredExceptionType : declaredExceptionTypes) {
                DocFinder.Output inheritedDoc =
                        DocFinder.search(writer.configuration(), new DocFinder.Input((gw.gosudoc.com.sun.javadoc.MethodDoc) holder, this,
                                                             declaredExceptionType.typeName()));
                if (inheritedDoc.tagList.size() == 0) {
                    inheritedDoc = DocFinder.search(writer.configuration(), new DocFinder.Input(
                            (gw.gosudoc.com.sun.javadoc.MethodDoc) holder, this,
                            declaredExceptionType.qualifiedTypeName()));
                }
                declaredExceptionTags.addAll(inheritedDoc.tagList);
            }
            result.addContent(throwsTagsOutput(
                declaredExceptionTags.toArray(new gw.gosudoc.com.sun.javadoc.ThrowsTag[] {}),
                writer, alreadyDocumented, false));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Doc holder, TagletWriter writer) {
        gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc execHolder = (gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc) holder;
        gw.gosudoc.com.sun.javadoc.ThrowsTag[] tags = execHolder.throwsTags();
        Content result = writer.getOutputInstance();
        HashSet<String> alreadyDocumented = new HashSet<>();
        if (tags.length > 0) {
            result.addContent(throwsTagsOutput(
                execHolder.throwsTags(), writer, alreadyDocumented, true));
        }
        result.addContent(inheritThrowsDocumentation(holder,
            execHolder.thrownExceptionTypes(), alreadyDocumented, writer));
        result.addContent(linkToUndocumentedDeclaredExceptions(
            execHolder.thrownExceptionTypes(), alreadyDocumented, writer));
        return result;
    }


    /**
     * Given an array of <code>Tag</code>s representing this custom
     * tag, return its string representation.
     * @param throwTags the array of <code>ThrowsTag</code>s to convert.
     * @param writer the TagletWriter that will write this tag.
     * @param alreadyDocumented the set of exceptions that have already
     *        been documented.
     * @param allowDups True if we allow duplicate throws tags to be documented.
     * @return the Content representation of this <code>Tag</code>.
     */
    protected Content throwsTagsOutput( gw.gosudoc.com.sun.javadoc.ThrowsTag[] throwTags,
                                        TagletWriter writer, Set<String> alreadyDocumented, boolean allowDups) {
        Content result = writer.getOutputInstance();
        if (throwTags.length > 0) {
            for ( ThrowsTag tt : throwTags) {
                gw.gosudoc.com.sun.javadoc.ClassDoc cd = tt.exception();
                if ((!allowDups) && (alreadyDocumented.contains(tt.exceptionName()) ||
                                     (cd != null && alreadyDocumented.contains(cd.qualifiedName())))) {
                    continue;
                }
                if (alreadyDocumented.size() == 0) {
                    result.addContent(writer.getThrowsHeader());
                }
                result.addContent(writer.throwsTagOutput(tt));
                alreadyDocumented.add(cd != null ?
                                      cd.qualifiedName() : tt.exceptionName());
            }
        }
        return result;
    }
}
