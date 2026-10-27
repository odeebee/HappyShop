package ci553.happyshop.client.customer;

public class CustomerController {
    public CustomerModel cusModel;

    public void doAction(String action) {
        System.out.println(action.substring(0,3));
        switch (action) {
            case "Search":
                cusModel.search();
                break;
            case "Add to Trolley":
                cusModel.addToTrolley();
                break;
            case "Cancel":
                cusModel.cancel();
                break;
            case "Check Out":
                cusModel.checkOut();
                break;
            case "OK & Close":
                cusModel.closeReceipt();
                break;
            case "Change":
                cusModel.changeSortStyle();
                break;
        }

        if(action.substring(0,4).equals("Sort")){
            cusModel.sortTrolley();
        }
    }

}
