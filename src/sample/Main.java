package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.Optional;

/**
 * Javadoc folder located at java_fx_forreal-try1\javadocs
 * This class handles all the Stages in the application.
 * Each method of the class launches a separate window of the program.
 * FUTURE ENHANCEMENT some aspects of the code could be simplified with Inheritance. For instance
 * the AddPart and Modify Part windows have many of the same attributes with a few key differences.
 * An abstract PartWindow class could be made which could then be subclassed in to AddPart and ModifyPart
 * Same with the Add and Modify Product methods
 */
public class Main extends Application {
    public int partNextIdValue = 1;
    public int productNextIdValue=1;

    @Override
    /**
     * Start method is automatically called when the program is run.
     * Loads the main form and uses Lambdas to call other form methods
     */
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Main Form");

        TableView<Part> partTableView = new TableView<Part>();
        TableView<Product>productTableView = new TableView<Product>();

        loadPartTable(partTableView,Inventory.getAllParts());
        loadProductTable(productTableView, Inventory.getAllProducts());


        Label partLabel = new Label("Parts");
        Label productLabel = new Label("Products");

        Button partAddButton = new Button("Add ");
        Button partModifyButton = new Button("Modify");
        Button partDeleteButton = new Button("Delete");
        Button productAddButton = new Button("Add");
        Button productModifyButton = new Button("Modify");
        Button productDeleteButton = new Button("Delete");
        Button exitButton = new Button("Exit");

/**
 * Create a new stage and launch addPart form
 */
        partAddButton.setOnAction((event) -> {

            Stage addPartStage =  new Stage();
            addPartWindow(addPartStage);
        });
/**
 * Crate a new stage,grab part to modify,and launch modifyPartForm
 */
        partModifyButton.setOnAction((event) ->{
            try {
                Stage modifyPartStage = new Stage();
                modifyPartWindow(modifyPartStage, partTableView.getSelectionModel().getSelectedItem(), partTableView.getSelectionModel().getSelectedIndex());
            }
            catch(NullPointerException e){
                Alert noModifyAlet = new Alert(Alert.AlertType.ERROR, "Please select a part to modify", ButtonType.OK);
                noModifyAlet.show();
            }
        });
/**
 * Delete selected part from Table
 */
        partDeleteButton.setOnAction((event) ->{

                try {
                    if (partTableView.getSelectionModel().getSelectedItem()==null)throw new NullPointerException();
                    Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
                    if(!result.isPresent());
                    else if(result.get() == ButtonType.YES){
                        Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());

                    }
                    else if(result.get() == ButtonType.NO);



                }
                catch(NullPointerException e){
                    Alert noModifyAlet = new Alert(Alert.AlertType.ERROR, "Please select a part to delete", ButtonType.OK);
                    noModifyAlet.show();
                }

                });

/**
 * Create a new Stage and launch the addPart form
 */
        productAddButton.setOnAction((event)  ->{
            Stage addProductStage = new Stage();
            addProductWindow(addProductStage);
        });


/**
 * create a new stage, grab part to modify, launch modifyPart Form
 */
        productModifyButton.setOnAction((event)  ->{
            try {
                Stage modifyProductStage = new Stage();
                modifyProductWindow(modifyProductStage,
                        productTableView.getSelectionModel().getSelectedItem(), productTableView.getSelectionModel().getSelectedIndex());
            }
            catch(NullPointerException e){
                Alert noModifyAlet = new Alert(Alert.AlertType.ERROR, "Please select a product to delete", ButtonType.OK);
                noModifyAlet.show();

            }

        });
