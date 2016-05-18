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
    
    public static DiaSemana getDiaSemana(int valor){
    	switch (valor) {
			case 1:
				return DiaSemana.SEG;	
			case 2:
				return DiaSemana.TER;	
			case 3:
				return DiaSemana.QUA;			
			case 4:
				return DiaSemana.QUI;			
			case 5:
				return DiaSemana.SEX;
		}
    	return DiaSemana.SEG;
    }
}
