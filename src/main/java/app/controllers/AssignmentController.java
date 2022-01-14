package app.controllers;

import app.domain.Assignment;
import app.domain.Position;
import app.domain.Status;
import app.repository.DocumentRepository;
import app.repository.UserRepository;
import app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Objects;

@Controller
@RequestMapping("/main/editAssignment")
public class AssignmentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("{editAssignment}")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public String cancelAssignment(@RequestParam Assignment assignment) { //отменить поручение = статус cancelled
        assignment.setStatus(Collections.singleton(Status.closed));
        return "/main/editAssignment";
    }

    @GetMapping("{editAssignment}")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public void submitForRevision(@RequestParam Assignment assignment) { //передать поручение = не подписать документ (статус active)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.director))) {
            documentRepository.findByIdAssigment(assignment.getId()).setDirectorSignature(false);
            assignment.setIdExecutor(userRepository.findByPositions(Collections.singleton(Position.headOfDepartment)));
        }
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.headOfDepartment))){
            documentRepository.findByIdAssigment(assignment.getId()).setDepHeadSignature(false);
            assignment.setIdExecutor(userRepository.findByPositions(Collections.singleton(Position.departmentSpecialist)));
        }
        assignment.setStatus(Collections.singleton(Status.active));
    }

    @GetMapping("document")
    @PreAuthorize("hasAuthority('departmentSpecialist') or ('headOfDepartment')")
    public void uploadDocument(@RequestParam(name = "form")MultipartFile form) {//загрузить документ
        documentService.uploadDoc(form);
    }
}
