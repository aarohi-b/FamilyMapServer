package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Response.ClearResponse;
/**
 * Clear Service generates a clear response object that contains either an error message or success message
 */
public class ClearService {
    /** clearDb uses DAOs to clear the whole database
     */
    public ClearResponse clearDb() {
        Database myDB = new Database();
        ClearResponse responseToReturn = new ClearResponse();
        try {
            myDB.openConnection();
            myDB.clearTables();
            myDB.closeConnection(true);
            responseToReturn.setSuccessFlag(true);
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
            responseToReturn.setErrorMessage(false, "Error: Internal server error");

            try{
                myDB.closeConnection(false);
            }catch (DataAccessException e2){
                responseToReturn.setErrorMessage(false, e2.getMessage());
                return responseToReturn;
            }
            return responseToReturn;
        }
        responseToReturn.setErrorMessage(true, "Clear succeeded");
        return responseToReturn;
    }

}
