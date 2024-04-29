package com.pinyou.db.entity.station

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "charge_station", indices = [Index(value = ["ID", "LONGITUDE", "LATITUDE"])])
data class StationEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var changeId: String,
    // 添加人/来源 
    @ColumnInfo(name = "BRAND")
    var brand: String?,
    // 0私有1公有2北汽3其他  
    @ColumnInfo(name = "TYPE")
    var type: Long?,
    // 电站名称 
    @ColumnInfo(name = "NAME")
    var name: String,
    // 电站名称备份 
    @ColumnInfo(name = "NAME_BAK")
    var nameBak: String?,
    // 电站类型 (电站类型ID 关联bq_charging_type表)  
    @ColumnInfo(name = "OWNER")
    var owner: Long?,
    // 开放时间 
    @ColumnInfo(name = "OPENTIME")
    var opentime: String?,
    // 运营方 bq_company表的id  
    @ColumnInfo(name = "OPERATOR")
    var operator: String,
    // 运营方名称  
    @ColumnInfo(name = "OPERATOR_NAME")
    var operatorName: String,
    // 运营方图片
    @ColumnInfo(name = "IMG")
    var img: String?,
    // 快充桩个数  
    @ColumnInfo(name = "FAST_CHARGING_PILE_COUNT")
    var fastChargingPileCount: Long,
    // 慢充桩个数 
    @ColumnInfo(name = "SLOW_CHARGING_PILE_COUNT")
    var slowChargingPileCount: Long,
    // 快充桩品牌 bq_charging_brand  
    @ColumnInfo(name = "FAST_CHARGING_PILE_BRAND")
    var fastChargingPileBrand: String?,
    // 慢充桩品牌 bq_charging_brand  
    @ColumnInfo(name = "SLOW_CHARGING_PILE_BRAND")
    var slowChargingPileBrand: String?,
    // 快充桩品牌名称 bq_charging_brand
    @ColumnInfo(name = "FAST_CHARGING_PILE_BRAND_NAME")
    var fastChargingPileBrandName: String?,
    // 慢充桩品牌名称 bq_charging_brand
    @ColumnInfo(name = "SLOW_CHARGING_PILE_BRAND_NAME")
    var slowChargingPileBrandName: String?,
    // 快充桩参数  
    @ColumnInfo(name = "FAST_CHARGING_PILE_PARAMETER")
    var fastChargingPileParameter: String?,
    // 慢充桩参数  
    @ColumnInfo(name = "SLOW_CHARGING_PILE_PARAMETER")
    var slowChargingPileParameter: String?,
    // 停车场类型  (停车场类型 0其他 1地面 2地下 3立体车库  4 未知 5停车楼) 
    @ColumnInfo(name = "CHARGING_PARK")
    var chargingPark: Long?,
    // 停车场类型名称  (停车场类型 0其他 1地面 2地下 3立体车库  4 未知 5停车楼) 
    @ColumnInfo(name = "CHARGING_PARK_NAME")
    var chargingParkName: String?,
    // 停车费：0免费 1 收费 2 不详 
    @ColumnInfo(name = "IS_PARKINGFEE")
    var isParkingfee: Long?,
    // 停车费名称：0免费 1 收费 2 不详 
    @ColumnInfo(name = "IS_PARKINGFEE_NAME")
    var isParkingfeeName: String?,
    // 停车收费信息  
    @ColumnInfo(name = "PARKINGFEE_INFORMATION")
    var parkingfeeInformation: String?,
    // 辅助字段 停车费信息  
    @ColumnInfo(name = "PARKINGMESS")
    var parkingmess: String?,
    // 充电费 0不详 1收费 2免费  
    @ColumnInfo(name = "CHARGEFREE")
    var chargefree: Long?,
    // 充电费名称 0不详 1收费 2免费  
    @ColumnInfo(name = "CHARGEFREE_NAME")
    var chargefreeName: String?,
    // 充电费信息  
    @ColumnInfo(name = "CHARGEFREE_MESS")
    var chargefreeMess: String?,
    // 辅助字段 充电费信息  
    @ColumnInfo(name = "CHARGTMESS")
    var chargtmess: String?,
    // 服务费：0免费 1收费2不详  
    @ColumnInfo(name = "IS_SERVICEFREE")
    var isServicefree: Long?,
    // 服务费名称：0免费 1收费2不详  
    @ColumnInfo(name = "IS_SERVICEFREE_NAME")
    var isServicefreeName: String?,
    // 服务收费信息
    @ColumnInfo(name = "SERVICEFREE_INFORMATION")
    var servicefreeInformation: String?,
    // 服务收费类型 0.元/度电  1.元/小时  2.元/次 3
    @ColumnInfo(name = "SERVICEFREE_TYPE")
    var servicefreeType: Long?,
    // 服务收费类型名称 0.元/度电  1.元/小时  2.元/次 3
    @ColumnInfo(name = "SERVICEFREE_TYPE_NAME")
    var servicefreeTypeName: String?,
    // 辅助字段  
    @ColumnInfo(name = "SERVICEFREEMESS")
    var servicefreemess: String?,
    // 支付方式 bq_pay表的id
    @ColumnInfo(name = "PAY_TYPE")
    var payType: String?,
    // 支付方式名称 bq_pay表的id
    @ColumnInfo(name = "PAY_TYPE_NAME")
    var payTypeName: String?,
    // 运营状态:  0在建中 1已建成未测试 2 已建成已测试 3关闭中 
    @ColumnInfo(name = "BRAND_STATUS")
    var brandStatus: Long?,
    // 运营状态名称:  0在建中 1已建成未测试 2 已建成已测试 3关闭中 
    @ColumnInfo(name = "BRAND_STATUS_NAME")
    var brandStatusName: String?,
    // 所属区域代码 
    @ColumnInfo(name = "AREA")
    var area: String,
    // 区域名称  
    @ColumnInfo(name = "AREA_NAME")
    var areaName: String?,
    // 所属省份代码 
    @ColumnInfo(name = "PARENT_AREA")
    var parentArea: String,
    // 省份名称  
    @ColumnInfo(name = "PARENT_AREA_NAME")
    var parentAreaName: String?,
    // 电站地址 
    @ColumnInfo(name = "ADDRESS")
    var address: String,
    // 联系电话  
    @ColumnInfo(name = "TELEPHONE")
    var telephone: String?,
    // 电站添加人手机  
    @ColumnInfo(name = "MOBILE")
    var mobile: String?,
    // 备注  
    @ColumnInfo(name = "NOTE")
    var note: String?,
    // 经度(百度)  
    @ColumnInfo(name = "LONGITUDE")
    var longitude: Double,
    // 纬度(百度)  
    @ColumnInfo(name = "LATITUDE")
    var latitude: Double,
    // 是否审核通过 0 未读   1已阅   2通过  -1不通过  
    @ColumnInfo(name = "NUMBER")
    var number: String?,
    // 是否审核通过名称 0 未读   1已阅   2通过  -1不通过  
    @ColumnInfo(name = "NUMBER_NAME")
    var numberName: String?,
    //  创建时间   
    @ColumnInfo(name = "CREATE_TIME")
    var createTime: String,
    // 展示图片  
    @ColumnInfo(name = "ICON")
    var icon: String?,
    // 是否显示（默认显示）0显示 1 隐藏  
    @ColumnInfo(name = "IS_SHOW")
    var isShow: Long?,
    // 是否显示名称（默认显示）0显示 1 隐藏  
    @ColumnInfo(name = "IS_SHOW_NAME")
    var isShowName: String?,
    // 是否完善（默认已完善）0未完善 1 已完善 
    @ColumnInfo(name = "IS_FINISH")
    var isFinish: Long?,
    // 是否完善（默认已完善）0未完善 1 已完善 
    @ColumnInfo(name = "IS_FINISH_NAME")
    var isFinishName: String?,
    // 桩品牌 bq_company表的id （无用） 
    @ColumnInfo(name = "CP_BRAND")
    var cpBrand: String?,
    // APP接口需要用到 辅助字段 收藏id  
    @ColumnInfo(name = "OPINION_ID")
    var opinionId: Long?,
    // APP接口需要用到 辅助字段 距离  
    @ColumnInfo(name = "DISTANCE")
    var distance: String?,
    // APP接口需要用到 辅助字段 充电站类型  
    @ColumnInfo(name = "OWNER_STR")
    var ownerStr: String?,
    // 电站类型  常量类中配置，对应不同第三方 
    @ColumnInfo(name = "STATION_TYPE")
    var stationType: Long?,
    // 第三方电站标识（1已删除 0未删除-默认） 【作用于后台】 
    @ColumnInfo(name = "STATION_STATU")
    var stationStatu: Long?,
    // 第三方电站标识名称（1已删除 0未删除-默认） 【作用于后台】 
    @ColumnInfo(name = "STATION_STATU_NAME")
    var stationStatuName: String?,
    // 是否删除 0未删除 1已删除  
    @ColumnInfo(name = "ID_DEL")
    var idDel: Long?,
    // 是否删除名称 0未删除 1已删除  
    @ColumnInfo(name = "ID_DEL_NAME")
    var idDelName: String?,
    // 电站编号
    @ColumnInfo(name = "STATION_NUMBER")
    var stationNumber: String?,
    // 辅助字段  是否可编辑  
    @ColumnInfo(name = "IS_EDIT")
    var isEdit: Long?,
    // 高速一级  
    @ColumnInfo(name = "HIGH_SPEED_CLASS_ONE")
    var highSpeedClassOne: Long?,
    // 高速一级  
    @ColumnInfo(name = "HIGH_SPEED_CLASS_TWO")
    var highSpeedClassTwo: Long?,
    // 唯一标识（用于数据同步） 
    @ColumnInfo(name = "MARK_ID")
    var markId: String?,
    // 高德地图经度  
    @ColumnInfo(name = "GD_LNG")
    var gdLng: String?,
    // 高德纬度  
    @ColumnInfo(name = "GD_LAT")
    var gdLat: String?,
    // 高德地图经度备份  
    @ColumnInfo(name = "GD_LNG_BAK")
    var gdLngBak: String?,
    // 高德地图纬度备份  
    @ColumnInfo(name = "GD_LAT_BAK")
    var gdLatBak: String?,
    // 电压最大值
    @ColumnInfo(name = "VOLTAGE_MAX")
    var voltageMax: Long,
    // 电压最小值
    @ColumnInfo(name = "VOLTAGE_MIN")
    var voltageMin: Long,
    // 功率最大值
    @ColumnInfo(name = "POWER_MAX")
    var powerMax: Double,
    // 功率最小值
    @ColumnInfo(name = "POWER_MIN")
    var powerMin: Double,
    // （0否1是）
    @ColumnInfo(name = "IS_NONE_INDUCTIVE")
    var isNoneInductive: String,
)
