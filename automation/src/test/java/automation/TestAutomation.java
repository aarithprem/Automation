package automation;

import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.gargoylesoftware.htmlunit.html.Keyboard;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.sun.javafx.scene.KeyboardShortcutsHandler;

public class TestAutomation {
	
	WebDriver driver;
	private static final Logger log = Logger.getLogger(TestAutomation.class.getName());
	String url="";
	
	@BeforeClass
	public void setUp(){
		driver = new FirefoxDriver();
		url="https://the-internet.herokuapp.com";
		driver.get(url);
		driver.manage().window().maximize();	
	}
	
	@Test
	public void scenario1() throws InterruptedException{
		//Navigate command is used here in all the test case to make sure test cases are running properly
		//When running as random individual tests
		driver.navigate().to(url);
		driver.findElement(By.linkText("Frames")).click();	
		driver.findElement(By.linkText("iFrame")).click();	
		//Switching to iframe
		WebElement iframeMsg = driver.findElement(By.xpath("//*[contains(@id, 'mce_0_ifr')]"));        
		driver.switchTo().frame(iframeMsg);

		WebElement body = driver.findElement(By.cssSelector("body"));
		body.clear();
		//Pressing ctrl+B for BOLD font
		Actions actions = new Actions(driver);
		actions.keyDown(Keys.CONTROL).sendKeys("b").build().perform();
		actions.keyUp(Keys.CONTROL);
		body.sendKeys("Lorem ipsum dolor sit amet");
		//Switching back to parent window
		driver.switchTo().defaultContent();
	
		driver.findElement(By.xpath("//button[@id='mceu_15-open']")).click();
		driver.findElement(By.xpath("//span[@class='mce-text']")).click();
		
		//Switching to frame for getting the text
		driver.switchTo().frame(iframeMsg);
		String text=body.getText();
		driver.switchTo().defaultContent();
		//Verifying whether text in the paragraph is not empty
		if(text.isEmpty()){
		Assert.assertTrue(true);
		}else{
			Assert.assertTrue(false,"Text in the paragraph body is not empty");
		}	
	}
	
	@Test
	public void scenario2(){
		//Navigate command is used here in all the test case to make sure test cases are running properly
		//When running as random individual tests
		driver.navigate().to(url);
		driver.findElement(By.linkText("Disappearing Elements")).click();		
		List<WebElement> list1 = driver.findElements(By.tagName("li"));		
		int size1=list1.size();	
		log.info(size1);		
		driver.navigate().refresh();
		List<WebElement> list2 = driver.findElements(By.tagName("li"));	
		int size2=list2.size();
		log.info(size2);		
		Assert.assertEquals(size1, size2-1,"Number of items in the list before refresh : "+size1+ " Number of items in the list after refresh "+size2);
		
	}
	@Test	
	public void scenario3(){
	
	driver.navigate().to(url);	
	driver.findElement(By.linkText("Hovers")).click();
	Actions actions = new Actions(driver);
	WebElement element = driver.findElement(By.cssSelector("div.row div#content.large-12.columns div.example div.figure img"));
	actions.moveToElement(element).build().perform();
	String usertoolTipText = driver.findElement(By.xpath("//div[@class='figcaption']/h5")).getText();
	String profileToolTipText=driver.findElement(By.xpath("//div[@class='figcaption']/a")).getText();
	Assert.assertEquals(usertoolTipText, "name: user1");
	Assert.assertEquals(profileToolTipText, "View profile");	
	}
	
	@Test
	public void scenario4() throws InterruptedException{
		
	driver.navigate().to("http://admin:admin@the-internet.herokuapp.com/basic_auth");
	log.info("http://admin:admin@"+url+"/basic_auth");
	String loginText=driver.findElement(By.xpath("//p")).getText();
	Assert.assertEquals(loginText, "Congratulations! You must have the proper credentials.");
	}
	
	@Test
	public void scenario6() throws InterruptedException{
			
	driver.navigate().to("http://www.jqueryrain.com/?5JwC1hwx");
	//Moving to parent iframe
	WebElement iframeMsg = driver.findElement(By.xpath("//*[contains(@class, 'sourceView')]"));        
	driver.switchTo().frame(iframeMsg);
	//Moving to child iframe
	WebElement iframeMsg1 = driver.findElement(By.xpath("//*[contains(@id, 'iframe1')]"));        
	driver.switchTo().frame(iframeMsg1);	
	
	driver.findElement(By.xpath("//ul[@class='tabAnchor']/li[2]/a")).click();
	WebDriverWait wait = new WebDriverWait(driver, 40);
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='tabs-2' and @class='tabContent']/p")));
	String text= driver.findElement(By.xpath("//div[@id='tabs-2' and @class='tabContent']/p")).getText();
	
	Assert.assertTrue(text.contains("pre loader rolling"), "pre loader rolling text is not displayed");
	WebElement element1 = driver.findElement(By.xpath("//ul[@class='tabAnchor']/li[1]/a"));
	wait.until(ExpectedConditions.elementToBeClickable(element1));
	element1.click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr/td/div/div/div[1]/ul/li")));
	
	List<WebElement> allElements = driver.findElements(By.xpath("//table/tbody/tr/td/div/div/div[1]/ul/li")); 
	
	for(WebElement element: allElements){
		System.out.println(element.getText());
	}
	Assert.assertTrue(allElements.size()>0,"Verified that the unordered list has list items");
	Assert.assertEquals(allElements.get(0).getText(),"list 1");
	Assert.assertEquals(allElements.get(1).getText(),"list 2");
	Assert.assertEquals(allElements.get(2).getText(),"list 3");
	Assert.assertEquals(allElements.get(3).getText(),"list 4");
	}
	
	@AfterClass
	public void tearDown(){
		driver.quit();
	}

}
