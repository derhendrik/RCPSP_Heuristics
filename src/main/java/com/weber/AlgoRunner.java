package com.weber;

import com.weber.priorityrules.*;
import com.weber.schedulers.ParallelForwardScheduler;
import com.weber.schedulers.Scheduler;
import com.weber.schedulers.SerialForwardScheduler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class AlgoRunner {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("No instance arguments provided. Running all instances in instance folder.");
            solveAllInstances();
        } else {
            System.out.println("Running instance: " + args[0]);
            solveSingleInstance(Paths.get(args[0]));
        }
    }

    private static void solveAllInstances() throws IOException {

        Path instancesPath = Paths.get(System.getProperty("user.dir"), "instances");

        Stream<Path> instanceStream = Files.list(instancesPath);
        instanceStream.filter(Files::isRegularFile).forEach(ip -> {
            try {
                solveSingleInstance(ip);
            } catch (IOException e) {
                System.err.println("Error processing instance: " + ip);
                e.printStackTrace();
            }
        });
    }

    private static void solveSingleInstance(Path instancePath) throws IOException {
        System.out.println("*".repeat(20));
        System.out.println("Processing instance: " + instancePath);

        Project project = new Project();
        project.read_project_with_format_autodetection(instancePath);
        project.setup_project();

        Scheduler[] schedulers = {new SerialForwardScheduler(), new ParallelForwardScheduler()};
        AbstractPriorityRule[] priorityRules = {
                new MaxCUMRED(),
                new MaxGRPW(),
                new MaxLPF(),
                new MaxLPT(),
                new MaxMIS(),
                new MaxMSPL(),
                new MaxMTS(),
                new MaxOGRPW(),
                new MaxPOP(),
                new MaxRED(),
                new MaxTRD(),
                new MinEFT(),
                new MinEST(),
                new MinFPS(),
                new MinLFT(),
                new MinLIS(),
                new MinLNRJ(),
                new MinLST(),
                new MinLTS(),
                new MinRANDOM(),
                new MinSLK(),
                new MinSPT()
        };

        for (Scheduler scheduler : schedulers) {
            for (AbstractPriorityRule priorityRule : priorityRules) {
                project.reset_states();
                priorityRule.schedule(project, scheduler);
                System.out.println("Scheduler: " + scheduler.type + ", Priority Rule: " + priorityRule.getClass().getSimpleName() + ", Makespan: " + priorityRule.makespan);
            }
        }



    }
}