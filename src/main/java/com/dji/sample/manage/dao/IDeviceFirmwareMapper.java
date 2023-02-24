package com.dji.sample.manage.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dji.sample.manage.model.entity.DeviceFirmwareEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/16
 */
public interface IDeviceFirmwareMapper extends BaseMapper<DeviceFirmwareEntity> {
    String sql = "<script> \n" +
                "SELECT \n" +
                "  * \n" +
                "from \n" +
                "  (\n" +
                "    select \n" +
                "      a.*, \n" +
                "      group_concat(b.device_name) device_name \n" +
                "    from \n" +
                "      manage_device_firmware a \n" +
                "      join manage_firmware_model b on a.firmware_id = b.firmware_id \n" +
                "   <if test='device_name != null and device_name != \"\"'> \n" +
                "       and b.device_name = #{device_name} \n" +
                "   </if> \n" +
                "   group by firmware_id \n" +
                "  ) c ${ew.customSqlSegment} \n";

    @Select(sql + "</script>")
    Page<DeviceFirmwareEntity> selectPage(Page page, @Param(Constants.WRAPPER)Wrapper<DeviceFirmwareEntity> wrapper, @Param("device_name") String deviceName);

    @Select(sql + " limit 1 </script>")
    DeviceFirmwareEntity selectOne(@Param(Constants.WRAPPER)Wrapper<DeviceFirmwareEntity> wrapper, @Param("device_name") String deviceName);
}
