package io.socket.client.executions;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.sql.*;
import java.sql.Connection;

public class PushDemo {

    public static void main(String[] args){
        try {
            // 推送时间
            String cod = null;
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            final Socket socket = IO.socket("https://localhost:8080",options);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("connect");
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("connect timeout");
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("connect error");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("disconnect");
                }
            });
            socket.open();
            String sql1 = "select * from push_time_record order by push_time desc limit 1";
            String sql2 = "INSERT INTO push_time_record (push_time, remarks, gmt_create, gmt_modified) VALUES(NOW(), 'test', NOW(), NOW())";
            String sql = "select * from data_source_info";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/projectinfo","root","root");
            Statement st = conn.createStatement();
            ResultSet res1 = st.executeQuery(sql1);
            while (res1.next()){
                cod = res1.getString("push_time");
            }
            if(cod != null){
                sql = sql + " where (SELECT UNIX_TIMESTAMP(gmt_create)) >= "  + "unix_timestamp('"+cod+"')";
            }
            ResultSet res = st.executeQuery(sql);
            JSONObject dataSourceInfo = new JSONObject();
            while(res.next()){
                dataSourceInfo.put("id",res.getInt("id"));
                dataSourceInfo.put("database",res.getString("database_code"));
                dataSourceInfo.put("databaseDescription",res.getString("database_desc"));
                dataSourceInfo.put("account",res.getString("account"));
                dataSourceInfo.put("password",res.getString("password"));
                dataSourceInfo.put("dataQuantity",res.getString("data_quantity"));
                dataSourceInfo.put("tableDtructureDescription",res.getString("table_dtructure_desc"));
                dataSourceInfo.put("remarks",res.getString("remarks"));
                dataSourceInfo.put("gmtCreate",res.getString("gmt_create"));
                dataSourceInfo.put("gmtModified",res.getString("gmt_modified"));
                dataSourceInfo.put("isDel",res.getString("is_del"));
                socket.emit("sendVerificationDatabaseCode",dataSourceInfo);
                System.out.println(dataSourceInfo);
            }
            System.out.println("推送结束");
            st.executeUpdate(sql2);
            conn.close();
            System.out.println("关闭conn");
            socket.close();
            System.out.println("关闭socket");
        }catch (Exception e){
            throw new RuntimeException("推送失败");
        }
    }
}
