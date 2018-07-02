package com.example.demo.payroll.utils;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

public class DateTimeUtils {
    public static Integer monthWeekDays(DateTime now){
        DateTime monthStart = now.dayOfMonth().withMinimumValue();
        DateTime monthEnd = now.dayOfMonth().withMaximumValue();
        return weekDays(monthStart,monthEnd);
    }

    public static Integer weekDays(DateTime start,DateTime end){
        if (start.isAfter(end)){
            throw new RuntimeException("开始时间大于结束时间，非法！");
        }

        if (Weeks.weeksBetween(start,end).isLessThan(Weeks.ONE)){
            Integer ret = 0;
            do {
                int weekDay = start.dayOfWeek().get();
                ret += weekDay != 6 && weekDay != 7 ? 1:0;
                start = start.plusDays(1);
            }while (start.isBefore(end) || start.isEqual(end));

            return ret;
        }

        Integer weeksBetween = Weeks.weeksBetween(start,end).getWeeks();
        return weekDays(start.plusDays(weeksBetween * 7),end) + weeksBetween * 5;
    }
}
