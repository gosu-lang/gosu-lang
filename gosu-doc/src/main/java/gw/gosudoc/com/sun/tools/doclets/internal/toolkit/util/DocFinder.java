/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets.InheritableTaglet;

/**
 * Search for the requested documentation.  Inherit documentation if necessary.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.5
 */
@Deprecated
public class DocFinder {

    /**
     * The class that encapsulates the input.
     */
    public static class Input {
        /**
         * The element to search documentation from.
         */
        public gw.gosudoc.com.sun.javadoc.ProgramElementDoc element;
        /**
         * The taglet to search for documentation on behalf of. Null if we want
         * to search for overall documentation.
         */
        public InheritableTaglet taglet = null;

        /**
         * The id of the tag to retrieve documentation for.
         */
        public String tagId = null;

        /**
         * The tag to retrieve documentation for.  This is only used for the
         * inheritDoc tag.
         */
        public gw.gosudoc.com.sun.javadoc.Tag tag = null;

        /**
         * True if we only want to search for the first sentence.
         */
        public boolean isFirstSentence = false;

        /**
         * True if we are looking for documentation to replace the inheritDocTag.
         */
        public boolean isInheritDocTag = false;

        /**
         * Used to distinguish between type variable param tags and regular
         * param tags.
         */
        public boolean isTypeVariableParamTag = false;

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element, InheritableTaglet taglet, gw.gosudoc.com.sun.javadoc.Tag tag,
                      boolean isFirstSentence, boolean isInheritDocTag) {
            this(element);
            this.taglet = taglet;
            this.tag = tag;
            this.isFirstSentence = isFirstSentence;
            this.isInheritDocTag = isInheritDocTag;
        }

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element, InheritableTaglet taglet, String tagId) {
            this(element);
            this.taglet = taglet;
            this.tagId = tagId;
        }

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element, InheritableTaglet taglet, String tagId,
                      boolean isTypeVariableParamTag) {
            this(element);
            this.taglet = taglet;
            this.tagId = tagId;
            this.isTypeVariableParamTag = isTypeVariableParamTag;
        }

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element, InheritableTaglet taglet) {
            this(element);
            this.taglet = taglet;
        }

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element) {
            if (element == null)
                throw new NullPointerException();
            this.element = element;
        }

        public Input( gw.gosudoc.com.sun.javadoc.ProgramElementDoc element, boolean isFirstSentence) {
            this(element);
            this.isFirstSentence = isFirstSentence;
        }

        public Input copy() {
            Input clone = new Input(this.element);
            clone.taglet = this.taglet;
            clone.tagId = this.tagId;
            clone.tag = this.tag;
            clone.isFirstSentence = this.isFirstSentence;
            clone.isInheritDocTag = this.isInheritDocTag;
            clone.isTypeVariableParamTag = this.isTypeVariableParamTag;
            if (clone.element == null)
                throw new NullPointerException();
            return clone;

        }
    }

    /**
     * The class that encapsulates the output.
     */
    public static class Output {
        /**
         * The tag that holds the documentation.  Null if documentation
         * is not held by a tag.
         */
        public gw.gosudoc.com.sun.javadoc.Tag holderTag;

        /**
         * The Doc object that holds the documentation.
         */
        public gw.gosudoc.com.sun.javadoc.Doc holder;

        /**
         * The inherited documentation.
         */
        public gw.gosudoc.com.sun.javadoc.Tag[] inlineTags = new gw.gosudoc.com.sun.javadoc.Tag[] {};

        /**
         * False if documentation could not be inherited.
         */
        public boolean isValidInheritDocTag = true;

        /**
         * When automatically inheriting throws tags, you sometime must inherit
         * more than one tag.  For example if the element declares that it throws
         * IOException and the overridden element has throws tags for IOException and
         * ZipException, both tags would be inherited because ZipException is a
         * subclass of IOException.  This subclass of DocFinder.Output allows
         * multiple tag inheritence.
         */
        public List<Tag> tagList  = new ArrayList<>();
    }

    /**
     * Search for the requested comments in the given element.  If it does not
     * have comments, return documentation from the overriden element if possible.
     * If the overriden element does not exist or does not have documentation to
     * inherit, search for documentation to inherit from implemented methods.
     *
     * @param input the input object used to perform the search.
     *
     * @return an Output object representing the documentation that was found.
     */
    public static Output search(Configuration configuration, Input input) {
        Output output = new Output();
        if (input.isInheritDocTag) {
            //Do nothing because "element" does not have any documentation.
            //All it has it {@inheritDoc}.
        } else if (input.taglet == null) {
            //We want overall documentation.
            output.inlineTags = input.isFirstSentence ?
                input.element.firstSentenceTags() :
                input.element.inlineTags();
            output.holder = input.element;
        } else {
            input.taglet.inherit(input, output);
        }

        if (output.inlineTags != null && output.inlineTags.length > 0) {
            return output;
        }
        output.isValidInheritDocTag = false;
        Input inheritedSearchInput = input.copy();
        inheritedSearchInput.isInheritDocTag = false;
        if (input.element instanceof gw.gosudoc.com.sun.javadoc.MethodDoc ) {
            gw.gosudoc.com.sun.javadoc.MethodDoc overriddenMethod = ((gw.gosudoc.com.sun.javadoc.MethodDoc) input.element).overriddenMethod();
            if (overriddenMethod != null) {
                inheritedSearchInput.element = overriddenMethod;
                output = search(configuration, inheritedSearchInput);
                output.isValidInheritDocTag = true;
                if (output.inlineTags.length > 0) {
                    return output;
                }
            }
            //NOTE:  When we fix the bug where ClassDoc.interfaceTypes() does
            //       not pass all implemented interfaces, we will use the
            //       appropriate element here.
            gw.gosudoc.com.sun.javadoc.MethodDoc[] implementedMethods =
                (new ImplementedMethods((gw.gosudoc.com.sun.javadoc.MethodDoc) input.element, configuration)).build(false);
            for ( gw.gosudoc.com.sun.javadoc.MethodDoc implementedMethod : implementedMethods) {
                inheritedSearchInput.element = implementedMethod;
                output = search(configuration, inheritedSearchInput);
                output.isValidInheritDocTag = true;
                if (output.inlineTags.length > 0) {
                    return output;
                }
            }
        } else if (input.element instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
            gw.gosudoc.com.sun.javadoc.ProgramElementDoc superclass = ((gw.gosudoc.com.sun.javadoc.ClassDoc) input.element).superclass();
            if (superclass != null) {
                inheritedSearchInput.element = superclass;
                output = search(configuration, inheritedSearchInput);
                output.isValidInheritDocTag = true;
                if (output.inlineTags.length > 0) {
                    return output;
                }
            }
        }
        return output;
    }
}
