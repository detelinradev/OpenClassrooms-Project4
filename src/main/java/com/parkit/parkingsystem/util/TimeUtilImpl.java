package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.util.contracts.TimeUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtilImpl implements TimeUtil {

    public LocalDateTime getTime() {

        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public long getTimeInSeconds() {

        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
