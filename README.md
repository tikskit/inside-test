# Тестовое задание для вакансии Junior Java Developer/ Младший программист
Описание задания:
https://inside24.bitrix24.ru/pub/task.php?task_id=167

Выполнил Лужных Виталий Сергеевич

https://novosibirsk.hh.ru/resume/b24f85d3ff002c0e580039ed1f736563726574

email: tikskit@gmail.com

тел: +7 (905) 951 50 15

# Инструкции по запуску

Запускаем в докере. 

## Запуск
Для запуска нужно сбилдить образ Docker и запустить его

### Билд образа
1. перейти в директорию, где лежит файл Dockerfile
2.  выполнить: `docker build -t inside-test .`

### Запуск образа
Выполнить: `docker run -d -p 8080:8080 inside-test`

# Эндпойнты

Когда приложение запущено в докере, оно доступно по адресу localhost:8080

## Эндпойнт POST /auth

Этот эндпойнт возвращает токен, если переданый в JSON пользователь существует в 
БД. Пользователи добавляются в БД в скрипте src\main\resources\data.sql

* В запрос нужно включить хедер `Content-Type: application/json`
* В тело запроса включить JSON:

        {
            "name": "test",
            "password": "000"
        }
        

Запрос должен вернуть JSON вида:

        {
            "token": "eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidGVzdCJ9.Inocw4tUCt4dCH7zsCum1yarB0IXjNAudtgIhNVStDiw5hWpJyqe7GJtuSmFSEdnY8q-eru0qTx6NfCisO7F7A"
        }
        
Если пользователь с заданным именем и паролем не существует в БД, то запрос вернет код 401. 
        
## Эндпойнт POST /message

Этот эндпойнт добавляет сообщение или возвращает 10 последних.

### Добавление нового сообщения

* В запрос нужно включить хедер `Content-Type: application/json`
* В запрос нужно включить хедер `token: eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidGVzdCJ9.Inocw4tUCt4dCH7zsCum1yarB0IXjNAudtgIhNVStDiw5hWpJyqe7GJtuSmFSEdnY8q-eru0qTx6NfCisO7F7A`

(поставить правильный токен, который вернул эндпойнт POST /auth)

* В тело запроса добавить JSON:

        {
            "name": "test",
            "message": "текст сообщения"
        } 
        
Значение name должно совпадать с каким-то существующим пользователем из БД, потому что добавляемое сообщение будет 
связано с ним (он типа его автор или что там подразумевалось). Если такого пользователя нет, то будет возвращен код 400,
а сообщение не добавится.
Если все выполнилось как надо, то запрос венет код 200.  
      
### Получение 10 последних сообщений

* В запрос нужно включить хедер `Content-Type: application/json`
* В запрос нужно включить хедер `token: eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoidGVzdCJ9.Inocw4tUCt4dCH7zsCum1yarB0IXjNAudtgIhNVStDiw5hWpJyqe7GJtuSmFSEdnY8q-eru0qTx6NfCisO7F7A`

(поставить правильный токен, который вернул эндпойнт POST /auth)

* В тело запроса добавить JSON:

        {
            "name": "test",
            "message": "history 10"
        } 
        
Запрос вернет JSON с массивом из 10 последних (если столько есть) сообщений и код 200. В данном случае значение поля
name не используется.