package br.gov.to.detran.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


@Entity
@Table(name = "tb_chamado_resposta_anexo")
public class TicketAttachmentReply extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "nome", nullable = false)
	private String name;

	@Column(name = "mime_type", nullable = false)
	private String mimeType;

	@Column(name = "tamanho", nullable = false)
	private Long size;

	@Lob
	@Column(name = "bytes", nullable = false)
	private byte[] dataByte;

	@JoinColumn(name = "fk_resposta_chamado")
	@ManyToOne
	private TicketReply respostaChamado;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public byte[] getDataByte() {
		return dataByte;
	}

	public void setDataByte(byte[] dataByte) {
		this.dataByte = dataByte;
	}

	public TicketReply getChamadoResposta() {
		return respostaChamado;
	}

	public void setChamadoResposta(TicketReply respostaChamado) {
		this.respostaChamado = respostaChamado;
	}

	public StreamedContent getDownloadFile() {
		InputStream stream = new ByteArrayInputStream(this.dataByte);
		return new DefaultStreamedContent(stream, this.mimeType, this.name);
	}
}
