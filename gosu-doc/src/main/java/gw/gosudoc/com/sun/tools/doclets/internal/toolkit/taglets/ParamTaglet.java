/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.TypeVariable;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * A taglet that represents the @param tag.
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
public class ParamTaglet extends BaseTaglet implements InheritableTaglet {

    /**
     * Construct a ParamTaglet.
     */
    public ParamTaglet() {
        name = "param";
    }

    /**
     * Given an array of <code>Parameter</code>s, return
     * a name/rank number map.  If the array is null, then
     * null is returned.
     * @param params The array of parmeters (from type or executable member) to
     *               check.
     * @return a name-rank number map.
     */
    private static Map<String,String> getRankMap(Object[] params){
        if (params == null) {
            return null;
        }
        HashMap<String,String> result = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            String name = params[i] instanceof gw.gosudoc.com.sun.javadoc.Parameter ?
                ((gw.gosudoc.com.sun.javadoc.Parameter) params[i]).name() :
                ((gw.gosudoc.com.sun.javadoc.TypeVariable) params[i]).typeName();
            result.put(name, String.valueOf(i));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void inherit( DocFinder.Input input, DocFinder.Output output) {
        if (input.tagId == null) {
            input.isTypeVariableParamTag = ((gw.gosudoc.com.sun.javadoc.ParamTag) input.tag).isTypeParameter();
            Object[] parameters = input.isTypeVariableParamTag ?
                (Object[]) ((gw.gosudoc.com.sun.javadoc.MethodDoc) input.tag.holder()).typeParameters() :
                (Object[]) ((gw.gosudoc.com.sun.javadoc.MethodDoc) input.tag.holder()).parameters();
            String target = ((gw.gosudoc.com.sun.javadoc.ParamTag) input.tag).parameterName();
            int i;
            for (i = 0; i < parameters.length; i++) {
                String name = parameters[i] instanceof gw.gosudoc.com.sun.javadoc.Parameter ?
                    ((gw.gosudoc.com.sun.javadoc.Parameter) parameters[i]).name() :
                    ((gw.gosudoc.com.sun.javadoc.TypeVariable) parameters[i]).typeName();
                if (name.equals(target)) {
                    input.tagId = String.valueOf(i);
                    break;
                }
            }
            if (i == parameters.length) {
                //Someone used {@inheritDoc} on an invalid @param tag.
                //We don't know where to inherit from.
                //XXX: in the future when Configuration is available here,
                //print a warning for this mistake.
                return;
            }
        }
        gw.gosudoc.com.sun.javadoc.ParamTag[] tags = input.isTypeVariableParamTag ?
            ((gw.gosudoc.com.sun.javadoc.MethodDoc)input.element).typeParamTags() : ((gw.gosudoc.com.sun.javadoc.MethodDoc)input.element).paramTags();
        Map<String, String> rankMap = getRankMap(input.isTypeVariableParamTag ?
            (Object[]) ((gw.gosudoc.com.sun.javadoc.MethodDoc)input.element).typeParameters() :
            (Object[]) ((gw.gosudoc.com.sun.javadoc.MethodDoc)input.element).parameters());
        for ( gw.gosudoc.com.sun.javadoc.ParamTag tag : tags) {
            if (rankMap.containsKey(tag.parameterName()) &&
                rankMap.get(tag.parameterName()).equals((input.tagId))) {
                output.holder = input.element;
                output.holderTag = tag;
                output.inlineTags = input.isFirstSentence ?
                                    tag.firstSentenceTags() : tag.inlineTags();
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean inField() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean inMethod() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean inOverview() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean inPackage() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean inType() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInlineTag() {
        return false;
    }

    /**
     * Given an array of <code>ParamTag</code>s,return its string representation.
     * @param holder the member that holds the param tags.
     * @param writer the TagletWriter that will write this tag.
     * @return the TagletOutput representation of these <code>ParamTag</code>s.
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Doc holder, TagletWriter writer) {
        if (holder instanceof gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc ) {
            gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member = (gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc) holder;
            Content output = getTagletOutput(false, member, writer,
                member.typeParameters(), member.typeParamTags());
            output.addContent(getTagletOutput(true, member, writer,
                member.parameters(), member.paramTags()));
            return output;
        } else {
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc = (gw.gosudoc.com.sun.javadoc.ClassDoc) holder;
            return getTagletOutput(false, classDoc, writer,
                classDoc.typeParameters(), classDoc.typeParamTags());
        }
    }

    /**
     * Given an array of <code>ParamTag</code>s,return its string representation.
     * Try to inherit the param tags that are missing.
     *
     * @param holder            the doc that holds the param tags.
     * @param writer            the TagletWriter that will write this tag.
     * @param formalParameters  The array of parmeters (from type or executable
     *                          member) to check.
     *
     * @return the TagletOutput representation of these <code>ParamTag</code>s.
     */
    private Content getTagletOutput(boolean isNonTypeParams, gw.gosudoc.com.sun.javadoc.Doc holder,
            TagletWriter writer, Object[] formalParameters, gw.gosudoc.com.sun.javadoc.ParamTag[] paramTags) {
        Content result = writer.getOutputInstance();
        Set<String> alreadyDocumented = new HashSet<>();
        if (paramTags.length > 0) {
            result.addContent(
                processParamTags(isNonTypeParams, paramTags,
                getRankMap(formalParameters), writer, alreadyDocumented)
            );
        }
        if (alreadyDocumented.size() != formalParameters.length) {
            //Some parameters are missing corresponding @param tags.
            //Try to inherit them.
            result.addContent(getInheritedTagletOutput (isNonTypeParams, holder,
                writer, formalParameters, alreadyDocumented));
        }
        return result;
    }

    /**
     * Loop through each indivitual parameter.  It it does not have a
     * corresponding param tag, try to inherit it.
     */
    private Content getInheritedTagletOutput(boolean isNonTypeParams, gw.gosudoc.com.sun.javadoc.Doc holder,
            TagletWriter writer, Object[] formalParameters,
            Set<String> alreadyDocumented) {
        Content result = writer.getOutputInstance();
        if ((! alreadyDocumented.contains(null)) &&
                holder instanceof gw.gosudoc.com.sun.javadoc.MethodDoc ) {
            for (int i = 0; i < formalParameters.length; i++) {
                if (alreadyDocumented.contains(String.valueOf(i))) {
                    continue;
                }
                //This parameter does not have any @param documentation.
                //Try to inherit it.
                DocFinder.Output inheritedDoc =
                    DocFinder.search(writer.configuration(), new DocFinder.Input((gw.gosudoc.com.sun.javadoc.MethodDoc) holder, this,
                        String.valueOf(i), ! isNonTypeParams));
                if (inheritedDoc.inlineTags != null &&
                        inheritedDoc.inlineTags.length > 0) {
                    result.addContent(
                        processParamTag(isNonTypeParams, writer,
                            (gw.gosudoc.com.sun.javadoc.ParamTag) inheritedDoc.holderTag,
                            isNonTypeParams ?
                                ((gw.gosudoc.com.sun.javadoc.Parameter) formalParameters[i]).name():
                                ((TypeVariable) formalParameters[i]).typeName(),
                            alreadyDocumented.size() == 0));
                }
                alreadyDocumented.add(String.valueOf(i));
            }
        }
        return result;
    }

    /**
     * Given an array of <code>Tag</code>s representing this custom
     * tag, return its string representation.  Print a warning for param
     * tags that do not map to parameters.  Print a warning for param
     * tags that are duplicated.
     *
     * @param paramTags the array of <code>ParamTag</code>s to convert.
     * @param writer the TagletWriter that will write this tag.
     * @param alreadyDocumented the set of exceptions that have already
     *        been documented.
     * @param rankMap a {@link Map} which holds ordering
     *                    information about the parameters.
     * @param rankMap a {@link Map} which holds a mapping
     *                of a rank of a parameter to its name.  This is
     *                used to ensure that the right name is used
     *                when parameter documentation is inherited.
     * @return the Content representation of this <code>Tag</code>.
     */
    private Content processParamTags( boolean isNonTypeParams,
                                      gw.gosudoc.com.sun.javadoc.ParamTag[] paramTags, Map<String, String> rankMap, TagletWriter writer,
                                      Set<String> alreadyDocumented) {
        Content result = writer.getOutputInstance();
        if (paramTags.length > 0) {
            for ( gw.gosudoc.com.sun.javadoc.ParamTag pt : paramTags) {
                String paramName = isNonTypeParams ?
                                   pt.parameterName() : "<" + pt.parameterName() + ">";
                if (!rankMap.containsKey(pt.parameterName())) {
                    writer.getMsgRetriever().warning(pt.position(),
                                                     isNonTypeParams ?
                                                     "doclet.Parameters_warn" :
                                                     "doclet.Type_Parameters_warn",
                                                     paramName);
                }
                String rank = rankMap.get(pt.parameterName());
                if (rank != null && alreadyDocumented.contains(rank)) {
                    writer.getMsgRetriever().warning(pt.position(),
                                                     isNonTypeParams ?
                                                     "doclet.Parameters_dup_warn" :
                                                     "doclet.Type_Parameters_dup_warn",
                                                     paramName);
                }
                result.addContent(processParamTag(isNonTypeParams, writer, pt,
                                                  pt.parameterName(), alreadyDocumented.size() == 0));
                alreadyDocumented.add(rank);
            }
        }
        return result;
    }
    /**
     * Convert the individual ParamTag into Content.
     *
     * @param isNonTypeParams true if this is just a regular param tag.  False
     *                        if this is a type param tag.
     * @param writer          the taglet writer for output writing.
     * @param paramTag        the tag whose inline tags will be printed.
     * @param name            the name of the parameter.  We can't rely on
     *                        the name in the param tag because we might be
     *                        inheriting documentation.
     * @param isFirstParam    true if this is the first param tag being printed.
     *
     */
    private Content processParamTag( boolean isNonTypeParams,
                                     TagletWriter writer, gw.gosudoc.com.sun.javadoc.ParamTag paramTag, String name,
                                     boolean isFirstParam) {
        Content result = writer.getOutputInstance();
        String header = writer.configuration().getText(
            isNonTypeParams ? "doclet.Parameters" : "doclet.TypeParameters");
        if (isFirstParam) {
            result.addContent(writer.getParamHeader(header));
        }
        result.addContent(writer.paramTagOutput(paramTag,
            name));
        return result;
    }
}
