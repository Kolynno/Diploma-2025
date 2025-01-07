// Время загрузки страницы
const pageLoadTime = performance.now();

// Функция для отправки данных через POST
function sendData(elapsedTime) {
    fetch('/memoryTestOneStageData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            reactionTime: elapsedTime
        })
    }).catch(err => console.error('Ошибка отправки данных:', err));
}

// Функция для обработки перенаправления
function redirectToPage() {
    const currentTime = performance.now();
    const elapsedTime = ((currentTime - pageLoadTime) / 1000).toFixed(1); // Время в секундах с точностью до 1 знака

    sendData(elapsedTime);

    setTimeout(() => {
        window.location.href = '/t/m/1?s=1';
    }, 100);
}

setTimeout(() => redirectToPage(), 3100);

window.addEventListener('keydown', function (event) {
    if (event.code === 'Space') {
        event.preventDefault();
        redirectToPage();
    }
});
