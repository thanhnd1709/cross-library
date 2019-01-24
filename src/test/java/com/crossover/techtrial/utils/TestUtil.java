package com.crossover.techtrial.utils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.MediaType;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
 
public class TestUtil {
 
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

	public static boolean isEqualsList(List<TopMemberDTO> topMemberList, List<TopMemberDTO> returnMemberList) {
		return !topMemberList.retainAll(returnMemberList);
	}
}