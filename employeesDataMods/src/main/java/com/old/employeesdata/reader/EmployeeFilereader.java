package com.old.employeesdata.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.old.employeesdata.entity.Employee;
import com.old.employeesdata.utils.EmployeeUtilClass;


@Component
public class EmployeeFilereader implements ItemReader<Employee>{
	
	
	@Bean
	public FlatFileItemReader<Employee> itemReader(){
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new ClassPathResource("employee.csv"));
		reader.setName(EmployeeUtilClass.readerName);
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	
	private LineMapper<Employee> lineMapper(){
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("employeeid","first_name","last_name","location");
		BeanWrapperFieldSetMapper<Employee> wrapper = new BeanWrapperFieldSetMapper<Employee>();
		wrapper.setTargetType(Employee.class);
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(wrapper);
		return lineMapper;
	}

	@Override
	public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		return null;
	}
	
}
