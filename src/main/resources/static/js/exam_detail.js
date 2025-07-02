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
    console.log("Username:", nameCookie);

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
                        <a href="/schedule">Lịch học của tôi</a>
                        <a href="/user/profile">Trang cá nhân</a>
                        <a  id="logoutBtn">Đăng xuất</a>
                    </div>
                </div>
            `;

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
function prac() {
    // Lấy dữ liệu từ form
    const selectedParts = $('input[name="part"]:checked').map(function () {
        return $(this).val();
    }).get();

    const timeLimit = $('#timeLimit').val();
    alert(selectedParts);
    // Validate
    if (selectedParts.length === 0) {
        alert('Vui lòng chọn ít nhất một phần thi');
        return;
    }
    fetch('/exam/practice', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
            selectedParts: selectedParts,
            timeLimit: timeLimit || null
        })
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
        });

}