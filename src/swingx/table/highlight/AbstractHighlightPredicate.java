package swingx.table.highlight;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

public abstract class AbstractHighlightPredicate implements HighlightPredicate {

	private boolean _highLightOnlyCells;
	
	private int _modelColumn;
	
	public AbstractHighlightPredicate(int modelColumn, boolean onlyCells) {
		_highLightOnlyCells = onlyCells;
		_modelColumn = modelColumn;
	}
	
	public boolean isHighlightingOnlyCells() {
		return _highLightOnlyCells;
	}
	
	public final boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
		if (adapter.modelToView(_modelColumn) != adapter.column
				&& _highLightOnlyCells) {
			return false;
		}
		Object value = adapter.getFilteredValueAt(adapter.row, _modelColumn);
		return test(value);
	}
	
	public abstract boolean test(Object value);

}
