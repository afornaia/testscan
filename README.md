# testscan

A simple @Test methods scanner based on JavaParser. It looks for all the java files inside a dir (e.g. src/test/java) having name containing "Test" keyword. For each file, it gives all the methods with the @Test annotation: *TestClass#testMethod*. This can be used with *mvn test* to run a single test case method:

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

An example of running the tool on this project:

```text
java -jar target/testscan.jar ./src/test/java
```

Output:

```text
AppTest#test0
AppTest#test1
AppTest#test2
AppTest#shouldListTwoTestFiles
AppTest#shouldScanTestFiles
DummyTest#test0
DummyTest#test1
```

Running a single test case with mvn test:

```text
mvn test -Dtest=DummyTest#test1
```

Output:

```text
[...]

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.testscan.DummyTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.087 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[...]
```
