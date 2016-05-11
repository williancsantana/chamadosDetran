/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.repository.Repository;
import java.util.HashMap;
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
public class LazyListEntities<T extends AbstractEntity> extends LazyDataModel<T>{
    private final Repository<T> repository;  
    
    private Map<String, Object> staticFilters;
    private String sortField;
    private SortOrder sortOrder;
    
    private Map<String, Object> fk_entities;
    
    public LazyListEntities(Repository<T> repository, Map<String, Object> fk_entities) {
        this.repository = repository;
        staticFilters = new HashMap<>();        
        this.fk_entities = fk_entities;  
    }
    
    public void addFilter(String field, Object value){
         if(staticFilters != null){
             staticFilters.put(field, value);
         }
    }
    
    public void clearFields(){
        if(staticFilters != null){
            staticFilters.clear();
        }
    }
    public void clearSort(){
        sortField = null;
        sortOrder = SortOrder.UNSORTED;
    }

    @Override
    public Long getRowKey(T object) {
        return object.getId();
    }        

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {        
        filters.putAll(staticFilters);        
        LazyResult<T> lazyResult = this.repository.lazyLoad(fk_entities, first, pageSize, sortField, sortOrder.name(), filters);
        this.setRowCount(lazyResult.getCount().intValue());
        return lazyResult.getResult();
    }    
    
    
        
}
