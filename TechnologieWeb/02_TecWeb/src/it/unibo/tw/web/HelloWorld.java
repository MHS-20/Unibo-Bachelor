package it.unibo.tw.web;

public class HelloWorld {

	public static final String GREETING = "Hello World, ";
	private String name;

	public HelloWorld(String name) {
		this.name = name; 
	}
	
	public HelloWorld() {
		this("anonimo"); 
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String sayIt() {
		return GREETING + this.name;
	}
}