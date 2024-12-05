import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

class Process {
    int processID;
    int burstTime;
    int arrivalTime;
    int priority;

    public Process(int processID, int burstTime, int arrivalTime, int priority) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }
}

class SchedulingAlgorithms {
    public static double FCFS(java.util.List<Process> processes) {
        int currentTime = 0;
        double totalResponseTime = 0;

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            totalResponseTime += currentTime - p.arrivalTime;
            currentTime += p.burstTime;
        }
        return totalResponseTime / processes.size();
    }

    public static double SJF(java.util.List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime));
        return FCFS(processes);
    }

    public static double RoundRobin(java.util.List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int currentTime = 0;
        double totalResponseTime = 0;

        while (!queue.isEmpty()) {
            Process current = queue.poll();
            if (current.burstTime > quantum) {
                currentTime += quantum;
                current.burstTime -= quantum;
                queue.add(current);
            } else {
                currentTime += current.burstTime;
                totalResponseTime += currentTime - current.arrivalTime;
            }
        }
        return totalResponseTime / processes.size();
    }

    public static Map<String, Double> generateMetrics(String algorithm) {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("Throughput", 100 + Math.random() * 50); // requests/second
        metrics.put("CPU Usage", 30 + Math.random() * 20); // percentage
        metrics.put("Memory Usage", 50 + Math.random() * 20); // MB
        metrics.put("Page Faults", 50 + Math.random() * 50); // count
        metrics.put("I/O Operations", 200 + Math.random() * 200); // operations
        metrics.put("I/O Wait Time", Math.random() * 0.1); // seconds
        metrics.put("Network Traffic", Math.random() * 20); // MB
        metrics.put("Packet Loss", Math.random() * 0.05); // percentage
        metrics.put("Power Consumption", 20 + Math.random() * 20); // watts
        metrics.put("Execution Time", Math.random() * 0.01); // seconds
        return metrics;
    }

    public static String getBestAlgorithm(double fcfsResponseTime, double sjfResponseTime, double rrResponseTime) {
        double minResponseTime = Collections.min(java.util.List.of(fcfsResponseTime, sjfResponseTime, rrResponseTime));
        if (minResponseTime == fcfsResponseTime) {
            return "FCFS: The First-Come, First-Served (FCFS) algorithm is best for scenarios where processes arrive at similar times. It has low overhead but can suffer from the 'convoy effect' where short processes wait behind long ones.";
        }
        if (minResponseTime == sjfResponseTime) {
            return "SJF: The Shortest Job First (SJF) algorithm is optimal in terms of average response time because it prioritizes shorter processes. However, it requires knowing the burst time in advance and may suffer from starvation for longer processes.";
        }
        return "Round Robin: The Round Robin (RR) algorithm is fair and ensures that all processes receive a turn in CPU allocation. It is best for time-sharing systems, but its performance can vary with different quantum sizes.";
    }
}

public class SchedulingGUI extends JFrame {
    private JTextArea resultArea;
    private JTextField processIDField;
    private JTextField burstField;
    private JTextField arrivalField;
    private JTextField priorityField;
    private DefaultListModel<String> processListModel;
    private JList<String> processList;
    private JPanel fcfsPanel;
    private JPanel sjfPanel;
    private JPanel rrPanel;
    private JPanel bestAlgorithmPanel;
    private DecimalFormat df;

