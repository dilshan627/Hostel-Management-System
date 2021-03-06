package controller;

import bo.BOFactory;
import bo.BOType;
import bo.custom.impl.StudentBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dto.StudentDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.tm.StudentTm;

import java.time.LocalDate;
import java.util.ArrayList;

public class StudentFormController {

    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXComboBox<String> cmdGender;
    public JFXButton btnAdd;

    public JFXDatePicker txtDob;
    public TableView<StudentTm> tblStudent;

    private final StudentBOImpl studentBO = BOFactory.getInstance().getBO(BOType.Student);
    public JFXTextField txtId;

    public void initialize() {
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dob"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("gender"));
        cmdGender.getItems().add("Male");
        cmdGender.getItems().add("Female");

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setText(newValue != null ? "Update" : "Add");
            if (newValue != null) {
                txtId.setText(newValue.getStudentId());
                txtName.setText(newValue.getName());
                txtAddress.setText(newValue.getAddress());
                txtContact.setText(newValue.getContact());
                txtDob.setValue(LocalDate.parse(newValue.getDob()));
                cmdGender.setValue(newValue.getGender());

            }
        });

        loadAllStudent();
    }

    private void loadAllStudent() {
        tblStudent.getItems().clear();

        ArrayList<StudentDTO> allStudent = null;
        try {
            allStudent = studentBO.getAll();
            for (StudentDTO student : allStudent) {
                tblStudent.getItems().add(new StudentTm(student.getStudentId(), student.getName(), student.getAddress(), student.getContact(), student.getDob(), student.getGender()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void addOnAction(ActionEvent actionEvent) {
        if (!txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() && !txtContact.getText().isEmpty() && !cmdGender.getValue().isEmpty()) {
            String code = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String contact = txtContact.getText();
            String dob = String.valueOf(txtDob.getValue());

            if (!name.matches("^[A-Za-z]+$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtName.requestFocus();
                return;
            } else if (!address.matches("^[A-Za-z]+$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtAddress.requestFocus();
                return;
            } else if (!contact.matches("^(?:7|0|(?:\\+94))[0-9]{9}$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtContact.requestFocus();
                return;
            } else if (!code.matches("^S00-[0-9]{4}$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtId.requestFocus();
                return;
            }


            if (btnAdd.getText().equalsIgnoreCase("Add")) {
                try {
                    studentBO.add(new StudentDTO(txtId.getText(), txtName.getText(), txtAddress.getText(), txtContact.getText(), String.valueOf(txtDob.getValue()), cmdGender.getValue()));
                    new Alert(Alert.AlertType.CONFIRMATION, "Add student").show();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                try {
                    studentBO.update(new StudentDTO(txtId.getText(), txtName.getText(), txtAddress.getText(), txtContact.getText(), dob, cmdGender.getValue()));
                    new Alert(Alert.AlertType.CONFIRMATION, "update student").show();
                    StudentTm selectedStudent = tblStudent.getSelectionModel().getSelectedItem();
                    selectedStudent.setStudentId(txtId.getText());
                    selectedStudent.setName(txtName.getText());
                    selectedStudent.setAddress(txtAddress.getText());
                    selectedStudent.setContact(txtContact.getText());
                    selectedStudent.setDob(dob);
                    selectedStudent.setGender(cmdGender.getValue());
                    tblStudent.refresh();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {

        }
        loadAllStudent();

    }

    public void newOnAction(ActionEvent actionEvent) {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtDob.getEditor().clear();
        btnAdd.setText("Add");
        cmdGender.setValue("");
        txtId.requestFocus();
        tblStudent.getSelectionModel().clearSelection();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        if (!txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() && !txtContact.getText().isEmpty() && !cmdGender.getValue().isEmpty()) {
            String code = tblStudent.getSelectionModel().getSelectedItem().getStudentId();

            try {
                studentBO.delete(code);
                new Alert(Alert.AlertType.CONFIRMATION, "delete student").show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tblStudent.getItems().remove(tblStudent.getSelectionModel().getSelectedItem());
            tblStudent.getSelectionModel().clearSelection();

        }
    }


}
