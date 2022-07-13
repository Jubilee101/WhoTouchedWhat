package com.hzhang.whotouchedwhat.serviceTest;

import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.service.DirectoryParseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.util.UriEncoder;

import java.util.List;

public class DirectoryParseServiceTest {
    private DirectoryParseService parseService;
    @Before
    public void setup() {
        parseService = new DirectoryParseService();
    }
    @Test
    public void testDirectoryBuilding() {
        String address = "E:\\Spring 2022\\17645\\Project\\milestone2\\group-project-s22-f-r-i-d-a-y";
        System.out.println(UriEncoder.encode("AIContainer/miniprogram/app.js"));
        parseService.parseDirectory(address);
        Directory root = parseService.getRoot();
        List<Directory> head = root.getSubDirectories();
        Assert.assertTrue(head.size() != 0);
    }
}
