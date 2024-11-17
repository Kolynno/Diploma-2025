// Обработка выбора квадрата состояния
document.querySelectorAll('.square').forEach(square => {
    square.addEventListener('click', function() {
        const row = this.getAttribute('data-row');

        // Удаляем выделение с других квадратов в той же строке
        document.querySelectorAll(`[data-row="${row}"]`).forEach(sq => {
            sq.classList.remove('selected');
        });

        // Выделяем выбранный квадрат
        this.classList.add('selected');
    });
});


// Обработка отправки данных
document.getElementById('submitBtn').addEventListener('click', function() {
    const selectedStates = {
        joy: document.querySelector('#row-joy .selected')?.getAttribute('data-value'),
        calm: document.querySelector('#row-calm .selected')?.getAttribute('data-value'),
        dominance: document.querySelector('#row-dominance .selected')?.getAttribute('data-value')
    };

    // Проверяем, что все состояния выбраны
    if (!selectedStates.joy || !selectedStates.calm || !selectedStates.dominance) {
        alert('Пожалуйста, выберите одно состояние в каждой строке.');
        return;
    }

    // Отправляем данные на сервер
    fetch('/stateAnalyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedStates)
    })
        .then(response => {
            if (response.ok) {
                // Перенаправление на страницу после успешной отправки данных
                window.location.href = '/t'; // путь страницы, которую вернет сервер
            }
        })
        .then(data => {
            console.log('Success:', data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

// Обработка перехода назад
document.getElementById('backBtn').addEventListener('click', function() {
    window.location.href = '/r';
});
