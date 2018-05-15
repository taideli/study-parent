package com.tdl.study.hadoop.mapreduce;

import com.tdl.study.hadoop.TestCases;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MrTestCases extends TestCases {
    public MrTestCases() {
        super();
    }

    public void simple() throws Exception {
        int exitCode = ToolRunner.run(new ConfigurationPrinter(), new String[] {});
        System.out.println("exit code: " + exitCode);
    }

    class ConfigurationPrinter extends Configured implements Tool {

        @Override
        public int run(String[] args) throws Exception {
            Configuration conf = getConf();
            List<String> keys = new LinkedList<>();
            for (Map.Entry<String, String> entry : conf) {
                keys.add(entry.getKey());
            }
            keys.stream().sorted().forEachOrdered(key -> {
                System.out.printf("%s = %s\n", key, conf.get(key));
            });

            return 0;
        }
    }
}
