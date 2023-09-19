package model.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

    // For creates the connection with the Database, our DAO has a dependency for Connection(java.sql.connection())
    // first declaire a attribute of type Connection 
    private Connection conn;

    // Second creates a constructor thar receives a "Connection conn" with parameter, it is a injection dependency
    public SellerDaoJDBC(Connection conn)
    {
        this.conn = conn;
    }


    @Override
    public void insert(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE seller.Id = ?"
                );
            st.setInt(1, id);
            rs = st.executeQuery();

            // the data returned from DataBase is in table format, 
            // we need convert this data to Objects from the classe that type of data

            if (rs.next()) // -> the rs returns for default the first position 0, we use o "rs.next()"" to veryfy if exists the position after the position 0, if the quary returns any value
            {
                Department dep = new Department();
                // this way we get the value returned from query and put this value in objects
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));

                Seller seller = new Seller();
                seller.setId(rs.getInt("Id"));
                seller.setName(rs.getString("Name"));
                seller.setEmail(rs.getString("Email"));
                seller.setBaseSalary(rs.getDouble("BaseSalary"));
                seller.setBirthDate(rs.getDate("BirthDate"));
                seller.setDepartement(dep);
                return seller;
            } 
            return null;
        }catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        finally
        {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }


    }

    @Override
    public List<Seller> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
}
