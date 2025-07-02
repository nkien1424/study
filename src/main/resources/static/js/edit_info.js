
    function showTab(tabId) {
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    document.querySelector(`[onclick="showTab('${tabId}')"]`).classList.add('active');
    document.getElementById(tabId).classList.add('active');
}

    document.getElementById('changePasswordForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const oldPass = document.getElementById('oldPassword').value;
    const newPass = document.getElementById('newPassword').value;
    const confirm = document.getElementById('confirmPassword').value;

    if (newPass !== confirm) {
        Swal.fire({
            icon: 'error',
            title: 'Mật khẩu mới không khớp nhau',
            showConfirmButton: false,
            timer: 1500,
        })
    return;
    }

    // Gửi lên server
    fetch('/user/change-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ oldPassword: oldPass, newPassword: newPass })
})
    .then(res => {
    if (res.ok) {
        Swal.fire({
            icon: 'success',
            title: 'Đổi mật khẩu thành công',
            showConfirmButton: false,
            timer: 1500,
        })
} else {
        Swal.fire({
            icon: 'error',
            title: 'Đổi mật khẩu thất bại',
            showConfirmButton: false,
            timer: 1500,
        })
}
});
});

    document.getElementById('editProfileForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const name = document.getElementById('username').value;
        fetch('/user/change-name', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({  newName:name })
        })
            .then(res => {
                if (res.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Đổi tên thành công',
                        showConfirmButton: false,
                        timer: 1500,
                    })
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Đổi tên thất bại',
                        showConfirmButton: false,
                        timer: 1500,
                    })
                }
            });
    });
