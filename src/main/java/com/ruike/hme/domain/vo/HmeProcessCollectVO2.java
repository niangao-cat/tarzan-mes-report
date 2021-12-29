package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class HmeProcessCollectVO2 implements Serializable {
    private static final long serialVersionUID = 6190818567702731269L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "EO_ID")
    private String eoId;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单产品ID")
    private String woMaterialId;

    @ApiModelProperty(value = "工单产品编码")
    private String woMaterialCode;

    @ApiModelProperty(value = "工单产品描述")
    private String woMaterialName;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "SN产品ID")
    private String snMaterialId;

    @ApiModelProperty(value = "SN产品编码")
    private String snMaterialCode;

    @ApiModelProperty(value = "SN产品描述")
    private String snMaterialName;

    @ApiModelProperty(value = "加工时间")
    private Date siteInDate;

    @ApiModelProperty(value = "加工人ID")
    private String siteInBy;

    @ApiModelProperty(value = "加工人用户名")
    private String siteInLoginName;

    @ApiModelProperty(value = "加工人")
    private String siteInRealName;

    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;

    @ApiModelProperty(value = "出站人ID")
    private String siteOutBy;

    @ApiModelProperty(value = "出站人用户名")
    private String siteOutLoginName;

    @ApiModelProperty(value = "出站人姓名")
    private String siteOutRealName;

    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位")
    private String workcellName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序")
    private String processName;

    @ApiModelProperty(value = "工段ID")
    private String lineId;

    @ApiModelProperty(value = "工段编码")
    private String lineCode;

    @ApiModelProperty(value = "工段名称")
    private String lineName;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "车间ID")
    private String workshopId;

    @ApiModelProperty(value = "车间编码")
    private String workshopCode;

    @ApiModelProperty(value = "车间名称")
    private String workshopName;

    @ApiModelProperty(value = "事业部ID")
    private String divisionId;

    @ApiModelProperty(value = "事业部编码")
    private String divisionCode;

    @ApiModelProperty(value = "事业部名称")
    private String divisionName;

    private String result1;
    private String result2;
    private String result3;
    private String result4;
    private String result5;
    private String result6;
    private String result7;
    private String result8;
    private String result9;
    private String result10;
    private String result11;
    private String result12;
    private String result13;
    private String result14;
    private String result15;
    private String result16;
    private String result17;
    private String result18;
    private String result19;
    private String result20;
    private String result21;
    private String result22;
    private String result23;
    private String result24;
    private String result25;
    private String result26;
    private String result27;
    private String result28;
    private String result29;
    private String result30;
    private String result31;
    private String result32;
    private String result33;
    private String result34;
    private String result35;
    private String result36;
    private String result37;
    private String result38;
    private String result39;
    private String result40;
    private String result41;
    private String result42;
    private String result43;
    private String result44;
    private String result45;
    private String result46;
    private String result47;
    private String result48;
    private String result49;
    private String result50;
    private String result51;
    private String result52;
    private String result53;
    private String result54;
    private String result55;
    private String result56;
    private String result57;
    private String result58;
    private String result59;
    private String result60;
    private String result61;
    private String result62;
    private String result63;
    private String result64;
    private String result65;
    private String result66;
    private String result67;
    private String result68;
    private String result69;
    private String result70;
    private String result71;
    private String result72;
    private String result73;
    private String result74;
    private String result75;
    private String result76;
    private String result77;
    private String result78;
    private String result79;
    private String result80;
    private String result81;
    private String result82;
    private String result83;
    private String result84;
    private String result85;
    private String result86;
    private String result87;
    private String result88;
    private String result89;
    private String result90;
    private String result91;
    private String result92;
    private String result93;
    private String result94;
    private String result95;
    private String result96;
    private String result97;
    private String result98;
    private String result99;
    private String result100;
    private String result101;
    private String result102;
    private String result103;
    private String result104;
    private String result105;
    private String result106;
    private String result107;
    private String result108;
    private String result109;
    private String result110;
    private String result111;
    private String result112;
    private String result113;
    private String result114;
    private String result115;
    private String result116;
    private String result117;
    private String result118;
    private String result119;
    private String result120;
    private String result121;
    private String result122;
    private String result123;
    private String result124;
    private String result125;
    private String result126;
    private String result127;
    private String result128;
    private String result129;
    private String result130;
    private String result131;
    private String result132;
    private String result133;
    private String result134;
    private String result135;
    private String result136;
    private String result137;
    private String result138;
    private String result139;
    private String result140;
    private String result141;
    private String result142;
    private String result143;
    private String result144;
    private String result145;
    private String result146;
    private String result147;
    private String result148;
    private String result149;
    private String result150;
    private String result151;
    private String result152;
    private String result153;
    private String result154;
    private String result155;
    private String result156;
    private String result157;
    private String result158;
    private String result159;
    private String result160;
    private String result161;
    private String result162;
    private String result163;
    private String result164;
    private String result165;
    private String result166;
    private String result167;
    private String result168;
    private String result169;
    private String result170;
    private String result171;
    private String result172;
    private String result173;
    private String result174;
    private String result175;
    private String result176;
    private String result177;
    private String result178;
    private String result179;
    private String result180;
    private String result181;
    private String result182;
    private String result183;
    private String result184;
    private String result185;
    private String result186;
    private String result187;
    private String result188;
    private String result189;
    private String result190;
    private String result191;
    private String result192;
    private String result193;
    private String result194;
    private String result195;
    private String result196;
    private String result197;
    private String result198;
    private String result199;
    private String result200;
    private String result201;
    private String result202;
    private String result203;
    private String result204;
    private String result205;
    private String result206;
    private String result207;
    private String result208;
    private String result209;
    private String result210;
    private String result211;
    private String result212;
    private String result213;
    private String result214;
    private String result215;
    private String result216;
    private String result217;
    private String result218;
    private String result219;
    private String result220;
    private String result221;
    private String result222;
    private String result223;
    private String result224;
    private String result225;
    private String result226;
    private String result227;
    private String result228;
    private String result229;
    private String result230;
    private String result231;
    private String result232;
    private String result233;
    private String result234;
    private String result235;
    private String result236;
    private String result237;
    private String result238;
    private String result239;
    private String result240;
    private String result241;
    private String result242;
    private String result243;
    private String result244;
    private String result245;
    private String result246;
    private String result247;
    private String result248;
    private String result249;
    private String result250;
    private String result251;
    private String result252;
    private String result253;
    private String result254;
    private String result255;
    private String result256;
    private String result257;
    private String result258;
    private String result259;
    private String result260;
    private String result261;
    private String result262;
    private String result263;
    private String result264;
    private String result265;
    private String result266;
    private String result267;
    private String result268;
    private String result269;
    private String result270;
    private String result271;
    private String result272;
    private String result273;
    private String result274;
    private String result275;
    private String result276;
    private String result277;
    private String result278;
    private String result279;
    private String result280;
    private String result281;
    private String result282;
    private String result283;
    private String result284;
    private String result285;
    private String result286;
    private String result287;
    private String result288;
    private String result289;
    private String result290;
    private String result291;
    private String result292;
    private String result293;
    private String result294;
    private String result295;
    private String result296;
    private String result297;
    private String result298;
    private String result299;
    private String result300;
    private String result301;
    private String result302;
    private String result303;
    private String result304;
    private String result305;
    private String result306;
    private String result307;
    private String result308;
    private String result309;
    private String result310;
    private String result311;
    private String result312;
    private String result313;
    private String result314;
    private String result315;
    private String result316;
    private String result317;
    private String result318;
    private String result319;
    private String result320;
    private String result321;
    private String result322;
    private String result323;
    private String result324;
    private String result325;
    private String result326;
    private String result327;
    private String result328;
    private String result329;
    private String result330;
    private String result331;
    private String result332;
    private String result333;
    private String result334;
    private String result335;
    private String result336;
    private String result337;
    private String result338;
    private String result339;
    private String result340;
    private String result341;
    private String result342;
    private String result343;
    private String result344;
    private String result345;
    private String result346;
    private String result347;
    private String result348;
    private String result349;
    private String result350;
    private String result351;
    private String result352;
    private String result353;
    private String result354;
    private String result355;
    private String result356;
    private String result357;
    private String result358;
    private String result359;
    private String result360;
    private String result361;
    private String result362;
    private String result363;
    private String result364;
    private String result365;
    private String result366;
    private String result367;
    private String result368;
    private String result369;
    private String result370;
    private String result371;
    private String result372;
    private String result373;
    private String result374;
    private String result375;
    private String result376;
    private String result377;
    private String result378;
    private String result379;
    private String result380;
    private String result381;
    private String result382;
    private String result383;
    private String result384;
    private String result385;
    private String result386;
    private String result387;
    private String result388;
    private String result389;
    private String result390;
    private String result391;
    private String result392;
    private String result393;
    private String result394;
    private String result395;
    private String result396;
    private String result397;
    private String result398;
    private String result399;
    private String result400;
    private String result401;
    private String result402;
    private String result403;
    private String result404;
    private String result405;
    private String result406;
    private String result407;
    private String result408;
    private String result409;
    private String result410;
    private String result411;
    private String result412;
    private String result413;
    private String result414;
    private String result415;
    private String result416;
    private String result417;
    private String result418;
    private String result419;
    private String result420;
    private String result421;
    private String result422;
    private String result423;
    private String result424;
    private String result425;
    private String result426;
    private String result427;
    private String result428;
    private String result429;
    private String result430;
    private String result431;
    private String result432;
    private String result433;
    private String result434;
    private String result435;
    private String result436;
    private String result437;
    private String result438;
    private String result439;
    private String result440;
    private String result441;
    private String result442;
    private String result443;
    private String result444;
    private String result445;
    private String result446;
    private String result447;
    private String result448;
    private String result449;
    private String result450;
    private String result451;
    private String result452;
    private String result453;
    private String result454;
    private String result455;
    private String result456;
    private String result457;
    private String result458;
    private String result459;
    private String result460;
    private String result461;
    private String result462;
    private String result463;
    private String result464;
    private String result465;
    private String result466;
    private String result467;
    private String result468;
    private String result469;
    private String result470;
    private String result471;
    private String result472;
    private String result473;
    private String result474;
    private String result475;
    private String result476;
    private String result477;
    private String result478;
    private String result479;
    private String result480;
    private String result481;
    private String result482;
    private String result483;
    private String result484;
    private String result485;
    private String result486;
    private String result487;
    private String result488;
    private String result489;
    private String result490;
    private String result491;
    private String result492;
    private String result493;
    private String result494;
    private String result495;
    private String result496;
    private String result497;
    private String result498;
    private String result499;
    private String result500;

    //动态获取字段值
    public static List<String> getResult(Object dto, Integer seqNum) {
        List<String> resultList = new ArrayList<>();
        Class<?> currentClass = dto.getClass();
        Field field;
        try {
            for (int i = 1; i <= seqNum; i++) {
                field = currentClass.getDeclaredField("result" + i);
                //打开私有访问
                field.setAccessible(true);
                //获取属性值
                resultList.add((String) field.get(dto));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}