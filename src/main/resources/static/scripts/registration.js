document.getElementById('reportBtn').addEventListener('click', function () {
    const loginId = document.getElementById('id').value;
    if (loginId == null) {
        alert('Введите корректный код участника');
        return;
    }
    const loginData = {
        loginId: loginId
    };

    fetch('/downloadReport', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/s';
            } else {
                throw new Error('Ошибка при входе');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            document.getElementById('error').textContent = 'Ошибка при входе: ' + error.message;
        });
})

document.getElementById('loginBtn').addEventListener('click', function () {
    const loginId = document.getElementById('id').value;
    if (loginId == null) {
        alert('Введите корректный код участника');
        return;
    }
    const loginData = {
        loginId: loginId
    };

    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/s';
            } else {
                throw new Error('Ошибка при входе');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            document.getElementById('error').textContent = 'Ошибка при входе: ' + error.message;
        });
})

document.getElementById('submitBtn').addEventListener('click', function() {
    const firstname = document.getElementById('firstname').value;
    const lastname = document.getElementById('lastname').value;
    const middlename = document.getElementById('middlename').value;
    const birthday = document.getElementById('birthday').value;
    const sex = document.getElementById('sex').value;
    const education = document.getElementById('education').value;

    let errorMessage = '';
    if (!firstname) errorMessage += 'Имя не введено.\n';
    if (!lastname) errorMessage += 'Фамилия не введена.\n';
    if (!middlename) errorMessage += 'Отчество не введено.\n';
    if (!birthday) errorMessage += 'Дата рождения не введена.\n';
    if (!sex) errorMessage += 'Пол не выбран.\n';
    if (!education) errorMessage += 'Место учебы не введено.\n';

    if (errorMessage) {
        alert('Введены некорректные данные:\n' + errorMessage);
        return;
    }

    const formData = {
        firstname: firstname,
        lastname: lastname,
        middlename: middlename,
        birthday: birthday,
        sex: sex,
        education: education
    };

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/s';
            } else {
                throw new Error('Ошибка при регистрации');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            document.getElementById('error').textContent = 'Ошибка при регистрации: ' + error.message;
        });
});