package utils;

import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Log4j2
public class AddMoneyUtils {

    /**
     * Универсальный метод для получения закэшированного значения или загрузки нового
     *
     * @param cachedValue   AtomicReference для хранения кэшированного значения
     * @param supplier      Функция, возвращающая значение любого типа
     * @param duration      Время ожидания между попытками
     * @param retries       Количество попыток
     * @param <T>           Тип возвращаемого значения
     * @return Значение или null, если не удалось получить
     */
    public static <T> @Nullable T getCached(
            AtomicReference<T> cachedValue,
            Supplier<T> supplier,
            Duration duration,
            int retries
    ) {
        T value = cachedValue.get();
        if (value == null) {
            log.info("Кэш пуст, загружаем значение...");
            value = getWithRetries(supplier, duration, retries);
            if (value != null) {
                cachedValue.set(value);
                log.info("Значение закэшировано: {}", value);
            }
        } else {
            log.info("Используем закэшированное значение: {}", value);
        }
        return value;
    }

    /**
     * Универсальный метод для получения значения с повторными попытками
     */
    public static <T> @Nullable T getWithRetries(
            Supplier<T> supplier,
            Duration duration,
            int retries
    ) {
        T result = null;
        int attempt = 1;
        while (result == null && attempt <= retries) {
            log.info("Attempt {} of {} to load value...", attempt, retries);
            waitingLoad(duration);
            result = supplier.get();
            attempt++;
            if (result == null) {
                log.warn("Supplier returned null on attempt {}", attempt - 1);
            }
        }
        return result;
    }

    /**
     * Ожидание с обработкой InterruptedException
     */
    public static void waitingLoad(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Waiting was interrupted", e);
        }
    }
}