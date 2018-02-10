package editor.plugin.typeloader.properties;

import manifold.internal.javac.IIssue;
import java.util.Collections;
import java.util.List;
import manifold.internal.javac.IIssueContainer;

/**
 */
public class PropertiesIssueContainer implements IIssueContainer
{
  @Override
  public List<IIssue> getIssues()
  {
    return Collections.emptyList();
  }

  @Override
  public List<IIssue> getWarnings()
  {
    return Collections.emptyList();
  }

  @Override
  public List<IIssue> getErrors()
  {
    return Collections.emptyList();
  }

  @Override
  public boolean isEmpty()
  {
    return true;
  }
}
