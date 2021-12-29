package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeCosStaffProductRepository;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO2;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO3;
import com.ruike.hme.infra.mapper.HmeCosStaffProductMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：sanfeng.zhang@hand-china.com 2021/5/17 14:33
 */
@Component
public class HmeCosStaffProductRepositoryImpl implements HmeCosStaffProductRepository {

    @Autowired
    private HmeCosStaffProductMapper hmeCosStaffProductMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtUserClient mtUserClient;

    @Override
    public Page<HmeCosStaffProductVO2> staffProductQuery(Long tenantId, HmeCosStaffProductVO dto, PageRequest pageRequest) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);
        List<String> jobTypeList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        // 优先工序 工段 产线
        List<String> workcellIdList = new ArrayList<>();
        Boolean queryFlag = false;
        if (CollectionUtils.isNotEmpty(dto.getProcessIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByProcessId(tenantId, dto.getProcessIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        } else if (CollectionUtils.isNotEmpty(dto.getLineWorkcellIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        } else if (CollectionUtils.isNotEmpty(dto.getProdLineIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByProdLineId(tenantId, dto.getProdLineIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        }
        if (queryFlag && CollectionUtils.isEmpty(workcellIdList)) {
            return new Page<>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }
        dto.setWorkcellIdList(workcellIdList);
        Page<HmeCosStaffProductVO2> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosStaffProductMapper.staffProductQuery(tenantId, dto, jobTypeList));
        handleData(tenantId, pageObj.getContent());
        return pageObj;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosStaffProductVO2> staffProductExport(Long tenantId, HmeCosStaffProductVO dto) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);
        List<String> jobTypeList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        // 优先工序 工段 产线
        List<String> workcellIdList = new ArrayList<>();
        Boolean queryFlag = false;
        if (CollectionUtils.isNotEmpty(dto.getProcessIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByProcessId(tenantId, dto.getProcessIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        } else if (CollectionUtils.isNotEmpty(dto.getLineWorkcellIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        } else if (CollectionUtils.isNotEmpty(dto.getProdLineIdList())) {
            List<String> workcellIds = hmeCosStaffProductMapper.queryWorkcellByProdLineId(tenantId, dto.getProdLineIdList());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
            queryFlag = true;
        }
        if (queryFlag && CollectionUtils.isEmpty(workcellIdList)) {
            return Collections.EMPTY_LIST;
        }
        dto.setWorkcellIdList(workcellIdList);
        List<HmeCosStaffProductVO2> resultList = hmeCosStaffProductMapper.staffProductQuery(tenantId, dto, jobTypeList);
        handleData(tenantId, resultList);
        return resultList;
    }

    private void handleData(Long tenantId, List<HmeCosStaffProductVO2> dtoList){
        List<String> processIdList = dtoList.stream().map(HmeCosStaffProductVO2::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeCosStaffProductVO3>> organizationInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(processIdList)) {
            List<HmeCosStaffProductVO3> organizationInfoList = hmeCosStaffProductMapper.queryOrganizationInfo(tenantId, processIdList);
            if (CollectionUtils.isNotEmpty(organizationInfoList)) {
                organizationInfoMap = organizationInfoList.stream().collect(Collectors.groupingBy(HmeCosStaffProductVO3::getProcessId));
            }
        }
        //批量查询员工姓名
        List<Long> siteInByList = dtoList.stream().map(HmeCosStaffProductVO2::getSiteInBy).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(siteInByList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, siteInByList);
        }

        for (HmeCosStaffProductVO2 hmeCosStaffProductVO2 : dtoList) {
            List<HmeCosStaffProductVO3> hmeCosStaffProductVO3s = organizationInfoMap.get(hmeCosStaffProductVO2.getProcessId());
            if (CollectionUtils.isNotEmpty(hmeCosStaffProductVO3s)) {
                // 工段
                hmeCosStaffProductVO2.setLineWordcellName(hmeCosStaffProductVO3s.get(0).getLineWorkcellName());
                // 产线
                hmeCosStaffProductVO2.setProdLineName(hmeCosStaffProductVO3s.get(0).getProdLineName());
            }
            // 一次合格率 (实际产出-不良数）/实际产出
            BigDecimal subQty = hmeCosStaffProductVO2.getTotalSnQty().subtract(hmeCosStaffProductVO2.getNcQty());
            if (hmeCosStaffProductVO2.getTotalSnQty().compareTo(BigDecimal.ZERO) != 0) {
                hmeCosStaffProductVO2.setPassRate(subQty.divide(hmeCosStaffProductVO2.getTotalSnQty(), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if(Objects.nonNull(hmeCosStaffProductVO2.getSiteInBy())){
                MtUserInfo mtUserInfo = userInfoMap.get(hmeCosStaffProductVO2.getSiteInBy());
                if (mtUserInfo != null) {
                    // 员工姓名
                    hmeCosStaffProductVO2.setRealName(mtUserInfo.getRealName());
                    //工号
                    hmeCosStaffProductVO2.setLoginName(mtUserInfo.getLoginName());
                }
            }
        }
    }
}
