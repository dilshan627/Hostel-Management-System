package bo.custom;

import bo.SuperBO;
import dto.StudentDTO;

public interface StudentBO extends SuperBO {
    public boolean add(StudentDTO studentDto) throws Exception;
}
