package pl.net.rozz.eclpresentation.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.collections.impl.list.mutable.CompositeFastList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

public class EclipseCollectionsListsImplTest {

	private List<String> popularMaleNames = new FastList<>(Arrays.asList("Tom", "Steve", "John", "Donald"));
	private List<String> popularFemaleNames = new FastList<>(Arrays.asList("Sandra", "Betty", "Jessica", "Carol"));

	@Test(expected=ConcurrentModificationException.class)
	public void shouldThrowCME() {
		List<String> names = new ArrayList<>();
		
		Iterator<String> i = names.iterator();
		
		names.add("Placek");
		
		i.next();
	}
	
	@Test
	public void shouldNotThrowCME() {
		List<String> names = new FastList<>();
		
		Iterator<String> i = names.iterator();
		
		names.add("Placek");
		
		System.out.println(i.next());
	}
	
	@Test
	public void shouldModifyUnderlyingCollectionInCompositeFastList() {
		CompositeFastList<Object> popularNames = new CompositeFastList<>();
		popularNames.addComposited(popularFemaleNames);
		popularNames.addComposited(popularMaleNames);
		
		assertTrue(popularNames.containsAll(popularFemaleNames));
		assertTrue(popularNames.containsAll(popularMaleNames));
		
		popularNames.add("Malcolm");
		popularNames.add(4, "Sarah");
		
		assertTrue(popularFemaleNames.contains("Sarah"));
		assertTrue(popularMaleNames.contains("Malcolm"));
	}

}
