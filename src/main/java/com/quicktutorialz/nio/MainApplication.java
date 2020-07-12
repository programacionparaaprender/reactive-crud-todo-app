package com.quicktutorialz.nio;

import com.mawashi.nio.jetty.ReactiveJ;
import com.quicktutorialz.nio.endpoints.v1.ToDoEndpoints;


public class MainApplication {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new ReactiveJ().port(8383)
		.endpoints(new com.quicktutorialz.nio.endpoints.v1.ToDoEndpoints())
		.start();
	}

}
