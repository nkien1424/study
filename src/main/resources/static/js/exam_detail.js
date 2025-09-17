document.addEventListener('DOMContentLoaded',async  function() {
    let nameCookie = 'guest';
    let id = 0;
    try {
        const res = await fetch("/auth/getCookie", {
            method: 'POST',
            credentials: "include"
        });

        if (res.ok) {
            const data = await res.json();
            nameCookie = data.username || 'guest';
            var role= data.role;
            id = data.id;
        }
    } catch (err) {
        console.error("Lỗi khi lấy cookie:", err);
    }

    //const token = localStorage.getItem('access_token');
    //const userData = token ? parseJwt(token) : null;
    const authSection = document.getElementById("auth-section");
    // Lấy username từ Google nếu có, hoặc từ JWT token nếu không
    const userGoogleElement = document.getElementById('userGoogle');
    const userNameFromGoogle = userGoogleElement ? userGoogleElement.value : null;

    // Ưu tiên username từ Google, nếu không có thì lấy từ token
    const name = userNameFromGoogle || nameCookie; // 'guest' là giá trị mặc định

    //console.log("User data:", userData);
    //console.log("Username:", nameCookie);

    document.querySelectorAll('.exam-card').forEach(card => {
        card.addEventListener('click', function() {
            const examId = this.getAttribute('data-id');
            if (examId) {
                window.location.href = `/exam?id_exam=${examId}&id_user=${id}`;
            }
        });
    });

        if (nameCookie !== 'guest' &&nameCookie !== null ) {
            authSection.innerHTML = `
                <div class="dropdown">
                    <img src="/images/user_image.jpg" alt="Avatar" class="avatar" id="avatarBtn">
                    <div class="dropdown-content" id="dropdownMenu">
                        <div class="notification-header">
                            <strong>Thông báo</strong>
                            <p style="margin: 0;">Bạn chưa có thông báo mới.</p>
                            <a href="/notifications">Xem tất cả >></a>
                        </div>
                        <hr>
                        <a id="admin" href="/admin">Trang Quản lý </a>
                        <a href="/schedule">Lịch học của tôi</a>
                        <a href="/user/profile">Trang cá nhân</a>
                        <a  id="logoutBtn">Đăng xuất</a>
                    </div>
                </div>
            `;
            if (Array.isArray(role) && role.includes("ADMIN")) {

                document.querySelector('#admin').style.display = 'block';
            } else {
                document.querySelector('#admin').style.display = 'none';
            }

            setTimeout(() => {
                const avatarBtn = document.getElementById("avatarBtn");
                const dropdown = document.getElementById("dropdownMenu");
                const logoutBtn = document.getElementById("logoutBtn");

                avatarBtn.addEventListener("click", function () {
                    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
                });

                logoutBtn.addEventListener("click", function (e) {
                    e.preventDefault();
                    fetch('/user/logout', {
                        method: 'POST',
                        credentials: 'include'
                    }).then(res => {
                        if (res.ok) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Đăng xuất thành công',
                                showConfirmButton: false,
                                timer: 1500
                            }).then(() => {
                                window.location.href = '/home'; // Hoặc trang bạn muốn redirect
                            });
                        }
                    });

                });

                document.addEventListener("click", function (event) {
                    if (!event.target.closest('.dropdown')) {
                        dropdown.style.display = "none";
                    }
                });
            }, 0);
        }



});
// Khởi tạo chế độ mặc định

let currentMode = 'practice'; // Giá trị mặc định

