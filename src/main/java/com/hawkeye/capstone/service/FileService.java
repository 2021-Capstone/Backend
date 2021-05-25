package com.hawkeye.capstone.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.hawkeye.capstone.base64.BASE64DecodedMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.*;
import java.util.UUID;

import static com.amazonaws.util.IOUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;


    @Value("${app.upload.dir}") //yml 파일에 정의되어 있는 directory
    private String uploadDir;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String bucketUrl;

    //로컬 파일에 업로드
//    public String fileUpload(MultipartFile multipartFile){
//        //separator: \, /과 같은 경로 분리, cleanPath: path 정규화(../ 내부 점들 사용 억제)
//        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));
//
//        try{
//            //inputStream을 가져와서
//            //copyOfLocation(실제 저장 위치)에 파일을 쓴다
//            //기존에 존재하면 REPLACE
//            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
//        }catch (IOException e){
//            e.printStackTrace();
//            throw new IllegalStateException("Could not store file");
//        }
//
//        return copyOfLocation.toString();
//    }

//    s3에 업로드
    public String fileUpload(MultipartFile multipartFile) {
        String originName = multipartFile.getOriginalFilename();
//        String url = null;
        try {
            //확장자 찾기
            final String ext = originName.substring(originName.lastIndexOf('.'));
            //파일이름 암호화
            String saveFileName = getUuid() + ext;
            //파일 객체 생성 (user.dir = 현재 작업 디렉토리)
            File file = new File(System.getProperty("user.dir") + saveFileName);
            //파일 변환
            multipartFile.transferTo(file);
            //S3파일 업로드
            uploadOnS3(saveFileName, file);
            //주소 할당
//            url = bucketUrl + saveFileName;
            //파일 삭제
            file.delete();
            return saveFileName;
        } catch (StringIndexOutOfBoundsException e) {
            log.error("StringIndexOutOfBoundsException");
        } catch (IOException e) {
            log.error("IOException");
        }
        return null;
    }

    //업로드한 이미지 꺼내오기
    public byte[] fileDownload(String fileName) {

        try {

            S3Object object = amazonS3.getObject(bucket, fileName);

            S3ObjectInputStream objectContent = object.getObjectContent();

            byte[] bytes = toByteArray(objectContent);
//            File sourceImage = new File(fileName);
//            FileOutputStream fileOutputStream = new FileOutputStream(sourceImage);
//
//            byte[] read_buf = new byte[1024];
//            int read_len = 0;
//            while ((read_len = objectContent.read(read_buf)) >= 0) {
//                fileOutputStream.write(read_buf, 0, read_len);
//            }

            objectContent.close();
//            fileOutputStream.close();

            return bytes;

        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException");
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException");
        } catch (IOException e) {
            throw new IllegalStateException("IOException");
        }
        return null;
    }


    private byte[] fileToByte(File file){
        try {
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);

            fileInputStream.close();

            return Base64.encodeBase64(bytes);

        } catch (Exception e) {
            throw new IllegalStateException("이미지 불러오기 실패");
        }
    }

    //로컬 파일에 업로드한 이미지 꺼내오기
//    public byte[] fileDownload(String fileName) {
//
//        Path readDir = Paths.get(fileName);
//
//        try {
//            File sourceImage = new File(fileName);
//            byte[] bytes = new byte[(int) sourceImage.length()];
//            FileInputStream fileInputStream = new FileInputStream(sourceImage);
//            fileInputStream.read(bytes);
//
//            fileInputStream.close();
//
//            return Base64.encodeBase64(bytes);
//
//        } catch (Exception e) {
//            throw new IllegalStateException("이미지 불러오기 실패");
//        }
//    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String fileName, final File file) {

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

    }
}
