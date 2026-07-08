package props;

import lombok.Getter;

import java.time.Duration;

@Getter
public class AddMoneyProps {

    private final Duration userIdLoadDuration = Duration.ofMillis(100);
    private final Duration addMoneyApplyDuration = Duration.ofMillis(100);
    private final int userIdLoadRetries = 10;
}
