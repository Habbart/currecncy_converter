# currecncy_converter

Конвертер валют в соответствии с заданием

## Требования
Разработать приложение-сервис для мониторинга курса валют.
С периодичностью 30 минут получать котировки для пар RUB-евро, USD-евро, JPY-евро из публичного API центробанка.
URL: https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml
Сохранять котировки в локальное хранилище (например SQLite).
Реализовать доступ к данным через вебсервис (REST) с возможностью фильтрации по дате и\или валютной паре.
Необходимо представить исходные тексты приложения в виде Git архива или ссылку на проект в Githab.


### Список URL HTTP-методов
#### GET /currency
Возвращает определенный коэффициент валюты в соответствии с переданной датой и/или кодом валюты

Параметры запроса передаются в URL:

date — дата в формате "yyyy-mm-dd" - получение курсов всех валют за определенную дату;
currency - валюта в формате "USD" - получение курса данной валюты за все периоды
пустой запрос - получение всех валют на текущую дату


Результаты:

HTTP 200 — запрос выполнен, результат в теле ответа в виде JSON:

HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;

HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

Примеры запросов:

currency?currency=rub

Ответ:

{

        "id": 15,
        
        "date": "2021-12-06",
        
        "name": "RUB",
        
        "ratio": 83.3889
        
        }


currency?date=2021-12-06&currency=usd

Ответ:

{

        "id": 2,
        
        "date": "2021-12-06",
        
        "name": "JPY",
        
        "ratio": 127.78
        
    }


