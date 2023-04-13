package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	private String strTestServer = "abc@de.com";
	
	private static final String[] TEST_EMAILS = { "ab@bc.com", "a.b@c.org",
			"abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd" };
	
	private EmailConcrete email;
	
	
	@Before			// Before is the set up that runs before executing each test case
	public void setUpEmailTest() throws Exception {	
		
		email = new EmailConcrete();
		
	}
	
	@After			// After is tear down which executes for each test case
	public void tearDownEmailTest() throws Exception {
		
	}
	
	
	// Test addBcc
	@Test
	public void testArrayAddBcc() throws Exception {		
		email.addBcc(TEST_EMAILS);		// Test using an array of emails
		
		assertEquals(3, email.getBccAddresses().size());	// assert matches expected with outcome
	}
	
	@Test
	public void testStringAddBcc() throws Exception {
		email.addBcc("abc@def");
		List<InternetAddress>bcc=email.getBccAddresses();		// Test single email
		
		assertEquals("abc@def", bcc.get(0).toString());		// assert matches expected with outcome
	}
	
	// Test addCc
	@Test
	public void testArrayAddCc() throws Exception {
		email.addCc(TEST_EMAILS);		// Test using an array of emails
		
		assertEquals(3, email.getCcAddresses().size());		// assert matches expected with outcome
		
	}
	
	@Test
	public void testStringAddCc() throws Exception {
		email.addCc("abc@def");				// Test single email
		List<InternetAddress>cc=email.getCcAddresses();
		
		assertEquals("abc@def", cc.get(0).toString());		// assert matches expected with outcome
	}
	
	@Test (expected = EmailException.class)
	public void testCheckException() throws EmailException {
		String invalidemail = "abc";
		email.addCc(invalidemail);			// Should Fail
	}
	
	// Test addHeader
	@Test
	public void testAddHeaderPass() {
		Map<String, String> ht = new Hashtable<String,String>();
		ht.put("Priorty", "1");
		ht.put("Notify", "ab@bc.com");
		ht.put("Mailer", "Sendmail");
		
		for (Iterator<Map.Entry<String, String>> items = ht.entrySet().iterator(); items.hasNext();)
		{
			Map.Entry<String, String> entry = items.next();
			String name = entry.getKey();
			String value = entry.getValue();
			this.email.addHeader(name, value);
		}
		
		assertEquals(ht.size(), this.email.getHeaders().size());
		assertEquals(ht, this.email.getHeaders());
	}
	
	@Test
	public void testAddHeader() {
		Map<String, String> htFail = new Hashtable<String,String>();
		htFail.put("Mailer", "");
		htFail.put("Priorty", "");
		htFail.put("", "ab@bc.com");
		
		Map<?, ?> arrEx = new Hashtable<Object, Object>();
		for(Iterator<Map.Entry<String, String>> items = htFail.entrySet().iterator(); items.hasNext();)
		{
			Map.Entry<String, String> element = items.next();
			try 
			{
				String name = element.getKey();
				String value = element.getValue();
				
				this.email.addHeader(name, value);			// Should fail
			}
			catch (IllegalArgumentException e)
			{
				assertTrue(true);
			}
		}
		
		assertEquals(arrEx.size(), this.email.getHeaders().size());
		assertEquals(arrEx.toString(), this.email.getHeaders().toString());
	}
	
	// Test addReplyTo
	@Test
	public void testAddReplyTo() throws Exception {
		List<InternetAddress> rt = new ArrayList<InternetAddress>();
		rt.add(new InternetAddress("ab@bc.com"));
		rt.add(new InternetAddress("a.b@c.org"));
		rt.add(new InternetAddress("abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd"));
		
		for (int i = 0; i < TEST_EMAILS.length; i++)
		{
			this.email.addReplyTo(TEST_EMAILS[i]);
		}
		
		assertEquals(rt.size(), this.email.getReplyToAddresses().size());
		assertEquals(rt.toString(), this.email.getReplyToAddresses().toString());
	}
	
	// Test buildMimeMessage
	@Test
	public void testBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
		email.setSmtpPort(4);
		email.setFrom("ab@bc.com");
		email.addTo("a.b@c.org");
		email.setSubject("Testing");
		
		String header = "1234567890 1234567890 1234567890 1234567890 1234567890 1234567890";
		email.addHeader("X-LongHeader", header);
		
		email.buildMimeMessage();
		
		final MimeMessage msg = email.getMimeMessage();
		msg.saveChanges();
		
		assertEquals(msg, email.getMimeMessage());
	}
	
	@Test (expected = EmailException.class)
	public void testTwoBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
		email.setSmtpPort(4);
		email.setFrom("ab@bc.com");
		
		email.buildMimeMessage();	// expected fail
	}
	
	@Test (expected = EmailException.class)
	public void testThreeBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
		email.setSmtpPort(4);
		
		email.buildMimeMessage();	// expected fail
	}
	
	@Test (expected = EmailException.class)
	public void testFourBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
	
		email.buildMimeMessage();	// expected fail
	}
	
	@Test
	public void testFiveBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
		email.setSmtpPort(4);
		email.setFrom("ab@bc.com");
		email.addTo("a.b@c.org");
		email.setSubject("Testing");
		
		email.setCharset("ISO-8859-1");
		email.setContent("test content", "text/plain");
		email.buildMimeMessage();
		
		final MimeMessage msg = email.getMimeMessage();
		msg.saveChanges();
		
		assertEquals(msg, email.getMimeMessage());			
	}
	
	@Test 
	public void testSixBuildMimeMessage() throws Exception {
		email.setHostName(strTestServer);
		email.setSmtpPort(4);
		email.setFrom("ab@bc.com");
		email.addTo("a.b@c.org");
		email.setSubject("Testing");
		
		email.setContent("test content", "text/plain");
		email.buildMimeMessage();
		
		final MimeMessage msg = email.getMimeMessage();
		msg.saveChanges();
		
		assertEquals(msg, email.getMimeMessage());
	}
	
	// Test getHostName
	@Test
	public void testGetHostNameWithoutSession() {
		email.setHostName("localhost");
		
		String hostname = email.getHostName();		// Creates hostname
		assertEquals("localhost",hostname);		// Compares name "localhost" with hostname set with getHostName function 
	}
	
	@Test
	public void testGetHostNameNull() {
		email.setHostName(null);
		
		String hostname = email.getHostName();
		assertEquals(null,hostname);		// Compares results for NULL host names
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetHostNameWithSession() {
		email.setHostName("localhost");
		Properties prop = new Properties();
		prop.setProperty(Email.MAIL_HOST, "localhost");
		Session scn = Session.getInstance(prop);
		email.setMailSession(scn);			// Sets mailsession with added properties
		
		String hostname = email.getHostName();
		assertEquals("localhost",hostname);		// Compares name "localhost" with hostname set with getHostName function 
	}
		
	// Test getMailSession				
	@SuppressWarnings("deprecation")
	@Test
	public void testGetMailSession() throws EmailException {
		Properties prop = new Properties();
		prop.setProperty(Email.MAIL_HOST, "localhost");
		Session scn = Session.getInstance(prop);
		
		this.email.setMailSession(scn);
		assertEquals(scn, this.email.getMailSession());	 // Compares session obj scn with getMailSession results
	}
	
	@SuppressWarnings("deprecation")
	@Test (expected = EmailException.class)
	public void testNullGetMailSession() throws EmailException {
		Properties prop = new Properties();
		prop.setProperty(Email.MAIL_HOST, "localhost");
		this.email.getMailSession();					// Expected fail with missing parameters, falls into differing tree
	}
	
	// Test getSentDate
	@Test
	public void testGetSentDate() {
		Date date = Calendar.getInstance().getTime();
		this.email.setSentDate(date);
		assertEquals(date, this.email.getSentDate());
		
		this.email.setSentDate(null);
		Date sentDate = this.email.getSentDate();
		assertTrue(Math.abs(sentDate.getTime() - date.getTime()) < 1000);		// checks if valid time given
	}
	
	// Test getSocketConnectionTimeout
	@Test
	public void testgetSocketConnectionTImeout() {
		email.setSocketConnectionTimeout(0);		// defines Sockettime as 0
		
		assertEquals(0, email.getSocketConnectionTimeout());	// check getSocketConnTime with setSockConntime of 0	
	}
	
	// Test setFrom
	@Test
	public void testSetFrom() throws Exception {
		email.setFrom("abc@def");				// Test single email
		InternetAddress from = email.getFromAddress();		// defines object intAddress
		
		assertEquals("abc@def", from.getAddress());		// compares getAddress with setFrom
	}
}
