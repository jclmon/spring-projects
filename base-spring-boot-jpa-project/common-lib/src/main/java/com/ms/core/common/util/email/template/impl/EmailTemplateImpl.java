package com.ms.core.common.util.email.template.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ms.core.common.util.email.template.EmailTemplate;

public class EmailTemplateImpl implements EmailTemplate {

	private String templateFile;
	private String[] params;

	public EmailTemplateImpl(String templateFile, String ... params) {
		this.templateFile = templateFile;
		this.params = params;
	}

	@Override
	public String getTextContent() throws Exception{
		
		Resource resource = new ClassPathResource(templateFile);
		try {
			byte[] bytes = new byte[1024];
			InputStream resourceInputStream = resource.getInputStream();
			StringBuilder str = new StringBuilder();
			int read = resourceInputStream.read(bytes);
			while(read != -1){
				str.append(new String(bytes));
				read = resourceInputStream.read(bytes);
			}
			String htmlString = str.toString();
			return String.format(htmlString, params);
		} catch (IOException e) {
			throw new Exception(String.format("%s Generating Error", templateFile), e);
		}
	}
}
