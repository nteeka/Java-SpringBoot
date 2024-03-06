package com.example.demo.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/image")
public class ImageUploadController {
//	@RequestMapping(value="getImage/{photo}",method = RequestMethod.GET)
	@ResponseBody
	@GetMapping("/getImage/{image}")
	public ResponseEntity<ByteArrayResource>getImage(@PathVariable("image" )String img){
		if(!img.equals("") || img!=null)
			try {
				Path filename = Paths.get("uploads",img);
				byte[] buffer = Files.readAllBytes(filename);
				ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
				return ResponseEntity.ok()
						.contentLength(buffer.length)
						.contentType(MediaType.parseMediaType("image/png"))
						.body(byteArrayResource);
				
			}catch (Exception e) {
				// TODO: handle exception
			}
		return ResponseEntity.badRequest().build();
	}
}
