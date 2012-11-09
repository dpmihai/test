package swingx.table.highlight;

import java.util.Arrays;
import java.util.TreeSet;

public class ValuesHighlightPredicate extends AbstractHighlightPredicate {
	
	private TreeSet<Object> _values;
	
	public ValuesHighlightPredicate(int modelColumn,
			boolean highLightOnlyCells,
			Object... values) {
		super(modelColumn, highLightOnlyCells);
		_values = new TreeSet<Object>(Arrays.asList(values));
	}

	@Override
	public boolean test(Object value) {
		return _values.contains(value);
	}
}
