package app.controllers;

import app.domain.Assignment;
import app.domain.Position;
import app.domain.Status;
import app.domain.User;
import app.repository.AssignmentRepository;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    @Value("${error.message}")
    private String errorMessage;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"/main", "/"})
    public String main(@RequestParam Model model) {
        Iterable<Assignment> assignments = assignmentRepository.findAll();
        model.addAttribute("assignments", assignments);
        return "main";
    }

    @GetMapping("/main/addAssignment")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public String addAssignment(@PathVariable Assignment assignment, Model model) { //добавить поручение = статус active
        model.addAttribute("assignment", assignment);
            return "addAssignment";
    }

    @PostMapping
    public String saveAssignment(Model model,
            @RequestParam String name,
            @RequestParam("userId") User user) {
        Assignment assignment =  new Assignment(name, user);
        assignmentRepository.save(assignment);
        assignment.setStatus(Collections.singleton(Status.active));
        if (name != null && name.length() > 0) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUser = auth.getName();
            if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.director))) {
                assignment.setIdExecutor(userRepository.findByPositions(Position.headOfDepartment));
            }
            if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.headOfDepartment))) {
                assignment.setIdExecutor(userRepository.findByPositions(Position.departmentSpecialist));
            }
            return "redirect:/main";
        }
        else {
            model.addAttribute("errorMessage", errorMessage);
            return "addAssignment";
        }
    }
}