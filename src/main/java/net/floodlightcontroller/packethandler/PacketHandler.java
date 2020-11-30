package net.floodlightcontroller.packethandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFFactories;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFRequest;
import org.projectfloodlight.openflow.protocol.OFStatsReply;
import org.projectfloodlight.openflow.protocol.OFStatsRequest;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListenableFuture;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFMessageWriter;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.forwarding.Forwarding;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.routing.IRoutingDecision;



public class PacketHandler implements IOFMessageListener, IOFMessageWriter,
		IFloodlightModule {
	
	public static final int NULL = -1;
	public static final int IDENTIFY = 0;
	public static final int DROP = 1;
	
	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	protected OFFactory my13Factory;
	protected Map<LPEC, FSM> map;
	protected Integer count;
	

	@Override
	public String getName() {
		return PacketHandler.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		   Collection<Class<? extends IFloodlightService>> l =
			        new ArrayList<Class<? extends IFloodlightService>>();
		   l.add(IFloodlightProviderService.class);
		   return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
	    floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    logger = LoggerFactory.getLogger(PacketHandler.class);
	    my13Factory =OFFactories.getFactory(OFVersion.OF_13);
	    map = new HashMap<LPEC, FSM>();
	    count = 0;
	    
	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);

	}

	@Override
	public boolean write(OFMessage m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<OFMessage> write(Iterable<OFMessage> msgList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R extends OFMessage> ListenableFuture<R> writeRequest(
			OFRequest<R> request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <REPLY extends OFStatsReply> ListenableFuture<List<REPLY>> writeStatsRequest(
			OFStatsRequest<REPLY> request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        Ethernet eth =
                IFloodlightProviderService.bcStore.get(cntx,
                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
        
        // TODO: update policies in switches
        // ------ add policy in switch -- packout - create a rule
        // rule - could set time out
        
        //Forwarding f;
        //f.processPacketInMessage(sw, (OFPacketIn) msg, (IRoutingDecision) DROP, cntx);
        

        /*
        Match match = my13Factory.buildMatchV3()
        		//.setExact(MatchField.ETH_SRC, MacAddress.of("00:00:00:00:00:01"))
        		//.setExact(MatchField.ETH_SRC, MacAddress.of("00:00:00:00:00:02"))
        		.setExact(MatchField.ETH_TYPE, EthType.IPv4)
        		.setExact(MatchField.IPV4_SRC, IPv4Address.of("10.0.0.1"))
        		.setExact(MatchField.IPV4_DST, IPv4Address.of("10.0.0.2"))
        		.build();
        OFFlowMod flowMod = (OFFlowMod) my13Factory.buildFlowModify()
        		.setMatch(match)
        		.setHardTimeout((short)0)
        		.setIdleTimeout((short)0)
        		.setPriority(Short.MAX_VALUE)
        		//.setActions(Collections.singletonList((OFAction) my13Factory.actions().output(OFPort.ANY, 0xffFFffFF)))
        		.build();
        sw.write(flowMod);
        */
        
     
        if (eth.getEtherType() == EthType.IPv4) {
        	IPv4 ipv4 = (IPv4) eth.getPayload();
        	count ++;
        	
        	LPEC lpec = new LPEC(my13Factory, ipv4);
        	if (!map.containsKey(lpec)) {
        		FSM fsm = new FSM();
        		map.put(lpec, fsm);
        	}
        	
        	//String eventName = "infected";
        	int action;
        	boolean eventValue = false;
        	if (count > 3 && count < 6)
        		eventValue = true;
        	
        	action = map.get(lpec).eventHandler("infected", eventValue);
        	
        	action = map.get(lpec).getAction();
        	switch (action) {
        		case IDENTIFY:
        		{
                    OFFlowMod flowMod = (OFFlowMod) my13Factory.buildFlowModify()
                    		.setMatch(lpec.match)
                    		.setHardTimeout((short)1)
                    		.setIdleTimeout((short)1)
                    		.setPriority(Short.MAX_VALUE)
                    		.setActions(Collections.singletonList((OFAction) my13Factory.actions().output(OFPort.FLOOD, 0xffFFffFF)))
                    		.build();
                    sw.write(flowMod);
        			break;
        		}
	        	case DROP: 
	        	{
                    OFFlowMod flowMod = (OFFlowMod) my13Factory.buildFlowModify()
                    		.setMatch(lpec.match)
                    		.setHardTimeout((short)0)
                    		.setIdleTimeout((short)0)
                    		.setPriority(Short.MAX_VALUE)
                    		.build();
                    sw.write(flowMod);
        			break;
	        	}
	        	case NULL:
	        		break;
        		default:
        			break;
        	}
        	
        	/*
            OFFlowMod flowMod = (OFFlowMod) my13Factory.buildFlowModify()
            		.setMatch(lpec.match)
            		.setHardTimeout((short)0)
            		.setIdleTimeout((short)0)
            		.setPriority(Short.MAX_VALUE)
            		//.setActions(Collections.singletonList((OFAction) my13Factory.actions().output(OFPort.ANY, 0xffFFffFF)))
            		.build();
            sw.write(flowMod);
        	*/
        	
//        	Match packetInMatch = my13Factory.buildMatchV3()
//        			.setExact(MatchField.IPV4_SRC, ipv4.getSourceAddress())
//        			.setExact(MatchField.IPV4_DST, ipv4.getDestinationAddress())
//        			.build();
        	
        	logger.info("> IPV4 {}", action);
        	logger.info("> IPV4 {}", count);
        	logger.info("> IPV4 {} -> {}", ipv4.getSourceAddress(), ipv4.getDestinationAddress());
        	
        	 
        	
        	return Command.CONTINUE;
        	
        } else if (eth.getEtherType() == EthType.ARP) {
        	ARP arp = (ARP) eth.getPayload();
            logger.info("> ARP");
        }
        
       

        return Command.CONTINUE;
	}

}
