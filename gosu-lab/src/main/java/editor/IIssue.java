package editor;

/**
 */
public interface IIssue
{
  enum Kind { Error, Warning }

  Kind getKind();
  int getStartOffset();
  int getEndOffset();
  int getLine();
  int getColumn();
  String getMessage();
}
