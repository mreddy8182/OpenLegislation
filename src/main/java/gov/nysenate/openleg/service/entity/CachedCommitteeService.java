package gov.nysenate.openleg.service.entity;

import gov.nysenate.openleg.dao.entity.CommitteeDao;
import gov.nysenate.openleg.model.entity.Chamber;
import gov.nysenate.openleg.model.entity.Committee;
import net.sf.ehcache.CacheManager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class CachedCommitteeService implements CommitteeService{

    private static final Logger logger = LoggerFactory.getLogger(CachedCommitteeService.class);

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CommitteeDao committeeDao;

    @PostConstruct
    private void init(){
        cacheManager.addCache("committeeCurrent");
        cacheManager.addCache("committeeVersion");
        cacheManager.addCache("committeeList");
        cacheManager.addCache("committeeHistory");
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(value = "committeeCurrent", key = "#name + #chamber.toString()")
    public Committee getCommittee(String name, Chamber chamber) throws CommitteeNotFoundEx {
        if(name==null){
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if(chamber==null) {
            throw new IllegalArgumentException("Chamber cannot be null!");
        }
        try{
            return committeeDao.getCommittee(name, chamber);
        }
        catch(Exception ex){
            throw new CommitteeNotFoundEx(name, chamber, ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(value = "committeeVersion", key = "#name + #chamber.toString() + #session + #time.toString()")
    public Committee getCommittee(String name, Chamber chamber, int session, Date time) throws CommitteeNotFoundEx {
        if(name==null){
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if(chamber==null) {
            throw new IllegalArgumentException("Chamber cannot be null!");
        }
        if(time==null){
            throw new IllegalArgumentException("Time cannot be null!");
        }
        DateTime now = new DateTime();
        if(time.after(now.toDate()) && (now.getYear()<=session || now.getYear()%2==0 && now.getYear()==session+1)){
            logger.debug("Using committeeCurrent instead of committeeVersion for " + chamber + " " + name + " " + session + " " + time);
            return getCommittee(name, chamber);
        }
        try{
            return committeeDao.getCommittee(name, chamber, session, time);
        }
        catch(Exception ex){
            throw new CommitteeNotFoundEx(name, chamber, session, time, ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(value = "committeeList", key = "#chamber.toString()")
    public List<Committee> getCommitteeList(Chamber chamber) throws CommitteeNotFoundEx {
        if (chamber==null){
            throw new IllegalArgumentException("Chamber cannot be null!");
        }
        try{
            return committeeDao.getCommitteeList(chamber);
        }
        catch(Exception ex){
            throw new CommitteeNotFoundEx(chamber, ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(value = "committeeHistory", key = "#name + #chamber.toString()")
    public List<Committee> getCommitteeHistory(String name, Chamber chamber) throws CommitteeNotFoundEx {
        if(name==null){
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if(chamber==null) {
            throw new IllegalArgumentException("Chamber cannot be null!");
        }

        try{
            return committeeDao.getCommitteeHistory(name, chamber);
        }
        catch(Exception ex){
            throw new CommitteeNotFoundEx(name, chamber, ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "committeeCurrent", key = "#committee.getName() + #committee.getChamber().toString()"),
            @CacheEvict(value = "committeeVersion", allEntries = true),
            @CacheEvict(value = "committeeList", key = "#committee.getChamber().toString()"),
            @CacheEvict(value = "committeeHistory", key = "#committee.getName() + #committee.getChamber().toString()")
        }
    )
    public void updateCommittee(Committee committee) {
        if(committee==null){
            throw new IllegalArgumentException("Committee cannot be null.");
        }
        committeeDao.updateCommittee(committee);
    }

    /** {@inheritDoc} */
    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "committeeCurrent", key = "#committee.getName() + #committee.getChamber().toString()"),
            @CacheEvict(value = "committeeVersion", allEntries = true),
            @CacheEvict(value = "committeeList", key = "#committee.getChamber().toString()"),
            @CacheEvict(value = "committeeHistory", key = "#committee.getName() + #committee.getChamber().toString()")
        }
    )
    public void deleteCommittee(Committee committee) {
        if(committee==null){
            throw new IllegalArgumentException("Committee cannot be null.");
        }
        committeeDao.deleteCommittee(committee);
    }
}
