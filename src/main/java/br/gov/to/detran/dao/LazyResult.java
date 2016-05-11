/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.dao;

import br.gov.to.detran.domain.AbstractEntity;
import java.util.List;

/**
 *
 * @author Maycon
 * @param <T>
 */
public class LazyResult<T extends AbstractEntity> {
    private List<T> result;
    private Long count;

    public LazyResult(List<T> result, Long count) {
        this.result = result;
        this.count = count;
    }        

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
    
}
