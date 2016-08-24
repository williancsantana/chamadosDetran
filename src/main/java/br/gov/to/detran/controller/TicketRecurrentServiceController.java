package br.gov.to.detran.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.to.detran.domain.TicketRecurrentService;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketRecurrentServiceRepository;
import br.gov.to.detran.repository.TicketServiceRepository;
import br.gov.to.detran.util.FacesUtil;

@Named
@ViewScoped
public class TicketRecurrentServiceController extends BaseController<TicketRecurrentService> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TicketRecurrentServiceRepository repository;
	
	@Inject
	private TicketServiceRepository repositorioServicos;
	
	private String nomeServicoRecorrente;
	private String mensagemPadrao;
	private TicketService servico;
	private String peridiocidade;
	
	private List<TicketService> listaServicos;
	private List<String> listaPeridiocidade = new ArrayList<String>();
	
	
	
	
	@PostConstruct
	public void postConstruct(){
		super.postContruct();
		TicketRecurrentService servicorecorrente = this.getFlash("servicoRecorrenteInstance");
		
		if(servicorecorrente != null){
			this.instance = servicorecorrente;
		}
		
		setListaServicos(repositorioServicos.getAll());
		listaPeridiocidade.add("ANUAL");
		listaPeridiocidade.add("MENSAL");
		listaPeridiocidade.add("SEMANAL");
		listaPeridiocidade.add("DIÁRIO");
	}
	
	public void insert(){
		try{
			this.criarServicoRecorrente(instance);
			this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro adicionado", "Cadastro");
		}catch(java.io.IOException ex){
			ex.printStackTrace();
		}
		catch(Exception ex){
			ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Validação");
		}
		
	}	
	
	public void criarServicoRecorrente(TicketRecurrentService recurrentService) throws Exception{
		recurrentService.setAssunto(nomeServicoRecorrente);
		recurrentService.setDescricao(mensagemPadrao);
		recurrentService.setSolicitante(FacesUtil.loggedUser());
		recurrentService.setServico(servico);
		
		repository.insert(recurrentService);
		redirectWithMessage("Teste","Serviço recorrente nº" + recurrentService.getId(),FacesUtil.INFO);
	}
	
	public void redirectWithMessage(String title, String msg, FacesMessage.Severity s) throws IOException{
		addMenssage(s, "Serviço recorrente", title);
		redirect("/index.jsf");
	}
	
	@Override
	public Repository getRepository() {
		// TODO Auto-generated method stub
		return this.repository;
	}


	public String getNomeServicoRecorrente() {
		return nomeServicoRecorrente;
	}


	public void setNomeServicoRecorrente(String nomeServicoRecorrente) {
		this.nomeServicoRecorrente = nomeServicoRecorrente;
	}


	public String getMensagemPadrao() {
		return mensagemPadrao;
	}


	public void setMensagemPadrao(String mensagemPadrao) {
		this.mensagemPadrao = mensagemPadrao;
	}


	public TicketService getServico() {
		return servico;
	}


	public void setServico(TicketService servico) {
		this.servico = servico;
	}


	public List<TicketService> getListaServicos() {
		return listaServicos;
	}


	public void setListaServicos(List<TicketService> listaServicos) {
		this.listaServicos = listaServicos;
	}


	public String getPeridiocidade() {
		return peridiocidade;
	}


	public void setPeridiocidade(String peridiocidade) {
		this.peridiocidade = peridiocidade;
	}


	public List<String> getListaPeridiocidade() {
		return listaPeridiocidade;
	}


	public void setListaPeridiocidade(List<String> listaPeridiocidade) {
		this.listaPeridiocidade = listaPeridiocidade;
	}
	
	
	
}
