package com.linghong.dongdong.utils;



import com.linghong.dongdong.bean.Base64MultipartFile;
import com.linghong.dongdong.bean.FastDfsFile;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class FastDfsUtil {
    private static Logger logger = LoggerFactory.getLogger(FastDfsUtil.class);

    private TrackerClient tClient;
    private TrackerServer tServer;
    private StorageServer storageServer = null;
    private StorageClient storageClient;

    public FastDfsUtil() {
        //初始化客户端配置文件，指定一个Tracker服务调度节点
        try {
            ClientGlobal.init("fdfs_client.conf");
            //创建Tracker客户端对象
            tClient = new TrackerClient();
            //创建Tracker服务端对象
            tServer = tClient.getConnection();
            //通过Tracker server 和 Storage server 构造一个 storage clinet 对象
            storageClient = new StorageClient(tServer, storageServer);
        } catch (Exception e) {
            System.out.println("error:初始化图片服务器失败");
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 返回上传的信息
     *
     * @param image
     * @return
     * @throws Exception
     */
    public String uploadImage(FastDfsFile image) throws Exception {
        //利用storageClient对象，调用上传文件的方法
        String[] result = storageClient.upload_file(image.getContent(), image.getExt(), null);
        //获取回显信息
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(result[0] + "/" + result[1]);
        return stringBuffer.toString();
    }

    /**
     * 根据MultipartFile 类型
     *
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
        String imageName = file.getOriginalFilename();
        String extName = imageName.substring(imageName.lastIndexOf(".") + 1);
        try {
            FastDfsFile fastDfsFile = new FastDfsFile(imageName, file.getBytes(), extName);
            return uploadImage(fastDfsFile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片base64字符串
     *
     * @param base64Image
     * @return
     */
    public String uploadBase64Image(String base64Image) {
        try {
            //按照 , 进行分割
            String[] imageData = base64Image.split(",");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(imageData[1]);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            String imageType = imageData[0].split(";")[0].split(":")[1].split("/")[1];
            String imageName = IDUtil.getCode();
            FastDfsFile fastDfsFile = new FastDfsFile(imageName, bytes, imageType);
            return uploadImage(fastDfsFile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片base64字符串数组
     *
     * @param base64Images
     * @return
     */
    public String[] uploadBase64Images(String[] base64Images) {
        try {
            String[] urls = new String[base64Images.length / 2];
            String base64Image = null;
            String imageType = null;
            //接收过来的数组 已经以  , 分隔开
            for (int i = 0; i < base64Images.length; i++) {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] b = new byte[0];
                if (i % 2 == 0) {
                    imageType = base64Images[i].split(";")[0].split(":")[1].split("/")[1];
                } else {
                    base64Image = base64Images[i];
                    b = decoder.decode(base64Image);
                    for (int j = 0; i < b.length; ++i) {
                        if (b[j] < 0) {
                            b[j] += 256;
                        }
                    }
                }
                FastDfsFile fastDfsFile = new FastDfsFile(IDUtil.getCode(), b, imageType);
                if (i % 2 == 1 && imageType != null & base64Image != null) {
                    urls[i / 2] = uploadImage(fastDfsFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据图片base64字符串
     *
     * @param base64Image
     * @return
     */
    public String uploadImage(String base64Image) {
        MultipartFile image = base64ToMultipart(base64Image);
        String imageName = image.getOriginalFilename();
        String extName = imageName.substring(imageName.lastIndexOf(".") + 1);
        try {
            FastDfsFile fastDfsFile = new FastDfsFile(imageName, image.getBytes(), extName);
            return uploadImage(fastDfsFile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStr = base64.split(",");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(baseStr[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64MultipartFile(b, baseStr[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
