// Функция для перенаправления на указанную страницу
function redirectToPage(isTimeout) {
    if (isTimeout) {
        window.location.href = "/t/m/1?s=1&t=1";
    } else {
        window.location.href = "/t/m/1?s=1&t=0";
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
})