package gw.lang.reflect.gs;

/**
 * The intention for this interface is to provide a lifecycle API for a user-defined base class of a program.
 * The use-case is as follows:
 *
 * <pre>
 * MyProgram.gsp
 * -------------
 *
 * extends MyBase
 *
 * doSomething()
 *
 *
 * MyBase.gs
 * ---------
 *
 * package abc
 *
 * class MyBase implements IManagedProgramInstance {
 *   override function beforeExecution() : boolean {
 *     // determine if the program should execute
 *   }
 *   override function afterExecution( t: Throwable ) {
 *     // handle the Throwable, do what you need to cleanup
 *   }
 * }
 * </pre>
 *
 * Internally the Gosu compiler generates the evaluate() method like so:
 * <pre>
 *   function evaluate( map: IExternalSymbolMap ) : Object {
 *     var $failure : Throwable
 *     if( this.beforeExecution() ) {
 *       try {
 *         [method-body] // returns result
 *       }
 *       catch( $catchFailure: Throwable ) {
 *         $failure = $catchFailure
 *       }
 *       finally {
 *         this.afterExecution( $failure )
 *       }
 *     }
 *     return null // only get here if exception not rethrown in afterExecution()
 *   }
 * </pre>
 */
public interface IManagedProgramInstance extends IProgramInstance {
  /**
   * Called before IProgramInstance#evaluate().
   * 
   * @return false if the program should not be executed i.e., don't call IProgramInstance#evaluate(),
   * true if the program should execute normally.
   */
  boolean beforeExecution();

  /**
   * Called after IProgramInstance#evaluate().
   *
   * @param error The exception or error if IProgramInstance#evaluate() terminates abnormally, otherwise null.
   */
  void afterExecution( Throwable error );
}
