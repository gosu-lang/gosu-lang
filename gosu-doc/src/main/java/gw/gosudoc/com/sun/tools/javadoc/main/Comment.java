/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.tools.javac.util.ListBuffer;
import gw.gosudoc.com.sun.javadoc.SerialFieldTag;

/**
 * Comment contains all information in comment part.
 *      It allows users to get first sentence of this comment, get
 *      comment for different tags...
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Kaiyang Liu (original)
 * @author Robert Field (rewrite)
 * @author Atul M Dambalkar
 * @author Neal Gafter (rewrite)
 */
@Deprecated
class Comment {

    /**
     * sorted comments with different tags.
     */
    private final ListBuffer<gw.gosudoc.com.sun.javadoc.Tag> tagList = new ListBuffer<>();

    /**
     * text minus any tags.
     */
    private String text;

    /**
     * Doc environment
     */
    private final gw.gosudoc.com.sun.tools.javadoc.main.DocEnv docenv;

    /**
     * constructor of Comment.
     */
    Comment( final gw.gosudoc.com.sun.tools.javadoc.main.DocImpl holder, final String commentString) {
        this.docenv = holder.env;

        /**
         * Separate the comment into the text part and zero to N tags.
         * Simple state machine is in one of three states:
         * <pre>
         * IN_TEXT: parsing the comment text or tag text.
         * TAG_NAME: parsing the name of a tag.
         * TAG_GAP: skipping through the gap between the tag name and
         * the tag text.
         * </pre>
         */
        @SuppressWarnings("fallthrough")
        class CommentStringParser {
            /**
             * The entry point to the comment string parser
             */
            void parseCommentStateMachine() {
                final int IN_TEXT = 1;
                final int TAG_GAP = 2;
                final int TAG_NAME = 3;
                int state = TAG_GAP;
                boolean newLine = true;
                String tagName = null;
                int tagStart = 0;
                int textStart = 0;
                int lastNonWhite = -1;
                int len = commentString.length();
                for (int inx = 0; inx < len; ++inx) {
                    char ch = commentString.charAt(inx);
                    boolean isWhite = Character.isWhitespace(ch);
                    switch (state)  {
                        case TAG_NAME:
                            if (isWhite) {
                                tagName = commentString.substring(tagStart, inx);
                                state = TAG_GAP;
                            }
                            break;
                        case TAG_GAP:
                            if (isWhite) {
                                break;
                            }
                            textStart = inx;
                            state = IN_TEXT;
                            /* fall thru */
                        case IN_TEXT:
                            if (newLine && ch == '@') {
                                parseCommentComponent(tagName, textStart,
                                                      lastNonWhite+1);
                                tagStart = inx;
                                state = TAG_NAME;
                            }
                            break;
                    }
                    if (ch == '\n') {
                        newLine = true;
                    } else if (!isWhite) {
                        lastNonWhite = inx;
                        newLine = false;
                    }
                }
                // Finish what's currently being processed
                switch (state)  {
                    case TAG_NAME:
                        tagName = commentString.substring(tagStart, len);
                        /* fall thru */
                    case TAG_GAP:
                        textStart = len;
                        /* fall thru */
                    case IN_TEXT:
                        parseCommentComponent(tagName, textStart, lastNonWhite+1);
                        break;
                }
            }

            /**
             * Save away the last parsed item.
             */
            void parseCommentComponent(String tagName,
                                       int from, int upto) {
                String tx = upto <= from ? "" : commentString.substring(from, upto);
                if (tagName == null) {
                    text = tx;
                } else {
                    gw.gosudoc.com.sun.tools.javadoc.main.TagImpl tag;
                    switch (tagName) {
                        case "@exception":
                        case "@throws":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.ThrowsTagImpl(holder, tagName, tx);
                            break;
                        case "@param":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.ParamTagImpl(holder, tagName, tx);
                            break;
                        case "@see":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.SeeTagImpl(holder, tagName, tx);
                            break;
                        case "@serialField":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.SerialFieldTagImpl(holder, tagName, tx);
                            break;
                        case "@return":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, tagName, tx);
                            break;
                        case "@author":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, tagName, tx);
                            break;
                        case "@version":
                            warnIfEmpty(tagName, tx);
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, tagName, tx);
                            break;
                        default:
                            tag = new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, tagName, tx);
                            break;
                    }
                    tagList.append(tag);
                }
            }

            void warnIfEmpty(String tagName, String tx) {
                if (tx.length() == 0) {
                    docenv.warning(holder, "tag.tag_has_no_arguments", tagName);
                }
            }

        }

        new CommentStringParser().parseCommentStateMachine();
    }

    /**
     * Return the text of the comment.
     */
    String commentText() {
        return text;
    }

    /**
     * Return all tags in this comment.
     */
    gw.gosudoc.com.sun.javadoc.Tag[] tags() {
        return tagList.toArray(new gw.gosudoc.com.sun.javadoc.Tag[tagList.length()]);
    }

    /**
     * Return tags of the specified kind in this comment.
     */
    gw.gosudoc.com.sun.javadoc.Tag[] tags( String tagname) {
        ListBuffer<gw.gosudoc.com.sun.javadoc.Tag> found = new ListBuffer<>();
        String target = tagname;
        if (target.charAt(0) != '@') {
            target = "@" + target;
        }
        for ( gw.gosudoc.com.sun.javadoc.Tag tag : tagList) {
            if (tag.kind().equals(target)) {
                found.append(tag);
            }
        }
        return found.toArray(new gw.gosudoc.com.sun.javadoc.Tag[found.length()]);
    }

    /**
     * Return throws tags in this comment.
     */
    gw.gosudoc.com.sun.javadoc.ThrowsTag[] throwsTags() {
        ListBuffer<gw.gosudoc.com.sun.javadoc.ThrowsTag> found = new ListBuffer<>();
        for ( gw.gosudoc.com.sun.javadoc.Tag next : tagList) {
            if (next instanceof gw.gosudoc.com.sun.javadoc.ThrowsTag ) {
                found.append((gw.gosudoc.com.sun.javadoc.ThrowsTag)next);
            }
        }
        return found.toArray(new gw.gosudoc.com.sun.javadoc.ThrowsTag[found.length()]);
    }

    /**
     * Return param tags (excluding type param tags) in this comment.
     */
    gw.gosudoc.com.sun.javadoc.ParamTag[] paramTags() {
        return paramTags(false);
    }

    /**
     * Return type param tags in this comment.
     */
    gw.gosudoc.com.sun.javadoc.ParamTag[] typeParamTags() {
        return paramTags(true);
    }

    /**
     * Return param tags in this comment.  If typeParams is true
     * include only type param tags, otherwise include only ordinary
     * param tags.
     */
    private gw.gosudoc.com.sun.javadoc.ParamTag[] paramTags( boolean typeParams) {
        ListBuffer<gw.gosudoc.com.sun.javadoc.ParamTag> found = new ListBuffer<>();
        for ( gw.gosudoc.com.sun.javadoc.Tag next : tagList) {
            if (next instanceof gw.gosudoc.com.sun.javadoc.ParamTag ) {
                gw.gosudoc.com.sun.javadoc.ParamTag p = (gw.gosudoc.com.sun.javadoc.ParamTag)next;
                if (typeParams == p.isTypeParameter()) {
                    found.append(p);
                }
            }
        }
        return found.toArray(new gw.gosudoc.com.sun.javadoc.ParamTag[found.length()]);
    }

    /**
     * Return see also tags in this comment.
     */
    gw.gosudoc.com.sun.javadoc.SeeTag[] seeTags() {
        ListBuffer<gw.gosudoc.com.sun.javadoc.SeeTag> found = new ListBuffer<>();
        for ( gw.gosudoc.com.sun.javadoc.Tag next : tagList) {
            if (next instanceof gw.gosudoc.com.sun.javadoc.SeeTag ) {
                found.append((gw.gosudoc.com.sun.javadoc.SeeTag)next);
            }
        }
        return found.toArray(new gw.gosudoc.com.sun.javadoc.SeeTag[found.length()]);
    }

    /**
     * Return serialField tags in this comment.
     */
    gw.gosudoc.com.sun.javadoc.SerialFieldTag[] serialFieldTags() {
        ListBuffer<gw.gosudoc.com.sun.javadoc.SerialFieldTag> found = new ListBuffer<>();
        for ( gw.gosudoc.com.sun.javadoc.Tag next : tagList) {
            if (next instanceof gw.gosudoc.com.sun.javadoc.SerialFieldTag ) {
                found.append((gw.gosudoc.com.sun.javadoc.SerialFieldTag)next);
            }
        }
        return found.toArray(new SerialFieldTag[found.length()]);
    }

    /**
     * Return array of tags with text and inline See Tags for a Doc comment.
     */
    static gw.gosudoc.com.sun.javadoc.Tag[] getInlineTags( gw.gosudoc.com.sun.tools.javadoc.main.DocImpl holder, String inlinetext) {
        ListBuffer<gw.gosudoc.com.sun.javadoc.Tag> taglist = new ListBuffer<>();
        int delimend = 0, textstart = 0, len = inlinetext.length();
        boolean inPre = false;
        DocEnv docenv = holder.env;

        if (len == 0) {
            return taglist.toArray(new gw.gosudoc.com.sun.javadoc.Tag[taglist.length()]);
        }
        while (true) {
            int linkstart;
            if ((linkstart = inlineTagFound(holder, inlinetext,
                                            textstart)) == -1) {
                taglist.append(new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, "Text",
                                           inlinetext.substring(textstart)));
                break;
            } else {
                inPre = scanForPre(inlinetext, textstart, linkstart, inPre);
                int seetextstart = linkstart;
                for (int i = linkstart; i < inlinetext.length(); i++) {
                    char c = inlinetext.charAt(i);
                    if (Character.isWhitespace(c) ||
                        c == '}') {
                        seetextstart = i;
                        break;
                     }
                }
                String linkName = inlinetext.substring(linkstart+2, seetextstart);
                if (!(inPre && (linkName.equals("code") || linkName.equals("literal")))) {
                    //Move past the white space after the inline tag name.
                    while (Character.isWhitespace(inlinetext.
                                                      charAt(seetextstart))) {
                        if (inlinetext.length() <= seetextstart) {
                            taglist.append(new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, "Text",
                                                       inlinetext.substring(textstart, seetextstart)));
                            docenv.warning(holder,
                                           "tag.Improper_Use_Of_Link_Tag",
                                           inlinetext);
                            return taglist.toArray(new gw.gosudoc.com.sun.javadoc.Tag[taglist.length()]);
                        } else {
                            seetextstart++;
                        }
                    }
                }
                taglist.append(new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, "Text",
                                           inlinetext.substring(textstart, linkstart)));
                textstart = seetextstart;   // this text is actually seetag
                if ((delimend = findInlineTagDelim(inlinetext, textstart)) == -1) {
                    //Missing closing '}' character.
                    // store the text as it is with the {@link.
                    taglist.append(new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, "Text",
                                               inlinetext.substring(textstart)));
                    docenv.warning(holder,
                                   "tag.End_delimiter_missing_for_possible_SeeTag",
                                   inlinetext);
                    return taglist.toArray(new gw.gosudoc.com.sun.javadoc.Tag[taglist.length()]);
                } else {
                    //Found closing '}' character.
                    if (linkName.equals("see")
                           || linkName.equals("link")
                           || linkName.equals("linkplain")) {
                        taglist.append( new gw.gosudoc.com.sun.tools.javadoc.main.SeeTagImpl(holder, "@" + linkName,
                              inlinetext.substring(textstart, delimend)));
                    } else {
                        taglist.append( new gw.gosudoc.com.sun.tools.javadoc.main.TagImpl(holder, "@" + linkName,
                              inlinetext.substring(textstart, delimend)));
                    }
                    textstart = delimend + 1;
                }
            }
            if (textstart == inlinetext.length()) {
                break;
            }
        }
        return taglist.toArray(new gw.gosudoc.com.sun.javadoc.Tag[taglist.length()]);
    }

    /** regex for case-insensitive match for {@literal <pre> } and  {@literal </pre> }. */
    private static final Pattern prePat = Pattern.compile("(?i)<(/?)pre>");

    private static boolean scanForPre(String inlinetext, int start, int end, boolean inPre) {
        Matcher m = prePat.matcher(inlinetext).region(start, end);
        while (m.find()) {
            inPre = m.group(1).isEmpty();
        }
        return inPre;
    }

    /**
     * Recursively find the index of the closing '}' character for an inline tag
     * and return it.  If it can't be found, return -1.
     * @param inlineText the text to search in.
     * @param searchStart the index of the place to start searching at.
     * @return the index of the closing '}' character for an inline tag.
     * If it can't be found, return -1.
     */
    private static int findInlineTagDelim(String inlineText, int searchStart) {
        int delimEnd, nestedOpenBrace;
        if ((delimEnd = inlineText.indexOf("}", searchStart)) == -1) {
            return -1;
        } else if (((nestedOpenBrace = inlineText.indexOf("{", searchStart)) != -1) &&
            nestedOpenBrace < delimEnd){
            //Found a nested open brace.
            int nestedCloseBrace = findInlineTagDelim(inlineText, nestedOpenBrace + 1);
            return (nestedCloseBrace != -1) ?
                findInlineTagDelim(inlineText, nestedCloseBrace + 1) :
                -1;
        } else {
            return delimEnd;
        }
    }

    /**
     * Recursively search for the characters '{', '@', followed by
     * name of inline tag and white space,
     * if found
     *    return the index of the text following the white space.
     * else
     *    return -1.
     */
    private static int inlineTagFound( DocImpl holder, String inlinetext, int start) {
        DocEnv docenv = holder.env;
        int linkstart = inlinetext.indexOf("{@", start);
        if (start == inlinetext.length() || linkstart == -1) {
            return -1;
        } else if (inlinetext.indexOf('}', linkstart) == -1) {
            //Missing '}'.
            docenv.warning(holder, "tag.Improper_Use_Of_Link_Tag",
                    inlinetext.substring(linkstart, inlinetext.length()));
            return -1;
        } else {
            return linkstart;
        }
    }


    /**
     * Return array of tags for the locale specific first sentence in the text.
     */
    static gw.gosudoc.com.sun.javadoc.Tag[] firstSentenceTags( DocImpl holder, String text) {
        gw.gosudoc.com.sun.tools.javadoc.main.DocLocale doclocale = holder.env.doclocale;
        return getInlineTags(holder,
                             doclocale.localeSpecificFirstSentence(holder, text));
    }

    /**
     * Return text for this Doc comment.
     */
    @Override
    public String toString() {
        return text;
    }
}
