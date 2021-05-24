package com.hawkeye.capstone.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.hawkeye.capstone.base64.BASE64DecodedMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;


    @Value("${app.upload.dir}") //yml 파일에 정의되어 있는 directory
    private String uploadDir;

    @Value("${aws.s3.image.bucket")
    private String bucket;

    @Value("${aws.s3.image.bucket.url")
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

    //s3에 업로드
    public String fileUpload(MultipartFile multipartFile) {
        String originName = multipartFile.getOriginalFilename();
        String url = null;
        try {
            //확장자 찾기
            final String ext = originName.substring(originName.lastIndexOf('.'));
            //파일이름 암호화
            final String saveFileName = getUuid() + ext;
            //파일 객체 생성 (user.dir = 현재 작업 디렉토리)
            File file = new File(System.getProperty("user.dir") + saveFileName);
            //파일 변환
            multipartFile.transferTo(file);
            //S3파일 업로드
            uploadOnS3(saveFileName, file);
            //주소 할당
            url = bucketUrl + saveFileName;
            //파일 삭제
            file.delete();
        } catch (StringIndexOutOfBoundsException e) {
            url = null;
        } catch (IOException e) {
            log.error("IOException");
        }
        return url;
    }

    //업로드한 이미지 꺼내오기
    public byte[] fileDownload(String fileDir) {

        Path readDir = Paths.get(fileDir);

        try {

            File sourceImage = new File(fileDir);
            byte[] bytes = new byte[(int) sourceImage.length()];
            FileInputStream fileInputStream = new FileInputStream(sourceImage);
            fileInputStream.read(bytes);

            fileInputStream.close();

            return Base64.encodeBase64(bytes);

        } catch (Exception e) {
            throw new IllegalStateException("이미지 불러오기 실패");
        }
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String fileName, final File file) {

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
//        //AWS S3 전송 객체 생성
//        @Deprecated
//        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
//        //요청 객체 생성
//        final PutObjectRequest request = new PutObjectRequest(bucket, findName, file);
//        //업로드 시도
//        final Upload upload = transferManager.upload(request);
//
//        try{
//            upload.waitForCompletion();
//        }catch (AmazonClientException amazonClientException){
//            log.error(amazonClientException.getMessage());
//        }catch (InterruptedException e){
//            log.error(e.getMessage());
//        }
    }
}
