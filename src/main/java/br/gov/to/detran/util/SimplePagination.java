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

/**
 * Classes responsavel para realizar a filtragem e paginação das tabelas do
 * primefaces.
 *
 * @author Maycon
 * @param <T>
 *
 */
public class SimplePagination<T extends AbstractEntity> {

    private final Repository<T> repository;
    private int currentPage;
    private int pageSize;
    private int rowCount;  
    private String filterValue;    
    private String[] filterColumns;
    private List<T> data;    

    public SimplePagination(Repository<T> repository) {
        this.repository = repository;
        this.currentPage = 0;
        this.pageSize = 60;         
    }
    
    public void update(){
        this.data = this.load();
    }
    
    public void nextPage(){
        if(isNextPage(this.currentPage + 1)){
            this.currentPage = this.currentPage + 1;
            this.data = this.load();
        }
    }
    
    public void previousPage(){
        if(isPreviousPage(this.currentPage - 1)){
            this.currentPage = this.currentPage - 1;
            this.data = this.load();
        }
    }
    
    public void actionFilter(String[] columns){
        this.currentPage = 0;
        this.pageSize = 3;
        this.filterColumns = columns;
        this.data = this.load();
    }

    public List<T> load() {
        HashMap<String, Object> filterHash = new HashMap<>();
        if(this.filterValue != null && !this.filterValue.isEmpty()){
            for(String filterColumn : filterColumns){
                filterHash.put(filterColumn, filterValue);
            }            
        }                
        LazyResult<T> lazyResult = this.repository.lazyLoad(this.currentPage * pageSize, pageSize, "id", "desc", filterHash);
        System.out.println(lazyResult.getCount().intValue());
        this.setRowCount(lazyResult.getCount().intValue());
        return lazyResult.getResult();
    }    
    
    public boolean isNextPage(Integer next){
        return next > 0 && next * pageSize <= this.getRowCount();
    }
    
    public boolean isPreviousPage(Integer previous){
        return previous >= 0 && previous * pageSize >= 0;
    }

    public int getRowCount() {
        return rowCount;
    }    

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<T> getData() {
        if(this.data == null){
            this.data = this.load();
        }
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }    

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String[] getFilterColumns() {
        return filterColumns;
    }

    public void setFilterColumns(String[] filterColumns) {
        this.filterColumns = filterColumns;
    }       
    
    public Integer getFirst(){        
        return (this.rowCount == 0 ? 0 : currentPage * pageSize + 1);
    }
    
    public Integer getLast(){
        Integer value = this.getFirst() + pageSize;
        if(this.rowCount < value){
            return this.rowCount;
        }
        return value - 1;
    }

}
