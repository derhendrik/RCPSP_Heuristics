package com.weber.priorityrules;

import com.weber.Individual;
import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractPriorityRule {

    public Project project;
    public Scheduler scheduler;
    public List<Node> activity_list;
    public int makespan;
    public long comp_time;
    public long comp_time_with_setup;

    public String name;
    public String mm_type;
    public HashMap<Node, Integer> schedule;

    public AbstractPriorityRule() {
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(Project project, Scheduler scheduler) {
        this.project = project;
        this.scheduler = scheduler;
        project.reset_states();
        long first_time_point = System.currentTimeMillis();
        activity_list = generate_activity_list(project, scheduler);
        Individual indiv = new Individual(activity_list, project);
//        if (!indiv.is_precedence_feasible()) {
//            //System.out.println("not precedence feasible. Restoring ...");
//            indiv.restore_precedence_feasibility();
//        }
        long second_time_point = System.currentTimeMillis();

        scheduler.schedule(indiv);

        long third_time_point = System.currentTimeMillis();

        comp_time_with_setup = third_time_point - first_time_point;
        comp_time = third_time_point - second_time_point;
        makespan = indiv.fitness;
        //System.out.println("Fitness");
    }

    public void store_schedule() {
        schedule = new HashMap<>();

        for (Node n : project.nodes) {
            schedule.put(n, n.start);
        }

    }

    public boolean check_schedule_feasibility() {

        //TODO change order of resources and nodes
        for (int t = 0; t <= makespan; t++) {
            for (int k = 0; k < project.number_of_renewable_resources; k++) {
                int res_availabilty = project.renewable_resource_availability.get(k);
                for (Node n : project.nodes) {
                    if (t >= n.start && t < n.start + n.duration) {
                        res_availabilty -= n.renewable_resource_requirements.get(k);
                        if (res_availabilty < 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }

    public abstract List<Node> generate_activity_list(Project project, Scheduler scheduler);


}
