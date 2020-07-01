package org.testscan;

import static org.junit.Assert.assertTrue;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class AppTest {
    @Test
    public void test0() {
        assertTrue( true );
    }

    @Test
    public void test1() {
        assertTrue( true );
    }

    @Test
    public void test2() {
        assertTrue( true );
    }

    @Test
    public void shouldListTwoTestFiles() {
        List<File> res = new TestScan().listTestFiles("src/test/java");
        
        List<String> names = res.stream()
            .map(File::getName)
            .collect(Collectors.toList());
        
        assertThat(names).containsExactlyInAnyOrder("AppTest.java","DummyTest.java");
    }

    @Test
    public void shouldScanTestFiles() {
        List<String> res = new TestScan().scan("src/test/java");

        assertThat(res).containsExactlyInAnyOrder(
            "AppTest#test0",
            "AppTest#test1",
            "AppTest#test2",
            "AppTest#shouldListTwoTestFiles",
            "AppTest#shouldScanTestFiles",
            "DummyTest#test0",
            "DummyTest#test1");
    }
}
