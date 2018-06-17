package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Reservation;

@Component
@Transactional
public class ReservationToStringConverter implements Converter<Reservation, String> {

	@Override
	public String convert(Reservation param) {
		String result;
		if (param == null) {
			result = null;
		} else {
			result = String.valueOf(param.getId());
		}
		return result;
	}
}
