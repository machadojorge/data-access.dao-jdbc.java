import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.mysql.cj.protocol.Resultset;

import db.DB;
import db.DBException;
import db.DBIntegrityException;

public class App {
    public static void main(String[] args) throws Exception {

        Connection con = null;
        PreparedStatement st = null;
        // this program will goes crach because we try delete an element that associate to other columns
        try{
            con = DB.getConnection();

         
        }
        finally{
            DB.closeStatement(st);
            DB.closeConnection();
        }



     }
}
