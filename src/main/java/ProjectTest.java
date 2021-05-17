import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectTest {
    @Test
    public void testPasswordHashing() throws Exception {
        String hashed = Authentication.hashPassword("abece");
        assertNotNull(hashed);
        boolean flg = Authentication.checkPassword("abece",hashed);
        assert (!flg);
    }

    @Test
    public void testSearchData() throws Exception{
        CompanyPrintPanel c = new CompanyPrintPanel();
        boolean flg = c.searchData("5","bala","porto");
        assert(!flg);
    }

    @Test
    public void testRegistrationUser() throws Exception {
        boolean flg1 = Authentication.registerUser("Company","username2","pas1","name2","surname2");
        boolean flg2 = Authentication.registerUser("Company","username3","pas1","name3","surname3");
        assertTrue(flg2!=flg1);
    }
}
