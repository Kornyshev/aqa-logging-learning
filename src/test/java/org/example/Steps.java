package org.example;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Steps {

    @Step("Открываем приложение")
    public void openApplication() {
        log.info("Приложение открыто.");
    }

    @Step("Авторизуемся в приложении")
    public void login() {
        log.info("Выполнен вход в приложение.");
    }

    @Step("Выполняем действия пользователя")
    public void performUserActions() {
        log.info("Выполняются действия пользователя.");
    }

    @Step("Выходим из приложения")
    public void logout() {
        log.info("Выполнен выход из приложения.");
    }

    @Step("Закрываем приложение")
    public void closeApplication() {
        log.info("Приложение закрыто.");
    }
}

