document.addEventListener("DOMContentLoaded", () => {
    const container = document.querySelector(".container");
    const movingDot = document.querySelector(".moving-dot");
    const marker = document.querySelector(".marker");
    const interval = parseInt(container.getAttribute("data-interval"), 10); // Интервал в мс
    const totalPresses = 5;
    let pressCount = 0;
    let reactionData = [];
    let expectedHitTime = null;
    let animationStartTime = null;
    let animationFrame;


    //TODO надо бы брать абс значенение, как будто сейчас вместо +100мс дает (100 - interval)мс
    function setMarkerPosition() {
        // Выбираем случайное время попадания в диапазоне 1 сек – interval
        let reactionTimeOffset = Math.random() * (interval - 500) + 1000;
        expectedHitTime = performance.now() + reactionTimeOffset;

        // Вычисляем будущий угол синей точки через reactionTimeOffset
        let futureAngle = (135 + (reactionTimeOffset / interval) * 360) % 360;
        let radianFutureAngle = futureAngle * (Math.PI / 180);
        const radius = 100; // Радиус окружности

        // Определяем координаты красной точки (где окажется синяя через reactionTimeOffset)
        const markerX = 100 + radius * Math.cos(radianFutureAngle);
        const markerY = 100 + radius * Math.sin(radianFutureAngle);

        marker.style.left = `${markerX}px`;
        marker.style.top = `${markerY}px`;

        console.log(`Попытка ${pressCount + 1}:`);
        console.log(`  - Красная точка поставлена на угол ${futureAngle.toFixed(2)}°`);
        console.log(`  - Нужно нажать через ${reactionTimeOffset.toFixed(2)} мс`);
        console.log(`  - Идеальное время нажатия: ${expectedHitTime.toFixed(2)} мс`);

        startAnimation();
    }

    function updateDotPosition() {
        let elapsedTime = performance.now() - animationStartTime;
        let progress = elapsedTime / interval;
        let currentAngle = (135 + (progress * 360)) % 360; // Текущий угол движения синей точки

        let radianCurrentAngle = currentAngle * (Math.PI / 180);
        let x = 100 + 100 * Math.cos(radianCurrentAngle);
        let y = 100 + 100 * Math.sin(radianCurrentAngle);

        movingDot.style.left = `${x}px`;
        movingDot.style.top = `${y}px`;

        if (pressCount < totalPresses) {
            animationFrame = requestAnimationFrame(updateDotPosition);
        }
    }

    function startAnimation() {
        if (animationFrame) cancelAnimationFrame(animationFrame);
        animationStartTime = performance.now();
        updateDotPosition();
    }

    function handleKeyPress(event) {
        if (event.code === "Space" && pressCount < totalPresses) {
            const now = performance.now();
            const reactionTime = now - expectedHitTime; // Разница между идеальным и реальным нажатием

            reactionData.push(reactionTime);
            console.log(`Реальное нажатие: ${now.toFixed(2)} мс, Разница: ${reactionTime.toFixed(2)} мс`);

            pressCount++;

            if (pressCount < totalPresses) {
                setMarkerPosition(); // Меняем положение маркера и перезапускаем тест
            } else {
                stopAnimation();
                sendReactionData();
            }
        }
    }

    function stopAnimation() {
        cancelAnimationFrame(animationFrame); // Останавливаем движение синей точки
    }

    function sendReactionData() {
        fetch("/reactionTestTwoStageData", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(reactionData)
        }).then(response => response.text()) // Читаем ответ как текст
            .then(text => {
                if (text) {
                    return JSON.parse(text); // Преобразуем только если ответ не пустой
                } else {
                    console.warn("Пустой ответ от сервера");
                    return {}; // Возвращаем пустой объект, чтобы избежать ошибки
                }
            })
            .then(data => {
                console.log("Server response:", data);
                document.querySelector("#nextBtn").style.display = "inline-block";
                document.querySelector("#backBtn").style.display = "inline-block";
            })
            .catch(error => console.error("Error:", error));
    }


    setMarkerPosition();
    document.addEventListener("keydown", handleKeyPress);
});

document.getElementById('nextBtn').addEventListener('click', function() {
    const url = this.getAttribute('data-url');
    window.location.href = url;
});

document.getElementById('backBtn').addEventListener('click', function() {
    const url = this.getAttribute('data-url');
    window.location.href = url;
});
