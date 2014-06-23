/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import gw.config.CommonServices;
import gw.internal.ext.org.apache.commons.cli.BasicParser;
import gw.internal.ext.org.apache.commons.cli.CommandLine;
import gw.internal.ext.org.apache.commons.cli.CommandLineParser;
import gw.internal.ext.org.apache.commons.cli.Option;
import gw.internal.ext.org.apache.commons.cli.Options;
import gw.internal.ext.org.apache.commons.cli.ParseException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.StreamUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CommandLineAccess {
  private static List<String> _args = new ArrayList<String>();
  private static File _currentProgram = null;
  private static boolean _exitEnabled = true;
  private static boolean _useTerminalWidth = true;

  /**
   * @return the raw string arguments a Gosu program was started with
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public static List<String> getRawArgs() {
    return _args;
  }

  /**
   * @param args the args to a Gosu program
   */
  public static void setRawArgs(List<String> args) {
    _args = args;
  }

  /**
   * @return the currently executing program as a file object
   */
  public static File getCurrentProgram() {
    return _currentProgram;
  }

  /**
   * @param currentProgram - the currently executing program
   */
  public static void setCurrentProgram(File currentProgram) {
    _currentProgram = currentProgram;
  }

  /**
   * @return true if CommandLineAccess will attempt to use the active terminals width
   * when printing it's help message
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public static boolean isUseTerminalWidth() {
    return _useTerminalWidth;
  }

  /**
   * Set to true for CommandLineAccess to use the active terminals width when printing it's help message
   */
  public static void setUseTerminalWidth(boolean b) {
    CommandLineAccess._useTerminalWidth = b;
  }

  /**
   * Initializes the static properties on the given type based on the command
   * line arguments.  If the arguments incorrectly map to the given type,
   * a help message will be printed and the JVM will exit with a -1 return value.
   *
   * @param commandLineShell the class to initialize from the passed in arguments
   */
  public static void initialize(IType commandLineShell) {
    initialize( commandLineShell, true );
  }

  /**
   * Initializes the properties on the given object based on the command
   * line arguments.  If the object passed in is a type, static properties
   * will be initialized.
   * <p/>
   * Note that you will get -h, -help and --help for free, there is no need to
   * explicitly include a help property on your command line class.
   *
   * @param obj the class to initialize from the passed in arguments
   * @param exitOnBadArgs if true is passed in and the arguments incorrectly map to the
   *        given type, a help message will be printed and the JVM will exit with a -1 return value,
   *        otherwise a false value will be returned
   * @return true if initialization was successful
   */
  public static boolean initialize(Object obj, boolean exitOnBadArgs) {
    List<IPropertyInfo> propsToSet = new ArrayList<IPropertyInfo>();
    IType type = obj instanceof IType ? ((IType) obj) : TypeSystem.getFromObject( obj );
    ITypeInfo typeInfo = type.getTypeInfo();
    Options options = deriveOptionsFromTypeInfo( typeInfo, propsToSet, obj instanceof IType );

    CommandLineParser parser = new BasicParser();
    try {
      CommandLine cl = parser.parse( options, _args.toArray( new String[_args.size()] ) );
      for (IPropertyInfo propertyInfo : propsToSet) {
        propertyInfo = typeInfo.getProperty( propertyInfo.getName() );
        if( isBooleanProp( propertyInfo ) ) {
            propertyInfo.getAccessor().setValue( obj, cl.hasOption( getShortName( propertyInfo ) ) );
        } else {
          String defaultValue = null;
          if (propertyInfo.hasAnnotation(TypeSystem.get(DefaultValue.class))) {
            IAnnotationInfo annotationInfo = propertyInfo.getAnnotationsOfType( TypeSystem.get( DefaultValue.class ) ).get( 0 );
            defaultValue = ((DefaultValue)annotationInfo.getInstance()).value();
          }

          String shortName = getShortName( propertyInfo );
          Object value;
          if (propertyInfo.getFeatureType().isArray()) {
            value = cl.getOptionValues( shortName );
            if (propertyInfo.hasAnnotation(TypeSystem.get(Args.class))) {
              value = cl.getArgs();
            } else if (value == null) {
              if (defaultValue != null) {
                value = defaultValue.split( " +" );
              } else {
                //Set the value to an empty array if the option is present
                if (cl.hasOption(shortName)) {
                  value = new String[0];
                }
              }
            }
          } else {
            if (!needsArg(propertyInfo) && defaultValue == null) {
              defaultValue = "";
            }
            value = cl.getOptionValue( shortName, defaultValue );
          }
          try {
            propertyInfo.getAccessor().setValue( obj, convertValue( propertyInfo.getFeatureType(), value ) );
          } catch (Exception e) {
            throw new ParseException( "The parameter \"" + shortName + "\" requires an argument of type " +
                                      propertyInfo.getFeatureType().getRelativeName() + ".  The value \"" + value +
                                      "\" cannot be converted to this type.  Please pass in a valid value." + (e.getMessage() == null ? "" : "  Error message was : " + e.getMessage()) );
          }
        }
      }
    } catch (ParseException e) {
      if (exitOnBadArgs) {
        if( !e.getMessage().endsWith( "-help" ) &&
            !e.getMessage().endsWith( "-h" ) &&
                !e.getMessage().endsWith("--help")) {
          System.out.println("\nArgument problem: " + e.getMessage() + "\n");
        }
        try {
          showHelp( getCurrentProgramName(), type );
        } catch (StringIndexOutOfBoundsException e1) {
          System.out.println( "Unable to print help message.  Exiting." );
        }
        if (_exitEnabled) {
          System.exit( -1 );
        }
        throw new SystemExitIgnoredException();
      }
      return false;
    }

    return true;
  }

  static Object convertValue(IType type, Object value) {
    if (value instanceof String) {
      if (type == JavaTypes.SHORT() || type == JavaTypes.pSHORT()) {
        return Short.parseShort( value.toString() );
      }
      if (type == JavaTypes.INTEGER() || type == JavaTypes.pINT()) {
        return Integer.parseInt( value.toString() );
      }
      if (type == JavaTypes.LONG() || type == JavaTypes.pLONG()) {
        return Long.parseLong( value.toString() );
      }
      if (type == JavaTypes.FLOAT() || type == JavaTypes.pFLOAT()) {
        return Float.parseFloat( value.toString() );
      }
      if (type == JavaTypes.DOUBLE() || type == JavaTypes.pDOUBLE()) {
        return Double.parseDouble( value.toString() );
      }
      if (type == JavaTypes.DATE()) {
        try {
          return CommonServices.getCoercionManager().parseDateTime( value.toString() );
        } catch (java.text.ParseException e) {
          throw new RuntimeException( e );
        }
      }
    }
    return CommonServices.getCoercionManager().convertValue( value, type );
  }

  private static String getCurrentProgramName() {
    File file = getCurrentProgram();
    if (file != null) {
      return file.getName();
    } else {
      return "unknown program";
    }
  }

  /**
   * Shows a help message for the program arguments derived from the given type, sent
   * to stdout
   *
   * @param programName the name of the program
   * @param obj either the type of the command line shell if static properties are used, or the instance if instance properties are used
   */
  public static void showHelp(String programName, Object obj) {
    PrintWriter pw = new PrintWriter( StreamUtil.getOutputStreamWriter( System.out ) );
    printHelpToWriter( programName, obj, pw );
    pw.flush();
  }

  /**
   * Shows a help message for the program arguments derived from the given type, sent
   * to stdout
   *
   * @param obj either the type of the command line shell if static properties are used, or the instance if instance properties are used
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public static void showHelp(IType obj) {
    PrintWriter pw = new PrintWriter( StreamUtil.getOutputStreamWriter( System.out ) );
    printHelpToWriter( getCurrentProgramName(), obj, pw );
    pw.flush();
  }

  /**
   * Returns the help message derived from the given type
   *
   * @param obj the object or type of the command line shell
   * @return the help message derived from the given type
   */
  public static String getHelpMessageFor(Object obj) {
    StringWriter writer = new StringWriter();
    PrintWriter pw = new PrintWriter( writer );
    printHelpToWriter( getCurrentProgramName(), obj, pw );
    pw.flush();
    return writer.toString();
  }

  private static void printHelpToWriter(String programName, Object obj, PrintWriter pw) {
    GosuHelpFormatter formatter = new GosuHelpFormatter();
    if( _useTerminalWidth ) // may be invalid if run from a non-terminal environment
    {
      formatter.setWidth(80);
    }
    ITypeInfo typeInfo = obj instanceof IType ? ((IType)obj).getTypeInfo() : TypeSystem.getFromObject( obj ).getTypeInfo();
    Options options = deriveOptionsFromTypeInfo( typeInfo, new ArrayList<IPropertyInfo>(), obj instanceof IType );
    System.err.println("Printing help with defaultWidth= " + formatter.getWidth() +
            " programName= " + programName +
            " defaultLeftPad= " + formatter.getLeftPadding() +
            " defaultDescPad= " + formatter.getDescPadding());
    formatter.printHelp(pw, formatter.getWidth(), programName, null, options, formatter.getLeftPadding(), formatter.getDescPadding(), null, false);
  }

  private static Options deriveOptionsFromTypeInfo( ITypeInfo typeInfo,
                                                    /*IN-OUT*/ List<IPropertyInfo> propsToSet,
                                                    boolean useStaticProps) {
    List<? extends IPropertyInfo> propertyInfos = typeInfo.getProperties();
    Options options = new Options();
    for (IPropertyInfo propertyInfo : propertyInfos) {
      if( propertyInfo instanceof IGosuVarPropertyInfo) {
        IGosuVarPropertyInfo gsVarPropInfo = (IGosuVarPropertyInfo) propertyInfo;
        if (gsVarPropInfo.hasDeclaredProperty() || gsVarPropInfo.isFinal()) {
          continue;
        }
      }
      if (useStaticProps == propertyInfo.isStatic() && propertyInfo.isWritable(null)) {
        String shortName = getShortName( propertyInfo );
        boolean needsArg = needsArg( propertyInfo );

        GosuOption opt = new GosuOption( shortName, getLongName( propertyInfo ), needsArg, deriveDescription( propertyInfo ) );
        opt.setHidden( propertyInfo.hasAnnotation( TypeSystem.get( Hidden.class ) ) );

        opt.setRequired( propertyInfo.hasAnnotation( TypeSystem.get( Required.class ) ) );
        if (propertyInfo.getFeatureType().isArray()) {
          opt.setType( propertyInfo.getFeatureType().getComponentType().getName() );

          if (propertyInfo.hasAnnotation(TypeSystem.get(ArgNames.class))) {
            ArgNames argNames = (ArgNames)propertyInfo.getAnnotationsOfType( TypeSystem.get( ArgNames.class ) ).get( 0 ).getInstance();
            if (argNames.names() != null && argNames.names().length > 0) {
              String compoundNames = "";
              for (int i = 0; i < argNames.names().length; i++) {
                if (i != 0) {
                  compoundNames += " ";
                }
                compoundNames += argNames.names()[i];
              }
              opt.setArgName( compoundNames );
              if (propertyInfo.hasAnnotation(TypeSystem.get(ArgOptional.class))) {
                opt.setOptionalArg( true );
              }
              opt.setArgs( argNames.names().length );
            }
          } else {
            opt.setArgs( Option.UNLIMITED_VALUES );
          }

          if (propertyInfo.hasAnnotation(TypeSystem.get(Separator.class))) {
            Separator argNames = (Separator)propertyInfo.getAnnotationsOfType( TypeSystem.get( Separator.class ) ).get( 0 ).getInstance();
            opt.setValueSeparator( argNames.value().charAt( 0 ) );
          }

        } else {
          opt.setType( propertyInfo.getFeatureType().getName() );
          if (propertyInfo.hasAnnotation(TypeSystem.get(ArgNames.class))) {
            ArgNames argNames = (ArgNames)propertyInfo.getAnnotationsOfType( TypeSystem.get( ArgNames.class ) ).get( 0 ).getInstance();
            if (argNames.names() != null && argNames.names().length > 0) {
              opt.setArgName( argNames.names()[0] );
            }
          }
        }

        propsToSet.add( propertyInfo );
        options.addOption( opt );
      }
    }
    return options;
  }

  protected static String deriveDescription(IPropertyInfo propertyInfo) {
    IType intrinsicType = propertyInfo.getOwnersType();
    String description;
    try {
      ResourceBundle resourceBundle = ResourceBundle.getBundle( intrinsicType.getName(),
                                                                Locale.getDefault(),  
                                                                TypeSystem.getGosuClassLoader().getActualLoader() );
       description = resourceBundle.getString(propertyInfo.getName());
    } catch (MissingResourceException e) {
      description = propertyInfo.getDescription();
    }

    return description == null ? "" : description.replaceAll("\n", " ");
    }

  private static String getShortName(IPropertyInfo propertyInfo) {
    String shortName = makeCmdLineOptionName( propertyInfo );
    if (propertyInfo.hasAnnotation(TypeSystem.get(ShortName.class))) {
      IAnnotationInfo annotation = propertyInfo.getAnnotationsOfType( TypeSystem.get( ShortName.class ) ).get( 0 );
      ShortName value = (ShortName)annotation.getInstance();
      shortName = value.name();
    }
    return shortName;
  }

  private static String getLongName(IPropertyInfo propertyInfo) {
    String shortName = makeCmdLineOptionName( propertyInfo );
    if (propertyInfo.hasAnnotation(TypeSystem.get(LongName.class))) {
      IAnnotationInfo annotation = propertyInfo.getAnnotationsOfType( TypeSystem.get( LongName.class ) ).get( 0 );
      LongName value = (LongName)annotation.getInstance();
      shortName = value.name();
    }
    return shortName;
  }

  private static String makeCmdLineOptionName(IPropertyInfo propertyInfo) {
    String name = propertyInfo.getName();
    name = name.substring(0, 1).toLowerCase() + name.substring(1);
    StringBuilder optionName = new StringBuilder();
    boolean lastWasLowerCase = false;
    for (int i = 0; i < name.length(); i++) {
      if (i == 0) {
        optionName.append( Character.toLowerCase( name.charAt( i ) ) );
        lastWasLowerCase = false;        
      } else if (Character.isUpperCase(name.charAt(i))) {
        if (lastWasLowerCase) {
          optionName.append( "_" );
        }
        optionName.append( Character.toLowerCase( name.charAt( i ) ) );
        lastWasLowerCase = false;
      } else {
        char c = name.charAt( i );
        optionName.append( c );
        lastWasLowerCase = Character.isLetter( c ) && Character.isLowerCase( c );
      }
    }
    return optionName.toString();
  }

  private static boolean needsArg(IPropertyInfo propertyInfo) {
    boolean requiresArgument = true;
    if (isBooleanProp(propertyInfo)) {
      requiresArgument = false;
    } else if (propertyInfo.getFeatureType().isArray()) {
      requiresArgument = false;
    } else if (propertyInfo.hasAnnotation(TypeSystem.get(ArgOptional.class))) {
      requiresArgument = false;
    }
    return requiresArgument;
  }

  private static boolean isBooleanProp(IPropertyInfo propertyInfo) {
    return propertyInfo.getFeatureType().equals( JavaTypes.BOOLEAN() ) ||
           propertyInfo.getFeatureType().equals( JavaTypes.pBOOLEAN() );
  }

  public static void setExitEnabled(boolean exitEnabled) {
    _exitEnabled = exitEnabled;
  }

}
