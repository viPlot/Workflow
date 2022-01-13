package app.controllers;

import app.domain.Assignment;
import app.domain.Status;
import app.domain.User;
import app.repository.AssignmentRepository;
import app.repository.DocumentRepository;
import app.repository.UserRepository;
import app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/headofdepartment")
@PreAuthorize("hasAuthority('headOfDepartment')")
public class HeadOfDepartmentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public void giveAssignment(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            Map<String, Object> model){
        Assignment assignment = new Assignment(name, user);
        assignmentRepository.save(assignment);

        Iterable<Assignment> assignments = assignmentRepository.findAll();
        model.put("assignments", assignments);
    }

    @GetMapping
    public void submitForRevision(Assignment assignment){
        documentRepository.findByIdAssigment(assignment.getId()).setDirectorSignature(false);
        assignment.setStatus(Collections.singleton(Status.active));
    }

    @GetMapping
    public void uploadDocument(@RequestParam(value = "id") Long id,
                               @RequestParam(name = "form") MultipartFile form,
                               Assignment assignment) {
        documentService.uploadDoc(form);
        assignment.setDocument(true);
    }

    @GetMapping
    public void putSignature(Assignment assignment) {
        documentRepository.findByIdAssigment(assignment.getId()).setDepHeadSignature(true);
        assignment.setStatus(Collections.singleton(Status.acceptedForExecution));
    }

    @GetMapping
    public void deleteDocument(Assignment assignment){
        documentRepository.delete(documentRepository.findByIdAssigment(assignment.getId()));
        assignment.setDocument(false);
    }

    @GetMapping("{assignment}")
    public void cancelAssignment(@PathVariable Assignment assignment){
        assignment.setStatus(Collections.singleton(Status.closed));
    }
}