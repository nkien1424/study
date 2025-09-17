let viewMode = 'all'; // 'all', 'selected', 'single'
//const Parts = document.getElementById("backendParts").value.split(',');
const timeLimit = document.getElementById("backendTime").value;

//let selectedParts =  new Set(Parts.map(Number));
//alert(selectedParts)
let i = 0;
//alert(Parts[0]);
let currentSinglePart = selectedParts[i];
const answeredQuestions = new Set();
//let timeLimit = parseInt(document.getElementById('time').textContent); // hoặc lấy từ biến backend truyền xuống

// Initialize
document.addEventListener('DOMContentLoaded', function () {
    setViewMode('single');
    total_questions()

});
let total_question = 0;
const gr = new Map();
gr.set(1, 6);       // Part 1: 6 câu
gr.set(2, 25);      // Part 2: 25 câu
gr.set(3, 39);      // Part 3: 29 câu
gr.set(4, 30);      // Part 4: 30 câu
gr.set(5, 30);      // Part 5: 30 câu
gr.set(6, 16);      // Part 6: 16 câu
gr.set(7, 54);      // Part 7: 54 câu

const partThresholds = [0, 1 ,7, 32, 71, 101, 131,147, 201]; // parts[0] không dùng

function findPart(questionNumber) {
    for (let i = 0; i <= partThresholds.length; i++) {
        if (questionNumber < partThresholds[i]) {
            return i-1 ; // Trả về part (từ 1-7)
        }
    }
    return partThresholds.length - 1; // Nếu lớn hơn ngưỡng cuối
}
function total_questions() {
    total_question = 0; // Reset lại tổng trước khi tính

    // Kiểm tra nếu selectedParts tồn tại
    if (selectedParts && selectedParts.length > 0) {
        for (let k = 0; k < selectedParts.length; k++) {
            // Lấy số câu hỏi của part tương ứng, mặc định là 0 nếu không tìm thấy
            total_question += gr.get(selectedParts[k]) || 0;
        }
    }

    // Cập nhật lên giao diện
    const currentElement = document.getElementById('total_question');
    if (currentElement) {
        currentElement.textContent = total_question;
    }
}

let totalSeconds = timeLimit * 60;

function updateTimer() {
    if (totalSeconds <= 0) {
        document.getElementById("time-info").textContent = "⏰ Hết giờ!";
        clearInterval(timerInterval);
        // Optional: tự động submit, chuyển trang,...
        return;
    }

    let minutes = Math.floor(totalSeconds / 60);
    let seconds = totalSeconds % 60;
    let formatted = `${minutes}:${seconds.toString().padStart(2, '0')}`;
    document.getElementById("time-info").textContent = `Thời gian còn lại: ${formatted}`;
    totalSeconds--;
}

// Gọi lần đầu để hiển thị ngay
updateTimer();

// Cập nhật mỗi giây
let timerInterval = setInterval(updateTimer, 1000);

function setViewMode(mode) {
    viewMode = mode;


    // Update part tabs appearance
    document.querySelectorAll('.part-tab').forEach(tab => {
        tab.classList.remove('active', 'selected');
    });

    if (mode === 'all') {
        showAllParts();
    } else if (mode === 'selected') {
        showSelectedParts();
    } else if (mode === 'single') {
        showSinglePart(currentSinglePart);
    }
}

function togglePart(partNumber) {
    const partTab = document.querySelector(`[data-part="${partNumber}"]`);

    if (viewMode === 'selected') {
        if (selectedParts.has(partNumber)) {
            selectedParts.delete(partNumber);
            partTab.classList.remove('selected');
        } else {
            selectedParts.add(partNumber);
            partTab.classList.add('selected');
        }
        showSelectedParts();
    } else if (viewMode === 'single') {
        // Remove active from all tabs


        // Set current single part

        showSinglePart(partNumber);
    }
}

function showSinglePart(partNumber) {
    // Hide all contents first
    document.querySelectorAll('.part-content').forEach(content => {
        content.classList.remove('active');
    });

    document.querySelectorAll('.part-tab').forEach(el => {
        const part = parseInt(el.getAttribute('data-part'), 10);
        if (selectedParts.includes(part)) {
            el.style.display = 'block'; // hoặc 'flex' nếu bạn dùng flexbox
        } else {
            el.style.display = 'none';
        }
    });
    document.querySelectorAll('[id^="nav-part"]').forEach(el => {
        // Lấy số phía sau "nav-part"
        const partId = parseInt(el.id.replace("nav-part", ""), 10);

        // So sánh với mảng selectedParts
        if (selectedParts.includes(partId)) {
            el.style.display = "block"; // hoặc "flex" tùy giao diện

        } else {
            el.style.display = "none";
        }
    });

    // Show only selected part
    const partContent = document.getElementById(`part${partNumber}`);
    const navSection = document.getElementById(`nav-part${partNumber}`);

    if (partContent) partContent.classList.add('active');
    if (navSection) navSection.style.display = 'block';
}

function restoreOriginalContent() {
    // This function would restore the original content structure
    // In a real application, you'd store the original HTML or rebuild it
    location.reload(); // Simple solution for demo
}

function toggleHighlight() {
    const toggle = document.querySelector('.toggle-switch');
    toggle.classList.toggle('active');
}


