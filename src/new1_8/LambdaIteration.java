package new1_8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaIteration {
	
	class Artist {
		private String name;
		private String location;
		
		public Artist(String name, String location) {
			super();
			this.name = name;
			this.location = location;
		}

		public String getName() {
			return name;
		}

		public String getLocation() {
			return location;
		}
		
		public boolean isFrom(String country) {
			return location.equals(country);
		}
				
	}	
	
	public static void main(String[] args) {
		LambdaIteration li = new LambdaIteration();
		Artist[] array = new Artist[] {li.new Artist("Ion", "Romania"), li.new Artist("Mike", "England"), li.new Artist("Ingrid", "Germany")};
		List<Artist> artists = Arrays.asList(array);
		
		// count artists from Romania
//		int count = 0;
//		for (Artist artist : artists) {
//			if (artist.isFrom("Romania")) {
//				count++;
//			}
//		}
		
		long count = artists.stream()
				.filter(artist -> artist.isFrom("Romania"))
				.count();
		System.out.println("count="+count);
		
		// print artist names like a toString
		// Collectors.joining  method is a convenience for building up strings from
		// streams. It lets us provide a delimiter (which goes between elements), a prefix for our
		// result, and a suffix for the result.
		String result =	artists.stream()
				.map(Artist::getName)
				.collect(Collectors.joining(", ", "[", "]"));
		System.out.println(result);
				
	}

}
