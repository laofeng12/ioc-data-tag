package com.openjava.datatag.component;


import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.ljdp.common.ftp.ApacheFTPClient;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Vector;

/**
 * @author: zmk
 * @Date: 2019/10/10 09:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "ljdp.ftpserver")
public class FtpUtil {
    private transient Logger log = LoggerFactory.getLogger(this.getClass());
    private String url ;
    private int port;
    private String mode;
    private String username;
    private String password;
    private String remotePath;
    private String localTempPath;
    private String localPath;

    public ApacheFTPClient ftpClient = null;
    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        try{
            ftpClient = new ApacheFTPClient(url, port, username, password, mode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 上传文件
     * @param pathname ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     *  @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile( String pathname, String fileName,String originfilename){
        InputStream inputStream = null;
        try{
            System.out.println("开始上传文件");
            inputStream = new FileInputStream(new File(originfilename));
            initFtpClient();
            //初始化目录
            System.out.println(ftpClient.makeDirectory(pathname));
            System.out.println(ftpClient.changeWorkingDirectory(pathname));

            boolean ossres = ftpClient.uploadFile(fileName, inputStream);
            if(!ossres) {
                System.out.println("Upload FTP fail："+fileName);
                System.out.println("Reply="+ftpClient.getFtp().getReply());
                System.out.println("ReplyCode="+ftpClient.getFtp().getReplyCode());
                System.out.println("ReplyString="+ftpClient.getFtp().getReplyString());
                throw new APIException(APIConstants.FTP_UPLOAD_FAIL, "上传FTP失败");
            }
            System.out.println("[FTP]upload:"+fileName+","+ossres);
        }catch (Exception e) {
            System.out.println("上传文件失败");
            e.printStackTrace();
        }finally{
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ftpClient.close();

        }
        return true;
    }

    /**
     * 下载文件
     * @param pathname FTP服务器文件目录 *
     * @param fileName 文件名称 *
     * @param localpath 下载后的文件路径 *
     * @return
     * */
    public  boolean downloadFile(String pathname, String fileName, String localpath){
        try {
            initFtpClient();
            //初始化目录
            System.out.println(ftpClient.changeWorkingDirectory(pathname));
            boolean ossres = ftpClient.downloadFile(fileName,localpath);
            if(!ossres) {
                System.out.println("Upload FTP fail："+fileName);
                System.out.println("Reply="+ftpClient.getFtp().getReply());
                System.out.println("ReplyCode="+ftpClient.getFtp().getReplyCode());
                System.out.println("ReplyString="+ftpClient.getFtp().getReplyString());
                throw new APIException(APIConstants.FTP_UPLOAD_FAIL, "上传FTP失败");
            }
            System.out.println("[FTP]dowload:"+fileName+","+ossres);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("下载失败");
        }finally {
            ftpClient.close();
        }
        return true;
    }

    /**
     * 下载文件
     * @param pathname FTP服务器文件目录 *
     * @param fileName 文件名称 *
     * @param outputStream 输出流 *
     * @return
     * */
    public  boolean downloadFile(String pathname, String fileName, OutputStream outputStream){
        try {
            initFtpClient();
            //初始化目录
            System.out.println(ftpClient.changeWorkingDirectory(pathname));
            boolean ossres = ftpClient.downloadFile(fileName,outputStream);
            if(!ossres) {
                System.out.println("Upload FTP fail："+fileName);
                System.out.println("Reply="+ftpClient.getFtp().getReply());
                System.out.println("ReplyCode="+ftpClient.getFtp().getReplyCode());
                System.out.println("ReplyString="+ftpClient.getFtp().getReplyString());
                throw new APIException(APIConstants.FTP_UPLOAD_FAIL, "上传FTP失败");
            }
            System.out.println("[FTP]dowload:"+fileName+","+ossres);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("下载失败");
        }finally {
            ftpClient.close();
        }
        return true;
    }

    /**
     * 删除文件
     * * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return
     * */
    public boolean deleteFile(String pathname, String filename){

        return false;
    }


    public static void main(String[] args) {
        FtpUtil ftp =new FtpUtil();
//        ftp.deleteFile("/");
        ftp.uploadFile("ioc-datatag", "1677352.zip", "C:\\export_result\\1\\1677352\\1677352.zip");
        ftp.downloadFile("ioc-datatag", "1677352.zip", "f:\\1677352.zip");
//        ftp.deleteFile("ioc_datatag/model_result", "1677352.zip");
    }

}
