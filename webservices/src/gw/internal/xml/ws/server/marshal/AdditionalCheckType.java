/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.xml.Marshaller;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;

import java.util.Map;

/**
 * Flag interface to indicate that this type has additional checks needed
 */
public interface AdditionalCheckType {

    /** This will perform the additional checks that are required for this type info.  This is primarily for
     * the recursion needed for exportable types.
     *
     * @param marshaller the calling marshaller
     * @param parsedElement  the parse element for errors
     * @param label the label for the element (note that this probably is a path
     * @param type the type being process right now
     * @param seenNamespaces the seen name spaces
     */
    void checkType(Marshaller marshaller, IParsedElement parsedElement, String label, IType type, Map<String, Object> seenNamespaces);

}
