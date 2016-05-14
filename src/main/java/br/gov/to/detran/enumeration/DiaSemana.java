package br.gov.to.detran.enumeration;

public enum DiaSemana {
	SEG("Segunda-Feira"),
	TER("Terça-Feira"),
	QUA("Quarta-Feira"),
	QUI("Quinta-Feira"),
	SEX("Sexta-Feira");
	
	private String label;
	
	private DiaSemana(String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
