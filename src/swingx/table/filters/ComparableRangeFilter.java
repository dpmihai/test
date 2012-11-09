package swingx.table.filters;

import java.util.Comparator;

/**
 * 
 * @author Thierry LEFORT
 * 4 déc. 07
 * 
 * Filter based on comparable values. 
 * Accepted values are contained between the given max and min.
 * 
 * @param <T> the type of Comparable to be compare in this Filter
 */
public class ComparableRangeFilter<T extends Comparable<T>> extends AbstractFilter implements Cloneable {

	private Comparator<T> _comparator;
	private T _min;
	private T _max;
	
	
	/**
	 * Instantiates a ComparableFilter with a min and a max values.
	 * 
	 * @param min the Minimum value of the filter
	 * @param max the Maximum value of the filter
	 */
	public ComparableRangeFilter(int col,
			T min,
			T max) {
		this(null, col, min, max);
	}
	/**
	 * Instantiates a ComparableFilter with a min and a max values 
	 * and a comparator.
	 * @param comparator the Comparator to use to define the order
	 * @param min the Minimum value of the filter
	 * @param max the Maximum value of the filter
	 */
	public ComparableRangeFilter(Comparator<T> comparator, 
			int col,
			T min,
			T max) {
		super(col);
		_min = min;
		_max = max;
		setComparator(comparator);
	}

	/**
	 * Sets the pattern used by this filter for matching.
	 *
	 * @param pattern the pattern used by this filter for matching
	 * @see java.util.regex.Pattern
	 */
	public void setComparator(Comparator<T> comparator) {
		_comparator = comparator;
		refresh();
	}

	/**
	 * Returns the pattern used by this filter for matching.
	 *
	 * @return the pattern used by this filter for matching
	 * @see java.util.regex.Pattern
	 */
	public Comparator<T> getComparator() {
		return _comparator;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean doTest(Object value) {
		return isGreaterThanMin((T) value) && isLowerThanMax((T) value);
	}

	/**
	 * Check if the given value is Greater than the min value
	 * If the min value is <code>null</code> it returns always <code>true</code>
	 * If the comparator is defined it is used to do the comparaison, 
	 * otherwise we assume the values are comparable.
	 * @param value the value to check
	 * @return <code>true</code> if the value is greater than the min value
	 */
	protected boolean isGreaterThanMin(T value) {
		if (_min == null) {
			return true;
		}
		if (_comparator != null) {
			return _comparator.compare(value, _min) >= 0;
		}
		return _min.compareTo(value) <= 0;
	}
	/**
	 * Check if the given value is Smaller than the max value
	 * If the man value is <code>null</code> it returns always <code>true</code>
	 * If the comparator is defined it is used to do the comparaison, 
	 * otherwise we assume the values are comparable.
	 * @param value the value to check
	 * @return <code>true</code> if the value is smaller than the max value
	 */
	protected boolean isLowerThanMax(T value) {
		if (_max == null) {
			return true;
		}
		if (_comparator != null) {
			return _comparator.compare(value, _max) <= 0;
		}
		return _max.compareTo(value) >= 0;
	}


	/**
	 * Define the filter's min and max values 
	 * @param min Mininum allowed
	 * @param max Maximum allowed
	 * @throws IllegalArgumentException if the min value is greater 
	 * than the max value.
	 */
	public void setMinMax(T min, T max) {
		if ((min != null && max != null) 
				&& ((_comparator != null && _comparator.compare(min, max) > 0) 
						|| (min.compareTo(max) > 0))) {
			throw new IllegalArgumentException("Min must be superior to Max");
		}
		_min = min;
		_max = max;
		refresh();
	}
	
	public T getMin() {
		return _min;
	}

	public T getMax() {
		return _max;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new ComparableRangeFilter(_comparator, getColumnIndex(), _min, _max);
	}
	
	
}
