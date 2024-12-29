// Время загрузки страницы
let pageLoadTime;
let startTime;
let reactionTimeout;
let isRed = true; // Флаг состояния квадрата

// Функция для установки случайного времени (от 1 до 10 секунд)
function getRandomDelay() {
    return Math.random() * (10 - 1) + 1; // Случайное число от 1 до 10
}

// Функция для обновления интерфейса
function showTimer() {
    const content = document.querySelector(".content");
    content.innerHTML = "<h1>Таймер запущен!</h1>";
    isRed = false;
    startTime = performance.now(); // Время старта таймера
}

// Функция для сброса интерфейса к красному квадрату
function resetSquare() {
    const content = document.querySelector(".content");
    content.innerHTML = '<img src="/img/reactionRed.png">';
    isRed = true;
    startReactionTest(); // Перезапуск
}

// Функция для обработки реакции
function handleReaction() {
    if (!startTime) return; // Если таймер ещё не запущен, ничего не делаем

    const reactionTime = ((performance.now() - startTime) / 1000).toFixed(3); // Время реакции
    window.location.href = `/t/r/1?s=1&t=${reactionTime}`; // Передача времени реакции

    // Сброс интерфейса после задержки
    setTimeout(resetSquare, 1000);
}

// Функция для старта теста реакции
function startReactionTest() {
    const delay = getRandomDelay() * 1000; // Задержка в миллисекундах

    // Сбрасываем предыдущее состояние
    clearTimeout(reactionTimeout);
    startTime = null;

    // Устанавливаем новый таймер
    reactionTimeout = setTimeout(() => {
        showTimer();
    }, delay);

    pageLoadTime = performance.now(); // Запоминаем время загрузки
}

// Обработчик события для клавиши пробел
window.addEventListener("keydown", function(event) {
    if (event.code === "Space") {
        event.preventDefault(); // Предотвращаем скроллинг страницы
        handleReaction();
    }
});

// Таймер для автоматического завершения через 20 секунд
setTimeout(() => {
    if (isRed) {
        window.location.href = `/t/r/1?s=2`;
    } else {
        // Если квадрат не красный, ждем завершения реакции и затем перенаправляем
        const interval = setInterval(() => {
            if (isRed) {
                clearInterval(interval);
                window.location.href = `/t/r/1?s=2`;
            }
        }, 100);
    }
}, 20000);

// Запуск теста при загрузке страницы
startReactionTest();
