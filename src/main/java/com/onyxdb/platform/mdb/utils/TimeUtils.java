package com.onyxdb.platform.mdb.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
    public static final ZoneId ZONE = ZoneId.of("Europe/Moscow");
    private static final Clock CLOCK = Clock.system(ZONE);

    public static LocalDateTime now() {
        return LocalDateTime.now(CLOCK);
    }
}
