package time;


import java.util.HashMap;
import java.util.List;

/**
 * @author JamesLiu
 * @version V1.0
 * @Title: TimeDao.java
 * @Package com.ubt.nlu.service.dao
 * @Description: 时间处理DAO
 * @date 2019年8月1日 下午4:32:06
 */
public interface TimeDao {

    public HashMap<String, String> getlunarFestivals();

    public HashMap<String, String> getSolarFestivals();

    public List<NlpPeriodTimeConfig> getNlpPeriodTimeConfigs();

    public List<NlpPeriodClockConfig> getNlpPeriodClockConfigs();

}
