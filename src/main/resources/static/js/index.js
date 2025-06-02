$(document).ready(function() {
    // Bắt sự kiện click trên exam-card
    $('.exam-card').click(function() {
        const examId = $(this).data('id'); // Lấy id từ data attribute

        // Gửi AJAX request
        $.ajax({
            url: '/exam',
            type: 'GET',
            data: {
                id: examId
            },
            success: function(response) {
                // Xử lý khi thành công
                window.location.href = '../exam?id=' + examId;
            },
            error: function(xhr) {
                // Xử lý lỗi
                alert('Error: ' + xhr.statusText);
            }
        });
    });
});