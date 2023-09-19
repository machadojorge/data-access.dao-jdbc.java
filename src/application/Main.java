package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Main {
    public static void main(String[] args) throws Exception {
    
        // This is to test the class
        Department dp1 = new Department(1,"Books");
        System.out.println(dp1.toString());

        Seller seller = new Seller(21,"bob", "bob@gmail.com", new Date(), 3000.0, dp1);
        System.out.println(seller.toString());

        // this is a way to hide the implementation or do a injection dependency
        SellerDao sellerDao = DaoFactory.creatSellerDao();
        // this is for test the query to the database
        Seller sellerResult = sellerDao.findById(3);

        System.out.println(sellerResult.toString());
    }
}
