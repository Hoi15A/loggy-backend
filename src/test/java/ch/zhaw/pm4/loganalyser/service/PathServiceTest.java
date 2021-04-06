package ch.zhaw.pm4.loganalyser.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class PathServiceTest {
    @Autowired
    PathService pathService;

    @Test
    void test() {
        System.out.println(Arrays.deepToString(pathService.getRootFolder()));
        System.out.println(Arrays.deepToString(pathService.getContentOfFolder("C:\\Users")));
    }
}
