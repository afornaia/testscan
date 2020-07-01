package org.testscan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Look for all the java files inside a dir (e.g. src/test/java)
 * having name containing "Test". For each file, it gives
 * all the methods with the @Test annotation:
 *  
 *  TestClass#testMethod
 * 
 * that can be used with mvn test to run a single test case method:
 *  
 *  mvn test -Dtest=TestClass#testMethod
 */
public class TestScan {
    List<String> scanResults = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: java -jar testscan.jar <path_to_tests>");
            System.out.println("e.g: java -jar testscan.jar src/test/java");
            System.exit(1);
        }
        String testDir = args[0];
        new TestScan().scan(testDir).forEach(System.out::println);
    }

    /**
     * Scan input dir for @Test annotated methods
     * @param testDir
     * @return List of methods in the form: TestClass#testMethod
     */
    public List<String> scan(String testDir) {
        for (File testFile : listTestFiles(testDir)) {
            try {
                CompilationUnit parsed = JavaParser.parse(testFile);
                parsed.accept(new ClassVisitor(), "");
            } catch (FileNotFoundException e) {
                e.printStackTrace(); // continue with next file
            }
        }
        return scanResults;
    }

    /**
     * Return a list of *Test*.java files inside a given dir (recursively)
     * @param dir
     * @return
     */
    List<File> listTestFiles(String dir) {
        List<File> result = new ArrayList<>();    
        try (Stream<Path> walk = Files.walk(Paths.get(dir))) {
            result = walk.filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(fn -> fn.getName().endsWith(".java"))
                .filter(fn -> fn.getName().contains("Test"))
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace(); // give an empty list
        }
        
        return result;
    }

    /**
     * JavaParser Visitor: used to give the className to a MethodVisitor
     */
    private class ClassVisitor extends VoidVisitorAdapter<String> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, String arg) {
            n.accept(new MethodVisitor(), n.getNameAsString());
        }
    }

    /**
     * JavaParser Visitor: add methods with @Test to the scanResults
     */
    private class MethodVisitor extends VoidVisitorAdapter<String> {
        @Override
        public void visit(MethodDeclaration n, String arg) {
            String className = arg;
            if (n.getAnnotationByName("Test").isPresent()) {
                scanResults.add(className + "#" + n.getName());
            }
        }
    }

}