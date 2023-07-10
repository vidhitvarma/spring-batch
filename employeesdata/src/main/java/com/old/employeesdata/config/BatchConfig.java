package com.old.employeesdata.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.old.employeesdata.entity.Employee;
import com.old.employeesdata.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory
				.get("empjob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory
				.get("empStep")
				.<Employee,Employee>chunk(50)
				.reader(fileReader())
				.processor(itemProcessor())
				.writer(dataWriter())
				.build();
	}
	
	@Bean
	public FlatFileItemReader<Employee> fileReader(){
		FlatFileItemReader<Employee> itemReader = new FlatFileItemReader();
		itemReader.setResource(new FileSystemResource("src/main/resources/employee.csv"));
		itemReader.setName("itemReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Employee> lineMapper() {
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("id","first_name","last_name","location");
		BeanWrapperFieldSetMapper< Employee> mapper = new BeanWrapperFieldSetMapper<Employee>();
		mapper.setTargetType(Employee.class);
		lineMapper.setFieldSetMapper(mapper);
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		return lineMapper;
	}
	
	@Bean
	public EmployeeProcessor itemProcessor() {
		return new EmployeeProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Employee> dataWriter(){
		RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<Employee>();
		writer.setRepository(employeeRepository);
		writer.setMethodName("save");
		return writer;
	}
}
