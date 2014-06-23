/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaindexer;

import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaFlattenedChild;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertyType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaChoiceIndexer extends XmlSchemaIndexer<XmlSchemaChoice> {
  @Override
    public <T> void index( XmlSchemaChoice choice, String prefix, List<XmlSchemaFlattenedChild> flattenedChildren, boolean topLevel, boolean isPlural, XmlSchemaType currentType, boolean createTypes, Map<QName, XmlSchemaElementTypeData> usedElementNames, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap, T context, List<Runnable> todo ) {
    isPlural |= choice.getMaxOccurs() > 1;
    // combinedLocalPluralityMap will collect the maximum plurality of each child among all of the choices of this xsd:choice
    // plural > singular > doesnotexist
    Map<Pair<XmlSchemaPropertyType, QName>, Boolean> combinedLocalPluralityMap = new HashMap<Pair<XmlSchemaPropertyType, QName>, Boolean>();
    for ( XmlSchemaObject member : choice.getItems() ) {
      Map<Pair<XmlSchemaPropertyType, QName>, Boolean> localPluralityMap = new HashMap<Pair<XmlSchemaPropertyType, QName>, Boolean>();
      XmlSchemaIndexer.invokeIndexer( member, prefix, flattenedChildren, false, isPlural, currentType, createTypes, usedElementNames, localPluralityMap, context, todo );
      for ( Map.Entry<Pair<XmlSchemaPropertyType, QName>, Boolean> entry : localPluralityMap.entrySet() ) {
        Pair<XmlSchemaPropertyType, QName> pair = entry.getKey();
        Boolean oldPlural = combinedLocalPluralityMap.get( pair );
        if ( oldPlural != null && oldPlural ) {
          continue; // was already plural in one of the choices - leave plural even if in this choice it was not plural
        }
        // so it either didn't exist before, or wasn't plural - set plurality equal to this choice's determination of plurality
        boolean newPlural = entry.getValue();
        combinedLocalPluralityMap.put( pair, newPlural );
      }
    }
    // now step through the combined plurality map for the choices, and actually change the original map using the normal approach
    for ( Map.Entry<Pair<XmlSchemaPropertyType, QName>, Boolean> entry : combinedLocalPluralityMap.entrySet() ) {
      Pair<XmlSchemaPropertyType, QName> pair = entry.getKey();
      boolean plural = isPlural || entry.getValue();
      setPlurality( pluralityMap, pair.getFirst(), pair.getSecond(), plural );
    }
  }
}