/**
 * delete selected product as long as it has no associated parts
 */
        productDeleteButton.setOnAction((event)  ->{

            try {
                if (productTableView.getSelectionModel().getSelectedItem()==null)throw new NullPointerException();
                Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
                if(!result.isPresent());
                else if(result.get() == ButtonType.YES){
                    if(productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() >0)
                        throw new IllegalStateException("Must remove Products associated parts before deleting the Product");
                    Inventory.deleteProduct(productTableView.getSelectionModel().getSelectedItem());
                }
                else if(result.get() == ButtonType.NO);

            }
            catch(NullPointerException e){
                Alert noModifyAlert = new Alert(Alert.AlertType.ERROR, "Please select a product to delete", ButtonType.OK);
                noModifyAlert.show();
            }
            catch(IllegalStateException e) {
                Alert noModifyAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                noModifyAlert.show();
            }

        });

        exitButton.setOnAction((event)  ->{
            primaryStage.close();

                });

        //search for Part
        TextField partSeach = new TextField();
        partSeach.setPromptText("Enter part ID or name");
        partSeach.textProperty().addListener((observableValue, oldvalue, newvalue) -> {

            try {
                ObservableList<Part> newPartsList = FXCollections.observableArrayList();
                if (!newvalue.equals("")) {

                    if (Character.isDigit(newvalue.charAt(0))) {
                        int searchId = Integer.parseInt(newvalue);
                        newPartsList.add(Inventory.lookupPart(searchId));//will try to add a null value if not found
                        partTableView.setItems(newPartsList);
                    }
                    else {
                       partTableView.setItems(Inventory.lookupPart(newvalue));
                    }

                } else partTableView.setItems(Inventory.getAllParts());

            }
            catch(IllegalArgumentException e ){
                Alert partNotFoundAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                        ButtonType.OK);
                partNotFoundAlert.show();
                Platform.runLater(() -> {
                    partSeach.clear();
                });
            }

            });
       //search for product
        TextField productSearch = new TextField();
        productSearch.setPromptText("Enter product ID or name");
        productSearch.textProperty().addListener((observableValue, oldvalue, newvalue) -> {

            try {
                ObservableList<Product> newProductsList = FXCollections.observableArrayList();
                if (!newvalue.equals("")) {

                    if (Character.isDigit(newvalue.charAt(0))) {
                        int searchId = Integer.parseInt(newvalue);
                        newProductsList.add(Inventory.lookupProduct(searchId));//will try to add a null value if not found
                        productTableView.setItems(newProductsList);
                    }
                    else {
                        productTableView.setItems(Inventory.lookupProduct(newvalue));
                    }

                } else productTableView.setItems(Inventory.getAllProducts());

            }
            catch(IllegalArgumentException e ){
                Alert productNotFoundAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                        ButtonType.OK);
                productNotFoundAlert.show();
                //partSeach.setText("");
                Platform.runLater(() -> {
                    productSearch.clear();
                });
            }

        });



        HBox hbox1 = new HBox(10, partTableView);
        HBox hbox2 = new HBox(10, partAddButton, partModifyButton, partDeleteButton);
        VBox vbox = new VBox(10, partLabel,partSeach, hbox1,hbox2);
        //--------------------------------------------------------
        HBox productHbox1 = new HBox(10, productTableView);
        HBox productHbox2 = new HBox(10,productAddButton, productModifyButton, productDeleteButton, exitButton);

        VBox productVBox = new VBox(10, productLabel, productSearch, productHbox1, productHbox2);

        HBox megaHBox = new HBox(10,vbox, productVBox);

        megaHBox.setAlignment(Pos.CENTER);
        megaHBox.setPadding(new Insets(10));
        Scene scene = new Scene(megaHBox, 700, 250);
        primaryStage.setScene(scene);
        primaryStage.show();



    }

    /**
     *
     *
     * Launches the addPartStage consisting of input textfields for the new part as well as save and cancel buttons
     * Saving and canceling are handled by lambdas
     * @param addPartStage launches add part window
     */
    public void addPartWindow(Stage addPartStage){

    addPartStage.setTitle("Add Part");
    Label addPartLabel = new Label("Add Part");

    RadioButton inHouseRadio = new RadioButton("InHouse");
    RadioButton outsourcedRadio = new RadioButton("Outsourced");
    ToggleGroup partType = new ToggleGroup();
    inHouseRadio.setToggleGroup(partType);
    outsourcedRadio.setToggleGroup(partType);
    inHouseRadio.setSelected(true);


        Label partIdLabel = new Label("ID:");
        Label partNameLabel = new Label("Name:");
        Label partStockLabel = new Label("Inventory:");
        Label partCostLabel = new Label("Price/ Cost :");
        Label partMaxLabel = new Label("Max :");
        Label partMinLabel = new Label("Min :");
        Label partPolyLabel = new Label("Machine ID:        ");


        TextField partIdText = new TextField();
        TextField partNameText = new TextField();
        TextField partStockText = new TextField();
        TextField partCostText = new TextField();
        TextField partMaxText = new TextField();
        TextField partMinText = new TextField();
        TextField partPolyText = new TextField();
        //
        partIdText.setDisable(true);
        partIdText.setText("ID values auto-generated");
        //



        TextField[] textFieldArray = {partIdText, partNameText, partStockText,
                partCostText, partMaxText, partMinText, partPolyText};
        Label[] labelArray = {partIdLabel, partNameLabel,
                partStockLabel, partCostLabel, partMaxLabel, partMinLabel, partPolyLabel};

        GridPane partGrid = new GridPane();
//Loop through to set up gridpane so labels are lined up with corresponding textfields
        for(int i=0;i<textFieldArray.length;i++)partGrid.add(textFieldArray[i], 1, i );
        for(int i=0; i<labelArray.length; i++)partGrid.add(labelArray[i], 0, i);



        inHouseRadio.setOnAction((event) -> {
            partPolyLabel.setText("Machine ID:        ");

        });

        outsourcedRadio.setOnAction((event) -> {
            partPolyLabel.setText("Company Name:");

        });

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        /**
         *When save button is clicked individual text fields are parsed using a wrapper class to give descriptive error messages.
         * Texfields are reset for user input where there is bad input.
         * If successfully created part, closes the addPart window.
         * LOGICAL ERROR in order to pinpoint the exact texfield where the error is occuring instead of a general messages,
         * needed to construct a Wrapper class for parsing user input
         */

        saveButton.setOnAction((event)  ->{

            try {
                int newPartId = this.partNextIdValue;
                String newPartName = ParseWrapper.parseName(partNameText);
                int newPartStock = ParseWrapper.parseStock(partStockText);
                double newPartPrice = ParseWrapper.parseCost(partCostText);
                int newPartMin =ParseWrapper.parseMin(partMinText);
                int newPartMax = ParseWrapper.parseMax(partMaxText);

                if (!(newPartMax>newPartMin))
                    throw new IllegalArgumentException("Max must be greater than Min");
                if(!(newPartMin<newPartStock&&newPartMax>newPartStock)) throw new
                        IllegalArgumentException("Part Inventory level" +
                        " must remain greater than Min and less than Max");
                if (inHouseRadio.isSelected()) {
                    int partMachineId = ParseWrapper.parseMachineId(partPolyText);
                    InHouse newInHouse = new InHouse(newPartId, newPartName,
                            newPartPrice, newPartStock, newPartMin, newPartMax, partMachineId);
                    Inventory.addPart(newInHouse);
                } else {
                    String newpartCompanyName = ParseWrapper.parseCompanyName(partPolyText);
                    Outsourced newOutsourced = new Outsourced(newPartId,
                            newPartName, newPartPrice, newPartStock, newPartMin, newPartMax, newpartCompanyName);
                    Inventory.addPart(newOutsourced);
                }
                this.partNextIdValue++;
                addPartStage.close();
            }
            catch (NumberFormatException e){
                Alert numFormatAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                        ButtonType.OK);
                numFormatAlert.show();
                partNameText.setText("");
                partStockText.setText("");
                partCostText.setText("");
                partMaxText.setText("");
                partMinText.setText("");
                partPolyText.setText("");

            }
            catch (IllegalArgumentException e){
                Alert maxMinAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                maxMinAlert.show();
                partMaxText.setText("");
                partMinText.setText("");
                partStockText.setText("");
            }
                });

        /**
         * Clicking cancel on the addPart window closes it without adding a new part
         */
        cancelButton.setOnAction((actionEvent) -> {
            addPartStage.close();


        });


        partGrid.setHgap(30);
        partGrid.setVgap(10);
        HBox okCancelBox = new HBox(20, saveButton, cancelButton);
        HBox partRadioBox= new HBox(20, addPartLabel, inHouseRadio, outsourcedRadio);
        VBox addPartVbox = new VBox(20, partRadioBox, partGrid, okCancelBox);
        addPartVbox.setPadding(new Insets(20));
        okCancelBox.setAlignment(Pos.CENTER);
        partGrid.setAlignment(Pos.CENTER);
        partRadioBox.setAlignment(Pos.CENTER);
        addPartVbox.setAlignment(Pos.CENTER);
       Scene partAdd = new Scene(addPartVbox, 500, 500);
        addPartStage.setScene(partAdd);
        addPartStage.show();


    }


    /**
     *
     * Simmilar to the addPart method, launches a new stage with input texfields to create a new part
     * However, prepopulate the textfields with the attributes from part to be modified
     * @param modifyPartStage new stage
     * @param partToModify part that will be modified
     * @param partToModifyIndex index of the part to be modified
     */
    public void modifyPartWindow(Stage modifyPartStage, Part partToModify, int partToModifyIndex){

    modifyPartStage.setTitle("Modify Part");
    Label modifyPartLabel = new Label("Modify Part");

    RadioButton inHouseRadio = new RadioButton("InHouse");
    RadioButton outsourcedRadio = new RadioButton("Outsourced");
    ToggleGroup partType = new ToggleGroup();
    inHouseRadio.setToggleGroup(partType);
    outsourcedRadio.setToggleGroup(partType);
    inHouseRadio.setSelected(true);


    Label partIdLabel = new Label("ID:");
    Label partNameLabel = new Label("Name:");
    Label partStockLabel = new Label("Inventory:");
    Label partCostLabel = new Label("Price/ Cost :");
    Label partMaxLabel = new Label("Max :");
    Label partMinLabel = new Label("Min :");
    Label partPolyLabel = new Label("Machine ID:        ");



    TextField partIdText = new TextField(String.valueOf(partToModify.getId()));
    TextField partNameText = new TextField(partToModify.getName());
    TextField partStockText = new TextField(String.valueOf(partToModify.getStock()));
    TextField partCostText = new TextField(String.valueOf(partToModify.getPrice()));
    TextField partMaxText = new TextField(String.valueOf(partToModify.getMax()));
    TextField partMinText = new TextField(String.valueOf(partToModify.getMin()));
    TextField partPolyText = new TextField();

//on initial load grab the machine id or company name based on part type
    if(partToModify instanceof InHouse){
        inHouseRadio.setSelected(true);
        partPolyText.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
    }
    else{
        outsourcedRadio.setSelected(true);
         partPolyText.setText(((Outsourced)partToModify).getCompanyName());
    }

    partIdText.setDisable(true);

    TextField[] textFieldArray = {partIdText, partNameText, partStockText,
            partCostText, partMaxText, partMinText, partPolyText};
    Label[] labelArray = {partIdLabel, partNameLabel,
            partStockLabel, partCostLabel, partMaxLabel, partMinLabel, partPolyLabel};

    GridPane partGrid = new GridPane();

    for(int i=0;i<textFieldArray.length;i++)partGrid.add(textFieldArray[i], 1, i );
    for(int i=0; i<labelArray.length; i++)partGrid.add(labelArray[i], 0, i);


//if the part is inhouse grabds the machine id to prepopulate
    inHouseRadio.setOnAction((event) -> {
        partPolyLabel.setText("Machine ID:        ");
        if(partToModify instanceof InHouse)partPolyText.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
        else partPolyText.setText("");
    });
//if the part is outsourced grabs the company name to prepopulate
    outsourcedRadio.setOnAction((event) -> {
        partPolyLabel.setText("Company Name:");
        if(partToModify instanceof Outsourced)partPolyText.setText((((Outsourced)partToModify).getCompanyName()));
        else partPolyText.setText("");
    });

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
/**
 * Same as Save button for Addpart except does not change the parts ID and does not increment the id counter
 */
    saveButton.setOnAction((event)  ->{

        try {
            int newPartId = partToModify.getId();
            String newPartName = ParseWrapper.parseName(partNameText);
            int newPartStock = ParseWrapper.parseStock(partStockText);
            double newPartPrice = ParseWrapper.parseCost(partCostText);
            int newPartMin = ParseWrapper.parseMin(partMinText);
            int newPartMax = ParseWrapper.parseMax(partMaxText);
            if (!(newPartMax>newPartMin))throw new IllegalArgumentException("Max must be greater than Min");
            if(!(newPartMin<newPartStock&&newPartMax>newPartStock)) throw new
                    IllegalArgumentException("Part Inventory level" +
                    " must remain greater than Min and less than Max");
            if (inHouseRadio.isSelected()) {
                int partMachineId = ParseWrapper.parseMachineId(partPolyText);
                InHouse newInHouse = new InHouse(newPartId, newPartName,
                        newPartPrice, newPartStock, newPartMin, newPartMax, partMachineId);
                Inventory.updatePart(partToModifyIndex, newInHouse);
            } else {
                String newpartCompanyName =ParseWrapper.parseCompanyName(partPolyText);
                Outsourced newOutsourced = new Outsourced(newPartId,
                        newPartName, newPartPrice, newPartStock, newPartMin, newPartMax, newpartCompanyName);
                Inventory.updatePart(partToModifyIndex, newOutsourced);
            }
            modifyPartStage.close();
        }
        catch (NumberFormatException e){
            Alert numFormatAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                    ButtonType.OK);
            numFormatAlert.show();
            partNameText.setText("");
            partStockText.setText("");
            partCostText.setText("");
            partMaxText.setText("");
            partMinText.setText("");
            partPolyText.setText("");

        }
        catch (IllegalArgumentException e){
            Alert maxMinAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            maxMinAlert.show();
            partMaxText.setText("");
            partMinText.setText("");
            partStockText.setText("");
        }
    });

    cancelButton.setOnAction((actionEvent) -> {
        modifyPartStage.close();


    });


    partGrid.setHgap(30);
    partGrid.setVgap(10);
    HBox okCancelBox = new HBox(20, saveButton, cancelButton);
    HBox partRadioBox= new HBox(20, modifyPartLabel, inHouseRadio, outsourcedRadio);
    VBox modifyPartVbox = new VBox(20, partRadioBox, partGrid, okCancelBox);
    modifyPartVbox.setPadding(new Insets(20));
    okCancelBox.setAlignment(Pos.CENTER);
    partGrid.setAlignment(Pos.CENTER);
    partRadioBox.setAlignment(Pos.CENTER);
    modifyPartVbox.setAlignment(Pos.CENTER);
    Scene partAdd = new Scene(modifyPartVbox, 500, 500);
    modifyPartStage.setScene(partAdd);
    modifyPartStage.show();

}

    /**
     * Launches a new stage with textfields for user input and tableviews for showing the products associated parts and
     * the total list of parts to choose from. Saving products, canceling out of the window, and the adding and removing
     * of associated parts are handled within the method by lambdas.
     * @param addProductStage launches add product window
     */
    public void addProductWindow(Stage addProductStage){

        addProductStage.setTitle("Add Product");
        Label addProductLabel = new Label("Add Product");


        Label productIdLabel = new Label("ID:");
        Label productNameLabel = new Label("Name:");
        Label productStockLabel = new Label("Inventory:");
        Label productCostLabel = new Label("Price/ Cost :");
        Label productMaxLabel = new Label("Max :");
        Label productMinLabel = new Label("Min :");

        TextField productIdText = new TextField();
        TextField productNameText = new TextField();
        TextField productStockText = new TextField();
        TextField productCostText = new TextField();
        TextField productMaxText = new TextField();
        TextField productMinText = new TextField();

        productIdText.setDisable(true);
        productIdText.setText("ID values auto-generated");

        TextField[] textFieldArray = {productIdText, productNameText, productStockText,
                productCostText, productMaxText, productMinText};
        Label[] labelArray = {productIdLabel, productNameLabel,
                productStockLabel, productCostLabel, productMaxLabel, productMinLabel};

        GridPane partGrid = new GridPane();

        for(int i=0;i<textFieldArray.length;i++)partGrid.add(textFieldArray[i], 1, i );
        for(int i=0; i<labelArray.length; i++)partGrid.add(labelArray[i], 0, i);


        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove Associated Part");

        TableView<Part> associatedPartsTable = new TableView<Part>();
        TableView<Part> partTableView = new TableView<Part>();

        ObservableList<Part> partsToAdd = FXCollections.observableArrayList();//temporary list for keeping track of associated parts

        loadPartTable(associatedPartsTable,partsToAdd );//these functions load the table with data from the specified list
        loadPartTable(partTableView, Inventory.getAllParts());

/**
 * Add selected part from Iventory to temporary list
 */
        addButton.setOnAction((event)  ->{
            try {
                Part y = partTableView.getSelectionModel().getSelectedItem();
                y.getId();
                partsToAdd.add(y);
            }
            catch(NullPointerException e){

                Alert noPartAlert = new Alert(Alert.AlertType.ERROR, "Please select a part to add",
                        ButtonType.OK);
                noPartAlert.show();
            }

        });



        TextField partSeach = new TextField();
        partSeach.setPromptText("Enter part ID or name");
        /**
         * search functionality for the table containing Inventory Part data
         */
        partSeach.textProperty().addListener((observableValue, oldvalue, newvalue) -> {

            try {
                ObservableList<Part> newPartsList = FXCollections.observableArrayList();
                if (!newvalue.equals("")) {

                    if (Character.isDigit(newvalue.charAt(0))) {
                        int searchId = Integer.parseInt(newvalue);
                        newPartsList.add(Inventory.lookupPart(searchId));//will add a null value if not found
                        partTableView.setItems(newPartsList);
                    }
                    else {
                        partTableView.setItems(Inventory.lookupPart(newvalue));
                    }

                } else partTableView.setItems(Inventory.getAllParts());

            }
            catch(IllegalArgumentException e ){
                Alert partNotFoundAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                        ButtonType.OK);
                partNotFoundAlert.show();
                Platform.runLater(() -> {
                    partSeach.clear();
                });
            }

        });
        /**
         * Parses the input product data and creates a new product from it if able
         * If an parts are contained in the tempoaray partstoadd list, adds them to the products associated parts
         */
        saveButton.setOnAction((event)  ->{

            try {
                int newProductId = this.productNextIdValue;
                String newPartName = ParseWrapper.parseName(productNameText);
                int newProducttStock = ParseWrapper.parseStock(productStockText);
                double newproductPrice =ParseWrapper.parseCost(productCostText);
                int newProductMin =ParseWrapper.parseMin(productMinText);
                int newProductMax = ParseWrapper.parseMax(productMaxText);

                if (!(newProductMax>newProductMin))throw new IllegalArgumentException("Max must be greater than Min");
                if(!(newProductMin<newProducttStock&&newProductMax>newProducttStock)) throw new
                        IllegalArgumentException("Product Inventory level" +
                        " must remain greater than Min and less than Max");

                Product myproduct = new Product(newProductId, newPartName,
                        newproductPrice, newProducttStock, newProductMin, newProductMax );
                if(partsToAdd.size()>0) {
                    for (Part i : partsToAdd) myproduct.addAssociatedPart(i);
                }
                Inventory.addProduct(myproduct);

               this.productNextIdValue++;
                addProductStage.close();
            }
            catch (NumberFormatException e){
                Alert numFormatAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                numFormatAlert.show();
                productNameText.setText("");
                productStockText.setText("");
                productCostText.setText("");
                productMaxText.setText("");
                productMinText.setText("");


            }
            catch (IllegalArgumentException e){
                Alert maxMinAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                maxMinAlert.show();
                productMaxText.setText("");
                productMinText.setText("");
                productStockText.setText("");
                  }
                  });

            cancelButton.setOnAction((actionEvent) -> {
            addProductStage.close();

                 });

        removeButton.setOnAction((actionEvent) -> {
                    try {
                        if (associatedPartsTable.getSelectionModel().getSelectedItem() == null) throw new NullPointerException();
                        Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?",
                                ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
                        if (!result.isPresent()) ;
                        else if (result.get() == ButtonType.YES) {
                            partsToAdd.remove(associatedPartsTable.getSelectionModel().getSelectedItem());

                        } else if (result.get() == ButtonType.NO) ;

                    }
                    catch(NullPointerException e){
                        Alert noRemoveAlert = new Alert(Alert.AlertType.ERROR, "Please select an associated part to remove", ButtonType.OK);
                        noRemoveAlert.show();
                    }

        });


        partGrid.setHgap(30);
        partGrid.setVgap(10);
        partGrid.setAlignment(Pos.CENTER);
        HBox saveCancelBox = new HBox(10, saveButton, cancelButton);
        VBox myVBox = new VBox(20,partSeach,partTableView, addButton,associatedPartsTable, removeButton, saveCancelBox);
        myVBox.setPadding(new Insets(10));
        VBox vbox2 = new VBox(30,addProductLabel, partGrid);
        vbox2.setAlignment(Pos.CENTER);
        HBox finalHBox = new HBox(50,vbox2, myVBox);
        finalHBox.setAlignment(Pos.CENTER);
        Scene productAdd = new Scene(finalHBox, 800, 500);
        addProductStage.setScene(productAdd);
        addProductStage.show();

    }

    /**
     * Similar functionality to AddProduct stage, except prepolulates the textfields with values fromt the Product to be modified
     * Also prepopulates Associated part table with the products current associated parts
     * @param addProductStage Launches addproduct stage
     * @param productToModify the Product to modify
     * @param index the index in the Invntory of the product to modify
     */
    public void modifyProductWindow(Stage addProductStage, Product productToModify, int index){

        addProductStage.setTitle("Modify Product");
        Label addProductLabel = new Label("Modify Product");
        Label productIdLabel = new Label("ID:");
        Label productNameLabel = new Label("Name:");
        Label productStockLabel = new Label("Inventory:");
        Label productCostLabel = new Label("Price/ Cost :");
        Label productMaxLabel = new Label("Max :");
        Label productMinLabel = new Label("Min :");

        TextField productIdText = new TextField(String.valueOf(productToModify.getId()));
        TextField productNameText = new TextField(productToModify.getName());
        TextField productStockText = new TextField(String.valueOf(productToModify.getStock()));
        TextField productCostText = new TextField(String.valueOf(productToModify.getPrice()));
        TextField productMaxText = new TextField(String.valueOf(productToModify.getMax()));
        TextField productMinText = new TextField(String.valueOf(productToModify.getMin()));

        productIdText.setDisable(true);

        TextField[] textFieldArray = {productIdText, productNameText, productStockText,
                productCostText, productMaxText, productMinText};
        Label[] labelArray = {productIdLabel, productNameLabel,
                productStockLabel, productCostLabel, productMaxLabel, productMinLabel};

        GridPane partGrid = new GridPane();

        for(int i=0;i<textFieldArray.length;i++)partGrid.add(textFieldArray[i], 1, i );
        for(int i=0; i<labelArray.length; i++)partGrid.add(labelArray[i], 0, i);


        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove Associated Part");

        TableView<Part> associatedPartsTable = new TableView<Part>();
        TableView<Part> partTableView = new TableView<Part>();

        ObservableList<Part> partsToAdd = FXCollections.observableArrayList();
        for (Part i : productToModify.getAllAssociatedParts()) partsToAdd.add(i);

        loadPartTable(associatedPartsTable, partsToAdd);
        loadPartTable(partTableView, Inventory.getAllParts());


        addButton.setOnAction((event)  ->{
            //grab the part that u have selected in the parttableview
            //add it to parts to add list
            try {
                Part y = partTableView.getSelectionModel().getSelectedItem();
                y.getId();
                partsToAdd.add(y);
            }
            catch(NullPointerException e){

                Alert noPartAlert = new Alert(Alert.AlertType.ERROR, "Please select a part to add",
                        ButtonType.OK);
                noPartAlert.show();
            }

        });


        TextField partSeach = new TextField();
        partSeach.setPromptText("Enter part ID or name");
        partSeach.textProperty().addListener((observableValue, oldvalue, newvalue) -> {

            try {
                ObservableList<Part> newPartsList = FXCollections.observableArrayList();
                if (!newvalue.equals("")) {

                    if (Character.isDigit(newvalue.charAt(0))) {
                        int searchId = Integer.parseInt(newvalue);
                        newPartsList.add(Inventory.lookupPart(searchId));//will add a null value if not found
                        partTableView.setItems(newPartsList);
                    }
                    else {
                        partTableView.setItems(Inventory.lookupPart(newvalue));
                    }

                } else partTableView.setItems(Inventory.getAllParts());

            }
            catch(IllegalArgumentException e ){
                Alert partNotFoundAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(),
                        ButtonType.OK);
                partNotFoundAlert.show();
                //partSeach.setText("");
                Platform.runLater(() -> {
                    partSeach.clear();
                });
            }

        });
