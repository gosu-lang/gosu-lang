package gw.lang;

import manifold.internal.javac.IIssueContainer;

/**
 */
public interface ICompilationResults
{
  IIssueContainer getIssues();
  byte[] getBytes();
}
