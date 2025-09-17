document.querySelectorAll('.blog-card').forEach(card => {
    card.addEventListener('click', function () {
        const blogId = this.getAttribute('data-id');
        if (blogId) {
            window.location.href = `/blog?blogId=1` ;
        }
    });
});

    // Nút chuyển lên đầu trang
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");

    window.addEventListener("scroll", () => {
    if (window.scrollY > 200) {
    scrollToTopBtn.classList.add("show");
} else {
    scrollToTopBtn.classList.remove("show");
}
});

    scrollToTopBtn.addEventListener("click", () => {
    window.scrollTo({top: 0, behavior: "smooth"});
});

    // Optional: Smooth scroll cho anchor link (nếu có)
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener("click", function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute("href"));
        if (target) {
            target.scrollIntoView({behavior: "smooth"});
        }
    });
});

