package com.openjava.datatag.utils.utils;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;


public class HiveUtils {
    private static FileSystem fs = null;
    @Before
    public void init()throws Exception{
        Configuration config = new Configuration();
        config.set("fs.defaultFS","http://120.78.212.123:9000/");
        fs = FileSystem.get(config);
    }
    @Test
    public void testUpload()throws Exception{
        fs.copyToLocalFile(new Path("D:/hdfstest.txt"),new Path("/"));
        fs.close();
    }
}
