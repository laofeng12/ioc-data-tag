package com.openjava.datatag.utils.export;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.POIExcelBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.FileCopyUtils.BUFFER_SIZE;

public class ExportUtil {
//    private static final String EXPORT_FILE_DIR =ExportUtil.class.getResource("/").getPath()+"export_result/";
    private static final String EXPORT_FILE_DIR ="c:/export_result/";
    private static final String ZIP_DIR = "/zip";


    /**
     * 导出文件
     * @param data 导出数据，第一个为表头,第一个之后为数据
     * @param btype 业务类型（1标签与画像打标结果、....）
     * @param bid 业务id（标签模型id、...）
     * @param page 页码
     * @return String 文件本地路径
     * @throws Exception
     */
    public static String export(String[][] data,String btype,String bid,int page)throws Exception {
        String head []= data[0];
        String value[][]= new String[data.length-1][];
        System.arraycopy(data, 1, value, 0, data.length-1);
        if (data==null|| StringUtils.isBlank(btype)||StringUtils.isBlank(bid)){
            return null;
        }
        File filePath = new File( URLDecoder.decode(EXPORT_FILE_DIR,"utf-8")+btype+"/"+bid+"/");
        String fileName = bid+"-"+page+".xlsx";
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(filePath+"/"+fileName);
        try {
            POIExcelBuilder myBuilder = new POIExcelBuilder(outputStream);
            for (int i = 0; i < head.length; i++) {
                myBuilder.addProperty( head[i]);
            }
            myBuilder.buildSheet(bid+"-"+page, Arrays.asList(value));//放到第一个sheet
            //开始导出
            myBuilder.finish();
            System.out.println("导出成功");
            return filePath+"\\"+fileName;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            outputStream.close();
        }
    }

    /**
     * 压缩文件
     * @param btype 业务类型（1标签与画像打标结果、....）
     * @param bid 业务id（标签模型id、...）
     * @return String 压缩包文件本地路径
     * @throws Exception
     */
    public static String toZip(String btype,String bid) throws Exception{
        if (StringUtils.isBlank(btype)||StringUtils.isBlank(bid)){
            return null;
        }
        File filePath = new File(URLDecoder.decode(EXPORT_FILE_DIR,"utf-8")+btype+"/"+bid+"/");
        String zipFileName =  bid+".zip";
        List<File> srcFiles = new ArrayList<>();//等待压缩文件列表
        if (filePath.isDirectory()) {
            String[] filelist = filePath.list();
            for (int i = 0; i < filelist.length; i++) {
                srcFiles.add(new File(filePath +"/"+ filelist[i]));
            }
        }
        FileOutputStream outputStream = new FileOutputStream(filePath+"/"+zipFileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        FileInputStream in = null;
        try {
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zipOutputStream.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zipOutputStream.write(buf, 0, len);
                }
                zipOutputStream.setComment("我是注释");
            }
            zipOutputStream.finish();
            return filePath+"\\"+zipFileName;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            //关闭流
            outputStream.close();
            in.close();
            zipOutputStream.close();
        }
    }

    /**
     * 删除文件
     * @param btype
     * @param bid
     * @return
     */
    public static boolean deleFile(String btype,String bid) throws Exception{
        if (StringUtils.isBlank(btype)||StringUtils.isBlank(bid)){
            return false;
        }
        File filePath = new File(URLDecoder.decode(EXPORT_FILE_DIR,"utf-8")+btype+"/"+bid+"/");
        String[] filelist = filePath.list();
        for (int i = 0; i < filelist.length; i++) {
            File delfile = new File(filePath.getPath()+"/" + filelist[i]);
            if (!delfile.isDirectory()) {
                System.out.println(delfile.delete());
            } else if (delfile.isDirectory()) {
                deleFile(btype,bid);//递归删
            }
        }
        return true;
    }

    public static File getZipLocalFile(String btype,String bid) throws Exception{
        if (StringUtils.isBlank(btype)||StringUtils.isBlank(bid)){
            return null;
        }
        File zipLocalFile = new File(URLDecoder.decode(EXPORT_FILE_DIR,"utf-8")+btype+"/"+bid+"/"+bid+".zip");

        return zipLocalFile;
    }

    public static String uploadToFTP(String btype,String bid) throws Exception{
        if (StringUtils.isBlank(btype)||StringUtils.isBlank(bid)){
            return null;
        }
        File filePath = new File(URLDecoder.decode(EXPORT_FILE_DIR,"utf-8")+btype+"/"+bid+"/");
        String zipFileName =  bid+".zip";
        return null;
    }

    public static void main(String[] args) throws Exception{
        String[][] data =new String[2][2];
        data[0][0] = "a";
        data[0][1] = "b";

        data[1][0] = "1";
        data[1][1] = "2";
        System.out.println(ExportUtil.export(data,"1","123",3));
        System.out.println(ExportUtil.toZip("1","123"));
//        System.out.println(ExportUtil.deleFile("1","123"));
        File file = ExportUtil.getZipLocalFile("1","123");
    }



}
