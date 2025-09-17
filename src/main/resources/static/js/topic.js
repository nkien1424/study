
    // Global variables
    let currentTopic = null;
    let vocabularies = [];
    let practiceVocabularies = [];
    let currentCardIndex = 0;
    let showEnglishFirst = true;
    let isCardFlipped = false;

    // Matching game variables
    let matchingVocabularies = [];
    let selectedEnglish = null;
    let selectedMeaning = null;
    let gameScore = 0;
    let correctMatches = 0;
    let incorrectMatches = 0;
    let gameStartTime = null;
    let gameTimer = null;

    // Sample vocabulary data
        const sampleVocabularies = {
        1: [
    { id: 1, englishWord: "schedule", vietnameseMeaning: "lịch trình, thời gian biểu", pronunciation: "/ˈʃedʒuːl/" },
    { id: 2, englishWord: "meeting", vietnameseMeaning: "cuộc họp", pronunciation: "/ˈmiːtɪŋ/" },
    { id: 3, englishWord: "deadline", vietnameseMeaning: "hạn chót", pronunciation: "/ˈdedlaɪn/" },
    { id: 4, englishWord: "proposal", vietnameseMeaning: "đề xuất, kiến nghị", pronunciation: "/prəˈpoʊzəl/" },
    { id: 5, englishWord: "conference", vietnameseMeaning: "hội nghị", pronunciation: "/ˈkɑːnfərəns/" },
    { id: 6, englishWord: "department", vietnameseMeaning: "phòng ban", pronunciation: "/dɪˈpɑːrtmənt/" },
    { id: 7, englishWord: "employee", vietnameseMeaning: "nhân viên", pronunciation: "/ɪmˈplɔɪiː/" },
    { id: 8, englishWord: "manager", vietnameseMeaning: "quản lý", pronunciation: "/ˈmænɪdʒər/" }
        ],
        2: [
    { id: 9, englishWord: "flight", vietnameseMeaning: "chuyến bay", pronunciation: "/flaɪt/" },
    { id: 10, englishWord: "hotel", vietnameseMeaning: "khách sạn", pronunciation: "/hoʊˈtel/" },
    { id: 11, englishWord: "reservation", vietnameseMeaning: "đặt chỗ trước", pronunciation: "/ˌrezərˈveɪʃən/" },
    { id: 12, englishWord: "destination", vietnameseMeaning: "điểm đến", pronunciation: "/ˌdestɪˈneɪʃən/" },
    { id: 13, englishWord: "luggage", vietnameseMeaning: "hành lý", pronunciation: "/ˈlʌɡɪdʒ/" },
    { id: 14, englishWord: "passenger", vietnameseMeaning: "hành khách", pronunciation: "/ˈpæsɪndʒər/" }
        ],
        3: [
    { id: 15, englishWord: "prescription", vietnameseMeaning: "đơn thuốc", pronunciation: "/prɪˈskrɪpʃən/" },
    { id: 16, englishWord: "symptom", vietnameseMeaning: "triệu chứng", pronunciation: "/ˈsɪmptəm/" },
    { id: 17, englishWord: "treatment", vietnameseMeaning: "điều trị", pronunciation: "/ˈtriːtmənt/" },
    { id: 18, englishWord: "appointment", vietnameseMeaning: "cuộc hẹn", pronunciation: "/əˈpɔɪntmənt/" },
    { id: 19, englishWord: "medicine", vietnameseMeaning: "thuốc", pronunciation: "/ˈmedɪsən/" },
    { id: 20, englishWord: "hospital", vietnameseMeaning: "bệnh viện", pronunciation: "/ˈhɑːspɪtəl/" }
        ]
    };

    // Topic management functions
    function selectTopic(topicId, topicName) {
    currentTopic = { id: topicId, name: topicName };
    vocabularies = sampleVocabularies[topicId] || [];

    // Switch to vocabulary tab
    document.getElementById('vocabulary-tab').click();

    // Update UI
    document.getElementById('selectedTopicInfo').innerHTML =
    `<i class="fas fa-folder-open text-primary"></i> <strong>${topicName}</strong> (${vocabularies.length} từ)`;
    document.getElementById('vocabularyContent').style.display = 'block';
    document.getElementById('noTopicSelected').style.display = 'none';

    // Load vocabulary list
    loadVocabularyList();
}

    function startPractice(topicId) {
    currentTopic = { id: topicId };
    practiceVocabularies = sampleVocabularies[topicId] || [];

    if (practiceVocabularies.length === 0) {
    alert('Chủ đề này chưa có từ vựng nào!');
    return;
}

    // Switch to practice tab
    document.getElementById('practice-tab').click();

    // Update UI
    const topicNames = { 1: 'Business & Work', 2: 'Travel & Transportation', 3: 'Health & Medicine' };
    document.getElementById('practiceTopicName').textContent = topicNames[topicId];
    document.getElementById('practiceContent').style.display = 'block';
    document.getElementById('noPracticeSelected').style.display = 'none';

    // Reset practice state
    currentCardIndex = 0;
    showEnglishFirst = true;
    isCardFlipped = false;

    updatePracticeUI();
}

    function startMatching(topicId) {
    currentTopic = { id: topicId };
    matchingVocabularies = sampleVocabularies[topicId] || [];

    if (matchingVocabularies.length === 0) {
    alert('Chủ đề này chưa có từ vựng nào!');
    return;
}

    // Switch to matching tab
    document.getElementById('matching-tab').click();

    // Update UI
    document.getElementById('matchingContent').style.display = 'block';
    document.getElementById('noMatchingSelected').style.display = 'none';

    // Reset game state
    resetMatchingGame();
}

    // Vocabulary management functions
    function loadVocabularyList() {
    const container = document.getElementById('vocabularyList');
    container.innerHTML = '';

    if (vocabularies.length === 0) {
    container.innerHTML = `
                    <div class="text-center py-4">
                        <i class="fas fa-book-open fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">Chưa có từ vựng nào</h5>
                        <p class="text-muted">Thêm từ vựng mới bằng form ở trên</p>
                    </div>
                `;
    return;
}

    vocabularies.forEach(vocab => {
    const vocabElement = document.createElement('div');
    vocabElement.className = 'vocabulary-item';
    vocabElement.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="flex-grow-1">
                            <div class="vocab-word">${vocab.englishWord}
                                ${vocab.pronunciation ? `<span class="text-muted ms-2">${vocab.pronunciation}</span>` : ''}
                            </div>
                            <div class="vocab-meaning">${vocab.vietnameseMeaning}</div>
                            ${vocab.exampleSentence ? `<div class="text-muted mt-2"><em>"${vocab.exampleSentence}"</em></div>` : ''}
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-sm btn-outline-primary" onclick="editVocabulary(${vocab.id})">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteVocabulary(${vocab.id})">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                `;
    container.appendChild(vocabElement);
});
}

    // Autocomplete functionality
    document.getElementById('englishWordInput').addEventListener('input', function() {
    const keyword = this.value.trim();
    const suggestionsDiv = document.getElementById('suggestions');

    if (keyword.length >= 2) {
    // Simple autocomplete simulation
    const matches = vocabularies.filter(vocab =>
    vocab.englishWord.toLowerCase().includes(keyword.toLowerCase())
    );

    if (matches.length > 0) {
    suggestionsDiv.innerHTML = '';
    matches.slice(0, 5).forEach(vocab => {
    const suggestion = document.createElement('div');
    suggestion.className = 'suggestion-item';
    suggestion.innerHTML = `
                            <strong>${vocab.englishWord}</strong> - ${vocab.vietnameseMeaning}
                        `;
    suggestion.onclick = function() {
    document.getElementById('englishWordInput').value = vocab.englishWord;
    document.getElementById('vietnameseMeaning').value = vocab.vietnameseMeaning;
    document.getElementById('pronunciationInput').value = vocab.pronunciation || '';
    suggestionsDiv.style.display = 'none';
};
    suggestionsDiv.appendChild(suggestion);
});
    suggestionsDiv.style.display = 'block';
} else {
    suggestionsDiv.style.display = 'none';
}
} else {
    suggestionsDiv.style.display = 'none';
}
});

    // Hide suggestions when clicking outside
    document.addEventListener('click', function(e) {
    const suggestionsDiv = document.getElementById('suggestions');
    const inputField = document.getElementById('englishWordInput');
    if (!suggestionsDiv.contains(e.target) && e.target !== inputField) {
    suggestionsDiv.style.display = 'none';
}
});

    // Add vocabulary form submission
    document.getElementById('addVocabForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const englishWord = document.getElementById('englishWordInput').value.trim();
    const vietnameseMeaning = document.getElementById('vietnameseMeaning').value.trim();
    const pronunciation = document.getElementById('pronunciationInput').value.trim();
    const exampleSentence = document.getElementById('exampleSentence').value.trim();

    if (!englishWord || !vietnameseMeaning) {
    alert('Vui lòng nhập từ tiếng Anh và nghĩa tiếng Việt!');
    return;
}

    // Check if word already exists
    const existing = vocabularies.find(v => v.englishWord.toLowerCase() === englishWord.toLowerCase());
    if (existing) {
    alert('Từ này đã tồn tại trong danh sách!');
    return;
}

    // Add new vocabulary
    const newVocab = {
    id: Date.now(),
    englishWord,
    vietnameseMeaning,
    pronunciation,
    exampleSentence
};

    vocabularies.push(newVocab);

    // Update sample data
    if (sampleVocabularies[currentTopic.id]) {
    sampleVocabularies[currentTopic.id].push(newVocab);
}

    // Reset form
    this.reset();
    document.getElementById('suggestions').style.display = 'none';

    // Reload vocabulary list
    loadVocabularyList();

    // Update topic count
    updateTopicCount(currentTopic.id);

    // Show success message
    showSuccessMessage('Đã thêm từ vựng thành công!');
});

    // Search functionality
    document.getElementById('searchInput').addEventListener('input', function() {
    const keyword = this.value.trim().toLowerCase();
    const vocabItems = document.querySelectorAll('.vocabulary-item');

    vocabItems.forEach(item => {
    const text = item.textContent.toLowerCase();
    if (keyword === '' || text.includes(keyword)) {
    item.style.display = 'block';
} else {
    item.style.display = 'none';
}
});
});

    // Practice functions
    function startPracticeSession() {
    if (practiceVocabularies.length === 0) {
    alert('Không có từ vựng để luyện tập!');
    return;
}

    // Shuffle vocabularies
    practiceVocabularies = shuffleArray([...practiceVocabularies]);
    currentCardIndex = 0;
    isCardFlipped = false;

    // Update UI
    document.getElementById('startBtn').style.display = 'none';
    document.getElementById('flipBtn').style.display = 'inline-block';
    document.getElementById('nextBtn').style.display = 'inline-block';
    document.getElementById('modeBtn').style.display = 'inline-block';
    document.getElementById('restartBtn').style.display = 'inline-block';

    showCurrentCard();
    updateProgress();
}

    function showCurrentCard() {
    if (currentCardIndex >= practiceVocabularies.length) {
    // Practice completed
    document.getElementById('frontText').textContent = 'Hoàn thành!';
    document.getElementById('backText').textContent = 'Bạn đã học hết tất cả từ vựng';
    return;
}

    const vocab = practiceVocabularies[currentCardIndex];
    const frontText = document.getElementById('frontText');
    const backText = document.getElementById('backText');

    if (showEnglishFirst) {
    frontText.textContent = vocab.englishWord;
    backText.textContent = vocab.vietnameseMeaning;
} else {
    frontText.textContent = vocab.vietnameseMeaning;
    backText.textContent = vocab.englishWord;
}

    // Reset card flip state
    document.getElementById('flashcard').classList.remove('flipped');
    isCardFlipped = false;
}

    function flipCard() {
    document.getElementById('flashcard').classList.toggle('flipped');
    isCardFlipped = !isCardFlipped;
}

    function nextCard() {
    if (currentCardIndex < practiceVocabularies.length - 1) {
    currentCardIndex++;
    showCurrentCard();
    updateProgress();
} else {
    // Practice completed
    showCompletionMessage();
}
}

    function toggleMode() {
    showEnglishFirst = !showEnglishFirst;
    document.getElementById('practiceMode').textContent =
    showEnglishFirst ? 'Anh → Việt' : 'Việt → Anh';
    showCurrentCard();
}

    function restartPractice() {
    currentCardIndex = 0;
    isCardFlipped = false;
    practiceVocabularies = shuffleArray([...practiceVocabularies]);
    showCurrentCard();
    updateProgress();
}

    function updateProgress() {
    const progress = ((currentCardIndex + 1) / practiceVocabularies.length) * 100;
    const progressBar = document.getElementById('progressBar');
    progressBar.style.width = progress + '%';
    progressBar.textContent = Math.round(progress) + '%';

    document.getElementById('practiceProgress').textContent =
    `${currentCardIndex + 1}/${practiceVocabularies.length}`;
}

    function updatePracticeUI() {
    document.getElementById('startBtn').style.display = 'inline-block';
    document.getElementById('flipBtn').style.display = 'none';
    document.getElementById('nextBtn').style.display = 'none';
    document.getElementById('modeBtn').style.display = 'none';
    document.getElementById('restartBtn').style.display = 'none';

    document.getElementById('frontText').textContent = 'Nhấn "Bắt đầu" để luyện tập';
    document.getElementById('backText').textContent = 'Nghĩa tiếng Việt';
    document.getElementById('practiceProgress').textContent = '0/0';
    document.getElementById('progressBar').style.width = '0%';
    document.getElementById('progressBar').textContent = '0%';
    document.getElementById('practiceMode').textContent = 'Anh → Việt';
}

    function showCompletionMessage() {
    document.getElementById('frontText').innerHTML = `
                <div>
                    <i class="fas fa-trophy fa-3x text-warning mb-3"></i>
                    <div>Chúc mừng!</div>
                    <div style="font-size: 0.7em; margin-top: 10px;">
                        Bạn đã hoàn thành ${practiceVocabularies.length} từ vựng
                    </div>
                </div>
            `;
    document.getElementById('backText').innerHTML = `
                <div>
                    <i class="fas fa-star fa-3x text-success mb-3"></i>
                    <div>Xuất sắc!</div>
                    <div style="font-size: 0.7em; margin-top: 10px;">
                        Hãy tiếp tục luyện tập để ghi nhớ tốt hơn
                    </div>
                </div>
            `;
}

    // Matching game functions
    function resetMatchingGame() {
    selectedEnglish = null;
    selectedMeaning = null;
    gameScore = 0;
    correctMatches = 0;
    incorrectMatches = 0;
    gameStartTime = null;

    if (gameTimer) {
    clearInterval(gameTimer);
    gameTimer = null;
}

    updateGameScore();
    document.getElementById('gameTime').textContent = '00:00';
    document.getElementById('startGameBtn').style.display = 'inline-block';
    document.getElementById('newGameBtn').style.display = 'none';
    document.getElementById('matchingContainer').style.display = 'none';
}

    function startMatchingGame() {
    if (matchingVocabularies.length < 4) {
    alert('Cần ít nhất 4 từ vựng để chơi trò chơi này!');
    return;
}

    // Reset game state FIRST
    selectedEnglish = null;
    selectedMeaning = null;
    gameScore = 0;
    correctMatches = 0;
    incorrectMatches = 0;

    if (gameTimer) {
    clearInterval(gameTimer);
    gameTimer = null;
}

    // Select random 6 words (or all if less than 6)
    const gameWords = shuffleArray([...matchingVocabularies]).slice(0, Math.min(6, matchingVocabularies.length));

    // Create game board
    createGameBoard(gameWords);

    // Start timer
    gameStartTime = new Date();
    startGameTimer();

    // Update UI
    document.getElementById('startGameBtn').style.display = 'none';
    document.getElementById('newGameBtn').style.display = 'inline-block';
    document.getElementById('matchingContainer').style.display = 'grid';

    // Update score display
    updateGameScore();
}

    function createGameBoard(words) {
    const container = document.getElementById('matchingContainer');
    container.innerHTML = '';

    // Create array of all cards (both English and Vietnamese)
    const allCards = [];

    // Add English cards
    words.forEach(word => {
    allCards.push({
    id: word.id,
    text: word.englishWord,
    type: 'english',
    matchId: word.id
});
});

    // Add Vietnamese cards
    words.forEach(word => {
    allCards.push({
    id: word.id + '_vn',
    text: word.vietnameseMeaning,
    type: 'vietnamese',
    matchId: word.id
});
});

    // Shuffle all cards
    const shuffledCards = shuffleArray(allCards);

    // Create card elements
    shuffledCards.forEach(card => {
    const cardElement = document.createElement('div');
    cardElement.className = `game-card ${card.type}-card`;
    cardElement.textContent = card.text;
    cardElement.dataset.matchId = card.matchId;
    cardElement.dataset.type = card.type;
    cardElement.dataset.cardId = card.id;
    cardElement.onclick = () => selectCard(cardElement);
    container.appendChild(cardElement);
});
}

    function selectCard(cardElement) {
    // Don't allow selection of already matched cards
    if (cardElement.classList.contains('matched') || cardElement.classList.contains('hidden')) {
    return;
}

    // If this card is already selected, deselect it
    if (cardElement.classList.contains('selected')) {
    cardElement.classList.remove('selected');
    if (selectedEnglish === cardElement) selectedEnglish = null;
    if (selectedMeaning === cardElement) selectedMeaning = null;
    return;
}

    const cardType = cardElement.dataset.type;

    if (cardType === 'english') {
    // Deselect previous English card
    if (selectedEnglish) {
    selectedEnglish.classList.remove('selected');
}
    selectedEnglish = cardElement;
    cardElement.classList.add('selected');
} else {
    // Deselect previous Vietnamese card
    if (selectedMeaning) {
    selectedMeaning.classList.remove('selected');
}
    selectedMeaning = cardElement;
    cardElement.classList.add('selected');
}

    // Check for match if both cards are selected
    if (selectedEnglish && selectedMeaning) {
    checkForMatch();
}
}

    function checkForMatch() {
    if (!selectedEnglish || !selectedMeaning) return;

    const englishMatchId = selectedEnglish.dataset.matchId;
    const meaningMatchId = selectedMeaning.dataset.matchId;

    setTimeout(() => {
    if (englishMatchId === meaningMatchId) {
    // Correct match - make cards disappear
    selectedEnglish.classList.remove('selected');
    selectedEnglish.classList.add('matched');
    selectedMeaning.classList.remove('selected');
    selectedMeaning.classList.add('matched');

    // Remove click handlers
    selectedEnglish.onclick = null;
    selectedMeaning.onclick = null;

    // Hide cards after animation
    setTimeout(() => {
    selectedEnglish.classList.add('hidden');
    selectedMeaning.classList.add('hidden');
}, 600);

    correctMatches++;
    gameScore += 10;

    // Create celebration effect
    createCelebrationEffect();

    // Check if game is complete
    const totalPairs = document.querySelectorAll('.game-card[data-type="english"]').length;
    if (correctMatches === totalPairs) {
    setTimeout(() => {
    completeMatchingGame();
}, 1000);
}
} else {
    // Incorrect match
    selectedEnglish.classList.remove('selected');
    selectedEnglish.classList.add('wrong-match');
    selectedMeaning.classList.remove('selected');
    selectedMeaning.classList.add('wrong-match');

    setTimeout(() => {
    selectedEnglish.classList.remove('wrong-match');
    selectedMeaning.classList.remove('wrong-match');
}, 600);

    incorrectMatches++;
    gameScore = Math.max(0, gameScore - 2);
}

    selectedEnglish = null;
    selectedMeaning = null;
    updateGameScore();
}, 200);
}

    function createCelebrationEffect() {
    // Create confetti effect
    for (let i = 0; i < 20; i++) {
    setTimeout(() => {
    const confetti = document.createElement('div');
    confetti.className = 'confetti';
    confetti.style.left = Math.random() * 100 + '%';
    confetti.style.backgroundColor = ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#27ae60', '#2ecc71'][Math.floor(Math.random() * 6)];
    confetti.style.animationDelay = Math.random() * 0.5 + 's';

    document.body.appendChild(confetti);

    setTimeout(() => {
    confetti.remove();
}, 3000);
}, i * 50);
}
}

    function completeMatchingGame() {
    if (gameTimer) {
    clearInterval(gameTimer);
    gameTimer = null;
}

    // Show completion modal
    document.getElementById('finalScore').textContent = gameScore;
    document.getElementById('finalCorrect').textContent = correctMatches;
    document.getElementById('finalTime').textContent = document.getElementById('gameTime').textContent;

    const modal = new bootstrap.Modal(document.getElementById('gameCompleteModal'));
    modal.show();
}

    function newMatchingGame() {
    resetMatchingGame();
    startMatchingGame();
}

    function startGameTimer() {
    gameTimer = setInterval(() => {
        const now = new Date();
        const elapsed = Math.floor((now - gameStartTime) / 1000);
        const minutes = Math.floor(elapsed / 60);
        const seconds = elapsed % 60;
        document.getElementById('gameTime').textContent =
            `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    }, 1000);
}

    function updateGameScore() {
    document.getElementById('gameScore').textContent = gameScore;
    document.getElementById('correctMatches').textContent = correctMatches;
    document.getElementById('incorrectMatches').textContent = incorrectMatches;
}

    // Topic management functions
    function addNewTopic() {
    const name = document.getElementById('topicName').value.trim();
    const description = document.getElementById('topicDescription').value.trim();

    if (!name) {
    alert('Vui lòng nhập tên chủ đề!');
    return;
}

    // Create new topic element
    const newTopicId = Date.now();
    const topicCard = document.createElement('div');
    topicCard.className = 'topic-card';
    topicCard.dataset.topicId = newTopicId;
    topicCard.innerHTML = `
                <div class="topic-header">
                    <h4 class="topic-title">${name}</h4>
                    <span class="vocab-count">0 từ</span>
                </div>
                <p class="text-muted mb-3">${description || 'Chủ đề mới'}</p>
                <div class="d-flex gap-2">
                    <button class="btn btn-primary btn-sm" onclick="selectTopic(${newTopicId}, '${name}')">
                        <i class="fas fa-book-open"></i> Quản lý từ
                    </button>
                    <button class="btn btn-success btn-sm" onclick="startPractice(${newTopicId})">
                        <i class="fas fa-play"></i> Luyện tập
                    </button>
                    <button class="btn btn-warning btn-sm" onclick="startMatching(${newTopicId})">
                        <i class="fas fa-puzzle-piece"></i> Ghép thẻ
                    </button>
                </div>
            `;

    document.getElementById('topicsList').appendChild(topicCard);

    // Initialize empty vocabulary array
    sampleVocabularies[newTopicId] = [];

    // Close modal and reset form
    bootstrap.Modal.getInstance(document.getElementById('addTopicModal')).hide();
    document.getElementById('addTopicForm').reset();

    showSuccessMessage('Đã tạo chủ đề mới thành công!');
}

    // Utility functions
    function shuffleArray(array) {
    const shuffled = [...array];
    for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
}
    return shuffled;
}

    function updateTopicCount(topicId) {
    const topicCard = document.querySelector(`[data-topic-id="${topicId}"]`);
    if (topicCard) {
    const countElement = topicCard.querySelector('.vocab-count');
    const count = sampleVocabularies[topicId] ? sampleVocabularies[topicId].length : 0;
    countElement.textContent = `${count} từ`;
}
}

    function showSuccessMessage(message) {
    // Create and show temporary success message
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success-custom alert-custom';
    alertDiv.innerHTML = `
                <i class="fas fa-check-circle me-2"></i>${message}
            `;

    // Insert at top of main container
    const mainContainer = document.querySelector('.main-container');
    mainContainer.insertBefore(alertDiv, mainContainer.firstChild);

    // Auto remove after 3 seconds
    setTimeout(() => {
    alertDiv.remove();
}, 3000);
}

    function showErrorMessage(message) {
    // Create and show temporary error message
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger-custom alert-custom';
    alertDiv.innerHTML = `
                <i class="fas fa-exclamation-triangle me-2"></i>${message}
            `;

    // Insert at top of main container
    const mainContainer = document.querySelector('.main-container');
    mainContainer.insertBefore(alertDiv, mainContainer.firstChild);

    // Auto remove after 3 seconds
    setTimeout(() => {
    alertDiv.remove();
}, 3000);
}

    function editVocabulary(id) {
    const vocab = vocabularies.find(v => v.id === id);
    if (vocab) {
    document.getElementById('englishWordInput').value = vocab.englishWord;
    document.getElementById('vietnameseMeaning').value = vocab.vietnameseMeaning;
    document.getElementById('pronunciationInput').value = vocab.pronunciation || '';
    document.getElementById('exampleSentence').value = vocab.exampleSentence || '';

    // Scroll to form
    document.querySelector('.vocab-form').scrollIntoView({ behavior: 'smooth' });

    // Remove the vocabulary from list (will be re-added when form is submitted)
    deleteVocabulary(id, false);
}
}

    function deleteVocabulary(id, showConfirm = true) {
    if (showConfirm && !confirm('Bạn có chắc chắn muốn xóa từ vựng này?')) {
    return;
}

    // Remove from vocabularies array
    vocabularies = vocabularies.filter(v => v.id !== id);

    // Remove from sample data
    if (sampleVocabularies[currentTopic.id]) {
    sampleVocabularies[currentTopic.id] = sampleVocabularies[currentTopic.id].filter(v => v.id !== id);
}

    // Reload list
    loadVocabularyList();

    // Update topic count
    updateTopicCount(currentTopic.id);

    if (showConfirm) {
    showSuccessMessage('Đã xóa từ vựng thành công!');
}
}

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
    // Only handle shortcuts when practice tab is active
    if (!document.getElementById('practice').classList.contains('show')) return;

    switch(e.key) {
    case ' ': // Spacebar to flip card
    e.preventDefault();
    if (document.getElementById('flipBtn').style.display !== 'none') {
    flipCard();
}
    break;
    case 'ArrowRight': // Right arrow for next card
    e.preventDefault();
    if (document.getElementById('nextBtn').style.display !== 'none') {
    nextCard();
}
    break;
    case 'ArrowLeft': // Left arrow to restart
    e.preventDefault();
    if (document.getElementById('restartBtn').style.display !== 'none') {
    restartPractice();
}
    break;
    case 'Enter': // Enter to start practice
    e.preventDefault();
    if (document.getElementById('startBtn').style.display !== 'none') {
    startPracticeSession();
}
    break;
}
});

    // Initialize tooltips for keyboard shortcuts
    document.addEventListener('DOMContentLoaded', function() {
    // Add keyboard shortcut hints
    const practiceTab = document.getElementById('practice');
    if (practiceTab) {
    const shortcutsInfo = document.createElement('div');
    shortcutsInfo.className = 'text-center mt-3 text-muted';
    shortcutsInfo.innerHTML = `
                    <small>
                        <i class="fas fa-keyboard me-1"></i>
                        Phím tắt: <kbd>Enter</kbd> bắt đầu • <kbd>Space</kbd> lật thẻ • <kbd>→</kbd> tiếp theo • <kbd>←</kbd> làm lại
                    </small>
                `;

    const practiceContent = document.getElementById('practiceContent');
    practiceContent.appendChild(shortcutsInfo);
}
});

    // Advanced features

    // Speech synthesis for pronunciation
    function speakWord(text, lang = 'en-US') {
    if ('speechSynthesis' in window) {
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = lang;
    utterance.rate = 0.8;
    speechSynthesis.speak(utterance);
}
}

    // Add pronunciation buttons to vocabulary items
    function addPronunciationButtons() {
    document.querySelectorAll('.vocab-word').forEach(wordElement => {
        if (!wordElement.querySelector('.speak-btn')) {
            const speakBtn = document.createElement('button');
            speakBtn.className = 'btn btn-sm btn-outline-info ms-2 speak-btn';
            speakBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
            speakBtn.onclick = (e) => {
                e.stopPropagation();
                const word = wordElement.textContent.split(' ')[0]; // Get first word only
                speakWord(word);
            };
            wordElement.appendChild(speakBtn);
        }
    });
}

    // Enhanced search with filters
    function initializeAdvancedSearch() {
    const searchContainer = document.querySelector('#searchInput').parentElement;

    // Add filter dropdown
    const filterDropdown = document.createElement('div');
    filterDropdown.className = 'dropdown ms-2';
    filterDropdown.innerHTML = `
                <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                    <i class="fas fa-filter"></i> Lọc
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#" onclick="filterVocabulary('all')">Tất cả</a></li>
                    <li><a class="dropdown-item" href="#" onclick="filterVocabulary('with-pronunciation')">Có phiên âm</a></li>
                    <li><a class="dropdown-item" href="#" onclick="filterVocabulary('with-example')">Có ví dụ</a></li>
                    <li><a class="dropdown-item" href="#" onclick="filterVocabulary('no-pronunciation')">Chưa có phiên âm</a></li>
                </ul>
            `;

    searchContainer.parentElement.appendChild(filterDropdown);
}

    function filterVocabulary(filterType) {
    const vocabItems = document.querySelectorAll('.vocabulary-item');

    vocabItems.forEach(item => {
    const hasPhonetic = item.textContent.includes('/');
    const hasExample = item.querySelector('em') !== null;

    let show = true;

    switch(filterType) {
    case 'with-pronunciation':
    show = hasPhonetic;
    break;
    case 'with-example':
    show = hasExample;
    break;
    case 'no-pronunciation':
    show = !hasPhonetic;
    break;
    case 'all':
    default:
    show = true;
    break;
}

    item.style.display = show ? 'block' : 'none';
});
}

    // Performance tracking
    let studyStats = {
    totalStudyTime: 0,
    cardsReviewed: 0,
    correctMatches: 0,
    sessionStartTime: null
};

    function startStudySession() {
    studyStats.sessionStartTime = new Date();
}

    function endStudySession() {
    if (studyStats.sessionStartTime) {
    const sessionTime = new Date() - studyStats.sessionStartTime;
    studyStats.totalStudyTime += sessionTime;
    studyStats.sessionStartTime = null;

    // Save to localStorage if available (for demo purposes)
    try {
    localStorage.setItem('studyStats', JSON.stringify(studyStats));
} catch(e) {
    console.log('Stats saved in memory only');
}
}
}

    // Load saved stats on page load
    document.addEventListener('DOMContentLoaded', function() {
    try {
    const savedStats = localStorage.getItem('studyStats');
    if (savedStats) {
    studyStats = { ...studyStats, ...JSON.parse(savedStats) };
}
} catch(e) {
    console.log('No saved stats found');
}
});

    // Export/Import functionality
    function exportVocabulary(topicId) {
    const vocabs = sampleVocabularies[topicId] || [];
    const dataStr = JSON.stringify(vocabs, null, 2);
    const dataBlob = new Blob([dataStr], {type: 'application/json'});

    const link = document.createElement('a');
    link.href = URL.createObjectURL(dataBlob);
    link.download = `vocabulary_topic_${topicId}.json`;
    link.click();
}

    function importVocabulary() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = function(e) {
    const file = e.target.files[0];
    if (file) {
    const reader = new FileReader();
    reader.onload = function(e) {
    try {
    const importedVocabs = JSON.parse(e.target.result);
    if (Array.isArray(importedVocabs)) {
    // Add to current topic
    if (currentTopic && sampleVocabularies[currentTopic.id]) {
    sampleVocabularies[currentTopic.id].push(...importedVocabs);
    vocabularies = sampleVocabularies[currentTopic.id];
    loadVocabularyList();
    updateTopicCount(currentTopic.id);
    showSuccessMessage(`Đã nhập ${importedVocabs.length} từ vựng thành công!`);
} else {
    showErrorMessage('Vui lòng chọn chủ đề trước khi nhập từ vựng!');
}
} else {
    showErrorMessage('File không đúng định dạng!');
}
} catch(error) {
    showErrorMessage('Lỗi khi đọc file: ' + error.message);
}
};
    reader.readAsText(file);
}
};
    input.click();
}

    // Initialize everything when page loads
    document.addEventListener('DOMContentLoaded', function() {
    // Initialize advanced search
    setTimeout(initializeAdvancedSearch, 100);

    // Add export/import buttons to vocabulary management
    setTimeout(() => {
    const vocabHeader = document.querySelector('#vocabulary h3').parentElement;
    if (vocabHeader && !document.getElementById('importExportButtons')) {
    const buttonsDiv = document.createElement('div');
    buttonsDiv.id = 'importExportButtons';
    buttonsDiv.className = 'd-flex gap-2';
    buttonsDiv.innerHTML = `
                        <button class="btn btn-outline-success btn-sm" onclick="importVocabulary()">
                            <i class="fas fa-upload"></i> Nhập
                        </button>
                        <button class="btn btn-outline-primary btn-sm" onclick="exportVocabulary(currentTopic?.id)" id="exportBtn" disabled>
                            <i class="fas fa-download"></i> Xuất
                        </button>
                    `;
    vocabHeader.appendChild(buttonsDiv);
}
}, 500);
});

    // Update export button state when topic is selected
    function updateExportButtonState() {
    const exportBtn = document.getElementById('exportBtn');
    if (exportBtn) {
    exportBtn.disabled = !currentTopic || vocabularies.length === 0;
}
}

    // Override selectTopic to update export button
    const originalSelectTopic = selectTopic;
    selectTopic = function(topicId, topicName) {
    originalSelectTopic(topicId, topicName);
    updateExportButtonState();

    // Add pronunciation buttons to loaded vocabulary
    setTimeout(addPronunciationButtons, 100);
};

    // Add study session tracking to practice
    const originalStartPracticeSession = startPracticeSession;
    startPracticeSession = function() {
    originalStartPracticeSession();
    startStudySession();
};

    const originalNextCard = nextCard;
    nextCard = function() {
    studyStats.cardsReviewed++;
    originalNextCard();
};

    // Add stats tracking to matching game
    const originalCompleteMatchingGame = completeMatchingGame;
    completeMatchingGame = function() {
    studyStats.correctMatches += correctMatches;
    endStudySession();
    originalCompleteMatchingGame();
};

    // Responsive adjustments
    function adjustForMobile() {
    if (window.innerWidth < 768) {
    // Adjust flashcard size for mobile
    const flashcards = document.querySelectorAll('.flashcard-face');
    flashcards.forEach(card => {
    card.style.fontSize = '1.2em';
    card.style.padding = '15px';
});

    // Adjust matching game for mobile
    const matchingGame = document.querySelector('.matching-game');
    if (matchingGame) {
    matchingGame.style.gridTemplateColumns = '1fr';
}
}
}

    // Listen for window resize
    window.addEventListener('resize', adjustForMobile);
    document.addEventListener('DOMContentLoaded', adjustForMobile);

    // Add visual feedback for interactions
    document.addEventListener('click', function(e) {
    if (e.target.classList.contains('btn')) {
    e.target.style.transform = 'scale(0.95)';
    setTimeout(() => {
    e.target.style.transform = '';
}, 150);
}
});

    // Prevent context menu on flashcards for better UX
    document.addEventListener('contextmenu', function(e) {
    if (e.target.closest('.flashcard')) {
    e.preventDefault();
}
});

    // Add swipe gestures for mobile (basic implementation)
    let touchStartX = 0;
    let touchEndX = 0;

    document.addEventListener('touchstart', function(e) {
    touchStartX = e.changedTouches[0].screenX;
});

    document.addEventListener('touchend', function(e) {
    touchEndX = e.changedTouches[0].screenX;
    handleSwipe();
});

    function handleSwipe() {
    const swipeThreshold = 50;
    const diff = touchStartX - touchEndX;

    // Only handle swipes on flashcard
    if (document.querySelector('.flashcard:hover') ||
    document.getElementById('practice').classList.contains('show')) {

    if (Math.abs(diff) > swipeThreshold) {
    if (diff > 0) {
    // Swipe left - next card
    if (document.getElementById('nextBtn').style.display !== 'none') {
    nextCard();
}
} else {
    // Swipe right - flip card
    if (document.getElementById('flipBtn').style.display !== 'none') {
    flipCard();
}
}
}
}
}

    console.log('🎉 TOEIC Flashcard System initialized successfully!');
    console.log('📚 Features: Topic management, Vocabulary CRUD, Flashcard practice, Matching game');
    console.log('⌨️ Keyboard shortcuts available in practice mode');
    console.log('📱 Mobile responsive with touch gestures');
