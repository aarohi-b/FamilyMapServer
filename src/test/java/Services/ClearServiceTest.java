package Services;
import Response.ClearResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearServiceTest {
    @Test
    public void clearDbTest(){ //Clearing the database with service method
        ClearService clearService = new ClearService();
        ClearResponse clearResult = clearService.clearDb();
        assertEquals(clearResult.getMessage(), "Clear succeeded.");
    }
}
