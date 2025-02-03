let timer = 0;
let intervalId = null;

if (document.getElementById('timerBtn') != null) {
    document.getElementById('timerBtn').addEventListener('click', function () {
        const timerButton = this;

        if (intervalId === null) {
            intervalId = setInterval(() => {
                timer += 0.1;
            }, 100);
            timerButton.textContent = `Запущено!`;
            document.getElementById('numTable').style.fontSize = "64px"
        } else {
            clearInterval(intervalId);
            intervalId = null;
            timerButton.textContent = `${Math.round(timer * 100) / 100} сек.`;
        }
    });
}

if (document.getElementById('nextBtn') != null) {
    document.getElementById('nextBtn').addEventListener('click', async function () {
        const url = this.getAttribute('data-url');
        const currentStage = parseInt(url.charAt(url.length - 1)) - 1;
        const errorsInput = document.getElementById("errorInput");
        const errors = errorsInput ? parseInt(errorsInput.value) || 0 : 0;

        const stageData = {
            time: Math.round(timer * 100) / 100,
            errors: errors,
            stage: currentStage
        };

        try {
            await fetch('/attentionTestTwoStageData', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(stageData)
            });
        } catch (error) {
            console.error('Ошибка отправки данных:', error);
        }

        window.location.href = url;
    });
}

document.getElementById('backBtn').addEventListener('click', function() {
    const url = this.getAttribute('data-url');
    window.location.href = url;
});
