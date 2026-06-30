package wrappers;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.$x;

public class GetStatus {
    public static String getStatus() { Selenide.sleep(3000);
        return $x("//button[contains(@class, 'status')]").getText();
    }
}
