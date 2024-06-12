package com.se4f7.prj301.service.impl;

import com.se4f7.prj301.constants.ErrorMessage;
import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.CategoryModelRequest;
import com.se4f7.prj301.model.response.CategoryModelResponse;
import com.se4f7.prj301.repository.CategoryRepository;
import com.se4f7.prj301.service.CategoryService;
import com.se4f7.prj301.utils.StringUtil;

public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository = new CategoryRepository();

	
	// method tạo category
	@Override
	public boolean create(CategoryModelRequest request) {

		   // create oldCategory object to get all data was exiting in DB
		CategoryModelResponse oldCategory = categoryRepository.getByName(request.getName());

		   // categoryRepository.getByName use to connect to database and query DB.
		
		
		   // if existing data was found by name -> send error to client
		if (oldCategory != null) {         
			throw new RuntimeException(ErrorMessage.NAME_IS_EXISTS);
		}
		   
		   // if this is a new name, return create(request)
		   // to crate new data
		return categoryRepository.create(request);
	}

	
	// method update
	@Override
	public boolean update(String id, CategoryModelRequest request) {
		// check data type
		Long idNumber = StringUtil.parseLong("Id", id);
		// lấy id từ bên DB lưu vào response
		CategoryModelResponse oldCategory = categoryRepository.getById(idNumber);
		
		// nếu (id) == null thì quăng lỗi
		if (oldCategory == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		
		// nếu có danh mục khác đã tồn tại với tên mới thì ném ra một ngoại lệ để ngăn chặn việc cập nhật với tên bị trùng
		if (!request.getName().equalsIgnoreCase(oldCategory.getName())) {
			CategoryModelResponse otherCategory = categoryRepository.getByName(request.getName());
			if (otherCategory != null) {
				throw new RuntimeException(ErrorMessage.NAME_IS_EXISTS);
			}
		}
		// nếu không bị những if trên thì update
		return categoryRepository.update(idNumber, request);
	}

	
	// method xóa bằng id
	@Override
	public boolean deleteById(String id) {
		Long idNumber = StringUtil.parseLong("Id", id);
		CategoryModelResponse oldCategory = categoryRepository.getById(idNumber);
		if (oldCategory == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		return categoryRepository.deleteById(idNumber);
	}
	
	// method lấy category bằng id
	@Override
	public CategoryModelResponse getById(String id) {
		Long idNumber = StringUtil.parseLong("Id", id);
		CategoryModelResponse oldCategory = categoryRepository.getById(idNumber);
		if (oldCategory == null) {
			throw new RuntimeException(ErrorMessage.RECORD_NOT_FOUND);
		}
		return oldCategory;
	}

	// method để gọi các method khác sau đó return về số trang số nội dung trên trang
	@Override
	public PaginationModel filter(String page, String size, String name) {
		int pageNumber = StringUtil.parseInt("Page", page);
		int sizeNumber = StringUtil.parseInt("Size", size);
		return categoryRepository.filterByName(pageNumber, sizeNumber, name);
	}

}
