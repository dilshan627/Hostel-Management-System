package controller;

import bo.BOFactory;
import bo.BOType;
import bo.custom.impl.RoomBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.RoomDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.tm.RoomTM;

import java.util.ArrayList;

public class RoomFormController {
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public JFXComboBox<String> txtType;

    public TableView<RoomTM> tblRoom;
    public JFXButton btnAdd;

    private final RoomBOImpl roomBO = BOFactory.getInstance().getBO(BOType.ROOM);
    public JFXTextField txtId;

    public void initialize() {
        tblRoom.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("roomId"));
        tblRoom.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        tblRoom.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rent"));
        tblRoom.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        txtType.getItems().add("AC");
        txtType.getItems().add("AC/Food ");
        txtType.getItems().add("Non-AC");
        txtType.getItems().add("Non-AC/Food");
        tblRoom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setText(newValue != null ? "Update" : "Add");
            if (newValue != null) {
                txtId.setText(newValue.getRoomId());
                txtType.setValue(newValue.getType());
                txtPrice.setText(newValue.getRent());
                txtQty.setText(String.valueOf(newValue.getQty()));
            }
        });
        loadAllRoom();
    }

    private void loadAllRoom() {
        tblRoom.getItems().clear();
        ArrayList<RoomDTO> allRoom = null;
        try {
            allRoom = roomBO.getAll();
            for (RoomDTO room : allRoom) {
                tblRoom.getItems().add(new RoomTM(room.getRoomId(), room.getType(), room.getRent(), room.getQty()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        if (!txtPrice.getText().isEmpty() && !txtQty.getText().isEmpty() && !txtType.getValue().isEmpty()) {
            int qty = Integer.parseInt(txtQty.getText());
            String id = txtId.getText();
            String price = txtPrice.getText();
            String qtt = txtQty.getText();


            if (!id.matches("^RM-[0-9]{4}$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtId.requestFocus();
                return;
            } else if (!price.matches("^[0-9]{1,}.00$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtPrice.requestFocus();
                return;
            } else if (!qtt.matches("^[0-9]{1,}$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid").show();
                txtQty.requestFocus();
                return;
            }


            if (btnAdd.getText().equalsIgnoreCase("Add")) {
                try {
                    roomBO.add(new RoomDTO(txtId.getText(), txtType.getValue(), txtPrice.getText(), qty));
                    new Alert(Alert.AlertType.CONFIRMATION, "Add room").show();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    roomBO.update(new RoomDTO(txtId.getText(), txtType.getValue(), txtPrice.getText(), qty));
                    new Alert(Alert.AlertType.CONFIRMATION, "update room").show();
                    RoomTM selectedRoom = tblRoom.getSelectionModel().getSelectedItem();
                    selectedRoom.setRoomId(txtId.getText());
                    selectedRoom.setType(txtType.getValue());
                    selectedRoom.setRent(txtPrice.getText());
                    selectedRoom.setQty(qty);
                    tblRoom.refresh();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            System.out.println("kk");
        }

        loadAllRoom();
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        txtId.clear();
        txtPrice.clear();
        txtQty.clear();
        txtType.setValue("");
        btnAdd.setText("Add");
        txtId.requestFocus();
        tblRoom.getSelectionModel().clearSelection();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        if (!txtPrice.getText().isEmpty() && !txtQty.getText().isEmpty() && !txtType.getValue().isEmpty()) {
            String code = txtId.getText();
            try {
                roomBO.delete(code);
                new Alert(Alert.AlertType.CONFIRMATION, "delete room").show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tblRoom.getItems().remove(tblRoom.getSelectionModel().getSelectedItem());
            tblRoom.getSelectionModel().clearSelection();
        }
    }

}
