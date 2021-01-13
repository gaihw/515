package com.zmj;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ZmjApplicationTests {

//	@LocalServerPort
//	private int port ;
//	private URL base;
//
//	@Autowired
//	private TestRestTemplate testRestTemplate;
//
//	@Before
//	public void setUp() throws Exception{
//		this.base = new URL("http://localhost:" + port +"/api/btc/getpeerinfo");
//	}
//
//	@Test
//	public void getblockcount(){
//		ResponseEntity<String> response = testRestTemplate.getForEntity(base.toString(),String.class);
//		System.out.println(response.getBody());
//	}
}
