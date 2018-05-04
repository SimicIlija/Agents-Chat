package rest;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.GroupMgmtLocal;

import jms_messages.GroupChat.GroupChatReqMsg;
import jms_messages.GroupChat.GroupChatReqMsgType;
import jms_messages.GroupChat.GroupChatResMsg;
import jms_messages.GroupChat.GroupChatResMsgType;
import model.Chat;

@Path("/group")
@Stateless
public class GroupRESTController {
	
	@EJB
	private GroupMgmtLocal groupMngmt;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GroupChatResMsg GroupChatREST(GroupChatReqMsg msg) {
		if(msg.getType() == GroupChatReqMsgType.NEW_CHAT) {
			Chat chat = groupMngmt.createNew(msg.getChat(), msg.getAdmin(), msg.getMemebers());
			if(chat != null)
				return new GroupChatResMsg(GroupChatResMsgType.CREATED, msg.getSessionId());
		}
		return new GroupChatResMsg(GroupChatResMsgType.ERROR, msg.getSessionId());
	}
}
