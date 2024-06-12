package com.se4f7.prj301.controller.admin.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.se4f7.prj301.constants.ErrorMessage;
import com.se4f7.prj301.constants.QueryType;
import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.CategoryModelRequest;
import com.se4f7.prj301.model.response.CategoryModelResponse;
import com.se4f7.prj301.service.CategoryService;
import com.se4f7.prj301.service.impl.CategoryServiceImpl;
import com.se4f7.prj301.utils.HttpUtil;
import com.se4f7.prj301.utils.ResponseUtil;

@WebServlet(urlPatterns = { "/api/category" })
public class CategoryController extends HttpServlet {

	private static final long serialVersionUID = -331986167361646886L;

	private CategoryService categoryService;

	
	// tạo một service để gọi method với từng featrure mà client request
	public void init() {
		categoryService = new CategoryServiceImpl();
	}

	
	
	// các method này để thực hiện tính năng dược yêu cầu và chuyển đối thành JSON theo kiểu đối tượng
	
	
	// tạo một category mới (put)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			
			// httpUtil chứa các method tĩnh để xử lý các thao tác liên quan đến HTTP.
			// doPost là method để create (cách phương thức do... chỉ mang tính định nghĩa để người khác hiểu code)
			// phương thức này nhập một request từ client là red. getReader để lấy dữ liệu trả về đối tượng BufferReader.
			// CategoryModelRequest.class convert từ BufferReader sang đối tượng CategoryModelRequest (object này data request)
			// toModel chuyển đổi nội dung JSON thành object
			CategoryModelRequest requestBody = HttpUtil.of(req.getReader()).toModel(CategoryModelRequest.class);
			
			// kiểm tra kết quả có đúng không khi thực hiện tạo một category mới. bằng cách gọi method create trong CategoryService
			boolean result = categoryService.create(requestBody);
			
			// method success này được sử dụng để chuẩn bị và gửi một phản hồi JSON thành công tới client. 
			ResponseUtil.success(resp, result);
		} catch (Exception e) {
			ResponseUtil.error(resp, e.getMessage());
		}
	}

	
	// update category (Put)
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CategoryModelRequest requestBody = HttpUtil.of(req.getReader()).toModel(CategoryModelRequest.class);
			// lấy parameter là id để update
			boolean result = categoryService.update(req.getParameter("id"), requestBody);
			ResponseUtil.success(resp, result);
		} catch (Exception e) {
			ResponseUtil.error(resp, e.getMessage());
		}
	}

	
	// xóa category (Delete)
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			boolean result = categoryService.deleteById(req.getParameter("id"));
			ResponseUtil.success(resp, result);
		} catch (Exception e) {
			ResponseUtil.error(resp, e.getMessage());
		}
	}

	
	// search và hiển thị
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			
			// lấy paremeter là type gì
			String type = req.getParameter("type");
			
			switch (type) {
			// QueryType.FILTER trong package constants
			// QueryType.FILTER lấy dữ liệu từ class với attribute FILTER với data là filter (lấy tất cả là hiển thị tất cả category) 
			// NẾU GET_ONE thì data là getOne (cearch dữ liệu theo tên do client nhập
			
			// get all category
			case QueryType.FILTER:
				String name = req.getParameter("name");
				String page = req.getParameter("page");
				String size = req.getParameter("size");
				
				
				PaginationModel results = categoryService.filter(page, size, name);
				ResponseUtil.success(resp, results);
				break;
				
			// get one category	
			case QueryType.GET_ONE:
				String id = req.getParameter("id");
				CategoryModelResponse result = categoryService.getById(id);
				ResponseUtil.success(resp, result);
				break;
			default:
				ResponseUtil.error(resp, ErrorMessage.TYPE_INVALID);
			}
		} catch (Exception e) {
			ResponseUtil.error(resp, e.getMessage());
		}
	}
}
