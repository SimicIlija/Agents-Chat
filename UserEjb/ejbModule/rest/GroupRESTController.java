package rest;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.GroupMgmt;
import beans.GroupMgmtLocal;
import jms_messages.LastChatsResMsg;
import jms_messages.GroupChatMsg.GroupChatMsgReq;
import jms_messages.GroupChatMsg.GroupChatMsgReqType;

@Path("/group")
@Stateless
public class GroupRESTController {
	
	@EJB
	private GroupMgmtLocal groupMngmt;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getGroups(GroupChatMsgReq msg) {
		if(msg.getType() == GroupChatMsgReqType.NEW_CHAT) {
			groupMngmt.createNew(msg.getChat(), msg.getAdmin(), msg.getMemebers());
		}
	}
}
