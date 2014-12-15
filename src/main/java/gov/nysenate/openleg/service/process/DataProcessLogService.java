package gov.nysenate.openleg.service.process;

import com.google.common.collect.Range;
import gov.nysenate.openleg.dao.base.LimitOffset;
import gov.nysenate.openleg.dao.base.PaginatedList;
import gov.nysenate.openleg.model.process.DataProcessRun;
import gov.nysenate.openleg.model.process.DataProcessUnit;

import java.time.LocalDateTime;
import java.util.List;

public interface DataProcessLogService
{
    /**
     * Returns a paginated list of DataProcessRuns that have been stored in the persistence layer.
     *
     * @param dateTimeRange Range<LocalDateTime> - The date/time range during which the runs started.
     * @param limOff LimitOffset - Limit the result set.
     * @param showActivityOnly boolean - Set to true to only return runs that have units associated with them.
     * @return List<DataProcessRun>
     */
    public PaginatedList<DataProcessRun> getRuns(Range<LocalDateTime> dateTimeRange, LimitOffset limOff,
                                                 boolean showActivityOnly);

    /**
     * Returns a paginated list of DataProcessUnits that are associated with the given process run id.
     *
     * @param processId int
     * @param limOff LimitOffset
     * @return PaginatedList<DataProcessUnit>
     */
    public PaginatedList<DataProcessUnit> getUnits(int processId, LimitOffset limOff);

    /**
     * Registers and returns a new data processing run.
     *
     * @param startDateTime LocalDateTime - When processing started
     * @param invoker String - How this process run was invoked.
     * @return DataProcessRun
     */
    public DataProcessRun startNewRun(LocalDateTime startDateTime, String invoker);

    /**
     * Saves the process unit and associates it with the given processId.
     *
     * @param processId int
     * @param unit DataProcessUnit
     */
    public void addUnit(int processId, DataProcessUnit unit);

    /**
     * Marks the given run as completed by setting the end date/time and
     * updates the run in the backing store.
     *
     * @param run DataProcessRun
     */
    public void finishRun(DataProcessRun run);
}
