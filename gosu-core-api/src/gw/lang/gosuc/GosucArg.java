/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

/**
 */
public class GosucArg {
  public static final GosucArg PROJECT = new GosucArg( true, false, "-project", "The GosuC project file to compile" );
  public static final GosucArg PARSER = new GosucArg( false, false, "-parser", "The fully qualified name of a custom parser class implementing " + ICustomParser.class.getName() );
  public static final GosucArg[] ARGS = {PROJECT, PARSER,};

  private String _name;
  private String _value;
  private String _desc;
  private boolean _bRequired;
  private boolean _bFlag;
  private boolean _bMatched;
  private String _error;

  public GosucArg( boolean bRequired, boolean bFlag, String name, String desc ) {
    _bRequired = bRequired;
    _bFlag = bFlag;
    _name = name;
    _desc = desc;
  }

  public String getName() {
    return _name;
  }

  public String getValue() {
    return _value;
  }
  public void setValue( String value ) {
    _value = value;
  }

  public boolean isRequired() {
    return _bRequired;
  }

  public boolean isFlag() {
    return _bFlag;
  }

  public String getDescription() {
    return _desc;
  }

  public boolean isMatched() {
    return _bMatched;
  }
  void setMatched( boolean bMatched ) {
    _bMatched = bMatched;
  }

  public String getError() {
    return _error;
  }
  public void setError( String error ) {
    _error = error;
  }
  public boolean hasError() {
    return getError() != null;
  }

  public String toString() {
    return _name + " = " + _value;
  }

  /**
   * @param args the arguments in the command line with the consumed ones removed from the beginning
   * @return the number of args consumed
   */
  private int match( String[] args ) {
    if( args.length < 1 ) {
      return 0;
    }
    if( getName().equalsIgnoreCase( args[0] ) ) {
      if( isMatched() ) {
        setError( "Multiple " + getName() + " arguments are not supported." );
        return 1;
      }
      setMatched( true );
      if( !isFlag() ) {
        if( args.length == 1 || args[1].length() == 0 ) {
          setError( "Expecting a value for " + getName() );
          return 1;
        }
        setValue( args[1] );
        return 2;
      }
      return 1;
    }
    return 0;
  }

  public static String parseArgs( String[] args ) {
    if( args.length == 0 ) {
      return "Expecting arguments: " + PROJECT.getName() + " <" + PROJECT.getDescription() + ">";
    }
    for( int i = 0; i < args.length; i++ ) {
      int iPos = i;
      for( GosucArg arg : GosucArg.ARGS ) {
        String[] csr = new String[args.length-i];
        System.arraycopy( args, i, csr, 0, csr.length );
        i += arg.match( csr );
      }
      if( i == iPos ) {
        return "Unknow arguments starting at " + args[i];
      }
    }
    String errors = "";
    for( GosucArg arg : ARGS ) {
      if( arg.hasError() ) {
        errors += arg.getError() + "\n";
      }
    }
    return errors.isEmpty() ? null : errors;
  }
}
