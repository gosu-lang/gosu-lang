package gw.gosudoc.doc;

import java.io.File;
import java.util.List;

public interface IGsDocHtmlWriter
{
  List<File> getInputDirs();
  void setInputDirs( List<File> inputDirs );

  File getOutput();
  void setOutput( File output );

  List getFilters();
  void setFilters( List filters );

  List<String> getExternalDocs();
  void setExternalDocs( List<String> externalDocs );

  Boolean getVerbose();
  void setVerbose( Boolean verbose );
}
