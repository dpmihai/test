package dbQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleImageTest {
	
	public static Connection openConnection(String username, String password) throws SQLException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (java.lang.ClassNotFoundException e) {
			throw new SQLException("Cannot load database driver");
		}
		String url = "jdbc:oracle:thin:@anytime-dev.intranet.asf.ro:1521:XE";
		return DriverManager.getConnection(url, username, password);
	}

	public static void insertImage(Connection conn, String name, String img) {

		int len;
		PreparedStatement pstmt;
		try {
			String query = "INSERT INTO blobs VALUES (?, ?)";						
			File file = new File(img);
			FileInputStream fis = new FileInputStream(file);
			len = (int) file.length();

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setBinaryStream(2, fis, len);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getImageData(Connection conn, String name) {

		byte[] fileBytes;
		String query;
		try {
			query = "select blob_col from blobs where id like '" + name + "'";
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(query);
			if (rs.next()) {
				Blob aBlob = rs.getBlob(1);	                
				fileBytes = aBlob.getBytes(1, (int) aBlob.length());
				OutputStream targetFile = new FileOutputStream("d://new.png");
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
			con = openConnection("anytime2", "anytime2");
			System.out.println("Connected");
			insertImage(con, "Mike", "D:\\Public\\nextreports-server-os\\webapp\\images\\drill_up.png");		
			System.out.println("Image inserted");
			getImageData(con, "Mike");
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
