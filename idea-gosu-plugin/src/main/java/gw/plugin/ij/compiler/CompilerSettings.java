/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jdom.Element;

@State(
        name = "GosuCompilerSettings",
        storages = {
                @Storage(
                        file = "$APP_CONFIG$/gosu_compiler_settings.xml"
                )}
)
public class CompilerSettings implements PersistentStateComponent<Element> {

  private static final String GOSU_COMPILER_SETTINGS_TAG = "GosuCompilerSettings";

  private static final String ExternalCompilerMemory = "ExternalCompilerMemory";
  private static final String ExternalToIncrementalCompilerLimit = "ExternalToIncrementalCompilerLimit";
  private static final int DEFAULT_Xmx = 5120;
  private static final int DEFAULT_INCREMENTAL_LIMIT = 200;

  private int myExternalCompilerMemory = DEFAULT_Xmx;
  private int myExternalToIncrementalCompiler = DEFAULT_INCREMENTAL_LIMIT;


  public static CompilerSettings getInstance() {
    return ServiceManager.getService(CompilerSettings.class);
  }

  @Override
  public Element getState() {
    final Element element = new Element(GOSU_COMPILER_SETTINGS_TAG);

    element.setAttribute(ExternalCompilerMemory, String.valueOf(myExternalCompilerMemory));
    element.setAttribute(ExternalToIncrementalCompilerLimit, String.valueOf(myExternalToIncrementalCompiler));

    return element;
  }

  @Override
  public void loadState(Element state) {
    setExternalCompilerMemory(Integer.valueOf(state.getAttributeValue(ExternalCompilerMemory, String.valueOf(DEFAULT_Xmx))));
    setExternalToIncrementalCompiler(Integer.valueOf(state.getAttributeValue(ExternalToIncrementalCompilerLimit, String.valueOf(DEFAULT_INCREMENTAL_LIMIT))));
  }

  public int getExternalCompilerMemory() {
    return myExternalCompilerMemory;
  }

  public void setExternalCompilerMemory(int myExternalCompilerMemory) {
    this.myExternalCompilerMemory = myExternalCompilerMemory;
  }

  public int getExternalToIncrementalCompilerLimit() {
    return myExternalToIncrementalCompiler;
  }

  public void setExternalToIncrementalCompiler(int myExternalToIncrementalCompiler) {
    this.myExternalToIncrementalCompiler = myExternalToIncrementalCompiler;
  }
}
