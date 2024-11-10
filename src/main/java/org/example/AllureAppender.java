package org.example;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.qameta.allure.Allure;

public class AllureAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent event) {
        // Форматируем лог-сообщение
        String logMessage = String.format("[%s] %s - %s",
                event.getLevel(), event.getLoggerName(), event.getFormattedMessage());

        // Добавляем лог-сообщение как вложение в Allure
        Allure.addAttachment("Log Entry", logMessage);
    }
}
