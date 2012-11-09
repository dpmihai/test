package misc;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public class CountryList {
	public static void main(String[] args) {
		List<Country> countries = new ArrayList<Country>();

		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			String iso = "NA";
			try {
				iso = locale.getISO3Country();
			} catch (MissingResourceException ex) {				
			}
			String code = locale.getCountry();
			String name = locale.getDisplayCountry();
			String language = locale.getLanguage();

			if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
				
				Country c = new Country(iso, code, name, language);
				if (!countries.contains(c)) {
					countries.add(c);
				}	
			}
		}

		Collections.sort(countries, new CountryComparator());
		for (Country country : countries) {
			System.out.println(country);
		}
	}
}

class CountryComparator implements Comparator<Country> {
	private Comparator comparator;

	public CountryComparator() {
		comparator = Collator.getInstance();
	}

	public int compare(Country o1, Country o2) {
		return comparator.compare(o1.getName(), o2.getName());
	}
}

class Country {
	private String iso;
	private String code;
	private String name;
	private String language;

	public Country(String iso, String code, String name, String language) {
		this.iso = iso;
		this.code = code;
		this.name = name;
		this.language = language;
	}		

	public String getIso() {
		return iso;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String toString() {
		return name.toUpperCase() + " (" + iso + ") " + language + "," + code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((iso == null) ? 0 : iso.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (iso == null) {
			if (other.iso != null)
				return false;
		} else if (!iso.equals(other.iso))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}