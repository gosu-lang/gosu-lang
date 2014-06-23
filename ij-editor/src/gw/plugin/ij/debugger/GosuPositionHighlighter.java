/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.intellij.debugger.DebuggerBundle;
import com.intellij.debugger.DebuggerInvocationUtil;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.DebugProcessEvents;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.events.DebuggerContextCommandImpl;
import com.intellij.debugger.impl.*;
import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.debugger.jdi.ThreadReferenceProxyImpl;
import com.intellij.debugger.ui.breakpoints.*;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.EventDispatcher;
import com.intellij.util.StringBuilderSpinAllocator;
import com.intellij.xdebugger.impl.actions.ViewBreakpointsAction;
import com.intellij.xdebugger.ui.DebuggerColors;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.MethodEntryEvent;
import gw.plugin.ij.editors.IEmbedsGosuEditors;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.filetypes.IGosuFileTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;

public class GosuPositionHighlighter {
  private static final Key<Boolean> HIGHLIGHTER_USERDATA_KEY = new Key<>("HIGHLIGHTER_USERDATA_KEY");
  private static final Logger LOG = Logger.getInstance("#com.intellij.debugger.ui.PositionHighlighter");
  private final Project myProject;
  private DebuggerContextImpl myContext = DebuggerContextImpl.EMPTY_CONTEXT;
  @Nullable
  private SelectionDescription mySelectionDescription = null;
  @Nullable
  private ExecutionPointDescription myExecutionPointDescription = null;

  public GosuPositionHighlighter(Project project, @NotNull DebuggerStateManager stateManager) {
    myProject = project;
    try {
      Field fieldDispacther = DebuggerStateManager.class.getDeclaredField("myEventDispatcher");
      fieldDispacther.setAccessible(true);
      EventDispatcher eventDispatcher = (EventDispatcher) fieldDispacther.get(stateManager);
      EventListener toRemove = null;
      for(Object l : eventDispatcher.getListeners()) {
        if(l.toString().startsWith("com.intellij.debugger.ui.PositionHighlighter")) {
          toRemove = (EventListener) l;
          break;
        }
      }
      if(toRemove != null) {
        eventDispatcher.removeListener(toRemove);
      }

    } catch (NoSuchFieldException e) {
    } catch (IllegalAccessException e) {
    }
    stateManager.addListener(new DebuggerContextListener() {
      public void changeEvent(DebuggerContextImpl newContext, int event) {
        myContext = newContext;
        if (event != DebuggerSession.EVENT_REFRESH_VIEWS_ONLY && event != DebuggerSession.EVENT_THREADS_REFRESH) {
          refresh();
        }
      }
    });
  }

  private void showLocationInEditor() {
    myContext.getDebugProcess().getManagerThread().schedule(new ShowLocationCommand(myContext));
  }

  private void refresh() {
    clearSelections();
    final DebuggerSession session = myContext.getDebuggerSession();
    if (session != null) {
      switch (session.getState()) {
        case DebuggerSession.STATE_PAUSED:
          if (myContext.getFrameProxy() != null) {
            showLocationInEditor();
            return;
          }
          break;
      }
    }
  }

  protected static class ExecutionPointDescription extends SelectionDescription {
    @Nullable
    private RangeHighlighter myHighlighter;
    private final int myLineIndex;

    protected ExecutionPointDescription(Editor editor, int lineIndex) {
      super(editor);
      myLineIndex = lineIndex;
    }

    public void select() {
      if (myIsActive) return;
      myIsActive = true;
      EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
      myHighlighter = myEditor.getMarkupModel().addLineHighlighter(
          myLineIndex,
          DebuggerColors.EXECUTION_LINE_HIGHLIGHTERLAYER,
          scheme.getAttributes(DebuggerColors.EXECUTIONPOINT_ATTRIBUTES)
      );
      myHighlighter.setErrorStripeTooltip(DebuggerBundle.message("position.highlighter.stripe.tooltip"));
      myHighlighter.putUserData(HIGHLIGHTER_USERDATA_KEY, Boolean.TRUE);
    }

