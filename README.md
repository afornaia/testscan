# testscan

A simple @Test methods scanner based on JavaParser. It looks for all the java files inside a dir (e.g. src/test/java) having its name containing "Test" keyword. For each file, it gives all the methods with the @Test annotation: *TestClass#testMethod*. This can be used with *mvn test* to run a single test case method:

```text
mvn test -Dtest=TestClass#testMethod
```

## Build

This will produce an executable jar with all the dependencies (*target/testscan.jar*)

```text
mvn package
```

## Run

Given a maven project dir, we can use testscan.jar to list test cases inside the *src/test/java* folder.

```text
java -jar target/testscan.jar <MAVEN_PROJECT_DIR>/src/test/java
```

This is an output example, running the tool on this project.

```text
AppTest#test0
AppTest#test1
AppTest#test2
AppTest#shouldListTwoTestFiles
AppTest#shouldScanTestFiles
DummyTest#test0
DummyTest#test1
```
