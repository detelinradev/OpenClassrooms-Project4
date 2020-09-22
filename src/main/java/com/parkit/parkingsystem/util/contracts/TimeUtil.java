package com.parkit.parkingsystem.util.contracts;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface TimeUtil {

    LocalDateTime getTime();

    long getTimeInSeconds();

}
