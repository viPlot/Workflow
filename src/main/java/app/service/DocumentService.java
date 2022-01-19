package app.service;

import app.domain.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    Document uploadDoc(MultipartFile document);
}
