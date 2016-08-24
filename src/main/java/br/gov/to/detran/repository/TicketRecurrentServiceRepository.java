package br.gov.to.detran.repository;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.domain.QTicketRecurrentService;
import br.gov.to.detran.domain.TicketRecurrentService;

/**
 * 
 * @author willian
 *
 */

@Transactional
public class TicketRecurrentServiceRepository extends AbstractRepository<TicketRecurrentService> implements java.io.Serializable{

	@Override
	public EntityPathBase<TicketRecurrentService> getEntityPath() {
		// TODO Auto-generated method stub
		return QTicketRecurrentService.ticketRecurrentService;
	}

	
	
}
