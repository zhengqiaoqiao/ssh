import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qiao.service.UserService;
import com.qiao.util.PoolUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
			locations={
					"classpath*:applicationContext.xml",
					"classpath*:spring-mvc-servlet.xml"
			}
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class UserTest {
	@Resource
	private UserService userService;
	@Resource
	private PoolUtil poolUtil;
	@Before
	public void before() {
		poolUtil.start();
	}
	@After
	public void after() {
		poolUtil.stop();
	}
	//测试hibernate一级缓存
//	@Test
	public void testHibernateFirstCache() {
		System.out.println("hibernate:");
		try {
			userService.testHibernateFirstCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("##########################");
	}
//	@Test
	public void atest() throws Exception {
		System.out.println("原生的jdbc:");
		for(int i=0;i<3;i++){
			userService.getAllUsersByMyJDBC();;
		}
		System.out.println("##########################");
	}
	
//	@Test
	public void btest() throws Exception {
		System.out.println("spring的jdbc:");
		for(int i=0;i<3;i++){
			userService.getAllUsersByJDBC();;
		}
		System.out.println("##########################");
	}
	
	
//	@Test
	public void ctest() {
		System.out.println("hibernate无二级缓存:");
		for(int i=0;i<3;i++){
			userService.getAllUsersByHibernate2();
		}
		System.out.println("##########################");
	}
	
//	@Test
	public void dtest() {
		System.out.println("hibernate有二级缓存:");
		for(int i=0;i<3;i++){
			userService.getAllUsersByHibernate();
		}
		System.out.println("##########################");
	}
	
	@Test
	public void etest() {
		userService.updateUserAge("8a80bc0b501be5ff01501be6118a0000", 11);

	}
	
}
