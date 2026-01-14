package com.weber.priorityrules;

import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MinLNRJ extends AbstractPriorityRule {

    public MinLNRJ() {
        this.name = "LNRJ";
        this.mm_type = "min";
    }

    @Override
    public List<Node> generate_activity_list(Project project, Scheduler scheduler) {
        List<Node> activity_list = project.nodes.stream().sorted(Comparator.comparing(n -> n.number_of_non_related_jobs)).collect(Collectors.toList());
        return activity_list;
    }
}