function selectOption(questionNumber, option) {
    if (!answeredQuestions.has(questionNumber)) {
        // Tăng số câu đã làm lên 1
        const currentElement = document.getElementById('sum_current-question');
        let currentCount = parseInt(currentElement.textContent) || 0;
        currentElement.textContent = currentCount + 1;

        // Đánh dấu câu hỏi đã được trả lời
        answeredQuestions.add(questionNumber);
    }

    const optionElement = document.getElementById(`q${questionNumber}${option.toLowerCase()}`);

    if (optionElement) {
        optionElement.checked = true;

        // Update visual state
        document.querySelectorAll(`input[name="q${questionNumber}"]`).forEach(input => {
            input.closest('.option').classList.remove('selected');
        });
        optionElement.closest('.option').classList.add('selected');
        let p = findPart(questionNumber);

        // Update navigation
        const navNumber = document.querySelector(`[onclick="goToQuestion(${questionNumber},${p})"]`);
        if (navNumber) {
            navNumber.classList.add('answered');
        }
    }
}




// Add CSS selector polyfill for :contains()
if (!CSS.supports('selector(:contains("test"))')) {
    // Fallback for browsers that don't support :contains()
    function goToQuestion(questionNumber, current_part) {

        if (current_part !== currentSinglePart) {
            showSinglePart(current_part);
            currentSinglePart = current_part;
        }
        const allQuestions = document.querySelectorAll('.question-number');
        let targetQuestion = null;
        allQuestions.forEach(q => {
            if (q.textContent.trim() === questionNumber.toString()) {
                targetQuestion = q.closest('.question');
            }
        });

        if (targetQuestion) {
            targetQuestion.scrollIntoView({behavior: 'smooth', block: 'center'});
        }

        // Update navigation current state
        document.querySelectorAll('.nav-question-number').forEach(num => {
            num.classList.remove('current');
        });
        const navButton = document.querySelector(`[onclick="goToQuestion(${questionNumber})"]`);
        if (navButton) {
            navButton.classList.add('current');
        }
    }
}

function BackPart() {
    if (i === selectedParts[0]) {
        showSinglePart(selectedParts[0]);
    } else {
        i -= 1;
        showSinglePart(selectedParts[i]);
    }
    setTimeout(() => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }, 100);
}

function NextPart() {
    if (i === selectedParts[selectedParts.length - 1]) {
        showSinglePart(selectedParts[selectedParts.length - 1]);
    } else {
        i += 1;
        showSinglePart(selectedParts[i]);
    }
    setTimeout(() => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }, 100);
}


    // Hàm hiển thị popup kết quả
    function showResultPopup(totalPoints, partScores) {
    // Cập nhật tổng điểm
    document.getElementById('totalScore').textContent = totalPoints;

    // Tạo grid cho các part
    const partsGrid = document.getElementById('partsGrid');
    partsGrid.innerHTML = '';

    // Tạo các part items
    for (let part = 1; part <= 7; part++) {
    const partScore = partScores[part] || 0;
    const partItem = document.createElement('div');
    partItem.className = 'part-item';
    partItem.innerHTML = `
                    <div class="part-number">PART ${part}</div>
                    <div class="part-score">${partScore}</div>
                `;
    partsGrid.appendChild(partItem);

}

    // Hiển thị popup
    document.getElementById('resultPopup').style.display = 'block';
        const popup = document.getElementById("resultPopup");

// Ngăn click ra ngoài tắt

    document.body.style.overflow = 'hidden'; // Ngăn scroll
}

    // Hàm đóng popup
    function closePopup() {
        document.getElementById('resultPopup').style.display = 'none';



    document.body.style.overflow = 'auto'; // Cho phép scroll lại
}

    // Hàm xem chi tiết (có thể redirect hoặc mở modal khác)
    function reviewAnswers() {
    alert('Chuyển đến trang xem chi tiết...');
    // window.location.href = '/exam/review';
}

    // Đóng popup khi click overlay
    document.getElementById('resultPopup').addEventListener('click', function(e) {
    if (e.target === this) {
    closePopup();
}
});

    // Đóng popup bằng ESC
    document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
    closePopup();
}
});

    // Demo function


    // ====== INTEGRATION CODE ======
    // Đây là code bạn cần thêm vào function submitTest() hiện tại

    function submitTest() {
    if (confirm('Bạn có chắc chắn muốn nộp bài không?')) {
    const questions = document.querySelectorAll('.question');
    const result = [];

    questions.forEach((question) => {
    const questionNumber = question.querySelector('.question-number').textContent;
    const selectedOption = question.querySelector('.option.selected');

    if (selectedOption) {
    const selectedValue = selectedOption.querySelector('input[type="radio"]').value;
    result.push({
    questionNumber: questionNumber,
    selectedAnswer: selectedValue
});
} else {
    result.push({
    questionNumber: questionNumber,
    selectedAnswer: null
});
}
});

    fetch('/exam/submit-answers', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(result),
})
    .then(response => {
    console.log("HTTP Status:", response.status);
    if (!response.ok) {
    return response.text().then(text => {throw new Error(text);});
}
    return response.json();
})
    .then(data => {

        console.log('Success:', data);

    // ==== THAY ĐỔI CHÍNH Ở ĐÂY ====
    // Thay vì alert, hiển thị popup với kết quả chi tiết
    showResultPopup(data.totalPoint, data.partScores || {});
        let btn = document.querySelector(".submit-nav");
        btn.onclick = null; // xoá sự kiện click
        btn.style.pointerEvents = "none"; // chặn click chuột
        btn.style.opacity = "0.5";
        document.getElementById("back-btn").style.display = "block";
    })
    .catch(error => {
    console.error('Full Error:', error);
    alert('Error: ' + error.message);
});
}
}
function disableInputs() {

    document.querySelectorAll('input[type="radio"]').forEach(el => {
        el.disabled = true;
    });
    document.querySelectorAll('.option').forEach(div => {
        div.removeAttribute('onclick');
    });
}