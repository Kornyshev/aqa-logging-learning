### Логирование в Java с использованием Lombok, SLF4J и Allure: Полное руководство

В современном программировании логирование играет ключевую роль в обеспечении наглядности работы приложения, помогая в поиске ошибок и оптимизации производительности. Библиотеки Lombok, SLF4J и Allure предоставляют мощные инструменты для реализации логирования и добавления отчетности. В данной статье мы разберем, как можно использовать их вместе, чтобы получить максимум пользы и удобства при логировании и создании отчетов.

---

## Что такое Lombok, SLF4J и Allure

- **Lombok** — библиотека для сокращения шаблонного кода. Позволяет автоматически генерировать методы `getter`/`setter`, конструкторы и методы `toString`, `equals` и `hashCode`. Также Lombok предлагает встроенную аннотацию `@Slf4j`, которая создает логгер `SLF4J` в классе.

- **SLF4J (Simple Logging Facade for Java)** — это фасад для различных систем логирования, таких как Logback, Log4j и Java Util Logging (JUL). Позволяет легко менять реализацию логирования, не изменяя основной код приложения.

- **Allure** — это инструмент для создания наглядных отчетов о тестировании. Allure позволяет добавлять в отчеты шаги выполнения тестов, логи, скриншоты, вложения и другую информацию, что значительно упрощает анализ результатов тестирования.

---

## Интеграция Lombok и SLF4J для логирования

С помощью Lombok можно упростить подключение логирования. Аннотация `@Slf4j` автоматически создает объект `Logger` в вашем классе, позволяя использовать его без лишнего кода. SLF4J используется как интерфейс для различных реализаций логирования, а в качестве бэкенда можно выбрать, например, Logback.

### Шаг 1: Настройка Maven-зависимостей

Для начала добавьте необходимые зависимости в `pom.xml`:

```xml
<dependencies>
    <!-- Lombok для автоматического создания логгера и других полезных аннотаций -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.26</version>
        <scope>provided</scope>
    </dependency>

    <!-- SLF4J API для логирования -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.36</version>
    </dependency>

    <!-- Logback для реализации логирования -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.11</version>
    </dependency>

    <!-- Allure для отчетности -->
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-java-commons</artifactId>
        <version>2.20.1</version>
    </dependency>
</dependencies>
```

### Шаг 2: Настройка класса с логированием

С помощью аннотации `@Slf4j` от Lombok можно легко добавить логгер в любой класс:

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleService {

    public void performAction() {
        log.info("Performing an action...");
        try {
            // Some logic
            log.debug("Debugging details for action.");
        } catch (Exception e) {
            log.error("An error occurred during action", e);
        }
    }
}
```

После добавления `@Slf4j`, в классе `ExampleService` появляется логгер `log`, и можно использовать методы `log.info()`, `log.debug()`, `log.error()` и другие.

### Шаг 3: Настройка конфигурации Logback для кастомизации логов

Чтобы настроить уровень логирования и вывести логи в нужном формате, создайте файл `logback.xml` в `src/main/resources`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

Здесь определяется `ConsoleAppender`, который выводит логи в консоль с уровнем `info` и выше.

---

## Логирование в Allure с использованием SLF4J и Lombok

Allure позволяет добавлять логи в отчеты, чтобы улучшить детализацию тестов. Можно сделать это двумя способами:

1. **Шаги Allure**: каждый шаг логируется и отображается в отчете Allure.
2. **Вложенные файлы логов**: прикрепить лог как отдельное вложение к отчету Allure.

### Вариант 1: Логирование как шаги Allure

Аннотация `@Step` позволяет делать запись в отчет Allure для каждого вызова метода, автоматически добавляя его как "шаг" в Allure. Добавьте аннотации `@Step` для логирования в виде шагов:

```java
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllureLoggingExample {

    @Step("Выполнение действия")
    public void performAction() {
        log.info("Начинаем выполнение действия.");
        try {
            // Некоторая логика
            log.debug("Подробности выполнения действия.");
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}
```

В этом случае метод `performAction` будет добавлен как шаг в Allure-отчет, а каждый лог внутри него будет записан в консоль.

### Вариант 2: Прикрепление логов как вложение

Для создания вложения в Allure можно создать кастомный `Appender`, который будет добавлять каждое лог-сообщение как вложение.

Пример `AllureAppender`, добавляющий каждое сообщение в отчет:

```java
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.qameta.allure.Allure;

public class AllureAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        String logMessage = String.format("[%s] %s - %s",
                event.getLevel(), event.getLoggerName(), event.getFormattedMessage());
        
        Allure.addAttachment("Log Entry", "text/plain", logMessage);
    }
}
```

Каждое лог-сообщение теперь будет отображаться в Allure как отдельное вложение.

---

## Примеры использования Allure и логирования в тестах

Для демонстрации возможностей Allure в тестах можно использовать методы `@BeforeEach`, `@AfterEach`, `@Test` и логировать в них важные шаги. Пример теста с использованием Allure, Lombok и SLF4J:

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Step;

@Slf4j
public class AppTest {

    @BeforeEach
    void setUp() {
        log.info("Подготовка перед тестом");
    }

    @Test
    void someTest() {
        log.info("Начало теста");
        performAction();
        log.info("Завершение теста");
    }

    @AfterEach
    void tearDown() {
        log.info("Завершение после теста");
    }

    @Step("Выполняем действие в тесте")
    void performAction() {
        log.info("Выполнение действия внутри теста");
    }
}
```

### Настройка Allure

Для интеграции Allure с тестами, запустите тесты с флагом для генерации отчетов Allure:

```bash
mvn clean test
mvn allure:serve
```

---

## Советы и особенности интеграции

1. **Совместимость версий**: Убедитесь, что версии Lombok, SLF4J и Allure совместимы друг с другом.
2. **Поддержка логирования в Allure**: Используйте `Appender`, если хотите, чтобы логи автоматически добавлялись в отчет.
3. **Форматирование логов**: Конфигурация Logback позволяет гибко настроить формат логов. Например, можно добавить информацию о потоке, времени выполнения и др.
4. **Уровни логирования**: Используйте уровни `info`, `debug`, `error` для более точного управления выводом.
5. **Allure Steps vs. Attachments**: Используйте `@Step` для создания шагов в отчете или добавляйте вложения, если нужно логировать весь поток.
