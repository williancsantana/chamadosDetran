package br.gov.to.detran.repository;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.domain.QTicketRecurrentServiceSchedule;
import br.gov.to.detran.domain.TicketRecurrentServiceSchedule;

@Transactional
public class TicketRecurrentServiceScheduleRepository extends AbstractRepository<TicketRecurrentServiceSchedule>{

	@Override
	public EntityPathBase<TicketRecurrentServiceSchedule> getEntityPath() {
		// TODO Auto-generated method stub
		return QTicketRecurrentServiceSchedule.ticketRecurrentServiceSchedule;
	}

}
