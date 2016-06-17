package fi.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;


import static org.junit.Assert.*;

/**
 * Created by ashwin on 6/3/16.
 */
public class UserTest {
    private static User obj;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        obj = Mockito.mock(User.class);
        Mockito.when(obj.getUsername()).thenReturn("testUser");
        Mockito.when(obj.getPassword()).thenReturn("testPassword");
    }

    @Test
    public void User() throws Exception {
        User object = new User("test","pass");
        assertEquals(object.getPassword(),"pass");
        assertEquals(object.getUsername(),"test");
    }

    @Test
    public void getApiToken() throws Exception {
        assertEquals(obj.getApiToken(),null);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(obj.getUsername(),"testUser");
    }

    @Test
    public void getPassword() throws Exception {
        assertEquals(obj.getPassword(),"testPassword");
    }

    // written as a part of TDD
    @Test
    public void isValidUser() throws Exception {
        //wrong username wrong password
        User obj1 = new User("test","pwd");
        boolean result = obj1.isValidUser();
        assertEquals(result,false);
        assertNull(obj1.getApiToken());
        //right username wrong password
        obj1 = new User("admin","pwd");
         result = obj1.isValidUser();
        assertEquals(result,false);
        assertNull(obj1.getApiToken());
        //both correct
        obj1 = new User("admin","password");
        assertEquals(obj1.isValidUser(),true);
        assertNotNull(obj1.getApiToken());
        // for the overloaded method that checks the token
        assertFalse(User.isValidUser("blablabla"));
        assertTrue(User.isValidUser("YWRtaW4scGFzc3dvcmQ="));
    }

    @Test
    public void ExceptionisValidUser()  {
        //TODO write method to test exception
    }

}