package com.hzhang.whotouchedwhat.serviceTest;

import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.service.DirectoryParseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DirectoryParseServiceTest {
    private DirectoryParseService parseService;
    @Before
    public void setup() {
        parseService = new DirectoryParseService();
    }
    @Test
    public void testDirectoryBuilding() {
        String address = "E:\\Spring 2022\\rec07-gui";
        parseService.parseDirectory(address);
        Directory root = parseService.getRoot();
        List<Directory> head = root.getSubDirectories();
        System.out.println(head.size());
        Assert.assertTrue(head.size() != 0);
    }
}
