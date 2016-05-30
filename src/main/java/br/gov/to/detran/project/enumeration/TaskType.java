package br.gov.to.detran.project.enumeration;

public enum TaskType {
	NOVA("Nova Funcionalidade"),
	BUG("Bug (Problema/Error)"),
	MELHORIA("Melhoria"),
	OUTRA("Outro");
	
	private String label;
	
	private TaskType(String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
       
}