    public void remove() {
      if (!myIsActive) return;
      myIsActive = false;
      if (myHighlighter != null) {
        myHighlighter.dispose();
        myHighlighter = null;
      }
    }

    @Nullable
    public RangeHighlighter getHighlighter() {
      return myHighlighter;
    }
  }

  protected abstract static class SelectionDescription {
    protected final Editor myEditor;
    protected boolean myIsActive;

    public SelectionDescription(Editor editor) {
      myEditor = editor;
    }

    public abstract void select();

    public abstract void remove();

    @NotNull
    public static ExecutionPointDescription createExecutionPoint(final Editor editor,
                                                                 final int lineIndex) {
      return new ExecutionPointDescription(editor, lineIndex);
    }

    @NotNull
    public static SelectionDescription createSelection(@NotNull final Editor editor, final int lineIndex) {
      return new SelectionDescription(editor) {
        public void select() {
          if (myIsActive) return;
          myIsActive = true;
          DocumentEx doc = (DocumentEx) editor.getDocument();
          editor.getSelectionModel().setSelection(
              doc.getLineStartOffset(lineIndex),
              doc.getLineEndOffset(lineIndex) + doc.getLineSeparatorLength(lineIndex)
          );
        }

        public void remove() {
          if (!myIsActive) return;
          myIsActive = false;
          myEditor.getSelectionModel().removeSelection();
        }
      };
    }
  }

  private void showSelection(@NotNull SourcePosition position) {
    Editor editor = getEditor(position);
    if (editor == null) {
      return;
    }
    if (mySelectionDescription != null) {
      mySelectionDescription.remove();
    }
    mySelectionDescription = SelectionDescription.createSelection(editor, position.getLine());
    mySelectionDescription.select();
  }

  private void showExecutionPoint(@NotNull final SourcePosition position, @NotNull List<Pair<Breakpoint, Event>> events) {
    if (myExecutionPointDescription != null) {
      myExecutionPointDescription.remove();
    }
    int lineIndex = position.getLine();
    Editor editor = getEditor(position);
    if (editor == null) {
      return;
    }
    myExecutionPointDescription = SelectionDescription.createExecutionPoint(editor, lineIndex);
    myExecutionPointDescription.select();

    RangeHighlighter highlighter = myExecutionPointDescription.getHighlighter();

    if (highlighter != null) {
      final List<Pair<Breakpoint, Event>> eventsOutOfLine = new ArrayList<>();

      for (final Pair<Breakpoint, Event> eventDescriptor : events) {
        final Breakpoint breakpoint = eventDescriptor.getFirst();
        // filter breakpoints that do not match the event
        if (breakpoint instanceof MethodBreakpoint) {
          try {
            if (!((MethodBreakpoint) breakpoint).matchesEvent((LocatableEvent) eventDescriptor.getSecond(), myContext.getDebugProcess())) {
              continue;
            }
          } catch (EvaluateException ignored) {
          }
        } else if (breakpoint instanceof WildcardMethodBreakpoint) {
          if (!((WildcardMethodBreakpoint) breakpoint).matchesEvent((LocatableEvent) eventDescriptor.getSecond())) {
            continue;
          }
        }

        if (breakpoint instanceof BreakpointWithHighlighter) {
          if (((BreakpointWithHighlighter) breakpoint).isVisible() && breakpoint.isValid()) {
            breakpoint.reload();
            final SourcePosition sourcePosition = ((BreakpointWithHighlighter) breakpoint).getSourcePosition();
            if (sourcePosition == null || sourcePosition.getLine() != lineIndex) {
              eventsOutOfLine.add(eventDescriptor);
            }
          }
        } else {
          eventsOutOfLine.add(eventDescriptor);
        }
      }

      if (!eventsOutOfLine.isEmpty()) {
        highlighter.setGutterIconRenderer(new MyGutterIconRenderer(eventsOutOfLine));
      }
    }
  }

