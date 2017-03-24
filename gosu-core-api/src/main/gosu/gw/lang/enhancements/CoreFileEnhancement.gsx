package gw.lang.enhancements
uses java.io.File
uses gw.util.StreamUtil
uses java.io.FileOutputStream
uses java.io.BufferedReader
uses java.io.FileInputStream
uses java.nio.ByteBuffer
uses java.lang.Integer
uses java.nio.BufferOverflowException
uses java.lang.NullPointerException
uses java.io.FileNotFoundException
uses java.util.Arrays
uses java.util.List
uses java.lang.Math

/**
 * File goodies
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreFileEnhancement : File
{
  /**
   * Reads the contents of the file into a string
   */
  function read() : String
  {
    using( var reader = StreamUtil.getInputStreamReader( new FileInputStream( this ) ) ) 
    {
      return StreamUtil.getContent( reader )
    }
  }

  /**
   * Writes the string to the file, overwriting its contents
   */
  function write( str : String ) {
    if( not this.exists() ){
      this.createNewFile()
    }
    using( var fos = StreamUtil.getOutputStreamWriter( new FileOutputStream( this, false ) ) )
    {
      fos.write( str )
    }
  }

  /**
   * Reads the bytes from the file
   */
  function readBytes() : byte[] {
    using( var fis = new FileInputStream(this) )
    {
      var fc = fis.Channel
      var size = fc.size()
      if( size > (Integer.MAX_VALUE as long) ){
        throw new BufferOverflowException()
      }
      var buff = ByteBuffer.allocate( size as int )
      fc.read(buff)
      return buff.array()
    }
  }

  /**
   * Writes the bytes to the file, overwriting its contents
   */
  function writeBytes( content : byte[] ) {
    if( not this.exists() ){
      this.createNewFile()
    }
    using( var fos = new FileOutputStream( this, false ) )
    {
      fos.write( content )
    }
  }

  function copyTo( otherFile : File )
  {
    using( var reader = new FileInputStream(this),
           var fos = new FileOutputStream( otherFile, false ) )
           {
      StreamUtil.copy( reader, fos )
    }
  }

  /**
   * Returns true if this file contans the given regexp, false otherwise
   */
  function containsText( regExpStr : String ) : boolean
  {
    var regExp = ".*${regExpStr}.*".toRegEx()
    using( var reader = new BufferedReader( StreamUtil.getInputStreamReader( new FileInputStream( this ) ) ) ) 
    {
      var line = reader.readLine()
      while( line != null ) {
        if( regExp.matcher( line ).matches() ) {
          return true
        }
        line = reader.readLine()
      }
    }
    return false
  }

  /**
   * Deletes this directory after deleting all files beneath it
   */
  function deleteRecursively()
  {
    eachFileInTree( \ f -> f.delete() )
  }

  /**
   * Executes the given block on every file below this directory
   */
  function eachFileInTree( operation(file:File) ) {
    for( child in this.listFiles() ) {
      child.eachFileInTree( operation )
    }
    operation(this)
  }

  /**
   * Executes the given block on each direct child of this directory
   */
  function eachChild( operation(file:File) ) {
    this.listFiles().each( \ f -> operation( f )  )
  }

  /**
   * Returns the extension of this file
   */
  property get Extension() : String {
    var lastDot = this.getName().lastIndexOf( "." )
    return  0 <= lastDot ? this.getName().substring( lastDot + 1 ) : ""
  }

  /**
   * Returns the name of this file without an extension
   */
  property get NameSansExtension() : String {
    var fullName = this.Name
    if(fullName == "." or fullName == ".." ) {
      return fullName
    }
    var lastDot = fullName.lastIndexOf( "." )
    if( lastDot != -1 ) {
      var extent = fullName.length - Extension.length - 1
      if( extent >= 0 ) {
        return fullName.substring( 0, extent )
      }
    }
    return fullName
  }

  /**
   * Executes the given block on each line.  This does not read the whole file into memory like
   * read does, and thus is more memory efficient for large files
   */
  function eachLine( lineProcessor(line:String) ) {
    using( var reader = new BufferedReader( StreamUtil.getInputStreamReader( new FileInputStream( this ) ) ) )
    {
      var line = reader.readLine()
      while( line != null ) {
        lineProcessor( line )
        line = reader.readLine()
      }
    }
  }

  function differsFrom(that : File) : boolean {
    if (that == null) {
      throw new NullPointerException()
    }
    if (!this.exists()) {
      throw new FileNotFoundException(this.AbsolutePath)
    }
    if (this == that) {
      return false
    }
    if (!that.exists()) {
      throw new FileNotFoundException(that.AbsolutePath)
    }
    if (this.length() != that.length()) {
      return true
    }

    // using a max buffer size that isn't too big so as to run out of memory
    var bufferSize = Math.min((1 << 20) as long, this.length()) as int

    using( var lfis = new FileInputStream(this),
           var rfis = new FileInputStream(that) )
    {
      var lfc = lfis.Channel
      var rfc = rfis.Channel
      var lbuff  = ByteBuffer.allocate(bufferSize)
      var rbuff  = ByteBuffer.allocate(bufferSize)

      while (lfc.position() < lfc.size()) {
        lfc.read(lbuff)
        rfc.read(rbuff)

        if (not Arrays.equals(lbuff.array(), rbuff.array())) 
        {
          return true
        }

        lbuff.clear()
        rbuff.clear()
      }
      return false
    }
  }

  function isReservedFileName() : boolean {
    if (java.lang.System.Platform == Windows) {
      var WINDOWS_RESERVED_DEVICE_NAMES : String[] =
          { "CON", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5",
          "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" }
      if (!this.isFile()) {
        return false
      }

      var nameNoExtension = this.getName()
      var lastDot = nameNoExtension.indexOf( "." )
      if (lastDot > 0) {
        nameNoExtension = nameNoExtension.substring( 0, lastDot )
      }

      nameNoExtension = nameNoExtension.toUpperCase()
      return WINDOWS_RESERVED_DEVICE_NAMES.contains( nameNoExtension )
    }
    return false
  }

  /**
   * Executes a block using a new temp file, deleting the temp file when the block returns.
   */
  static function usingTempFile( b( f : File ) )
  {
    var f = File.createTempFile( "gwtemp", ".tmp" )
    try {
      b( f )
    } finally  {
      f.delete()
    }
  }

  property get Children() : List<File> {
    return this.listFiles().toList()
  }
  
  function getChild( name : String ) : File {
    return new File( this, name )
  }
  
}