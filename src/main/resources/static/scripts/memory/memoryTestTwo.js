document.addEventListener('DOMContentLoaded', function () {
    const wordList = document.getElementById('wordList');
    const nextBtn = document.getElementById('nextBtn');
    const backBtn = document.getElementById('backBtn');
    const newWordsInput = document.getElementById('newWordsInput');

    let selectedWords = new Set();

    if (wordList) {
        try {
            const words = JSON.parse(wordList.getAttribute('data-words'));
            words.forEach(word => {
                const li = document.createElement('li');
                li.textContent = word;
                li.classList.add('word-item');
                li.addEventListener('click', function () {
                    if (selectedWords.has(word)) {
                        selectedWords.delete(word);
                        li.classList.remove('selected');
                    } else {
                        selectedWords.add(word);
                        li.classList.add('selected');
                    }
                });
                wordList.appendChild(li);
            });
        } catch (error) {
            console.error('Ошибка парсинга JSON:', error);
        }
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', async function () {
            const url = this.getAttribute('data-url');

            let newWords = [];
            if (newWordsInput) {
                newWords = newWordsInput.value
                    .split(/\s*,\s*/)
                    .map(word => word.trim()) // Убираем лишние пробелы
                    .filter(word => word.length > 0);
            }

            const stageData = {
                mentionWordsSet: Array.from(selectedWords).concat(newWords)
            };

            console.log("Отправляем данные:", stageData); // Проверка перед отправкой

            try {
                const response = await fetch('/memoryTestTwoStageData', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(stageData)
                });

                if (!response.ok) {
                    throw new Error(`Ошибка запроса: ${response.status}`);
                }

                console.log("Ответ сервера:", await response.json()); // Вывод ответа сервера
            } catch (error) {
                console.error('Ошибка отправки данных:', error);
            }

            window.location.href = url;
        });
    }

    if (backBtn) {
        backBtn.addEventListener('click', function () {
            window.location.href = this.getAttribute('data-url');
        });
    }
});
