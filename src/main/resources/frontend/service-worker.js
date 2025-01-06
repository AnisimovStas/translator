// Слушаем событие sync
self.addEventListener('sync', event => {
    if (event.tag === 'fetch-data') {
        event.waitUntil(fetchData());
    }
});

function fetchData() {
    // Проверка разрешения на уведомления
    if (Notification.permission !== 'granted') {
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                sendPostRequest();
            } else {
                console.log('Permission denied for notifications');
            }
        });
    } else {
        sendPostRequest();
    }

    function sendPostRequest() {
        // Отправка POST запроса
        fetch('http://localhost:8085/words', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'  // Указываем тип данных как JSON
            },
            body: JSON.stringify({})  // Отправляем пустой объект, если не нужно передавать данные
        })
        .then(response => response.json())  // Предполагаем, что сервер вернет JSON
        .then(data => {
            // Обрабатываем полученные данные
            if (data === null) return;

            const notifTitle = `Добавлен перевод для слова: ${data.originalText}!`;
            const notifBody = `${data.originalText} - ${data.translatedText}`;
            self.registration.showNotification(notifTitle, {
                body: notifBody
            });

            console.log('Response data:', data);  // Логируем полученные данные
        })
        .catch(error => {
            console.error('Error fetching data:', error);  // Логируем ошибку, если запрос не удался
        });
    }
}
