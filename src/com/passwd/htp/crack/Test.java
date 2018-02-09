package com.passwd.htp.crack;

import java.io.IOException;
import java.net.MalformedURLException;

public class Test {
	public static void main(String[] args) throws MalformedURLException, IOException {
		Task task = new Task();
		System.out.println(task.getResult(386));
	}
}
