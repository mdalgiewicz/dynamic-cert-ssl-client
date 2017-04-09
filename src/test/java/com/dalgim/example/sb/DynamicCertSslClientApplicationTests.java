package com.dalgim.example.sb;

import com.dalgim.example.sb.client.BookClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicCertSslClientApplicationTests {

	@Autowired
	BookClient bookClient;

	@Test
	public void contextLoads() {
		bookClient.getAllBook();
	}

}
