package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class UserDatabase {
	private final String driver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/card";
	private final String userName = "root";
	private final String userPassword = "mysql12345";
	
//	static int user_id = 0;
	
	Connection con = null;
	Statement stmt = null;
	
	
	public boolean createConnection() {
		try{
			Class.forName(driver);
			con = (Connection) DriverManager.getConnection(url, userName, userPassword);
	//		insert();
			System.out.println("数据库连接成功");
			return true;
		}
		catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        return false;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
		
		
		
		
	}
	
/*	public static void main(String[] args) {
		UserDatabase ud = new UserDatabase();
		ud.createConnection();
//		ud.insert();
	}*/
/*	
	public void insert(String name, String password) {
	    String sql = "insert into user_information (user_name,password) values(?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) con.prepareStatement(sql);
	        pstmt.setString(1, name);
	        pstmt.setString(2, password);
	        pstmt.executeUpdate();
	        pstmt.close();
	//        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public String getUserZan(String str, String word, String user) {
		String numLike = "";
		PreparedStatement pstmt;
		String sql = "select " + str + " from user_like where word = " + "'" + word + "' and user_name = " + "'" + user + "'";
//		String sql = "select youdao from likeNum where word = '" + word + "'";
		try{
			
			pstmt = (PreparedStatement) con.prepareStatement(sql);
	        ResultSet rset = pstmt.executeQuery();
	        if (rset.next()) {
	        	numLike = rset.getString(1);
	        }
	        else {
	        	numLike = "0";
	        }
	        pstmt.close();
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }	
		return numLike;
	}
	
	*/
	
}
