package com.sg.netex.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class ThymeleafConfig implements ApplicationContextAware,WebMvcConfigurer {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext= applicationContext;
	}
	
	  @Bean
	  public ViewResolver viewResolver() {
	    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
	    resolver.setTemplateEngine(templateEngine());
	    resolver.setCharacterEncoding("UTF-8");
	    return resolver;
	  }

	  @Bean
	  public ISpringTemplateEngine templateEngine() {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.setEnableSpringELCompiler(true);
	    engine.setTemplateResolver(templateResolver());
	    return engine;
	  }
	  
	  private ITemplateResolver templateResolver() {
		    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		    resolver.setApplicationContext(applicationContext);
		    resolver.setPrefix("/WEB-INF/templates/");
		    resolver.setSuffix(".html");
		    resolver.setTemplateMode(TemplateMode.HTML);
		    return resolver;
		  }
	  
	  @Bean
	  @Description("Spring Message Resolver")
	  public ResourceBundleMessageSource messageSource() {
	  	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	  	messageSource.setBasename("messages");
	  	messageSource.setDefaultEncoding("UTF8");
	  	return messageSource;
	  }

	  @Bean(name = "textTemplateEngine")
	    public TemplateEngine textTemplateEngine() {
	        TemplateEngine templateEngine = new TemplateEngine();
	        templateEngine.addTemplateResolver(textTemplateResolver());
	        return templateEngine;
	    }

	    private ITemplateResolver textTemplateResolver() {
	        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	        templateResolver.setPrefix("/templates/text/");
	        templateResolver.setSuffix(".java");
	        templateResolver.setTemplateMode(TemplateMode.TEXT);
	        templateResolver.setCharacterEncoding("UTF8");
	        templateResolver.setCheckExistence(true);
	        templateResolver.setCacheable(false);
	        return templateResolver;
	    }
	  
}

