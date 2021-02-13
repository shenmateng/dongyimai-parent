package com.offcn.shop.controller;

import com.offcn.entity.Result;
import com.offcn.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Controller
 *
 * @author Administrator
 */
@RestController
public class UpLoadController {
    //<input type="file" >
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;   //文件服务器地址

    @RequestMapping("/upload")
    public Result upLoadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();//拿到从页面传过来的文件名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);//文件扩展名
        try {
            //获取FastDFSClient客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //拿到文件路径
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //4、拼接返回的 url 和 ip 地址，拼装成完整的 url
            String url = FILE_SERVER_URL + path;
            System.out.println(url);
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }

    }
}
