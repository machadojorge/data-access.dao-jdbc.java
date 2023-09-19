package model.dao;

import db.DB;
import model.daoImpl.DepartmentDaoJDBC;
import model.daoImpl.SellerDaoJDBC;

public class DaoFactory {
    // this class is important because this is mandatory to instance the objects from SellerDao and DepartmentDao
    // this class has a static methods that returns a interface in the declaration but, in the implementation block
    // returns a object that implementation of the class that implements the interface

    public static SellerDao creatSellerDao()
    {
        return new SellerDaoJDBC(DB.getConnection());
    }

    // This class returns a object for use to access all methodos to CRUD operations in the Table Department
    public static DepartmentDao createDepartmentDao()
    {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
