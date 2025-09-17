
    // Navigation
    function showPage(pageId) {
    // Hide all pages
    var pages = document.querySelectorAll('.page');
    for (var i = 0; i < pages.length; i++) {
    pages[i].classList.remove('active');
}

    // Remove active class from all nav items
    var navItems = document.querySelectorAll('.nav-item');
    for (var i = 0; i < navItems.length; i++) {
    navItems[i].classList.remove('active');
}

    // Show selected page
    document.getElementById(pageId).classList.add('active');

    // Add active class to clicked nav item
    event.target.closest('.nav-item').classList.add('active');
}

    // Filter users (for search functionality)
    function filterUsers(searchTerm) {
    var rows = document.querySelectorAll('#userTableBody tr');
    for (var i = 0; i < rows.length; i++) {
    var row = rows[i];
    var name = row.querySelector('.user-name').textContent.toLowerCase();
    var email = row.querySelector('.user-email').textContent.toLowerCase();

    if (name.includes(searchTerm.toLowerCase()) || email.includes(searchTerm.toLowerCase())) {
    row.style.display = '';
} else {
    row.style.display = 'none';
}
}
}

    // User actions
    function viewUser(id) {
    alert('Xem chi tiết user ID: ' + id);
    // Redirect to user detail page
    // window.location.href = '/admin/users/' + id;
}

    function editUser(id) {
    alert('Chỉnh sửa user ID: ' + id);

}

    function deleteUser(id) {
    if (confirm('Bạn có chắc muốn xóa user này?')) {
    alert('Xóa user ID: ' + id);
    $.ajax({
    type: "POST",
    url: "/admin/delete",
    data: id, // gửi thẳng text
    contentType: "text/plain; charset=UTF-8",
    success: function(res) {


    Swal.fire({
    icon: 'success',
    title: 'Xóa User thành công!',
    showConfirmButton: false,
    timer: 1500
}).then(() => {
    location.reload(); // Chuyển hướng
});

},
    error: function(xhr) {
    if (xhr.status === 401) {
    alert("Bạn cần đăng nhập để bình luận!");
} else {
    alert("Có lỗi xảy ra!");
}
}
});
}
}

    function addUser() {
    alert('Thêm user mới');
    // Redirect to add user page
    // window.location.href = '/admin/users/add';
}

    // Exam actions
    function viewExam(id) {
    alert('Xem chi tiết đề thi ID: ' + id);
    // window.location.href = '/admin/exams/' + id;
}

    function editExam(id) {
    alert('Chỉnh sửa đề thi ID: ' + id);
    // window.location.href = '/admin/exams/' + id + '/edit';
}

    function deleteExam(id) {
    if (confirm('Bạn có chắc muốn xóa đề thi này?')) {
    alert('Xóa đề thi ID: ' + id);
    // window.location.href = '/admin/exams/' + id + '/delete';
}
}

    function addExam() {
    alert('Tạo đề thi mới');
    // window.location.href = '/admin/exams/add';
}
