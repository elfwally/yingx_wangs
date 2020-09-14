package com.wang.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
/*
* 阿里云对象存储OSS工具 需要导入阿里云jar 创建存储空间  上传本地文件 byte字节文件  删除文件  下载到本地  上传网络流到阿里云  视频截取帧 工具
* */
public class AliyunOSSUtil {

    private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";  //看你阿里云oss的地址位置 有北京 上海等等
    //阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    //进入阿里云  个人资料  AccessKey 管理  里获取 accessKeyId  accessKeySecret
    private static String accessKeyId = "LTAI4G9GXGJuWrMhYhtJBynj";
    private static String accessKeySecret = "PswrBWKWBanj06bcqvOnmSH9dKTFkX";

    /*
    * 创建存储空间
    * 参数：
    * bucketName：存储空间名
    * */
    public static void insertBucket(String bucketName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
     * 上传本地文件
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     *   localFile: 本地文件路径
     * */
    public static void uploadLocalFiles(String bucketName,String fileName,String localFile){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, new File(localFile));
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
     * 上传byte字节数组
     * 参数：
     *   headImg:MultipartFile类型的文件
     *   bucketName：存储空间名
     *   fileName：文件名
     * */
    public static void uploadFileByte(MultipartFile headImg,String bucketName, String fileName){

        //转换为字节数组
        byte[] bytes =null;
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);
        // 上传Byte数组。
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /*
     * 文件下载到本地
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     *   localFile: 本地文件路径
     * */
    public  static void download(String bucketName,String fileName,String localFile){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, fileName), new File(localFile));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
    * 删除文件
    * 参数：
    *   bucketName：存储空间名
    *   fileName：文件名
    * */
    public static void deleteFile(String bucketName,String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, fileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }




    /*
     * 视频截取帧
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     * */
    public static URL videoInterceptCover(String bucketName, String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        //System.out.println(signedUrl);
        // 关闭OSSClient。
        ossClient.shutdown();
        return signedUrl;
    }

    /*
     * 上传网络流至阿里云
     * 参数：
     *   netPath:网络路径
     *   bucketName：存储空间名
     *   fileName：指定文件名
     * */
    public static void uploadNetFile(String netPath,String bucketName, String fileName) throws IOException {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = new URL(netPath).openStream();
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    //测试
    public static void main(String[] args) {
        //截取视频封面
        URL url = videoInterceptCover("yingx-192", "video/1594002361143-草原.mp4");

        //上传网络图片
        try {
            uploadNetFile(url.toString(),"yingx-192","cover/1594002361143-草原s.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(url);
    }
}
