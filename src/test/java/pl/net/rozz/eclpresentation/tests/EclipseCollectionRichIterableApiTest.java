package pl.net.rozz.eclpresentation.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.partition.PartitionIterable;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.block.factory.Predicates2;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

import pl.net.rozz.eclpresentation.Height;
import pl.net.rozz.eclpresentation.Person;

public class EclipseCollectionRichIterableApiTest {

	private Person johny = new Person("Johny", "Kowalski", 10, 144);
	private Person adam = new Person("Adam", "Spelling", 19, 178);
	private Person jenny = new Person("Jenny", "O'brien", 48, 167);
	private Person dakotta = new Person("Dakotta", "Marley", 8, 128);
	private Person mohammed = new Person("Mohammed", "Al-Saibai", 32, 198);
	private RichIterable<Person> persons = Sets.mutable.with(
			johny,
			adam,
			jenny,
			dakotta,
			mohammed
		);
	
	private Predicate<Person> adultsPredicate = person -> person.getAge() >= 18;
	private Predicate<Person> canDriveTruckPredicate = person -> person.getAge() >= 21;
	private Predicate<Person> canDriveScooterPredicate = person -> person.getAge() >= 16;

	private Predicate<Person> reallyTallPredicate = person -> person.getHeight() >= 190;
	
	
	@Test
	public void shouldSayHelloWorld() {
		RichIterable<String> words = Lists.immutable.of("Hello", "world");
		
		assertEquals("Hello world!", words.makeString("", " ", "!"));
	}
	
	@Test
	public void shouldUsePredicateToSelectAndRejectAdultsInRichIterable() {
		RichIterable<Person> adults = persons.select(adultsPredicate);
		RichIterable<Person> underages = persons.reject(adultsPredicate);
		
		assertTrue(adults.containsAllArguments(adam, jenny, mohammed));
		assertTrue(underages.containsAllArguments(johny, dakotta));
	}
	
	@Test
	public void shouldUsePredicateToDistinguishAdultsAndUnderagesInRichIterable() {
		PartitionIterable<Person> partitioned = persons.partition(adultsPredicate);
		
		assertTrue(partitioned.getSelected().containsAllArguments(adam, jenny, mohammed));
		assertTrue(partitioned.getRejected().containsAllArguments(johny, dakotta));
	}

	@Test
	public void shouldUsePredicateToDistinguishWhoCanDriveTruckAndScooter() {
		PartitionIterable<Person> canDriveTruck = persons.partition(canDriveTruckPredicate);
		
		assertTrue(canDriveTruck.getSelected().containsAllArguments(jenny, mohammed));
		assertTrue(canDriveTruck.getRejected().containsAllArguments(adam, johny, dakotta));

		PartitionIterable<Person> canDriveScooter = persons.partition(canDriveScooterPredicate);
		
		assertTrue(canDriveScooter.getSelected().containsAllArguments(adam, jenny, mohammed));
		assertTrue(canDriveScooter.getRejected().containsAllArguments(johny, dakotta));
	}
	
	@Test
	public void shouldUsePredicateToDistinguishWhoCanBePresidentInPoland() {
		Predicate2<Person, Integer> isOldEnough = (person, age) -> person.getAge() >= age;
		
		PartitionIterable<Person> canBePresidentInPoland = 
				persons.partitionWith(isOldEnough, 35);
		
		assertTrue(canBePresidentInPoland
				.getSelected()
				.containsAllArguments(jenny));
		assertTrue(canBePresidentInPoland
				.getRejected()
				.containsAllArguments(adam, johny, dakotta, mohammed));
	}
	
	@Test
	public void shouldUsePredicatesConjunctionToDistinguishAdultsAndUnderagesInRichIterable() {
		RichIterable<Person> reallyTallAdults = 
				persons.select(adultsPredicate.and(reallyTallPredicate)::test);

		assertTrue(reallyTallAdults.containsAllArguments(mohammed));
	}

	@Test
	public void shouldUseFunctionToDistinguishSmallMediumAndHighInRichIterable() {
		Multimap<Height, Person> groupByHeight = persons.groupBy(person -> Height.heightInCm(person.getHeight()));
		
		assertTrue(groupByHeight.get(Height.HIGH).contains(mohammed));
		assertTrue(groupByHeight.get(Height.MEDIUM).containsAllArguments(adam, jenny));
		assertTrue(groupByHeight.get(Height.SMALL).containsAllArguments(johny, dakotta));
	}
	
	@Test
	public void shouldGetListOfLastNames() {
		assertTrue(
			persons
				.collect(Person::getLastName)
				.containsAllArguments(
						"Kowalski", 
						"Spelling", 
						"O'brien", 
						"Marley", 
						"Al-Saibai"
						)
			);
	}
	
	@Test
	public void shouldGetListOfLastNamesThatEndsWithI() {
		assertTrue(
			persons
				.collectIf(person -> person.getLastName().endsWith("i") ,Person::getLastName)
				.containsAllArguments(
						"Kowalski", 
						"Al-Saibai")
			);
	}

	@Test
	public void shouldGetListOfWordsInLastNames() {
		RichIterable<String> flatCollect = persons
			.collect(Person::getLastName)
			.flatCollect(lastName -> Arrays.asList(lastName.split("-|'")));
		
		System.out.println(flatCollect);
		assertTrue(
			flatCollect
				.containsAllArguments(
						"Kowalski", 
						"Spelling", 
						"O","brien", 
						"Marley", 
						"Al","Saibai"
						)
					);
	}

}
