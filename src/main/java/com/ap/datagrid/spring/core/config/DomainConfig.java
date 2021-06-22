package com.ap.datagrid.spring.core.config;


import java.lang.reflect.Method;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.SaslQop;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
//import org.infinispan.configuration.global.GlobalConfigurationBuilder;
//import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.infinispan.spring.provider.SpringRemoteCacheManager;
import org.infinispan.spring.provider.SpringRemoteCacheManagerFactoryBean;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ap.datagrid.spring.core.client.CachedClientGetter;
import com.ap.datagrid.spring.core.client.ClientCache;
import com.ap.datagrid.spring.core.client.ClientGetter;

/**
 * Spring configuration for domain objects.
 * 
 * @author anand.prakash
 * 
 */
@Configuration
@EnableCaching
public class DomainConfig {


    @Bean
    public ClientGetter clientGetter() {
        return new ClientGetter();
    }

    @Bean
    public com.ap.datagrid.spring.core.client.CachedClientGetter cachedClientGetter(ClientGetter clientGetter) {
        return new CachedClientGetter(clientGetter);
    }

   @Bean
    public ClientCache cacheHandler(CacheManager cacheManager) {
	    
        return new ClientCache(cacheManager());
        
    }
    
    @Bean
    public RemoteCacheManager cacheManager(){
    	System.out.println("Inside DomainConfig.cacheManager()---AP");

    	String host ="example-infinispan";
    	int port = 11222;
    	

	    
    	ConfigurationBuilder builder = new ConfigurationBuilder();
	    builder.addServer()
	    	.host(host).port(port)
	    	// Use BASIC client intelligence.
	    	.clientIntelligence(ClientIntelligence.BASIC)
	    	.security()
	            // Authentication
	            .authentication().enable()
	            .username("operator")
	            .password("supersecretoperatorpassword")
	            .serverName("infinispan")
	            .saslMechanism("DIGEST-MD5")
	            .saslQop(SaslQop.AUTH)
	            // Encryption
	            .ssl()
	            .sniHostName("example-infinispan")
	            .trustStorePath("/var/run/secrets/kubernetes.io/serviceaccount/service-ca.crt")
	            ;
	   
	    builder.maxRetries(1).socketTimeout(20000).connectionTimeout(50000);
	    builder.tcpNoDelay(true);
	    builder.addJavaSerialWhiteList("com.ap.datagrid.spring.rest.model.*");
	    builder.marshaller(new JavaSerializationMarshaller());
	    
	    
	    System.out.println("======> Connecting to HOST->'"+host +"' and PORT->"+port );
	    return new RemoteCacheManager(builder.build(),true);
	   //return new SpringRemoteCacheManager(new RemoteCacheManager(builder.build(),true));
    }

    
    @Bean
    public KeyGenerator simpleKeyGenerator() {
      return new KeyGenerator() {
		@Override
		public Object generate(Object target, Method method, Object... params) {
		    StringBuilder sb = new StringBuilder();
		    sb.append(target.getClass().getName());
		    sb.append(method.getName());
		    for (Object obj : params) {
		      sb.append(obj.toString());
		    }
		    return sb.toString();
		  }
	};
    }
}
