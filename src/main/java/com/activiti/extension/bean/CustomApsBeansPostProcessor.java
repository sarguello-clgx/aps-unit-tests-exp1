package com.activiti.extension.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomApsBeansPostProcessor implements BeanPostProcessor {

    @Autowired
    CustomApsRestCallBean customApsRestCallBean;

        public CustomApsBeansPostProcessor(CustomApsRestCallBean customRestCallBean) {
        this.customApsRestCallBean = customRestCallBean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("activiti_restCallDelegate")) {
            log.info("Replacing activiti_restCallDelegate bean with custom BpmnError-enable implementation");
            return customApsRestCallBean;
        } else {
            return bean;
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
}
