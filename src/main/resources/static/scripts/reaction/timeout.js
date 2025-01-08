let pageLoadTime;
let startTime;
let reactionTimeout;
let isSquareRed = true;
let delay;

function getRandomDelay() {
    return Math.random() * (10 - 1) + 1;
}

function showTimer() {
    const content = document.querySelector(".content");
    content.innerHTML = "<h1>Таймер запущен!</h1>";
    isSquareRed = false;
    startTime = performance.now();
}

function resetSquare() {
    const content = document.querySelector(".content");
    content.innerHTML = '<img src="/img/reactionRed.png">';
    isSquareRed = true;
    startReactionTest();
}

async function handleReaction(falseStart) {
    let reactionTime = ((performance.now() - startTime) / 1000);

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

    window.location.href = `/t/r/1?s=1`;

    setTimeout(resetSquare, 1000);
}

function startReactionTest() {
    delay = getRandomDelay() * 1000;

    clearTimeout(reactionTimeout);
    startTime = null;

    reactionTimeout = setTimeout(() => {
        showTimer();
    }, delay);

    pageLoadTime = performance.now();
}

window.addEventListener("keydown", function(event) {
    if (event.code === "Space") {
        event.preventDefault();
        if (!isSquareRed) {
            handleReaction(false);
        } else {
            handleReaction(true);
        }
    }
});

startReactionTest();