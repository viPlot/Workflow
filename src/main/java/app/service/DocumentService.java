package app.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    void uploadDoc(MultipartFile document);
}
