package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student implements SuperEntity {
    @Id

    private String studentId;
    private String name;
    private String address;
    private String contact;
    private String dob;
    private String gender;

    @OneToMany(mappedBy = "student")
    private List<Reserve> arrayList = new ArrayList();

    public Student(String studentId, String name, String address, String contact, String dob, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.dob = dob;
        this.gender = gender;
    }


    public Student(String student) {
        this.studentId = student;
    }

}
