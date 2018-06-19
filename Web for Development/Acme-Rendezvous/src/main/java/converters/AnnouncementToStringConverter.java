package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Announcement;

@Component
@Transactional
public class AnnouncementToStringConverter implements Converter<Announcement, String> {

	@Override
	public String convert(final Announcement param) {
		String result;

		if (param == null)
			result = null;
		else
			result = String.valueOf(param.getId());

		return result;
	}

}
