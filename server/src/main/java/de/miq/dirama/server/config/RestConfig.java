package de.miq.dirama.server.config;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0],
                    new TrustManager[] { new DefaultTrustManager() },
                    new SecureRandom());
        } catch (Exception e) {

        }

        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setHostnameVerifier(new X509HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }

                    @Override
                    public void verify(String host, String[] cns,
                            String[] subjectAlts) throws SSLException {
                    }

                    @Override
                    public void verify(String host, X509Certificate cert)
                            throws SSLException {
                    }

                    @Override
                    public void verify(String host, SSLSocket ssl)
                            throws IOException {
                    }
                })
                .setSslcontext(ctx)
                .setDefaultSocketConfig(
                        SocketConfig.custom().setTcpNoDelay(true)
                                .setSoReuseAddress(true).setSoTimeout(30000)
                                .build()).build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                client);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);

        // Add the Jackson message converter
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());

        return restTemplate;
    }

    // public static void main(String[] args) throws Exception {
    // SSLContext ctx = null;
    // try {
    // ctx = SSLContext.getInstance("TLS");
    // ctx.init(new KeyManager[0],
    // new TrustManager[] { new DefaultTrustManager() },
    // new SecureRandom());
    // } catch (Exception e) {
    //
    // }
    //
    // CloseableHttpClient client = HttpClientBuilder
    // .create()
    // .setHostnameVerifier(new X509HostnameVerifier() {
    // @Override
    // public boolean verify(String hostname, SSLSession session) {
    // return true;
    // }
    //
    // @Override
    // public void verify(String host, String[] cns,
    // String[] subjectAlts) throws SSLException {
    // }
    //
    // @Override
    // public void verify(String host, X509Certificate cert)
    // throws SSLException {
    // }
    //
    // @Override
    // public void verify(String host, SSLSocket ssl)
    // throws IOException {
    // }
    // })
    // .setSslcontext(ctx)
    // .setDefaultSocketConfig(
    // SocketConfig.custom().setTcpNoDelay(true)
    // .setSoReuseAddress(true).setSoTimeout(5000)
    // .build()).build();
    //
    // HttpComponentsClientHttpRequestFactory factory = new
    // HttpComponentsClientHttpRequestFactory(
    // client);
    //
    // // Create a new RestTemplate instance
    // RestTemplate restTemplate = new RestTemplate(factory);
    //
    // System.out.println(restTemplate.getForObject(
    // "https://localhost:8080/srs/upload", String.class));
    // }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
