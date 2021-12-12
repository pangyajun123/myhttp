package com.iwbfly.myhttp.register;

import com.iwbfly.myhttp.EnableMyhttpClients;
import com.iwbfly.myhttp.scanner.ClassPathMyhttpClientScanner;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @auther: pangyajun
 * @create: 2021/10/12 11:18
 **/
public class MyhttpClientsRegister implements ImportBeanDefinitionRegistrar {


    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerMyhttpClients(metadata, registry);
    }

    public void registerMyhttpClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathMyhttpClientScanner scanner = this.getScanner(registry);
        Set<String> basePackages;
        basePackages = getBasePackages(metadata);
        scanner.doScan(StringUtils.toStringArray(basePackages));

    }

    protected ClassPathMyhttpClientScanner getScanner(BeanDefinitionRegistry registry) {
        return new ClassPathMyhttpClientScanner(registry);
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableMyhttpClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }
}
