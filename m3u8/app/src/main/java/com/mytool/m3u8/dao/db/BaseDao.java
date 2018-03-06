package com.mytool.m3u8.dao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mytool.m3u8.dao.db.annotion.DbFiled;
import com.mytool.m3u8.dao.db.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghaofei on 2018/1/4.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {


    public SQLiteDatabase database;

    //保证实例化一次
    private boolean isInit = false;

    //持有操作数据库表所对应的java类型
    private Class<T> entityClass;

    //维护表名与成员变量名的映射关系
    private HashMap<String, Field> cacheMap;

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {

        if (!isInit) {
            this.database = sqLiteDatabase;
            entityClass = entity;
            if (entity.getAnnotation(DbTable.class) == null) {
                tableName = entity.getClass().getSimpleName();
            } else {
                tableName = entity.getAnnotation(DbTable.class).value();
            }

            if (!database.isOpen()) {
                return false;
            }

            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }

            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }

        return isInit;
    }


    /**
     * 维护映射关系
     */
    private void initCacheMap() {

        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {

            cursor = database.rawQuery(sql, null);
            //表的列名
            String[] columnNames = cursor.getColumnNames();
            //拿到filed数组
            Field[] colmunFields = entityClass.getFields();
            for (Field filed : colmunFields) {
                filed.setAccessible(true);
            }
            //开始找对应关系
            for (String colmunName : columnNames) {
                //如果找到对应的Filed就赋值给他
                Field colmunFiled = null;

                for (Field field : colmunFields) {
                    String fieldName = null;
                    if (field.getAnnotation(DbFiled.class).value() != null) {
                        fieldName = field.getAnnotation(DbFiled.class).value();
                    } else {
                        fieldName = field.getName();
                    }

                    //如果表的列名 等于了  成员变量的注解名字
                    if (colmunName.equals(fieldName)) {
                        colmunFiled = field;
                        break;
                    }
                }
                //找到了对应关系
                if (colmunFiled != null) {
                    cacheMap.put(colmunName, colmunFiled);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }


    }


    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        Long resultVal = database.insert(tableName, null, values);
        return resultVal;
    }


    @Override
    public int update(T entity, T where) {
        //
        int result = -1;

        Map<String, String> map = getValues(entity);

        Condition condition = new Condition(map);

        ContentValues values = getContentValues(map);
        result = database.update(tableName, values, condition.getWhereCause(), condition.getWhereArgs());
        return result;
    }

    @Override
    public int delete(T where) {
        Map<String, String> map = getValues(where);
        Condition condition = new Condition(map);

        int result = database.delete(tableName, condition.getWhereCause(), condition.getWhereArgs());
        return result;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValues(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }

        Condition condition = new Condition(map);
        Cursor cursor = database.query(tableName, null, condition.getWhereCause()
                , condition.getWhereArgs(), null, null, orderBy, limitString);

        List<T> result = getResult(cursor, where);
        cursor.close();
        return result;
    }

    @Override
    public List<T> query(String sql) {
        return null;
    }


    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list = new ArrayList();
        Object item;


        while (cursor.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                /**
                 * 列名  name
                 * 成员变量名  Filed;
                 */
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    //得到列名
                    String colomunName = (String) entry.getKey();
                    /**
                     * 然后以列名拿到  列名在游标的位子
                     */
                    Integer colmunIndex = cursor.getColumnIndex(colomunName);

                    Field field = (Field) entry.getValue();

                    Class type = field.getType();

                    if (colmunIndex != -1) {
                        if (type == String.class) {
                            //反射方式赋
                            field.set(item, cursor.getString(colmunIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(colmunIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(colmunIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(colmunIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(colmunIndex));
                            /*
                            不支持的类型
                             */
                        } else {
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 转换成ContentValues
     *
     * @param map
     * @return
     */
    private ContentValues getContentValues(Map<String, String> map) {

        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {

            String key = iterator.next();
            String values = map.get(key);
            if (values != null) {
                contentValues.put(key, values);
            }
        }

        return contentValues;
    }


    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> filedsIterator = cacheMap.values().iterator();

        while (filedsIterator.hasNext()) {

            Field colmunToFiled = filedsIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (colmunToFiled.getAnnotation(DbFiled.class) != null) {
                cacheKey = colmunToFiled.getAnnotation(DbFiled.class).value();
            } else {
                cacheKey = colmunToFiled.getName();
            }

            try {
                if (null == colmunToFiled.get(entity)) {
                    continue;
                }

                cacheValue = colmunToFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            result.put(cacheKey, cacheValue);
        }
        return result;
    }


    /**
     * 封装修改语句
     */
    class Condition {

        //查询条件
        private String whereCause;

        private String[] whereArgs;


        public String getWhereCause() {
            return whereCause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }

        public Condition(Map<String, String> whereCause) {
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            Set keys = whereCause.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = whereCause.get(key);
                if (value != null) {
                    /*
                    拼接条件查询语句
                    1=1 and name =? and password=?
                     */
                    stringBuilder.append(" and " + key + " =?");
                    /**
                     * ？----》value
                     */
                    list.add(value);
                }
            }

            this.whereCause = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }


    /**
     * 创建表
     *
     * @return
     */
    protected abstract String createTable();

}
