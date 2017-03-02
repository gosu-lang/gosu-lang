package editor.run;

import com.sun.jdi.VirtualMachine;

/**
 */
public interface IProcessRunner<T extends IRunConfig>
{
  void execute( T typeName );
  RunState getRunState();
  Process getProcess();
  VirtualMachine getVm();

  T getRunConfig();
}
