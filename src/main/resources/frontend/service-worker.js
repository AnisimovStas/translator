self.addEventListener('sync', event => {
    if (event.tag === 'fetch-data') {
        event.waitUntil(fetchData());
    }
});

const interval = 1000; // 1 секунда

let isPermissionRequested = false;

function fetchData() {
    console.log("Fetching data...");

    // Проверка разрешения на уведомления только один раз
    if (Notification.permission !== 'granted' && !isPermissionRequested) {
        isPermissionRequested = true;
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                sendPostRequest();
            } else {
                console.log('Permission denied for notifications');
            }
        });
    } else if (Notification.permission === 'granted') {
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

// Если нужно периодически повторять запросы
// Можно запустить setInterval только если вкладка открыта
if (!self.intervalRunning) {
    self.intervalRunning = true;
    setInterval(fetchData, interval);
}
