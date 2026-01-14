package com.weber.schedulers;

import com.weber.Individual;
import com.weber.Node;

import java.util.*;
import java.util.stream.Collectors;

public class ParallelForwardScheduler extends Scheduler {


    public ParallelForwardScheduler() {
        type = "ParallelForward";
    }

    @Override
    public void schedule(Individual indiv) {

        indiv.reset_states();
        indiv.set_prio_values();

        Set<Node> scheduled = new HashSet<>();
        scheduled.add(indiv.project.start_node);
        Set<Node> eligibles = new HashSet<>(indiv.project.start_node.successors);

        int current_t = 0;
        Node selected_node;

        List<Node> eligibles_list = new ArrayList<>(eligibles);
        Collections.sort(eligibles_list);
        List<Integer> finish_times = new ArrayList<>();

        while (scheduled.size() != indiv.project.number_of_jobs) {

            activity_check:
            for (Node candidate : eligibles_list) {
                if (candidate.es_scheduled > current_t) {
                    continue;
                }

                for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                    for (int t_inc = current_t; t_inc < current_t + candidate.duration; t_inc++) {
                        if (candidate.renewable_resource_requirements.get(k) > indiv.R_kt[k][t_inc]) {
                            continue activity_check;
                        }
                    }
                }
                selected_node = candidate;
                selected_node.start = current_t;
                selected_node.finish = current_t + selected_node.duration;
                selected_node.scheduled = true;
                selected_node.started = true;
                selected_node.finished = true;

                for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                    for (int t_inc = selected_node.start; t_inc < selected_node.finish; t_inc++) {
                        indiv.R_kt[k][t_inc] -= selected_node.renewable_resource_requirements.get(k);
                    }
                }
                eligibles.remove(candidate);
                scheduled.add(candidate);

                eligibles.addAll(selected_node.successors.stream().filter(s -> (s.predecessors.stream().allMatch(p -> p.scheduled))).collect(Collectors.toList()));
                for (Node s : selected_node.successors) {
                    if (s.es_scheduled < selected_node.finish) s.es_scheduled = selected_node.finish;
                }
                finish_times.add(selected_node.finish);
            }

            if (scheduled.size() == indiv.project.number_of_jobs) break;

            eligibles_list = new ArrayList<>(eligibles);
            Collections.sort(eligibles_list);
            int finalCurrent_t = current_t;
            finish_times = finish_times.stream().filter(fin -> fin > finalCurrent_t).collect(Collectors.toList());
            //current_t = Collections.min(finish_times);
            current_t++;
//            System.out.println(current_t);
//            System.out.println(eligibles_list.stream().map(e-> e.name).collect(Collectors.toList()));
//            System.out.println("scheduled:");
//            System.out.println(scheduled.stream().map(s -> s.name).collect(Collectors.toList()));

        }

        //System.out.println("Finished_run");
        indiv.fitness = indiv.project.finish_node.finish;
    }
}
