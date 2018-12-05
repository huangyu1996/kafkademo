package com.learn.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class TestHbase {
    //判断表是否存在
    private static Admin admin = null;
    private static Connection connection = null;

    static {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "192.168.37.100");
        /* 获取hbase的管理员对象 */

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(Connection conn, Admin admin) {
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static boolean old_tableExist(String tableName) throws IOException {
//        HBaseConfiguration configuration = new HBaseConfiguration();
//        configuration.set("hbase.zookeeper.quorum", "192.168.37.100");
//        //获取hbase的管理员对象
//        HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
//        boolean tableExists = hBaseAdmin.tableExists(tableName);
//        hBaseAdmin.close();
//        return tableExists;
//    }

    private static boolean tableExist(String tableName) throws IOException {
//        HBaseConfiguration configuration = new HBaseConfiguration();

        boolean tableExists = admin.tableExists(TableName.valueOf(tableName));
        admin.close();
        return tableExists;
    }


    //创建表
    public static void createTable(String tableName, String... cfs) throws IOException {//
        if (tableExist(tableName)) {
            System.out.println("Table already exists");
            return;
        }
        //创建表描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        //add column family
        for (String cf : cfs) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
//            hColumnDescriptor.setMaxVersions();
            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        admin.createTable(hTableDescriptor);
        System.out.println("Create table success");
    }

    public static void deleteTable(String tableName) throws IOException {
        if (tableExist(tableName)) {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));

        }
        if (!tableExist(tableName)) {
            System.out.println("Delete table " + tableName + " success");
        }
    }

    //删除表
    //CRUD
    public static void main(String[] args) throws IOException {
        System.out.println("123");
//        System.out.println(tableExist("staff"));
//        System.out.println(tableExist("student"));

        deleteTable("clienttable");
        System.out.println(tableExist("clienttable"));
        close(connection, admin);

    }
}
