
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Servicio;

@Component
@Transactional
public class ServicioToStringConverter implements Converter<Servicio, String> {

	@Override
	public String convert(final Servicio servicio) {
		String result;
		if (servicio == null)
			result = null;
		else
			result = String.valueOf(servicio.getId());
		return result;
	}
}
