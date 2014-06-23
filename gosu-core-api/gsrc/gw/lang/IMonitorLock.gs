package gw.lang

/**
 * Any object can be coerced to this type so that it can be used as a monitor lock
 * e.g., the following code is the same as synchronizing on obj in java.
 * <pre>
 * using( obj as IMonitorLock )
 * {
 *   ...
 * }
 * </pre>
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
final class IMonitorLock
{
}
