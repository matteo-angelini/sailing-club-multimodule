package common;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

/**
 * This class handles connection and interactions between DAOs and database
 */
public class DBUtil {
    private static String JDBC_DRIVER;

    private static Connection conn = null;

    private static String connStr;

    /**
     * Setter for connection string. This enables the use of
     * a test db
     * @param cStr Connection String
     * @param driver Driver of Database
     */
    public static void initDB(String cStr, String driver) {
        // Make it thread safe
        synchronized (DBUtil.class){
            connStr = cStr;
            JDBC_DRIVER = driver;
        }
    }

    /**
     * Open the connection to the database
     */
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(connStr, "root", "pass");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            System.out.println("MySql JDBC Driver not found");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Remove the connection to the database
     */
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * Generic method for executing a query to the specific connection to the database.
     * This enables us to not open too many connections slowing down our application
     * @param queryStmt The query that will be executed
     * @return Returns the result of the SQL statement
     */
    public synchronized static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;

        try {
            //Connect to DB
            dbConnect();

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = RowSetProvider.newFactory().createCachedRowSet();

            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    /**
     * This method will be used for Update/Insert/Delete operations
     * @param sqlStmt Execute the given statement
     */
    public synchronized static int dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        int ID = 0;

        try {
            //Connect to DB
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement and get
            // auto generated ID
            ID = stmt.executeUpdate(sqlStmt, Statement.RETURN_GENERATED_KEYS);

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }

        return ID;
    }

    /**
     * Getter for connection object
     * @return Connection
     */
    public static Connection getConn() {
        return conn;
    }
}
