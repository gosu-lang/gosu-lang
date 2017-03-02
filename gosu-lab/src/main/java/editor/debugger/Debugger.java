package editor.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.RunMe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 */
public class Debugger
{
  private static final String TEMPORARY = "temporary";
  private static final String FROM_LOCATION = "from_location";
  private static final String BACK_OUT_LOCATION = "back_out_location";
  private static final boolean DEBUG = false;

  private Thread _debuggerThread;
  private final BreakpointManager _bpm;
  private VirtualMachine _vm;
  private EventSet _eventSet;
  private boolean _vmExit;
  private Location _location;
  private ThreadReference _eventThread;
  private Map<String, ClassPrepareRequest> _classPrepareRequests;
  private final Object _monitor;
  private boolean _bPaused;
  private List<Consumer<Debugger>> _listeners;
  private String _eventName;

  public Debugger( VirtualMachine vm, BreakpointManager bpm )
  {
    _vm = vm;
    _bpm = bpm;
    _classPrepareRequests = new HashMap<>();
    _debuggerThread = new Thread( this::run, "Debugger" );
    _listeners = new ArrayList<>();
    _monitor =
      new Object()
      {
        public String toString()
        {
          return "Debugger Monitor";
        }
      };
  }

  public void startDebugging()
  {
    _debuggerThread.start();
  }

  public void resumeExecution()
  {
    synchronized( _monitor )
    {
      if( isPaused() )
      {
        _bPaused = false;
        resumed();
        _vm.resume();
      }
      else
      {
        _monitor.notifyAll();
      }
    }
  }

  public Location getSuspendedLocation()
  {
    return _location;
  }

  public ThreadReference getSuspendedThread()
  {
    return _eventThread;
  }

  public void addChangeListener( Consumer<Debugger> listener )
  {
    if( !_listeners.contains( listener ) )
    {
      _listeners.add( listener );
    }
  }
  public boolean removeChangeListener( Consumer<Debugger> listener )
  {
    return _listeners.remove( listener );
  }
  private void notifyListeners()
  {
    java.awt.EventQueue.invokeLater( () ->
      {
        if( DEBUG )
        {
          System.out.println( toString() );
        }
        for( Consumer<Debugger> listener : _listeners )
        {
          listener.accept( this );
        }
      } );
  }

  public String getEventName()
  {
    return _eventName;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if( isSuspended() )
    {
      String name;
      try
      {
        name = getSuspendedLocation().sourceName();
      }
      catch( AbsentInformationException e )
      {
        name = getSuspendedLocation().declaringType().name();
      }
      sb.append( "Suspended for event: " ).append( _eventName ).append( " at (" ).append( name ).append( ": " ).append( getSuspendedLocation().lineNumber() ).append( ")" );
    }
    else if( isPaused() )
    {
      sb.append( "Paused" );
    }
    else
    {
      sb.append( "Running" );
    }
    return sb.toString();
  }

  public void muteBreakpoints( boolean mute )
  {
    if( _vm == null )
    {
      return;
    }

    EventRequestManager erm = getEventRequestManager();
    List<BreakpointRequest> bpRequests = new ArrayList<>( erm.breakpointRequests() );
    for( BreakpointRequest req : bpRequests )
    {
      if( mute )
      {
        req.setEnabled( false );
      }
      else
      {
        Location location = req.location();
        Breakpoint breakpoint = _bpm.findBreakpoint( location.declaringType().name(), location.lineNumber() );
        req.setEnabled( breakpoint.isActive() );
      }
    }
  }

  private void run()
  {
    EventQueue eventQueue = _vm.eventQueue();
    while( true )
    {
      if( _vmExit )
      {
        break;
      }
      try
      {
        _eventSet = eventQueue.remove();
      }
      catch( InterruptedException e )
      {
        e.printStackTrace();
      }
      EventIterator eventIterator = _eventSet.eventIterator();
      while( eventIterator.hasNext() )
      {
        Event event = eventIterator.next();
        _eventName = event.getClass().getSimpleName();

        if( event instanceof StepEvent )
        {
          handleStepEvent( (StepEvent)event );
        }
        else if( event instanceof BreakpointEvent )
        {
          handleBreakpointEvent( (BreakpointEvent)event );
        }
        else if( event instanceof ExceptionEvent )
        {
          handleExceptionEvent( (ExceptionEvent)event );
        }
        else if( event instanceof ClassPrepareEvent )
        {
          handleClassPrepareEvent( (ClassPrepareEvent)event );
        }
        else if( event instanceof VMStartEvent )
        {
          handleVMStartEvent();
        }
        else if( event instanceof VMDisconnectEvent )
        {
          handleVMDisconnectEvent();
        }
        else if( event instanceof VMDeathEvent )
        {
          handleVMDeathEvent();
        }
        else
        {
          resumeProgram();
        }
      }
      _eventSet = null;
    }
    stopDebugging( false );
  }

