
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.security;

import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.repository.UserSecurityRepository;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * Repositorio de segurança
 * @author Maycon
 */
@Transactional
public class SecurityPersistenceRepository implements java.io.Serializable {

    private @Inject
    UserSecurityRepository userPersistenceRepository;     
    
    public SecurityPersistenceRepository() {
    }
    
    public boolean insertUser(UserSecurity user){
        try {
            user.setPassword(new Sha256Hash(user.getPassword()).toHex());
            this.userPersistenceRepository.insert(user);            
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Pesquisa um usuario ou tecnico no banco usando o cpf ou cnpj
     * @param field
     * @param value     
     * @return
     * @throws Exception 
     */
    public UserSecurity findUser(String field, String value) throws Exception {
        try {            
            return userPersistenceRepository.searchByField(field, value);
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return null;
    }
    
    /**
     * Faz uma atualização da senha.
     * @param password
     * @param token
     * @throws Exception 
     */
    public void updatePassword(String password, String token) throws Exception{        
    }

    /**
     * Cria um token de resete para o recuperar a senha
     * @param username
     * @throws Exception 
     */
    public void resetPassword(String username) throws Exception {        
    }

}
