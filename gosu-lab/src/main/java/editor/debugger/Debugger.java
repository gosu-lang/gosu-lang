package editor.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
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
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.StepRequest;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.RunMe;

import editor.shipit.CompiledClass;
import editor.shipit.ExperimentBuild;
import editor.util.EditorUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JOptionPane;

/**
 */
public class Debugger
{
  private static final String TEMPORARY = "temporary";
  private static final String FROM_LOCATION = "from_location";
  private static final String BACK_OUT_LOCATION = "back_out_location";

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
  private final HashSet<ObjectReference> _refs;
  private boolean _temporarilySuspended;
  private EventIterator _eventIterator;
  private ExperimentBuild _classRedefiner;

  public Debugger( VirtualMachine vm, BreakpointManager bpm )
  {
    _vm = vm;
    _bpm = bpm;
    _classPrepareRequests = new HashMap<>();
    _debuggerThread = new Thread( this::run, "Debugger" );
    _listeners = new ArrayList<>();
    _refs = new HashSet<>();
    _classRedefiner = new ExperimentBuild( false );
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
        _vm.resume();
        resumed( false );
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

  private void assignSuspendedState( LocatableEvent event, boolean temporary )
  {
    _location = event.location();
    _eventThread = event.thread();
    _temporarilySuspended = temporary;
  }
  private void clearSuspendedState()
  {
    _location = null;
    _eventThread = null;
    _temporarilySuspended = false;
  }

  public void addChangeListener( Consumer<Debugger> listener )
  {
    if( !_listeners.contains( listener ) )
    {
      _listeners.add( listener );
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public boolean removeChangeListener( Consumer<Debugger> listener )
  {
    return _listeners.remove( listener );
  }

  private void notifyListeners()
  {
    for( Consumer<Debugger> listener : _listeners )
    {
      listener.accept( this );
    }
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

    List<ExceptionRequest> exceptionRequests = new ArrayList<>( erm.exceptionRequests() );
    for( ExceptionRequest req: exceptionRequests )
    {
      if( mute )
      {
        req.setEnabled( false );
      }
      else
      {
        Breakpoint breakpoint = _bpm.getExceptionBreakpoint( req.exception().name() );
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
      _eventIterator = _eventSet.eventIterator();
      while( _eventIterator.hasNext() )
      {
        Event event = _eventIterator.next();
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
          resumeProgram( true );
        }
      }
      _eventSet = null;
    }
    stopDebugging( false );
  }

  private void handleStepEvent( StepEvent event )
  {
    assignSuspendedState( event, true );

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
        resumeProgram( false );
      }
      else
      {
        consumeRemainingEvents();
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

      resumeProgram( false );
    }
  }

  private EventRequestManager getEventRequestManager()
  {
    return _vm.eventRequestManager();
  }

  private void handleBreakpointEvent( LocatableEvent event )
  {
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
      for( Breakpoint bp : _bpm.getBreakpoints() )
      {
        if( bp.getFqn().equals( fqn ) && bp.getLine() == line )
        {
          breakpoint = bp;
          break;
        }
      }

      if( breakpoint == null )
      {
        // Exception breakpoint

        EventRequest request = event.request();
        if( request instanceof ExceptionRequest )
        {
          String exceptionName = ((ExceptionRequest)request).exception().name();
          for( Breakpoint bp : _bpm.getExceptionBreakpoints() )
          {
            if( bp.getFqn().equals( exceptionName ) )
            {
              breakpoint = bp;
              break;
            }
          }
        }
        else
        {
          throw new IllegalStateException( "Did not find breakpoint for: " + loc + " Class: " + fqn + " Line: " + line );
        }
      }

      if( breakpoint != null )
      {
        assignSuspendedState( event, true );
        boolean suspend = true;
        try
        {
          suspend = breakpoint.condition();
        }
        catch( VMDisconnectedException e )
        {
          return;
        }
        finally
        {
          clearSuspendedState();
        }
        if( suspend )
        {
          consumeRemainingEvents();
          handleSuspendLocatableEvent( event );
        }
        else
        {
          if( _eventIterator.hasNext() )
          {
            return;
          }
          releaseRefs();
          resumeProgram( true );
        }
      }
      else
      {
        resumeProgram( true );
      }
    }
  }

  private void consumeRemainingEvents()
  {
    while( _eventIterator.hasNext() )
    {
      Event event = _eventIterator.next();
      if( event instanceof StepEvent )
      {
        // a pending step event is superceded by a breakpoint or exception event
        getEventRequestManager().deleteEventRequest( event.request() );
      }
    }
    // delete any pending step requests
    getEventRequestManager().deleteEventRequests( getEventRequestManager().stepRequests() );
  }

  private void handleExceptionEvent( ExceptionEvent event )
  {
    handleBreakpointEvent( event );
  }

  private void handleVMStartEvent()
  {
    addBreakpoints();
    resumeProgram( false );
  }

  private void handleClassPrepareEvent( ClassPrepareEvent event )
  {
    String className = event.referenceType().name();
    addPendingBreakpointFor( className );
    if( _eventIterator.hasNext() )
    {
      return;
    }
    resumeProgram( true );
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
    clearSuspendedState();
    _vmExit = true;
  }

  private void handleSuspendLocatableEvent( LocatableEvent event )
  {
    assignSuspendedState( event, false );
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
    resumeProgram( false );
  }

  public boolean isSuspended()
  {
    return _location != null && !_temporarilySuspended;
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
    _bpm.getExceptionBreakpoints().forEach( this::addBreakpointJdi );
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
        if( bp.isLineBreakpoint() )
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
        else
        {
          EventRequestManager erm = getEventRequestManager();
          ExceptionRequest req = erm.createExceptionRequest( list.get( 0 ), bp.isCaughtException(), bp.isUncaughtException() );
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

    if( bp.isLineBreakpoint() )
    {
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
    else
    {
      EventRequestManager erm = getEventRequestManager();
      List<ExceptionRequest> bpRequests = new ArrayList<>( erm.exceptionRequests() );
      for( ExceptionRequest req : bpRequests )
      {
        String fqn = req.exception().name();
        if( bp.getFqn().equals( fqn ) )
        {
          erm.deleteEventRequest( req );
        }
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
    if( byLine != null )
    {
      EventRequestManager erm = getEventRequestManager();
      List<BreakpointRequest> bpRequests = new ArrayList<>( erm.breakpointRequests() );
      outer:
      for( Breakpoint bp : byLine.values() )
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
    else
    {
      Breakpoint bp = _bpm.getExceptionBreakpoint( className );
      if( bp != null )
      {
        addBreakpointJdi( bp );
        EventRequestManager erm = getEventRequestManager();
        erm.deleteEventRequest( _classPrepareRequests.get( className ) );
      }
    }
  }

  private void resumeProgram( boolean silent )
  {
    clearSuspendedState();
    resumed( silent );
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

  private void resumed( boolean silent )
  {
    releaseRefs();
    if( !silent )
    {
      notifyListeners();
    }
  }

  public void retain( ObjectReference ref )
  {
    if( _refs.add( ref ) )
    {
      try
      {
        ref.disableCollection();
      }
      catch( Exception e )
      {
        // ignore
      }
    }
  }
  private void releaseRefs()
  {
    for( ObjectReference objectReference : _refs )
    {
      try
      {
        objectReference.enableCollection();
      }
      catch( Exception e )
      {
        // ignore
      }
    }
    _refs.clear();
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

  public ExperimentBuild getClassRedefiner()
  {
    return _classRedefiner;
  }

  public void redefineClasses( List<CompiledClass> listCompiledClasses )
  {
    Map<ReferenceType, byte[]> classes = new HashMap<>();
    for( CompiledClass cc: listCompiledClasses )
    {
      List<ReferenceType> referenceTypes = _vm.classesByName( cc.getGosuClass().getName() );
      if( referenceTypes.size() > 0 )
      {
        classes.put( referenceTypes.get( 0 ), cc.getBytes() );
      }
    }
    try
    {
      _vm.redefineClasses( classes );
      EditorUtilities.invokeNowOrLater( () -> JOptionPane.showMessageDialog( RunMe.getEditorFrame(), "Reloaded " + classes.size() + " classes" ) );
    }
    catch( UnsupportedOperationException e )
    {
      EditorUtilities.invokeNowOrLater( () -> JOptionPane.showMessageDialog( RunMe.getEditorFrame(), "Could not reload classes" ) );
    }
  }
}
