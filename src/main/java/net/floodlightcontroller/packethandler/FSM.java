package net.floodlightcontroller.packethandler;

import net.floodlightcontroller.packet.IPv4;

enum State {
	INITIAL {
		@Override
		State event(String eventName, boolean isTrue) {
			if (eventName == "infected") {
				if (isTrue)
					return INFECTED;
			}
			return INITIAL;
		}
	},
	
	INFECTED {
		@Override
		State event(String eventName, boolean isTrue) {
			if (eventName == "infected") {
				if (!isTrue)
					return INITIAL;
			}
			return INFECTED;
		}
	};
	
	
	abstract State event(String eventName, boolean eventValue);
}



public class FSM extends AFSM {

	public State state;
	
	public FSM () {
		state = State.INITIAL;
	}
	
	
	@Override
	public int eventHandler(String eventName, boolean eventValue) {
		state = state.event(eventName, eventValue);
		if (state == State.INITIAL) {
			return IDENTIFY;
		} else if (state == State.INFECTED) {
			return DROP;
		}
		return NULL;
	}


	@Override
	public int getAction() {
		if (state == State.INITIAL) {
			return IDENTIFY;
		} else if (state == State.INFECTED) {
			return DROP;
		}
		return NULL;
	}

}






