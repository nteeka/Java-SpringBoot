package com.example.demo.helpers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cloudinary.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.cloudinary.utils.ObjectUtils;

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
    
    @GetMapping("/downloadFileCloud/{publicId}")
    @ResponseBody
    public byte[] downloadFileCloud(@PathVariable String publicId) throws IOException {
        // Replace 'your_cloud_name', 'your_api_key', and 'your_api_secret' with your Cloudinary credentials
        String cloudName = "dccmckgvc";
        String apiKey = "891328625785465";
        String apiSecret = "szFBRogObiQHosinNgfK9pA1W0I";
        
        // Replace 'your_cloud_name' with your Cloudinary cloud name
        String url = "https://res.cloudinary.com/" + cloudName + "/raw/upload/" + publicId;

        // Create a RestTemplate to send HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Send a GET request to download the file
        return restTemplate.getForObject(url, byte[].class);
    }

}
