package com.hawkeye.capstone.service;

import com.hawkeye.capstone.base64.BASE64DecodedMultipartFile;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.*;

@Service
public class FileService {

    @Value("${app.upload.dir}") //yml 파일에 정의되어 있는 directory
    private String uploadDir;

    public String fileDownLoad(MultipartFile multipartFile){
        //separator: \, /과 같은 경로 분리, cleanPath: path 정규화(../ 내부 점들 사용 억제)
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));

        try{
            //inputStream을 가져와서
            //copyOfLocation(실제 저장 위치)에 파일을 쓴다
            //기존에 존재하면 REPLACE
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
            throw new IllegalStateException("Could not store file");
        }

        return copyOfLocation.toString();
    }

    public byte[] fileUpload(String fileName){

        Path readDir = Paths.get(fileName);

        try{

            File sourceImage = new File(fileName);
            byte[] bytes = new byte[(int)sourceImage.length()];
            FileInputStream fileInputStream = new FileInputStream(sourceImage);
            fileInputStream.read(bytes);

            return Base64.encodeBase64(bytes);

        }catch (Exception e){
            throw new IllegalStateException("이미지 불러오기 실패");
        }
    }
}
