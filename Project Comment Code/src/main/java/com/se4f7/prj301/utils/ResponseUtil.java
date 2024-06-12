package com.se4f7.prj301.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.se4f7.prj301.model.ResponseModel;

public class ResponseUtil {

	public static void success(HttpServletResponse resp, Object respData) {
		try {
			
			// mã hóa ký tự theo kiểu UTF8
			resp.setCharacterEncoding("UTF-8");
			// dữ liệu sẽ phản hồi dưới dạng json
			resp.setContentType("application/json");
			
			// ObjectMapper là của thư viện Jackson để ánh xạ các đối tượng java thành chuỗi JSON và ngược lại
			// phương thức writer của ObjectMapper trả về một đối tượng ObjectWriter
			// ObjectWriter được sử dụng để ghi các đối tượng java thành chuỗi json
			// withDefaultPrettyPrinter là định dạng JSON cho đẹp và dễ đọc
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			
			// tạo một object lưu data response. truyền các tham số true để check thành công hay không, respData và null là k có lỗi gì để gửi lên client
			ResponseModel responseModel = new ResponseModel(true, respData, null);
			
			// chuyển đổi đối tượng responseModel thành một chuỗi JSON và gán nó vào biến respStr
			String respStr = ow.writeValueAsString(responseModel);
			
			// ghi chuỗi jSON respStr vào phản hồi HTTP
			resp.getWriter().print(respStr);
			// flush đảm bảo dữ liệu được gửi đi
			resp.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void error(HttpServletResponse resp, String errMsg) {
		try {
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// khác ở trên một chút vì method này là erro nên sẽ truyền false để lưu vào responModel là lỗi, null là k có  dữ liệu để phản hồi, errMsg là gửi lỗi đến client
			ResponseModel responseModel = new ResponseModel(false, null, errMsg);
			String respStr = ow.writeValueAsString(responseModel);
			resp.getWriter().print(respStr);
			resp.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
