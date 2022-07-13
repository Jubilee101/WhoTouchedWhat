package com.hzhang.whotouchedwhat.controller;

import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.service.DirectoryParseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryParseController {

    private DirectoryParseService parseService;

    public DirectoryParseController() {
        parseService = new DirectoryParseService();
    }

    @GetMapping(value = "/parse")
    public Directory parseDirectory(@RequestParam(name = "file_path") String filePath) {
        parseService.parseDirectory(filePath);
        return parseService.getRoot();
    }

}
