package utils;

import lombok.extern.log4j.Log4j2;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Log4j2
public class Retry implements IRetryAnalyzer {
    private int count = 0 ;
    private static int maxTry = 3 ;
    @Override
    public boolean retry ( ITestResult iTestResult ) {
        if ( !iTestResult. isSuccess ()) { //Проверка, не завершился ли тест успешно
            if ( count < maxTry ) { //Проверяем, достигнуто ли максимальное количество попыток
                count++;                                      //Увеличьте значение maxTry на 1
                log.info("Need more try");
                iTestResult. setStatus ( ITestResult. FAILURE ) ;   //Отметить тест как неудавшийся
                return true ;                                  // Сообщает TestNG о необходимости повторного запуска теста
            } else  {
                log.error("Max retry");
                iTestResult.setStatus ( ITestResult.FAILURE ) ;   // Если достигнуто значение maxCount, тест помечается как неудачный
            }
        } else  {
            log.info("Test is good");
            iTestResult.setStatus ( ITestResult.SUCCESS ) ; //       Если тест пройден успешно, TestNG помечает его как пройденный
        }
        return false;
    }
}
