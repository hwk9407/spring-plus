package org.example.expert.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.image.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image/upload")
    public ResponseEntity<Map<String, Object>> imageUpload(@RequestParam("file") MultipartFile reqFile) {
        Map<String, Object> res = new HashMap<>();
        try {
            if (reqFile.isEmpty()) {
                res.put("error", "존재하지 않는 파일");
                return ResponseEntity.badRequest().body(res);
            }
            String fileUrl = imageService.imageUploadToS3(reqFile);
            res.put("uploaded", true);
            res.put("url", fileUrl);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            res.put("uploaded", false);
            res.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(res);
        } catch (Exception e) {
            res.put("uploaded", false);
            res.put("error", "파일 업로드 실패");
            return ResponseEntity.internalServerError().body(res);
        }
    }
    @DeleteMapping("/image/delete")
    public ResponseEntity<Map<String, Object>> imageDelete(@RequestParam("url") String imageUrl) {
        Map<String, Object> res = new HashMap<>();
        try {
            imageService.deleteImageFromS3(imageUrl);
            res.put("message", "이미지 삭제 성공");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("error", "이미지 삭제 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

    }
}
