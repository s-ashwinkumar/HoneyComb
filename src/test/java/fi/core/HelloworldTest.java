package fi.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by ashwin on 6/3/16.
 */
public class HelloworldTest {
    private static Helloworld obj;

    private static final ByteArrayOutputStream outContent = new
    ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new
    ByteArrayOutputStream();

    @BeforeClass
    public static void initObj() {
        obj = new Helloworld();
    }

    @BeforeClass
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @org.junit.Test
    public void main() throws Exception {
        obj.main();
        assertEquals("5\n", outContent.toString());
    }

    @org.junit.Test
    public void TestAddition() throws Exception {
        int result = obj.addition(3, 4);
        assertEquals(7, result);
    }

    @org.junit.Test
    public void TestAddtion2() throws Exception {
        int result = obj.addition(4, 4);
        assertEquals(8, result);
    }

    @AfterClass
    public static void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

}