package com.user.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.user.bean.User;
import com.user.dao.UserDao;

@WebServlet("/")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserDao userDao;

	@Override
	public void init() throws ServletException {
		userDao = new UserDao();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getServletPath();

		switch (action) {
		case "/new":
			showNewForm(req, resp);
			break;

		case "/insert":
			insertUser(req, resp);
			break;

		case "/delete":
			deleteUser(req, resp);
			break;

		case "/edit":
			showEditForm(req, resp);
			break;

		case "/update":
			updateUser(req, resp);
			break;

		default:
			listUser(req, resp);
			break;
		}
	}

	private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("user-form.jsp");
		rd.forward(req, resp);
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		userDao.createUser(newUser);
		resp.sendRedirect("list");
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDao.getUserById(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		User user = new User(id, name, email, country);
		try {
			userDao.updateUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		List<User> listUser = userDao.getUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

}
