/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.smartcopy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.impl.EditorImpl;
import gw.plugin.ij.actions.TypeSystemAwareAction;

import java.io.IOException;
import java.io.StringReader;

public class CopyUnquotedAction extends TypeSystemAwareAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    EditorComponentImpl editorComponent = (EditorComponentImpl) e.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    EditorImpl editor = editorComponent.getEditor();
    String selectedText = editor.getSelectionModel().getSelectedText();
    if (selectedText != null && selectedText.length() > 0) {
      boolean isCtrlPressed = false;
      ClipboardUtil.copyToClipboard(process(selectedText, isCtrlPressed));
    }
  }

  public void updateImpl(AnActionEvent event) {
    Presentation presentation = event.getPresentation();
    EditorComponentImpl editorComponent = (EditorComponentImpl) event.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    EditorImpl editor = editorComponent.getEditor();
    String selectedText = editor.getSelectionModel().getSelectedText();
    presentation.setVisible(selectedText != null && selectedText.length() > 0);
  }

  public static class Tokenizer {

    private StringBuilder builder = new StringBuilder(128);
    private StringReader reader;
    private String EOLChar;

    public Tokenizer(String text, boolean useCRLF) throws IOException {
      reader = new StringReader(text);
      EOLChar = useCRLF ? "\r\n" : "\n";
      skipFirst();
    }

    /**
     * If we find a quote with only leading whitespace, assume it's the opening
     * quote.  Otherwise, assume the selection context started inside quotes.
     *
     * @throws IOException
     */
    private void skipFirst() throws IOException {
      int ch = reader.read();
      while (ch != -1 && Character.isWhitespace(ch)) {
        ch = reader.read();
      }
      if (ch != '"') {
        reader.reset();
      }
    }

    /**
     * Search for next quoted block.  Context is outside quotes at this point.
     * Ignore anything marked by // and slash-star .. star-slash comment blocks.
     *
     * @throws IOException
     */
    private void skipNext() throws IOException {
      int ch = reader.read();
      int peekc;
      while (ch != -1 && ch != '"') {
        peekc = -1;
        if (ch == '/') {
          ch = reader.read();
          if (ch == '/') {
            skipTo('\n');
          } else if (ch == '*') {
            do {
              skipTo('*');
              ch = reader.read();
              if (ch == -1 || ch == '/') {
                break;
              } else {
                peekc = ch;
              }
            } while (true);
          } else {
            peekc = ch;
          }
        } else if (ch == 'C') {
          ch = reader.read();
          if (ch == 'R') {
            builder.append(EOLChar);
          } else {
            peekc = ch;
          }
        }
        ch = peekc != -1 ? peekc : reader.read();
      }
    }

    /**
     * Read and throwaway characters until eof or specific char hit.
     *
     * @param next
     * @return
     * @throws IOException
     */
    private int skipTo(char next) throws IOException {
      int ch = reader.read();
      while (ch != -1 && ch != next) {
        ch = reader.read();
      }
      return ch;
    }

    /**
     * Get next string, quotes removed, escaped chars already processed
     * and caret markers removed.
     *
     * @return
     * @throws IOException
     */
    public String nextString() throws IOException {
      builder.setLength(0);

      // -1 start means EOF, return null;
      int ch = reader.read();
      if (ch == -1) {
        return null;
      }

      do {
        if (ch == '"') {
          break;
        }
        if (ch == '\\') {
          ch = reader.read();
          switch (ch) {
            case 'n':
              ch = '\n';
              break;
            case 'r':
              ch = '\r';
              break;
            case 't':
              ch = '\t';
              break;
            case 'f':
              ch = '\f';
              break;
            case 'b':
              ch = '\b';
              break;
          }
        } else if (ch == '^') {
          ch = reader.read();
          if (ch == '^') {
            ch = reader.read();
            continue;
          } else if (Character.isUpperCase(ch)) {
            int middle = ch;
            ch = reader.read();
            if (ch == '^') {
              ch = reader.read();
            } else {
              builder.append('^');
              builder.append((char) middle);
            }
            continue;
          }
        } else if (matchPairOrTriple('[', ch, false)
            || matchPairOrTriple(']', ch, false)
            ) {
          ch = reader.read();
          continue;
        } else if (matchPair('#', ch)
            || matchPair('%', ch)
            || matchPair('!', ch)
            ) {
          ch = reader.read();
          continue;
        }
        if (ch == -1) {
          break;
        }
        builder.append((char) ch);
        ch = reader.read();
      } while (true);

      reader.mark(0);
      skipNext();
      return builder.toString();
    }

    private boolean matchPair(char pattern, int ch) throws IOException {
      if(pattern == (char) ch) {
        int ch2 = reader.read();
        if (ch2 == pattern) {
        } else {
          builder.append((char) ch);
          builder.append((char) ch2);
        }
        return true;
      } else {
        return false;
      }
    }

    private boolean matchPairOrTriple(char pattern, int ch, boolean matchTripleOnly) throws IOException {
      if (pattern == ch) {
        int ch2 = reader.read();
        if (pattern == ch2) {
          int ch3 = reader.read();
          if (pattern == ch3) {
          } else {
            if(matchTripleOnly) {
              builder.append((char) ch);
              builder.append((char) ch2);
            }
            builder.append((char) ch3);
          }
          return true;
        } else {
          builder.append((char) ch);
          builder.append((char) ch2);
        }
        return true;
      } else {
        return false;
      }
    }

  }

  private String process(String text, boolean useCRLF) {
    StringBuilder builder = new StringBuilder(text.length());
    try {
      // format:   {ws} " {stuff} | {\ + "<$\nrt} * " {ws} + ...
      // output:   {stuff} | "$\nrt *
      Tokenizer tokenizer = new Tokenizer(text, useCRLF);
      String part;
      while ((part = tokenizer.nextString()) != null) {
        builder.append(part);
      }
    } catch (IOException ex) {
      builder.append(" " + ex.getLocalizedMessage());
      ex.printStackTrace();
    }

    return builder.toString();
  }
}
