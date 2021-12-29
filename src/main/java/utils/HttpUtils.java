package utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @ClassName HttpUtils
 * @Description 封装Http工具类，把traceId加入头中，带到下一个服务。
 * @Author lkj
 * @Date 2020/12/25
 */
@Slf4j
public class HttpUtils {

    /**
     * <strong>Title : get</strong><br/>
     * <strong>Description : 发送请求工具 </strong><br/>
     * <strong>Create on : 2020/12/28 上午10:30</strong><br/>
     *
     * @param url
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    public static String get(String url, Map<String, Object> param) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("traceId", MDC.get("traceId"));
        param.forEach((k, v) -> {
            headers.add(k, String.valueOf(v));
        });
        URI uri = new URI(url);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            log.info("send http request success");
        }
        return exchange.getBody();
    }

}
