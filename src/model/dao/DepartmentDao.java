package model.dao;

import java.util.List;

import model.entities.Department;

// This interface has all operations that we realize in Database, some like CRUD operations
public interface DepartmentDao {
    
    void insert ( Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
    
    
}
