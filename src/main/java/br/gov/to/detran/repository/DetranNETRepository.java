/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import br.gov.to.detran.domain.view.ViewOperadorDetrannet;

/**
 *
 * @author maycon
 */
@Transactional
public class DetranNETRepository extends AbstractRepositoryDetranNET implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public Object[] pesquisarVeiculoChassi(String chassi) {
		Session session = this.getEm().unwrap(Session.class);
		ProcedureCall call = session
			    .createStoredProcedureCall("stp_Rev_Ws_DetranMovel");		    
		call.registerParameter(2, String.class, 
			    ParameterMode.IN).bindValue(chassi);
		
		Output output = call.getOutputs().getCurrent();
		if (output.isResultSet()) {		    
			List<Object[]> veiculo = ((ResultSetOutput) output).getResultList();
		    if(!veiculo.isEmpty()){
		    	return veiculo.get(0);
		    }
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] pesquisarVeiculoPlacaVistoria(String p) {
		Session session = this.getEm().unwrap(Session.class);
		ProcedureCall call = session
			    .createStoredProcedureCall("stp_Rev_Vist_Ws_ConsultaVeiculo");		    
		call.registerParameter(1, String.class, 
			    ParameterMode.IN).bindValue(p);
		
		Output output = call.getOutputs().getCurrent();
		if (output.isResultSet()) {		    
			List<Object[]> veiculo = ((ResultSetOutput) output).getResultList();
		    if(!veiculo.isEmpty()){
		    	if(veiculo.get(0) instanceof Object[]){
		    		return veiculo.get(0);
		    	}		    	
		    	try{
		    		System.out.println(veiculo.get(0));
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}		    	
		    }
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ViewOperadorDetrannet consultarOperador(String cpf){
		Session session = this.getEm().unwrap(Session.class);
		Criteria criteria = session.createCriteria(ViewOperadorDetrannet.class);
		criteria.add(Restrictions.eq("cpf", cpf));		
		List<ViewOperadorDetrannet> lista = criteria.list();
		if(lista.isEmpty()){
			return null;
		}
		ViewOperadorDetrannet operador = lista.get(0);
		operador.setPerfis(new ArrayList<String>());
		for(ViewOperadorDetrannet op : lista){
			operador.getPerfis().add(op.getPerfil());
		}		
		return operador;
	}

	
	
    
}
