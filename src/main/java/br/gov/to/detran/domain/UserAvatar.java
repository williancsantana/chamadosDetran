package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user_avatar")
public class UserAvatar extends AbstractEntity{
	@Column(name = "mime_type", nullable = false)
    private String mimeType;    
    
    @Lob
    @Column(name = "bytes", nullable = false)
    private byte[] dataByte;          
    
    @JoinColumn(name = "fk_user", nullable = true)    
    @OneToOne
    private UserSecurity user;

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getDataByte() {
		return dataByte;
	}

	public void setDataByte(byte[] dataByte) {
		this.dataByte = dataByte;
	}

	public UserSecurity getUser() {
		return user;
	}

	public void setUser(UserSecurity user) {
		this.user = user;
	}
	
}
