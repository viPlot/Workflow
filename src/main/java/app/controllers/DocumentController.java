package app.controllers;

import app.domain.Document;
import app.domain.Position;
import app.repository.DocumentRepository;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Objects;

@Controller
@RequestMapping("/main/editAssignment/document")
public class DocumentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/sign/{document}")
    @PreAuthorize("hasAuthority('departmentSpecialist') or ('headOfDepartment')")
    public void putSignature(@PathVariable Document document) { //подписать документ
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.director)))
            document.setDirectorSignature(true);
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.headOfDepartment)))
            document.setDepHeadSignature(true);
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.departmentSpecialist)))
            document.setSpecDepartmentSignature(true);
    }

    @GetMapping("delete/{document}")
    @PreAuthorize("hasAuthority('departmentSpecialist') or ('headOfDepartment')")
    public void deleteDocument(@PathVariable Document document) { //удалить документ
        documentRepository.delete(document);
    }
}