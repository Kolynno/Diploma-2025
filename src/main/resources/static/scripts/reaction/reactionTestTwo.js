document.addEventListener("DOMContentLoaded", () => {
    const container = document.querySelector(".container");
    const movingDot = document.querySelector(".moving-dot");
    const marker = document.querySelector(".marker");
    const interval = parseInt(container.getAttribute("data-interval"), 10);
    const totalPresses = 5;
    let angle = 135;
    let pressCount = 0;
    let reactionData = [];
    let lastPressTime = null;
    let animationInterval;

    function setMarkerPosition() {
        const markerAngle = Math.random() * 180 + 270;
        const markerX = 100 + 95 * Math.cos(markerAngle * Math.PI / 180);
        const markerY = 100 + 95 * Math.sin(markerAngle * Math.PI / 180);
        marker.style.left = `${markerX}px`;
        marker.style.top = `${markerY}px`;
    }

    function updateDotPosition() {
        angle = (angle + 6) % 360; // Двигаем точку по кругу
        const x = 100 + 95 * Math.cos(angle * Math.PI / 180);
        const y = 100 + 95 * Math.sin(angle * Math.PI / 180);
        movingDot.style.left = `${x}px`;
        movingDot.style.top = `${y}px`;
    }

    function checkAlignment() {
        const distance = Math.hypot(movingDot.offsetLeft - marker.offsetLeft, movingDot.offsetTop - marker.offsetTop);
        return distance < 10; // Проверяем совпадение точек
    }

    function handleKeyPress(event) {
        if (event.code === "Space" && pressCount < totalPresses) {
            const now = performance.now();
            const isAligned = checkAlignment();
            let reactionTime = lastPressTime !== null ? now - lastPressTime : 0;

            reactionData.push(reactionTime);

            pressCount++;
            lastPressTime = now;

            setMarkerPosition(); // Меняем положение красной точки
            angle = 135; // Сбрасываем синюю точку до 135 градусов
            updateDotPosition();

            if (pressCount >= totalPresses) {
                clearInterval(animationInterval); // Останавливаем движение точки
                sendReactionData();
            }
        }
    }

    function sendReactionData() {
        fetch("/reactionTestTwoStageData", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(reactionData.filter(rt => rt !== 0))
        }).then(response => response.json())
            .then(data => {
                console.log("Server response:", data);
                document.querySelector("#nextBtn").style.display = "inline-block";
                document.querySelector("#backBtn").style.display = "inline-block";
            })
            .catch(error => console.error("Error:", error));
    }

    setMarkerPosition();
    animationInterval = setInterval(updateDotPosition, interval / 60);
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