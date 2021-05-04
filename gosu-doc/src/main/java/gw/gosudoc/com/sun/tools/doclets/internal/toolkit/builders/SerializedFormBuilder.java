/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders;

import java.io.*;
import java.util.*;



import com.sun.tools.javac.util.StringUtils;
import gw.gosudoc.com.sun.javadoc.SerialFieldTag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;

/**
 * Builds the serialized form.
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
public class SerializedFormBuilder extends AbstractBuilder {

    /**
     * The root element of the serialized form XML is {@value}.
     */
    public static final String NAME = "SerializedForm";

    /**
     * The writer for this builder.
     */
    private SerializedFormWriter writer;

    /**
     * The writer for serializable fields.
     */
    private SerializedFormWriter.SerialFieldWriter fieldWriter;

    /**
     * The writer for serializable method documentation.
     */
    private SerializedFormWriter.SerialMethodWriter methodWriter;

    /**
     * The header for the serial version UID.  Save the string
     * here instead of the properties file because we do not want
     * this string to be localized.
     */
    private static final String SERIAL_VERSION_UID_HEADER = "serialVersionUID:";

    /**
     * The current package being documented.
     */
    private gw.gosudoc.com.sun.javadoc.PackageDoc currentPackage;

    /**
     * The current class being documented.
     */
    private gw.gosudoc.com.sun.javadoc.ClassDoc currentClass;

    /**
     * The current member being documented.
     */
    protected gw.gosudoc.com.sun.javadoc.MemberDoc currentMember;

    /**
     * The content that will be added to the serialized form documentation tree.
     */
    private Content contentTree;


    /**
     * Construct a new SerializedFormBuilder.
     * @param context  the build context.
     */
    private SerializedFormBuilder(Context context) {
        super(context);
    }

    /**
     * Construct a new SerializedFormBuilder.
     * @param context  the build context.
     */
    public static SerializedFormBuilder getInstance(Context context) {
        return new SerializedFormBuilder(context);
    }

    /**
     * Build the serialized form.
     */
    public void build() throws IOException {
        if (! serialClassFoundToDocument(configuration.root.classes())) {
            //Nothing to document.
            return;
        }
        try {
            writer = configuration.getWriterFactory().getSerializedFormWriter();
            if (writer == null) {
                //Doclet does not support this output.
                return;
            }
        } catch (Exception e) {
            throw new DocletAbortException(e);
        }
        build(layoutParser.parseXML(NAME), contentTree);
        writer.close();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return NAME;
    }

    /**
     * Build the serialized form.
     *
     * @param node the XML element that specifies which components to document
     * @param serializedTree content tree to which the documentation will be added
     */
    public void buildSerializedForm(XMLNode node, Content serializedTree) throws Exception {
        serializedTree = writer.getHeader(configuration.getText(
                "doclet.Serialized_Form"));
        buildChildren(node, serializedTree);
        writer.addFooter(serializedTree);
        writer.printDocument(serializedTree);
        writer.close();
    }

    /**
     * Build the serialized form summaries.
     *
     * @param node the XML element that specifies which components to document
     * @param serializedTree content tree to which the documentation will be added
     */
    public void buildSerializedFormSummaries(XMLNode node, Content serializedTree) {
        Content serializedSummariesTree = writer.getSerializedSummariesHeader();
        for ( gw.gosudoc.com.sun.javadoc.PackageDoc pkg : configuration.packages) {
            currentPackage = pkg;
            buildChildren(node, serializedSummariesTree);
        }
        serializedTree.addContent(writer.getSerializedContent(
                serializedSummariesTree));
    }

    /**
     * Build the package serialized form for the current package being processed.
     *
     * @param node the XML element that specifies which components to document
     * @param serializedSummariesTree content tree to which the documentation will be added
     */
    public void buildPackageSerializedForm(XMLNode node, Content serializedSummariesTree) {
        Content packageSerializedTree = writer.getPackageSerializedHeader();
        String foo = currentPackage.name();
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes = currentPackage.allClasses(false);
        if (classes == null || classes.length == 0) {
            return;
        }
        if (!serialInclude(currentPackage)) {
            return;
        }
        if (!serialClassFoundToDocument(classes)) {
            return;
        }
        buildChildren(node, packageSerializedTree);
        writer.addPackageSerializedTree(serializedSummariesTree, packageSerializedTree);
    }

    /**
     * Build the package header.
     *
     * @param node the XML element that specifies which components to document
     * @param packageSerializedTree content tree to which the documentation will be added
     */
    public void buildPackageHeader(XMLNode node, Content packageSerializedTree) {
        packageSerializedTree.addContent(writer.getPackageHeader(
                utils.getPackageName(currentPackage)));
    }

    /**
     * Build the class serialized form.
     *
     * @param node the XML element that specifies which components to document
     * @param packageSerializedTree content tree to which the documentation will be added
     */
    public void buildClassSerializedForm(XMLNode node, Content packageSerializedTree) {
        Content classSerializedTree = writer.getClassSerializedHeader();
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes = currentPackage.allClasses(false);
        Arrays.sort(classes);
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc : classes) {
            currentClass = classDoc;
            fieldWriter = writer.getSerialFieldWriter(currentClass);
            methodWriter = writer.getSerialMethodWriter(currentClass);
            if (currentClass.isClass() && currentClass.isSerializable()) {
                if (!serialClassInclude(currentClass)) {
                    continue;
                }
                Content classTree = writer.getClassHeader(currentClass);
                buildChildren(node, classTree);
                classSerializedTree.addContent(classTree);
            }
        }
        packageSerializedTree.addContent(classSerializedTree);
    }

    /**
     * Build the serial UID information for the given class.
     *
     * @param node the XML element that specifies which components to document
     * @param classTree content tree to which the serial UID information will be added
     */
    public void buildSerialUIDInfo(XMLNode node, Content classTree) {
        Content serialUidTree = writer.getSerialUIDInfoHeader();
        for ( gw.gosudoc.com.sun.javadoc.FieldDoc field : currentClass.fields(false)) {
            if (field.name().equals("serialVersionUID") &&
                field.constantValueExpression() != null) {
                writer.addSerialUIDInfo(SERIAL_VERSION_UID_HEADER,
                                        field.constantValueExpression(), serialUidTree);
                break;
            }
        }
        classTree.addContent(serialUidTree);
    }

    /**
     * Build the summaries for the methods and fields.
     *
     * @param node the XML element that specifies which components to document
     * @param classTree content tree to which the documentation will be added
     */
    public void buildClassContent(XMLNode node, Content classTree) {
        Content classContentTree = writer.getClassContentHeader();
        buildChildren(node, classContentTree);
        classTree.addContent(classContentTree);
    }

    /**
     * Build the summaries for the methods that belong to the given
     * class.
     *
     * @param node the XML element that specifies which components to document
     * @param classContentTree content tree to which the documentation will be added
     */
    public void buildSerializableMethods(XMLNode node, Content classContentTree) {
        Content serializableMethodTree = methodWriter.getSerializableMethodsHeader();
        gw.gosudoc.com.sun.javadoc.MemberDoc[] members = currentClass.serializationMethods();
        int membersLength = members.length;
        if (membersLength > 0) {
            for (int i = 0; i < membersLength; i++) {
                currentMember = members[i];
                Content methodsContentTree = methodWriter.getMethodsContentHeader(
                        (i == membersLength - 1));
                buildChildren(node, methodsContentTree);
                serializableMethodTree.addContent(methodsContentTree);
            }
        }
        if (currentClass.serializationMethods().length > 0) {
            classContentTree.addContent(methodWriter.getSerializableMethods(
                    configuration.getText("doclet.Serialized_Form_methods"),
                    serializableMethodTree));
            if (currentClass.isSerializable() && !currentClass.isExternalizable()) {
                if (currentClass.serializationMethods().length == 0) {
                    Content noCustomizationMsg = methodWriter.getNoCustomizationMsg(
                            configuration.getText(
                            "doclet.Serializable_no_customization"));
                    classContentTree.addContent(methodWriter.getSerializableMethods(
                    configuration.getText("doclet.Serialized_Form_methods"),
                    noCustomizationMsg));
                }
            }
        }
    }

    /**
     * Build the method sub header.
     *
     * @param node the XML element that specifies which components to document
     * @param methodsContentTree content tree to which the documentation will be added
     */
    public void buildMethodSubHeader(XMLNode node, Content methodsContentTree)  {
        methodWriter.addMemberHeader((gw.gosudoc.com.sun.javadoc.MethodDoc)currentMember, methodsContentTree);
    }

    /**
     * Build the deprecated method description.
     *
     * @param node the XML element that specifies which components to document
     * @param methodsContentTree content tree to which the documentation will be added
     */
    public void buildDeprecatedMethodInfo(XMLNode node, Content methodsContentTree) {
        methodWriter.addDeprecatedMemberInfo((gw.gosudoc.com.sun.javadoc.MethodDoc) currentMember, methodsContentTree);
    }

    /**
     * Build the information for the method.
     *
     * @param node the XML element that specifies which components to document
     * @param methodsContentTree content tree to which the documentation will be added
     */
    public void buildMethodInfo(XMLNode node, Content methodsContentTree)  {
        if(configuration.nocomment){
            return;
        }
        buildChildren(node, methodsContentTree);
    }

    /**
     * Build method description.
     *
     * @param node the XML element that specifies which components to document
     * @param methodsContentTree content tree to which the documentation will be added
     */
    public void buildMethodDescription(XMLNode node, Content methodsContentTree) {
        methodWriter.addMemberDescription((gw.gosudoc.com.sun.javadoc.MethodDoc) currentMember, methodsContentTree);
    }

    /**
     * Build the method tags.
     *
     * @param node the XML element that specifies which components to document
     * @param methodsContentTree content tree to which the documentation will be added
     */
    public void buildMethodTags(XMLNode node, Content methodsContentTree) {
        methodWriter.addMemberTags((gw.gosudoc.com.sun.javadoc.MethodDoc) currentMember, methodsContentTree);
        gw.gosudoc.com.sun.javadoc.MethodDoc method = (gw.gosudoc.com.sun.javadoc.MethodDoc)currentMember;
        if (method.name().compareTo("writeExternal") == 0
                && method.tags("serialData").length == 0) {
            if (configuration.serialwarn) {
                configuration.getDocletSpecificMsg().warning(
                        currentMember.position(), "doclet.MissingSerialDataTag",
                        method.containingClass().qualifiedName(), method.name());
            }
        }
    }

    /**
     * Build the field header.
     *
     * @param node the XML element that specifies which components to document
     * @param classContentTree content tree to which the documentation will be added
     */
    public void buildFieldHeader(XMLNode node, Content classContentTree) {
        if (currentClass.serializableFields().length > 0) {
            buildFieldSerializationOverview(currentClass, classContentTree);
        }
    }

    /**
     * Build the serialization overview for the given class.
     *
     * @param classDoc the class to print the overview for.
     * @param classContentTree content tree to which the documentation will be added
     */
    public void buildFieldSerializationOverview( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc, Content classContentTree) {
        if (classDoc.definesSerializableFields()) {
            gw.gosudoc.com.sun.javadoc.FieldDoc serialPersistentField = classDoc.serializableFields()[0];
            // Check to see if there are inline comments, tags or deprecation
            // information to be printed.
            if (fieldWriter.shouldPrintOverview(serialPersistentField)) {
                Content serializableFieldsTree = fieldWriter.getSerializableFieldsHeader();
                Content fieldsOverviewContentTree = fieldWriter.getFieldsContentHeader(true);
                fieldWriter.addMemberDeprecatedInfo(serialPersistentField,
                        fieldsOverviewContentTree);
                if (!configuration.nocomment) {
                    fieldWriter.addMemberDescription(serialPersistentField,
                            fieldsOverviewContentTree);
                    fieldWriter.addMemberTags(serialPersistentField,
                            fieldsOverviewContentTree);
                }
                serializableFieldsTree.addContent(fieldsOverviewContentTree);
                classContentTree.addContent(fieldWriter.getSerializableFields(
                        configuration.getText("doclet.Serialized_Form_class"),
                        serializableFieldsTree));
            }
        }
    }

    /**
     * Build the summaries for the fields that belong to the given class.
     *
     * @param node the XML element that specifies which components to document
     * @param classContentTree content tree to which the documentation will be added
     */
    public void buildSerializableFields(XMLNode node, Content classContentTree) {
        gw.gosudoc.com.sun.javadoc.MemberDoc[] members = currentClass.serializableFields();
        int membersLength = members.length;
        if (membersLength > 0) {
            Content serializableFieldsTree = fieldWriter.getSerializableFieldsHeader();
            for (int i = 0; i < membersLength; i++) {
                currentMember = members[i];
                if (!currentClass.definesSerializableFields()) {
                    Content fieldsContentTree = fieldWriter.getFieldsContentHeader(
                            (i == membersLength - 1));
                    buildChildren(node, fieldsContentTree);
                    serializableFieldsTree.addContent(fieldsContentTree);
                }
                else {
                    buildSerialFieldTagsInfo(serializableFieldsTree);
                }
            }
            classContentTree.addContent(fieldWriter.getSerializableFields(
                    configuration.getText("doclet.Serialized_Form_fields"),
                    serializableFieldsTree));
        }
    }

    /**
     * Build the field sub header.
     *
     * @param node the XML element that specifies which components to document
     * @param fieldsContentTree content tree to which the documentation will be added
     */
    public void buildFieldSubHeader(XMLNode node, Content fieldsContentTree) {
        if (!currentClass.definesSerializableFields()) {
            gw.gosudoc.com.sun.javadoc.FieldDoc field = (gw.gosudoc.com.sun.javadoc.FieldDoc) currentMember;
            fieldWriter.addMemberHeader(field.type().asClassDoc(),
                    field.type().typeName(), field.type().dimension(), field.name(),
                    fieldsContentTree);
        }
    }

    /**
     * Build the field deprecation information.
     *
     * @param node the XML element that specifies which components to document
     * @param fieldsContentTree content tree to which the documentation will be added
     */
    public void buildFieldDeprecationInfo(XMLNode node, Content fieldsContentTree) {
        if (!currentClass.definesSerializableFields()) {
            gw.gosudoc.com.sun.javadoc.FieldDoc field = (gw.gosudoc.com.sun.javadoc.FieldDoc)currentMember;
            fieldWriter.addMemberDeprecatedInfo(field, fieldsContentTree);
        }
    }

    /**
     * Build the serial field tags information.
     *
     * @param serializableFieldsTree content tree to which the documentation will be added
     */
    public void buildSerialFieldTagsInfo(Content serializableFieldsTree) {
        if(configuration.nocomment){
            return;
        }
        gw.gosudoc.com.sun.javadoc.FieldDoc field = (gw.gosudoc.com.sun.javadoc.FieldDoc)currentMember;
        // Process Serializable Fields specified as array of
        // ObjectStreamFields. Print a member for each serialField tag.
        // (There should be one serialField tag per ObjectStreamField
        // element.)
        SerialFieldTag[] tags = field.serialFieldTags();
        Arrays.sort(tags);
        int tagsLength = tags.length;
        for (int i = 0; i < tagsLength; i++) {
            if (tags[i].fieldName() == null || tags[i].fieldType() == null) // ignore malformed @serialField tags
                continue;
            Content fieldsContentTree = fieldWriter.getFieldsContentHeader(
                    (i == tagsLength - 1));
            fieldWriter.addMemberHeader(tags[i].fieldTypeDoc(),
                    tags[i].fieldType(), "", tags[i].fieldName(), fieldsContentTree);
            fieldWriter.addMemberDescription(tags[i], fieldsContentTree);
            serializableFieldsTree.addContent(fieldsContentTree);
        }
    }

    /**
     * Build the field information.
     *
     * @param node the XML element that specifies which components to document
     * @param fieldsContentTree content tree to which the documentation will be added
     */
    public void buildFieldInfo(XMLNode node, Content fieldsContentTree) {
        if(configuration.nocomment){
            return;
        }
        gw.gosudoc.com.sun.javadoc.FieldDoc field = (gw.gosudoc.com.sun.javadoc.FieldDoc)currentMember;
        gw.gosudoc.com.sun.javadoc.ClassDoc cd = field.containingClass();
        // Process default Serializable field.
        if ((field.tags("serial").length == 0) && ! field.isSynthetic()
                && configuration.serialwarn) {
            configuration.message.warning(field.position(),
                    "doclet.MissingSerialTag", cd.qualifiedName(),
                    field.name());
        }
        fieldWriter.addMemberDescription(field, fieldsContentTree);
        fieldWriter.addMemberTags(field, fieldsContentTree);
    }

    /**
     * Return true if the given Doc should be included
     * in the serialized form.
     *
     * @param doc the Doc object to check for serializability.
     */
    public static boolean serialInclude( gw.gosudoc.com.sun.javadoc.Doc doc) {
        if (doc == null) {
            return false;
        }
        return doc.isClass() ?
            serialClassInclude((gw.gosudoc.com.sun.javadoc.ClassDoc)doc) :
            serialDocInclude(doc);
    }

    /**
     * Return true if the given ClassDoc should be included
     * in the serialized form.
     *
     * @param cd the ClassDoc object to check for serializability.
     */
    private static boolean serialClassInclude( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        if (cd.isEnum()) {
            return false;
        }
        try {
            cd.superclassType();
        } catch (NullPointerException e) {
            //Workaround for null pointer bug in ClassDoc.superclassType().
            return false;
        }
        if (cd.isSerializable()) {
            if (cd.tags("serial").length > 0) {
                return serialDocInclude(cd);
            } else if (cd.isPublic() || cd.isProtected()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Return true if the given Doc should be included
     * in the serialized form.
     *
     * @param doc the Doc object to check for serializability.
     */
    private static boolean serialDocInclude( gw.gosudoc.com.sun.javadoc.Doc doc) {
        if (doc.isEnum()) {
            return false;
        }
        gw.gosudoc.com.sun.javadoc.Tag[] serial = doc.tags("serial");
        if (serial.length > 0) {
            String serialtext = StringUtils.toLowerCase(serial[0].text());
            if (serialtext.contains("exclude")) {
                return false;
            } else if (serialtext.contains("include")) {
                return true;
            }
        }
        return true;
    }

    /**
     * Return true if any of the given classes have a @serialinclude tag.
     *
     * @param classes the classes to check.
     * @return true if any of the given classes have a @serialinclude tag.
     */
    private boolean serialClassFoundToDocument( gw.gosudoc.com.sun.javadoc.ClassDoc[] classes) {
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc aClass : classes) {
            if (serialClassInclude(aClass)) {
                return true;
            }
        }
        return false;
    }
}
