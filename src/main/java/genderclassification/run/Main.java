package genderclassification.run;

import genderclassification.domain.CategoryOrder;
import genderclassification.pipeline.AbstractPipelineAdapter;
import genderclassification.pipeline.MemPipelineAdapter;
import genderclassification.utils.DataParser;

import java.io.IOException;

import org.apache.crunch.Pipeline;

public class Main {
    private static final MemPipelineAdapter pipelineAdapter = MemPipelineAdapter.getInstance();
	private static final int SEED = 57138921;

	public static void main(String[] args) throws IOException {
        CategoryOrder.setCategories(DataParser.parseCategories());
        
        CrossValidation crossValidation = new CrossValidation(SEED);
        double score = crossValidation.performCrossValidation(new NaiveBayesianClassification());
        System.out.println("Score: " + score);
    }

	public static Pipeline getPipeline() {
		return pipelineAdapter.getPipeline();
	}

	public static AbstractPipelineAdapter getAdapter() {
		return pipelineAdapter;
	}
}
