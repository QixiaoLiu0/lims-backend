package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.ChatQueryRespDTO;
import ca.sait.aris.lims.service.ChatService;

public class ChatController {

    private final ChatService chatService = new ChatService();

    public RespResult<ChatQueryRespDTO> chat(String userPrompt) {
        try {
            ChatQueryRespDTO result = chatService.processChat(userPrompt);
            return RespResult.success(result);
        } catch (Exception e) {
            System.err.println("[ChatController] chat failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to process chat request.");
        }
    }
}