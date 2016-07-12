/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

/**
 *
 * @author maycon
 */
@Transactional
public class DetranNETRepository extends AbstractRepositoryDetranNET implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public Object[] pesquisarVeiculoPlaca(String placa) {
		Session session = this.getEm().unwrap(Session.class);
		ProcedureCall call = session
			    .createStoredProcedureCall("stp_Rev_Ws_DetranMovel");		    
		call.registerParameter(1, String.class, 
			    ParameterMode.IN).bindValue(placa);
		
		Output output = call.getOutputs().getCurrent();
		if (output.isResultSet()) {
		    List<Object[]> veiculo = ((ResultSetOutput) output).getResultList();
		    if(!veiculo.isEmpty()){
		    	return veiculo.get(0);
		    }
		}
		return null;
	}
	
    
}
