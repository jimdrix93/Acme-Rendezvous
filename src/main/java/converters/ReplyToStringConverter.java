package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Reply;

@Component
@Transactional
public class ReplyToStringConverter implements Converter<Reply, String> {

	@Override
	public String convert(Reply param) {
		String result;
		if (param == null) {
			result = null;
		} else {
			result = String.valueOf(param.getId());
		}
		return result;
	}
}
