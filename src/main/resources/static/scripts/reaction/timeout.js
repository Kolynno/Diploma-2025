// Время загрузки страницы
let pageLoadTime;
let startTime;
let reactionTimeout;
let isRed = true; // Флаг состояния квадрата
let delay;

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
async function handleReaction(falseStart) {
    let reactionTime = ((performance.now() - startTime) / 1000); // Время реакции

    if (falseStart) {
        reactionTime = 0;
        delay = 0;
    }

    const stageData = {
        reactionTime: reactionTime.toFixed(3),
        delay: delay.toFixed(3)
    };

    try {
        await fetch('/reactionTestOneStageData', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(stageData)
        });
    } catch (error) {
        console.error('Ошибка отправки данных:', error);
    }

    // Переход на следующую страницу
    window.location.href = `/t/r/1?s=1`;

    // Сброс интерфейса после задержки
    setTimeout(resetSquare, 1000);
}

// Функция для старта теста реакции
function startReactionTest() {
    delay = getRandomDelay() * 1000; // Задержка в миллисекундах

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
        if (!isRed) {
            handleReaction(false);
        } else {
            handleReaction(true);
        }
    }
});

// Запуск теста при загрузке страницы
startReactionTest();
