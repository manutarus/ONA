import junit.framework.TestCase;

/**
 * Created by manu on 30/04/17.
 */


public class EntryCountTest extends TestCase {
    private EntryCount<String> entryCount;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        entryCount = new EntryCount<String>();
    }

    public void testInitialCountIsZero() throws Exception {
        assertEquals(0, entryCount.get("yes"));
    }

    public void testCount() throws Exception {
        entryCount.count("water_functioning");
        assertEquals(1, entryCount.get("yes"));
    }





}
