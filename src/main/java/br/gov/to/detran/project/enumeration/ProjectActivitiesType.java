package br.gov.to.detran.project.enumeration;

public enum ProjectActivitiesType {
	COMMENT_INSERTED("Comentário Inserido"),
	COMMENT_EDITED("Comentário Editado"),
	COMMENT_REMOVED("Comentário Removido"),
	TASK_CREATED("Tarefa Inserida"),
	TASK_UPDATED("Tarefa Completada"),
	TASK_REMOVED("Tarefa Removida"),	
	DOCUMENT_INSERTED("Documento Inserido"),	
	DOCUMENT_REMOVED("Documento Removido"),
	STATUS_UPDATED("Status Alterado"),
	USER_REMOVED("Usuário Removido"),
	USER_INSERTED("Usuário Inserido"),
	USER_COORDINATOR("Coordenador Alterado");
	
	private String label;
	
	private ProjectActivitiesType(String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
       
}
