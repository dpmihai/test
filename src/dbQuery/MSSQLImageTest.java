package dbQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MSSQLImageTest {

	public static Connection openConnection(String dataSourceName, String username, String password) throws SQLException {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			throw new SQLException("Cannot load database driver");
		}
		String url = "jdbc:jtds:sqlserver://192.168.16.86:1433/" + dataSourceName;
		return DriverManager.getConnection(url, username, password);
	}

	public static void insertImage(Connection conn, int id, String name, String img) {
		int len;
		String query;
		PreparedStatement pstmt;

		try {
			File file = new File(img);
			FileInputStream fis = new FileInputStream(file);
			len = (int) file.length();

			query = ("insert HumanResources.EmployeesImage2 (Id, Name, Photo)  VALUES(?,?,?)");
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setString(2, name);						
			pstmt.setBinaryStream(3, fis, len);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getImageData(Connection conn, int id) {

		byte[] fileBytes;
		String query;
		try {
			query = "select Photo from HumanResources.EmployeesImage2 where Id = " + id;
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(query);
			if (rs.next()) {
				fileBytes = rs.getBytes(1);
				OutputStream targetFile = new FileOutputStream("d://new.JPG");
				targetFile.write(fileBytes);
				targetFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Connection con = null;
		try {
			con = openConnection("AdventureWorks2008R2", "sa", "mumutz6");
			System.out.println("Connected");
			//insertImage(con, 1, "Mike", "D:\\Public\\nextreports-server-os\\webapp\\images\\drill_up.png");
			insertImage(con, 1, "Mike", "D:\\Public\\nextreports-server-os\\webapp\\images\\drill_up.png");		
			//System.out.println("Image inserted");
			getImageData(con, 1);
			System.out.println("Image read");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
