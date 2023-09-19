package model.daoImpl;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao
{
    private Connection conn;

    public DepartmentDaoJDBC (Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {

        PreparedStatement st = null;
        try{

            st= conn.prepareStatement(
            "INSERT INTO department "
            + "(Name) "
            + "VALUES "
            + "(?)", Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());

            //verify if the insert is did
            int rowsaffected = st.executeUpdate();
            if (rowsaffected > 0)
            {
                // get the id of the new object inserted
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next())
                {
                    // this is the position "1" in the table. for that the argument is "1"
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                else
                {
                    
                        throw new DBException("Unexpected error! No rows affected!");
                    
                }
                DB.closeResultSet(rs);
            }
       }
       catch (SQLException e)
       {
        throw new DBException(e.getMessage());
       }
       finally{
        DB.closeStatement(st);
       }
    }

    @Override
    public void update(Department obj) {
       PreparedStatement st = null;

       try{
            st = conn.prepareStatement(
                "UPDATE department "
                + "SET Name = ? "
                + "WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

            st.executeUpdate();

       }
       catch (SQLException e)
       {
        throw new DBException(e.getMessage());
       }
       finally{
        DB.closeStatement(st);
       }
    }

    @Override
    public void deleteById(Integer id) {
       PreparedStatement st = null;

       // Preparing the query
       try{
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            // Replace the "?" in the query for the valu received
            st.setInt(1,id);

            // execute the command
            st.executeUpdate();
       }
       catch (SQLException e)
       {
        throw new DBException(e.getMessage());
       }
       finally{
        DB.closeStatement(st);
       }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet  rs = null;

        try{
            //first creates a slq command to realize the query
            st = conn.prepareStatement(
                "SELECT * "
                + "FROM department "
                + "WHERE department.Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();

            // testing if the query returns any value
            if (rs.next())
            {
                Department dep = instanciateDepartment(rs);
                return dep;
            }

            return null;
            
        }
        catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    // this exception is care in try catch in the query, in the function FindById
    private Department instanciateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }

    @Override
    public List<Department> findAll() {
             PreparedStatement st = null;
        ResultSet  rs = null;

        try{
            //first creates a slq command to realize the query
            st = conn.prepareStatement(
                "SELECT * "
                + "FROM department "
            );

            rs = st.executeQuery();
            // creates a list of Departments for save all Departments resulted from the query
            List<Department> listDep = new ArrayList<>();

            // testing if the query returns any value
            while (rs.next())
            {
                Department dep = instanciateDepartment(rs);
                listDep.add(dep);
            }

            return listDep;
            
        }
        catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
    
}