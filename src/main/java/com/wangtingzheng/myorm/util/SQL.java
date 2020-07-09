package com.wangtingzheng.myorm.util;

import com.wangtingzheng.myorm.entity.TableItemEntity;
import com.wangtingzheng.myorm.enums.ItemTypeEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author WangTingZheng
 * @date 2020/7/9 16:11
 * @features
 */
public class SQL {
    private static String getUseDatabaseSql(String databaseName){
        return "use "+databaseName+";\n";
    }

    private static String getCreateDatabaseSql(String databaseName){
        return "create database if not exists "+databaseName+";\n";
    }


    public static boolean executeSQL(Connection connection, String sql){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean useDatabase(Connection connection, String databaseName){
        return executeSQL(connection, getUseDatabaseSql(databaseName));
    }

    public static boolean createDatabase(Connection connection, String databaseName){
        return executeSQL(connection, getCreateDatabaseSql(databaseName));
    }

    /**
     * 根据输入的列名称和列值，生成插入语句
     * @param value 列名称和列值，第一个是列名称，第二个是列值
     * @return 可执行的sql语句
     */
    private static String getInsertSQL(String tableName, HashMap<String,String> value){
        String sql = "insert into "+ tableName +" (";
        String items = "";
        String itemValues = "";
        Set<String> set = value.keySet();
        for (String item : set) {
            String itemValue = value.get(item);
            items = items + item + ",";
            itemValues = itemValues + "'"+itemValue+"'" + ",";
        }
        items = items.substring(0, items.length()-1)+")";
        itemValues = itemValues.substring(0, itemValues.length()-1)+");";
        return sql + items + " values(" + itemValues;
    }

    public static boolean insert(Connection connection, String tableName, HashMap<String,String> value){
        return executeSQL(connection, getInsertSQL(tableName, value));
    }

    private static String getCreateTableSQL(String tableName, List<TableItemEntity> tableItemEntities) {
        String sql = "create table if not exists " + tableName+"(\n";
        for(TableItemEntity tableItemEntity : tableItemEntities) {
            sql = sql + toSQL(tableItemEntity.getName(), tableItemEntity.getType(), tableItemEntity.getLength(), tableItemEntity.isPrimaryKey())+",\n";
        }
        return sql.substring(0, sql.length()-2)+"\n);";
    }

    private static String toSQL(String name, ItemTypeEnum type, long length, boolean isPrimaryKey){
        return name +" "+ type +" ("+length+") "+ (isPrimaryKey ?"PRIMARY KEY":"");
    }

    public static boolean createTable(Connection connection, String tableName, List<TableItemEntity> tableItemEntities){
        return executeSQL(connection, getCreateTableSQL(tableName, tableItemEntities));
    }

    //delete from mytable where username = "wtz" && password = "123";
    private static String getDeleteSQL(String tableName, HashMap<String,String> value){
        String sql = "delete from "+tableName+ " where ";
        String items = "";
        Set<String> set = value.keySet();
        for (String item : set) {
            String itemValue = value.get(item);
            items = items + item + " = " + "\"" +itemValue+"\""+" && ";
        }
        items = items.substring(0, items.length()-4)+";";
        return sql + items;
    }

    public static boolean delete(Connection connection, String tableName, HashMap<String,String> value){
        return executeSQL(connection, getDeleteSQL(tableName,value));
    }

    /*
    //update mytable set username = "wtz", password = "1234 where;
    private static String getUpdateSQL(String tableName){

    }*/

    public static void main(String[] args) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("wtz","123");
        System.out.println(getDeleteSQL("mytable",hashMap));
    }
}