  private void handleStepEvent( StepEvent event )
  {
    _location = event.location();
    _eventThread = event.thread();

    String fqn = getOutermostType( event.location().declaringType() );
    EventRequest request = event.request();
    getEventRequestManager().deleteEventRequest( request );
    if( FileTreeUtil.find( fqn ) != null )
    {
      Location backOutLoc = (Location)request.getProperty( BACK_OUT_LOCATION );
      if( backOutLoc != null && backOutLoc.lineNumber() == event.location().lineNumber() )
      {
        // This is a generated StepOut from a class we can't debug (like a Java class) (see comment below).
        // Since we are on the same line from where we stepped in, try stepping in to the next call site, if one exists
        if( createStep( event.thread(), StepRequest.STEP_INTO ) == null )
        {
          throw new IllegalStateException();
        }
        resumeProgram();
      }
      else
      {
        handleSuspendLocatableEvent( event );
      }
    }
    else
    {
      if( ((StepRequest)request).depth() == StepRequest.STEP_INTO )
      {
        // We stepped into a class we can't debug (like a Java class),
        // generate a StepOut to backout of this
        StepRequest step = createStep( event.thread(), StepRequest.STEP_OUT );
        step.putProperty( BACK_OUT_LOCATION, request.getProperty( FROM_LOCATION ) );
      }

      resumeProgram();
    }
  }

  private EventRequestManager getEventRequestManager()
  {
    return _vm.eventRequestManager();
  }

  private void handleBreakpointEvent( BreakpointEvent event )
  {
    List<Breakpoint> allBreakpoints = _bpm.getBreakpoints();

    Location loc = event.location();
    String fqn = getOutermostType( loc.declaringType() );
    int line = loc.lineNumber();

    Boolean temporary = (Boolean)event.request().getProperty( TEMPORARY );
    if( temporary != null && temporary )
    {
      // Temporary breakpoint e.g., "Run to Cursor", delete them after first use

      getEventRequestManager().deleteEventRequest( event.request() );
      handleSuspendLocatableEvent( event );
    }
    else
    {
      // Normal breakpoint

      Breakpoint breakpoint = null;
      for( Breakpoint bp : allBreakpoints )
      {
        if( bp.getFqn().equals( fqn ) && bp.getLine() == line )
        {
          breakpoint = bp;
          break;
        }
      }

      if( breakpoint != null && breakpoint.condition() )
      {
        handleSuspendLocatableEvent( event );
      }
      else
      {
        resumeProgram();
      }
    }
  }

  private void handleExceptionEvent( ExceptionEvent event )
  {
    handleSuspendLocatableEvent( event );
  }

  private void handleVMStartEvent()
  {
    addBreakpoints();
    resumeProgram();
  }

  private void handleClassPrepareEvent( ClassPrepareEvent event )
  {
    String className = event.referenceType().name();
    addPendingBreakpointFor( className );
    resumeProgram();
  }

  private void handleVMDeathEvent()
  {
    quit();
  }

  private void handleVMDisconnectEvent()
  {
    quit();
  }

  private void quit()
  {
    _location = null;
    _eventThread = null;
    _vmExit = true;
  }

  private void handleSuspendLocatableEvent( LocatableEvent event )
  {
    _location = event.location();
    _eventThread = event.thread();
    synchronized( _monitor )
    {
      suspended();
      try
      {
        _monitor.wait();
      }
      catch( Exception e )
      {
        // don't care
      }
    }
    resumeProgram();
  }

  public boolean isSuspended()
  {
    return _location != null;
  }

  public void stepOver()
  {
    step( StepRequest.STEP_OVER );
  }

  public void stepInto()
  {
    step( StepRequest.STEP_INTO );
  }

  public void stepOut()
  {
    step( StepRequest.STEP_OUT );
  }

  private void step( int depth )
  {
    StepRequest step = createStep( _eventThread, depth );
    if( step == null )
    {
      return;
    }
    synchronized( _monitor )
    {
      _monitor.notifyAll();
    }
  }

  private StepRequest createStep( ThreadReference eventThread, int depth )
  {
    if( getEventRequestManager().stepRequests().size() > 0 )
    {
      // Only one at a time
      return null;
    }

    StepRequest req = getEventRequestManager().createStepRequest( eventThread, StepRequest.STEP_LINE, depth );
    req.addClassExclusionFilter( "sun.*" );
    req.addClassExclusionFilter( "com.sun.*" );
    req.addClassExclusionFilter( "java.*" );
    req.addClassExclusionFilter( "gw.*" );
    req.addCountFilter( 1 );
    req.putProperty( FROM_LOCATION, _location );
    req.enable();
    return req;
  }

