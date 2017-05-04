package gw.specContrib.sourceproducers;

import gw.specContrib.sourceproducers.impl.Person;
import static gw.specContrib.sourceproducers.impl.Person.*;
import junit.framework.TestCase;
import java.util.*;

public class JavaJsonTest extends TestCase
{
  public void testMe() {
    Person person = new Person();
    person.setName( "Joe Namath" );
    assertEquals( "Joe Namath", person.getName() );

    Address address = new Address();
    address.setCity( "Dunedin" );
    person.setAddress( address );
    assertEquals( "Dunedin", person.getAddress().getCity() );

    Hobby baseball = new Hobby();
    baseball.setCategory( "Sport" );
    Hobby fishing = new Hobby();
    fishing.setCategory( "Recreation" );
    List<Hobby> hobbies = Arrays.asList( baseball, fishing );
    person.setHobby( hobbies );
    List<Hobby> h = person.getHobby();
    assertEquals( 2, h.size() );
    assertEquals( baseball, h.get( 0 ) );
    assertEquals( fishing, h.get( 1 ) );
  }
}