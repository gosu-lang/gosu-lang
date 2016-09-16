package editor;

/**
 */
public interface IProcessRunner
{
  void execute( String fqn, GosuPanel gosuPanel );
  Process getProcess();
}
