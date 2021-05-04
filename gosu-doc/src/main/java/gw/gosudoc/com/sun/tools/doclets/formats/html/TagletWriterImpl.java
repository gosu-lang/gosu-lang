/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import gw.gosudoc.com.sun.javadoc.ThrowsTag;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.ContentBuilder;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlStyle;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlTree;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.RawHtml;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.StringContent;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders.SerializedFormBuilder;


import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.*;

/**
 * The taglet writer that writes HTML.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.5
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 */

@Deprecated
public class TagletWriterImpl extends TagletWriter
{

    private final HtmlDocletWriter htmlWriter;
    private final ConfigurationImpl configuration;
    private final Utils utils;

    public TagletWriterImpl(HtmlDocletWriter htmlWriter, boolean isFirstSentence) {
        super(isFirstSentence);
        this.htmlWriter = htmlWriter;
        configuration = htmlWriter.configuration;
        this.utils = configuration.utils;
    }

    /**
     * {@inheritDoc}
     */
    public Content getOutputInstance() {
        return new ContentBuilder();
    }

    /**
     * {@inheritDoc}
     */
    protected Content codeTagOutput( gw.gosudoc.com.sun.javadoc.Tag tag) {
        Content result = HtmlTree.CODE(new StringContent(utils.normalizeNewlines(tag.text())));
        return result;
    }

