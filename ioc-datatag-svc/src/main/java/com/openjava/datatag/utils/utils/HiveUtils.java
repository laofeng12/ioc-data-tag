package com.openjava.datatag.utils.utils;


import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


/**
 * 远程调用hdf和hive的例子
 */
public class HiveUtils {
    private static final String driverName =
            "org.apache.hive.jdbc.HiveDriver";
//    private static Configuration conf = new Configuration();
//    private static FileSystem hdfs;
    private static final String dir = "/datatag/";
//    @Before
//    public void init() throws Exception{
//        //root是你主节点虚机的用户名
//        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("root");
//        //本地环境必须要指定目录（hadoop-common-2.2.0-bin要安装并配置HADOOP_HOME）
//        System.setProperty("hadoop.home.dir", "F:\\linux\\win-linux-share\\hadoop-common-2.2.0-bin");
//        try {
//            ugi.doAs(new PrivilegedExceptionAction<Void>() {
//                public Void run() throws Exception {
//                    Configuration conf = new Configuration();
//                    conf.set("fs.default.name", "hdfs://120.78.212.123:9000/");
//                    //conf.set("hadoop.job.ugi", "root");
//                    //以下两行是支持 hdfs的追加 功能的：hdfs.append()
//                    conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
//                    conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
//                    Path path = new Path("hdfs://120.78.212.123:9000/");
//                    //如果在本地测试，需要使用此种方法获取文件系统
//                    hdfs = FileSystem.get(path.toUri(), conf);
//                    //hdfs = path.getFileSystem(conf); // 这个也可以
//                    //如果在Hadoop集群下运行，使用此种方法可以直接获取默认文件系统
//                    //hdfs = FileSystem.get(conf); //这个不行，这样得到的hdfs所有操作都是针对本地文件系统，而不是针对hdfs的，原因不太清楚
//                    return null;
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    @Test
    public void test()throws Exception{
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection(
                "jdbc:hive2://120.78.212.123:10000/datatag", "root", "123456");
        Statement statement = con.createStatement();
//        ResultSet resultSet= statement.executeQuery("select * from  datatag");
//        resultSet.·
//        statement1.getc
//                resultSet.
//    System.out.println("123");
       /* String tableName = "jdbcTest";
        statement.execute("drop table if exists " + tableName);
        statement.execute("create table " + tableName +
                " (key int, value string)");
        System.out.println("Create table success!");
        // show tables
        String sql = "show tables '" + tableName + "'";
        System.out.println("Running: " + sql);
        ResultSet res = statement.executeQuery(sql);
        if (res.next()) {
            System.out.println(res.getString(1));
        }

        // describe table
        sql = "describe " + tableName;
        System.out.println("Running: " + sql);
        res = statement.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }


        sql = "select * from " + tableName;
        res = statement.executeQuery(sql);
        while (res.next()) {
            System.out.println(String.valueOf(res.getInt(1)) + "\t"
                    + res.getString(2));
        }

        sql = "select count(1) from " + tableName;
        System.out.println("Running: " + sql);
        res = statement.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }*/
    }
    // 创建hdfs目录
//    @Test
//    public void createDir() throws Exception {
//        Path path = new Path(dir);
//        if (hdfs.exists(path)) {
//            System.out.println("dir \t" + conf.get("fs.default.name") + dir
//                    + "\t already exists");
//            return;
//        }
//        hdfs.mkdirs(path);
//        System.out.println("new dir \t" + conf.get("fs.default.name") + dir);
//    }

//    /**
//     * 将数据插入hdfs中，用于load到hive表中，默认分隔符是"\001"
//     * @throws IOException
//     */
//    @Test
//    public void createFile() throws IOException {
//        List<List> argList = new ArrayList<>();
//        List<String> arg = new ArrayList<String>();
//        arg.add("12345");
//        arg.add("m");
//        argList.add(arg);
//        arg = new ArrayList<String>();
//        arg.add("54321");
//        arg.add("f");
//        Path dstPath = new Path(dir); //目标路径
//        //打开一个输出流
//        FSDataOutputStream outputStream = hdfs.append(dstPath);
//        StringBuffer sb = new StringBuffer();
//        for (List<String> recore : argList) {
//            for (String value : recore) {
//                sb.append(value).append("\001");
//            }
//            sb.deleteCharAt(sb.length() - 4);//去掉最后一个分隔符
//            sb.append("\n");
//        }
//        sb.deleteCharAt(sb.length() - 2);//去掉最后一个换行符
//        byte[] contents = sb.toString().getBytes();
//        outputStream.write(contents);
//        outputStream.close();
//        hdfs.close();
//        System.out.println("文件创建成功！");
//    }
}
