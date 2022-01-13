package app.controllers;

import app.domain.Assignment;
import app.domain.Status;
import app.repository.DocumentRepository;
import app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Controller
@RequestMapping("/departmentspecialist")
@PreAuthorize("hasAuthority('departmentSpecialist')")
public class DepartmentSpecialistController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public void uploadDocument(@RequestParam(value = "id") Long id,
                               @RequestParam(name = "form") MultipartFile form,
                               Assignment assignment) {
        documentService.uploadDoc(form);
        assignment.setDocument(true);
    }

    @GetMapping
    public void putSignature(Assignment assignment) {
        documentRepository.findByIdAssigment(assignment.getId()).setSpecDepartmentSignature(true);
        assignment.setStatus(Collections.singleton(Status.acceptedForExecution));
    }

    @GetMapping
    public void deleteDocument(Assignment assignment){
        assignment.setDocument(false);
        Long id_doc = assignment.getId();
        documentRepository.delete(documentRepository.findByIdAssigment(id_doc));
    }
}
