<pre>Мобильное клиент-серверное приложение по просмотру японских мультипликаций.</pre> <br />
Архитектура:<br />
![image](https://user-images.githubusercontent.com/82723047/218302050-4136ebf3-d94d-4550-af05-4a60d056f889.png)<br />
-Клиент: Мобильное приложение на языка Java.<br />
-Rest-api сервер: ASP.net Core WEB API.<br />
-БД(Сервер БД): PostgreSQL.<br />
-Дополнительно: На сервере реализована пагинация, а также два вида обмена пакетами: запросы, требующие авторизации и без(JWT-Beare token).<br />
Use-Case-диаграмма:<br />
![image](https://user-images.githubusercontent.com/82723047/218301664-12cb45f5-f122-49ba-b962-3c6b4e2376da.png)
<br />
Er-диаграмма:<br />
![image](https://user-images.githubusercontent.com/82723047/218301702-a9cd05a5-4d63-44ec-8952-ce4eccba5f1b.png)<br />
Диаграмма потоков экранов: https://www.figma.com/file/wonhxJuNlYcMzlKarMptbK/Diagram---Untitled?t=9kkMi7XnIAfyUPaE-6 <br/>
![image](https://user-images.githubusercontent.com/82723047/218301836-a066ac81-8922-4a59-a7ae-afa2a9cb0735.png)
