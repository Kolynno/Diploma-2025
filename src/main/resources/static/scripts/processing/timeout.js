let startTime = 0;
const displayTime = 500; // Время показа числа в мс
const intervalTime = 500; // Интервал между числами в мс
let hasReacted = false; // Флаг для отслеживания реакции

const numbersElement = document.querySelector('.content p');

// Функция для отправки данных на сервер
function sendData(reactionTime) {
    fetch('/processingTestOneStageData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            reactionTime: reactionTime
        })
    }).catch(err => console.error('Ошибка отправки данных:', err));
}

// Функция для показа следующего числа
function showNextNumber() {
    startTime = performance.now();

    setTimeout(() => {
        numbersElement.textContent = "";

        // Если пользователь ничего не нажал, отправляем реакцию 1 (нет реакции)
        setTimeout(() => {
            if (!hasReacted) {
                sendData(1);
            }
            window.location.href = `/t/p/1?s=1`;
        }, intervalTime);
    }, displayTime);
}

// Обработчик нажатия клавиши
window.addEventListener("keydown", function(event) {
    if (event.code === "Space" && !hasReacted) { // Проверяем, что нажата клавиша "Пробел" и не было реакции
        hasReacted = true; // Устанавливаем флаг реакции
        const reactionTime = performance.now() - startTime;
        sendData(reactionTime);
    }
});


// Старт теста
showNextNumber();
