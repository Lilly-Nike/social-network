Описание проекта social-network
=====================================

 В проекте используется Docker, для запуска в докере используется команда docker-compose up -d в корневой папке проекта.

 На проекте используется Swagger, поэтому все методы контроллера описываются его аннотациями.
   heroku : https://spring-boot-social-network.herokuapp.com/swagger-ui/index.html
   local : http://localhost:8088/swagger-ui/index.html

 Для отправки запроса на защищенный url необходим jwt token с нужными правами. Чтобы получить токен нужно отправить POST запрос на JwtAuthenticationRestController::getToken.

 Чтобы отправить токен вместе с запросом на защищенный url нужно добавить header - Authorization: Bearer ... и дальше сам токен.

 На проекте используется Lombok.

 Аннотации lombok которые мы используем @AllArgsConstructor @NoArgsConstructor @Getter @Setter. Equals и hashcode нужно переопределять самим через IDEA Alt + Insert.

 Если нужно заинжектить какие то классы в контроллер, сервис и тд... используйте не сам класс, а интерфейс который он реализует. Например: UserService, а не UserServiceImpl.

 Инжектирование или внедрение бинов нужно делать через конструктор, если есть такая возможность. Преимущественно через аннотацию @RequiredArgsConstructor.

 В контроллерах используются аннотации @Validated и @Valid для валидации принимаемых объектов.

 Исключения, выбрасываемые контроллерами, обрабатываются не в самих контроллерах, а снаружи - методами класса GlobalRestControllerExceptionHandler.

 Для валидации данных в контроллере используются статические методы класса ApiValidationUtil который в случае неудачи сам выбросит исключение, ему лишь нужно передать сообщение, которое отобразится в ответе клиенту.

 В качестве ответа клиенту из всех rest контроллеров используется класс Response<> обязательно параметризованный. Если ничего не нужно отправлять, то параметризовывать Void, те Response\<Void>.

 Code Style
* Убрать импорт \*, должны импоритроваться только нужные пакеты Settings -> Editor -> Code Style -> Java -> Imports -> Class count to use import with '\*' = 999, Names count to use static import with '\*' = 999.
* Отступы должны быть 4 пробела, не использовать табуляцию Settings -> Editor -> Code Style -> Java -> Tabs and Indents -> Use tab character должен быть отключен, Tab size = 4, Ident = 4, Label indent = 0.
* Все ветки ДОЛЖНЫ ответвляться от ветки dev предварительно обновив ее, чтоб было меньше конфилктов. И создавать PullRequest соответственно в ветку dev.
* НЕЛЬЗЯ делать коммиты и пушить напрямую в ветку dev, heroku или любую другую не вашу ветку.
* НЕЛЬЗЯ самим мержить PullRequest, этим занимается только reviewer после проверки вашего кода.
* Название ветки должно начинаться с ключевого слова BACK номера задачи, затем дефис feature и потом описание через дефис. Например: BACK15-feature-add-tests-UserRestController или BACK20-feature-fix-bug-with-user-registration.
* При первом коммите для проверки задачи, нужно пушить ровно один коммит, если нужно добавить изменения в текущий коммит, то можно сделать еще один коммит и объеденить 2 коммита в один выполнив rebase и затем через force push отправить изменения в удаленный репозиторий. Если после проверки code review есть замечания, то замечания нужно исправлять вторым коммитом. Если после проверки замечаний опять нужно что то исправлять, то третьим и тд.
* Сообщение коммита должно быть от имени коммита и должно начинаться с номера задачи. Можно мысленно добавить фразу 'Коммит ...' и дальше описывать что он выполняет. Например: 'BACK20 Удаляет метод в UserService::getAll' или 'BACK5 Фиксит регастрацию студента в UserService'.

 Интеграционные тесты
* Id сущностей в тестах должны начинаться с 101, 102, 103 и тд.
* Каждый тестовый класс должен полностью повторять название тестового класса с приставкой IT в конце и наследоваться от SpringSimpleContextTest. Например: public class UserRegistrationRestControllerIT extends SpringSimpleContextTest{}.
* Каждый метод в тестовом классе должен должен повторять название метода в контроллере, затем _ и с большой буквы описывать тест, конец метода должен заканчиваться Test. Например: registerNewUser_SuccessfulTest, registerNewUser_WithExistingUsernameTest. 
* В тестах не должна использоваться аннотация @Transactional.
* Если тест добавляет, обновляет или удаляет данные, то обязательно должен быть запрос в бд через EntityManager для подтверждения, что данные действительно изменились.
* База данных для тестов поднимается автоматически в докер контейнере.
* На каждый контроллер создается свой пакет с тестовыми данными. Пакет должен лежать в scripts/ и дальше структура папок начиная от rest. Например: kata.academy.socialnetwork.web.rest.user.UserRegistrationRestController значит scripts/user/UserRegistrationRestController.
* Каждый тест должен иметь собственный пакет с тестовыми данными и лежать в пакете соответствующего контроллера. Название пакета должно должно совпадать с названием теста. В пакете должно быть 2 .sql файла: BeforeTest.sql и AfterTest.sql. Например: scripts/user/UserRegistrationRestController/registerNewUser_SuccessfulTest/BeforeTest.sql. Очищение тестовых данных после теста должно использовать TRUNCATE TABLE название таблицы CASCADE. 
* Каждый тест должен инициализировать тестовые данные необходимые только для теста и по окончанию очищать все после себя, не загружать тестовые данные для всего тестового класса.
