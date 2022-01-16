package app.controllers;

import app.domain.Assignment;
import app.domain.Position;
import app.domain.Status;
import app.repository.DocumentRepository;
import app.repository.UserRepository;
import app.service.DocumentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/main/editAssignment")
public class AssignmentController {

    private final UserRepository userRepository;

    private final DocumentServiceImpl documentServiceImpl;

    private final DocumentRepository documentRepository;

    @GetMapping("/cancel/{assignment}")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public String cancelAssignment(@PathVariable Assignment assignment) { //отменить поручение = статус cancelled
        assignment.setStatus(Collections.singleton(Status.closed));
        return "/main/editAssignment";
    }

    @GetMapping("/submit/{assignment}")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public void submitForRevision(@PathVariable Assignment assignment) { //передать поручение = не подписать документ (статус active)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.director))) {
            documentRepository.findByIdAssigment(assignment.getId()).setDirectorSignature(false);
            assignment.setIdExecutor(userRepository.findByPositions(Position.headOfDepartment));
        }
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.headOfDepartment))){
            documentRepository.findByIdAssigment(assignment.getId()).setDepHeadSignature(false);
            assignment.setIdExecutor(userRepository.findByPositions(Position.departmentSpecialist));
        }
        assignment.setStatus(Collections.singleton(Status.active));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('departmentSpecialist') or ('headOfDepartment')")
    public void uploadDocument(@RequestParam(name = "form")MultipartFile form) {//загрузить документ
        documentServiceImpl.uploadDoc(form);
    }
}