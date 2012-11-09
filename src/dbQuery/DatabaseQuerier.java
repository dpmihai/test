package dbQuery;

/*
 * (c) 2004, Slav Boleslawski
 *
 * Released under terms of the Artistic Licence
 * http://www.opensource.org/licences/artistic-licence.php
 */

import java.sql.SQLException;
import java.util.List;

/**
 * This interface is required for the demonstration purposes only. It requires
 * the implementing class to define the <code>getRecords()</code> and <code>close()</code>
 * methods.
 */

public interface DatabaseQuerier {

	List getRecords(String query) throws SQLException, InterruptedException;
	void close();

}
