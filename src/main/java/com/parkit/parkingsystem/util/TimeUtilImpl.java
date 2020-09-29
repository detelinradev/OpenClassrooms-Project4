package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.util.contracts.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtilImpl implements TimeUtil {

    private static final Logger logger = LoggerFactory.getLogger("TimeUtil");

    public LocalDateTime getTime() {

        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public long getTimeInSeconds() {

        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
