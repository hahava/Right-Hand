package com.righthand;

import com.righthand.membership.service.MembershipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RighthandApplicationTests {

	@Autowired
	MembershipService membershipService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void checkFileGrp() throws Exception {
		assertThat(membershipService.checkFileGrpSeq(29), is(nullValue()));
	}


}
