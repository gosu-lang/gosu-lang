/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit;

import java.io.*;
import java.util.*;

import gw.gosudoc.com.sun.javadoc.Tag;

/**
 * The interface for writing member summary output.
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
public interface MemberSummaryWriter {

    /**
     * Get the member summary header for the given class.
     *
     * @param classDoc the class the summary belongs to
     * @param memberSummaryTree the content tree to which the member summary will be added
     * @return a content tree for the member summary header
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree);

    /**
     * Get the summary table for the given class.
     *
     * @param classDoc the class the summary table belongs to
     * @param tableContents list of contents that will be added to the summary table
     * @return a content tree for the member summary table
     */
    public Content getSummaryTableTree( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                        List<Content> tableContents);

    /**
     * Add the member summary for the given class and member.
     *
     * @param classDoc the class the summary belongs to
     * @param member the member that is documented
     * @param firstSentenceTags the tags for the sentence being documented
     * @param tableContents list of contents to which the summary will be added
     * @param counter the counter for determining id and style for the table row
     */
    public void addMemberSummary( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc, gw.gosudoc.com.sun.javadoc.ProgramElementDoc member,
                                  Tag[] firstSentenceTags, List<Content> tableContents, int counter);

    /**
     * Get the inherited member summary header for the given class.
     *
     * @param classDoc the class the summary belongs to
     * @return a content tree containing the inherited summary header
     */
    public Content getInheritedSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc);

    /**
     * Add the inherited member summary for the given class and member.
     *
     * @param classDoc the class the inherited member belongs to
     * @param member the inherited member that is being documented
     * @param isFirst true if this is the first member in the list
     * @param isLast true if this is the last member in the list
     * @param linksTree the content tree to which the links will be added
     */
    public void addInheritedMemberSummary( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, boolean isFirst, boolean isLast,
                                           Content linksTree);

    /**
     * Get inherited summary links.
     *
     * @return a content tree containing the inherited summary links
     */
    public Content getInheritedSummaryLinksTree();

    /**
     * Add the member tree to the member summary tree.
     *
     * @param memberSummaryTree the content tree representing the member summary
     * @param memberTree the content tree representing the member
     */
    public void addMemberTree(Content memberSummaryTree, Content memberTree);

    /**
     * Get the member tree.
     *
     * @param memberTree the content tree representing the member
     * @return a content tree for the member
     */
    public Content getMemberTree(Content memberTree);

    /**
     * Close the writer.
     */
    public void close() throws IOException;
}
