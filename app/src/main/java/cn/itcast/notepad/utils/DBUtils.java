package cn.itcast.notepad.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    public static final String DATABASE_NAME = "Notepad";  //数据库名
    public static final String DATABASE_TABLE = "Note";    //数据库名
    public static final int DATABASE_VERION = 1;           //数据库版本
    //数据库表中的列名
    public static final String NOTEPAD_ID = "id";
    public static final String NOTEPAD_CONTENT = "content";
    public static final String NOTEPAD_TIME = "notetime";
    //获取当前时间
    public static final String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
