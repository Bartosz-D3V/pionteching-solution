[![CI](https://github.com/Bartosz-D3V/pionteching-solution/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Bartosz-D3V/pionteching-solution/actions/workflows/build.yml)
[![SAST_Semgrep](https://github.com/Bartosz-D3V/pionteching-solution/actions/workflows/semgrep.yml/badge.svg)](https://github.com/Bartosz-D3V/pionteching-solution/actions/workflows/semgrep.yml)

# ING Zielony Kod Contest
This is a Java-based project for participating in programming contest organised by ING - Zielona Tesla za Zielony Kod.

It provides a solution for all three programming problems provided in the contest description.

## Technologies Used
The project is built using the following technologies:

* Java 17
* Micronaut as REST framework
* JUnit 5 for testing
* Gatling and Java JMH for performance testing

## SAST Used
* [Semgrep](https://semgrep.dev/)
* [CodeQL](https://codeql.github.com/)

## Folder Structure
The project is organized into several folders, each containing a different aspect of the project:

* **src/main/java**: This folder contains the Java code for the project with each problem solution in a separate java package.
* **src/test/java**: This folder contains the test code for the project, including the integration tests.
* **src/gatling/java**: This folder contains Gatling performance testing code and simulations.
* **src/gatling/resources**: This folder contains the test JSON data for the project, organized by programming task.
* **src/jmh/java**: This folder contains JMH performance testing code and simulations.

## Getting Started
To use this project, you should have Java 17 or later installed on your system as well as Gradle 8.0.1.
If Gradle is not available on your system, Gradle Wrapper can be used.

Clone this repository to your local machine:
```shell
git clone https://github.com/Bartosz-D3V/pionteching-solution
```
Navigate into the project:
```shell
cd ./pionteching-solution
```
Build the project:
```shell
chmod +x ./build.sh
./build.sh
```
Run the application:
```shell
chmod +x ./run.sh
./run.sh
```

##Tasks
### Task 1: ATM Service
#### Time Complexity
O(n) where n is number of orders.

#### Space Complexity
O(n) where n is number of orders.

### Task 2: Online game
#### Time Complexity
O(n) where n is number of clans.

#### Space Complexity
O(n) where n is number of clans.

### Task 3: Transactions
#### Time Complexity
O(n) where n is number of transactions.

#### Space Complexity
O(m) where m is number of bank accounts.
