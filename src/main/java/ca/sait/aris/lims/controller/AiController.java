package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.req.AiChatReqDTO;
import ca.sait.aris.lims.dto.resp.AiChatRespDTO;
import ca.sait.aris.lims.service.AiService;

public class AiController {

    private final AiService aiService = new AiService();

    public RespResult<AiChatRespDTO> handleAiChat(AiChatReqDTO reqDto) {
        try {
            if (reqDto == null || reqDto.getPrompt() == null || reqDto.getPrompt().trim().isEmpty()) {
                return RespResult.error(400, "Prompt cannot be empty.");
            }
            AiChatRespDTO result = aiService.processAiQuery(reqDto.getPrompt());
            return RespResult.success(result);
        } catch (Exception e) {
            System.err.println("[AiController] handleAiChat failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to process AI chat request.");
        }
    }
}