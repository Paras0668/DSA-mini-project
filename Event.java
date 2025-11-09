import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Event implements Comparable<Event> {
    
    private String title;
    private LocalDateTime dateTime;
    private String description;

    // Formatter for user input/display
    public static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Event(String title, LocalDateTime dateTime, String description) {
        this.title = title;
        this.dateTime = dateTime;
        this.description = description;
    }

    // Getters
    public String getTitle() { return title; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }
    
    /**
     * DSA Core Logic: Defines the natural ordering for the PriorityQueue.
     * Events are sorted by their date and time (Min-Heap property).
     */
    @Override
    public int compareTo(Event other) {
        return this.dateTime.compareTo(other.dateTime);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", 
            dateTime.format(FORMATTER), title, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(title, event.title) && Objects.equals(dateTime, event.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, dateTime);
    }
}
