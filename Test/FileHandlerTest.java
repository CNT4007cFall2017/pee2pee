import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    @Test
    public void testUtilWrite() {
        File shouldExist = new File("Resources/Common.cfg");
        assertTrue(shouldExist.exists());
        // now read the file and assert the data in it is correct
    }
}