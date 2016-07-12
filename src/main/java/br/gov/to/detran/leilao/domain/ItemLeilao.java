package br.gov.to.detran.leilao.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.to.detran.domain.AbstractEntity;

@Entity
@Table(name="tb_item_leilao", schema = "leilao")
public class ItemLeilao extends AbstractEntity{	
	private static final long serialVersionUID = 1L;
	private Integer processo;	
	private String regiao;
	private Date dataApreensao;
	private String nAuto;
	private String patio;
	private String proprietario;
	@Enumerated(EnumType.STRING)	
	private Situacao situacao;
	private String cnpj;
	private String placa;
	private String renavam;
	private String documento;
	private String uf;
	private String ar;
	private String comentario;
	private Boolean vistoria;
	private String chassi;
	private String motor;
	private String marcaModelo;
	private String cor;
	private String anoModelo;
	private String endereco;
	private String cep;
	private String cidade;
	private String uf2;
	private String restricao;
	private String box;
	private String etiqueta;
	private String digitador;
	private String nLeilao;
	private String carac;
	private String avalicao;
	private String detran;
	private Double multa;
	private Double ipva;
	private Double dpvat;
	private Double guincho;
	private Integer diarias;
	private Double totalDebitosDetran;	
	private String alienacao;
	private String tiporestricao;	
	
	@ManyToOne
	@JoinColumn(name = "fk_categoria")
	private Categoria categoria;
	
	public Integer getProcesso() {
		return processo;
	}
	public void setProcesso(Integer processo) {
		this.processo = processo;
	}
	public String getRegiao() {
		return regiao;
	}
	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
	public Date getDataApreensao() {
		return dataApreensao;
	}
	public void setDataApreensao(Date dataApreensao) {
		this.dataApreensao = dataApreensao;
	}
	public String getnAuto() {
		return nAuto;
	}
	public void setnAuto(String nAuto) {
		this.nAuto = nAuto;
	}
	public String getPatio() {
		return patio;
	}
	public void setPatio(String patio) {
		this.patio = patio;
	}
	public String getProprietario() {
		return proprietario;
	}
	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}
	public Situacao getSituacao() {
		return situacao;
	}
	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getAr() {
		return ar;
	}
	public void setAr(String ar) {
		this.ar = ar;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Boolean getVistoria() {
		return vistoria;
	}
	public void setVistoria(Boolean vistoria) {
		this.vistoria = vistoria;
	}
	public String getChassi() {
		return chassi;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public String getMotor() {
		return motor;
	}
	public void setMotor(String motor) {
		this.motor = motor;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getAnoModelo() {
		return anoModelo;
	}
	public void setAnoModelo(String anoModelo) {
		this.anoModelo = anoModelo;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf2() {
		return uf2;
	}
	public void setUf2(String uf2) {
		this.uf2 = uf2;
	}
	public String getRestricao() {
		return restricao;
	}
	public void setRestricao(String restricao) {
		this.restricao = restricao;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getDigitador() {
		return digitador;
	}
	public void setDigitador(String digitador) {
		this.digitador = digitador;
	}
	public String getnLeilao() {
		return nLeilao;
	}
	public void setnLeilao(String nLeilao) {
		this.nLeilao = nLeilao;
	}
	public String getCarac() {
		return carac;
	}
	public void setCarac(String carac) {
		this.carac = carac;
	}
	public String getAvalicao() {
		return avalicao;
	}
	public void setAvalicao(String avalicao) {
		this.avalicao = avalicao;
	}
	public String getDetran() {
		return detran;
	}
	public void setDetran(String detran) {
		this.detran = detran;
	}	
	public Double getMulta() {
		return multa;
	}
	public void setMulta(Double multa) {
		this.multa = multa;
	}
	public Double getIpva() {
		return ipva;
	}
	public void setIpva(Double ipva) {
		this.ipva = ipva;
	}
	public Double getDpvat() {
		return dpvat;
	}
	public void setDpvat(Double dpvat) {
		this.dpvat = dpvat;
	}	
	public Integer getDiarias() {
		return diarias;
	}
	public void setDiarias(Integer diarias) {
		this.diarias = diarias;
	}
	public Double getTotalDebitosDetran() {
		return totalDebitosDetran;
	}
	public void setTotalDebitosDetran(Double totalDebitosDetran) {
		this.totalDebitosDetran = totalDebitosDetran;
	}	
	public String getRenavam() {
		return renavam;
	}
	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}
	public String getAlienacao() {
		return alienacao;
	}
	public void setAlienacao(String alienacao) {
		this.alienacao = alienacao;
	}
	public String getTiporestricao() {
		return tiporestricao;
	}
	public void setTiporestricao(String tiporestricao) {
		this.tiporestricao = tiporestricao;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Double getGuincho() {
		return guincho;
	}
	public void setGuincho(Double guincho) {
		this.guincho = guincho;
	}	
}
