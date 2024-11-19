package org.example.expert.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.S3Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final long MAX_FILE_SILE = 1024 * 1024; // 1MB

    public String imageUploadToS3(MultipartFile reqFile) {
        // 이미지 파일 크기 체크
        if (reqFile.getSize() > MAX_FILE_SILE) {
            throw new IllegalArgumentException("이미지의 크기가 1MB를 넘을 수 없습니다.");
        }
        String filename = reqFile.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase()
                ;
        // 이미지 확장자 체크 (jpg, jpeg, png만 허용)
        if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))) {
            throw new IllegalArgumentException("지원되지 않는 확장자입니다. jpg, jpeg, png만 업로드 가능합니다.");
        }

        String uuidFileName = UUID.randomUUID() + "." + ext;
        InputStream inputStream;
        try {
            inputStream = reqFile.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(reqFile.getSize());
            amazonS3Client.putObject(new PutObjectRequest(bucket, uuidFileName, inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, uuidFileName).toString();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void deleteImageFromS3(String fileUrl) {
        try {
            String key = fileUrl.split("/")[3];
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        } catch (Exception e) {
            throw new RuntimeException("S3에서 파일 삭제 중 오류 발생했습니다.");
        }
    }
}
