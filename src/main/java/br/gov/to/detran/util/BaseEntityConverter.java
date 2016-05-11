/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;


import br.gov.to.detran.domain.AbstractEntity;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor geral para as entidades.
 * converter="baseEntityConverter"
 */
@FacesConverter(value = "baseEntityConverter", forClass = AbstractEntity.class)
public class BaseEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {        
        if (value != null) {                        
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {        
        if (value != null && !"".equals(value)) {
            AbstractEntity entity = (AbstractEntity) value;
            this.addAttribute(component, entity);
            Long codigo = entity.getId();
            if (codigo != null) {
                return String.valueOf(codigo);
            }
        }
        if(value instanceof AbstractEntity){
            return "-1";
        }
        return (String) value;
    }

    protected void addAttribute(UIComponent component, AbstractEntity o) {        
        if(o != null && o.getId() != null){            
            String key = o.getId().toString();
            this.getAttributesFrom(component).put(key, o);
        }        
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
}
