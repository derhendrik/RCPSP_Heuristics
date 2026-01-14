package com.weber;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Project {

    public List<Integer> renewable_resource_availability;
    public int number_of_jobs;
    public int number_of_nondummy_jobs;
    public int number_of_renewable_resources;

    public List<Node> nodes;
    public Node start_node;
    public Node finish_node;

    public int horizon;
    @JsonIgnore
    //public List<Individual> population;
    public String name;
    public int maxProgLevel;
    public double spi;
    public double ad;
    public double topologicalFloatIndicator;
    public double freeFloatRatio;
    public double resourceFactor;
    public double maxResourceConstrainedness;
    public double networkComplexity;
    public double minResourceStrength;
    public double orderStrength;
    public double maxUtilization;

    public String instanceType;
    public String instanceName;

    public int number_of_arcs;

    public double disjunction_ratio;

    public boolean is_precedence_feasible(List<Node> candidate) {

        boolean precedence_feasible = true;

        // TODO only investigate the swap! Pass to function: Initial list
        for (Node n : candidate) {
            if (n.predecessors.stream().allMatch(p -> candidate.indexOf(n) > candidate.indexOf(p))) {
            } else {
                return false;
            }
        }

        return precedence_feasible;
    }

    public void read_project_patterson(String instanceType, String instanceFilename) throws FileNotFoundException {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path instancePath = Paths.get(currentPath.toString(), "instances", instanceType, instanceFilename);
        Scanner scanner = new Scanner(new File(instancePath.toString()));

        String current_line = scanner.nextLine();

        if (current_line.isEmpty()) {
            current_line = scanner.nextLine();
        }

        Scanner line_sc = new Scanner(current_line);

        number_of_jobs = line_sc.nextInt();
        number_of_nondummy_jobs = number_of_jobs - 2;

        nodes = new ArrayList<>();

        for (int i = 0; i < this.number_of_jobs; i++) {
            Node dummy_node = new Node();
            dummy_node.name = i;
            this.nodes.add(dummy_node);
        }

        this.start_node = this.nodes.get(0);
        this.finish_node = this.nodes.get(this.nodes.size() - 1);

        number_of_renewable_resources = line_sc.nextInt();

        current_line = scanner.nextLine();
        line_sc = new Scanner((current_line));
        this.renewable_resource_availability = new ArrayList<>();
        for (int i = 0; i < number_of_renewable_resources; i++) {
            renewable_resource_availability.add(line_sc.nextInt());
        }

        int i = 0;
        while (scanner.hasNext()) {

            current_line = scanner.nextLine();

            if (!current_line.isEmpty()) {
                line_sc = new Scanner(current_line);
                Node current_node = nodes.get(i);

                current_node.duration = line_sc.nextInt();
                current_node.renewable_resource_requirements = new ArrayList<>();

                for (int j = 0; j < number_of_renewable_resources; j++) {
                    current_node.renewable_resource_requirements.add(line_sc.nextInt());
                }

                int no_of_successors = line_sc.nextInt();
                current_node.successors = new ArrayList<>();

                for (int j = 0; j < no_of_successors; j++) {
                    int succ_name = line_sc.nextInt() - 1;
                    Node succ = nodes.get(succ_name);
                    current_node.successors.add(succ);
                }

                i++;
            }
        }
        horizon = 0;
        for (Node n : this.nodes) {
            n.predecessors = new ArrayList<>();
            this.horizon += n.duration;
        }

        // set predecessors:
        for (Node n : this.nodes) {
            for (Node s : n.successors) {
                s.predecessors.add(n);
            }
        }
    }

    public void read_project_patterson_300(String instanceType, String instanceFilename) throws FileNotFoundException {

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path instancePath = Paths.get(currentPath.toString(), "instances", instanceType, instanceFilename);
        Scanner scanner = new Scanner(new File(instancePath.toString()));

        String current_line = scanner.nextLine();

        Scanner line_sc = new Scanner(current_line);
        number_of_jobs = line_sc.nextInt();
        number_of_nondummy_jobs = number_of_jobs - 2;

        nodes = new ArrayList<>();

        for (int i = 0; i < this.number_of_jobs; i++) {
            Node dummy_node = new Node();
            dummy_node.name = i;
            this.nodes.add(dummy_node);
        }

        this.start_node = this.nodes.get(0);
        this.finish_node = this.nodes.get(this.nodes.size() - 1);

        number_of_renewable_resources = line_sc.nextInt();

        current_line = scanner.nextLine();
        line_sc = new Scanner(current_line);

        this.renewable_resource_availability = new ArrayList<>();

        for (int i = 0; i < number_of_renewable_resources; i++) {
            renewable_resource_availability.add(line_sc.nextInt());
        }

        int i = 0;

        while (scanner.hasNext()) {

            Node current_node = nodes.get(i);

            current_node.duration = scanner.nextInt();

            current_node.renewable_resource_requirements = new ArrayList<>();

            for (int j = 0; j < number_of_renewable_resources; j++) {
                current_node.renewable_resource_requirements.add(scanner.nextInt());
            }

            int no_of_successors = scanner.nextInt();
            current_node.successors = new ArrayList<>();

            for (int j = 0; j < no_of_successors; j++) {
                int succ_name = scanner.nextInt() - 1;
                Node succ = nodes.get(succ_name);
                current_node.successors.add(succ);
            }

            i++;
        }

        horizon = 0;
        for (Node n : this.nodes) {
            n.predecessors = new ArrayList<>();
            this.horizon += n.duration;
        }

        // set predecessors:
        for (Node n : this.nodes) {
            for (Node s : n.successors) {
                s.predecessors.add(n);
            }
        }
    }

    public void read_project_psplib(Path instancePath) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(instancePath.toString()));

        while (scanner.hasNext()) {
            String current_line = scanner.nextLine();

            if (current_line.contains("jobs (incl. supersource/sink ):")) {
                String[] split_line = current_line.split(" ");
                this.number_of_jobs = Integer.parseInt(split_line[split_line.length - 1]);
                this.number_of_nondummy_jobs = this.number_of_jobs - 2;

                nodes = new ArrayList<>();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    Node dummy_node = new Node();
                    dummy_node.name = i;
                    this.nodes.add(dummy_node);
                }

                this.start_node = this.nodes.get(0);
                this.finish_node = this.nodes.get(this.nodes.size() - 1);
            }

            if (current_line.contains("- renewable")) {
                String[] split_line = current_line.split(" ");
                List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));

                while (line_as_list.contains("")) line_as_list.remove("");
                this.number_of_renewable_resources = Integer.parseInt(line_as_list.get(line_as_list.size() - 2));
            }

            if (current_line.contains("PRECEDENCE RELATIONS:")) {
                scanner.nextLine();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    current_line = scanner.nextLine();
                    String[] split_line = current_line.split(" ");
                    List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                    while (line_as_list.contains("")) {
                        line_as_list.remove("");
                    }

                    int no_of_successors = Integer.parseInt(line_as_list.get(2));

                    Node current_node = this.nodes.get(i);
                    current_node.successors = new ArrayList<>();

                    for (int s = 0; s < no_of_successors; s++) {
                        int succ_name = Integer.parseInt(line_as_list.get(3 + s)) - 1;
                        Node succ = this.nodes.get(succ_name);
                        current_node.successors.add(succ);
                    }
                }
            }

            if (current_line.contains("REQUESTS/DURATIONS:")) {
                scanner.nextLine();
                scanner.nextLine();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    current_line = scanner.nextLine();
                    String[] split_line = current_line.split(" ");
                    List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                    while (line_as_list.contains("")) {
                        line_as_list.remove("");
                    }

                    Node current_node = this.nodes.get(i);

                    current_node.renewable_resource_requirements = new ArrayList<>();
                    current_node.duration = Integer.parseInt(line_as_list.get(2));

                    for (int r = 0; r < this.number_of_renewable_resources; r++) {

                        int res_requirements = Integer.parseInt(line_as_list.get(3 + r));
                        current_node.renewable_resource_requirements.add(res_requirements);
                    }
                }
            }

            if (current_line.contains("RESOURCEAVAILABILITIES:")) {
                scanner.nextLine();
                current_line = scanner.nextLine();
                this.renewable_resource_availability = new ArrayList<>();
                String[] split_line = current_line.split(" ");
                List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                while (line_as_list.contains("")) {
                    line_as_list.remove("");
                }

                for (int r = 0; r < this.number_of_renewable_resources; r++) {
                    int availability = Integer.parseInt(line_as_list.get(r));
                    this.renewable_resource_availability.add(availability);
                }
            }
        }

        horizon = 0;
        for (Node n : this.nodes) {
            n.predecessors = new ArrayList<>();
            this.horizon += n.duration;
        }

        // set predecessors:
        for (Node n : this.nodes) {
            for (Node s : n.successors) {
                s.predecessors.add(n);
            }
        }
    }

    public void read_project_psplib(String instanceType, String instanceFilename) throws FileNotFoundException {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path instancePath = Paths.get(currentPath.toString(), "instances", instanceType, instanceFilename);
        Scanner scanner = new Scanner(new File(instancePath.toString()));

        while (scanner.hasNext()) {
            String current_line = scanner.nextLine();

            if (current_line.contains("jobs (incl. supersource/sink ):")) {
                String[] split_line = current_line.split(" ");
                this.number_of_jobs = Integer.parseInt(split_line[split_line.length - 1]);
                this.number_of_nondummy_jobs = this.number_of_jobs - 2;

                nodes = new ArrayList<>();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    Node dummy_node = new Node();
                    dummy_node.name = i;
                    this.nodes.add(dummy_node);
                }

                this.start_node = this.nodes.get(0);
                this.finish_node = this.nodes.get(this.nodes.size() - 1);
            }

            if (current_line.contains("- renewable")) {
                String[] split_line = current_line.split(" ");
                List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));

                while (line_as_list.contains("")) line_as_list.remove("");
                this.number_of_renewable_resources = Integer.parseInt(line_as_list.get(line_as_list.size() - 2));
            }

            if (current_line.contains("PRECEDENCE RELATIONS:")) {
                scanner.nextLine();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    current_line = scanner.nextLine();
                    String[] split_line = current_line.split(" ");
                    List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                    while (line_as_list.contains("")) {
                        line_as_list.remove("");
                    }

                    int no_of_successors = Integer.parseInt(line_as_list.get(2));

                    Node current_node = this.nodes.get(i);
                    current_node.successors = new ArrayList<>();

                    for (int s = 0; s < no_of_successors; s++) {
                        int succ_name = Integer.parseInt(line_as_list.get(3 + s)) - 1;
                        Node succ = this.nodes.get(succ_name);
                        current_node.successors.add(succ);
                    }
                }
            }

            if (current_line.contains("REQUESTS/DURATIONS:")) {
                scanner.nextLine();
                scanner.nextLine();

                for (int i = 0; i < this.number_of_jobs; i++) {
                    current_line = scanner.nextLine();
                    String[] split_line = current_line.split(" ");
                    List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                    while (line_as_list.contains("")) {
                        line_as_list.remove("");
                    }

                    Node current_node = this.nodes.get(i);

                    current_node.renewable_resource_requirements = new ArrayList<>();
                    current_node.duration = Integer.parseInt(line_as_list.get(2));

                    for (int r = 0; r < this.number_of_renewable_resources; r++) {

                        int res_requirements = Integer.parseInt(line_as_list.get(3 + r));
                        current_node.renewable_resource_requirements.add(res_requirements);
                    }
                }
            }

            if (current_line.contains("RESOURCEAVAILABILITIES:")) {
                scanner.nextLine();
                current_line = scanner.nextLine();
                this.renewable_resource_availability = new ArrayList<>();
                String[] split_line = current_line.split(" ");
                List<String> line_as_list = new ArrayList<>(Arrays.asList(split_line));
                while (line_as_list.contains("")) {
                    line_as_list.remove("");
                }

                for (int r = 0; r < this.number_of_renewable_resources; r++) {
                    int availability = Integer.parseInt(line_as_list.get(r));
                    this.renewable_resource_availability.add(availability);
                }
            }
        }

        horizon = 0;
        for (Node n : this.nodes) {
            n.predecessors = new ArrayList<>();
            this.horizon += n.duration;
        }

        // set predecessors:
        for (Node n : this.nodes) {
            for (Node s : n.successors) {
                s.predecessors.add(n);
            }
        }
    }

    public void read_project_generic_patterson(Path instanceFilePath) throws FileNotFoundException {

        // Read in all data as a list of integers, skipping line breaks and whitespaces
        List<Integer> allInts = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(instanceFilePath.toString()))) {
            while (scanner.hasNext()) {
                allInts.add(scanner.nextInt());
            }
        }

        if (allInts.isEmpty()) throw new IllegalArgumentException("No data in file found.");

        int idx = 0;

        // Set up nodes
        number_of_jobs = allInts.get(idx++);
        number_of_nondummy_jobs = number_of_jobs - 2;

        this.nodes = new ArrayList<>();

        for (int i = 0; i < this.number_of_jobs; i++) {
            Node dummy_node = new Node();
            dummy_node.name = i;
            this.nodes.add(dummy_node);
        }

        this.start_node = this.nodes.get(0);
        this.finish_node = this.nodes.get(this.nodes.size() - 1);

        // Set up renewable resource availabilities
        number_of_renewable_resources = allInts.get(idx++);

        renewable_resource_availability = new ArrayList<>();
        for (int i = 0; i < number_of_renewable_resources; i++) {
            renewable_resource_availability.add(allInts.get(idx++));
        }

        // Set up durations, renewable resource requirements and successors for each node
        for (int i = 0; i < number_of_jobs; i++) {
            Node current_node = nodes.get(i);

            current_node.duration = allInts.get(idx++);

            current_node.renewable_resource_requirements = new ArrayList<>();
            for (int j = 0; j < number_of_renewable_resources; j++) {
                current_node.renewable_resource_requirements.add(allInts.get(idx++));
            }

            int no_of_successors = allInts.get(idx++);
            current_node.successors = new ArrayList<>();

            for (int j = 0; j < no_of_successors; j++) {
                int succ_name = allInts.get(idx++) - 1;
                Node succ = nodes.get(succ_name);
                current_node.successors.add(succ);
            }
        }

        // Set up predecessors of each node and calculate upper bound horizon
        horizon = 0;
        for (Node n : this.nodes) {
            n.predecessors = new ArrayList<>();
            this.horizon += n.duration;
        }

        for (Node n : this.nodes) {
            for (Node s : n.successors) {
                s.predecessors.add(n);
            }
        }
    }

    public void read_project(String instanceType, String instanceFilename) throws FileNotFoundException {

        this.name = instanceType + "_" + instanceFilename;
        this.instanceType = instanceType;
        this.instanceName = instanceFilename;

        switch (instanceType) {
            // case "j30" means files in kolisch-format, RG30 in Patterson with extra line break format and RG300 in regular patterson format
            case "j30", "j60", "j90", "j120", "test_instances", "smallset" ->
                    read_project_psplib(instanceType, instanceFilename);
            case "RG30", "DC2", "test_instances_patterson_lb" -> read_project_patterson(instanceType, instanceFilename);
            case "RG300", "sD", "CV", "NetRes", "DC1", "LPP", "LPSP", "MH", "VNR", "test_instances_patterson" ->
                    read_project_patterson_300(instanceType, instanceFilename);

            default -> System.out.println("Error: No available read method for defined instance type.");
        }
    }

    public void read_project_with_format_autodetection(Path instanceFilePath) throws IOException {
        this.name = instanceFilePath.getFileName().toString();
        //this.instanceType = "instanceType";

        char firstChar_of_file;
        try (InputStream is = Files.newInputStream(instanceFilePath)) {
            firstChar_of_file = (char) is.read();
        }
        if (firstChar_of_file == '*') {
            this.instanceType = "psplib";
        } else {
            this.instanceType = "patterson";
        }

        switch (instanceType) {
            case "patterson" -> read_project_generic_patterson(instanceFilePath);
            case "psplib" -> read_project_psplib(instanceFilePath);
            default -> read_project_generic_patterson(instanceFilePath);
        }
    }

    public void calculate_es_ef_ls_lf() {
        // Forward pass
        this.start_node.es = 0;
        this.start_node.ef = this.start_node.es + this.start_node.duration;

        for (Node n : this.nodes) {
            if (n == start_node) {
            } else {
                n.es = Collections.max(n.predecessors.stream().map(p -> p.ef).toList());
                n.ef = n.es + n.duration;
            }
        }

        // Backward pass
        this.finish_node.lf = this.horizon;
        this.finish_node.ls = this.finish_node.lf - this.finish_node.duration;

        List<Node> reversed_nodes = new ArrayList<>(this.nodes);
        Collections.reverse(reversed_nodes);

        for (Node n : reversed_nodes) {
            if (n == finish_node) {
            } else {
                n.lf = Collections.min(n.successors.stream().map(s -> s.ls).toList());
                n.ls = n.lf - n.duration;
            }
        }

        for (Node n : nodes) {
            n.es_scheduled = n.es;
        }
    }

    public void calculate_es_ef_ls_lf_small_horizon() {
        // Forward pass
        this.start_node.es = 0;
        this.start_node.ef = this.start_node.es + this.start_node.duration;

        for (Node n : this.nodes) {
            if (n == start_node) {
            } else {
                n.es = Collections.max(n.predecessors.stream().map(p -> p.ef).toList());
                n.ef = n.es + n.duration;
            }
        }

        // Backward pass
        this.finish_node.lf = this.finish_node.ef;
        this.finish_node.ls = this.finish_node.ef - this.finish_node.duration;

        List<Node> reversed_nodes = new ArrayList<>(this.nodes);
        Collections.reverse(reversed_nodes);

        for (Node n : reversed_nodes) {
            if (n == finish_node) {
            } else {
                n.lf = Collections.min(n.successors.stream().map(s -> s.ls).toList());
                n.ls = n.lf - n.duration;
            }
        }

        for (Node n : nodes) {
            n.es_scheduled = n.es;
        }
    }

    public void reset_states() {

        for (Node n : nodes) {
            n.started = false;
            n.finished = false;
            n.scheduled = false;
        }
    }

    public void set_rank_positional_weights() {
        for (Node n : nodes) {
            n.set_rank_positional_weight();
        }
    }

    public void set_old_rank_positional_weights() {
        for (Node n : nodes) {
            n.set_old_rank_positional_weight();
        }
    }

    public void set_total_resource_demands() {
        for (Node n : nodes) {
            n.set_total_resource_demand();
        }
    }

    public void set_transitive_relations() {
        for (Node n : nodes) {
            n.set_transitive_relations();
        }
    }

    public void set_node_degrees() {
        for (Node n : nodes) {
            n.set_degrees();
        }
    }

    public void set_work_contents() {
        for (Node n : nodes) {
            n.set_work_content();
        }
    }

    public void set_number_of_non_related_jobs() {
        for (Node n : nodes) {
            n.set_number_of_non_related_jobs(number_of_jobs);
        }
    }

    public void set_slacks() {
        for (Node n : nodes) {
            n.set_slack();
        }
    }

    public void set_levels() {
        for (Node n : nodes) {
            if (n.predecessors.size() == 0) {
                n.pl = 1;
            } else {
                n.pl = Collections.max(n.predecessors.stream().map(pred -> pred.pl).toList()) + 1;
            }
        }

        maxProgLevel = Collections.max(nodes.stream().map(node -> node.pl).toList());

        List<Node> reversedNodes = new ArrayList<>(nodes);
        Collections.reverse(reversedNodes);

        for (Node n : reversedNodes) {
            if (n.successors.size() == 0) {
                n.rl = maxProgLevel;
            } else {
                n.rl = Collections.max(n.successors.stream().map(succ -> succ.rl).toList()) - 1;
            }
        }

        for (Node n : nodes) {
            n.tf = n.rl - n.pl;
        }
    }

    public void set_floats_per_successor() {
        for (Node n : nodes) {
            n.set_float_per_successor();
        }
    }

    public void set_successors_per_level() {
        for (Node n : nodes) {
            n.set_successors_per_level();
        }
    }

    public void set_free_floats() {

    }

    public void set_products_of_priorities() {
        for (Node n : nodes) {
            n.set_product_of_priorities();
        }
    }

    public void set_resource_equivalent_durations() {

        int[][] R_kt = new int[number_of_renewable_resources][finish_node.ef];

        for (Node n : nodes) {
            for (int k = 0; k < number_of_renewable_resources; k++) {
                for (int t = n.es; t < n.ef; t++) {
                    R_kt[k][t] += n.renewable_resource_requirements.get(k);
                }
            }
        }

        double[] rud_k = new double[number_of_renewable_resources];

        for (int k = 0; k < number_of_renewable_resources; k++) {

            int L_k = 0;
            int E_k = 0;

            for (int t = R_kt[k].length - 1; t >= 0; t--) {
                if (R_kt[k][t] > 0) {
                    L_k = t + 1;
                    break;
                }
            }

            for (int t = 0; t < finish_node.ef; t++) {
                E_k += Math.max(0, R_kt[k][t] - renewable_resource_availability.get(k));
            }

            rud_k[k] = (double) L_k + ((double) E_k / renewable_resource_availability.get(k));
        }

        if (number_of_renewable_resources == 0) {
            for (Node n : nodes) {

                // TODO: Here own definition: 0 resources in project, therefore sum over all resources can be considered 0
                n.red = 0;
            }
        } else {
            double rudmax_k = Arrays.stream(rud_k).max().getAsDouble();

            for (Node n : nodes) {
                double red = 0;
                for (int k = 0; k < number_of_renewable_resources; k++) {
                    red += ((double) n.renewable_resource_requirements.get(k) / renewable_resource_availability.get(k)) * rud_k[k] / rudmax_k * n.duration;
                }

                n.red = red;
            }
        }
    }

    public void set_cumreds() {
        for (Node n : nodes) {
            n.cumred = n.red + n.transitive_successors.stream().mapToDouble(s -> s.red).sum();
        }
    }

    public void set_node_attributes() {
        set_work_contents();
        set_transitive_relations();
        set_rank_positional_weights();
        set_old_rank_positional_weights();
        set_total_resource_demands();
        set_number_of_non_related_jobs();
        set_slacks();
        set_levels();
        set_floats_per_successor();
        set_products_of_priorities();
        set_resource_equivalent_durations();
        set_cumreds();
        set_floats_per_successor();
        set_successors_per_level();
        set_succ_pred_names();
        set_succ_pred_sizes();
    }

    public void set_node_attributes_extended() {

        set_node_attributes();

        //setup adjacency matrix
        boolean[][] adjacencyMatrix = new boolean[number_of_jobs][number_of_jobs];
        for (Node n : nodes) {
            for (Node s : n.successors) {
                adjacencyMatrix[n.name][s.name] = true;
            }
        }

        //set number of arcs based on adjacency matrix
        number_of_arcs = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j]) {
                    number_of_arcs++;
                }
            }
        }

        //set network complexity
        networkComplexity = (double) number_of_arcs / number_of_jobs;

        //set reachability matrix
        boolean[][] reachabilityMatrix = new boolean[number_of_jobs][number_of_jobs];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            reachabilityMatrix[i] = adjacencyMatrix[i].clone();
        }

        for (int i = 0; i < reachabilityMatrix.length; i++) {
            reachabilityMatrix[i][i] = true;
        }

        for (int i = 0; i < reachabilityMatrix.length; i++) {
            for (int j = 0; j < reachabilityMatrix.length; j++) {
                for (int k = 0; k < reachabilityMatrix.length; k++) {
                    reachabilityMatrix[i][j] = reachabilityMatrix[i][j] || (reachabilityMatrix[i][k] && reachabilityMatrix[k][j]);
                }
            }
        }

        double trueEntries = 0;
        for (int i = 1; i < reachabilityMatrix.length - 1; i++) {
            for (int j = 1; j < reachabilityMatrix.length - 1; j++) {
                if (reachabilityMatrix[i][j]) {
                    trueEntries++;
                }
            }
        }

        trueEntries -= number_of_nondummy_jobs;

        //set order strength
        orderStrength = trueEntries * 2 / (double) (number_of_nondummy_jobs * (number_of_nondummy_jobs - 1));

        //set disjunction ratio
        int disjunctions = 0;
        List<Node> seen_nodes = new ArrayList<>();

        for (Node n_i : nodes) {
            for (Node n_j : seen_nodes) {
                for (int k = 0; k < number_of_renewable_resources; k++) {
                    if (n_i.renewable_resource_requirements.get(k) + n_j.renewable_resource_requirements.get(k) > renewable_resource_availability.get(k)) {
                        disjunctions++;
                        break;
                    }
                }
            }
            seen_nodes.add(n_i);
        }

        disjunction_ratio = (disjunctions + trueEntries) * 2 / (double) (number_of_nondummy_jobs * (number_of_nondummy_jobs - 1));

        //set max progressive level
        maxProgLevel = Collections.max(nodes.stream().map(node -> node.pl).toList());

        //set free float ratio (see Artigues book, proposed by Alvarez 89)
        List<Node> nodes_without_start_end = new ArrayList<>(nodes);
        nodes_without_start_end.remove(start_node);
        nodes_without_start_end.remove(finish_node);

        for (Node n : nodes_without_start_end) {
            int min_successor_start = Collections.min(n.successors.stream().map(succ -> succ.es).toList());
            n.freeFloat = min_successor_start - n.ef;
        }

        start_node.freeFloat = 0;
        finish_node.freeFloat = 0;

        int sum_of_durations = nodes_without_start_end.stream().mapToInt(elem -> elem.duration).sum();
        int sum_of_durations_free_float = nodes_without_start_end.stream().mapToInt(elem -> (elem.duration + elem.freeFloat)).sum();
        freeFloatRatio = (double) sum_of_durations / sum_of_durations_free_float;

        //set utilization factor
        double[] utilizations = new double[number_of_renewable_resources];
        double[] workContents = new double[number_of_renewable_resources];

        for (int k = 0; k < number_of_renewable_resources; k++) {
            for (Node n : nodes) {
                workContents[k] += n.renewable_resource_requirements.get(k) * n.duration;
            }
            utilizations[k] = workContents[k] / (finish_node.ef * renewable_resource_availability.get(k));
        }

        if (number_of_renewable_resources == 0) {
            maxUtilization = 0;
        } else {
            maxUtilization = Arrays.stream(utilizations).max().getAsDouble();
        }
    }

    private void set_succ_pred_sizes() {
        for (Node n : nodes) {
            n.number_of_successors = n.successors.size();
            n.number_of_predecessors = n.predecessors.size();
            n.number_of_transitive_successors = n.transitive_successors.size();
            n.number_of_transitive_predecessors = n.transitive_predecessors.size();
        }
    }

    private void set_succ_pred_names() {
        for (Node n : nodes) {
            n.successors_names = n.successors.stream().map(s -> s.name).toList();
            n.predecessors_names = n.predecessors.stream().map(p -> p.name).toList();

            n.transitive_successors_names = n.transitive_successors.stream().map(s -> s.name).toList();
            n.transitive_predecessors_names = n.transitive_predecessors.stream().map(p -> p.name).toList();
        }
    }

    public void setup_project() {
        calculate_es_ef_ls_lf_small_horizon();
        set_node_attributes();
    }

    public void setup_project_extended() {
        calculate_es_ef_ls_lf_small_horizon();
        set_node_attributes_extended();
    }

    public void store_as_json() {

    }
}
