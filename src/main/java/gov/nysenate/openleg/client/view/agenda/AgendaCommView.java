package gov.nysenate.openleg.client.view.agenda;

import gov.nysenate.openleg.client.view.base.ListView;
import gov.nysenate.openleg.client.view.base.ViewObject;
import gov.nysenate.openleg.model.agenda.Agenda;
import gov.nysenate.openleg.model.agenda.AgendaInfoCommittee;
import gov.nysenate.openleg.model.agenda.AgendaVoteCommittee;
import gov.nysenate.openleg.model.entity.CommitteeId;

import java.util.ArrayList;
import java.util.List;

public class AgendaCommView implements ViewObject
{
    private CommitteeId committeeId;
    private ListView<AgendaCommAddendumView> addenda;

    public AgendaCommView(CommitteeId committeeId, Agenda agenda) {
        this.committeeId = committeeId;
        List<AgendaCommAddendumView> addendaList = new ArrayList<>();
        if (agenda != null) {
            for (String addendumId : agenda.getAgendaInfoAddenda().keySet()) {
                AgendaInfoCommittee infoComm = null;
                AgendaVoteCommittee voteComm = null;
                if (agenda.getAgendaInfoAddenda().containsKey(addendumId) &&
                    agenda.getAgendaInfoAddendum(addendumId).getCommitteeInfoMap().containsKey(committeeId)) {
                    infoComm = agenda.getAgendaInfoAddendum(addendumId).getCommitteeInfoMap().get(committeeId);
                }
                if (infoComm != null) {
                    if (agenda.getAgendaVoteAddenda().containsKey(addendumId) &&
                        agenda.getAgendaVoteAddendum(addendumId).getCommitteeVoteMap().containsKey(committeeId)) {
                        voteComm = agenda.getAgendaVoteAddendum(addendumId).getCommitteeVoteMap().get(committeeId);
                    }
                    addendaList.add(new AgendaCommAddendumView(addendumId, infoComm, voteComm));
                }
            }
            this.addenda = ListView.of(addendaList);
        }
    }

    public CommitteeId getCommitteeId() {
        return committeeId;
    }

    public ListView<AgendaCommAddendumView> getAddenda() {
        return addenda;
    }

    @Override
    public String getViewType() {
        return "agenda-committee";
    }
}