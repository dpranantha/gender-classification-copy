package genderclassification.utils;

import java.io.File;

import org.apache.crunch.MapFn;
import org.apache.crunch.PCollection;
import org.apache.crunch.PTable;
import org.apache.crunch.Pair;
import org.apache.crunch.Pipeline;

import com.google.common.base.Preconditions;

public class DataParser {
    public static final File OUTPUT_FOLDER = new File("output/");
    public static final String OUTPUT_FOLDER_CLASSIFY = "output/classify/";
    public static final File OUTPUT_FOLDER_MODEL = new File("output/model/");

    private static final String INPUT_FILE_USER_PRODUCT = "input/new_userId_productId.txt";
    private static final String INPUT_FILE_USER_GENDER = "input/new_userId_gender.txt";
    private static final String INPUT_FILE_PRODUCT_CATEGORY = "input/new_productId_category.txt";

    static {
        {
            Preconditions.checkArgument(new File(DataParser.INPUT_FILE_USER_PRODUCT).exists());
            Preconditions.checkArgument(new File(DataParser.INPUT_FILE_USER_GENDER).exists());
            Preconditions.checkArgument(new File(DataParser.INPUT_FILE_PRODUCT_CATEGORY).exists());
        }
    }

    public static final PCollection<String> userProductData(final Pipeline pipeline) {
        return pipeline.readTextFile(INPUT_FILE_USER_PRODUCT);
    };

    public static final PCollection<String> userGenderData(final Pipeline pipeline) {
        return pipeline.readTextFile(INPUT_FILE_USER_GENDER);
    };

    public static final PCollection<String> productCategoryData(final Pipeline pipeline) {
        return pipeline.readTextFile(INPUT_FILE_PRODUCT_CATEGORY);
    }

    public static final PCollection<String> classifiedUsers(final Pipeline pipeline) {
        try {
            return pipeline.readTextFile(OUTPUT_FOLDER_CLASSIFY);
        } catch (Exception e) {
            return pipeline.emptyPCollection(DataTypes.STRING_TYPE);
        }
    }

    public final static PTable<String, String> productUser(final PCollection<String> userProductLines) {
        // (P,U)
        return userProductLines.parallelDo(new MapFn<String, Pair<String, String>>() {
            private static final long serialVersionUID = 5368118058771709696L;

            @Override
            public Pair<String, String> map(final String line) {
                final String[] values = line.split("\t");
                final String userId = values[0];
                final String productId = values[1];
                return new Pair<String, String>(productId, userId);
            }
        }, DataTypes.STRING_TO_STRING_TABLE_TYPE);
    }

    public final static PTable<String, String> productCategory(final PCollection<String> productCategoryLines) {
        // (P,C)
        return productCategoryLines.parallelDo(new MapFn<String, Pair<String, String>>() {
            private static final long serialVersionUID = 8685387020655952971L;

            @Override
            public Pair<String, String> map(final String line) {
                final String[] values = line.split("\t");
                final String productId = values[0];
                final String category = values[1];
                return new Pair<String, String>(productId, category);
            }
        }, DataTypes.STRING_TO_STRING_TABLE_TYPE);
    }

    public final static PTable<String, String> userGender(final PCollection<String> userGenderLines) {
        // (U,G)
        return userGenderLines.parallelDo(new MapFn<String, Pair<String, String>>() {
            private static final long serialVersionUID = 8685387120655952971L;

            @Override
            public Pair<String, String> map(final String line) {
                final String[] values = line.split("\t");
                final String userId = values[0];
                final String genderProbabilities = values[1];
                return new Pair<String, String>(userId, genderProbabilities);
            }
        }, DataTypes.STRING_TO_STRING_TABLE_TYPE);
    }

    public final static PTable<String, String> userProduct(final PCollection<String> userProductLines) {
        // (U,P)
        return userProductLines.parallelDo(new MapFn<String, Pair<String, String>>() {
            private static final long serialVersionUID = 4431093387533962416L;

            @Override
            public Pair<String, String> map(final String line) {
                final String[] values = line.split("\t");
                final String userId = values[0];
                final String productId = values[1];
                return new Pair<String, String>(userId, productId);
            }
        }, DataTypes.STRING_TO_STRING_TABLE_TYPE);
    }

    public static PTable<String, String> classifiedUserGender(final PCollection<String> classifiedUserLines) {
        // (U,G)
        return classifiedUserLines.parallelDo(new MapFn<String, Pair<String, String>>() {
            private static final long serialVersionUID = 532879173812973289L;

            @Override
            public Pair<String, String> map(final String line) {
                final String[] values = line.split("\t");
                final String userId = values[0];
                final String probabilities = values[1];
                return new Pair<String, String>(userId, probabilities);
            }
        }, DataTypes.STRING_TO_STRING_TABLE_TYPE);
    }
}