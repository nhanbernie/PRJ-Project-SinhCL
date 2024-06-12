package com.se4f7.prj301.controller.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// class gọi này được gọi khi người dùng nhập đường link này 
@WebServlet(urlPatterns = { "/admin", "/admin/home" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	// method này sẽ gọi đến trang jsp admin jsp khi người dùng nhập link trên
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin/index.jsp");
		requestDispatcher.forward(request, response);
	}
}
