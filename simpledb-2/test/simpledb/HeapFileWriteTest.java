//package simpledb;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//import junit.framework.JUnit4TestAdapter;
//
//public class HeapFileWriteTest extends TestUtil.CreateHeapFile {
//    private TransactionId tid;
//
//    /**
//     * Set up initial resources for each unit test.
//     */
//    @Before public void setUp() throws Exception {
//        super.setUp();
//        tid = new TransactionId();
//    }
//
//    @After public void tearDown() throws Exception {
//        Database.getBufferPool().transactionComplete(tid);
//    }
//
//    /**
//     * Unit test for HeapFile.addTuple()
//     */
//    @Test public void addTuple() throws Exception {
//        for (int i = 0; i < 1000; ++i) {
//            empty.addTuple(tid, Utility.getHeapTuple(i, 2));
//        }
//        assertEquals(3, empty.numPages());
//        DbFileIterator iter = empty.iterator(tid);
//        iter.open();
//        for (int i = 0; i < 1000; ++i) {
//        	assertTrue(iter.hasNext());
//        	assertTrue(TestUtil.compareTuples(Utility.getHeapTuple(i, 2), iter.next()));
//        }
//    }
//
//    /**
//     * JUnit suite target
//     */
//    public static junit.framework.Test suite() {
//        return new JUnit4TestAdapter(HeapFileWriteTest.class);
//    }
//}
//