  @Nullable
  private Editor getEditor(@NotNull SourcePosition position) {
    final PsiFile psiFile = position.getFile();
    Document doc = PsiDocumentManager.getInstance(myProject).getDocument(psiFile);
    if (!psiFile.isValid()) {
      return null;
    }
    final int lineIndex = position.getLine();
    if (lineIndex < 0 || lineIndex > doc.getLineCount()) {
      //LOG.assertTrue(false, "Incorrect lineIndex " + lineIndex + " in file " + psiFile.getName());
      return null;
    }
    return openEditor(position, false);
  }

  @Nullable
  public Editor openEditor(@NotNull SourcePosition position, final boolean requestFocus) {
    final PsiFile psiFile = position.getFile();
    final Project project = psiFile.getProject();
    if (project.isDisposed()) {
      return null;
    }
    final VirtualFile virtualFile = psiFile.getVirtualFile();
    if (virtualFile == null || !virtualFile.isValid()) {
      return null;
    }
    final int offset = position.getOffset();
    if (offset < 0) {
      return null;
    }

    OpenFileDescriptor descriptor = null;
    IGosuFileTypeProvider fileTypeProvider = GosuCodeFileType.getFileTypeProvider(virtualFile);
    if (fileTypeProvider != null) {
      descriptor = fileTypeProvider.getOpenFileDescriptor(project, virtualFile, offset);
    }

    return openTextEditor(descriptor != null ? descriptor : new OpenFileDescriptor(project, virtualFile, offset), requestFocus);
  }

  @Nullable
  public Editor openTextEditor(@NotNull final OpenFileDescriptor descriptor, final boolean focusEditor) {
    FileEditorManager manager = FileEditorManager.getInstance(descriptor.getProject());
    final Collection<FileEditor> fileEditors = manager.openEditor(descriptor, focusEditor);
    for (FileEditor fileEditor : fileEditors) {
        if (fileEditor instanceof IEmbedsGosuEditors) {
          return ((IEmbedsGosuEditors) fileEditor).getGosuEditorAt(descriptor);
        }
    }
    return manager.openTextEditor(descriptor,focusEditor);
  }

