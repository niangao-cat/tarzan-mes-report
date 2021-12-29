package com.ruike.hme.app.assembler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.ruike.hme.domain.vo.HmeCosFunctionVO8;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HmeCosFunctionMergeStrategy extends AbstractMergeStrategy {

    private Map<String, List<HmeCosFunctionVO8>> strategyMap;
    private Sheet sheet;

    public HmeCosFunctionMergeStrategy(Map<String, List<HmeCosFunctionVO8>> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
        this.sheet = sheet;
        //如果没有标题，只有表头的话，这里的 cell.getRowIndex() == 1
        if (cell.getRowIndex() == 2 && cell.getColumnIndex() == 0) {
            /**
             * 保证每个cell被合并一次，如果不加上面的判断，因为是一个cell一个cell操作的，
             * 例如合并A2:A3,当cell为A2时，合并A2,A3，但是当cell为A3时，又是合并A2,A3，
             * 但此时A2,A3已经是合并的单元格了
             */
            for (Map.Entry<String, List<HmeCosFunctionVO8>> entry : strategyMap.entrySet()) {
                Integer columnIndex = Integer.valueOf(entry.getKey());
                entry.getValue().forEach(rowRange -> {
                    //添加一个合并请求
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(rowRange.getStart(),
                            rowRange.getEnd(), columnIndex, columnIndex));
                });
            }
        }
    }


    public static Map<String, List<HmeCosFunctionVO8>> addAnnualMerStrategy(List<List<Object>> dataList) {
        Map<String, List<HmeCosFunctionVO8>> strategyMap = new HashMap<>();
        String preDate = null;
        for (int i = 0; i < dataList.size(); i++) {
            String curDate = dataList.get(i).get(0).toString();
            //如果日期一样，将日期合并（真正开发中一般不会通过日期这样字段，而是通过一些关联的唯一值，比如父id）
            if (preDate != null) {
                if (curDate.equals(preDate)){    // 日期相同则合并第一列
//                    BizMergeStrategy.fillStrategyMap(strategyMap, "0", i+1);
                    //如果没有标题，只有表头的话，这里为 BizMergeStrategy.fillStrategyMap(strategyMap, "1", i);
                    HmeCosFunctionMergeStrategy.fillStrategyMap(strategyMap, "0", i+1);
                }
            }
            preDate = curDate;
        }
        return strategyMap;
    }
    /**
     * @description: 新增或修改合并策略map
     * @author

     * @param strategyMap
     * @param key
     * @param index
     * @since 2020/11/17 17:32
     * @Modified By:
     * @return
     */
    private static void fillStrategyMap(Map<String, List<HmeCosFunctionVO8>> strategyMap, String key, int index){
        List<HmeCosFunctionVO8> HmeCosFunctionVO8List = strategyMap.get(key) == null ? new ArrayList<>() : strategyMap.get(key);
        boolean flag = false;
        for (HmeCosFunctionVO8 dto : HmeCosFunctionVO8List) {
            //分段list中是否有end索引是上一行索引的，如果有，则索引+1
            if (dto.getEnd() == index) {
                dto.setEnd(index + 1);
                flag = true;
            }
        }
        //如果没有，则新增分段
        if (!flag) {
            HmeCosFunctionVO8List.add(new HmeCosFunctionVO8(index, index + 1));
        }
        strategyMap.put(key, HmeCosFunctionVO8List);
    }

    /**
     * @description: 表格样式
     * @author

     * @since 2020/11/20 9:40
     * @Modified By:
     * @return
     */
    public static HorizontalCellStyleStrategy CellStyleStrategy(){
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }
}
