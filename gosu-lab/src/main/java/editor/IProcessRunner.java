package editor;

import com.sun.jdi.VirtualMachine;

/**
 */
public interface IProcessRunner
{
  void execute( String fqn, GosuPanel gosuPanel );
  RunState getRunState();
  Process getProcess();
  VirtualMachine getVm();

  String getTypeName();
}