function changeMode(el) {
    // Bỏ active ở tất cả tab
    document.querySelectorAll('.mode-tab').forEach(t => t.classList.remove('active'));

    // Thêm active cho tab được chọn
    el.classList.add('active');
    currentMode = el.dataset.mode;

    // Ẩn/hiện phần chọn part
    const tagSection = document.querySelector('.tag-section');
    const queryDescription =document.querySelector('#modeDescription');
    const queryDescription_full =document.querySelector('#modeDescription_full');
    const queryComment =document.querySelector('.comments-card');

        if (currentMode === 'fulltest') {
            tagSection.style.display = 'none';
            queryDescription.style.display= 'none';
            queryDescription_full.style.display = 'block';
            queryComment.style.display = 'none';
        } else if(currentMode === 'practice') {
            tagSection.style.display = 'block';
            queryDescription.style.display= 'block';
            queryDescription_full.style.display = 'none';
            queryComment.style.display = 'block';

        }else {
            tagSection.style.display = 'none';
            queryDescription.style.display= 'none';
            queryDescription_full.style.display = 'none';
            queryComment.style.display = 'block';
        }

}

// Hàm prac() tích hợp đa chế độ
function prac() {

    if (!document.cookie.includes("jwt=")) {
        Swal.fire({ icon: 'error', title: 'Vui lòng đăng nhập để làm bài', timer: 1500, showConfirmButton: false });
        return;
    }

    switch(currentMode) {
        case 'practice':
            const selectedParts = $('input[name="part"]:checked').map(function() {
                return $(this).val();
            }).get();

            if (selectedParts.length === 0) {
                alert('Vui lòng chọn ít nhất một phần thi');
                return;
            }

            sendRequest({
                selectedParts: selectedParts,
                timeLimit: $('#timeLimit').val() || null,
                mode: 'practice'
            });
            break;

        case 'fulltest':
            sendRequest({
                selectedParts: [1, 2, 3, 4, 5, 6, 7], // Tất cả part
                timeLimit: 120, // Mặc định 120 phút cho full test
                mode: 'fulltest'
            });
            break;

        case 'discuss':
            window.location.href = '/discussion';
            break;
    }
}

function sendRequest(data) {
    fetch('/exam/practice', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(data)
    }).then(response => {
        if (response.redirected) window.location.href = response.url;
    });
}
$(document).ready(function () {
    // Lấy CSRF token nếu có (Thymeleaf + Spring Security)
    const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : null;
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN';

    $(".comment-form").off('submit').on("submit", function (e) {
        e.preventDefault();

        const content = $("#commentInput").val().trim(); // LẤY VALUE chứ không phải element
        if (!content) {
            Swal.fire({ icon: 'error', title: 'Vui lòng nhập nội dung', timer: 1500, showConfirmButton: false });
            return;
        }
        try {
            const response =  fetch("http://localhost:8080/auth/getCookie", {
                method: "POST",
                credentials: "include" // 🔑 quan trọng để gửi cookie kèm theo
            });

            if (response.ok) {
                const data =  response.json();

            } else {
                Swal.fire({ icon: 'error', title: 'Vui lòng đăng nhập để comment nội dung', timer: 1500, showConfirmButton: false });
                return;
            }
        } catch (error) {
            console.error("Lỗi khi gọi API:", error);
        }

        $.ajax({
            type: "POST",
            url: $(this).attr("action") || "/exam/comment",
            data: JSON.stringify({ content: content }), // gửi JSON
            contentType: "application/json; charset=UTF-8",
            dataType: "html", // kỳ vọng server trả về HTML fragment
            beforeSend: function (xhr) {
                if (csrfToken) xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (htmlFragment) {
                // thay toàn bộ phần comment-list bằng fragment server trả về
                $(".comment-list").first().replaceWith(htmlFragment);
                // Nếu server trả fragment là <ul class="comment-list">...</ul> thì replaceWith OK
                $("#commentInput").val(""); // clear
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    Swal.fire({ icon: 'error', title: 'Bạn cần đăng nhập để bình luận', timer: 1500, showConfirmButton: false });
                } else {
                    Swal.fire({ icon: 'error', title: 'Có lỗi', text: 'Không thể gửi bình luận' });
                }
            }
        });
    });
});
function thongbao() {

    // Ngăn hành vi mặc định của thẻ <a>
    Swal.fire({
        icon: 'info',
        title: '🚧 Đang phát triển',
        text: 'Tính năng này sẽ sớm ra mắt!',
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true
    });
}