import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {

    @Test
    public void testAddContacts() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","123456789","dan@yahoo.com","Brightling");
        assertEquals("{\"status\":\"SUCCESS\"}", cs.addContact(dan));
    }

    @Test
    public void testGetContacts() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","123456789","dan@yahoo.com","Brightling");
        cs.addContact(dan);
        assertEquals(dan, cs.getContact("Dan"));
    }

    @Test
    public void testInvalidPhoneNumber() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","0","dan@yahoo.com","Brightling");
        cs.addContact(dan);
        assertEquals("Invalid Number", cs.getContact("Dan").getNumber());
        Contact ben = new Contact("Ben","800000000001","ben@yahoo.com","Brightling");
        cs.addContact(ben);
        assertEquals("Invalid Number", cs.getContact("Ben").getNumber());
    }

    @Test
    public void testInvalidEmail() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@","Brightling");
        cs.addContact(dan);
        assertEquals("Invalid Email", cs.getContact("Dan").getEmail());
    }

    @Test
    public void testUpdateContact() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        cs.addContact(dan);
        Contact newDan = new Contact("Dan","9548003264","dan@yahoo.com","Google");
        cs.updateContact("Dan",newDan);
        assertEquals("Google",cs.getContact("Dan").getCompany());
    }

    @Test
    public void testDeleteContact() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        cs.addContact(dan);
        cs.deleteContact("Dan");
        assertEquals(null,cs.getContact("Dan"));
    }

    @Test
    public void testGetAllContacts() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        Contact ham = new Contact("Ham","9765332534","ham@yahoo.com","Brightling");
        Contact pat = new Contact("Pat","9769802334","Pat@google.com","Google");
        cs.addContact(dan);
        cs.addContact(ham);
        cs.addContact(pat);
        //3 contacts total, pageSize 3, expect 3
        assertEquals(3,cs.getQuery(3,1,null).size());
    }

    @Test
    public void testPageSizeCutoff() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        Contact ham = new Contact("Ham","9765332534","ham@yahoo.com","Brightling");
        Contact pat = new Contact("Pat","9769802334","Pat@google.com","Google");
        cs.addContact(dan);
        cs.addContact(ham);
        cs.addContact(pat);
        //pageSize 2, expect 2 results
        assertEquals(2,cs.getQuery(2,1,null).size());
    }

    @Test
    public void testHigherPageResults() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        Contact ham = new Contact("Ham","9765332534","ham@yahoo.com","Brightling");
        Contact pat = new Contact("Pat","9769802334","Pat@google.com","Google");
        cs.addContact(dan);
        cs.addContact(ham);
        cs.addContact(pat);
        //3 contacts total, with pageSize 2. Page 2 should have 1 result.
        assertEquals(1,cs.getQuery(2,2,null).size());
    }

    @Test
    public void testGetAllWithQuery() {
        ContactService cs = new ContactService("localhost",9200,"/tbook/contacts");
        Contact dan = new Contact("Dan","9548003264","dan@yahoo.com","Brightling");
        Contact ham = new Contact("Ham","9765332534","ham@yahoo.com","Brightling");
        Contact pat = new Contact("Pat","9769802334","Pat@google.com","Google");
        cs.addContact(dan);
        cs.addContact(ham);
        cs.addContact(pat);
        //pageSize 3, but only 2 contacts under Brightling
        assertEquals(2,cs.getQuery(3,1,"q=company:Brightling").size());
    }
}
