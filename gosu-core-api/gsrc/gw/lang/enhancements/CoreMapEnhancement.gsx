package gw.lang.enhancements
uses java.util.*
uses java.io.*
uses gw.util.AutoMap

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreMapEnhancement<K, V> : java.util.Map<K, V>
{

  property get Keys() : java.util.Set<K> {
    return this.keySet()
  }

  property get Values() : java.util.Collection<V> {
    return this.values()
  }

  public function eachKeyAndValue( eachBlock(k : K, val : V):void ) : void
  {
    for( entry in this.entrySet() )
    {
      eachBlock( entry.Key, entry.Value )
    }
  }

  public function eachKey( eachBlock(k : K):void ) : void
  {
    for( key in Keys )
    {
      eachBlock( key )
    }
  }
  
  public function eachValue( eachBlock(value : V):void ) : void
  {
    for( key in Keys )
    {
      eachBlock( this[key] )
    }
  }
  
  public function mapValues<V2>( mapper(value : V):V2 ) : java.util.Map<K, V2>
  {
    var returnMap = new HashMap<K, V2>()
    eachKeyAndValue( \ k, v ->{ returnMap[k] = mapper(v) } )
    return returnMap
  }

  public function toAutoMap( defaultValue(k : K):V ) : Map<K, V> {
    return new AutoMap<K,V>(this, defaultValue)
  }

  /**
   * Returns a copy of this set
   */
  function copy() : Map<K, V> {
    return new HashMap<K, V>(this)
  }

  /**
   * Removes all entries whose keys do not satisfy the keyFilter expression
   * Return true if this map changed as a result of the call
   */
  function retainWhereKeys( keyFilter(k : K) : boolean ) : boolean {
    var modified = false
    var iterator = this.entrySet().iterator()
    while( iterator.hasNext() ) {
      var entry = iterator.next()
      if(not keyFilter( entry.Key ) ) {
        iterator.remove()
        modified = true
      }
    }
    return modified
  }

  /**
   * Removes all entries whose values do not satisfy the valueFilter expression
   * Return true if this map changed as a result of the call
   */
  function retainWhereValues( valueFilter(v : V) : boolean ) : boolean {
    var modified = false
    var iterator = this.entrySet().iterator()
    while( iterator.hasNext() ) {
      var entry = iterator.next()
      if(not valueFilter( entry.Value ) ) {
        iterator.remove()
        modified = true
      }
    }
    return modified
  }

  /**
   * Removes all entries whose keys do not satisfy the keyFilter expression
   * and returns the new resulting map
   */
  function filterByKeys( keyFilter(k : K):boolean ) : Map<K, V> {
    var returnMap = new HashMap<K, V>()
    eachKeyAndValue( \ k, v ->{ if( keyFilter( k ) ) { returnMap[k] = v } } )
    return returnMap
  }

  /**
   * Removes all entries whose values do not satisfy the valueFilter expression
   * and returns the new resulting map
   */
  function filterByValues( valueFilter(v : V):boolean ) : Map<K, V> {
    var returnMap = new HashMap<K, V>()
    eachKeyAndValue( \ k, v ->{ if( valueFilter( v ) ) { returnMap[k] = v } } )
    return returnMap
  }

  /**
   * Uses a java.util.Properties object to write the contents of this
   * map to the given file in the java properties format.
   */
  function writeToPropertiesFile( file : File ) {
    writeToPropertiesFile( file, "" )
  }

  /**
   * Uses a java.util.Properties object to write the contents of this
   * map to the given file in the java properties format.
   */
  function writeToPropertiesFile( file : File, comments : String ) {
    var x = new Properties()
    x.putAll( this )
    using( var out = new FileOutputStream( file ) ) {
      x.store( out, comments )
    }
  }

  /**
   * Reads the given properties file into a map
   */
  static function readFromPropertiesFile( file : File ) : Map<String, String> {
    if( not file.exists() ) {
      return {}
    }
    using( var ins = new FileInputStream( file ) ) 
    {
      var x = new Properties()
      x.load( ins )
      return x as Map<String, String>
    }
  }

  property get Count() : int {
    return this.size() 
  }
}