    protected Content indexTagOutput( gw.gosudoc.com.sun.javadoc.Tag tag) {
        String text = tag.text();
        String tagText = "";
        String desc = "";
        if (text.isEmpty() || text.trim().isEmpty()) {
            configuration.message.warning(tag.position(), "doclet.invalid_usage_of_tag", tag.name());
        } else {
            int len = text.length();
            int tagTextEnd = 0;
            int descstart = 0;
            int start = 0;
            Character term = ' ';
            int cp = text.codePointAt(0);
            if (cp == '"') {
                term = '"';
                start++;
            }
            for (int i = start; i < len; i += Character.charCount(cp)) {
                cp = text.codePointAt(i);
                if (cp == term) {
                    tagTextEnd = i;
                    break;
                }
            }
            if (tagTextEnd < len - 1 && tagTextEnd != 0) {
                descstart = tagTextEnd + 1;
            }
            String desctext = "";
            if (descstart > 0) {
                tagText = text.substring(start, tagTextEnd).trim();
                desctext = text.substring(descstart, len).trim();
                // strip off the white space which can be between tag description and the
                // actual label.
                for (int i = 0; i < desctext.length(); i++) {
                    char ch2 = desctext.charAt(i);
                    if (!(ch2 == ' ' || ch2 == '\t' || ch2 == '\n')) {
                        desc = desctext.substring(i);
                        break;
                    }
                }
            } else {
                if (term == '"') {
                    if (tagTextEnd == 0) {
                        // If unclosed quote, print out a warning and ignore the invalid tag text.
                        configuration.message.warning(tag.position(), "doclet.invalid_usage_of_tag", tag.name());
                        tagText = "";
                    } else {
                        tagText = text.substring(start, tagTextEnd).trim();
                    }
                } else {
                    tagText = text.trim();
                }
                desc = "";
            }
        }
        String anchorName = htmlWriter.getName(tagText);
        Content result = HtmlTree.A_ID(anchorName, new StringContent(tagText));
        if (configuration.createindex && !tagText.isEmpty()) {
            SearchIndexItem si = new SearchIndexItem();
            si.setLabel(tagText);
            si.setDescription(desc);
            if (tag.holder() instanceof gw.gosudoc.com.sun.javadoc.ProgramElementDoc ) {
                if (tag.holder() instanceof gw.gosudoc.com.sun.javadoc.MemberDoc ) {
                    si.setUrl( DocPath.forClass(((gw.gosudoc.com.sun.javadoc.MemberDoc) tag.holder()).containingClass()).getPath()
                            + "#" + anchorName);
                    si.setHolder(((gw.gosudoc.com.sun.javadoc.MemberDoc) tag.holder()).qualifiedName());
                } else {
                    si.setUrl(DocPath.forClass((gw.gosudoc.com.sun.javadoc.ClassDoc) tag.holder()).getPath() + "#" + anchorName);
                    si.setHolder(((gw.gosudoc.com.sun.javadoc.ClassDoc) tag.holder()).qualifiedName());
                }
            } else if (tag.holder() instanceof gw.gosudoc.com.sun.javadoc.PackageDoc ) {
                si.setUrl(DocPath.forPackage((gw.gosudoc.com.sun.javadoc.PackageDoc) tag.holder()).getPath()
                        + "/" + DocPaths.PACKAGE_SUMMARY.getPath() + "#" + anchorName);
                si.setHolder(((gw.gosudoc.com.sun.javadoc.PackageDoc) tag.holder()).name());
            }
            si.setCategory(configuration.getResource("doclet.SearchTags").toString());
            configuration.tagSearchIndex.add(si);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content getDocRootOutput() {
        String path;
        if (htmlWriter.pathToRoot.isEmpty())
            path = ".";
        else
            path = htmlWriter.pathToRoot.getPath();
        return new StringContent(path);
    }

    /**
     * {@inheritDoc}
     */
    public Content deprecatedTagOutput( gw.gosudoc.com.sun.javadoc.Doc doc) {
        ContentBuilder result = new ContentBuilder();
        gw.gosudoc.com.sun.javadoc.Tag[] deprs = doc.tags("deprecated");
        if (doc instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
            if (utils.isDeprecated((gw.gosudoc.com.sun.javadoc.ProgramElementDoc) doc)) {
                result.addContent(HtmlTree.SPAN( HtmlStyle.deprecatedLabel,
                        new StringContent(configuration.getText("doclet.Deprecated"))));
                result.addContent( RawHtml.nbsp);
                if (deprs.length > 0) {
                    gw.gosudoc.com.sun.javadoc.Tag[] commentTags = deprs[0].inlineTags();
                    if (commentTags.length > 0) {
                        result.addContent(commentTagsToOutput(null, doc,
                            deprs[0].inlineTags(), false)
                        );
                    }
                }
            }
        } else {
            gw.gosudoc.com.sun.javadoc.MemberDoc member = (gw.gosudoc.com.sun.javadoc.MemberDoc) doc;
            if (utils.isDeprecated((gw.gosudoc.com.sun.javadoc.ProgramElementDoc) doc)) {
                result.addContent(HtmlTree.SPAN(HtmlStyle.deprecatedLabel,
                        new StringContent(configuration.getText("doclet.Deprecated"))));
                result.addContent(RawHtml.nbsp);
                if (deprs.length > 0) {
                    Content body = commentTagsToOutput(null, doc,
                        deprs[0].inlineTags(), false);
                    if (!body.isEmpty())
                        result.addContent(HtmlTree.SPAN(HtmlStyle.deprecationComment, body));
                }
            } else {
                if (utils.isDeprecated(member.containingClass())) {
                    result.addContent(HtmlTree.SPAN(HtmlStyle.deprecatedLabel,
                            new StringContent(configuration.getText("doclet.Deprecated"))));
                    result.addContent(RawHtml.nbsp);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    protected Content literalTagOutput( gw.gosudoc.com.sun.javadoc.Tag tag) {
        Content result = new StringContent(utils.normalizeNewlines(tag.text()));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MessageRetriever getMsgRetriever() {
        return configuration.message;
    }

    /**
     * {@inheritDoc}
     */
    public Content getParamHeader(String header) {
        HtmlTree result = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.paramLabel,
                new StringContent(header)));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content paramTagOutput( gw.gosudoc.com.sun.javadoc.ParamTag paramTag, String paramName) {
        ContentBuilder body = new ContentBuilder();
        body.addContent(HtmlTree.CODE(new RawHtml(paramName)));
        body.addContent(" - ");
        body.addContent(htmlWriter.commentTagsToContent(paramTag, null, paramTag.inlineTags(), false));
        HtmlTree result = HtmlTree.DD(body);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content propertyTagOutput( gw.gosudoc.com.sun.javadoc.Tag tag, String prefix) {
        Content body = new ContentBuilder();
        body.addContent(new RawHtml(prefix));
        body.addContent(" ");
        body.addContent(HtmlTree.CODE(new RawHtml(tag.text())));
        body.addContent(".");
        Content result = HtmlTree.P(body);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content returnTagOutput( gw.gosudoc.com.sun.javadoc.Tag returnTag) {
        ContentBuilder result = new ContentBuilder();
        result.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.returnLabel,
                new StringContent(configuration.getText("doclet.Returns")))));
        result.addContent(HtmlTree.DD(htmlWriter.commentTagsToContent(
                returnTag, null, returnTag.inlineTags(), false)));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content seeTagOutput( gw.gosudoc.com.sun.javadoc.Doc holder, gw.gosudoc.com.sun.javadoc.SeeTag[] seeTags) {
        ContentBuilder body = new ContentBuilder();
        if (seeTags.length > 0) {
            for ( gw.gosudoc.com.sun.javadoc.SeeTag seeTag : seeTags) {
                appendSeparatorIfNotEmpty(body);
                body.addContent(htmlWriter.seeTagToContent(seeTag));
            }
        }
        if (holder.isField() && ((gw.gosudoc.com.sun.javadoc.FieldDoc)holder).constantValue() != null &&
                htmlWriter instanceof ClassWriterImpl) {
            //Automatically add link to constant values page for constant fields.
            appendSeparatorIfNotEmpty(body);
            DocPath constantsPath =
                    htmlWriter.pathToRoot.resolve(DocPaths.CONSTANT_VALUES);
            String whichConstant =
                    ((ClassWriterImpl) htmlWriter).getClassDoc().qualifiedName() + "." + ((gw.gosudoc.com.sun.javadoc.FieldDoc) holder).name();
            DocLink link = constantsPath.fragment(whichConstant);
            body.addContent(htmlWriter.getHyperLink(link,
                    new StringContent(configuration.getText("doclet.Constants_Summary"))));
        }
        if (holder.isClass() && ((gw.gosudoc.com.sun.javadoc.ClassDoc)holder).isSerializable()) {
            //Automatically add link to serialized form page for serializable classes.
            if ((SerializedFormBuilder.serialInclude(holder) &&
                      SerializedFormBuilder.serialInclude(((gw.gosudoc.com.sun.javadoc.ClassDoc)holder).containingPackage()))) {
                appendSeparatorIfNotEmpty(body);
                DocPath serialPath = htmlWriter.pathToRoot.resolve(DocPaths.SERIALIZED_FORM);
                DocLink link = serialPath.fragment(((gw.gosudoc.com.sun.javadoc.ClassDoc)holder).qualifiedName());
                body.addContent(htmlWriter.getHyperLink(link,
                        new StringContent(configuration.getText("doclet.Serialized_Form"))));
            }
        }
        if (body.isEmpty())
            return body;

        ContentBuilder result = new ContentBuilder();
        result.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.seeLabel,
                new StringContent(configuration.getText("doclet.See_Also")))));
        result.addContent(HtmlTree.DD(body));
        return result;

    }

    private void appendSeparatorIfNotEmpty(ContentBuilder body) {
        if (!body.isEmpty()) {
            body.addContent(", ");
            body.addContent( DocletConstants.NL);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Content simpleTagOutput( gw.gosudoc.com.sun.javadoc.Tag[] simpleTags, String header) {
        ContentBuilder result = new ContentBuilder();
        result.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.simpleTagLabel, new RawHtml(header))));
        ContentBuilder body = new ContentBuilder();
        for (int i = 0; i < simpleTags.length; i++) {
            if (i > 0) {
                body.addContent(", ");
            }
            body.addContent(htmlWriter.commentTagsToContent(
                    simpleTags[i], null, simpleTags[i].inlineTags(), false));
        }
        result.addContent(HtmlTree.DD(body));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content simpleTagOutput( gw.gosudoc.com.sun.javadoc.Tag simpleTag, String header) {
        ContentBuilder result = new ContentBuilder();
        result.addContent(HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.simpleTagLabel, new RawHtml(header))));
        Content body = htmlWriter.commentTagsToContent(
                simpleTag, null, simpleTag.inlineTags(), false);
        result.addContent(HtmlTree.DD(body));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content getThrowsHeader() {
        HtmlTree result = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.throwsLabel,
                new StringContent(configuration.getText("doclet.Throws"))));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content throwsTagOutput( ThrowsTag throwsTag) {
        ContentBuilder body = new ContentBuilder();
        Content excName = (throwsTag.exceptionType() == null) ?
                new RawHtml(throwsTag.exceptionName()) :
                htmlWriter.getLink(new LinkInfoImpl(configuration, LinkInfoImpl.Kind.MEMBER,
                throwsTag.exceptionType()));
        body.addContent(HtmlTree.CODE(excName));
        Content desc = htmlWriter.commentTagsToContent(throwsTag, null,
            throwsTag.inlineTags(), false);
        if (desc != null && !desc.isEmpty()) {
            body.addContent(" - ");
            body.addContent(desc);
        }
        HtmlTree result = HtmlTree.DD(body);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content throwsTagOutput( gw.gosudoc.com.sun.javadoc.Type throwsType) {
        HtmlTree result = HtmlTree.DD(HtmlTree.CODE(htmlWriter.getLink(
                new LinkInfoImpl(configuration, LinkInfoImpl.Kind.MEMBER, throwsType))));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Content valueTagOutput( gw.gosudoc.com.sun.javadoc.FieldDoc field, String constantVal,
                                   boolean includeLink) {
        return includeLink ?
            htmlWriter.getDocLink(LinkInfoImpl.Kind.VALUE_TAG, field,
                constantVal, false) : new RawHtml(constantVal);
    }

    /**
     * {@inheritDoc}
     */
    public Content commentTagsToOutput( gw.gosudoc.com.sun.javadoc.Tag holderTag, gw.gosudoc.com.sun.javadoc.Tag[] tags) {
        return commentTagsToOutput(holderTag, null, tags, false);
    }

    /**
     * {@inheritDoc}
     */
    public Content commentTagsToOutput( gw.gosudoc.com.sun.javadoc.Doc holderDoc, gw.gosudoc.com.sun.javadoc.Tag[] tags) {
        return commentTagsToOutput(null, holderDoc, tags, false);
    }

    /**
     * {@inheritDoc}
     */
    public Content commentTagsToOutput( gw.gosudoc.com.sun.javadoc.Tag holderTag,
                                        gw.gosudoc.com.sun.javadoc.Doc holderDoc, gw.gosudoc.com.sun.javadoc.Tag[] tags, boolean isFirstSentence) {
        return htmlWriter.commentTagsToContent(
            holderTag, holderDoc, tags, isFirstSentence);
    }

    /**
     * {@inheritDoc}
     */
    public Configuration configuration() {
        return configuration;
    }
}
