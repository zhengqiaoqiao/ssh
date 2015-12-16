import java.util.List;

import javax.annotation.Resource;

import junit.textui.TestRunner;

import org.hibernate.LockMode;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qiao.dao.TestDao;
import com.qiao.model.pojo.Student;
import com.qiao.model.pojo.User;
import com.qiao.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
			locations={
					"classpath*:applicationContext.xml",
					"classpath*:spring-mvc-servlet.xml"
			}
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class pTest {
	@Resource
	private TestDao testDao;
	
	@Before
	public void before() {
		
	}
	@After
	public void after() {
		
	}
	
//	@Test
	public void testHibernateLeftQuery() throws Exception {
		List<Student> list = testDao.getStudentInfo();
//		String msg = JsonUtil.obj2json(list);
//		System.out.println(msg);
		for(Student s : list){
			String msg = s.getName()+"------"+s.getRoom().getName();
			System.out.println(msg);
		}
		
	}
	
//	测试hibernate关联关系
//	@Test
	public void testDeleteStudent() throws Exception {
		String hql = "delete from ClassRoom t where t.id='101'";
		testDao.getHibernateTemplate().bulkUpdate(hql);
	}
  
	//乐观锁测试
	@Test
	public void testPessimisticLock() throws Exception {
		Thread t1 = new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				String hql = "from Student where id='20150001'";
				Student sd = (Student) testDao.getHibernateTemplate().find(hql).get(0);
				sd.setAge(101);
				testDao.getHibernateTemplate().update(sd);
			}
			
		});
		t1.start();
		
		Thread t2 = new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				String hql = "from Student where id='20150001'";
				Student sd = (Student) testDao.getHibernateTemplate().find(hql).get(0);
//				String hql1 = "update Student t set age = 0 where id='20150001'";
//				testDao.getHibernateTemplate().bulkUpdate(hql1);
				sd.setAge(105);
				testDao.getHibernateTemplate().update(sd);
			}
			
		});
		
		t2.start();
		while(1==1){
			if(t1.isAlive()||t2.isAlive())
				continue;
			else
				break;
		}
		
	}
}
