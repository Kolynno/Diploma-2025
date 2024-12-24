// Время загрузки страницы
const pageLoadTime = performance.now();

// Функция для перенаправления на указанную страницу
function redirectToPage(isTimeout) {
    const currentTime = performance.now();
    const elapsedTime = ((currentTime - pageLoadTime) / 1000).toFixed(1); // Время в секундах с точностью до 1 знака

    if (isTimeout) {
        window.location.href = `/t/m/1?s=1&t=${elapsedTime}`;
    } else {
        window.location.href = `/t/m/1?s=1&t=${elapsedTime}`;
    }
}

// Таймер для автоматического перенаправления через 3,1 секунды
setTimeout(() => redirectToPage(true), 3100);

// Обработчик события для клавиши пробел
window.addEventListener("keydown", function(event) {
    if (event.code === "Space") { // Проверяем, что нажата клавиша "Пробел"
        event.preventDefault(); // Предотвращаем скроллинг страницы
        redirectToPage(false);
    }
});
