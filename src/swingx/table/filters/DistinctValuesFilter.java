package swingx.table.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Thierry LEFORT
 * 4 déc. 07
 * 
 * Define a filter based on a list of possible values.
 * 
 */
public class DistinctValuesFilter<T> extends AbstractFilter implements Cloneable {
	/** List of possible values */
	private Collection<T> _list;
	/**
	 * Constructor with column index to be filtered. 
	 * All values will be accepted.
	 * @param col the index of the column to filter
	 */
	public DistinctValuesFilter(int col) {
		this(col, (List<T>) null);
	}
	/**
	 * Constructor with column index to be filtered. 
	 * And the list of accepted values.
	 * @param col the index of the column to filter
	 * @param list the list of accepted values
	 */
	public DistinctValuesFilter(int col, List<T> list) {
		super(col);
		setList(list);
	}
	/**
	 * Constructor with column index to be filtered. 
	 * And the list of accepted values.
	 * @param col the index of the column to filter
	 * @param list the list of accepted values
	 */
	public DistinctValuesFilter(int col, T... values) {
		super(col);
		setList(new TreeSet<T>(Arrays.asList(values)));
	}
	
	/**
	 * Define the list of accepted values and refresh the filter
	 * @see #refresh()
	 * @param list the list of accepted values
	 */
	public void setList(Collection<T> list) {
		_list = list;
		refresh();
	}

	@Override
	public boolean doTest(Object value) {
		if (_list == null || _list.size() == 0) {
			return true;
		}
		return _list.contains(value);
	}

	public List<T> getList() {
		return new ArrayList<T>(_list);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new DistinctValuesFilter(getColumnIndex(), getList());
	}
	
}
