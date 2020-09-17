package com.parkit.parkingsystem.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {
    public LocalDateTime getTime(){
        return LocalDateTime.now();
    }

    public long getTimeInSeconds(){
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
