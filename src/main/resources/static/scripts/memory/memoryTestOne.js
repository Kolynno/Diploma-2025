let timer = 0;

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
            await fetch('/attentionTestOneStageData', {
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