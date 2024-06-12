package com.se4f7.prj301.service;

import com.se4f7.prj301.model.PaginationModel;
import com.se4f7.prj301.model.request.CategoryModelRequest;
import com.se4f7.prj301.model.response.CategoryModelResponse;


// một interface chưa tất cả tính năng sẽ được implement
public interface CategoryService {

	public boolean create(CategoryModelRequest request);

	public boolean update(String id, CategoryModelRequest request);

	public boolean deleteById(String id);

	public CategoryModelResponse getById(String id);

	public PaginationModel filter(String page, String size, String name);

}
