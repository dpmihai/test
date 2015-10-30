package new1_8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapNewMethods {
	
	static class Person {
		
		private String name;
		private int age;
				
		public Person(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}	
		
	}
	
	public static void main(String[] args) {
		Stream<Person> people = Stream.of(new Person("Andy", 25), new Person("Paul", 30), new Person("Mary", 27), new Person("Paul", 5));
		Map<String, List<Person>> byNameMap = new HashMap<>();	
		
//		for(Person person: people.collect(Collectors.toList())) {
//		    byNameMap.computeIfAbsent(person.getName(), name -> new ArrayList<>()).add(person);
//		}
		
		byNameMap = people.collect(Collectors.groupingBy(Person::getName));
		
		byNameMap.forEach((k, v) -> System.out.println(k + "=" + 
				v.stream().
				  sorted((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge())).
				  //map(p -> p.toString()).
				  map(p -> String.valueOf(p.getAge())).
				  collect(Collectors.joining(", ", "[", "]")))); 
		
		
		
	}

}
