package com.weber;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Node implements Comparable<Node> {

    public int duration;

    public int es;
    public int ef;
    public int ls;
    public int lf;

    @JsonIgnore
    public int start;
    @JsonIgnore
    public int finish;

    @JsonIgnore
    public List<Node> predecessors;
    @JsonIgnore
    public List<Node> successors;

    public List<Integer> predecessors_names;
    public List<Integer> successors_names;

    @JsonIgnore
    public int in_degree;
    @JsonIgnore
    public int out_degree;

    @JsonIgnore
    public List<Node> transitive_predecessors;
    public List<Integer> transitive_predecessors_names;
    @JsonIgnore
    public List<Node> transitive_successors;
    public List<Integer> transitive_successors_names;

    public List<Integer> renewable_resource_requirements;

    public int number_of_successors;
    public int number_of_predecessors;
    public int number_of_transitive_successors;
    public int number_of_transitive_predecessors;

    public int name;

    @JsonIgnore
    public boolean started = false;
    @JsonIgnore
    public boolean finished = false;
    @JsonIgnore
    public boolean scheduled = false;

    @JsonIgnore
    public int prio_value;
    @JsonIgnore
    public int es_scheduled;
    public int pl;
    public int rl;
    public int tf;
    public int freeFloat;
    @JsonIgnore
    public List<Node> backward_successors;
    @JsonIgnore
    public List<Node> backward_predecessors;
    public int rank_positional_weight;
    public int old_rank_positional_weight;

    public int total_resource_demand;

    public int number_of_non_related_jobs;

    public int slack;
    public double product_of_priorities;
    public double red;
    public double cumred;

    public double float_per_successor;
    public double successors_per_level;

    public int work_content;

    @JsonIgnore
    double selection_probability;

    @Override
    public int compareTo(Node n) {
        return Integer.compare(prio_value, n.prio_value);
    }

    @Override
    public String toString() {
        return Integer.toString(this.name);
    }

    public void set_transitive_predecessors() {
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>(predecessors);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            queue.addAll(current.predecessors);
        }

        transitive_predecessors = new ArrayList<>(visited);
    }

    public void set_transitive_successors() {
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>(successors);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            queue.addAll(current.successors);
        }

        transitive_successors = new ArrayList<>(visited);
    }

    public void set_degrees() {
        in_degree = predecessors.size();
        out_degree = successors.size();
    }

    public void set_rank_positional_weight() {
        rank_positional_weight = duration;

        for (Node succ : successors) {
            rank_positional_weight += succ.duration;
        }
    }

    public void set_old_rank_positional_weight() {
        old_rank_positional_weight = duration;

        for (Node tran_succ : transitive_successors) {
            old_rank_positional_weight += tran_succ.duration;
        }
    }

    public void set_total_resource_demand() {
        total_resource_demand = 0;

        for (Integer renewableResourceRequirement : renewable_resource_requirements) {
            total_resource_demand += duration * renewableResourceRequirement;
        }
    }

    public void set_number_of_non_related_jobs(Integer number_of_jobs) {
        number_of_non_related_jobs = number_of_jobs - transitive_successors.size() - transitive_predecessors.size();
    }

    public void set_slack() {
        slack = lf - ef;
    }

    public void set_product_of_priorities() {
        product_of_priorities = (double) duration * transitive_successors.size() * transitive_successors.size() * rank_positional_weight / pl;
    }

    public void set_float_per_successor() {
        if (successors.size() == 0) {
            float_per_successor = 0.0;
        } else {
            float_per_successor = (double) slack / successors.size();
        }
    }

    public void set_successors_per_level() {
        successors_per_level = (double) transitive_successors.size() / pl;
    }

    public void set_transitive_relations() {
        set_transitive_predecessors();
        set_transitive_successors();
    }

    public void set_work_content() {
        work_content = 0;
        for (Integer renewableResourceRequirement : renewable_resource_requirements) {
            work_content += renewableResourceRequirement * duration;
        }
    }
}
