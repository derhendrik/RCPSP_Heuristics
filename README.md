## RCPSP Heuristic Solver

This repository contains a variety of static priority rules for the Resource-Constrained Project Scheduling Problem (RCPSP).
Each priority rule generates an ordering of jobs which can subsequently be used with either the parallel or serial schedule generation scheme to generate a feasible solution.

While in some cases, it is clear whether the maximum or minimum value of a job characteristic determines its priority (e.g. "Most Total Successors"), the following convention for clarity is used:
- "Maximum" (prefix "Max" in implementations of the abstract base class) indicates that jobs with higher values of the characteristic are prioritized first.
- "Minimum" (prefix "Min" in implementations of the abstract base class) indicates that jobs with lower values of the characteristic are prioritized first.

Note that these job characteristics are all determined when the problem instance is set up.
Thus, all calculations for the job characteristics can be found in the [Project](src/main/java/com/weber/Project.java) class.

The priority rules in this repository include:
- Maximum Cumulative Resource Equivalent Duration (MaxCUMRED)
- Maximum Greatest Rank Positional Weight (MaxGRPW)
- Maximum Longest Path Following (MaxLPF)
- Maximum Longest Processing Time (MaxLPT)
- Maximum Most Immediate Successors (MaxMIS)
- Maximum Most Successors per Level (MaxMSPL)
- Maximum Most Total Successors (MaxMTS)
- Maximum "Old" Greatest Rank Positional Weight (MaxOGRPW). An older variant of GRPW.
- Maximum Product of Priorities (MaxPOP)
- Maximum Resource Equivalent Duration (MaxRED)
- Maximum Total Resource Demand (MaxTRD)
- Minimum Earliest Finish Time (MinEFT)
- Minimum Earliest Start Time (MinEST)
- Minimum Float per Successor (MinFPS)
- Minimum Latest Finish Time (MinLFT)
- Minimum Least Immediate Successors (MinLIS)
- Minimum Least Non-Related Jobs (MinLNRJ)
- Minimum Latest Start Time (MinLST)
- Minimum Least Total Successors (MinLTS)
- Random
- Minimum Slack (MinSLK)
- Minimum Shortest Processing Time (MinSPT)

### How to use

All dependencies are managed via Maven and can be found in the [pom.xml](pom.xml) file.

To run the program, do the following.

1. Clone the repository and navigate into the project root directory:
```
git clone https://github.com/derhendrik/RCPSP_Heuristics.git
cd RCPSP_Heuristics
```
2. Build the project using Maven:

```
mvn package
```

3. Run the program with the following command to solve ALL instances located in the `instances/` folder:

```java -cp target/RCPSP_Heuristics.jar com.weber.AlgoRunner```

4. Alternatively, you can specify a single instance file to solve:

```java -cp target/RCPSP_Heuristics.jar com.weber.AlgoRunner instances/your_instance_file.rcp```

