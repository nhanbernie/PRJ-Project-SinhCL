$(document).ready(function () {
  let menus = document.getElementsByClassName("nav-link");
  // lấy tất cả những tab có nav-link
  
  for (const menu of menus) {
	// nhận sự kiện
    menu.addEventListener("click", function () {
      let current = document.getElementsByClassName("active");
      if (current.length > 0) {
        current[0].className = current[0].className.replace(
          " active", ""
        );
      }
      this.className += " active";
    });
  }
  $('.sidebar-menu').click(function (e) {
    e.preventDefault();
    // xóa ?? even mặc định
    $("#content").load($(this).attr('href'));
    // append vào cái id content by .load
  });
  $("#default-menu").click();
});
// jquery java 
// $ jquery no $ is js