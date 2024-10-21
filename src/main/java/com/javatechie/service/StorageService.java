package com.javatechie.service;

import com.javatechie.entity.ImageData;
import com.javatechie.respository.StorageRepository;
import com.javatechie.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public String uploadImage(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage(String fileName){
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
    
    
 // Get image by ID
    public byte[] getImageById(Long id) {
        Optional<ImageData> dbImageData = repository.findById(id);
        return dbImageData.map(data -> ImageUtils.decompressImage(data.getImageData())).orElse(null);
    }

    // Get all image names
    public List<String> getAllImages() {
        List<ImageData> imageList = repository.findAll();
        return imageList.stream().map(ImageData::getName).collect(Collectors.toList());
    }

    // Update image by filename
    public String updateImage(String fileName, MultipartFile file) throws IOException {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        if (dbImageData.isPresent()) {
            ImageData updatedImageData = dbImageData.get();
            updatedImageData.setImageData(ImageUtils.compressImage(file.getBytes()));
            updatedImageData.setType(file.getContentType());
            repository.save(updatedImageData);
            return "Image updated successfully: " + fileName;
        }
        return "Image not found: " + fileName;
    }

    // Delete image by filename
    @Transactional
    public String deleteImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        if (dbImageData.isPresent()) {
            repository.deleteByName(fileName);
            return "Image deleted successfully: " + fileName;
        }
        return "Image not found: " + fileName;
    }
}
