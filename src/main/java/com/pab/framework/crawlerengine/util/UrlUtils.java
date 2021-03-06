package com.pab.framework.crawlerengine.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class UrlUtils {

    /**
     * 信任任何站点，实现https页面的正常访问
     */

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final void printUrl() throws IOException {
        trustEveryone();
        Connection connect = Jsoup.connect("http://www.iresearch.com.cn/report.shtml");
        Document document = connect.get();
        Elements elements = document.getElementsByTag("a");
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            System.out.println(elements.get(i).getElementsByAttribute("href"));
        }
    }

    public static int maxPage(String urlAddr) {
        Pattern pattern = Pattern.compile("\\d+}");
        Matcher matcher = pattern.matcher(urlAddr);
        if (matcher.find()) {
            String group = matcher.group();
            return Integer.parseInt(group.substring(0, group.length() - 1));
        }
        return -1;
    }

    public static StringBuffer getAhref(String str) {
        StringBuffer stringBufferResult = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (chr == '%') {
                StringBuffer stringTmp = new StringBuffer();
                stringTmp.append(str.charAt(i + 1)).append(str.charAt(i + 2));
                stringBufferResult.append((char) (Integer.valueOf(stringTmp.toString(), 16).intValue()));
                i = i + 2;
                continue;
            }
            stringBufferResult.append(chr);
        }
        return stringBufferResult;
    }

}
