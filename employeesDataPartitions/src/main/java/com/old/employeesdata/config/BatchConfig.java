package com.old.employeesdata.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.old.employeesdata.entity.Employee;
import com.old.employeesdata.partition.EmployeePartitioner;
import com.old.employeesdata.repository.EmployeeRepository;
import com.old.employeesdata.writer.EmployeeWriter;

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
	@Autowired
	private EmployeeWriter employeeWriter;
	
	
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
		delimitedLineTokenizer.setNames("employeeid","first_name","last_name","location");
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
	public EmployeePartitioner partitioner() {
		return new EmployeePartitioner();
	}
	
	@Bean PartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(4);
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(slaveStep());
		return handler;
	}
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory
				.get("employeeJob")
				.incrementer(new RunIdIncrementer())
				.start(masterStep())
				.build();
	}
	
	@Bean 
	public Step masterStep() {
		return stepBuilderFactory
				.get("employeeMasterStep")
				.partitioner(slaveStep().getName(), partitioner())
				.partitionHandler(partitionHandler())
				.build();
	}
	
	@Bean
	public Step slaveStep() {
		return stepBuilderFactory
				.get("employeeSlaveStep")
				.<Employee,Employee>chunk(250)
				.reader(fileReader())
				.processor(itemProcessor())
				.writer(employeeWriter)
				.build();
		
	}
	

	
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(8);
		executor.setCorePoolSize(8);
		executor.setQueueCapacity(8);
		return executor;
	}
	

}
