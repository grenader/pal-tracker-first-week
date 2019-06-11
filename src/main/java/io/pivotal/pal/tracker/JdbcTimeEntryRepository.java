package io.pivotal.pal.tracker;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate template;
    private GeneratedKeyHolder keyHolder;

    public JdbcTimeEntryRepository(DataSource dataSource) {

        template = new JdbcTemplate(dataSource);
        keyHolder = new GeneratedKeyHolder();
    }

    @Override
    public TimeEntry create(TimeEntry entry) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement("insert into time_entries (project_id, user_id, date, hours)  values(?, ?, ?, ?)",
                                        new String[] {"id"});
                        ps.setLong(1, entry.getProjectId());
                        ps.setLong(2, entry.getUserId());
                        ps.setDate(3, java.sql.Date.valueOf(entry.getDate()));
                        ps.setLong(4, entry.getHours());
                        return ps;
                    }
                },
                keyHolder);
        Number autoKey = keyHolder.getKey();


/*
        int autoId = template.update("insert into time_entries (project_id, user_id, date, hours)  values(?, ?, ?, ?)",
                entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());
*/

        entry.setTimeEntryId(autoKey.longValue());

        return entry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<Map<String, Object>> list = template.queryForList("Select * from time_entries where id = ?",
                new Object[]{timeEntryId});
        if (list == null || list.isEmpty())
            return null;
        Map<String, Object> objectMap = list.get(0);
        return mapDBtoTimeEntry(objectMap);
    }

    private TimeEntry mapDBtoTimeEntry(Map<String, Object> objectMap) {
        return new TimeEntry(((Long) objectMap.get("id")).longValue(), ((Long) objectMap.get("project_id")).longValue(),
                ((Long) objectMap.get("user_id")).longValue(),
                ((Date) objectMap.get("date")).toLocalDate(), ((Integer) objectMap.get("hours")).intValue());
    }

    @Override
    public List<TimeEntry> list() {
        List<Map<String, Object>> list = template.queryForList("Select * from time_entries");
        if (list == null && list.isEmpty())
            return null;

        List<TimeEntry> result = new ArrayList<>();
        for (Map<String, Object> item : list)
            result.add(mapDBtoTimeEntry(item));
        return result;
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        int update = template.update("update time_entries SET project_id = ?, user_id = ?, date = ?, hours = ?  WHERE id =?",
                entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours(), id);

        entry.setTimeEntryId(id);
        return entry;
    }

    @Override
    public TimeEntry delete(long timeEntryId) {
        TimeEntry timeEntry = find(timeEntryId);
        if (timeEntry != null)
            template.update("DELETE FROM time_entries WHERE id = ?",
                    timeEntry.getId());
        return timeEntry;
    }
}
