package MVC.service;

import MVC.domain.Document;
import MVC.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    @SneakyThrows
    public void uploadDoc(MultipartFile document) {
        var bytes = document.getBytes();
        if (bytes.length != 0) {
            var doc = new Document(document.getBytes());
            documentRepository.save(doc);
        }
    }
}
