package com.weber;

import com.weber.schedulers.Scheduler;

import java.util.*;
import java.util.stream.Collectors;

public class Individual implements Comparable<Individual> {

    private Scheduler scheduler;
    public int fitness = Integer.MAX_VALUE;
    public List<Node> activity_list;
    public List<Integer> prio_value_list;
    public Project project;
    public int[][] R_kt;
    public Map StartTimes;


    public Individual(List<Node> activity_list, Project project) {
        this.activity_list = activity_list;
        this.project = project;
    }

    public Individual(List<Node> activity_list, Project project, Scheduler scheduler) {
        this.activity_list = activity_list;
        this.project = project;
        this.scheduler = scheduler;
    }



    public void set_prio_values() {
        prio_value_list = new ArrayList<>();

        for (Node n : project.nodes) {
            int index_of_node = activity_list.indexOf(n);
            prio_value_list.add(index_of_node);
            n.prio_value = index_of_node;
        }
    }

    public void reset_states() {

        // reset node states
        for (Node n : project.nodes) {
            n.started = false;
            n.finished = false;
            n.scheduled = false;

            n.es_scheduled = n.es;
        }

        // create empty resource profile
        R_kt = new int[project.number_of_renewable_resources][project.horizon];
        for (int k = 0; k < project.number_of_renewable_resources; k++) {
            int finalK = k;
            Arrays.setAll(R_kt[k], i -> project.renewable_resource_availability.get(finalK));
        }

        project.start_node.start = 0;
        project.start_node.finish = 0;
        project.start_node.scheduled = true;
    }

    public boolean is_precedence_feasible() {

        for (Node n : activity_list) {
            if (n.predecessors.stream().allMatch(p -> activity_list.indexOf(n) > activity_list.indexOf(p))) {
            } else {
                return false;
            }
        }

        return true;
    }

    public void restore_precedence_feasibility() {

        project.set_node_degrees();
        List<Node> result = new ArrayList<>();

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(activity_list::indexOf));

        for (Node n : activity_list) {
            if (n.in_degree == 0) {
                queue.add(n);
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add(current);

            for (Node succ : current.successors) {
                succ.in_degree--;
                if (succ.in_degree == 0) {
                    queue.add(succ);
                }
            }
        }

        activity_list = result;
        project.set_node_degrees();
    }

    public void schedule_serial() {
        reset_states();
        set_prio_values();
        Set<Node> scheduled = new HashSet<>();

        scheduled.add(project.start_node);
        Set<Node> eligibles = new HashSet<>(project.start_node.successors);

        //List<Integer> resource_profile_changes = new ArrayList<>();
        //resource_profile_changes.add(0);

        //List<Integer> time_points = new ArrayList<>();
        //time_points.add(0);

        //PriorityQueue<Node> activity_list_prio = new PriorityQueue<>(activity_list);

        List<Node> copy_activity_list = new ArrayList<>(activity_list);
        copy_activity_list.remove(project.start_node);

        Node selected_node = project.start_node;

        activity_check:
        while (scheduled.size() != project.number_of_jobs) {

            for (Node candidate : copy_activity_list) {
                if (eligibles.contains(candidate)) {
                    selected_node = candidate;
                    copy_activity_list.remove(candidate);
                    break;
                }
            }

            //selected_node = Collections.min(eligibles, Comparator.comparing(e -> e.prio_value));
            int current_t = Collections.max(selected_node.predecessors.stream().map(p -> p.finish).toList());

            //time_points.add(current_t);
            //int finalCurrent_t = current_t;
            //int[] next_t =  scheduled.stream().map(n->n.finish).filter(t -> t > finalCurrent_t).mapToInt(i->i).toArray();
            //Collections.sort(time_points);

            //int i = 0;
            resource_check:
            while (!selected_node.scheduled) {

                for (int t_inc = current_t; t_inc < current_t + selected_node.duration; t_inc++) {
                    for (int k = 0; k < project.number_of_renewable_resources; k++) {
                        if (selected_node.renewable_resource_requirements.get(k) > R_kt[k][t_inc]) {
                            // TODO hier könnte schlauer der nächste Zeitpunkt gewählt werden
                            current_t++;
                            //current_t = next_t[i];
                            //i++;

                            continue resource_check;
                        }
                    }
                }

                selected_node.scheduled = true;
                scheduled.add(selected_node);
                selected_node.start = current_t;
                selected_node.finish = current_t + selected_node.duration;
                //time_points.add(selected_node.finish);

                for (int k = 0; k < project.number_of_renewable_resources; k++) {
                    for (int t_inc = selected_node.start; t_inc < selected_node.finish; t_inc++) {
                        R_kt[k][t_inc] -= selected_node.renewable_resource_requirements.get(k);
                    }
                }

                eligibles.remove(selected_node);
                eligibles.addAll(selected_node.successors.stream().filter(s -> (s.predecessors.stream().allMatch(p -> p.scheduled))).collect(Collectors.toList()));
            }
        }

        fitness = project.finish_node.finish;
    }

    public void schedule_parallel() {
        reset_states();
        set_prio_values();
    }

    @Override
    public int compareTo(Individual indiv) {
        return Integer.compare(fitness, indiv.fitness);
    }



    public void schedule(){
        reset_states();
        set_prio_values();
        scheduler.schedule(this);
    }

    public void schedule(Scheduler scheduler){
        reset_states();
        set_prio_values();
        scheduler.schedule(this);
    }

    public void schedule_no_prio_values(Scheduler scheduler){
        reset_states();
        scheduler.schedule(this);
    }
}
