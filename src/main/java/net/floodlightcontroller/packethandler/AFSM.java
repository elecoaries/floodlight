package net.floodlightcontroller.packethandler;



public abstract class AFSM {
	
	public static final int NULL = -1;
	
	public static final int IDENTIFY = 0;
	
	public static final int DROP = 1;
	
	public abstract int eventHandler(String eventName, boolean eventValue);
	
	public abstract int getAction();
}
