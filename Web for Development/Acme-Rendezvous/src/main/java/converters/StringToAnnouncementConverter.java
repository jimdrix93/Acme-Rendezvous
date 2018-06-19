package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Announcement;
import repositories.AnnouncementRepository;

@Component
@Transactional
public class StringToAnnouncementConverter implements Converter<String, Announcement> {

	@Autowired
	AnnouncementRepository repository;

	@Override
	public Announcement convert(final String text) {
		Announcement result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.repository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
