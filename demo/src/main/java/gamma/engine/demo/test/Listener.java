package gamma.engine.demo.test;

import gamma.engine.core.ApplicationListener;

public class Listener implements ApplicationListener {

	// TODO: Files under META-INF/services replace each other in jars with dependencies

	@Override
	public void onInit() {
		System.out.println("OnInit");
	}

	@Override
	public void onTerminate() {
		System.out.println("Termination");
	}
}
