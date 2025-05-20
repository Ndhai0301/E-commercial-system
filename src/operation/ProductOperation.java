package operation;

import java.io.*;

import java.util.*;
// import javafx.*;
import data.databaseWork;
import model.Product;
import util.ProductListResult;

public class ProductOperation {
    private static ProductOperation instance;
    private static final String productDataFile = "src/data/products.txt";
    

    private ProductOperation(){}

    public static ProductOperation getInstance(){
        if (instance == null){
            instance = new ProductOperation();
        }        
        return instance;
    }

    /**
     * Extracts product information from the given product data files.
     * The data is saved into the data/products.txt file.
     */
    public void extractProductsFromFiles(){
        String inputFile = "src/data/raw_products.txt";  // file gốc chứa sản phẩm
        String outputFile = "src/data/products.txt";

       try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null){
                databaseWork.addOneLine(line, outputFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retrieves one page of products from the database.
     * One page contains a maximum of 10 items.
     * @param pageNumber The page number to retrieve
     * @return A list of Product objects, current page number, and total pages
     */
    // public ProductListResult getProductList(int pageNumber) {
    //     int page_size = 10;
    //     List <Product> listProducts = new ArrayList<>();
    //     File inputFile = new File("src/data/products.txt");

    //     try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
    //         String line;
    //         while ((line = reader.readLine())!=null){
    //             Product product = parseProductFromLine(line);
    //             if (product  != null){
    //                 listProducts.add(product);
    //             }
    //         }
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //     }

    //     int totalProducts = listProducts.size();
    //     int totalPages = (int) Math.ceil(totalProducts / 10.0);

    //     if (pageNumber < 1) pageNumber = 1;
    //     if (pageNumber > totalPages) pageNumber = totalPages;

    //     int startIndex = (pageNumber - 1) * page_size;
    //     int endIndex = Math.min(startIndex + page_size, totalProducts);

    //     List<Product> productsOnPage = new ArrayList<>();
    //     if (startIndex < totalProducts) {
    //         productsOnPage = listProducts.subList(startIndex, endIndex);
    //     }

    //     return new ProductListResult(productsOnPage, pageNumber, totalPages);
    // }

    public ProductListResult getProductList(int pageNumber) {
        int page_size = 10;
        List<Product> listProducts = new ArrayList<>();
        File inputFile = new File("src/data/products.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductFromLine(line);
                if (product != null) {
                    listProducts.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int totalProducts = listProducts.size();
        int totalPages = (int) Math.ceil(totalProducts / 10.0);

        if (totalPages == 0) {
            return new ProductListResult(new ArrayList<>(), 1, 1);
        }

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        int startIndex = (pageNumber - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, totalProducts);

        List<Product> productsOnPage = listProducts.subList(startIndex, endIndex);

        return new ProductListResult(productsOnPage, pageNumber, totalPages);
    }


    private Product parseProductFromLine(String line){
        try{
            String proID = databaseWork.extractField(line, "pro_id");
            String proModel = databaseWork.extractField(line, "pro_model");
            String proCategory = databaseWork.extractField(line, "pro_category");
            String proName = databaseWork.extractField(line, "pro_name");
            double proCurrentPrice = Double.parseDouble(databaseWork.extractField(line, "pro_current_price")); 
            double proRawPrice = Double.parseDouble(databaseWork.extractField(line, "pro_raw_price"));
            double proDiscount = Double.parseDouble(databaseWork.extractField(line, "pro_discount"));
            int proLikesCount = Integer.parseInt(databaseWork.extractField(line, "pro_likes_count"));

            return new Product(proID, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
        } catch (Exception e){
            return null;
        }
    }
    
    /**
    * Deletes the product from the system based on the provided product_id.
    * @param productId The ID of the product to delete
    * @return true if successful, false otherwise
    */
    
    public boolean deleteProduct(String productId) {
        return databaseWork.deleteOneLine(productId, productDataFile);
    }

    /**
    * Retrieves all products whose name contains the keyword (case insensitive).
    * @param keyword The search keyword
    * @return A list of Product objects matching the keyword
    */
    public List<Product> getProductListByKeyword(String keyword) {
        File inputFile = new File(productDataFile);
        List<Product> listProduct = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProductFromLine(line);
                if (product != null && product.getProName().toLowerCase().contains(keyword.toLowerCase())) {
                    listProduct.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listProduct;
    }    

    /**
     * Returns one product object based on the given product_id.
     * @param productId The ID of the product to retrieve
     * @return A Product object or null if not found
     */    
    public Product getProductById(String productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String id = databaseWork.extractField(line, "pro_id");
                if (productId.equals(id)) {
                    return parseProductFromLine(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * Generates a bar chart showing the total number of products
    * for each category in descending order.
    * Saves the figure into the data/figure folder.
    */
    public void generateCategoryFigure() {
        
    }

     
    public void generateDiscountFigure() {
    // Implementation using Java charting library
    }

    
    public void generateLikesCountFigure() {
    // Implementation using Java charting library
    }

    public void generateDiscountLikesCountFigure() {
    // Implementation using Java charting library
    }

    
    public void deleteAllProducts() {
        File productFile = new File("src/data/products.txt");
    
        try {
            if (!productFile.exists()) {
                System.out.println("File products does not exist");
                return;
            }           
            
            FileWriter writer = new FileWriter(productFile);
            writer.write(""); 
            writer.close();
            
            System.out.println("    deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting products: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

