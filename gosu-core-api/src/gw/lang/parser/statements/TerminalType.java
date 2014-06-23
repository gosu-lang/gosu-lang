/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

/**
*/
public enum TerminalType {
  // Break is weaker than continue b/c a break flows to what follows the
  // context of the break, whereas continue may not (it could forever loop)
  Break,
  // Continue is weaker than ReturnOrThrow because continue applies to a
  // specific scope within a function, not the whole method
  Continue,
  // ReturnOrThrow must be combined b/c in aggregate they can absolutely
  // terminate e.g., an if-else-stmt can have a return in the if-part and
  // a throw in the else-part, but together they absolutely terminate the
  // whole if-stmt.
  ReturnOrThrow,
  // ForeverLoop is strongest because it is a black hole -- flow never escapes it
  ForeverLoop,
}