  public boolean isPaused()
  {
    return _bPaused;
  }
  public void pause()
  {
    synchronized( _monitor )
    {
      _vm.suspend();
      _bPaused = true;
    }
    notifyListeners();
  }

  public void dropToFrame( StackFrame frame )
  {
    if( frame == null )
    {
      return;
    }

    try
    {
      if( isFirstFrame( frame ) )
      {
        return;
      }
      getSuspendedThread().popFrames( frame );
      StackFrame currentFrame = getSuspendedThread().frame( 0 );
      _location = currentFrame.location();
      notifyListeners();
    }
    catch( IncompatibleThreadStateException e )
    {
      // eat
    }
  }

  private boolean isFirstFrame( StackFrame frame ) throws IncompatibleThreadStateException
  {
    List<StackFrame> frames = getSuspendedThread().frames();
    return frame.equals( frames.get( frames.size() - 1 ) );
  }

  private void addBreakpoints()
  {
    _bpm.getBreakpoints().forEach( this::addBreakpointJdi );
  }

  public void addBreakpointJdi( Breakpoint bp )
  {
    if( _vm == null )
    {
      return;
    }

    List<ReferenceType> list = _vm.classesByName( bp.getFqn() );
    if( list.size() > 0 )
    {
      try
      {
        List<Location> locations = list.get( 0 ).locationsOfLine( bp.getLine() );
        if( locations.size() > 0 )
        {
          EventRequestManager erm = getEventRequestManager();
          BreakpointRequest req = erm.createBreakpointRequest( locations.get( 0 ) );
          req.putProperty( TEMPORARY, bp.isTemporary() );
          req.setEnabled( bp.isActive() && (bp.isActiveWhenMuted() || !_bpm.isMuted()) );
        }
      }
      catch( AbsentInformationException e )
      {
        throw new RuntimeException( e );
      }
    }
    else
    {
      deferAddVmBreakpoint( bp );
    }
  }

  private void deferAddVmBreakpoint( Breakpoint bp )
  {
    String className = bp.getFqn();
    if( _classPrepareRequests.containsKey( className ) )
    {
      return;
    }
    ClassPrepareRequest request = getEventRequestManager().createClassPrepareRequest();
    request.addClassFilter( className );
    request.enable();
    _classPrepareRequests.put( className, request );
  }

  public void removeBreakpointJdi( Breakpoint bp )
  {
    if( _vm == null )
    {
      return;
    }

    EventRequestManager erm = getEventRequestManager();
    List<BreakpointRequest> bpRequests = new ArrayList<>( erm.breakpointRequests() );
    for( BreakpointRequest req : bpRequests )
    {
      Location location = req.location();
      if( getOutermostType( location.declaringType() ).equals( bp.getFqn() ) && location.lineNumber() == bp.getLine() )
      {
        erm.deleteEventRequest( req );
      }
    }
  }

  private void addPendingBreakpointFor( String className )
  {
    ClassPrepareRequest pendingReq = _classPrepareRequests.get( className );
    if( pendingReq == null )
    {
      return;
    }

    Map<Integer, Breakpoint> byLine = _bpm.getBreakpointsByType( className );
    EventRequestManager erm = getEventRequestManager();
    List<BreakpointRequest> bpRequests = new ArrayList<>( erm.breakpointRequests() );
    outer: for( Breakpoint bp: byLine.values() )
    {
      for( BreakpointRequest req : bpRequests )
      {
        Location location = req.location();
        if( getOutermostType( location.declaringType() ).equals( bp.getFqn() ) && location.lineNumber() == bp.getLine() )
        {
          continue outer;
        }
      }
      addBreakpointJdi( bp );
    }
    erm.deleteEventRequest( _classPrepareRequests.get( className ) );
  }

  private void resumeProgram()
  {
    _location = null;
    _eventThread = null;
    resumed();
    if( _eventSet != null )
    {
      _eventSet.resume();
    }
  }

  private void stopDebugging( boolean bKill )
  {
    if( _vm == null )
    {
      return;
    }
    try
    {
      if( bKill )
      {
        _vm.exit( 1 );
      }
      else
      {
        _vm.dispose();
      }
    }
    catch( Throwable e )
    {
      // don't care
    }
    _vm = null;
    getGosuPanel().clearDebugger();
  }

  private void suspended()
  {
    notifyListeners();
  }

  private void resumed()
  {
    notifyListeners();
  }

  public static String getOutermostType( ReferenceType type )
  {
    String name = type.name();
    if( name.contains( "$ProxyFor_" ) )
    {
      // this is a generated proxy; there is no source for this
      return name;
    }

    int iDollar = name.indexOf( '$' );
    if( iDollar > 0 )
    {
      name = name.substring( 0, iDollar );
    }
    return name;
  }

  private GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  public List<ThreadReference> getThreads()
  {
    return _vm.allThreads();
  }
}
