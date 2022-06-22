package controller;

import bo.BOFactory;
import bo.BOType;
import bo.custom.impl.RoomBOImpl;
import bo.custom.impl.StudentBOImpl;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dto.RoomDTO;
import dto.StudentDTO;

import java.io.IOException;
import java.util.List;

public class RegistrationFormController {
    public JFXComboBox<String> cmbStudentId;
    public JFXComboBox<String> cmbRoomId;
    public JFXDatePicker cmbDate;

    private final StudentBOImpl studentBO = BOFactory.getInstance().getBO(BOType.Student);
    private final RoomBOImpl roomBO = BOFactory.getInstance().getBO(BOType.ROOM);
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXTextField txtDob;
    public JFXTextField txtGender;
    public JFXTextField txtType;
    public JFXTextField txtRent;
    public JFXTextField txtQty;

    public void initialize() {
        cmbStudentId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    List<StudentDTO> search = studentBO.search(newValue);
                    for (StudentDTO dto : search) {
                        txtName.setText(dto.getName());
                        txtAddress.setText(dto.getAddress());
                        txtContact.setText(dto.getContact());
                        txtDob.setText(dto.getDob());
                        txtGender.setText(dto.getGender());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cmbRoomId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    List<RoomDTO> search = roomBO.search(newValue);
                    for (RoomDTO dto : search) {
                        txtType.setText(dto.getType());
                        txtRent.setText(dto.getRent());
                        txtQty.setText(String.valueOf(dto.getQty()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        studentIdLoad();
        roomIdLoad();
    }

    private void studentIdLoad() {
        try {
            List<String> list = studentBO.StudentId();
            for (String Id : list) {
                cmbStudentId.getItems().add(Id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void roomIdLoad() {
        try {
            List<String> list = roomBO.roomId();
            for (String Id : list) {
                cmbRoomId.getItems().add(Id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
