/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.xml.XmlSerializationOptions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A factory for creating XMLWriter objects.
 */
public final class XMLWriterFactory {

    private XmlSerializationOptions _options = new XmlSerializationOptions();

    /**
     * Creates a new XMLWriter that writes to the specified output stream. UTF-8 encoding will be used.
     *
     * @param os      the output stream to write XML to
     * @param options the XML writer options for the writer
     * @return the new XMLWriter
     * @throws IOException if an I/O error occurs writing to the stream
     */
    public static XMLWriter newDefaultXMLWriter(OutputStream os, XmlSerializationOptions options) throws IOException {
        return new XMLWriter(os, options);
    }

    public static XMLWriter newDefaultXMLWriter(OutputStream os) throws IOException {
        return new XMLWriter(os, new XmlSerializationOptions());
    }


    /**
     * Creates a new XMLWriter that writes to the specified output stream. UTF-8 encoding will be used.
     *
     * @param os the output stream to write XML to
     * @return the new XMLWriter
     * @throws IOException if an I/O error occurs writing to the stream
     */
    public XMLWriter newXMLWriter(OutputStream os) throws IOException {
        return new XMLWriter(os, _options);
    }

}
