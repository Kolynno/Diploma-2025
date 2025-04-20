document.querySelectorAll('.square').forEach(square => {
    square.addEventListener('click', function() {
        const row = this.getAttribute('data-row');

        document.querySelectorAll(`[data-row="${row}"]`).forEach(sq => {
            sq.classList.remove('selected');
        });

        this.classList.add('selected');
    });
});

document.getElementById('submitBtn').addEventListener('click', function() {
    const selectedStates = {
        joy: document.querySelector('#row-joy .selected')?.getAttribute('data-value'),
        calm: document.querySelector('#row-calm .selected')?.getAttribute('data-value'),
        dominance: document.querySelector('#row-dominance .selected')?.getAttribute('data-value')
    };

    if (!selectedStates.joy || !selectedStates.calm || !selectedStates.dominance) {
        alert('Пожалуйста, выберите одно состояние в каждой строке.');
        return;
    }

    fetch('/stateAnalyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedStates)
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/t';
            }
        })
        .then(data => {
            console.log('Success:', data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

document.getElementById('backBtn').addEventListener('click', function() {
    window.location.href = '/r';
});

document.getElementById('skipBtn').addEventListener('click', function() {
    window.location.href = '/t';
});