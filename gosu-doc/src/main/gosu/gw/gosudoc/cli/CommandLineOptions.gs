package gw.gosudoc.cli

uses gw.internal.ext.com.beust.jcommander.IParameterValidator
uses gw.internal.ext.com.beust.jcommander.ParameterException
uses gw.internal.ext.com.beust.jcommander.Parameter

uses java.io.File

class CommandLineOptions {

  @Parameter(:names = { "-externalDocs" }, :description = "List of external documents", :variableArity = true)
  var _externalDocs : List<String>
  
  property get ExternalDocs() : List<String> {
    return _externalDocs
  }

  @Parameter(:names = { "-filters" }, :description = "List of filters", :variableArity = true)
  var _filters : List<Object>
  
  property get Filters() : List<Object> {
    return _filters
  }

  @Parameter(:names = { "-help" }, :description = "Print a synopsis of standard options", :help = true)
  var _help : Boolean

  /**
   * @return true if and only if '-help' was specified on the command line
   */
  property get Help() : boolean {
    return _help ?: false
  }

  @Parameter(:names = { "-inputDirs" }, :description = "List of source directories to process", :variableArity = true, :validateWith = FileExists)
  var _inputDirs : List<File>
  
  property get InputDirs() : List<File> {
    return _inputDirs
  }

  @Parameter(:names = { "-output" }, :description = "Directory to write output")
  var _output : File
  
  property get Output() : File {
    return _output
  }

  @Parameter(:names = { "-verbose" }, :description = "Output messages about what gosudoc is doing")
  var _verbose : Boolean

  /**
   * @return true if and only if '-verbose' was specified on the command line
   */
  property get Verbose() : boolean {
    return _verbose ?: false
  }

  protected static class FileExists implements IParameterValidator {
    override function validate(name : String, value : String) {
      if(new File(value).exists() == false) {
        throw new ParameterException("Parameter ${name} contained a non-existent directory: ${value}")
      }
    }
  }

}