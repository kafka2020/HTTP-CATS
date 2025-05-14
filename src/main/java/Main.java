import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;


import java.io.IOException;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, ParseException {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent("My Test Service")
                .setDefaultHeaders(List.of(new BasicHeader(HttpHeaders.ACCEPT, "application/json")))
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);

        CloseableHttpResponse response = httpClient.execute(request);

        List<Fact> facts = mapper.readValue(
                        EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8),
                        new TypeReference<List<Fact>>() {}
                ).stream()
                .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                .toList();
        facts.forEach(System.out::println);

        response.close();
        httpClient.close();
    }
}