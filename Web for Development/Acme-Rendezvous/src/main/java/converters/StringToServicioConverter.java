
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ServicioRepository;
import domain.Servicio;

@Component
@Transactional
public class StringToServicioConverter implements Converter<String, Servicio> {

	@Autowired
	private ServicioRepository	servicioRepository;


	@Override
	public Servicio convert(final String str) {
		Servicio result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.servicioRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
