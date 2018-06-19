package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ReplyRepository;

import domain.Reply;

@Component
@Transactional
public class StringToReplyConverter implements Converter<String, Reply>{
	

	@Autowired
	private ReplyRepository repository;
	
	@Override
	public Reply convert(String text) {
		Reply result;
		int id;		
		try {
			if(StringUtils.isEmpty(text)){
				result = null;
			}else{
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch(Throwable oops) {
			throw new IllegalArgumentException(oops);
		}		
		return result;
	}	
}
