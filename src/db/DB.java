package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

    // now, we go create a static atribute of type "Connection", this actribute is mandatory to connect to the Database
    private static Connection connection = null;

    // creates a connection method
    public static Connection getConnection() throws ClassNotFoundException
    {
  
        if (connection == null)
        {
            try{
                // if the connection do not exist, we get the proprities and create the connection with the database
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url, props);
                return connection;
            }
            catch (SQLException e)
            {
                throw new DBException(e.getMessage());
            }
        }
        return connection;
    }

    private static Properties loadProperties()
    {
        try(FileInputStream fs = new FileInputStream("db.properties"))
        {
            // we create a object of type properties and this object goes loads the data in the fs object
            Properties props = new Properties();
            props.load(fs);
            return props;
        }
        catch (IOException e)
        {
            // this block of code throws the exception and use the classe we created.
            throw new DBException(e.getMessage());
        }
    }

    // this method close the connection
    public static void closeConnection()
    {
        if (connection != null)
        {
            try{
                connection.close();
            }
            catch (SQLException e)
            {
                throw new DBException(e.getMessage());
            }

        }
    }

    public static void closeStatement(Statement st)
    {
        if (st != null)
        {
            try {
                st.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
               throw new DBException((e.getMessage()));
            }
        }

    }

        public static void closeResultSet(ResultSet rs)
    {
        if (rs != null)
        {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
               throw new DBException((e.getMessage()));
            }
        }

    }
    
}
