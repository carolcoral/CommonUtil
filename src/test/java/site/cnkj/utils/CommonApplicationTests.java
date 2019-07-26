package site.cnkj.utils;

import org.junit.Test;

public class CommonApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args){
		int rollInterval = 5;
		long nowTime = DateUtil.getCurrentTime();
		long timeDiscrepancy = nowTime%(rollInterval*60000)/1000;
		System.out.println(timeDiscrepancy);
	}

}
