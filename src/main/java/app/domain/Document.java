package app.domain;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "status")
    private boolean status;

    @OneToOne
    @JoinColumn(name = "id_assigment")
    private Assignment idAssigment;

    @Column(name = "director_signature")
    private boolean directorSignature;

    @Column(name = "department_head_signature")
    private boolean depHeadSignature;

    @Column(name = "specialist_department_signature")
    private boolean specDepartmentSignature;

    @Column(name = "file")
    private byte[] data;

    public Document(byte[] data) {
        this.data = data;
    }
}
