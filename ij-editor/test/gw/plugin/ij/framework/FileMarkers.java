/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework;

import com.intellij.openapi.editor.Editor;
import gw.plugin.ij.framework.core.CodeInsightTestCase;
import junit.framework.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FileMarkers extends Assert {
  private final List<CaretMarker> carets = new ArrayList<>();
  private final List<CaretMarker> postCarets = new ArrayList<>();
  private final List<SmartTextRange> ranges = new ArrayList<>();
  private final List<SmartTextRange> deltas = new ArrayList<>();
  private String textWithDeltas;
  private String text;

  public void addRange(int rangeStartIndex, int rangeEndIndex, String text) {
    ranges.add(new SmartTextRange(rangeStartIndex, rangeEndIndex, text, this));
  }

  public void addDelta(int deltaStartIndex, int deltaEndIndex, String text) {
    deltas.add(new SmartTextRange(deltaStartIndex, deltaEndIndex, text, this));
  }

  public int getCaretOffset() {
    assertEquals("No caret marker found.", 1, carets.size());
    CaretMarker caretMarker = carets.get(0);
    assertEquals(MarkerType.CARET1, caretMarker.type);
    return caretMarker.offset;
  }

  public int getCaretOffset(MarkerType type) {
    CaretMarker caretMarker = null;
    for (CaretMarker caret : carets) {
      if (caret.type.equals(type)) {
        caretMarker = caret;
      }
    }
    assertNotNull("No caret found " + type, caretMarker);
    return caretMarker.offset;
  }

  @NotNull
  public CaretMarker getCaret(MarkerType type) {
    CaretMarker caretMarker = null;
    for (CaretMarker caret : carets) {
      if (caret.type.equals(type)) {
        caretMarker = caret;
      }
    }
    assertNotNull("No caret found " + type, caretMarker);
    return caretMarker;
  }

  @NotNull
  public CaretMarker getPostCaret(MarkerType type) {
    CaretMarker caretMarker = maybeGetPostCaret(type);
    assertNotNull("No caret found " + type, caretMarker);
    return caretMarker;
  }

  @Nullable
  public CaretMarker maybeGetPostCaret(MarkerType type) {
    CaretMarker caretMarker = null;
    for (CaretMarker caret : postCarets) {
      if (caret.type.equals(type)) {
        caretMarker = caret;
      }
    }
    return caretMarker;
  }

  @NotNull
  public List<SmartTextRange> getRanges() {
    return ranges;
  }

  public void add(@NotNull FileMarkers that) {
    this.carets.addAll(that.carets);
    this.postCarets.addAll(that.postCarets);
    this.ranges.addAll(that.ranges);
    this.deltas.addAll(that.deltas);
  }

  public void setEditor(Editor editor) {
    for (CaretMarker caret : carets) {
      caret.setEditor(editor);
    }
    for (CaretMarker postCaret : postCarets) {
      postCaret.setEditor(editor);
    }
    for (SmartTextRange range : ranges) {
      range.setEditor(editor);
    }
  }

  @NotNull
  public List<SmartTextRange> getDeltas() {
    return deltas;
  }

  public void setTextWithDeltas(String textWithDeltas) {
    CodeInsightTestCase.CodeTokenizer codeTokenizer = new CodeInsightTestCase.CodeTokenizer(textWithDeltas);

    while (codeTokenizer.next()) {
      if (codeTokenizer.markerType == MarkerType.CARET1 ||
          codeTokenizer.markerType == MarkerType.CARET2 ||
          codeTokenizer.markerType == MarkerType.CARET3 ||
          codeTokenizer.markerType == MarkerType.CARET4) {
        postCarets.add(new CaretMarker(codeTokenizer.markerIndex, codeTokenizer.markerType, this));
      }
    }

    for (MarkerType value : MarkerType.values()) {
      textWithDeltas = textWithDeltas.replace(value.markerText, "");
    }
    this.textWithDeltas = textWithDeltas;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void addCaret(int markerIndex, MarkerType markerType) {
    carets.add(new CaretMarker(markerIndex, markerType, this));
  }

  public String getTextWithDeltas() {
    return textWithDeltas;
  }
}
