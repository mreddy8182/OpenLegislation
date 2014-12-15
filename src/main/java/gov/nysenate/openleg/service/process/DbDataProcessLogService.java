package gov.nysenate.openleg.service.process;

import com.google.common.collect.Range;
import gov.nysenate.openleg.dao.base.LimitOffset;
import gov.nysenate.openleg.dao.base.PaginatedList;
import gov.nysenate.openleg.dao.base.SortOrder;
import gov.nysenate.openleg.dao.process.DataProcessLogDao;
import gov.nysenate.openleg.dao.process.SqlDataProcessLogDao;
import gov.nysenate.openleg.model.base.Environment;
import gov.nysenate.openleg.model.process.DataProcessRun;
import gov.nysenate.openleg.model.process.DataProcessUnit;
import org.apache.shiro.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DbDataProcessLogService implements DataProcessLogService
{
    @Autowired private Environment env;
    @Autowired private SqlDataProcessLogDao processLogDao;

    /** {@inheritDoc} */
    @Override
    public PaginatedList<DataProcessRun> getRuns(Range<LocalDateTime> dateTimeRange, LimitOffset limOff,
                                                 boolean showActivityOnly) {
        return processLogDao.getRuns(dateTimeRange, showActivityOnly, SortOrder.DESC, limOff);
    }

    @Override
    public PaginatedList<DataProcessUnit> getUnits(int processId, LimitOffset limOff) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public DataProcessRun startNewRun(LocalDateTime startDateTime, String invoker) {
        DataProcessRun run = new DataProcessRun(0, startDateTime, invoker);
        if (env.isProcessLoggingEnabled()) {
            processLogDao.insertRun(run);
        }
        return run;
    }

    /** {@inheritDoc} */
    @Override
    public void addUnit(int processId, DataProcessUnit unit) {
        if (env.isProcessLoggingEnabled()) {
            processLogDao.insertUnit(processId, unit);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void finishRun(DataProcessRun run) {
        if (env.isProcessLoggingEnabled()) {
            run.setEndDateTime(LocalDateTime.now());
            processLogDao.updateRun(run);
        }
    }
}