package com.iwbfly.myhttp;

import com.iwbfly.myhttp.register.MyhttpClientsRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyhttpClientsRegister.class)
public @interface EnableMyhttpClients {

    String[] value() default {};
    String[] basePackages() default {};
    Class<?> [] basePackageClasses() default {};
}
