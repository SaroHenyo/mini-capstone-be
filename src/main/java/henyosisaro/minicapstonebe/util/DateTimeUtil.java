package henyosisaro.minicapstonebe.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class DateTimeUtil {

    public ZonedDateTime currentDate() {
        final ZoneId zoneId = ZoneId.of("Asia/Manila");
        return ZonedDateTime.now().withZoneSameInstant(zoneId);
    }
}
