package editor.run;

/**
 */
public enum DebugTransport
{
  AttachingSocket( "com.sun.jdi.SocketAttach", "Socket: Attach to already running process",
                   "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=$port" ),
  ListeningSocket( "com.sun.jdi.SocketListen", "Socket: Listen for a process to launch",
                   "-agentlib:jdwp=transport=dt_socket,server=n,suspend=n,address=$host:$port" ),
  AttachingMemory( "com.sun.jdi.SharedMemoryAttach", "Shared memory: Attach to already running process",
                   "-agentlib:jdwp=transport=dt_shmem,server=y,suspend=y,address=$address" ),
  ListeningMemory( "com.sun.jdi.SharedMemoryListen", "Shared memory: Listen for a process to launch",
                   "-agentlib:jdwp=transport=dt_shmem,server=n,suspend=n,address=$address");

  private final String _fqn;
  private final String _displayName;
  private final String _agentArgs;

  DebugTransport( String fqn, String displayName, String agentArgs )
  {
    _fqn = fqn;
    _displayName = displayName;
    _agentArgs = agentArgs;
  }

  public String getDisplayName()
  {
    return _displayName;
  }

  public String getFqn()
  {
    return _fqn;
  }

  public String getAgentArgs()
  {
    return _agentArgs;
  }

  public String toString()
  {
    return getDisplayName();
  }
}
