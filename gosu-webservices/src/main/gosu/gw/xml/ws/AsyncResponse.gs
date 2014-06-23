/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

uses gw.xml.XmlElement
uses gw.internal.xml.ws.AsyncResponseImpl
uses java.util.concurrent.TimeUnit
uses java.lang.RuntimeException
uses java.io.InputStream
uses java.util.concurrent.TimeoutException

/**
 * This class wraps the response object and can be used to poll the
 * webservice for completion of an async request or to block on the completion
 * of that request.
 * 
 * Any call that returns information from the response will cause the request to be started,
 * assuming it has not already been started.
 *
 * Although this class can itself spawn a separate thread, calls into it are not intended to be thread safe.
 */
abstract class AsyncResponse<T, E extends XmlElement> {

  protected construct()
  {
    if ( ( typeof this ).GenericType != AsyncResponseImpl.Type.GenericType ) {
      throw new RuntimeException( "Invalid AsyncResponse subclass: " + typeof this )
    }
  }

  /**
   * Returns the request envelope.
   *
   * @return the request envelope
   */
  abstract property get RequestEnvelope() : E

  /**
   * Sets the request envelope.
   */
  abstract property set RequestEnvelope( envelope : E )

  /**
   * Sets the response envelope.
   */
  abstract property set ResponseEnvelope( envelope : E )

  /**
   * Returns the response envelope.
   *
   * @return the response envelope
   */
  abstract property get ResponseEnvelope() : E

  /**
   * Returns the response envelope. This call will block until the request completes or
   * the timeout occurs.
   *
   * @param timeout the amount of time to wait for a response
   * @param unit the timeunit to apply to timeout
   * @return the response envelope
   */
  @Throws( TimeoutException, "if the timeout period expires before the response is received" )
  abstract function getResponseEnvelope( timeout : long, unit : TimeUnit ) : E

  /**
   * Returns the unwrapped response object.  Note that this is
   * a blocking call.
   *
   * @return the response object from the web service call
   */
  abstract function get() : T

  /**
   * Waits if necessary for at most the given time for the computation
   * to complete, and then retrieves its result, if available.
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @return the computed result
   * @throws InterruptedException if the current thread was interrupted while waiting
   * @throws TimeoutException if the timeout period expires before the response is received
   */
  @Throws( TimeoutException, "if the timeout period expires before the response is received" )
  abstract function get( timeout : long, unit : TimeUnit ) : T

  /**
   * Starts this async request. If it has already been started, this call has no effect.
   */
  abstract function start()

  /**
   * A transform to be applied to the request before it is sent.
   */
  abstract property get RequestTransform() : block( is : InputStream ) : InputStream

  /**
   * A transform to be applied to the request before it is sent.
   */
  abstract property set RequestTransform( bl( is : InputStream ) : InputStream )

  /**
   * A transform to be applied to the response before it is processed.
   */
  abstract property get ResponseTransform() : block( is : InputStream ) : InputStream

  /**
   * A transform to be applied to the response before it is processed.
   */
  abstract property set ResponseTransform( bl( is : InputStream ) : InputStream )

  /**
   * The HTTP headers of the response.
   */
  abstract property get ResponseHttpHeaders() : HttpHeaders

}
