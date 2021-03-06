package genderclassification.pipeline;

import genderclassification.algorithm.naivebayesian.NaiveBayesianGenderModel;

import org.apache.crunch.Pipeline;
import org.apache.crunch.impl.mr.MRPipeline;

public class MRPipelineAdapter extends AbstractPipelineAdapter {
    private MRPipelineAdapter(final Pipeline pipeline) {
        super(pipeline);
    }

    protected boolean acceptFile(final String filename) {
        return filename.startsWith("part-");
    }

    public static MRPipelineAdapter getInstance() {
        return new MRPipelineAdapter(new MRPipeline(NaiveBayesianGenderModel.class));
    }
}
