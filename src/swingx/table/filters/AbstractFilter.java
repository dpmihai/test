package swingx.table.filters;

import java.util.ArrayList;

import org.jdesktop.swingx.decorator.Filter;

/**
 * @author Thierry LEFORT
 * 4 déc. 07
 * 
 * Abstract filter used to create the list of index to be displayed.
 * 
 * The method {@link #doTest(int)} is to be implemented in order to create 
 * the list of the index to display. 
 * 
 */
public abstract class AbstractFilter extends Filter {
	
	
	
	private ArrayList<Integer>	toPrevious;


	public AbstractFilter(int col) {
		super(col);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void filter() {
		int inputSize = getInputSize();
		int current = 0;
		for (int i = 0; i < inputSize; i++) {
			if (test(i)) {
				toPrevious.add(new Integer(i));
				// generate inverse map entry while we are here
				fromPrevious[i] = current++;
			}
		}
	}

	/**
	 * Resets the internal row mappings from this filter to the previous filter.
	 */
	@Override
	protected void reset() {
		toPrevious.clear();
		int inputSize = getInputSize();
		fromPrevious = new int[inputSize];  // fromPrevious is inherited protected
		for (int i = 0; i < inputSize; i++) {
			fromPrevious[i] = -1;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return toPrevious.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int mapTowardModel(int row) {
		return toPrevious.get(row);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() {
		toPrevious = new ArrayList<Integer>();
	}
	/**
	 * 
	 * @param row
	 * @return
	 */
	public final boolean test(int row) {
		if (adapter == null) {
			return false;
		}
		// ask the adapter if the column should be includes
		if (!adapter.isTestable(getColumnIndex())) {
			return false; 
		}
		Object value = getInputValue(row, getColumnIndex());
		return value == null ? false : doTest(value);
	}

	/**
	 * Tests whether the given row (in this filter's coordinates) should
	 * be added. <p>
	 * 
	 * PENDING JW: why is this public? called from a protected method? 
	 * @param row the row to test
	 * @return true if the row should be added, false if not.
	 * @throws ClassCastException if the tested type is not T
	 */
	public abstract boolean doTest(Object value);


}
