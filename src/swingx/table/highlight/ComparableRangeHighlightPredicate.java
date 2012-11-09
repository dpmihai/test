package swingx.table.highlight;

public class ComparableRangeHighlightPredicate extends AbstractHighlightPredicate {

	private Comparable<Object> _min;
	
	private Comparable<Object> _max;
	
	public ComparableRangeHighlightPredicate(
			int modelColumn, 
			boolean onlyCells,
			Comparable<Object> min,
			Comparable<Object> max) {
		super(modelColumn, onlyCells);
		_min = min;
		_max = max;
	}

	@Override
	public boolean test(Object value) {
		if (_min == null && _max == null) {
			return value == null;
		}
		boolean isGreaterThanMin = 
			_min == null ? value != null : _min.compareTo(value) <= 0;
		boolean isLowerThanMax = 
			_max == null ? value == null : _max.compareTo(value) >= 0;
		return isGreaterThanMin && isLowerThanMax;
	}

}
