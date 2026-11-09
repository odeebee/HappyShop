module ci553.happyshop {
    requires javafx.controls;
    requires java.sql;
    requires javafx.graphics;

    // Uncomment the following line when running tests in IntelliJ:
    // requires org.junit.jupiter.api;

    opens ci553.happyshop;
    opens ci553.happyshop.client;
    opens ci553.happyshop.client.customer;
    opens ci553.happyshop.client.picker;
    opens ci553.happyshop.client.orderDashboard;
    opens ci553.happyshop.client.warehouse;
    opens ci553.happyshop.client.emergency;

    exports ci553.happyshop;
    exports ci553.happyshop.client;
    exports ci553.happyshop.utility;
    exports ci553.happyshop.client.customer;
    exports ci553.happyshop.client.orderDashboard;
    exports ci553.happyshop.client.emergency;
    exports ci553.happyshop.systemSetup;
    exports ci553.happyshop.Execptions;
}