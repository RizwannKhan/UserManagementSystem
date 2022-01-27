package com.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.user.bean.User;

public class UserDao {

	private static final String url = "jdbc:mysql://localhost:3306/user_management?useSSL=false";
	private static final String username = "root";
	private static final String password = "1234";
	private static final String driver = "com.mysql.jdbc.Driver";

	private static final String INSERT_USERS_SQL = "INSERT INTO users(name, email, country) VALUES  (?, ?, ?)";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?";

	public UserDao() {

	}

	protected Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	// create user method...
	public void createUser(User user) {
		System.out.println(INSERT_USERS_SQL);
		try {
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_USERS_SQL);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getCountry());
			System.out.println(stmt);
			stmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	// get user by userId...
	public User getUserById(int id) {
		User user = null;
		try {
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_USER_BY_ID);
			stmt.setInt(1, id);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(name, email, country);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}

	// get all users...
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();

		try {
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_ALL_USERS);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}

	// edit the user by user id...
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated = false;
		try {
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_USERS_SQL);
			System.out.println("updated USer:" + stmt);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getCountry());
			stmt.setInt(4, user.getId());

			rowUpdated = stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			printSQLException(e);
		}
		return rowUpdated;
	}

	// delete user by id....
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted = false;
		try {
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_USERS_SQL);
			stmt.setInt(1, id);
			rowDeleted = stmt.executeUpdate() > 0;
		}catch (SQLException e) {
			printSQLException(e);
		}
		return rowDeleted;
	}

	private void printSQLException(SQLException ex) {
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
