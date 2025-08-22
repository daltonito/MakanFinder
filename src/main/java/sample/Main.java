package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // Create static  labels
    Label label1 = new Label("Hardware or Part number:");
    Button searchButton = new Button("Search");
    Label moduleTypeLabel = new Label("Module Type:");
    Label hardwareLabel = new Label("Hardware:");
    Label vehicleLabel = new Label("Vehicle Group:");
    Label ecuManufacturerLabel = new Label("ECU Manufacturer:");
    Label cloningLabel = new Label("Cloning:");

    Label cloningToolsLabel = new Label("Cloning Tools:");
    Label authorLabel = new Label("© Powered by Martin C.");

    // Create dynamic labels/fields
    final TextField searchField = new TextField();
    final Label moduleTypeField = new Label();
    final Label hardwareField = new Label();
    final Label vehicleField = new Label();
    final Label ecuManufacturerField = new Label();
    final Label cloningField = new Label();
    final Label cloningToolsField = new Label();

    // create the popup messages
    Alert alertShortInput;
    Alert alertNotFound;

    public void initErrorShortInput() {
        alertShortInput = new Alert(Alert.AlertType.WARNING);
        alertShortInput.initModality(Modality.APPLICATION_MODAL);
        alertShortInput.setTitle("Invalid input");
        alertShortInput.setHeaderText(null);
        alertShortInput.setContentText("The search input value is too short!\nPlease enter the full part number.");
    }

    public void initErrorNotFound() {
        alertNotFound = new Alert(Alert.AlertType.ERROR);
        alertNotFound.initModality(Modality.APPLICATION_MODAL);
        alertNotFound.setTitle("Not found");
        alertNotFound.setHeaderText(null);
        alertNotFound.setContentText("The part number entered is not found!\nPlease contact RMA prescreen team for more information.");
    }

    private void performSearch() {

        try {

            moduleTypeField.setText("");
            hardwareField.setText("");
            vehicleField.setText("");
            ecuManufacturerField.setText("");
            cloningField.setText("");
            cloningToolsField.setText("");

            if (searchField.getText().length() < 4) {
                alertShortInput.showAndWait();
                searchField.clear();
                return;
            }

            ReturnedRow row = ReturnedRow.retrieveByKeyword(searchField.getText());

            if (row.isNoResultFound()) {
                alertNotFound.showAndWait();
                searchField.clear();
                return;
            }


            moduleTypeField.setText(row.moduleType);
            hardwareField.setText(row.hardware);
            vehicleField.setText(row.vehicleGroup);
            ecuManufacturerField.setText(row.ecuManufacturer);
            cloningField.setText(row.cloning);
            cloningToolsField.setText(row.cloningTools);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        cloningLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        authorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        cloningField.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));

        initErrorNotFound();
        initErrorShortInput();

        searchButton.setOnAction(event -> performSearch());
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal gap between cells
        gridPane.setVgap(10); // Vertical gap between cells
        gridPane.setPadding(new Insets(20));

        gridPane.add(label1, 0, 0);
        gridPane.add(searchField, 1, 0);
        gridPane.add(searchButton, 2, 0);

        gridPane.add(cloningLabel, 0, 1);
        gridPane.add(cloningField, 1, 1);

        gridPane.add(new Label(""), 1, 2);

        gridPane.add(moduleTypeLabel, 0, 3);
        gridPane.add(moduleTypeField, 1, 3);

        gridPane.add(hardwareLabel, 0, 4);
        gridPane.add(hardwareField, 1, 4);

        gridPane.add(vehicleLabel, 0, 5);
        gridPane.add(vehicleField, 1, 5);

        gridPane.add(ecuManufacturerLabel, 0, 6);
        gridPane.add(ecuManufacturerField, 1, 6);

        gridPane.add(cloningToolsLabel, 0, 7);
        gridPane.add(cloningToolsField, 1, 7);

        gridPane.add(authorLabel, 3, 8);

        Scene scene = new Scene(gridPane, 620, 300);

        ReturnedRow.loadExcelData();

        setWindowIcon(primaryStage);
        primaryStage.setTitle("Part number search");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setWindowIcon(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/icon.ico"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Could not load icon: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
