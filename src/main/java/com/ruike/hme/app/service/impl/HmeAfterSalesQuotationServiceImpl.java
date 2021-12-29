package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeAfterSalesQuotationDTO;
import com.ruike.hme.app.service.HmeAfterSalesQuotationService;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO2;
import com.ruike.hme.domain.vo.HmeItfSnSapIfaceVO;
import com.ruike.hme.infra.constant.Constant;
import com.ruike.hme.infra.mapper.HmeAfterSalesQuotationMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description  报表查询
 *
 * @author wengang.qiang@hand-chian.com 2021/10/15 10:50
 */
@Service
public class HmeAfterSalesQuotationServiceImpl implements HmeAfterSalesQuotationService {

    private final HmeAfterSalesQuotationMapper hmeAfterSalesQuotationMapper;
    private final MtUserClient mtUserClient;

    public HmeAfterSalesQuotationServiceImpl(HmeAfterSalesQuotationMapper hmeAfterSalesQuotationMapper, MtUserClient mtUserClient) {
        this.hmeAfterSalesQuotationMapper = hmeAfterSalesQuotationMapper;
        this.mtUserClient = mtUserClient;
    }

    @Override
    @ProcessLovValue
    public Page<HmeAfterSalesQuotationVO> query(Long tenantId, HmeAfterSalesQuotationDTO dto, PageRequest pageRequest) {
        Page<HmeAfterSalesQuotationVO> page = PageHelper.doPage(pageRequest, () -> hmeAfterSalesQuotationMapper.querySalesQuotation(tenantId, dto));
        //提交人，更新人，创建人
        List<Long> submitterList = new ArrayList<>();
        List<Long> updaterList = new ArrayList<>();
        List<Long> creatorList = new ArrayList<>();
        // 报价单头ID
        List<String> quotationHeaderIdList = page.getContent().stream().map(HmeAfterSalesQuotationVO::getQuotationHeaderId).distinct().collect(Collectors.toList());
        // 根据报价单找对对应的行
        Map<String, List<HmeAfterSalesQuotationVO2>> hmeAfterSalesQuotationLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(quotationHeaderIdList)) {
            List<HmeAfterSalesQuotationVO2> hmeAfterSalesQuotationLineList = hmeAfterSalesQuotationMapper.querySalesQuotationLine(tenantId, quotationHeaderIdList);
            hmeAfterSalesQuotationLineMap = hmeAfterSalesQuotationLineList.stream().collect(Collectors.groupingBy(HmeAfterSalesQuotationVO2::getQuotationHeaderId));
        }
        page.getContent().forEach(hmeAfterSalesQuotationVO -> {
            HmeItfSnSapIfaceVO hmeItfSnSapIfaceVO = new HmeItfSnSapIfaceVO();
            //设置序列号
            hmeItfSnSapIfaceVO.setSernr(hmeAfterSalesQuotationVO.getSerialNumber());
            hmeItfSnSapIfaceVO.setSttxt("ECUS");
            hmeItfSnSapIfaceVO.setStatus("Y");
            List<HmeItfSnSapIfaceVO> itfSnSapIfaceVOList = hmeAfterSalesQuotationMapper.queryItfSnSapIface(tenantId, hmeItfSnSapIfaceVO);
            //根据查询接口表的  last_update_date 拿出最大的一条数据
            List<HmeItfSnSapIfaceVO> snSapIfaceVOList = itfSnSapIfaceVOList.stream().sorted(Comparator.comparing(HmeItfSnSapIfaceVO::getLastUpdateDate)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(snSapIfaceVOList)) {
                HmeItfSnSapIfaceVO snSapIfaceVO = snSapIfaceVOList.get(0);
                //【update_time】<= itf_sn_sap_iface.【last_update_date】;若报价单状态为“SUBMIT”才带出，否则为空；
                if (hmeAfterSalesQuotationVO.getUpdateTime().getTime() <= snSapIfaceVO.getLastUpdateDate().getTime() && StringUtils.equals(hmeAfterSalesQuotationVO.getQuotationStatus(), "SUBMIT")) {
                    hmeAfterSalesQuotationVO.setTheDateOfIssuance(snSapIfaceVO.getLastUpdateDate());
                } else {
                    //发货日期置空
                    hmeAfterSalesQuotationVO.setTheDateOfIssuance(null);
                }
            }
            submitterList.add(hmeAfterSalesQuotationVO.getSubmitter());
            updaterList.add(hmeAfterSalesQuotationVO.getUpdater());
            creatorList.add(hmeAfterSalesQuotationVO.getCreator());
        });
        //去重
        List<Long> submitterDistinctList = submitterList.stream().distinct().collect(Collectors.toList());
        List<Long> updaterDistinctList = updaterList.stream().distinct().collect(Collectors.toList());
        List<Long> creatorDistinctList = creatorList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> submitterMap = new HashMap<>();
        Map<Long, MtUserInfo> updaterMap = new HashMap<>();
        Map<Long, MtUserInfo> creatorMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(submitterDistinctList)) {
            submitterMap = mtUserClient.userInfoBatchGet(tenantId, submitterDistinctList);
        }
        if (CollectionUtils.isNotEmpty(updaterDistinctList)) {
            updaterMap = mtUserClient.userInfoBatchGet(tenantId, updaterDistinctList);
        }
        if (CollectionUtils.isNotEmpty(creatorDistinctList)) {
            creatorMap = mtUserClient.userInfoBatchGet(tenantId, creatorDistinctList);
        }
        for (HmeAfterSalesQuotationVO hmeAfterSalesQuotationVO : page.getContent()) {
            //提交人姓名
            hmeAfterSalesQuotationVO.setSubmitterName(submitterMap.getOrDefault(hmeAfterSalesQuotationVO.getSubmitter(), new MtUserInfo()).getRealName());
            //更新人姓名
            hmeAfterSalesQuotationVO.setUpdaterName(updaterMap.getOrDefault(hmeAfterSalesQuotationVO.getUpdater(), new MtUserInfo()).getRealName());
            //创建人姓名
            hmeAfterSalesQuotationVO.setCreatorName(creatorMap.getOrDefault(hmeAfterSalesQuotationVO.getCreator(), new MtUserInfo()).getRealName());
            List<HmeAfterSalesQuotationVO2> lineList = hmeAfterSalesQuotationLineMap.getOrDefault(hmeAfterSalesQuotationVO.getQuotationHeaderId(), Collections.emptyList());
            // 电学器件已录入
            if(!Constant.ConstantValue.YES.equals(hmeAfterSalesQuotationVO.getElectricNoFlag())) {
                Optional<HmeAfterSalesQuotationVO2> electricOpt = lineList.stream().filter(line -> StringUtils.equals("ELECTRIC", line.getDemandType())).findFirst();
                if (electricOpt.isPresent()) {
                    hmeAfterSalesQuotationVO.setElectricNoFlag(Constant.ConstantValue.YES);
                } else {
                    hmeAfterSalesQuotationVO.setElectricNoFlag(Constant.ConstantValue.NO);
                }
            }
            // 光学器件已录入
            if(!Constant.ConstantValue.YES.equals(hmeAfterSalesQuotationVO.getOpticsNoFlag())) {
                Optional<HmeAfterSalesQuotationVO2> electricOpt = lineList.stream().filter(line -> StringUtils.equals("OPTICS", line.getDemandType())).findFirst();
                if (electricOpt.isPresent()) {
                    hmeAfterSalesQuotationVO.setOpticsNoFlag(Constant.ConstantValue.YES);
                } else {
                    hmeAfterSalesQuotationVO.setOpticsNoFlag(Constant.ConstantValue.NO);
                }
            }
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeAfterSalesQuotationVO> export(Long tenantId, HmeAfterSalesQuotationDTO dto) {
        List<HmeAfterSalesQuotationVO> salesQuotationList = hmeAfterSalesQuotationMapper.querySalesQuotation(tenantId, dto);
        //提交人，更新人，创建人
        List<Long> submitterList = new ArrayList<>();
        List<Long> updaterList = new ArrayList<>();
        List<Long> creatorList = new ArrayList<>();
        // 报价单头ID
        List<String> quotationHeaderIdList = salesQuotationList.stream().map(HmeAfterSalesQuotationVO::getQuotationHeaderId).distinct().collect(Collectors.toList());
        // 根据报价单找对对应的行
        Map<String, List<HmeAfterSalesQuotationVO2>> hmeAfterSalesQuotationLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(quotationHeaderIdList)) {
            List<HmeAfterSalesQuotationVO2> hmeAfterSalesQuotationLineList = hmeAfterSalesQuotationMapper.querySalesQuotationLine(tenantId, quotationHeaderIdList);
            hmeAfterSalesQuotationLineMap = hmeAfterSalesQuotationLineList.stream().collect(Collectors.groupingBy(HmeAfterSalesQuotationVO2::getQuotationHeaderId));
        }
        salesQuotationList.forEach(hmeAfterSalesQuotationVO -> {
            HmeItfSnSapIfaceVO hmeItfSnSapIfaceVO = new HmeItfSnSapIfaceVO();
            //设置序列号
            hmeItfSnSapIfaceVO.setSernr(hmeAfterSalesQuotationVO.getSerialNumber());
            hmeItfSnSapIfaceVO.setSttxt("ECUS");
            hmeItfSnSapIfaceVO.setStatus("S");
            List<HmeItfSnSapIfaceVO> itfSnSapIfaceVOList = hmeAfterSalesQuotationMapper.queryItfSnSapIface(tenantId, hmeItfSnSapIfaceVO);
            //根据查询接口表的  last_update_date 拿出最大的一条数据
            List<HmeItfSnSapIfaceVO> snSapIfaceVOList = itfSnSapIfaceVOList.stream().sorted(Comparator.comparing(HmeItfSnSapIfaceVO::getLastUpdateDate)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(snSapIfaceVOList)) {
                HmeItfSnSapIfaceVO snSapIfaceVO = snSapIfaceVOList.get(0);
                //【Submission_data】>= itf_sn_sap_iface.【last_update_date】;若报价单状态为“SUBMIT”才带出，否则为空；
                if (hmeAfterSalesQuotationVO.getUpdateTime().getTime() <= snSapIfaceVO.getLastUpdateDate().getTime() && StringUtils.equals(hmeAfterSalesQuotationVO.getQuotationStatus(), "SUBMIT")) {
                    hmeAfterSalesQuotationVO.setTheDateOfIssuance(snSapIfaceVO.getLastUpdateDate());
                } else {
                    //发货日期置空
                    hmeAfterSalesQuotationVO.setTheDateOfIssuance(null);
                }
            }
            submitterList.add(hmeAfterSalesQuotationVO.getSubmitter());
            updaterList.add(hmeAfterSalesQuotationVO.getUpdater());
            creatorList.add(hmeAfterSalesQuotationVO.getCreator());
        });
        //去重
        List<Long> submitterDistinctList = submitterList.stream().distinct().collect(Collectors.toList());
        List<Long> updaterDistinctList = updaterList.stream().distinct().collect(Collectors.toList());
        List<Long> creatorDistinctList = creatorList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> submitterMap = new HashMap<>();
        Map<Long, MtUserInfo> updaterMap = new HashMap<>();
        Map<Long, MtUserInfo> creatorMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(submitterDistinctList)) {
            submitterMap = mtUserClient.userInfoBatchGet(tenantId, submitterDistinctList);
        }
        if (CollectionUtils.isNotEmpty(updaterDistinctList)) {
            updaterMap = mtUserClient.userInfoBatchGet(tenantId, updaterDistinctList);
        }
        if (CollectionUtils.isNotEmpty(creatorDistinctList)) {
            creatorMap = mtUserClient.userInfoBatchGet(tenantId, creatorDistinctList);
        }
        for (HmeAfterSalesQuotationVO hmeAfterSalesQuotationVO : salesQuotationList) {
            //提交人姓名
            hmeAfterSalesQuotationVO.setSubmitterName(submitterMap.getOrDefault(hmeAfterSalesQuotationVO.getSubmitter(), new MtUserInfo()).getRealName());
            //更新人姓名
            hmeAfterSalesQuotationVO.setUpdaterName(updaterMap.getOrDefault(hmeAfterSalesQuotationVO.getUpdater(), new MtUserInfo()).getRealName());
            //创建人姓名
            hmeAfterSalesQuotationVO.setCreatorName(creatorMap.getOrDefault(hmeAfterSalesQuotationVO.getCreator(), new MtUserInfo()).getRealName());

            List<HmeAfterSalesQuotationVO2> lineList = hmeAfterSalesQuotationLineMap.getOrDefault(hmeAfterSalesQuotationVO.getQuotationHeaderId(), Collections.emptyList());

            // 电学器件已录入
            if(!Constant.ConstantValue.YES.equals(hmeAfterSalesQuotationVO.getElectricNoFlag())) {
                Optional<HmeAfterSalesQuotationVO2> electricOpt = lineList.stream().filter(line -> StringUtils.equals("ELECTRIC", line.getDemandType())).findFirst();
                if (electricOpt.isPresent()) {
                    hmeAfterSalesQuotationVO.setElectricNoFlag(Constant.ConstantValue.YES);
                } else {
                    hmeAfterSalesQuotationVO.setElectricNoFlag(Constant.ConstantValue.NO);
                }
            }
            // 光学器件已录入
            if(!Constant.ConstantValue.YES.equals(hmeAfterSalesQuotationVO.getOpticsNoFlag())) {
                Optional<HmeAfterSalesQuotationVO2> electricOpt = lineList.stream().filter(line -> StringUtils.equals("OPTICS", line.getDemandType())).findFirst();
                if (electricOpt.isPresent()) {
                    hmeAfterSalesQuotationVO.setOpticsNoFlag(Constant.ConstantValue.YES);
                } else {
                    hmeAfterSalesQuotationVO.setOpticsNoFlag(Constant.ConstantValue.NO);
                }
            }
        }
        return salesQuotationList;
    }
}
