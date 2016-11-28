package gw.lang;

/**
 */
public interface ICompilationResults
{
  IIssueContainer getIssues();
  byte[] getBytes();
}
