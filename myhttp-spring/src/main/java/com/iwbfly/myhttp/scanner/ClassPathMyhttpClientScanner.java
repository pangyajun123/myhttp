package com.iwbfly.myhttp.scanner;

import com.iwbfly.myhttp.annotation.MyhttpClient;
import com.iwbfly.myhttp.beans.MyhttpClientFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 15:24
 **/
public class ClassPathMyhttpClientScanner extends ClassPathBeanDefinitionScanner {

    private static Class[] CLEINT_ANNOTATION = new Class[]{
            MyhttpClient.class
    };

    public ClassPathMyhttpClientScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerDefaultFilters(){
        for (Class clas: CLEINT_ANNOTATION){
            AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(clas);
            addIncludeFilter(annotationTypeFilter);
        }
    }
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
            isCandidate = true;
        }
        return isCandidate;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();

            // verify annotated class is an interface
            AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
            String beanClassName = abstractBeanDefinition.getBeanClassName();
            abstractBeanDefinition.setBeanClass(MyhttpClientFactoryBean.class);
            abstractBeanDefinition.getPropertyValues().add("type",beanClassName);
            logger.info("[Myhttp] Created Myhttp Client Bean with name '" + beanDefinitionHolder.getBeanName()
                    + "' and Proxy of '" + beanClassName + "' client interface");
        }
        return beanDefinitionHolders;
    }
}
