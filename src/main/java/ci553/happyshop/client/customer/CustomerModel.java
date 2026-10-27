package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.Trolley;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// This class includes TODO comments in addToTrolley() and checkout()
// indicating possible extension ideas to explore.
public class CustomerModel {
    public CustomerView cusView; //part of MVC
    private DatabaseRW databaseRW; //Interface type, not specific implementation
                                  //Benefits: Flexibility: Easily change the database implementation.

    private Product searchResult =null; // product found from search
    private Trolley trolley =  new Trolley(); // holds the products currently in the customer's trolley

    // Data passed to CustomerView for UI updates (including search, trolley and receipt page)
    private String searchResultImageName = "imageHolder.jpg";
                    // Image file name representing the current search result.
                    // - Product image if a product is found
                   // - Default placeholder image otherwise
    private String searchResultProductText = "No product has been searched yet";
                  // Textual representation of the product from the search result.
                  // - Shows product details (e.g. "0001, 40-inch TV, ...") if found
                 // - Otherwise shows a status message
    private String trolleyContentsText = "";
                 // Textual representation of current trolley contents,
                 // used for display in the Trolley page
    private String receiptContentsText = "";
                 // Textual representation of the generated receipt after checkout,
                // used for display in the Receipt page

    // Dependency Injection via constructor
    public CustomerModel(DatabaseRW databaseRW){
        this.databaseRW = databaseRW;
    }

    //SELECT productID, description, image, unitPrice,inStock quantity
    void search(){
        String productId = cusView.tfId.getText().trim();
        if(!productId.isEmpty()){
            searchResult = databaseRW.searchProductById(productId); //search database
            if(searchResult != null && searchResult.getStockQuantity()>0){
                double unitPrice = searchResult.getUnitPrice();
                String description = searchResult.getProductDescription();
                int stock = searchResult.getStockQuantity();

                String baseInfo = String.format("Product_Id: %s\n%s,\nPrice: £%.2f", productId, description, unitPrice);
                String quantityInfo = stock < 100 ? String.format("\n%d units left.", stock) : "";
                searchResultProductText = baseInfo + quantityInfo;
                System.out.println(searchResultProductText);
            }
            else{
                searchResult =null;
                searchResultProductText = "No Product was found with ID " + productId;
                System.out.println("No Product was found with ID " + productId);
            }
        }else{
            searchResult =null;
            searchResultProductText = "Please type ProductID";
            System.out.println("Please type ProductID.");
        }
        updateView();
    }

    void changeSortStyle(){
        if(cusView.sortNum==5){cusView.sortNum=0;}else{cusView.sortNum+=1;}
        updateView();
    }

