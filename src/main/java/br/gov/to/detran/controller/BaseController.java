/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.util.LazyList;
import br.gov.to.detran.util.SimplePagination;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Maycon
 * @param <T>
 */
public abstract class BaseController<T extends AbstractEntity> {

    protected String id;
    protected boolean managed = false;
    protected boolean view = false;
    protected T instance;
    protected LazyList list;
    protected List<Object> columns;    
    protected SimplePagination<T> pagination;

    public abstract Repository getRepository();

    public void postContruct() {
        this.clear();
        this.list = new LazyList(this.getRepository());        
        this.pagination = new SimplePagination<>(this.getRepository());
    }

    public void loadInstance() {
        if (id != null && !id.trim().isEmpty()) {
            try {
                this.instance = (T) this.getRepository().getInstancePorId(Long.parseLong(id));
            } catch (Exception ex) {
                Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void printParameters() {
        Iterator<String> requestParameterNames = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterNames();
        for (Iterator teste = requestParameterNames; teste.hasNext();) {
            Object next = teste.next();
            System.out.println(next);
        }
    }    

    public LazyDataModel<T> getLazyModel() {
        return this.list;
    }

    public void clear() {
        try {
            this.instance = (T) this.getRepository().getPersitenceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SimplePagination<T> getPagination() {
        return pagination;
    }

    public void setPagination(SimplePagination<T> pagination) {
        this.pagination = pagination;
    }        

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean checkError(Throwable e){
        Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                System.out.println("Entramos");
                addMenssage(FacesMessage.SEVERITY_ERROR, "Error", "Não é possivel deletar um registro que esteja vinculado.");
                return true;
            }
            return false;
    }    
    
    public void click(T object, String key, String link){
        try {
            this.setFlash(key, object);
            this.redirect(link);
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para a pagina: " + link);
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setFlash(String key, T value){        
        this.getFlashContext().put(key, value);
    }
    
    public T getFlash(String key){
        return (T) this.getFlashContext().get(key);
    }
    
    public Object getFlashObject(String key){
        return this.getFlashContext().get(key);
    }
    
    protected Flash getFlashContext(){        
        return this.getExternalContext().getFlash();
    }
    
    public void redirect(String link) throws IOException{
        ExternalContext externalContext = this.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath() + link);
    }
    
    private ExternalContext getExternalContext(){
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public String getParameter(String param) {
        ServletRequest request = (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameter(param);
    }

    public void addMenssage(FacesMessage.Severity tipo, String escopo, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, new FacesMessage(tipo, escopo, msg));
    }

}
