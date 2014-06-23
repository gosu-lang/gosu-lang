/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.google.common.base.Objects;
import com.intellij.ide.DataManager;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PlainTextTokenTypes;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.impl.TextBlock;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.PlainTextASTFactory;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.io.StringRef;
import gw.config.CommonServices;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFileStub;
import gw.plugin.ij.lang.psi.stubs.GosuFileStubBuilder;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class GosuStubFileElementType extends IStubFileElementType<GosuFileStub> {
  public static final Key<Boolean> KEY_RAW_TEXT = new Key<>( "RawText" );
  private final String _externalId;
  private boolean _bSkipParsingWhileTyping;

  public GosuStubFileElementType(String externalId, Language language) {
    super(externalId, language);

    _externalId = externalId;
    _bSkipParsingWhileTyping = true;
  }

  @NotNull
  public StubBuilder getBuilder() {
    return new GosuFileStubBuilder();
  }

  @Override
  public int getStubVersion() {
    return super.getStubVersion() + 2;
  }

  public String getExternalId() {
    return _externalId;
  }

  public boolean isSkipParsingWhileTyping() {
    return _bSkipParsingWhileTyping;
  }
  public void setSkipParsingWhileTyping( boolean bParseFastWhileTyping ) {
    _bSkipParsingWhileTyping = bParseFastWhileTyping;
  }

  public ASTNode parseContents(@NotNull ASTNode chameleon) {
    AbstractGosuClassFileImpl psiFile = (AbstractGosuClassFileImpl) chameleon.getPsi();
    IModule module = psiFile.getModule();
    TypeSystem.pushModule(module);
    try {
      //TODO-dp remove TypeSystem.getCurrentCompilingType() != null which was put in to avoid reentrancy of the Gosu parser
      String content = psiFile.getText();
      if (CommonServices.getPlatformHelper().isPathIgnored(FileUtil.getFileFromPsi(psiFile).getPath()) ||
        isDoNotVerify( content ) ||
        TypeSystem.getCurrentCompilingType() != null) {
        return new PlainTextASTFactory().createLeaf(PlainTextTokenTypes.PLAIN_TEXT, psiFile.getText());
      }
      else if( isSkipParsingWhileTyping() && wasModifiedInEditor( psiFile, content ) ) {
        //System.out.println( "Skipping Psi: " + psiFile.getName() );
        return skipParsing( psiFile );
      }
      else {
        //System.out.println( "Building Psi: " + psiFile.getName() );
        return psiFile.parse(chameleon); // bad: refresh(psiFile);
      }
    } catch (RuntimeException e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw new ProcessCanceledException(e.getCause());
      }
      printStackTrace(e, psiFile);
    } catch (Throwable e) {
      printStackTrace(e, psiFile);
    } finally {
      TypeSystem.popModule(module);
    }

    return null;
  }

  private ASTNode skipParsing( AbstractGosuClassFileImpl psiFile ) {
    LeafElement node = new PlainTextASTFactory().createLeaf( PlainTextTokenTypes.PLAIN_TEXT, psiFile.getText() );
    node.putUserData( KEY_RAW_TEXT, true );
    return node;
  }

  private boolean wasModifiedInEditor( AbstractGosuClassFileImpl psiFile, String content ) {
    AbstractGosuClassFileImpl originalFile = (AbstractGosuClassFileImpl)psiFile.getOriginalFile();
    if( originalFile != null ) {
      if( !originalFile.isReparse() ) {
        final Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        FileEditor editor = PlatformDataKeys.FILE_EDITOR.getData( DataManager.getInstance().getDataContext(owner));
        if( editor instanceof PsiAwareTextEditorImpl && !isSingleLineEditor( (PsiAwareTextEditorImpl)editor ) ) {
          DataProvider component = ((PsiAwareTextEditorImpl)editor).getComponent();
          if( component != null ) {
            VirtualFile vfile = (VirtualFile)component.getData( PlatformDataKeys.VIRTUAL_FILE.getName() );
            if( vfile != null && vfile.equals( originalFile.getVirtualFile() ) ) {
              TextBlock textBlock = TextBlock.get( originalFile );
              boolean bChangedText = textBlock != null && !textBlock.isEmpty();
              if( !changeHasChar( new char[]{'.', '\n'}, content, textBlock ) ) {
                if( bChangedText ) {
                  //System.out.println( psiFile.getName() + " Changed" );
                }
                return bChangedText;
              }
            }
          }
        }
      }
      else {
        //originalFile.setReparse( false );
      }
    }
    return false;
  }

  private boolean isSingleLineEditor( PsiAwareTextEditorImpl editor ) {
    Editor activeEditor = editor.getEditor();
    return activeEditor != null && activeEditor.isOneLineMode();
  }

  private boolean changeHasChar( char[] chars, String content, TextBlock tb ) {
    int iStart = tb.getStartOffset();
    int iEnd = tb.getTextEndOffset();
    if( iStart >= 0 && iStart < iEnd ) {
      if( content.length() > iEnd ) {
        for( char c : chars ) {
          int iChar = content.indexOf( c, iStart );
          if( iChar > 0 && iChar < iEnd ) {
            return true;
          }
        }
      }
    }
    return false;
  }

//  private boolean isFromTyping( AbstractGosuClassFileImpl psiFile ) {
//    AbstractGosuClassFileImpl originalFile = (AbstractGosuClassFileImpl)psiFile.getOriginalFile();
//    if( originalFile != null ) {
//      if( !originalFile.isReparse() ) {
//        //!! this blows up b/c we may not be in the dispatch thread (can't invokeAndWait() either b/c deadlock)
//        FileEditorManager fem = FileEditorManagerImpl.getInstance( psiFile.getProject() );
//        VirtualFile vfile = originalFile.getVirtualFile();
//        if( vfile != null && fem.getSelectedEditor( vfile ) != null ) {
//          TextBlock textBlock = TextBlock.get( originalFile );
//          boolean bChangedText = textBlock != null && !textBlock.isEmpty();
//          if( bChangedText ) {
//            System.out.println( psiFile.getName() + " Changed" );
//          }
//          return bChangedText;
//        }
//      }
//      else {
//        originalFile.setReparse( false );
//      }
//    }
//    return false;
//  }

  private boolean isDoNotVerify(String text) {
    return false; //text != null && (text.contains("@DoNotVerifyResource") || text.contains("@gw.testharness.DoNotVerifyResource"));
  }

  private void printStackTrace(@NotNull Throwable e, @NotNull AbstractGosuClassFileImpl file) {
    final String message = Objects.firstNonNull(e.getMessage(), e.getClass().getSimpleName());
    new Exception(String.format("Error parsing %s: %s", file.getQualifiedClassNameFromFile(), message), e).printStackTrace();
  }

  @Override
  public void serialize(@NotNull final GosuFileStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
    IModule rootModule = stub.getPsi() != null
                         ? GosuModuleUtil.getGlobalModule(stub.getProject())
                         : null;
    if( rootModule != null ) {
      TypeSystem.pushModule( rootModule );
    }
    try {
      dataStream.writeName( stub.getPackageName().toString() );
      dataStream.writeName(stub.getName().toString());
      dataStream.writeName(stub.getExt().toString());
    }
    finally {
      if( rootModule != null ) {
        TypeSystem.popModule( rootModule );
      }
    }
  }

  @NotNull
  @Override
  public GosuFileStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
    final StringRef packageName = dataStream.readName();
    final StringRef name = dataStream.readName();
    final StringRef ext = dataStream.readName();
    return new GosuFileStub(packageName, name, ext);
  }
}
