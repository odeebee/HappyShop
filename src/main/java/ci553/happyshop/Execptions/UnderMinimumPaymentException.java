package ci553.happyshop.Execptions;

import javafx.scene.control.Alert;

public class UnderMinimumPaymentException extends RuntimeException{
    public UnderMinimumPaymentException(String message){
        super(message);
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }
}
