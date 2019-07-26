package com.openjava.datatag.utils.jdbc.dataprovider.util;

import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.config.AggConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.config.DimensionConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.result.ColumnIndex;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by zyong on 2017/9/28.
 */
public class DPCommonUtils {

    public static AggregateResult transform2AggResult(AggConfig config, List<String[]> list) throws Exception {
        // recreate a dimension stream
        Stream<DimensionConfig> dimStream = Stream.concat(config.getColumns().stream(), config.getRows().stream());
        List<ColumnIndex> dimensionList = dimStream.map(ColumnIndex::fromDimensionConfig).collect(Collectors.toList());
        int dimSize = dimensionList.size();
        dimensionList.addAll(config.getValues().stream().map(ColumnIndex::fromValueConfig).collect(Collectors.toList()));
        IntStream.range(0, dimensionList.size()).forEach(j -> dimensionList.get(j).setIndex(j));
        list.forEach(row -> {
            IntStream.range(0, dimSize).forEach(i -> {
                if (row[i] == null) {
                    row[i] = DataProvider.NULL_STRING;
                }
            });
        });
        String[][] result = list.toArray(new String[][]{});
        return new AggregateResult(dimensionList, result);
    }

    public static AggregateResult transform2AggResult(List<String[]> list) throws Exception {
        String[][] result = list.toArray(new String[][]{});
        return new AggregateResult(result);
    }
}
