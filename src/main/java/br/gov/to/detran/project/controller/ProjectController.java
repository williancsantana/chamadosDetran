/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.controller;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.to.detran.controller.BaseController;
import br.gov.to.detran.project.domain.Project;
import br.gov.to.detran.project.domain.ProjectComment;
import br.gov.to.detran.project.domain.ProjectDocument;
import br.gov.to.detran.project.domain.ProjectTask;
import br.gov.to.detran.project.enumeration.ProjectStatus;
import br.gov.to.detran.project.repository.ProjectCommentRepository;
import br.gov.to.detran.project.repository.ProjectDocumentRepository;
import br.gov.to.detran.project.repository.ProjectRepository;
import br.gov.to.detran.project.repository.ProjectTaskRepository;
import br.gov.to.detran.push.NotifySessions;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.UserSecurityRepository;
import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class ProjectController extends BaseController<Project> implements java.io.Serializable {

    private @Inject
    ProjectRepository repository;
    private @Inject
    ProjectCommentRepository projectCommentRepository;
    private @Inject
    ProjectDocumentRepository projectDocumentRepository;
    private @Inject
    ProjectTaskRepository projectTaskRepository;
    private @Inject
    UserSecurityRepository userSecurityRepository;
    private @Inject
    NotifySessions notifySessions;
              
    private ProjectComment projectComment;
    private ProjectDocument projectDocument;
    private ProjectTask projectTask;

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        Project Project = this.getFlash("projetoInstance");
        if (Project != null) {
            this.instance = Project;
        }        
    }

    public void checkProject() throws IOException {
        try {            
            if (this.instance == null || this.instance.getId() == 0) {
                this.instance = this.repository.getProject(Long.parseLong(id));
                if (this.instance == null) {
                    this.redirectWithMensagem("Projeto não foi encontrado!", FacesUtil.ERROR);
                }
            }                        
            this.instance.setComments(this.projectCommentRepository.getAllComments(this.instance.getId()));
            this.instance.setDocuments(this.projectDocumentRepository.getAllDocuments(this.instance.getId()));
            this.instance.setTasks(this.projectTaskRepository.getAllTasks(this.instance.getId()));            
            this.projectComment = new ProjectComment();
            this.projectDocument = new ProjectDocument();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            this.redirectWithMensagem("Projeto não foi encontrado!", FacesUtil.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.redirectWithMensagem("Projeto não foi encontrado!", FacesUtil.ERROR);
        }
    }       

    public void viewprojeto(Project projeto) {
        try {
            this.setFlash("projetoInstance", projeto);
            this.redirect("/projeto.jsf?id=" + projeto.getId());
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para o projeto");
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectWithMensagem(String title, String msg, FacesMessage.Severity s) throws IOException {
        addMenssage(s, title, msg);
        redirect("/index.jsf");
    }

    public void redirectWithMensagem(String title, FacesMessage.Severity s) throws IOException {
        addMenssage(s, "projeto", title);
        redirect("/index.jsf");
    }

    public void insert() {
        try {            
            this.criarprojeto(instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Validação");
        }
    }

    public void update() throws Exception {
        try {            
            this.repository.update(this.instance);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void removeAndRedirect(Project instance, String link) {
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(Project instance) {
        try {
            this.repository.remove(instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }       

    public void criarprojeto(Project instance) throws Exception {
    	
    	if(instance.getDescricao().trim().isEmpty() || instance.getDescricao().trim().length() < 30){
    		throw new Exception("É necessário informar uma descrição do projeto de no minimo 30 caracteres!");
    	}
        instance.setCreated(new Date());
        instance.setUpdated(new Date());        
        instance.setAutor(FacesUtil.loggedUser());
        instance.setStatus(ProjectStatus.ABERTO);        
        this.repository.insert(instance);               
        this.redirectWithMensagem("projeto", "O Projeto foi aberto com sucesso!", FacesUtil.INFO);
    }
    
    public void startProject(){
    	  this.updateProjectStatus(ProjectStatus.PROGRESSO);
    }
    
    public void updateProjectStatus(ProjectStatus newStatus){
    	try {    		  
  		  	this.instance.setStatus(newStatus);
            this.repository.update(instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Projeto Iniciado", "Atualização");
  	  } catch (NullPointerException ex) {
            ex.printStackTrace();          
            this.addMenssage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Error");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Error");
        }
    }

    public void commentProject() {
        try {
            if (Jsoup.parse(projectComment.getDescricao()).text().trim().isEmpty()) {
                throw new Exception("É necessário informar uma mensagem!");
            }            
            projectComment.setProject(this.instance);
            projectComment.setCreated(new Date());
            projectComment.setUpdated(new Date());
            projectComment.setUserSecurity(FacesUtil.loggedUser());
            projectCommentRepository.insert(projectComment);            
            projectComment = new ProjectComment();            
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }       
    
    public void updateComment() {
    	 try {
    		 if(projectComment == null){
    			 throw new Exception("Não foi possivel realizar a operação de atualização!");
    		 }
             if (Jsoup.parse(projectComment.getDescricao()).text().trim().isEmpty()) {
                 throw new Exception("É necessário informar uma mensagem!");
             }                         
             projectComment.setUpdated(new Date());             
             projectCommentRepository.update(projectComment);             
             projectComment = new ProjectComment();            
         } catch (Exception ex) {
             ex.printStackTrace();
             addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
         }
    }
    
    public void removeComment(ProjectComment comment){
    	try {
	   		 if(comment == null){
	   			 throw new Exception("Não foi possivel realizar a operação de atualização!");
	   		 }                                                            
            projectCommentRepository.remove(comment, true);                        
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }
    
    public void insertTask(){
    	try {
	   		 if(projectTask == null){
	   			 throw new Exception("Não foi possivel realizar a operação de atualização!");
	   		 }                                                            
           projectTask.setCreated(new Date());
           projectTask.setUpdated(new Date());
           projectTask.setProject(instance);
           projectTaskRepository.insert(projectTask);
       } catch (Exception ex) {
           ex.printStackTrace();
           addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
       }
    }
    
    public void completeTask(ProjectTask task){
    	try {
	   		 if(projectTask == null){
	   			 throw new Exception("Não foi possivel realizar a operação de atualização!");
	   		 }                                                            
          projectTask.setFinalizado(true);
          projectTask.setUpdated(new Date());          
          projectTaskRepository.update(projectTask);
      } catch (Exception ex) {
          ex.printStackTrace();
          addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
      }
    }
    
    public void removeTask(ProjectTask task){
    	try {
	   		 if(task == null){
	   			 throw new Exception("Não foi possivel realizar a operação de atualização!");
	   		 }                                                            
	   		task.setFinalizado(true);
	   		task.setUpdated(new Date());          
	   		projectTaskRepository.update(task);
     } catch (Exception ex) {
         ex.printStackTrace();
         addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
     }
    }

    public void validar() {

    }

    @Override
    public Repository getRepository() {
        return this.repository;
    }
    
    public void sendDocument(FileUploadEvent event) {
    	try{
    		UploadedFile file = event.getFile();
            if(file != null){
            	projectDocument.setProject(instance);
            	projectDocument.setCreated(new Date());
            	projectDocument.setMimeType(file.getContentType());
            	projectDocument.setName(file.getFileName());
            	projectDocument.setSize(file.getSize());
            	projectDocument.setDataByte(file.getContents());            	
            	projectDocumentRepository.insert(projectDocument);
            	projectDocument = new ProjectDocument();
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    		addMenssage(FacesUtil.ERROR, "Anexo", ex.getMessage());
    	}            	   
    }
    
    public void removeDocument(ProjectDocument document){
    	try{    		
            if(document != null){            	
            	projectDocumentRepository.remove(document, true);            	
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    		addMenssage(FacesUtil.ERROR, "Anexo", ex.getMessage());
    	}
    }    
        	
}
