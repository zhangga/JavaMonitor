package com.abc.monitor_server.mapper;

import com.abc.monitor_server.model.LogData;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by U-Demon
 * Date: 2019/7/4
 */
public interface LogMapper {

    @Insert("INSERT INTO `method_time`(class_name, method_name, run_time, log_time) VALUES " +
            "(#{class_name}, #{method_name}, #{run_time}, #{log_time})")
    Long insert(LogData log);

}
