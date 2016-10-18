package editor.search;

class TextFileIndexer {}
//## the following is some messing with Lucene indexing


//import gw.util.StreamUtil;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.FieldType;
//import org.apache.lucene.document.TextField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexOptions;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.Sort;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.postingshighlight.DefaultPassageFormatter;
//import org.apache.lucene.search.postingshighlight.Passage;
//import org.apache.lucene.search.postingshighlight.PassageFormatter;
//import org.apache.lucene.search.postingshighlight.PostingsHighlighter;
//import org.apache.lucene.store.FSDirectory;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//
///**
// * This terminal application creates an Apache Lucene index in a folder and adds files into this index
// * based on the input of the user.
// */
//public class TextFileIndexer
//{
//  private static StandardAnalyzer analyzer = new StandardAnalyzer();
//  private static final List<String> TEXT_FILES = Arrays.asList(
//    ".txt",
//    ".gs",
//    ".gsx",
//    ".gsp",
//    ".gst",
//    ".java" );
//
//  private IndexWriter writer;
//  private ArrayList<File> queue = new ArrayList<>();
//
//
//  public static void main( String[] args ) throws IOException
//  {
//    System.out.println( "Enter the path where the index will be created: (e.g. /tmp/index or c:\\temp\\index)" );
//
//    String indexLocation = null;
//    Scanner scan = new Scanner( System.in );
//    String s = scan.nextLine();
//
//    TextFileIndexer indexer = null;
//    try
//    {
//      indexLocation = s;
//      indexer = new TextFileIndexer( s );
//    }
//    catch( Exception ex )
//    {
//      System.out.println( "Cannot create index..." + ex.getMessage() );
//      System.exit( -1 );
//    }
//
//    //===================================================
//    //read input from user until he enters q for quit
//    //===================================================
//    while( !s.equalsIgnoreCase( "q" ) )
//    {
//      try
//      {
//        System.out.println( "Enter the full path to add into the index (q=quit): (e.g. /home/ron/mydir or c:\\Users\\ron\\mydir)" );
//        System.out.println( "[Acceptable file types: .xml, .html, .html, .txt]" );
//        s = scan.nextLine();
//        if( s.equalsIgnoreCase( "q" ) )
//        {
//          break;
//        }
//
//        //try to add file into the index
//        indexer.indexFileOrDirectory( s );
//      }
//      catch( Exception e )
//      {
//        System.out.println( "Error indexing " + s + " : " + e.getMessage() );
//      }
//    }
//
//    //===================================================
//    //after adding, we always have to call the
//    //closeIndex, otherwise the index is not created
//    //===================================================
//    indexer.closeIndex();
//
//    //=========================================================
//    // Now search
//    //=========================================================
//    IndexReader reader = DirectoryReader.open( FSDirectory.open( new File( indexLocation ).toPath() ) );
//
//    s = "";
//    while( !s.equalsIgnoreCase( "q" ) )
//    {
//      try
//      {
//        System.out.println( "Enter the search query (q=quit):" );
//        s = scan.nextLine();
//        if( s.equalsIgnoreCase( "q" ) )
//        {
//          break;
//        }
//        Query q = new QueryParser( "contents", analyzer ).parse( s );
//
//        //QueryScorer queryScorer = new QueryScorer( q, "contents" );
//        //Fragmenter fragmenter = new SimpleSpanFragmenter( queryScorer );
//
//        StringBuilder sb = new StringBuilder();
//        PostingsHighlighter highlighter = new PostingsHighlighter( 1000 )
//        {
//          @Override
//          protected PassageFormatter getFormatter( String field )
//          {
//            return new DefaultPassageFormatter()
//            {
//              @Override
//              public String format( Passage[] passages, String content )
//              {
//                for( Passage passage : passages )
//                {
//                  if( passage.getNumMatches() <= 0 )
//                  {
//                    continue;
//                  }
//                  sb.append( "Score: " ).append( passage.getScore() );
//                  sb.append( "  Start: " ).append( passage.getStartOffset() ).append( "  --  " );
//                }
//                sb.append( "\n" );
//                return sb.toString();
//              }
//            };
//          }
//        };
//        IndexSearcher searcher = new IndexSearcher( reader );
//        TopDocs topDocs = searcher.search( q, 1000, Sort.RELEVANCE );
//        String[] contentss = highlighter.highlight( "contents", q, searcher, topDocs );
//        for( String ss : contentss )
//        {
//          System.out.println( ss );
//        }
//
//        System.out.println( sb );
//      }
//      catch( Exception e )
//      {
//        System.out.println( "Error searching " + s + " : " + e.getMessage() );
//      }
//    }
//  }
//
//  /**
//   * Constructor
//   *
//   * @param indexDir the name of the folder in which the index should be created
//   *
//   * @throws java.io.IOException when exception creating index.
//   */
//  TextFileIndexer( String indexDir ) throws IOException
//  {
//    // the boolean true parameter means to create a new index everytime,
//    // potentially overwriting any existing files there.
//    FSDirectory dir = FSDirectory.open( new File( indexDir ).toPath() );
//
//
//    IndexWriterConfig config = new IndexWriterConfig( analyzer );
//    writer = new IndexWriter( dir, config );
//  }
//
//  /**
//   * Indexes a file or directory
//   *
//   * @param fileName the name of a text file or a folder we wish to add to the index
//   *
//   * @throws java.io.IOException when exception
//   */
//  public void indexFileOrDirectory( String fileName ) throws IOException
//  {
//    //===================================================
//    //gets the list of files in a folder (if user has submitted
//    //the name of a folder) or gets a single file name (is user
//    //has submitted only the file name)
//    //===================================================
//    addFiles( new File( fileName ) );
//
//    int originalNumDocs = writer.numDocs();
//    for( File f : queue )
//    {
//      try( FileReader reader = new FileReader( f ) )
//      {
//        Document doc = new Document();
//
//        FieldType offsetsType = new FieldType( TextField.TYPE_STORED );
//        offsetsType.setIndexOptions( IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS );
//        Field contents = new Field( "contents", StreamUtil.getContent( reader ), offsetsType );
//        contents.fieldType().setIndexOptions( IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS );
//        doc.add( contents );
//
//        writer.addDocument( doc );
//        System.out.println( "Added: " + f );
//      }
//      catch( Exception e )
//      {
//        System.out.println( "Could not add: " + f );
//      }
//    }
//
//    int newNumDocs = writer.numDocs();
//    System.out.println( "" );
//    System.out.println( "************************" );
//    System.out.println( (newNumDocs - originalNumDocs) + " documents added." );
//    System.out.println( "************************" );
//
//    queue.clear();
//  }
//
//  private void addFiles( File file )
//  {
//
//    if( file.isDirectory() )
//    {
//      for( File f : file.listFiles() )
//      {
//        addFiles( f );
//      }
//    }
//    else if( file.isFile() )
//    {
//      String filename = file.getName().toLowerCase();
//      if( TEXT_FILES.stream().anyMatch( filename::endsWith ) )
//      {
//        queue.add( file );
//      }
//      else
//      {
//        System.out.println( "Skipped " + filename );
//      }
//    }
//  }
//
//  /**
//   * Close the index.
//   *
//   * @throws java.io.IOException when exception closing
//   */
//  public void closeIndex() throws IOException
//  {
//    writer.close();
//  }
//}