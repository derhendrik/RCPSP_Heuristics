package com.weber.priorityrules;

import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MinSLK extends AbstractPriorityRule {

    public MinSLK() {
        this.name = "SLK";
        this.mm_type = "min";
    }

    @Override
    public List<Node> generate_activity_list(Project project, Scheduler scheduler) {
        List<Node> activity_list = project.nodes.stream().sorted(Comparator.comparing(n -> n.slack)).collect(Collectors.toList());
        return activity_list;
    }
}
