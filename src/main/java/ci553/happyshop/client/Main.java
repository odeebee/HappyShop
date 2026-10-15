package ci553.happyshop.client;

import ci553.happyshop.client.customer.*;

import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.orderDashboard.OrderDashboard;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.picker.PickerView;

import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWCreator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main JavaFX application class. This class can be executed directly
 * and starts all HappyShop clients (UIs).
 *
 * It launches the standalone clients (Customer, Picker, Order_Dashboard,
 * Warehouse, EmergencyExit) and links them into a fully working system.
 *
 * It also performs essential setup tasks, such as initializing the
 * order map in OrderHub and registering observers.
 *
 * @version 1.5
 * @author  Shine Shan University of Brighton
 */

public class Main extends Application {

    // If your IDE shows 'public' as redundant (greyed out),
   // see the explanation in the Launcher class.
    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application and calls the @Override start()
    }

    /**
     * Starts the system:
     * Multiple instances of the same client type run simultaneously —
     * for example, two Customer UIs, two Picker UIs, and two Warehouse UIs.
     *
     * This simulates a multi-user environment where many users with different roles — including multiple users
     * in the same role — working concurrently on shared data.
     */
    @Override
    public void start(Stage window) {
        startCustomerClient();
        startPickerClient();
        startOrderDashboard();

        startWarehouseClient();

        // Initializes the order map for the OrderHub.
        // Must be called after starting observer clients (Order_Dashboard and Picker clients)
        // so they receive updates.
        initializeOrderMap();

        startEmergencyExit();

        // Note: You may temporarily comment out duplicate UIs to create more screen space,
        // but the system should still correctly synchronise data across clients.
    }

    /** The customer GUI -search prodduct, add to trolley, cancel/submit trolley, view receipt.
     *
     * Responsible for setting up the MVC structure:
     * creates the Model, View, and Controller and links them together.
     *
     * It also creates the DatabaseRW instance via the DatabaseRWCreator
     * and passed into the CustomerModel using constructor injection.
     *
     * Once the components are linked, the customer interface (view) is started.
     *
     * Also creates the RemoveProductNotifier, which tracks the position of the Customer View
     * and is triggered by the Customer Model when needed (used by the extension helper).
     */
    private void startCustomerClient(){

        DatabaseRW databaseRW = DatabaseRWCreator.createDatabaseRW();

        CustomerView cusView = new CustomerView();
        CustomerController cusController = new CustomerController();
        CustomerModel cusModel = new CustomerModel(databaseRW);

        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusView.start(new Stage());

        // Optional feature: Handling Stock Shortages at Checkout.
        // Code is commented out for now; students may explore this as an extension.
        //RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        //removeProductNotifier.cusView = cusView;
        //cusModel.removeProductNotifier = removeProductNotifier;
    }

    /** The picker GUI, - for staff to pack customer's order,
     *
     * Creates the Model, View, and Controller objects for the Picker client.
     * Links them together so they can communicate with each other.
     * Starts the Picker interface.
     *
     * Also registers the PickerModel with the OrderHub to receive order notifications.
     */
    private void startPickerClient(){
        PickerModel pickerModel = new PickerModel();
        PickerView pickerView = new PickerView();
        PickerController pickerController = new PickerController();
        pickerView.pickerController = pickerController;
        pickerController.pickerModel = pickerModel;
        pickerModel.pickerView = pickerView;
        pickerModel.registerWithOrderHub();
        pickerView.start(new Stage());
    }

    //The OrderDashboard GUI - for customer to track their order's state(Ordered, Progressing, Collected)
    //This client is simple and does not follow the MVC pattern, as it only registers with the OrderHub
    //to receive order status notifications. All logic is handled internally within the OrderDashboard.
    private void startOrderDashboard(){
        OrderDashboard orderDashboard = new OrderDashboard();
        orderDashboard.registerWithOrderHub();
    }

    //initialize the orderMap<orderId, orderState> for OrderHub during system startup
    private void initializeOrderMap(){
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
    }


    /** The Warehouse GUI- for warehouse staff to manage stock;
     *
     * Initializes the Warehouse client's Model, View, and Controller,and links them together for communication.
     * It also creates the DatabaseRW instance via the DatabaseRWCreator and passed into the warehouse Model using constructor injection.
     * Once the components are linked, the warehouse interface (view) is started.
     *
     * Also creates the dependent HistoryWindow and AlertSimulator,
     * which track the position of the Warehouse window and are triggered by the Model when needed.
     * These components are linked after launching the Warehouse interface.
     */
    private void startWarehouseClient(){
        DatabaseRW databaseRW = DatabaseRWCreator.createDatabaseRW();
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel(databaseRW);

        // Link controller, model, and view and start view
        view.controller = controller;
        controller.model = model;
        model.view = view;
        view.start(new Stage());

        //create dependent views that need window info
        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();

        // Link after start
        model.successHistoryWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;
    }

    //starts the EmergencyExit GUI, - used to close the entire application immediatelly
    private void startEmergencyExit(){
        EmergencyExit.getEmergencyExit();
    }
}



