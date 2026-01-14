package com.weber.priorityrules;

import com.weber.Node;
import com.weber.Project;
import com.weber.schedulers.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinRANDOM extends AbstractPriorityRule {

    public MinRANDOM() {
        this.name = "RANDOM";
        this.mm_type = "min";
    }

    @Override
    public List<Node> generate_activity_list(Project project, Scheduler scheduler) {
        List<Node> activity_list = new ArrayList<>(project.nodes);
        Collections.shuffle(activity_list);
        return activity_list;
    }
}
