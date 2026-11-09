package ci553.happyshop.Execptions;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.Trolley;
import ci553.happyshop.client.customer.CustomerModel;
import ci553.happyshop.client.customer.CustomerView;
import javafx.scene.control.Alert;

public class ExcessiveOrderQuantityException extends RuntimeException{
    public ExcessiveOrderQuantityException(String message, CustomerModel view){
        super(message);
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }
}
