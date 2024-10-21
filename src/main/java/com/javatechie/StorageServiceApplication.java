package com.javatechie;

import com.javatechie.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/image")
public class StorageServiceApplication {

	@Autowired
	private StorageService service;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
		String uploadImage = service.uploadImage(file);
		return ResponseEntity.status(HttpStatus.OK)
				.body(uploadImage);
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName){
		byte[] imageData=service.downloadImage(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);

	}
	
	
//	// Download image by filename
//    @GetMapping("/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
//        byte[] imageData = service.downloadImage(fileName);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageData);
//    }

    // Get image by ID
    @GetMapping("/id/{imageId}")
    public ResponseEntity<?> getImageById(@PathVariable Long imageId) {
        byte[] imageData = service.getImageById(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    // Get all image names
    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllImages() {
        List<String> imageList = service.getAllImages();
        return ResponseEntity.status(HttpStatus.OK).body(imageList);
    }

    // Update an existing image by filename
    @PutMapping("/{fileName}")
    public ResponseEntity<?> updateImage(@PathVariable String fileName, @RequestParam("image") MultipartFile file) throws IOException {
        String updatedImage = service.updateImage(fileName, file);
        return ResponseEntity.status(HttpStatus.OK).body(updatedImage);
    }

    // Delete image by filename
    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {
        String message = service.deleteImage(fileName);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

	public static void main(String[] args) {
		SpringApplication.run(StorageServiceApplication.class, args);
	}

}
