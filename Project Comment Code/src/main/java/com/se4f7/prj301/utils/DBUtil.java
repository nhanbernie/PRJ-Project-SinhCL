package com.se4f7.prj301.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// class kết nối đến database
public class DBUtil {
	private static Connection conn;

	public DBUtil() {
	}

	public static Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				// getBundle lấy nhưng file trường là đuôi properties
				// ResourceBundle với method trên đọc data với key và data
				
				ResourceBundle rb = ResourceBundle.getBundle("application");
				// đọc từng key trong file application rồi lưu với 4 biến String được khởi tạo
				// gọi phương thức getConnection với port username và pass
				String connectionString = rb.getString("db.connectionString");
				String driverName = rb.getString("db.driverName");
				String username = rb.getString("db.username");
				String password = rb.getString("db.password");
				try {
					Class.forName(driverName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				conn = DriverManager.getConnection(connectionString, username, password);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Connection with database fail !");
		}

		return conn;
	}

	
	// đóng kết nối
	public static void closeConnection() {

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void closeConnection(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(PreparedStatement ps) {
		try {
			if (ps != null && !ps.isClosed()) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	// print lỗi
	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
