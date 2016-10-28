package com.santex.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    void addFromWeb(MultipartFile image, String SKU);

    void addInBatch();

    void remove(String SKU);
}
