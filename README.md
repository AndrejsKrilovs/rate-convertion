# Currency Exchange REST API

Простое учебное REST API на Kotlin + Jakarta Servlets (Jetty), реализующее CRUD-интерфейс для валют и обменных курсов.

---

## 📌 Возможности API

- Управление валютами (создание, просмотр списка и отдельной валюты)
- Управление обменными курсами (создание, просмотр списка, просмотр конкретного курса, обновление)
- Обмен валюты с вычислением сконвертированной суммы
- Корректные HTTP-статусы и JSON-ответы для ошибок

---

## 📖 Методы API

### 💵 Валюты

#### `GET /currencies`
Получение списка всех валют.

Пример ответа:
```json
[
  {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  }
]
```

Коды ответа:
- `200 OK` — успех  
- `500 Internal Server Error` — ошибка сервера

---

#### `GET /currency/{code}`
Получение конкретной валюты по коду.

Пример ответа:
```json
{
  "id": 1,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```

Коды ответа:
- `200 OK` — успех  
- `400 Bad Request` — код валюты не указан  
- `404 Not Found` — валюта не найдена  
- `500 Internal Server Error` — ошибка сервера  

---

#### `POST /currencies`
Добавление новой валюты.  
Данные передаются как `application/x-www-form-urlencoded`.

Поля формы:
- `name` — название валюты  
- `code` — буквенный код (например, USD)  
- `sign` — символ (например, `$`)  

Пример ответа:
```json
{
  "id": 2,
  "name": "Euro",
  "code": "EUR",
  "sign": "€"
}
```

Коды ответа:
- `201 Created` — успех  
- `400 Bad Request` — отсутствует обязательное поле  
- `409 Conflict` — валюта с таким кодом уже существует  
- `500 Internal Server Error` — ошибка сервера  

---

### 📊 Обменные курсы

#### `GET /exchangeRates`
Получение списка всех курсов.

Пример ответа:
```json
[
  {
    "id": 0,
    "baseCurrency": { 
      "id": 0, 
      "name": "United States dollar",
      "code": "USD", 
      "sign": "$"
    },
    "targetCurrency": {
      "id": 1, 
      "name": "Euro",
      "code": "EUR", 
      "sign": "€"
    },
    "rate": 0.99
  }
]
```

Коды ответа:
- `200 OK` — успех  
- `500 Internal Server Error` — ошибка сервера  

---

#### `GET /exchangeRate/{pair}`
Получение конкретного обменного курса по валютной паре (например, `USDRUB`).

Пример ответа:
```json
{
  "id": 0,
  "baseCurrency": { 
    "id": 0, 
    "name": "United States dollar", 
    "code": "USD", 
    "sign": "$"
  },
  "targetCurrency": { 
    "id": 2, 
    "name": "Russian Ruble", 
    "code": "RUB", 
    "sign": "₽"
  },
  "rate": 80
}
```

Коды ответа:
- `200 OK` — успех  
- `400 Bad Request` — отсутствует код валютной пары  
- `404 Not Found` — курс не найден  
- `500 Internal Server Error` — ошибка сервера  

---

#### `POST /exchangeRates`
Добавление нового курса.  
Форма: `application/x-www-form-urlencoded`.

Поля формы:
- `baseCurrencyCode` — базовая валюта  
- `targetCurrencyCode` — целевая валюта  
- `rate` — курс обмена  

Пример ответа:
```json
{
  "id": 1,
  "baseCurrency": { 
    "id": 0, 
    "name": "United States dollar", 
    "code": "USD", 
    "sign": "$"
  },
  "targetCurrency": { 
    "id": 1, 
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```

Коды ответа:
- `201 Created` — успех  
- `400 Bad Request` — отсутствует обязательное поле  
- `404 Not Found` — одна или обе валюты отсутствуют в базе  
- `409 Conflict` — пара уже существует  
- `500 Internal Server Error` — ошибка сервера  

---

#### `PATCH /exchangeRate/{pair}`
Обновление курса валютной пары.  

Форма: `application/x-www-form-urlencoded`.  
Поля:
- `rate` — новый курс  

Пример ответа:
```json
{
  "id": 0,
  "baseCurrency": { 
    "id": 0, 
    "name": "United States dollar", 
    "code": "USD", 
    "sign": "$"
  },
  "targetCurrency": { 
    "id": 2, 
    "name": "Russian Ruble", 
    "code": "RUB", 
    "sign": "₽"
  },
  "rate": 80
}
```

Коды ответа:
- `200 OK` — успех  
- `400 Bad Request` — отсутствует обязательное поле  
- `404 Not Found` — пара не найдена  
- `500 Internal Server Error` — ошибка сервера  

---

### 🔄 Обмен валюты

#### `GET /exchange?from=USD&to=EUR&amount=10`
Вычисление обмена средств между валютами.

Пример ответа:
```json
{
  "baseCurrency": { 
    "id": 0, 
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": { 
    "id": 1, 
    "name": "Euro",
    "code": "EUR", 
    "sign": "€"
  },
  "rate": 0.99,
  "amount": 10.00,
  "convertedAmount": 9.90
}
```

Коды ответа:
- `200 OK` — успех  
- `400 Bad Request` — отсутствует параметр  
- `404 Not Found` — курс или валюта не найдены  
- `500 Internal Server Error` — ошибка сервера  

---

## ⚠️ Ошибки

Все ошибки возвращаются в формате:
```json
{
  "message": "Описание ошибки"
}
```

Примеры:
- `"message": "Валюта не найдена"`  
- `"message": "Отсутствует нужное поле формы"`  
