package bogdanov.kafkadbtransferer.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageBuilder {

    public <T> String getMessageForList(String header, List<T> list) {
        StringBuilder sb = new StringBuilder(header);
        sb.append("{\n");
        list.forEach(obj -> sb.append('\t').append(obj).append('\n'));
        sb.append('}');
        return sb.toString();
    }

}