/**
 * functions similarly to the save button in the adddproduct method except retains the part id of the original product
 * and does not increment the product id counter
 */
        saveButton.setOnAction((event)  ->{

            try {
                int newProductId = ParseWrapper.parseId(productIdText);
                String newPartName = ParseWrapper.parseName(productNameText);
                int newProducttStock = ParseWrapper.parseStock(productStockText);
                double newproductPrice =ParseWrapper.parseCost(productCostText);
                int newProductMin =ParseWrapper.parseMin(productMinText);
                int newProductMax = ParseWrapper.parseMax(productMaxText);

                if (!(newProductMax>newProductMin))throw new IllegalArgumentException("Max must be greater than Min");
                if(!(newProductMin<newProducttStock&&newProductMax>newProducttStock)) throw new
                        IllegalArgumentException("Product Inventory level" +
                        " must remain greater than Min and less than Max");

                Product myproduct = new Product(newProductId, newPartName,
                        newproductPrice, newProducttStock, newProductMin, newProductMax);
                if(partsToAdd.size()>0) {
                    for (Part i : partsToAdd) myproduct.addAssociatedPart(i);
                }
                Inventory.updateProduct(index, myproduct);
                addProductStage.close();
            }
            catch (NumberFormatException e){
                Alert numFormatAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                numFormatAlert.show();
                productNameText.setText("");
                productStockText.setText("");
                productCostText.setText("");
                productMaxText.setText("");
                productMinText.setText("");


            }
            catch (IllegalArgumentException e){
                Alert maxMinAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                maxMinAlert.show();
                productMaxText.setText("");
                productMinText.setText("");
                productStockText.setText("");
            }
        });

        cancelButton.setOnAction((actionEvent) -> {
            addProductStage.close();


        });

        removeButton.setOnAction((actionEvent) -> { ;
            try {
                if (associatedPartsTable.getSelectionModel().getSelectedItem() == null) throw new NullPointerException();
                Alert confirmDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
                if (!result.isPresent()) ;
                else if (result.get() == ButtonType.YES) {
                    partsToAdd.remove(associatedPartsTable.getSelectionModel().getSelectedItem());

                } else if (result.get() == ButtonType.NO) ;

            }
            catch(NullPointerException e){
                Alert noRemoveAlert = new Alert(Alert.AlertType.ERROR, "Please select an associated part to remove", ButtonType.OK);
                noRemoveAlert.show();
            }
        });


        partGrid.setHgap(30);
        partGrid.setVgap(10);
        partGrid.setAlignment(Pos.CENTER);
        HBox saveCancelBox = new HBox(10, saveButton, cancelButton);
        VBox myVBox = new VBox(20,partSeach,partTableView, addButton,associatedPartsTable, removeButton, saveCancelBox);
        myVBox.setPadding(new Insets(10));
        VBox vbox2 = new VBox(30,addProductLabel, partGrid);
        vbox2.setAlignment(Pos.CENTER);
        HBox finalHBox = new HBox(50,vbox2, myVBox);
        finalHBox.setAlignment(Pos.CENTER);
        Scene productAdd = new Scene(finalHBox, 800, 500);
        addProductStage.setScene(productAdd);
        addProductStage.show();

    }

    /**
     * function which loads part data from the specified list into the specified table
     * @param partTableView table to be loaded
     * @param partList list from which data will be loaded
     */
    public void loadPartTable (TableView<Part> partTableView, ObservableList<Part> partList){


        partTableView.setItems(partList);

        TableColumn<Part, String> partidCol = new TableColumn<Part,String>(" Part ID");
        partidCol.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn<Part,String> partNameCol = new TableColumn<Part,String>(" Name");
        partNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Part,String> partStockCol = new TableColumn<Part,String>(" Stock Level");
        partStockCol.setCellValueFactory(new PropertyValueFactory("stock"));
        TableColumn<Part,String> partPriceCol = new TableColumn<Part,String>(" Price/ Cost per Unit");
        partPriceCol.setCellValueFactory(new PropertyValueFactory("price"));

        partTableView.getColumns().setAll(partidCol,partNameCol,partStockCol, partPriceCol);

    }

    /**
     * loads product data from specified list into specified table
     * @param productTableView table to be loaded
     * @param productList list from which data will be loaded
     */
    public void loadProductTable(TableView<Product> productTableView, ObservableList<Product> productList){
        productTableView.setItems(productList);

        TableColumn<Product, String> productidCol = new TableColumn<Product, String>("Product ID");
        productidCol.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn<Product, String> productNameCol = new TableColumn<Product, String>("Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Product, String> productStockCol = new TableColumn<Product, String>("Stock Level");
        productStockCol.setCellValueFactory(new PropertyValueFactory("stock"));
        TableColumn<Product, String> productPriceCol = new TableColumn<Product, String>("Price/ Cost per Unit");
        productPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
        productTableView.getColumns().setAll(productidCol, productNameCol, productStockCol, productPriceCol);

    }




    public static void main(String[] args) {
        launch(args);



    }



}


