package com.weber.priorityrules;

import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MinLFT extends AbstractPriorityRule {

    public MinLFT() {
        this.name = "LFT";
        this.mm_type = "min";
    }

    @Override
    public List<Node> generate_activity_list(Project project, Scheduler scheduler) {
        List<Node> activity_list = project.nodes.stream().sorted(Comparator.comparing(n -> n.lf)).collect(Collectors.toList());
        return activity_list;
    }
}
