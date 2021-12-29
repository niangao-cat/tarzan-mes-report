package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeSelfRepairDTO;
import com.ruike.hme.domain.repository.HmeSelfRepairRepository;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import com.ruike.hme.domain.vo.HmeSelfRepairVO;
import com.ruike.hme.infra.mapper.HmeSelfRepairMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.common.domain.sys.MtUserClient;
import tarzan.common.domain.sys.MtUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自制件返修统计报表
 *
 * @author xin.t@raycuslaser 2021/7/5 15:17
 */
@Component
public class HmeSelfRepairRepositoryImpl implements HmeSelfRepairRepository {

    @Autowired
    private HmeSelfRepairMapper hmeSelfRepairMapper;

    @Autowired
    private  MtUserClient userClient;

    @Override
    @ProcessLovValue
    public Page<HmeSelfRepairVO> query(Long tenantId, HmeSelfRepairDTO dto, PageRequest pageRequest) {
        Page<HmeSelfRepairVO> vos = PageHelper.doPage(pageRequest,()->hmeSelfRepairMapper.fetchList(tenantId,dto));
        setValue(tenantId,vos.getContent());

        return vos;

    }

    @Override
    @ProcessLovValue
    public List<HmeSelfRepairVO> export(Long tenantId, HmeSelfRepairDTO dto) {
        List<HmeSelfRepairVO> voList = hmeSelfRepairMapper.fetchList(tenantId,dto);
        setValue(tenantId,voList);
        return voList;
    }

    private void setValue(Long tenantId,List<HmeSelfRepairVO> voList){

        if (CollectionUtils.isEmpty(voList)) return;

        // 查询用户信息
        List<Long> userIdList = Stream.concat(voList.stream().map(HmeSelfRepairVO::getCreatedBy), voList.stream().map(HmeSelfRepairVO::getCreatedBy)).collect(Collectors.toList());
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, userIdList);

        //查询工位信息
        List<String> wos = voList.stream().map(HmeSelfRepairVO::getWorkOrderNum).collect(Collectors.toList());
        Map<String,List<HmeCosWorkcellExceptionVO>> wcMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(wos)){
            List<HmeCosWorkcellExceptionVO> workcells = hmeSelfRepairMapper.queryWorkcell(tenantId,wos);
            if (CollectionUtils.isNotEmpty(workcells)){
                wcMap = workcells.stream().collect(Collectors.groupingBy(HmeCosWorkcellExceptionVO::getWorkOrderNum));
            }
        }

        for (HmeSelfRepairVO vo:voList){
            //用户信息赋值
            MtUserInfo info = userMap.get(vo.getCreatedBy());
            if (info != null){
                vo.setCreatedId(info.getLoginName());
                vo.setCreatedName(info.getRealName());
            }
            //工位信息赋值
            List<HmeCosWorkcellExceptionVO> wcs = wcMap.get(vo.getWorkOrderNum());
            if (CollectionUtils.isNotEmpty(wcs)){
                HmeCosWorkcellExceptionVO wc = wcs.get(0);
                vo.setWorkcellCode(wc.getWorkcellCode() == null ? "" : wc.getWorkcellCode());
                vo.setWorkcellName(wc.getWorkcellName() == null ? "" : wc.getWorkcellName());
            }

            //条码在制状态完工后未更新的，重新更新  条码为在制但工单状态为完成的 ==> 在制标识为N
            if("COMPLETED".equals(vo.getWorkOrderStatus()) && StringUtils.isBlank(vo.getWorkcellCode())){
                vo.setMfFlag("N");
            }
        }
    }
}

