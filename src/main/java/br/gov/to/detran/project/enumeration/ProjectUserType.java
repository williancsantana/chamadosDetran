package br.gov.to.detran.project.enumeration;

public enum ProjectUserType {
	EQUIPE("Equipe"),
	COORDENADOR("Coordenador");
	
	private String label;
	
	private ProjectUserType(String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }       
}
