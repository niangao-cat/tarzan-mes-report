package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWipStocktakeDocDTO15;
import com.ruike.hme.app.service.HmeWipStocktakeDocService;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocInfoVO;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7;
import com.ruike.hme.infra.mapper.HmeWipStocktakeDocMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.common.domain.sys.MtException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * 在制盘点单应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Service
@Slf4j
public class HmeWipStocktakeDocServiceImpl implements HmeWipStocktakeDocService {

    @Autowired
    private HmeWipStocktakeDocMapper hmeWipStocktakeDocMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Page<HmeWipStocktakeDocVO7> releaseDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocVO7> resultPage = new Page<>();
        List<String> stocktakeIdList = dto.getStocktakeIdList();
        if (isEmpty(stocktakeIdList)) {
            return resultPage;
        }
        for (String stocktakeId:stocktakeIdList) {
            HmeWipStocktakeDocInfoVO hmeWipStocktakeDocInfoVO = hmeWipStocktakeDocMapper.wipStocktakeDocInfoQuery(tenantId, stocktakeId);
            //如果盘点单是COS盘点单，则报错
            if(!"N".equals(hmeWipStocktakeDocInfoVO.getAttribute1())){
                throw new CommonException("盘点单"+ hmeWipStocktakeDocInfoVO.getStocktakeNum() +"的属性为COS盘点单,不允许在制投料汇总,请确认!");
            }
        }
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.releaseDetailPageQueryNoCos(tenantId, stocktakeIdList, cosItemGroupList, dto));
        for (HmeWipStocktakeDocVO7 result : resultPage) {
            //已投数量
            result.setReleaseQty(result.getReleaseQty().subtract(result.getScrapQty()));
        }
        return resultPage;
    }

    @Override
    public List<HmeWipStocktakeDocVO11> releaseDetailExport(Long tenantId, HmeWipStocktakeDocDTO15 dto) {
        List<HmeWipStocktakeDocVO11> resultList = new ArrayList<>();
        List<String> stocktakeIdList = dto.getStocktakeIdList();
        if (isEmpty(stocktakeIdList)) {
            return resultList;
        }
        for (String stocktakeId:stocktakeIdList) {
            HmeWipStocktakeDocInfoVO hmeWipStocktakeDocInfoVO = hmeWipStocktakeDocMapper.wipStocktakeDocInfoQuery(tenantId, stocktakeId);
            //如果盘点单是COS盘点单，则报错
            if(!"N".equals(hmeWipStocktakeDocInfoVO.getAttribute1())){
                throw new CommonException("盘点单"+ hmeWipStocktakeDocInfoVO.getStocktakeNum() +"的属性为COS盘点单,不允许在制投料汇总,请确认!");
            }
        }
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        resultList = hmeWipStocktakeDocMapper.releaseDetailExport(tenantId, stocktakeIdList, cosItemGroupList, dto);
        for (HmeWipStocktakeDocVO11 result : resultList) {
            //在制数量
            result.setReleaseQty(result.getReleaseQty().subtract(result.getScrapQty()));
        }
        return resultList;
    }
}
