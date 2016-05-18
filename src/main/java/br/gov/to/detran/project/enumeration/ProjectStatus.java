package br.gov.to.detran.project.enumeration;

public enum ProjectStatus {
	ABERTO("Aberto"),
	PROGRESSO("Em Progresso"),
	HOMOLOGACAO("Em Homologação"),
	PRODUCAO("Em Produção"),
	FINALIZADO("Finalizado"),
	CANCELADO("Cancelado"),
	PAUSADO("Pausado");
	
	private String label;
	
	private ProjectStatus(String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
       
}
