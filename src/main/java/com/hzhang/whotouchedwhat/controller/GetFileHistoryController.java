package com.hzhang.whotouchedwhat.controller;

import com.hzhang.whotouchedwhat.model.AuthorHistory;
import com.hzhang.whotouchedwhat.service.GetFileHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetFileHistoryController {
    private GetFileHistoryService fileHistoryService;

    public GetFileHistoryController() {
        fileHistoryService = new GetFileHistoryService();
    }

    @GetMapping(value = "/history")
    public AuthorHistory getHistory(@RequestParam(name = "file_path") String filePath,
                                    @RequestParam(name = "repo_path") String repoPath) {
        fileHistoryService.getAuthorHistory(filePath, repoPath);
        return fileHistoryService.getHistory();
    }
}
