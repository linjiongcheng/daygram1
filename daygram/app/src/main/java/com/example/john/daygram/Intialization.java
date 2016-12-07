package com.example.john.daygram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Initialization {
    List<Data> data = new ArrayList<Data>();
    public List<Data> init(){
        Date date;
        Calendar c = Calendar.getInstance();
        String year;
        String month;
        String day;
        String week;
        Map<String,String> mEngMonth = new HashMap<String,String>(){{
            put("1","JANUARY");put("2","FEBRUARY");put("3","MARCH");put("4","APRIL");put("5","MAY");put("6","JUNE");
            put("7","JULY");put("8","AUGUST");put("9","SEPTEMBER");put("10","OCTOBER");put("11","NOVEMBER");put("12","DECEMBER");
        }};
        String start = "2016-01-01";
        String end = "2020-12-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null;
        Date dEnd = null;
        try{
            dBegin = sdf.parse(start);
            dEnd = sdf.parse(end);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<Date> listDate = getDatesBetweenTwoDate(dBegin, dEnd);
        String str;
        for(Iterator<Date> iter = listDate.iterator(); iter.hasNext();){
            date = iter.next();
            str = sdf.format(date);
            week = getWeekOfDate(date);
            c.setTime(date);
            year = c.get(Calendar.YEAR)+"";
            month = (c.get(Calendar.MONTH)+1)+"";
            day = c.get(Calendar.DATE)+"";
            Data temp = new Data();
            temp.setDate(str);
            temp.setYear(year);
            temp.setMonth(mEngMonth.get(month));
            temp.setDay(day);
            temp.setWeek(week);
            temp.setDetail("");
            data.add(temp);
        }
        return data;
    }
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}