package com.cycle.reggie_take_out.controller;

import com.cycle.reggie_take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用控制器
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path.upload}")
    private String uploadPath;
    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("上传文件：{}",file.getOriginalFilename());
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid作为文件名，防止生成的临时文件重复
        String fileName = UUID.randomUUID().toString()+ suffix;
        try{
            file.transferTo(new File(uploadPath+fileName));
        } catch (IOException e){
            log.error("上传文件失败：{}",e.getMessage(),e);
            return R.error("上传失败");
        }
        return R.success(fileName);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try{
            //从图片存储位置获取用户下载的图片
            FileInputStream fileInputStream = new FileInputStream(uploadPath + name);
            //通过输出流将图片写到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            //设置回写文件类型
            response.setContentType("image/jpeg");
            //定义缓存区
            int len = 0;
            byte[] buffer = new byte[1024];
            //循环将缓存区的数据写到浏览器
            while((len = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
                outputStream.flush();
            }
            //关闭流
            outputStream.close();
            fileInputStream.close();
        }catch(IOException e){
            log.error("下载文件失败：{}",e.getMessage(),e);
        }
    }
}
