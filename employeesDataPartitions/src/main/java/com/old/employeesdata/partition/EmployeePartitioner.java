package com.old.employeesdata.partition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class EmployeePartitioner implements Partitioner{

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		
		Map<String, ExecutionContext> result = new HashMap<>();
		
		int minimumValue = 1;
		int maximumValue = 1000;
		int target = maximumValue - minimumValue / gridSize + 1; //grid = 4 (999 /4 ) = 249 + 1
		
		int currentNumber = 0;
		int start = minimumValue;
		int end = start + target - 1; // 1 - 1 + 250
		
		while(start <= end) {
			ExecutionContext context = new ExecutionContext();
			result.put("Partition Value " +currentNumber , context);
			
			if(end >= maximumValue) end = maximumValue;
			
			context.put("Start Value", minimumValue);
			context.put("End Value", end);
			start += target;
			end += target;
			currentNumber++;
		}
		System.out.println("Result" +result.toString());
		return result;
	}

}
