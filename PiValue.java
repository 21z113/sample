import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class PiValue {
    public static class PiMapper extends Mapper<Object, Text, Text, DoubleWritable> {
        private final static Text piKey = new Text("pi");
        private DoubleWritable piValue = new DoubleWritable();

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            // Calculate pi value (you can replace this with your own computation)
            double pi = Math.PI;
            piValue.set(pi);
            context.write(piKey, piValue);
        }
    }

    public static class PiReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        private DoubleWritable result = new DoubleWritable();

        public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            // Simply output the pi value
            for (DoubleWritable value : values) {
                result.set(value.get());
                context.write(key, result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: PiValue <input-path> <output-path>");
            System.exit(1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "pi value calculation");
        job.setJarByClass(PiValue.class);
        job.setMapperClass(PiMapper.class);
        job.setReducerClass(PiReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        // Set the number of reducers to 1 to create a single output file
        job.setNumReduceTasks(1);

        // Set the output format to TextOutputFormat
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0])); // Input path from command line
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // Output path from command line

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
