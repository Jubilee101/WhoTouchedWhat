package com.hzhang.whotouchedwhat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.service.DirectoryParseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DirectoryParseController {

    private DirectoryParseService parseService;

    public DirectoryParseController() {

    }

    @GetMapping(value = "/parse")
    public Directory parseDirectory(@RequestParam(name = "file_path") String filePath) {
        parseService = new DirectoryParseService();
        parseService.parseDirectory(filePath);
        Directory root = parseService.getRoot();
        return root;
    }

}
