package net.floodlightcontroller.packethandler;

import net.floodlightcontroller.packet.IPv4;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.EthType;

public class LPEC {

	public Match match;
	
	public LPEC (OFFactory factory, IPv4 pkt) {
		match = factory.buildMatchV3()
				.setExact(MatchField.ETH_TYPE, EthType.IPv4)
				.setExact(MatchField.IPV4_SRC, pkt.getSourceAddress())
				.setExact(MatchField.IPV4_DST, pkt.getDestinationAddress())
				.build();
	}
	
	 @Override
	 public boolean equals (Object o) {
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof LPEC)) { 
            return false; 
        } 
        
        LPEC l = (LPEC) o;
        
        return match.equals(l.match);
        
	 }
	
}
