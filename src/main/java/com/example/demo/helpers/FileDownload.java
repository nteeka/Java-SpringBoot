package com.example.demo.helpers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
public class FileDownload {
	// Thay đổi đường dẫn đến thư mục lưu trữ file tải xuống tùy thuộc vào cấu hình của bạn
    private final Path fileStoragePath = Paths.get("fileUploads");

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // Xây dựng đường dẫn tới file
        Path filePath = fileStoragePath.resolve(fileName);
        Resource resource;
        try {
            // Kiểm tra xem file có tồn tại không
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                // Nếu file tồn tại và có thể đọc được, trả về response để người dùng tải xuống
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Nếu file không tồn tại hoặc không thể đọc được, trả về response lỗi 404
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Nếu có lỗi xảy ra, trả về response lỗi 500
            return ResponseEntity.status(500).build();
        }
    }
}
