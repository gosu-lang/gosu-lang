/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.process;

import gw.util.StreamUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 */
class Gobbler extends Thread {
  private final InputStream _streamToGobble;
  private final OutputHandler _outputHandler;
  private final String _gobblerCharset;

  Gobbler( InputStream streamToGobble, OutputHandler outputHandler, String charSet )
  {
    _streamToGobble = streamToGobble;
    _outputHandler = outputHandler;
    _gobblerCharset = charSet;
  }

  @Override
  public void run()
  {
    try
    {
      Reader inputStreamReader = StreamUtil.getInputStreamReader(_streamToGobble, _gobblerCharset);
      BufferedReader br = new BufferedReader(inputStreamReader);

      String line;
      while( (line = br.readLine()) != null ) {
        _outputHandler.handleLine(line);
      }
    } catch (IOException ioe) {
      //ignore
    }
  }

}
