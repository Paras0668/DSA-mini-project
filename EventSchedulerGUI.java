import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.PriorityQueue;

public class EventSchedulerGUI extends JFrame implements ActionListener {
    
    // --- DSA Component: Priority Queue (Min-Heap) ---
    private PriorityQueue<Event> eventQueue;
    
    // --- GUI Components ---
    private JTextArea displayArea;
    private JTextField titleField, dateTimeField;
    private JTextArea descriptionArea;
    private JButton addButton, viewNextButton, viewAllButton;

    // Define the placeholder text as a constant
    private static final String DATE_PLACEHOLDER = "YYYY-MM-DD HH:MM";

    public EventSchedulerGUI() {
        // Initialize the Priority Queue
        eventQueue = new PriorityQueue<>();

        // --- Frame Setup ---
        setTitle("Event Scheduling System (DSA: Min-Heap)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Assemble Layout ---
        JPanel controls = new JPanel(new BorderLayout());
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controls.add(createInputPanel(), BorderLayout.NORTH);
        controls.add(createButtonPanel(), BorderLayout.CENTER);
        
        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        add(controls, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        titleField = new JTextField(20);
        dateTimeField = new JTextField(DATE_PLACEHOLDER); // Set placeholder text
        descriptionArea = new JTextArea(3, 20);
        
        // --- CORRECTED IMPLEMENTATION: Focus Listener for Placeholder Text ---
        dateTimeField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // If the current text is the placeholder, clear it for typing
                if (dateTimeField.getText().equals(DATE_PLACEHOLDER)) {
                    dateTimeField.setText(""); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // If the user leaves the field and it's empty, put the placeholder back
                if (dateTimeField.getText().isEmpty()) {
                    dateTimeField.setText(DATE_PLACEHOLDER);
                }
            }
        });
        // ----------------------------------------------------------------------

        inputPanel.add(new JLabel("Event Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Date/Time (" + DATE_PLACEHOLDER + "):"));
        inputPanel.add(dateTimeField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(descriptionArea));
        
        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Event");
        viewNextButton = new JButton("View Next Event");
        viewAllButton = new JButton("View All Events");
        
        addButton.addActionListener(this);
        viewNextButton.addActionListener(this);
        viewAllButton.addActionListener(this);
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewNextButton);
        buttonPanel.add(viewAllButton);
        
        return buttonPanel;
    }

    // --- Event Handling (ActionListener) ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addEvent();
        } else if (e.getSource() == viewNextButton) {
            viewNextEvent();
        } else if (e.getSource() == viewAllButton) {
            viewAllEvents();
        }
    }

    // --- Core DSA Operations ---

    // Adds a new event to the PriorityQueue (O(log n))
    private void addEvent() {
        String title = titleField.getText();
        String dateTimeStr = dateTimeField.getText();
        String description = descriptionArea.getText();
        
        if (title.isEmpty() || dateTimeStr.isEmpty() || dateTimeStr.equals(DATE_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Title and a valid Date/Time are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, Event.FORMATTER);
            Event newEvent = new Event(title, dateTime, description);
            
            // PriorityQueue.offer() is the O(log n) insertion
            eventQueue.offer(newEvent); 
            
            displayArea.setText(""); 
            displayArea.append("Event Added Successfully:\n" + newEvent.toString());
            
            // Clear input fields for next entry
            titleField.setText("");
            descriptionArea.setText("");
            // Reset placeholder in case it was cleared by focusGained but not typed into
            dateTimeField.setText(DATE_PLACEHOLDER); 

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date/Time format. Use " + DATE_PLACEHOLDER, "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Views the next event (O(1))
    private void viewNextEvent() {
        displayArea.setText(""); 
        if (eventQueue.isEmpty()) {
            displayArea.append("No events scheduled.");
        } else {
            // PriorityQueue.peek() is the O(1) retrieval of the minimum element
            Event nextEvent = eventQueue.peek(); 
            displayArea.append("NEXT UPCOMING EVENT (Priority Queue Top):\n\n");
            displayArea.append(nextEvent.toString());
        }
    }

    // Views all events in sorted order (O(n log n))
    private void viewAllEvents() {
        displayArea.setText(""); 
        if (eventQueue.isEmpty()) {
            displayArea.append("No events scheduled.");
        } else {
            displayArea.append("ALL SCHEDULED EVENTS (Sorted by Date/Time):\n\n");
            
            // Use a temporary copy to display contents without destroying the original queue
            PriorityQueue<Event> tempQueue = new PriorityQueue<>(eventQueue);
            
            // Poll repeatedly to extract and display events in sorted order
            while (!tempQueue.isEmpty()) {
                Event event = tempQueue.poll();
                displayArea.append(event.toString() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventSchedulerGUI());
    }
}
