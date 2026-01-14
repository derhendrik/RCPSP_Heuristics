package com.weber.priorityrules;

import com.weber.Project;
import com.weber.schedulers.Scheduler;

public interface PriorityRule {
    void schedule(Project project, Scheduler scheduler);

}
