// hàm này đảm bảo js chỉ được thực thi sau khi toàn bộ trang web tỉa xong
$(document).ready(function() {
	// dấu đô la là Jquery
	// gán một biến paginatioin cho phần tử dom với id là categoryPaginaiton
	// thao tác với phần tử đó để them sửa xóa
	var $pagination = $('#categoryPagination');
	// lưu tên để tìm kiếm
	var inpSearchCategoryName = "";

	// twbsPagination là plugin của của jquery (hoạt động tốt với bootstrap)
	// $pagination lưa phần tử dom với id là câtegoryPagination
	// defaulOpts là cấu hình cho twbs như số trang, trang hiện tại nut hiện tại
	$pagination.twbsPagination(defaultOpts);


	this.onSearchByName = function() {
		// lưu vào biến này nếu get được data
		inpSearchCategoryName = $("#inpSearchCategoryName").val();
		// phương thức getCategories với tham số: trang bắt đầu, kích thước trang và tên danh mục tìm kiếm
		this.getCategories(0, defaultPageSize, inpSearchCategoryName);
	}

	// Call API get categories.
	this.getCategories = function(page = 0, size = defaultPageSize, name = "") {
		// gọi server từ phía client
		// ajax giúp tăng tốc độ trang web vì không phải load lại trang. và ajax được kết hợp từ nhiều công nghệ khác nhau
		// như html css dom, json, xml
		// ajax trong đoạn này để yêu cầu server để lấy list categories bằng method get
		$.ajax({
			type: 'GET',
			dataType: "json", // trả dữ liệu về json từ server
			// syntax jquery

			// đường dẫn để lấy dữ liệu từ server
			url: `${domain}/api/category?type=filter&page=${page}&size=${size}&name=${name}`, // is string 
			// replace http://localhost:8082/study-with-me//api/category?type=filter&name&page=0&size=3
			headers: {},
			// syntax `${}` string (name + "" + biến)

			// hàm callback thực hiện nếu request thành công từ ajax
			// success xử lý dl từ server xóa dữ liệu hiện tại và hủy đối tượng phân trang hiện tại sau đó lấy dữ liệu từng bảng ghi
			// tạo lại phân trang với các tùy chọn mới dựa trên dữ liệu trả về từ server
			success: (res) => {
				// lưu trữ HTML để thêm vào bảng
				let appendHTML = "";

				// jquery tìm element có id là tblCategories để xóa nội dung bên trong thẻ này
				$("#tblCategories").empty();

				// Reset pagination.
				// twbsPagination để hủy bỏ đối tượng phân trang hiện tại 
				$pagination.twbsPagination('destroy');

				// nếu không có dữ liệu thì append vào cái element với id .... là No Data
				if (!res.success || res.data.totalRecord === 0) {
					// Append text No Data when records empty;
					$("#tblCategories").append(`<tr><td colspan="9" style="text-align: center;">No Data</td></tr>`);
					return;
				}
				debugger
				// for of run hết các element 
				// duyệt hết các record sau đó append vào các hàng dữ liệu
				for (const record of res.data.records) {
					appendHTML += "<tr>";
					appendHTML += `<td>${record.id}</td>`;
					appendHTML += `<td>${record.name}</td>`;
					appendHTML += `<td>${record.description}</td>`;
					appendHTML +=
						`<td>
						<span class="badge ${record.status.toLocaleLowerCase() === 'active' ? 'bg-success' : 'bg-danger'}">
							${record.status}
						</span>
					</td>`;
					appendHTML += `<td>${record.createdBy}</td>`;
					appendHTML += `<td>${record.createdDate}</td>`;
					appendHTML += `<td>${record.updatedBy}</td>`;
					appendHTML += `<td>${record.updatedDate}</td>`;
					appendHTML +=
						`<td class="text-right">
							<a class="btn btn-info btn-sm" onclick="swicthViewCategory(false, ${record.id})">
								<i class="fas fa-pencil-alt"></i>
							</a>
							<a class="btn btn-danger btn-sm" onclick="deleteCategory(${record.id})">
								<i class="fas fa-trash"></i>
							</a>
						</td>`;
					appendHTML += "</tr>";
				}

				// Build pagination with twbsPagination.
				// tổng số trang dựa trên tổng số bản ghi và kích thước trang
				$pagination.twbsPagination($.extend({}, defaultOpts, {
					// trang bắt đầu là trang hiện tại cộng thêm 1 vì twbsPagination thường sử dụng chỉ số trang bắt đầu từ 1 trong khi chỉ số trang từ server có thể bắt đầu từ 0
					startPage: res.data.page + 1,
					totalPages: Math.ceil(res.data.totalRecord / res.data.size)
				}));

				// Add event listener when page change.
				$pagination
					.on('page', (event, num) => {
						this.getCategories(num - 1, defaultPageSize, inpSearchCategoryName);
					});

				// Append html table into tBody.
				$("#tblCategories").append(appendHTML);

			},
			
			// để hiển thị thông báo lỗi nếu yêu cầu thất bại.
			error: (err) => {
				toastr.error(err.errMsg);
				// toast jquery
			}
		});
	}

	// ẩn hiện table
	// Action change display screen between Table and Form Create/Edit.
	// js k có datatype nên case này nó tự hiểu là boolean
	this.swicthViewCategory = function(isViewTable, id = null) {
		if (isViewTable) {
			// case: true thì hiện bản ẩn form
			$("#category-table").css("display", "block");
			$("#category-form").css("display", "none");
			this.getCategories(0, defaultPageSize);
		} else {
			// case: false thì ngược lại
			$("#category-table").css("display", "none");
			$("#category-form").css("display", "block");
			if (id == null) {
				// nếu id là null thiết lập các trường trong form là rỗng
				$("#inpCategoryId").val(null);
				$("#inpCategoryName").val(null);
				$("#inpCategoryDesc").val(null);
			} else {
				// case nhấn vào edit category
				// nếu không null thì gọi hàm này để lấy data
				this.getCategoryById(id);
			}
		}
	};

	// Set default view mode is table.
	this.swicthViewCategory(true);

	// Call API delete category.
	// ajax với method delete
	this.deleteCategory = function(id) {
		$.ajax({
			type: 'DELETE',
			dataType: "json",
			url: `${domain}/api/category?id=${id}`,
			success: (res) => {
				if (res.success) {
					// sau khi xoa thì build lại cái table cho all category
					this.swicthViewCategory(true);
					toastr.success('Delete category success !')
				} else {
					// quăng lỗi nếu xóa không thành công
					toastr.error(res.errMsg);
				}
			},
			error: (err) => {
				toastr.error(err.errMsg);
			}
		});
	}

	// Call API get category by id.
	this.getCategoryById = function(id) {
		$.ajax({
			type: 'GET',
			dataType: "json",
			url: `${domain}/api/category?type=getOne&id=${id}`,
			success: (res) => {
				if (res.success) {
					$("#inpCategoryId").val(id);
					$("#inpCategoryName").val(res.data.name);
					$("#inpCategoryDesc").val(res.data.description);
				} else {
					toastr.error(res.errMsg);
				}
			},
			error: (err) => {
				toastr.error(err.errMsg);
			}
		});
	}

	// Call API create/edit category.
	this.saveCategory = function() {
		const currentId = $("#inpCategoryId").val();
		// val() không tryền thì get. nếu truyền val(id) thì set
		const body = {
			"name": $("#inpCategoryName").val(),
			"description": $("#inpCategoryDesc").val()
		}
		$.ajax({
			type: currentId ? 'PUT' : 'POST', // post truyền pram
			dataType: "json",
			// curentid imp với number
			url: currentId ? `${domain}/api/category?id=${currentId}` : `${domain}/api/category`,
			data: JSON.stringify(body),
			//JSON.stringify(body) ?? nhận string k nhận json direct
			// axios thì đọc json
			// string json is string


			// json.parel

			// json.stringtyfy
			success: (res) => {
				if (res.success) {
					// edit xong thì load lại 
					this.swicthViewCategory(true);
					toastr.success(`${currentId ? "Update" : "Create"} category success !`)
				} else {
					toastr.error(res.errMsg);
				}
			},
			error: (err) => {
				toastr.error(err.errMsg);
			}
		});
	};


});