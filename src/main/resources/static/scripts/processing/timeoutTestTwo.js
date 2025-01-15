let startTime = 0;
const displayTime = 2000;
const intervalTime = displayTime;
let hasReacted = false;

const numbersElement = document.querySelector('.content p');

function sendData(reactionTime) {
    fetch('/processingTestTwoStageData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            reactionTime: reactionTime
        })
    }).catch(err => console.error('Ошибка отправки данных:', err));
}

function showNextNumber() {
    startTime = performance.now();

    setTimeout(() => {
        numbersElement.textContent = "";

        // Если пользователь ничего не нажал, отправляем реакцию 1 (нет реакции)
        setTimeout(() => {
            if (!hasReacted) {
                sendData(2000);
            }
            window.location.href = `/t/p/2?s=1`;
        }, intervalTime);
    }, displayTime);
}

window.addEventListener("keydown", function(event) {
    if (event.code === "Space" && !hasReacted) {
        hasReacted = true;
        const reactionTime = performance.now() - startTime;
        sendData(reactionTime);
    }
});

showNextNumber();