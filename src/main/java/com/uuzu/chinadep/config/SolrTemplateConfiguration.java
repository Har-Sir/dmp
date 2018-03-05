/*
package com.uuzu.chinadep.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;


*/
/**
 * @author jiangll
 *
 *//*

@Configuration
@ConditionalOnClass(SolrTemplate.class)
public class SolrTemplateConfiguration {

    @Value("${spring.data.solr.zk-host}")
    private String zkHost;

    @Bean
    public SolrClient solrClient() {
        CloudSolrClient cloudSolrClient = new CloudSolrClient(zkHost);
        cloudSolrClient.setDefaultCollection("device_id_mapping_solr_second_new");
        return cloudSolrClient;
    }

    @Bean
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(solrClient());
    }

}
*/