  private void clearSelections() {
    if (mySelectionDescription != null || myExecutionPointDescription != null) {
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        public void run() {
          if (mySelectionDescription != null) {
            mySelectionDescription.remove();
            mySelectionDescription = null;
          }
          if (myExecutionPointDescription != null) {
            myExecutionPointDescription.remove();
            myExecutionPointDescription = null;
          }
        }
      });
    }
  }


  public void updateContextPointDescription() {
    if (myContext.getDebuggerSession() == null) return;

    showLocationInEditor();
  }

  private class ShowLocationCommand extends DebuggerContextCommandImpl {
    private final DebuggerContextImpl myContext;

    public ShowLocationCommand(DebuggerContextImpl context) {
      super(context);
      myContext = context;
    }

    public void threadAction() {
      final SourcePosition contextPosition = myContext.getSourcePosition();
      if (contextPosition == null) {
        return;
      }

      boolean isExecutionPoint = false;
      try {
        StackFrameProxyImpl frameProxy = myContext.getFrameProxy();
        final ThreadReferenceProxyImpl thread = getSuspendContext().getThread();
        isExecutionPoint = thread != null && frameProxy.equals(thread.frame(0));
      } catch (Throwable th) {
        LOG.debug(th);
      }

      final List<Pair<Breakpoint, Event>> events = DebuggerUtilsEx.getEventDescriptors(getSuspendContext());

      final SourcePosition position = ApplicationManager.getApplication().runReadAction(new Computable<SourcePosition>() {
        public SourcePosition compute() {
          Document document = PsiDocumentManager.getInstance(myProject).getDocument(contextPosition.getFile());
          if (document != null) {
            if (contextPosition.getLine() < 0 || contextPosition.getLine() >= document.getLineCount()) {
              return SourcePosition.createFromLine(contextPosition.getFile(), 0);
            }
          }
          return contextPosition;
        }
      });

      if (isExecutionPoint) {
        DebuggerInvocationUtil.swingInvokeLater(myProject, new Runnable() {
          public void run() {
            final SourcePosition highlightPosition = getHighlightPosition(events, position);
            showExecutionPoint(highlightPosition, events);
          }
        });
      } else {
        DebuggerInvocationUtil.swingInvokeLater(myProject, new Runnable() {
          public void run() {
            showSelection(position);
          }
        });
      }
    }

    @NotNull
    private SourcePosition getHighlightPosition(@NotNull final List<Pair<Breakpoint, Event>> events, @NotNull SourcePosition position) {
      for (final Pair<Breakpoint, Event> eventDescriptor : events) {
        final Breakpoint breakpoint = eventDescriptor.getFirst();
        if (breakpoint instanceof LineBreakpoint) {
          breakpoint.reload();
          final SourcePosition breakPosition = ((BreakpointWithHighlighter) breakpoint).getSourcePosition();
          if (breakPosition != null && breakPosition.getLine() != position.getLine()) {
            position = SourcePosition.createFromLine(position.getFile(), breakPosition.getLine());
          }
        } else if (breakpoint instanceof MethodBreakpoint) {
          final MethodBreakpoint methodBreakpoint = (MethodBreakpoint) breakpoint;
          methodBreakpoint.reload();
          final SourcePosition breakPosition = methodBreakpoint.getSourcePosition();
          final LocatableEvent event = (LocatableEvent) eventDescriptor.getSecond();
          if (breakPosition != null && breakPosition.getFile().equals(position.getFile()) && breakPosition.getLine() != position.getLine() && event instanceof MethodEntryEvent) {
            try {
              if (methodBreakpoint.matchesEvent(event, myContext.getDebugProcess())) {
                position = SourcePosition.createFromLine(position.getFile(), breakPosition.getLine());
              }
            } catch (EvaluateException ignored) {
            }
          }
        }
      }
      return position;
    }
  }

  private class MyGutterIconRenderer extends GutterIconRenderer {
    private final List<Pair<Breakpoint, Event>> myEventsOutOfLine;

    public MyGutterIconRenderer(List<Pair<Breakpoint, Event>> eventsOutOfLine) {
      myEventsOutOfLine = eventsOutOfLine;
    }

    @NotNull
    public Icon getIcon() {
      return myEventsOutOfLine.get(0).getFirst().getIcon();
    }

    public String getTooltipText() {
      DebugProcessImpl debugProcess = myContext.getDebugProcess();
      if (debugProcess == null) {
        return null;
      }
      final StringBuilder buf = StringBuilderSpinAllocator.alloc();
      try {
        //noinspection HardCodedStringLiteral
        buf.append("<html><body>");
        for (Iterator<Pair<Breakpoint, Event>> iterator = myEventsOutOfLine.iterator(); iterator.hasNext(); ) {
          Pair<Breakpoint, Event> eventDescriptor = iterator.next();
          buf.append(((DebugProcessEvents) debugProcess).getEventText(eventDescriptor));
          if (iterator.hasNext()) {
            //noinspection HardCodedStringLiteral
            buf.append("<br>");
          }
        }
        //noinspection HardCodedStringLiteral
        buf.append("</body></html>");
        return buf.toString();
      } finally {
        StringBuilderSpinAllocator.dispose(buf);
      }
    }

    public ActionGroup getPopupMenuActions() {
      DefaultActionGroup group = new DefaultActionGroup();
      for (Pair<Breakpoint, Event> eventDescriptor : myEventsOutOfLine) {
        Breakpoint breakpoint = eventDescriptor.getFirst();
        ViewBreakpointsAction viewBreakpointsAction = new ViewBreakpointsAction(breakpoint.getDisplayName(), breakpoint);
        group.add(viewBreakpointsAction);
      }

      return group;
    }

    @Override
    public boolean equals(@NotNull Object obj) {
      return obj instanceof MyGutterIconRenderer &&
          Comparing.equal(getTooltipText(), ((MyGutterIconRenderer) obj).getTooltipText()) &&
          Comparing.equal(getIcon(), ((MyGutterIconRenderer) obj).getIcon());
    }

    @Override
    public int hashCode() {
      return getIcon().hashCode();
    }
  }
}