    void sortTrolley(){

        //{"A-Z ↑","A-Z ↓","ID ↑","ID ↓","Price ↑","Price ↓"};
        //0=AZ ASC 1=AZ DES 2=ID ASC 3=ID DES 4=PRICE ASC 5=PRICE DES
        int size = trolley.getProducts().size();

        boolean ASC = true;
        int[] sortingItems = new int[size];

        //Creates array for sorting
        Product[] temp = new Product[size];
        //TODO change to for loop LOL
        int counter = 0;
        for(Product x : trolley.getProducts()){
            temp[counter] = x;
            counter+=1;
        }

        //Switch statement for sorting types
        //TODO Shorten switch to 3 case and set ASC bool to the %2 value of sortNum
        switch (cusView.sortNum){
            case 0:
                ASC = true;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = temp[i].getProductDescription().charAt(0);}
                break;
            case 1:
                ASC = false;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = temp[i].getProductDescription().charAt(0);}
                break;
            case 2:
                ASC = true;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = Integer.parseInt(temp[i].getProductId());}
                break;
            case 3:
                ASC = false;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = Integer.parseInt(temp[i].getProductId());}
                break;
            case 4:
                ASC = true;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = (int)(temp[i].getUnitPrice());}
                break;
            case 5:
                ASC = false;
                for(int i = 0; i < trolley.getProducts().size(); i++){sortingItems[i] = (int)(temp[i].getUnitPrice());}
                break;
        }

        //The sort DES
        boolean swap = true;
        while(swap){
            swap = false;
            for(int i = 0; i < temp.length; i++){
                if(i != temp.length-1){
                    if(ASC){
                        if(sortingItems[i] > sortingItems[i+1]){

                            Product holder = temp[i+1];
                            temp[i+1] = temp[i];
                            temp[i] = holder;

                            int holder2 = sortingItems[i+1];
                            sortingItems[i+1] = sortingItems[i];
                            sortingItems[i] = holder2;

                            swap = true;
                        }
                    }
                    if(!ASC){
                        if(sortingItems[i] < sortingItems[i+1]){

                            Product holder = temp[i+1];
                            temp[i+1] = temp[i];
                            temp[i] = holder;

                            int holder2 = sortingItems[i+1];
                            sortingItems[i+1] = sortingItems[i];
                            sortingItems[i] = holder2;

                            swap = true;
                        }
                    }
                }
            }
        }

        ArrayList<Product> sorted = new ArrayList<>(Arrays.asList(temp));
        trolleyContentsText = ProductListFormatter.buildString(sorted);
        updateView();
    }


    void addToTrolley(){
        if(searchResult != null){
            // Add the selected product to the trolley.
            // Currently this simply appends the product to the end of the list.
            // TODO: Improve the organisation of the trolley (see class Trolley).
            trolley.addProduct(searchResult);
            // Build a formatted string so the trolley contents can be displayed in the UI
            trolleyContentsText = ProductListFormatter.buildString(trolley.getProducts());
        }
        else{
            searchResultProductText = "Please search and get an available product before adding to trolley";
            System.out.println("must search and get an available product before add to trolley");
        }
        receiptContentsText =""; // Clear receipt to switch back to trolleyPage (receipt shows only when not empty)
        updateView();
    }

    public void checkOut()  {
        if(!trolley.getProducts().isEmpty()){
            // Group the products in the trolley by productId to optimize stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organized (merged and sorted), grouping is unnecessary.
            ArrayList<Product> groupedProducts= groupProductsById(trolley.getProducts());
            ArrayList<Product> insufficientProducts= databaseRW.purchaseStocks(groupedProducts);

            if(insufficientProducts.isEmpty()){ // If stock is sufficient for all products
                //get OrderHub and tell it to make a new Order
                OrderHub orderHub =OrderHub.getOrderHub();
                Order theOrder = orderHub.newOrder(trolley.getProducts());
                trolley.clear();
                trolleyContentsText ="";
                searchResult = null;
                searchResultProductText ="Thank you for shopping with us.";
                receiptContentsText = String.format(
                        "Order_ID: %s\nOrdered_Date_Time: %s\n%s",
                        theOrder.getOrderId(),
                        theOrder.getOrderedDateTime(),
                        ProductListFormatter.buildString(theOrder.getProductList())
                );
                System.out.println(receiptContentsText);
            }
            else{ // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for(Product p : insufficientProducts){
                    errorMsg.append("\u2022 "+ p.getProductId()).append(", ")
                            .append(p.getProductDescription()).append(" (Only ")
                            .append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }
                searchResult =null;

                //TODO
                // Add the following logic here:
                // 1. Remove products with insufficient stock from the trolley.
                // 2. Trigger a message window to notify the customer about the insufficient stock, rather than directly changing displayLaSearchResult.
                //You can use the provided RemoveProductNotifier class and its showRemovalMsg method for this purpose.
                //remember close the message window where appropriate (using method closeNotifierWindow() of RemoveProductNotifier class)
                searchResultProductText = "Checkout failed due to insufficient stock for the following products:\n" + errorMsg.toString();
                System.out.println("stock is not enough");
            }
        }
        else{
            trolleyContentsText = "Your trolley is empty";
            System.out.println("Your trolley is empty");
        }
        updateView();
    }

    /**
     * Groups products by their productId to optimize database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */
    private ArrayList<Product> groupProductsById(ArrayList<Product> proList) {
        Map<String, Product> grouped = new HashMap<>();
        for (Product p : proList) {
            String id = p.getProductId();
            if (grouped.containsKey(id)) {
                Product existing = grouped.get(id);
                existing.setOrderedQuantity(existing.getOrderedQuantity() + p.getOrderedQuantity());
            } else {
                // Make a shallow copy to avoid modifying the original
                grouped.put(id,new Product(p.getProductId(),p.getProductDescription(),
                        p.getProductImageName(),p.getUnitPrice(),p.getOrderedQuantity(),p.getStockQuantity()));
            }
        }
        return new ArrayList<>(grouped.values());
    }

    void cancel(){
        trolley.clear();
        trolleyContentsText ="";
        updateView();
    }
    void closeReceipt(){
        receiptContentsText ="";
    }

    void updateView() {
        if(searchResult != null){
            searchResultImageName = searchResult.getProductImageName();
            String relativeImageUrl = StorageLocation.imageFolder + searchResultImageName; //relative file path, eg images/0001.jpg
            // Get the full absolute path to the image
            Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
            searchResultImageName = imageFullPath.toUri().toString(); //get the image full Uri then convert to String
            System.out.println("Image absolute path: " + imageFullPath); // Debugging to ensure path is correct
        }
        else{
            searchResultImageName = "imageHolder.jpg";
        }
        cusView.update(searchResultImageName, searchResultProductText, trolleyContentsText, receiptContentsText);
    }
     // extra notes:
     //Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
     //File.toURI(): Converts a File object (a file on the filesystem) to a URI object

    //for test
    public Trolley getTrolley() {
        return trolley;
    }
}
