package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeMonthlyPlanDTO;
import com.ruike.hme.app.service.HmeMonthlyPlanService;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO2;
import com.ruike.hme.infra.mapper.HmeCommonReportMapper;
import com.ruike.hme.infra.mapper.HmeMonthlyPlanMapper;
import com.ruike.hme.infra.util.HmeCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import tarzan.common.domain.util.DateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName HmeMonthlyPlanServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 12:06
 * @Version 1.0
 **/
@Service
public class HmeMonthlyPlanServiceImpl implements HmeMonthlyPlanService {

    @Autowired
    private HmeMonthlyPlanMapper hmeMonthlyPlanMapper;
    @Autowired
    private HmeCommonReportMapper hmeCommonReportMapper;

    @Override
    public Page<HmeMonthlyPlanVO> monthlyPlanQuery(Long tenantId, HmeMonthlyPlanDTO dto, PageRequest pageRequest) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date queryDate = new Date();
        if (StringUtils.isNotBlank(dto.getQueryMonth())) {
            try {
                queryDate = sdf.parse(dto.getQueryMonth());
            } catch (Exception e) {
                throw new CommonException("时间格式错误！！！");
            }
        }
        this.handleQueryParam(tenantId, dto, queryDate);
        // 查询eo入库及完工数量
        List<HmeMonthlyPlanVO> monthlyPlanVOList = hmeMonthlyPlanMapper.monthlyPlanQuery(tenantId, dto);
        // 查询所选月份部门下的月季计划信息
        List<HmeMonthlyPlanVO2> monthlyPlanVO2List = hmeMonthlyPlanMapper.queryMonthPlanByAreaId(tenantId, dto);
        // 组装数据 按物料进行合并
        List<HmeMonthlyPlanVO> resultList = this.assemblyDataByMaterial(tenantId, monthlyPlanVOList, monthlyPlanVO2List, dto);
        Page<HmeMonthlyPlanVO> pageObj = HmeCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        return pageObj;
    }

    @Override
    public Page<HmeMonthlyPlanVO> monthlyPlanQueryNew(Long tenantId, HmeMonthlyPlanDTO dto, PageRequest pageRequest) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date queryDate = new Date();
        if (StringUtils.isNotBlank(dto.getQueryMonth())) {
            try {
                queryDate = sdf.parse(dto.getQueryMonth());
            } catch (Exception e) {
                throw new CommonException("时间格式错误！！！");
            }
        }
        this.handleQueryParam(tenantId, dto, queryDate);
        Page<HmeMonthlyPlanVO> pageResult = PageHelper.doPage(pageRequest, () -> hmeMonthlyPlanMapper.monthlyPlanQueryNew(tenantId, dto));
        return pageResult;
    }

    @Override
    public List<HmeMonthlyPlanVO> monthlyPlanExport(Long tenantId, HmeMonthlyPlanDTO dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date queryDate = new Date();
        if (StringUtils.isNotBlank(dto.getQueryMonth())) {
            try {
                queryDate = sdf.parse(dto.getQueryMonth());
            } catch (Exception e) {
                throw new CommonException("时间格式错误！！！");
            }
        }
        this.handleQueryParam(tenantId, dto, queryDate);
        // 查询eo入库及完工数量
        List<HmeMonthlyPlanVO> monthlyPlanVOList = hmeMonthlyPlanMapper.monthlyPlanQuery(tenantId, dto);
        // 查询所选月份部门下的月季计划信息
        List<HmeMonthlyPlanVO2> monthlyPlanVO2List = hmeMonthlyPlanMapper.queryMonthPlanByAreaId(tenantId, dto);
        // 组装数据 按物料进行合并
        List<HmeMonthlyPlanVO> resultList = this.assemblyDataByMaterial(tenantId, monthlyPlanVOList, monthlyPlanVO2List, dto);
        return resultList;
    }

    @Override
    public List<HmeMonthlyPlanVO> monthlyPlanExportNew(Long tenantId, HmeMonthlyPlanDTO dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date queryDate = new Date();
        if (StringUtils.isNotBlank(dto.getQueryMonth())) {
            try {
                queryDate = sdf.parse(dto.getQueryMonth());
            } catch (Exception e) {
                throw new CommonException("时间格式错误！！！");
            }
        }
        this.handleQueryParam(tenantId, dto, queryDate);
        return hmeMonthlyPlanMapper.monthlyPlanQueryNew(tenantId, dto);
    }

    private void handleQueryParam (Long tenantId, HmeMonthlyPlanDTO dto, Date queryDate) {
        List<String> prodLineIdList = StringUtils.isBlank(dto.getProdLineId()) ? null : Arrays.asList(StringUtils.split(dto.getProdLineId(), ","));
        dto.setProdLineIdList(prodLineIdList);
        // 没有选择月份 则默认当月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(queryDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dto.setQueryMonthFrom(DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        dto.setQueryMonthTo(DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
        dto.setYearMonth(DateUtil.date2String(queryDate, "yyyyMM"));
        //用户默认站点
        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        dto.setSiteId(defaultSiteId);
    }

    private List<HmeMonthlyPlanVO> assemblyDataByMaterial (Long tenantId, List<HmeMonthlyPlanVO> monthlyPlanVOList, List<HmeMonthlyPlanVO2> monthlyPlanVO2List, HmeMonthlyPlanDTO dto) {
        List<HmeMonthlyPlanVO> resultList = new ArrayList<>();
        // 两个都为空 则返回空 交集部分取出计划数量 非交集拼接
        List<String> materialIdList = monthlyPlanVOList.stream().map(HmeMonthlyPlanVO::getMaterialId).distinct().collect(Collectors.toList());
        // 取出非交集的
        List<HmeMonthlyPlanVO2> nonCrossList = monthlyPlanVO2List.stream().filter(vo -> !materialIdList.contains(vo.getMaterialId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(monthlyPlanVOList)) {
            BeanCopier copier = BeanCopier.create(HmeMonthlyPlanVO.class, HmeMonthlyPlanVO.class, false);
            for (HmeMonthlyPlanVO hmeMonthlyPlanVO : monthlyPlanVOList) {
                HmeMonthlyPlanVO planVO = new HmeMonthlyPlanVO();
                copier.copy(hmeMonthlyPlanVO, planVO, null);
                Optional<HmeMonthlyPlanVO2> firstOpt = monthlyPlanVO2List.stream().filter(vo -> StringUtils.equals(vo.getMaterialId(), hmeMonthlyPlanVO.getMaterialId()) && StringUtils.equals(vo.getBusinessId(), hmeMonthlyPlanVO.getAreaId())).findFirst();
                planVO.setPlanQty(firstOpt.isPresent() ? firstOpt.get().getQuantity() : BigDecimal.ZERO);
                // 达成率 = 入库数量/计划数量 * 100%
                BigDecimal planReachRate = BigDecimal.ZERO;
                if (BigDecimal.ZERO.compareTo(planVO.getPlanQty()) != 0) {
                    planReachRate = planVO.getActualQty().divide(planVO.getPlanQty(), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                planVO.setPlanReachRate(planReachRate + "%");
                resultList.add(planVO);
            }
        }
        if (CollectionUtils.isNotEmpty(nonCrossList)) {
            // 判断是cos物料还是非cos物料 cos物料完工数等于入库数
            List<HmeMonthlyPlanVO2> cosMaterialList = nonCrossList.stream().filter(vo -> StringUtils.equals(vo.getMaterialType(), "COS")).collect(Collectors.toList());
            Map<String, List<HmeMonthlyPlanVO>> cosFinishQtyMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(cosMaterialList)) {
                List<String> cosMaterialIdList = cosMaterialList.stream().map(HmeMonthlyPlanVO2::getMaterialId).distinct().collect(Collectors.toList());
                List<HmeMonthlyPlanVO> cosFinishQtyList = hmeMonthlyPlanMapper.queryFinishQtyByCosMaterialIds(tenantId, cosMaterialIdList, dto);
                if (CollectionUtils.isNotEmpty(cosFinishQtyList)) {
                    cosFinishQtyMap = cosFinishQtyList.stream().collect(Collectors.groupingBy(HmeMonthlyPlanVO::getMaterialId));
                }
            }
            for (HmeMonthlyPlanVO2 hmeMonthlyPlanVO2 : nonCrossList) {
                HmeMonthlyPlanVO planVO = new HmeMonthlyPlanVO();
                planVO.setProdLineCode("");
                planVO.setProdLineName("");
                planVO.setMaterialId(hmeMonthlyPlanVO2.getMaterialId());
                planVO.setMaterialCode(hmeMonthlyPlanVO2.getMaterialCode());
                planVO.setMaterialName(hmeMonthlyPlanVO2.getMaterialName());
                planVO.setAreaId(hmeMonthlyPlanVO2.getBusinessId());
                planVO.setAreaName(hmeMonthlyPlanVO2.getBusinessName());
                planVO.setPlanQty(hmeMonthlyPlanVO2.getQuantity());
                if (StringUtils.equals(hmeMonthlyPlanVO2.getMaterialType(), "COS")) {
                    List<HmeMonthlyPlanVO> cosFinishQtyList = cosFinishQtyMap.get(hmeMonthlyPlanVO2.getMaterialId());
                    if (CollectionUtils.isNotEmpty(cosFinishQtyList)) {
                        planVO.setQty(cosFinishQtyList.get(0).getQty());
                        planVO.setActualQty(cosFinishQtyList.get(0).getQty());
                    } else {
                        planVO.setQty(BigDecimal.ZERO);
                        planVO.setActualQty(BigDecimal.ZERO);
                    }
                } else {
                    planVO.setQty(BigDecimal.ZERO);
                    planVO.setActualQty(BigDecimal.ZERO);
                }
                // 达成率 = 入库数量/计划数量 * 100%
                BigDecimal planReachRate = BigDecimal.ZERO;
                if (BigDecimal.ZERO.compareTo(planVO.getPlanQty()) != 0) {
                    planReachRate = planVO.getActualQty().divide(planVO.getPlanQty(), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                planVO.setPlanReachRate(planReachRate + "%");
                resultList.add(planVO);
            }
        }
        return resultList;
    }

}
