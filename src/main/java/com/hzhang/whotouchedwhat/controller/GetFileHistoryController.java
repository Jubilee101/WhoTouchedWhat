package com.hzhang.whotouchedwhat.controller;

import com.hzhang.whotouchedwhat.model.Author;
import com.hzhang.whotouchedwhat.service.GetFileHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFileHistoryController {
    private GetFileHistoryService fileHistoryService;

    public GetFileHistoryController() {
    }

    @GetMapping(value = "/history")
    public List<Author> getHistory(@RequestParam(name = "file_path") String filePath,
                                   @RequestParam(name = "repo_path") String repoPath) {
        fileHistoryService = new GetFileHistoryService();
        fileHistoryService.getAuthorHistory(filePath, repoPath);
        return fileHistoryService.getHistory();
    }
}
