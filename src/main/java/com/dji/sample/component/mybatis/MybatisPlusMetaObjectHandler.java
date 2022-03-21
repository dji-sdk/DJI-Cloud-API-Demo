package com.dji.sample.component.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Automatic filling for set values
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * Automatic filling when inserting into the database.
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Long.class,
                LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
        this.strictInsertFill(metaObject, "updateTime", Long.class,
                LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
    }

    /**
     * Automatic filling when updating the data.
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Long.class,
                LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
    }
}
