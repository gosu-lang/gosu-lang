package gw.lang.enhancements
uses java.lang.RuntimeException
uses java.lang.Error
uses java.lang.Throwable
uses java.io.StringWriter
uses java.io.PrintWriter
uses java.util.IdentityHashMap

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreThrowableEnhancement : java.lang.Throwable
{
  /**
   * Determines whether this Throwable is a "checked" exception.
   */
  property get Checked() : Boolean
  {
    return not( this typeis Error or this typeis RuntimeException )
  }
  
  /**
   * Gets the nearest Throwable of the given type in this Throwable chain.
   *
   * Returns this object if it is of the given type. Returns null if no Throwable
   * exists in the cause chain that is of the given type.
   */
  reified function getCauseOfType<T extends Throwable>( causeType : Type<T> ) : T
  {
    var visited = new IdentityHashMap<Throwable, Boolean>()
    var cause = this
    while( cause != null and not causeType.Type.isAssignableFrom( typeof cause ) )
    {
      // handle loops in the cause chain
      if( visited.put( cause, true ) )
      {
        // we've seen this one before, so bail out
        return null
      }
      cause = cause.Cause
    }
    return cause as T
  }
  
  /**
   * Gets this Throwable's stack trace, as a string.
   */
  property get StackTraceAsString() : String
  {
    var buf = new StringWriter()
    this.printStackTrace( new PrintWriter( buf ) )
    return buf.toString()
  }
}
