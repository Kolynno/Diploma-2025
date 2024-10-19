document.getElementById('submitBtn').addEventListener('click', function() {
    // Получаем данные формы
    const firstname = document.getElementById('firstname').value;
    const lastname = document.getElementById('lastname').value;
    const middlename = document.getElementById('middlename').value;
    const birthday = document.getElementById('birthday').value;
    const sex = document.getElementById('sex').value;
    const education = document.getElementById('education').value;

    // Проверка на заполнение всех полей
    let errorMessage = '';
    if (!firstname) errorMessage += 'Имя не введено.\n';
    if (!lastname) errorMessage += 'Фамилия не введена.\n';
    if (!middlename) errorMessage += 'Отчество не введено.\n';
    if (!birthday) errorMessage += 'Дата рождения не введена.\n';
    if (!sex) errorMessage += 'Пол не выбран.\n';
    if (!education) errorMessage += 'Место учебы не введено.\n';

    // Если есть ошибки, выводим их и прекращаем выполнение
    if (errorMessage) {
        alert('Пожалуйста, заполните каждое поле корректными данными.');
        return;
    }

    // Создаем объект с данными
    const formData = {
        firstname: firstname,
        lastname: lastname,
        middlename: middlename,
        birthday: birthday,
        sex: sex,
        education: education
    };

    // Пример для отправки данных на сервер через fetch
    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                // Перенаправление на страницу после успешной отправки данных
                window.location.href = '/s'; // путь страницы, которую вернет сервер
            } else {
                throw new Error('Ошибка при регистрации');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            document.getElementById('error').textContent = 'Ошибка при регистрации: ' + error.message;
        });
});
