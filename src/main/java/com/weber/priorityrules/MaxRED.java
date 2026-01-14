package com.weber.priorityrules;

import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MaxRED extends AbstractPriorityRule {

    public MaxRED() {
        this.name = "RED";
        this.mm_type = "max";
    }

    @Override
    public List<Node> generate_activity_list(Project project, Scheduler scheduler) {
        List<Node> activity_list = project.nodes.stream().sorted(Comparator.comparing(n -> n.red)).collect(Collectors.toList());
        Collections.reverse(activity_list);
        return activity_list;
    }
}
