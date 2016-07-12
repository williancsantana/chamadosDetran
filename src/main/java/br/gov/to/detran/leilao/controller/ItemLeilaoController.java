/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.leilao.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.to.detran.controller.BaseController;
import br.gov.to.detran.leilao.domain.Categoria;
import br.gov.to.detran.leilao.domain.ItemLeilao;
import br.gov.to.detran.leilao.domain.QItemLeilao;
import br.gov.to.detran.leilao.domain.Situacao;
import br.gov.to.detran.leilao.repository.CategoriaRepository;
import br.gov.to.detran.leilao.repository.ItemLeilaoRepository;
import br.gov.to.detran.repository.DetranNETRepository;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class ItemLeilaoController extends BaseController<ItemLeilao> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
	private @Inject ItemLeilaoRepository repository;
	private @Inject CategoriaRepository categoriaRepository;
    private @Inject DetranNETRepository detranRepository;
    private Categoria categoria;
    private String placa; 

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        
        ItemLeilao itemLeilao = this.getFlash("itemLeilaoInstance");
        if (itemLeilao != null) {
            this.instance = itemLeilao;            
        }           
    }
    
    @SuppressWarnings("unchecked")
	public void checkCategoria() throws IOException {
        try {
            if (this.categoria == null || this.categoria.getId() == 0) {
                this.categoria = this.categoriaRepository.getCategoria(Long.parseLong(id));
                if (this.categoria == null) {
                    redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);
                }
                list.getPredicate().add(QItemLeilao.itemLeilao.categoria.id.eq(this.categoria.getId()));
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            this.redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);        
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);
        }
    }
    
    public void redirectWithMensagem(String title, FacesMessage.Severity s) throws IOException {
        addMenssage(s, "Chamado", title);
        redirect("/leilao/edit.jsf");
    }
    
    public void voltarLista(){        
        try {
        	if(categoria != null){
        		redirect("/leilao/veiculo/list.jsf?id=" + String.valueOf(categoria.getId()));
        	}else{
        		redirect("/leilao/categoria/list.jsf");
        	}        	
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void insert() {
        try {        	
        	this.instance.setCategoria(categoria);
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());            
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
            this.instance = new ItemLeilao();
            this.placa = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {        	
            this.instance.setUpdated(new Date());            
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(ItemLeilao instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(ItemLeilaoController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(ItemLeilao instance) {
        try {
            this.repository.remove(instance, true);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void viewVeiculoEdit(ItemLeilao itemLeilao, String url) {
        try {
        	if(itemLeilao != null){
        		setFlash("itemLeilaoInstance", itemLeilao);
        	}	
        	if(categoria != null){
        		this.redirect(url + "?id=" + String.valueOf(categoria.getId()));
        	}else{
        		this.redirect(url);
        	}            
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para o chamado");
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pesquisarPlaca(){
    	try{
    		if(repository.existePlacaCadastrada(this.placa)){
    			throw new Exception("Placa já cadastrada!");
    		}
    		Object[] veiculo = detranRepository.pesquisarVeiculoPlaca(placa);
        	ItemLeilao item = new ItemLeilao();
        	item.setPlaca(String.valueOf(veiculo[3]));    	
        	item.setChassi(String.valueOf(veiculo[2]));
        	item.setRenavam(String.valueOf(veiculo[1]));
        	item.setProprietario(String.valueOf(veiculo[13]));
        	item.setMarcaModelo(String.valueOf(veiculo[4]));
        	item.setCor(String.valueOf(veiculo[5]));
        	item.setCnpj(String.valueOf(veiculo[14]));
        	item.setUf(String.valueOf(veiculo[11]));
        	item.setUf2(String.valueOf(veiculo[18]));
        	item.setMotor(String.valueOf(veiculo[15]));
        	item.setAnoModelo(String.valueOf(veiculo[9])+"/"+String.valueOf(veiculo[10]));    	
        	String tipoLog = String.valueOf(veiculo[19]);
        	String log = String.valueOf(veiculo[16]);
        	String numero = String.valueOf(veiculo[21]);
        	String bairro = String.valueOf(veiculo[22]);
        	
        	item.setEndereco(tipoLog + " " + log + ", n° " + numero + " - " + bairro);
        	item.setCep(String.valueOf(veiculo[17]));
        	item.setCidade((String) veiculo[12]);
        	String restricao1 = (String) veiculo[25];
        	String restricao2 = (String) veiculo[26];
        	String restricao3 = (String) veiculo[27];
        	String restricao4 = (String) veiculo[28];
        	item.setRestricao(restricao1 + ";" + restricao2 + ";" +restricao3 + ";" +restricao4);
        	item.setMulta(((BigDecimal) veiculo[32]).doubleValue());
        	item.setIpva(((BigDecimal) veiculo[30]).doubleValue());
        	item.setDpvat(((BigDecimal) veiculo[31]).doubleValue());
        	item.setTotalDebitosDetran(((BigDecimal) veiculo[29]).doubleValue());
        	item.setTiporestricao(String.valueOf(veiculo[23]));
        	item.setAlienacao(String.valueOf(veiculo[24]));
        	item.setDigitador(FacesUtil.loggedUser().getName());
        	Integer tipoPessoa = 2;
        	if(veiculo[36] instanceof String){
        		if(((String) veiculo[36]).equalsIgnoreCase("Pessoa Física (CPF)")){
        			tipoPessoa = 1;
        		}
        	}else if(veiculo[36] instanceof Integer){
        		if(((Integer) veiculo[36]) == 1){
        			tipoPessoa = 1;
        		}
        	}else if(veiculo[36] instanceof BigDecimal){
        		if(((BigDecimal) veiculo[36]).intValue() == 1){
        			tipoPessoa = 1;
        		}
        	}
        	if(tipoPessoa == 1){
        		String cnpj = item.getCnpj();        		        		    
        		if(cnpj.length() > 11){
        			String cpf= cnpj.substring(Math.max(0, cnpj.length() - 11));
        			item.setCnpj(cpf);
        		}        		
        	}        	
        	this.instance = item;
    	}catch(Exception e){
    		e.printStackTrace();
    		FacesContext.getCurrentInstance().validationFailed();
    		addMenssage(FacesMessage.SEVERITY_ERROR, "Validação", "Placa já cadastrada na base!");
    	}
    	
    }
    
    public StreamedContent getExportarExcel(){    	
    	List<ItemLeilao> itens = repository.getVeiculosDeCategoria(categoria.getId());
        try(InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/modelos/temp.xlsx")) {
        	Context context = new Context();
            context.putVar("veiculos", itens);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            JxlsHelper.getInstance().processTemplate(is,result, context);
            return new DefaultStreamedContent(new ByteArrayInputStream(result.toByteArray()), "application/xls", categoria.getDescricao() + ".xls");
        } catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Repository getRepository() {
        return this.repository;
    }

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}    
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}    

}
