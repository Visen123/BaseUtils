package com.yanyiyun.baseutils.library.function.stepDown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.yanyiyun.baseutils.library.function.stepDown.interfaces.IStepDBHelper;
import com.yanyiyun.baseutils.library.tool.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StepDBHelper extends SQLiteOpenHelper implements IStepDBHelper {

    /**
     * 只保留mLimit天的数据
     */
    private int mLimit = -1;

    public static IStepDBHelper factory(Context context){
        return new StepDBHelper(context);

    }
    private StepDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private StepDBHelper(Context context){
        super(context,DBConstants.DATABASE_NAME,null,DBConstants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable();
        onCreate(db);
    }

    @Override
    public void createTable() {
        getWritableDatabase().execSQL(DBConstants.SQL_CREATE_TABLE);
    }

    @Override
    public void deleteTable() {
        getWritableDatabase().execSQL(DBConstants.SQL_DELETE_TABLE);
    }

    @Override
    public void clearCapacity(String curData, int limit) {
        mLimit=limit;
        if(mLimit<0){
            return;
        }

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(DateUtils.getDateMillis(curData,DBConstants.DATE_PATTERN_YYYY_MM_DD));
        calendar.add(Calendar.DAY_OF_YEAR,-mLimit);

        List<StepData> stepDataList=getQueryAll();
        Set<String> delDateSet=new HashSet<>();
        for(StepData stepData:stepDataList){
            long daDate=DateUtils.getDateMillis(stepData.getToday(),DBConstants.DATE_PATTERN_YYYY_MM_DD);
            if(calendar.getTimeInMillis()>=daDate){
                delDateSet.add(stepData.getToday());
            }
        }

        for (String delDate : delDateSet) {
            getWritableDatabase().execSQL(DBConstants.SQL_DELETE_TODAY, new String[]{delDate});
        }
    }

    @Override
    public boolean isExist(StepData stepData) {
        Cursor cursor = getReadableDatabase().rawQuery(DBConstants.SQL_QUERY_STEP,
                new String[]{stepData.getToday(), stepData.getStep() + ""});
        boolean exist = cursor.getCount() > 0 ? true : false;
        cursor.close();
        return exist;
    }

    @Override
    public boolean isExistDay(long time) {
        String sql="SELECT * FROM " +DBConstants.TABLE_NAME + " WHERE " + DBConstants.TODAY
                +" = ?";
        Cursor cursor = getReadableDatabase().rawQuery(sql,
                new String[]{DateUtils.dateFormat(time,"yyyy-MM-dd")});
        boolean exist=cursor.getCount()>0?true:false;
        return exist;
    }


    @Override
    public void insert(StepData stepData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.TODAY, stepData.getToday());
        contentValues.put(DBConstants.DATE, stepData.getDate());
        contentValues.put(DBConstants.STEP, stepData.getStep());
        contentValues.put(DBConstants.LAST_SENOR_STEP,stepData.getLastSenorStep());
        getWritableDatabase().insert(DBConstants.TABLE_NAME, null, contentValues);
    }

    @Override
    public StepData getMaxStepByDate(long millis) {
        Cursor cursor = getReadableDatabase().rawQuery(DBConstants.SQL_QUERY_STEP_ORDER_BY,
                new String[]{DateUtils.dateFormat(millis, "yyyy-MM-dd")});
        StepData todayStepData = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            todayStepData = getStepData(cursor);
        }
        cursor.close();
        return todayStepData;
    }

    private StepData getStepData(Cursor cursor){
        String today = cursor.getString(cursor.getColumnIndex(DBConstants.TODAY));
        long date = cursor.getLong(cursor.getColumnIndex(DBConstants.DATE));
        long step = cursor.getLong(cursor.getColumnIndex(DBConstants.STEP));
        StepData todayStepData = new StepData();
        todayStepData.setToday(today);
        todayStepData.setDate(date);
        todayStepData.setStep(step);
        return todayStepData;
    }


    @Override
    public List<StepData> getQueryAll() {
        Cursor cursor = getReadableDatabase().rawQuery(DBConstants.SQL_QUERY_ALL, new String[]{});
        List<StepData> todayStepDatas = getStepDataList(cursor);
        cursor.close();
        return todayStepDatas;
    }

    private List<StepData> getStepDataList(Cursor cursor) {

        List<StepData> todayStepDatas = new ArrayList<>();
        while (cursor.moveToNext()) {
            StepData todayStepData = getStepData(cursor);
            todayStepDatas.add(todayStepData);
        }
        return todayStepDatas;
    }

    @Override
    public List<StepData> getStepListByData(String dateString) {
        Cursor cursor = getReadableDatabase().rawQuery(DBConstants.SQL_QUERY_STEP_BY_DATE, new String[]{dateString});
        List<StepData> todayStepDatas = getStepDataList(cursor);
        cursor.close();
        return todayStepDatas;
    }

    @Override
    public List<StepData> getStepListByStartDateAndDays(String startDate, int days) {
        List<StepData> todayStepDatas = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateUtils.getDateMillis(startDate, DBConstants.DATE_PATTERN_YYYY_MM_DD));
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Cursor cursor = getReadableDatabase().rawQuery(DBConstants.SQL_QUERY_STEP_BY_DATE,
                    new String[]{DateUtils.dateFormat(calendar.getTimeInMillis(), DBConstants.DATE_PATTERN_YYYY_MM_DD)});
            todayStepDatas.addAll(getStepDataList(cursor));
            cursor.close();
        }
        return todayStepDatas;
    }
}
