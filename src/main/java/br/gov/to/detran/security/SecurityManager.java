package br.gov.to.detran.security;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

public class SecurityManager extends DefaultWebSecurityManager {    
    @Override
    public Subject createSubject(SubjectContext subjectContext) {
        Subject subject = super.createSubject(subjectContext);
        subject.getSession().setTimeout(10 * 60 * 100000);
        return subject;
    }

}
