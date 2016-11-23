package editor.plugin.typeloader.properties;

import editor.IIssue;
import editor.IIssueContainer;
import java.util.Collections;
import java.util.List;

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
