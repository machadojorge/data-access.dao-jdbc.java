package model.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
       
        PreparedStatement st = null;
        try{

            st= conn.prepareStatement(
                "INSERT INTO seller "
                + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                + "VALUES "
                + "(?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            // we instanceate a java.sqlDate, to put the data in the atributes
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartement().getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0)
            {
                // this is for get the Id this new object insert
                ResultSet rs = st.getGeneratedKeys();           
                if (rs.next())
                {
                    // get the id from rs variable. the id is in position 1 in the table
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                else
                {
                    throw new DBException("Unexpected error! No rows affected!");
                }

                DB.closeResultSet(rs);
            }


        }catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }



    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try{

            st= conn.prepareStatement(
                "UPDATE seller "
                + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                + "WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            // we instanceate a java.sqlDate, to put the data in the atributes
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartement().getId());
            st.setInt(6, obj.getId());

           st.executeUpdate();

        }catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        } }

    @Override
    public void deleteById(Integer id) {
       PreparedStatement st = null;

       try{
        st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

        st.setInt(1, id);

        st.executeUpdate();
       }
       catch(SQLException e)
       {
        throw new DBException(e.getMessage());
       }
       finally 
       {
        DB.closeStatement(st);
       }
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
                Department dep = instanciateDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);
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

    /// IMPORTANT ///
    // these two method are mandatory to pass the information from the query result to class object for is possible work with this data
    // thes methods are call in the function ""findById""" and returns the object class ready to use.
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
       Seller seller = new Seller();
                seller.setId(rs.getInt("Id"));
                seller.setName(rs.getString("Name"));
                seller.setEmail(rs.getString("Email"));
                seller.setBaseSalary(rs.getDouble("BaseSalary"));
                seller.setBirthDate(rs.getDate("BirthDate"));
                seller.setDepartement(dep);
        return seller;
    }


    private Department instanciateDepartment(ResultSet rs) throws SQLException { // this throw declaration is importante because we pass the error to the method that call this function. this method implements the resolution of this exception
        Department dep =  new Department();
                // this way we get the value returned from query and put this value in objects
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));
                return dep;

    }


    @Override
    public List<Seller> findAll() {
       PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "ORDER BY Name "
                );
          
            rs = st.executeQuery();

           // this method search all the departments

            // in this case, we receive many elements, we need a while to read all this elements and put this elements an list of Sellers
            // because, we search by depeartment, it's means, the department is the same.
           // this code envoit thar repeat the instance of object departments
           // in this case, just one object Department is creates, not duplicated, and the seller point to this object
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) 
            {
                Department department = map.get(rs.getInt("DepartmentId"));
                if (department == null)
                {
                    department = instanciateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), department);
                }

                Seller seller = instantiateSeller(rs, department);
                list.add(seller);
              
            } 

           return list;
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
    public List<Seller> findByDepartment(Department dep) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE DepartmentId = ? "
                + "ORDER BY Name "
                );
            st.setInt(1, dep.getId());
            rs = st.executeQuery();

            // the data returned from DataBase is in table format, 
            // we need convert this data to Objects from the classe that type of data

            // in this case, we receive many elements, we need a while to read all this elements and put this elements an list of Sellers
            // because, we search by depeartment, it's means, the department is the same.
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) 
            {
                Department department = map.get(rs.getInt("DepartmentId"));
                if (department == null)
                {
                    department = instanciateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), department);
                }

                Seller seller = instantiateSeller(rs, department);
                list.add(seller);
              
            } 

           return list;
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
    
}
