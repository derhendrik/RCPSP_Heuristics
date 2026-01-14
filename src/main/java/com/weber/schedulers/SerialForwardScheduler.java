package com.weber.schedulers;

import com.weber.Individual;
import com.weber.Node;

import java.util.*;

public class SerialForwardScheduler extends Scheduler {


    public SerialForwardScheduler() {
        type = "SerialForward";
    }

    @Override
    public void schedule(Individual indiv) {

        indiv.reset_states();
        //indiv.set_prio_values();

        Set<Node> scheduled = new HashSet<>();

        scheduled.add(indiv.project.start_node);
        Set<Node> eligibles = new HashSet<>(indiv.project.start_node.successors);

        //List<Integer> resource_profile_changes = new ArrayList<>();
        //resource_profile_changes.add(0);

        //List<Integer> time_points = new ArrayList<>();
        //time_points.add(0);

        //PriorityQueue<Node> activity_list_prio = new PriorityQueue<>(activity_list);

        List<Node> copy_activity_list = new ArrayList<>(indiv.activity_list);
        copy_activity_list.remove(indiv.project.start_node);

        Node selected_node = indiv.project.start_node;

        while (scheduled.size() != indiv.project.number_of_jobs) {

//            selected_node = copy_activity_list.stream()
//                    .filter(eligibles::contains)
//                    .findFirst()
//                    .orElseThrow();

            for (Node candidate : copy_activity_list) {
                if (eligibles.contains(candidate)) {
                    selected_node = candidate;
                    //copy_activity_list.remove(candidate);
                    break;
                }
            }
            copy_activity_list.remove(selected_node);

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
                        for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                            if (selected_node.renewable_resource_requirements.get(k) > indiv.R_kt[k][t_inc]) {
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

                for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                    for (int t_inc = selected_node.start; t_inc < selected_node.finish; t_inc++) {
                        indiv.R_kt[k][t_inc] -= selected_node.renewable_resource_requirements.get(k);
                    }
                }

                eligibles.remove(selected_node);
                eligibles.addAll(selected_node.successors.stream().filter(s -> (s.predecessors.stream().allMatch(p -> p.scheduled))).toList());
            }
        }

        indiv.fitness = indiv.project.finish_node.finish;

        /*indiv.reset_states();

        Set<Node> scheduled = new HashSet<>();
        scheduled.add(indiv.project.start_node);

        Set<Node> eligibles = new HashSet<>(indiv.project.start_node.successors);
        List<Node> copy_activity_list = new ArrayList<>(indiv.activity_list);
        copy_activity_list.remove(indiv.project.start_node);

        Node selected_node = indiv.project.start_node;

        while (scheduled.size() != indiv.project.number_of_jobs) {
            selected_node = eligibles.stream()
                    .filter(copy_activity_list::contains)
                    .findFirst()
                    .orElseThrow();

            copy_activity_list.remove(selected_node);

            int current_t = selected_node.predecessors.stream()
                    .mapToInt(p -> p.finish)
                    .max()
                    .orElse(0);

            resource_check:
            while (!selected_node.scheduled) {
                for (int t_inc = current_t; t_inc < current_t + selected_node.duration; t_inc++) {
                    for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                        if (selected_node.renewable_resource_requirements.get(k) > indiv.R_kt[k][t_inc]) {
                            current_t++;
                            continue resource_check;
                        }
                    }
                }

                selected_node.scheduled = true;
                scheduled.add(selected_node);
                selected_node.start = current_t;
                selected_node.finish = current_t + selected_node.duration;

                for (int k = 0; k < indiv.project.number_of_renewable_resources; k++) {
                    for (int t_inc = selected_node.start; t_inc < selected_node.finish; t_inc++) {
                        indiv.R_kt[k][t_inc] -= selected_node.renewable_resource_requirements.get(k);
                    }
                }

                eligibles.remove(selected_node);
                eligibles.addAll(selected_node.successors.stream()
                        .filter(s -> s.predecessors.stream().allMatch(p -> p.scheduled))
                        .toList());
            }
        }

        indiv.fitness = indiv.project.finish_node.finish;*/
    }


}

