package gw.sample;

uses java.time.LocalDate;
uses java.util.Collections;
uses java.util.List;
uses manifold.ext.api.Jailbreak;
uses manifold.graphql.request.Executor;
uses org.junit.Test;


uses gw.sample.movies.*
uses gw.sample.movies.Genre.*
uses gw.sample.queries.*
uses org.junit.Assert#*

public class QueryTest
{
  function testMoviesQuery()
  {
    var actors = Collections.singletonList( ActorInput
      .builder( "McQueen" )
      .withNationality( "American" )
      .build() );

    var movieQuery = MovieQuery
      .builder()
      .withTitle( "Le Mans" )
      .withActors( actors )
      .withGenre( Action )
      .withReleaseDate( LocalDate.of( 1971, 6, 3 ) )
      .build();
    assertEquals(
      "{\n" +
      "  \"title\": \"Le Mans\",\n" +
      "  \"actors\": [\n" +
      "    {\n" +
      "      \"name\": \"McQueen\",\n" +
      "      \"nationality\": \"American\"\n" +
      "    }\n" +
      "  ],\n" +
      "  \"genre\": \"Action\",\n" +
      "  \"releaseDate\": \"1971-06-03\"\n" +
      "}",
      movieQuery.write().toJson() );
    assertEquals( actors, movieQuery.Actors );
    assertEquals( movieQuery.Genre.Action, movieQuery.Genre );
    assertEquals( "Le Mans", movieQuery.Title );
    assertEquals( LocalDate.of(1971, 6, 3), movieQuery.ReleaseDate );
  }

  function testActorsQuery()
  {
    var actorQuery = ActorQuery
      .builder( "The Getaway" )
      .withGenre( Drama )
      .build();
    assertEquals(
      "{\n" +
      "  \"title\": \"The Getaway\",\n" +
      "  \"genre\": \"Drama\"\n" +
      "}",
      actorQuery.write().toJson() );
    assertEquals( actorQuery.Genre.Drama, actorQuery.Genre );
    assertEquals( "The Getaway", actorQuery.Title );
//
//    @Jailbreak Executor<ActorQuery.Result> request = actorQuery.request( "" );
//    String query = request._reqArgs.getQuery();
//    String expected = "query ActorQuery($title:String!,$genre:Genre) {actors(title:$title,genre:$genre) {... on Person {id name} ... on Animal {id name kind}}}";
//    assertEquals( expected.replaceAll( "\\s+", "" ), query.replaceAll( "\\s+", "" ) );
  }

//  @Test
//  public void testGraphQLFragmentIncludedWithQuery()
//  {
//    CompareRoles compareRoles = CompareRoles.builder("", "").build();
//    @Jailbreak Executor<CompareRoles.Result> request = compareRoles.request("");
//    // ensure the fragment used in the query is included with the query
//    assertTrue( request._reqArgs.getQuery().contains( "fragment comparisonFields" ) );
//    // ensure only the fragment used in the query is included
//    // e.g., the 'otherComparisonFields' fragments should not be included
//    assertFalse( request._reqArgs.getQuery().contains( "fragment otherComparisonFields" ) );
//  }
//
//  @Test
//  public void testEmbeddedQuery()
//  {
//    /* [> MyEmbedded.graphql <]
//    query MyOneAnimal($id: ID!) {
//      animal(id: $id) {
//        id
//        name
//        kind
//        nationality
//      }
//    }*/
//    MyEmbedded.MyOneAnimal myOneAnimal = MyEmbedded.MyOneAnimal.builder( "1" ).build();
//    assertNotNull( myOneAnimal );
//
//    /**[>MyEmbedded2.graphql<]
//    query MyOneAnimal2($id: ID!) {
//      animal(id: $id) {
//        id
//        name
//        kind
//        nationality
//      }
//    }*/
//    MyEmbedded2.MyOneAnimal2 myOneAnimal2 = MyEmbedded2.MyOneAnimal2.builder( "1" ).build();
//    assertNotNull( myOneAnimal2 );
//  }
//
//  @Test
//  public void testStringLiteralQuery()
//  {
//    Foo value = "[>Foo.graphql<] query MyOneAnimal($id: ID!) { animal(id: $id) { id name } }";
//    Foo.MyOneAnimal myOneAnimal = value.builder( "1" ).build();
//    assertNotNull( myOneAnimal );
//  }
//
//  @Test
//  public void testAnonymousStringLiteralQuery()
//  {
//    Object value = "[>.graphql<] query MyOneAnimal2($id: ID!) { animal(id: $id) { id name } }";
//    assertTrue( value.getClass().getSimpleName().startsWith( ANONYMOUS_FRAGMENT_PREFIX ) );
//  }
}
