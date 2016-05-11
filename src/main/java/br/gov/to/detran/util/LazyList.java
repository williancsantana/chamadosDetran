/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.repository.Repository;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Classes responsavel para realizar a filtragem e paginação das tabelas do
 * primefaces.
 * @author Maycon
 * @param <T>
 *
 */
public class LazyList<T extends AbstractEntity> extends LazyDataModel<T>{
    private final Repository<T> repository;    
    
    public LazyList(Repository<T> repository) {
        this.repository = repository;
    }        

    @Override
    public Long getRowKey(T object) {
        return object.getId();
    }        

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {        
        LazyResult<T> lazyResult = this.repository.lazyLoad(first, pageSize, sortField, sortOrder.name(), filters);
        this.setRowCount(lazyResult.getCount().intValue());
        return lazyResult.getResult();
    }            
        
}
