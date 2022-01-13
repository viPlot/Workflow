package app.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignment {
    public Assignment(String name, User user) {
        this.name = name;
        this.idCreator = user;
        isDocument = false;
        status = Collections.singleton(Status.active);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creator")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User idCreator;

    @Column(name = "is_document")
    private boolean isDocument;

    @ElementCollection(targetClass = Status.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "assignment_status", joinColumns = @JoinColumn(name = "assignment_id"))
    @Enumerated(EnumType.STRING)
    private Set<Status> status;


}
