package app.controllers;

import app.domain.Assignment;
import app.domain.Position;
import app.domain.Status;
import app.domain.User;
import app.repository.AssignmentRepository;
import app.repository.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Controller
public class MainController {
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Assignment> assignments = assignmentRepository.findAll();
        model.addAttribute("assignments", assignments);
        return "main";
    }

    @GetMapping("/main/editAssignment")
    @PreAuthorize("hasAuthority('headOfDepartment') or ('director')")
    public String addAssignment(@PathVariable Assignment assignment, Model model) { //добавить поручение = статус active
        model.addAttribute("assignment", assignment);
            return "editAssignment";
    }

    @PostMapping
    public String saveAssignment(@RequestParam String name,
                                 @RequestParam("userId") User user) {
        Assignment assignment =  new Assignment(name, user);
        assignmentRepository.save(assignment);
        assignment.setStatus(Collections.singleton(Status.active));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.director))) {
            assignment.setIdExecutor(userRepository.findByPositions(Collections.singleton(Position.headOfDepartment)));
        }
        if (Objects.equals(userRepository.findByEmail(currentUser).getPositions(), Collections.singleton(Position.headOfDepartment))) {
            assignment.setIdExecutor(userRepository.findByPositions(Collections.singleton(Position.departmentSpecialist)));
        }
        return "redirect:/main";
    }
}