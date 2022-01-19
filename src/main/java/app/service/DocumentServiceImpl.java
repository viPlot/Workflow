package app.service;

import app.domain.Document;
import app.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;

    @Override
    @SneakyThrows
    public Document uploadDoc(MultipartFile document) {
        var bytes = document.getBytes();
        if (bytes.length != 0) {
            var doc = new Document(document.getBytes());
            documentRepository.save(doc);
            doc.setFilename(document.getName());
            return doc;
        }
        return null;
    }
}
