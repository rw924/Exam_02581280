

public class Main {
    public static void main(String[] args) {
        public abstract class Assessment {
            private final String title;
            private final double weight;
            private final boolean aiPermitted;

            public Assessment(String title, double weight, boolean aiPermitted) {
                this.title = title;
                this.weight = weight;
                this.aiPermitted = aiPermitted;
            }

            public abstract double calculateFinalMark();

            public abstract Map<string, Double> getSubMarks();

            public class Student {
                private final String cid;
                private final String name;
                private final String type; // "UG" or "MSc"
                private final List<Assessment> assessments = new ArrayList<>();

                //  addAssessment method

                public double getOverallWeightedMark() {
                    double total = 0.0;
                    for (Assessment a : assessments) {
                        total += a.calculateFinalMark() * a.getWeight();
                    }
                    return total;
                }

                public double getAiAverage(boolean aiPermitted) {
                    return assessments.stream()
                            .filter(a -> a.isAiPermitted() == aiPermitted)
                            .mapToDouble(Assessment::calculateFinalMark)
                            .average()
                            .orElse(0.0);
                }

                public boolean hasAiDiscrepancy() {
                    double aiAvg = getAiAverage(true);
                    double nonAiAvg = getAiAverage(false);
                    return (aiAvg - nonAiAvg) >= 20.0;


                }

            }
        }
    }
    public class AssessmentGridUI extends JFrame {
        public AssessmentGridUI(List<Student> students) {
            setTitle("Module Assessment Results");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Name, CID, Type, 4 Assessments, Overall Mark = 8
            int cols = 4 + students.get(0).getAssessments().size();
            JPanel mainGrid = new JPanel(new GridLayout(students.size() + 1, cols, 2, 2));
            mainGrid.setBackground(Color.GRAY);

            // 1. header row
            addHeaderRow(mainGrid, students.get(0));

            // 2. add student data row
            for (Student s : students) {
                Color rowColor = s.hasAiDiscrepancy() ? Color.RED : Color.WHITE;

                addCell(mainGrid, s.getName(), rowColor);
                addCell(mainGrid, s.getCid(), rowColor);
                addCell(mainGrid, s.getType(), rowColor);

                for (Assessment a : s.getAssessments()) {

                    addCell(mainGrid, String.format("%.2f%%", a.calculateFinalMark()), rowColor);
                }

                addCell(mainGrid, String.format("%.2f%%", s.getOverallWeightedMark()), rowColor);
            }

            add(new JScrollPane(mainGrid));
            pack();
            setLocationRelativeTo(null);
        }

        private void addCell(JPanel grid, String text, Color bgColor) {
            JPanel cellPanel = new JPanel(new BorderLayout());
            cellPanel.setBackground(bgColor);
            cellPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            JLabel label = new JLabel(text, SwingConstants.CENTER);
            cellPanel.add(label, BorderLayout.CENTER);
            grid.add(cellPanel);
        }
    }

}