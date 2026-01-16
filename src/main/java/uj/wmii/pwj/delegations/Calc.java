package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");

    public BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {
        ZonedDateTime startTime = ZonedDateTime.parse(start, FORMATTER);
        ZonedDateTime endTime = ZonedDateTime.parse(end, FORMATTER);

        if (!endTime.isAfter(startTime)) {
            return BigDecimal.ZERO.setScale(2);
        }

        long fullDays = Duration.between(startTime, endTime).toDays();
        ZonedDateTime afterFullDays = startTime.plusDays(fullDays);
        Duration remainingDuration = Duration.between(afterFullDays, endTime);
        long remainingMinutes = remainingDuration.toMinutes();

        BigDecimal total = BigDecimal.ZERO;

        total = total.add(dailyRate.multiply(new BigDecimal(fullDays)));

        if (remainingMinutes > 0) {
            if (remainingMinutes <= 8 * 60) {
                total = total.add(dailyRate.divide(new BigDecimal(3), 2, RoundingMode.HALF_UP));
            } else if (remainingMinutes <= 12 * 60) {
                total = total.add(dailyRate.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP));
            } else {
                total = total.add(dailyRate);
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
