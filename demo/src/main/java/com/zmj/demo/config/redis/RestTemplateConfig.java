//package com.zmj.demo.config.redis;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Lists;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.*;
//import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
//import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
//import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.http.converter.json.JsonbHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
//import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
//import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
//import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
//import org.springframework.http.converter.xml.SourceHttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ClassUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * restTemplate配置文件，接口请求
// */
//@Component
//public class RestTemplateConfig {
//
//    private static final boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", RestTemplate
//            .class.getClassLoader());
//    private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());
//    private static final boolean jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());
//    private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader());
//    private static final boolean jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", RestTemplate.class.getClassLoader());
//    private static final boolean jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", RestTemplate.class.getClassLoader());
//    private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class.getClassLoader());
//    private static final boolean jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", RestTemplate.class.getClassLoader());
//
//    // 启动的时候要注意，由于我们在服务中注入了RestTemplate，所以启动的时候需要实例化该类的一个实例
//    @Autowired
//    private RestTemplateBuilder builder;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
//    @Bean
//    public RestTemplate restTemplate() {
//
//        RestTemplate restTemplate = builder.build();
//
//        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(objectMapper);
//
//        //不加会出现异常
//        //Could not extract response: no suitable HttpMessageConverter found for response type [class ]
//
//        MediaType[] mediaTypes = new MediaType[]{
//                MediaType.APPLICATION_JSON,
//                MediaType.APPLICATION_OCTET_STREAM,
//
//                MediaType.APPLICATION_JSON_UTF8,
//                MediaType.TEXT_HTML,
//                MediaType.TEXT_PLAIN,
//                MediaType.TEXT_XML,
//                MediaType.APPLICATION_STREAM_JSON,
//                MediaType.APPLICATION_ATOM_XML,
//                MediaType.APPLICATION_FORM_URLENCODED,
//                MediaType.APPLICATION_PDF,
//        };
//
//        converter.setSupportedMediaTypes(Arrays.asList(mediaTypes));
//
//        //messageConverters.add(converter);
//        if (jackson2Present) {
//            messageConverters.add(converter);
//        } else if (gsonPresent) {
//            messageConverters.add(new GsonHttpMessageConverter());
//        } else if (jsonbPresent) {
//            messageConverters.add(new JsonbHttpMessageConverter());
//        }
//
//        messageConverters.add(new FormHttpMessageConverter());
//
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new ResourceHttpMessageConverter(false));
//        messageConverters.add(new SourceHttpMessageConverter());
//        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
//        if (romePresent) {
//            messageConverters.add(new AtomFeedHttpMessageConverter());
//            messageConverters.add(new RssChannelHttpMessageConverter());
//        }
//
//        if (jackson2XmlPresent) {
//            messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
//        } else if (jaxb2Present) {
//            messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
//        }
//
//
//        if (jackson2SmilePresent) {
//            messageConverters.add(new MappingJackson2SmileHttpMessageConverter());
//        }
//
//        if (jackson2CborPresent) {
//            messageConverters.add(new MappingJackson2CborHttpMessageConverter());
//        }
//
//        restTemplate.setMessageConverters(messageConverters);
//
//        return restTemplate;
//    }
//
//}
//
