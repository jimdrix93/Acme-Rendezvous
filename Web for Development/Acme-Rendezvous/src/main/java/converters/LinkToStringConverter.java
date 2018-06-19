package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Link;

@Component
@Transactional
public class LinkToStringConverter implements Converter<Link, String> {

	@Override
	public String convert(Link param) {
		String result;
		if (param == null) {
			result = null;
		} else {
			result = String.valueOf(param.getId());
		}
		return result;
	}
}
