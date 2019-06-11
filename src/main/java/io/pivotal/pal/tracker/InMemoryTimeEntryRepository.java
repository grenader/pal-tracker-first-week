package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> allEntries = new HashMap<>();
    private long lastEntry = 0;

    public TimeEntry create(TimeEntry entry) {
        if (entry.getId() == -1)
            entry.setTimeEntryId(++lastEntry);
        allEntries.put(entry.getId(), entry);
        return entry;
    }

    public TimeEntry find(long timeEntryId) {
        if (timeEntryId == -1)
            return null;
        return allEntries.get(timeEntryId);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(allEntries.values());

    }

    public TimeEntry update(long id, TimeEntry entry) {
        TimeEntry timeEntry = find(id);
        if (timeEntry == null)
            return null;
        allEntries.put(id, entry);
        entry.setTimeEntryId(id);

        return entry;
    }

    public TimeEntry delete(long id) {
        TimeEntry timeEntry = find(id);
        if (timeEntry != null)
            allEntries.remove(id);
        return timeEntry;
    }
}
