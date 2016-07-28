package br.gov.to.detran.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.to.detran.domain.EscalaTrabalho;
import br.gov.to.detran.domain.TicketStickerSupport;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketStickerRepository;
import br.gov.to.detran.util.FacesUtil;


@Named
@ViewScoped
public class TicketStickerController extends BaseController<TicketStickerSupport> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject
	TicketStickerRepository repository;
	private Date dataLembrete;
	
	 @PostConstruct
	    public void postConstruct() {
		 	
	        super.postContruct();
	        TicketStickerSupport sticker = this.getFlash("lembreteInstance");
	       
	        if (sticker != null) {
	            this.instance = sticker;
	            
	            
	        }
	        
	    }

	
	@Override
	public Repository getRepository() {
		
		return repository;
	}
	
	public void exibirMensagem(Boolean exibirMensagem){
		FacesUtil.loggedUser().setShowNotification(exibirMensagem);
		
	}
	
	public boolean showSticker(UserSecurity atendente){
		List<TicketStickerSupport> lembretes = this.repository.getLembretesAtendente(atendente);
		Boolean notification = true;
		if(FacesUtil.loggedUser()!=null)
			notification = FacesUtil.loggedUser().getShowNotification();
		if(notification==null)
			notification = true;
		
		if(lembretes!=null && lembretes.size()>0  && notification){
				
		return true;
		}
		return false;
		
	}
	
	
    public boolean existeLembrete(TicketSupport chamado, UserSecurity atendente){
    	TicketStickerSupport lembrete = this.repository.getLembreteChamado(chamado);
    	if(lembrete!=null && chamado.getAtendente().getId().equals(atendente.getId())&& chamado.getStatus()!=TicketSupportStatus.FECHADO)
    		return true;
    	return false;
    }
    
    public boolean naoExisteLembrete(TicketSupport chamado, UserSecurity atendente){
    	TicketStickerSupport lembrete = this.repository.getLembreteChamado(chamado);
    	if(lembrete==null && chamado.getAtendente().getId().equals(atendente.getId()) && chamado.getStatus()!=TicketSupportStatus.FECHADO)
    		return true;
    	return false;
    }
    
    public void removeLembrete(TicketSupport chamado){
    	TicketStickerSupport lembrete = this.repository.getLembreteChamado(chamado);
    	if(lembrete!=null){
    		this.instance = lembrete;
    		try {
				this.repository.remove(this.instance);
				FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(null, new FacesMessage("Sucesso",  "Lembrete removido com sucesso  chamado: " + this.getInstance().getTicketSupport().getNumero()) );	
	    		context.getCurrentInstance().getExternalContext().redirect("index.jsf");
	    		return;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    		
    	}  
    	
    }
    public void adicionarLembrete(TicketSupport chamado){
    	try {
    		this.instance.setHoraLembrete(dataLembrete);
    		instance.setUpdated(new Date());
    		instance.setTicketSupport(chamado);
    		TicketStickerSupport lembrete = this.repository.getLembreteChamado(chamado);
    		if(lembrete!=null){
    			instance.setCreated(lembrete.getCreated());
    			instance.setId(lembrete.getId());
    			repository.update(this.instance);
    			FacesUtil.loggedUser().setShowNotification(true);
    		}else{
    			instance.setCreated(new Date());
    			repository.insert(this.instance);
    			FacesUtil.loggedUser().setShowNotification(true);
    		}
    		
			
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage("Sucesso",  "Lembrete adicionado com sucesso ao chamado: " + this.getInstance().getTicketSupport().getNumero()) );
	        context.getCurrentInstance().getExternalContext().redirect("index.jsf");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void setDataLembrete(Date dataLembrete){
    	this.dataLembrete = dataLembrete;
    }
    
    public Date getDataLembrete(){
    	return this.dataLembrete;
    }
    

}