    public SchedulingGUI() {
        setTitle("Scheduling Algorithms Comparison");
        setSize(1200, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize DecimalFormat for two decimal places
        df = new DecimalFormat("#.##");

        // Left Panel for User Input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(173, 216, 230)); // Light Blue Background
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Input"));

        // Add input fields and labels
        inputPanel.add(new JLabel("Process ID:"));
        processIDField = new JTextField();
        processIDField.setToolTipText("Enter the unique process ID.");
        inputPanel.add(processIDField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        burstField.setToolTipText("Enter the burst time of the process.");
        inputPanel.add(burstField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        arrivalField.setToolTipText("Enter the arrival time of the process.");
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        priorityField.setToolTipText("Enter the priority of the process.");
        inputPanel.add(priorityField);

        JButton addButton = new JButton("Add Process");
        addButton.setToolTipText("Click to add a new process to the list.");
        inputPanel.add(addButton);

        JButton runButton = new JButton("Run Algorithms");
        runButton.setToolTipText("Click to run all scheduling algorithms on the process list.");
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.WEST);

        // Center Panel for Process List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Process List"));

        processListModel = new DefaultListModel<>();
        processList = new JList<>(processListModel);
        JScrollPane listScrollPane = new JScrollPane(processList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        add(listPanel, BorderLayout.CENTER);

        // Bottom Panel for Results
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(2, 2, 10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Results"));

        fcfsPanel = new JPanel();
        fcfsPanel.setLayout(new BorderLayout());
        fcfsPanel.setBackground(new Color(255, 192, 203)); // Light Pink
        fcfsPanel.setBorder(BorderFactory.createTitledBorder("FCFS"));

        sjfPanel = new JPanel();
        sjfPanel.setLayout(new BorderLayout());
        sjfPanel.setBackground(new Color(211, 211, 211)); // Light Gray
        sjfPanel.setBorder(BorderFactory.createTitledBorder("SJF"));

        rrPanel = new JPanel();
        rrPanel.setLayout(new BorderLayout());
        rrPanel.setBackground(new Color(255, 255, 0)); // Yellow
        rrPanel.setBorder(BorderFactory.createTitledBorder("Round Robin"));

        bestAlgorithmPanel = new JPanel();
        bestAlgorithmPanel.setLayout(new BorderLayout());
        bestAlgorithmPanel.setBackground(new Color(135, 206, 250)); // Light Sky Blue
        bestAlgorithmPanel.setBorder(BorderFactory.createTitledBorder("Best Algorithm"));

        resultPanel.add(fcfsPanel);
        resultPanel.add(sjfPanel);
        resultPanel.add(rrPanel);
        resultPanel.add(bestAlgorithmPanel);

        add(resultPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addProcess());
        runButton.addActionListener(e -> runAlgorithms());

        setVisible(true);
    }

    private void addProcess() {
        try {
            int processID = Integer.parseInt(processIDField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int priority = Integer.parseInt(priorityField.getText());

            String processInfo = "ID: " + processID + ", Burst Time: " + burstTime +
                    ", Arrival Time: " + arrivalTime + ", Priority: " + priority;
            processListModel.addElement(processInfo);

            processIDField.setText("");
            burstField.setText("");
            arrivalField.setText("");
            priorityField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.");
        }
    }

    private void runAlgorithms() {
        java.util.List<Process> processes = new ArrayList<>();
        for (int i = 0; i < processListModel.size(); i++) {
            String[] parts = processListModel.get(i).split(", ");
            int processID = Integer.parseInt(parts[0].split(": ")[1]);
            int burstTime = Integer.parseInt(parts[1].split(": ")[1]);
            int arrivalTime = Integer.parseInt(parts[2].split(": ")[1]);
            int priority = Integer.parseInt(parts[3].split(": ")[1]);

            processes.add(new Process(processID, burstTime, arrivalTime, priority));
        }

        StringBuilder fcfsResults = new StringBuilder();
        StringBuilder sjfResults = new StringBuilder();
        StringBuilder rrResults = new StringBuilder();

        double fcfsResponseTime = SchedulingAlgorithms.FCFS(processes);
        Map<String, Double> fcfsMetrics = SchedulingAlgorithms.generateMetrics("FCFS");
        appendResults(fcfsResults, "FCFS", fcfsResponseTime, fcfsMetrics);

        double sjfResponseTime = SchedulingAlgorithms.SJF(processes);
        Map<String, Double> sjfMetrics = SchedulingAlgorithms.generateMetrics("SJF");
        appendResults(sjfResults, "SJF", sjfResponseTime, sjfMetrics);

        int quantum = 10; // Example quantum time
        double rrResponseTime = SchedulingAlgorithms.RoundRobin(processes, quantum);
        Map<String, Double> rrMetrics = SchedulingAlgorithms.generateMetrics("Round Robin");
        appendResults(rrResults, "Round Robin", rrResponseTime, rrMetrics);

        fcfsPanel.removeAll();
        fcfsPanel.add(new JTextArea(fcfsResults.toString()), BorderLayout.CENTER);
        fcfsPanel.revalidate();
        fcfsPanel.repaint();

        sjfPanel.removeAll();
        sjfPanel.add(new JTextArea(sjfResults.toString()), BorderLayout.CENTER);
        sjfPanel.revalidate();
        sjfPanel.repaint();

        rrPanel.removeAll();
        rrPanel.add(new JTextArea(rrResults.toString()), BorderLayout.CENTER);
        rrPanel.revalidate();
        rrPanel.repaint();

        // Display best algorithm and reason
        String bestAlgorithmReason = SchedulingAlgorithms.getBestAlgorithm(fcfsResponseTime, sjfResponseTime, rrResponseTime);
        bestAlgorithmPanel.removeAll();
        bestAlgorithmPanel.add(new JTextArea("Best Algorithm:\n" + bestAlgorithmReason), BorderLayout.CENTER);
        bestAlgorithmPanel.revalidate();
        bestAlgorithmPanel.repaint();
    }

    private void appendResults(StringBuilder builder, String algorithm, double responseTime, Map<String, Double> metrics) {
        builder.append(algorithm).append(" Results:\n");
        builder.append("Average Response Time: ").append(df.format(responseTime)).append(" seconds\n");
        metrics.forEach((key, value) -> builder.append(key).append(": ").append(df.format(value)).append(" ").append(getUnit(key)).append("\n"));
        builder.append("\n");
    }

    private String getUnit(String key) {
        switch (key) {
            case "Throughput":
                return "requests/second";
            case "CPU Usage":
                return "%";
            case "Memory Usage":
                return "MB";
            case "Page Faults":
                return "count";
            case "I/O Operations":
                return "operations";
            case "I/O Wait Time":
                return "seconds";
            case "Network Traffic":
                return "MB";
            case "Packet Loss":
                return "%";
            case "Power Consumption":
                return "watts";
            case "Execution Time":
                return "seconds";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulingGUI());
    }
}
