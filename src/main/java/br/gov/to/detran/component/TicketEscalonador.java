package br.gov.to.detran.component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.enumeration.DiaSemana;
import br.gov.to.detran.repository.TicketSupportRepository;
import br.gov.to.detran.repository.UserSecurityRepository;

@RequestScoped
public class TicketEscalonador implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private @Inject
	UserSecurityRepository userSecurityRepository;
	private @Inject
	TicketSupportRepository ticketSupportRepository;
	
	public UserSecurity selecionarProximoAtendente(UserSecurity userAtendente, TicketService service){
    	/**
         * 0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday
         */        
        DiaSemana diaSemana = DiaSemana.getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        List<Long> atendentesIds = this.userSecurityRepository.solicitanteIds(userAtendente, service, diaSemana, new Date());        
        System.out.println(Arrays.deepToString(atendentesIds.toArray()));
        Collections.shuffle(atendentesIds);
        Map<Long, Object> contarChamados = this.ticketSupportRepository.contarChamados(atendentesIds);
        Long id = Long.MAX_VALUE;
        Long count = Long.MAX_VALUE;
        for (Long atendente : atendentesIds) {
            Long tempCount = 0L;
            Integer key = atendente.intValue();            
            if (contarChamados.containsKey(atendente)) {           
            	LinkedHashSet obj = (LinkedHashSet) contarChamados.get(atendente);
            	Long value = (Long) obj.toArray()[0];
                tempCount = value;                 
            }
            if (tempCount < count) {
                id = atendente;
                count = tempCount;
            }
        }
        try {
            UserSecurity user = userSecurityRepository.getInstancePorId(id);
            return user;
        } catch (Exception e) {
        	e.printStackTrace();            
        }
        return null;
    	
    }
}