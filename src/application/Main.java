package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
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

        /// 
        System.out.println("\n=== Test 1 : Seller By Id ===");
        // this is a way to hide the implementation or do a injection dependency
        SellerDao sellerDao = DaoFactory.creatSellerDao();
        // this is for test the query to the database
        Seller sellerResult = sellerDao.findById(3);

        System.out.println(sellerResult.toString());
        System.out.println("==== End Test ====");

        System.out.println("\n=== Test 2 : Seller FindByDepartment===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);

        for (Seller obj : list)
        {
            System.out.println(obj);
        }
         System.out.println("==== End Test ====");

        System.out.println("\n=== Test 3 : Seller FindAll===");
     
        List<Seller> list2 = sellerDao.findAll();

        for (Seller obj : list2)
        {
            System.out.println(obj);
        }
         System.out.println("==== End Test ====");


        System.out.println("\n=== Test 5 : Inesert Seller===");
     
        Seller seller3 = new Seller( null, "Greg", "greg@gmail.com", new Date(), 4000.0,department);
        sellerDao.insert(seller3);
        System.out.println("Inserted! New id = " + seller3.getId());
         System.out.println("==== End Test ====");
    
    
        System.out.println("\n=== Test 6 : Update Seller===");

        seller3 = sellerDao.findById(1);
        seller3.setName("Martha Wayne");
        sellerDao.update(seller3);
        System.out.println("Update");
        System.out.println("==== End Test ====");

        System.out.println("\n=== Test 8 : Update Seller===");
        sellerDao.deleteById(11);
        System.out.println("Delete Completed");


        System.out.println("\n\n\n===================================================");
        System.out.println("=== Test 1 :Department -> FindByID ===");
        DepartmentDao depDao = DaoFactory.createDepartmentDao();
        Department dep1 = depDao.findById(2);
        System.out.println(dep1.toString());
        System.out.println("==== End Test ====\n");

        System.out.println("\n\n\n===================================================");
        System.out.println("=== Test 2 :Department -> FindALL ===");
        List<Department> listDep = new ArrayList<>();
        listDep = depDao.findAll();
        for (Department x : listDep){
            System.out.println(x.toString());
        }
        System.out.println("==== End Test ====\n");

         System.out.println("\n\n\n===================================================");
        System.out.println("=== Test 3 :Department -> DeletebyId ===");
       // depDao.deleteById(8);
        System.out.println("Delete Complete!");
        System.out.println("==== End Test ====\n");


         System.out.println("\n\n\n===================================================");
        System.out.println("=== Test 4 :Department -> UpdatebyId ===");
        Department d4 = new Department(9, "Other fing");
        depDao.update(d4);
        d4 = depDao.findById(9);
        System.out.println(d4.toString());
        System.out.println("==== End Test ====\n");

        
         System.out.println("\n\n\n===================================================");
        System.out.println("=== Test 5 :Department -> Insert ===");
        Department d5 = new Department(null, "Other fing");
        depDao.insert(d5);
        System.out.println("==== End Test ====\n");

        }
}
