/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.xml.BinaryData;

import java.util.Map;

public class UnmarshalContext {
  final private Map<String, BinaryData> _attachments;

  public UnmarshalContext(Map<String, BinaryData> attachments) {
    _attachments = attachments;
  }

  Map<String, BinaryData> getAttachments() {
    return _attachments;
  }
}
