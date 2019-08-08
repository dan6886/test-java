package time;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @author Paper
 * @date 2017年10月31日 下午3:45:51
 */
public class TimeNormalizer {

    private static final String PATTERN_STR = "(\\d+年代)|((前|昨|今|明|后)(天|日)?(早|晚)(晨|上|间)?)" + "|(\\d+个?[年月日天][以之]?[前后])"
            + "|(\\d+个?半?(小时|钟头)[以之]?[前后])" // support n小时/钟头 之前，之后
            + "|(\\d+个?半?(小时|钟头|h|H))" + "|(半个?(小时|钟头)[以之]?[前后])" // support
            // 半小时/钟头
            // 之前，之后
            + "|(半个?(小时|钟头))" + "|(\\d+(分|分钟|min)[以之]?[前后])" // support n分钟
            // 之前，之后
            + "|(\\d+(分|分钟|min))" + "|([13]刻钟[以之]?[前后])" // support n刻钟 之前，之后
            + "|([13]刻钟)" + "|(\\d+秒钟?[以之]?[前后])" // support n秒钟 之前，之后
            + "|(\\d+秒钟?)" + "|((上个|下个|上|这|本|下)+(周|星期|礼拜)([一二三四五六七天日]" + "|[1-7])?)" + "|(\\d+(周|星期|礼拜)[以之]?[前后])" // support
            // n周/星期
            // 之前，之后
            + "|((周|星期|礼拜)([一二三四五六七天日]|[1-7]))" + "|((早|晚)?([0-2]?[0-9](点|时)半)(am|AM|pm|PM)?)"
            + "|((早|晚)?(\\d+[:：]\\d+([:：]\\d+)*)\\s*(am|AM|pm|PM)?)"
            + "|((早|晚)?([0-2]?[0-9](点|时)[13一三]刻)(am|AM|pm|PM)?)"
            + "|((早|晚)?(\\d+[时点](\\d+)?分?(\\d+秒?)?)\\s*(am|AM|pm|PM)?)" + "|(大+(前|后)天)"
            + "|([0-9]?[0-9]?[0-9]{2}\\.((10)|(11)|(12)|([1-9]))\\.((?<!\\\\d))([0-3][0-9]|[1-9]))"
            + "|(现在)|(届时)|(这个月)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)日)|(晚些时候)" + "|(今年)|(长期)|(以前)|(过去)|(时期)|(时代)|(当时)|(近来)"
            + "|(当前)|(日(数|多|多少|好几|几|差不多|近|前|后|上|左右))"
            +
            "|((\\d+)点)|(今年([零一二三四五六七八九十百千万]+|\\d+))|(\\d+[:：]\\d+(分|))|((\\d+):(\\d+))|(\\d+/\\d+/\\d+)|(未来)|((充满美丽、希望、挑战的)?未来)|(早上)|(早(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(日前)|(新世纪)|(小时)|(([0-3][0-9]|[1-9])(日|号))|(明天)|(([0-3][0-9]|[1-9])[日号])|((数|多|多少|好几|几|差不多|近|前|后|上|左右)周)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)年)|([一二三四五六七八九十百千万几多]+[天日周月年][后前左右]*)|(每[年月日天小时分秒钟]+)|((\\d+分)+(\\d+秒)?)|([一二三四五六七八九十]+来?[岁年])|([新?|\\d*]世纪末?)|((\\d+)时)|(世纪)|(([零一二三四五六七八九十百千万]+|\\d+)岁)|(今年)|([星期周]+[一二三四五六七])|(星期([零一二三四五六七八九十百千万]+|\\d+))|(([零一二三四五六七八九十百千万]+|\\d+)年)|([本后昨当后明今去前这][一二三四五六七八九十]?[年日天])|([本当那这][一二三四五六七八九十]?[月])|(晚上)|(回归前后)|(晚间)|((\\d+点)+(\\d+分)?(\\d+秒)?左右?)|((\\d+)年代)|("
            +
            "本月(\\d+))|(第(\\d+)天)|((\\d+)岁)|((\\d+)年(\\d+)月)|([去今明]?[年月](底|末))|(([零一二三四五六七八九十百千万]+|\\d+)世纪)|(昨天(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(年度)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)星期)|(年底)|([下个本]+赛季)|(今年(\\d+)月(\\d+)日)|((\\d+)月(\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午(\\d+)时)|(今年晚些时候)|(两个星期)|(过去(数|多|多少|好几|几|差不多|近|前|后|上|左右)周)|(本赛季)|(半个(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(稍晚)|((\\d+)号晚(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(今(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+)年)|(这个时候)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)个小时)|(最(数|多|多少|好几|几|差不多|近|前|后|上|左右)(数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(凌晨)|((\\d+)年(\\d+)月(\\d+)日)|((\\d+)个月)|(今天早(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(第[一二三四五六七八九十\\d+]+季)|(当地时间)|(今(数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)年)|(早晨)|(一段时间)|([本上]周[一二三四五六七])|(凌晨(\\d+)点)|(去年(\\d+)月(\\d+)日)|(年关)|(如今)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)小时)|(当晚)|((\\d+)日晚(\\d+)时)|(([零一二三四五六七八九十百千万]+|\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(每年(\\d+)月(\\d+)日)|(([零一二三四五六七八九十百千万]+|\\d+)周)|((\\d+)月)|(两个小时)|(本周([零一二三四五六七八九十百千万]+|\\d+))|(长久)|(清晨)|((\\d+)号晚)|(春节)|(星期日)|(圣诞)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)段)|(现年)|(当日)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)分钟)|(\\d+(天|日|周|月|年)(后|前|))|((文艺复兴|巴洛克|前苏联|前一|暴力和专制|成年时期|古罗马|我们所处的敏感)+时期)|((\\d+)[年月天])|(清早)|(两年)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(昨天(数|多|多少|好几|几|差不多|近|前|后|上|左右)午(\\d+)时)|(([零一二三四五六七八九十百千万]+|\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(今(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+))|(圣诞节)|(学期)|(\\d+来?分钟)|(过去(数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(星期天)|(夜间)|((\\d+)日凌晨)|(([零一二三四五六七八九十百千万]+|\\d+)月底)|(当天)|((\\d+)日)|(((10)|(11)|(12)|([1-9]))月)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)(数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(今年(\\d+)月份)|(晚(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+)时)|(连[年月日夜])|((\\d+)年(\\d+)月(\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|((一|二|两|三|四|五|六|七|八|九|十|百|千|万|几|多|上|\\d+)+个?(天|日|周|月|年)(后|前|半|))|((胜利的)日子)|(青春期)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(早(数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)点(数|多|多少|好几|几|差不多|近|前|后|上|左右))|([0-9]{4}年)|(周末)|(([零一二三四五六七八九十百千万]+|\\d+)个(数|多|多少|好几|几|差不多|近|前|后|上|左右)小时)|(([(小学)|初中?|高中?|大学?|研][一二三四五六七八九十]?(\\d+)?)?[上下]半?学期)|(([零一二三四五六七八九十百千万]+|\\d+)时期)|(午间)|(次年)|(这时候)|(农历新年)|([春夏秋冬](天|季))|((\\d+)天)|(元宵节)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)分)|((\\d+)月(\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(晚(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+)时(\\d+)分)|(傍晚)|(周([零一二三四五六七八九十百千万]+|\\d+))|((数|多|多少|好几|几|差不多|近|前|后|上|左右)午(\\d+)时(\\d+)分)|(同日)|((\\d+)年(\\d+)月底)|((\\d+)分钟)|((\\d+)世纪)|(冬季)|(国庆)|(年代)|(([零一二三四五六七八九十百千万]+|\\d+)年半)|(今年年底)|(新年)|(本周)|(当地时间星期([零一二三四五六七八九十百千万]+|\\d+))|(([零一二三四五六七八九十百千万]+|\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)岁)|(半小时)|(每周)|(([零一二三四五六七八九十百千万]+|\\d+)周年)|((重要|最后)?时刻)|(([零一二三四五六七八九十百千万]+|\\d+)期间)|(周日)|(晚(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(今后)|(([零一二三四五六七八九十百千万]+|\\d+)段时间)|(明年)|([12][09][0-9]{2}(年度?))|(今天凌晨)|(过去(\\d+)年)|(元月)|((\\d+)月(\\d+)日凌晨)|([前去今明后新]+年)|((\\d+)月(\\d+))|(夏天)|((\\d+)日凌晨(\\d+)时许)|((\\d+)月(\\d+)日)|((\\d+)点半)|(去年底)|(最后一[天刻])|(最(数|多|多少|好几|几|差不多|近|前|后|上|左右)(数|多|多少|好几|几|差不多|近|前|后|上|左右)个月)|(圣诞节?)|(下?个?(星期|周)(一|二|三|四|五|六|七|天))|((\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)年)|(当天(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(每年的(\\d+)月(\\d+)日)|((\\d+)日晚(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(星期([零一二三四五六七八九十百千万]+|\\d+)晚)|(深夜)|(现如今)|([上中下]+午)|(第(一|二|三|四|五|六|七|八|九|十|百|千|万|几|多|\\d+)+个?(天|日|周|月|年))|(昨晚)|(近年)|(今天清晨)|(中旬)|(星期([零一二三四五六七八九十百千万]+|\\d+)早)|(([零一二三四五六七八九十百千万]+|\\d+)战期间)|(星期)|(昨天晚(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(较早时)|(个(数|多|多少|好几|几|差不多|近|前|后|上|左右)小时)|((民主高中|我们所处的|复仇主义和其它危害人类的灾难性疾病盛行的|快速承包电影主权的|恢复自我美德|人类审美力基础设施|饱受暴力、野蛮、流血、仇恨、嫉妒的|童年|艰苦的童年)+时代)|(元旦)|(([零一二三四五六七八九十百千万]+|\\d+)个礼拜)|(礼拜)|(昨日)|([年月]初)|((\\d+)年的(\\d+)月)|(每年)|(([零一二三四五六七八九十百千万]+|\\d+)月份)|(今年(\\d+)月(\\d+)号)|(今年([零一二三四五六七八九十百千万]+|\\d+)月)|((\\d+)月底)|(未来(\\d+)年)|(第([零一二三四五六七八九十百千万]+|\\d+)季)|(\\d?多年)|(([零一二三四五六七八九十百千万]+|\\d+)个星期)|((\\d+)年([零一二三四五六七八九十百千万]+|\\d+)月)|([下上中]午)|(早(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+)点)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)月)|(([零一二三四五六七八九十百千万]+|\\d+)个(数|多|多少|好几|几|差不多|近|前|后|上|左右)月)|(同([零一二三四五六七八九十百千万]+|\\d+)天)|((\\d+)号凌晨)|(夜里)|(两个(数|多|多少|好几|几|差不多|近|前|后|上|左右)小时)|(昨天)|(罗马时代)|(目(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(([零一二三四五六七八九十百千万]+|\\d+)月)|((\\d+)年(\\d+)月(\\d+)号)|(((10)|(11)|(12)|([1-9]))月份?)|([12][0-9]世纪)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)天)|(工作日)|(稍后)|((\\d+)号(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(未来([零一二三四五六七八九十百千万]+|\\d+)年)|([0-9]+[天日周月年][后前左右]*)|(([零一二三四五六七八九十百千万]+|\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(最(数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)刻)|(很久)|((\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)岁)|(去年(\\d+)月(\\d+)号)|(两个月)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)午(\\d+)时)|(两天)|(\\d+个?(小时|星期))|((\\d+)年半)|(较早)|(([零一二三四五六七八九十百千万]+|\\d+)个小时)|([一二三四五六七八九十]+周年)|(星期([零一二三四五六七八九十百千万]+|\\d+)(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(时刻)|((\\d+天)+(\\d+点)?(\\d+分)?(\\d+秒)?)|((\\d+)日([零一二三四五六七八九十百千万]+|\\d+)时)|((\\d+)周年)|(([零一二三四五六七八九十百千万]+|\\d+)早)|(([零一二三四五六七八九十百千万]+|\\d+)日)|(去年(\\d+)月)|(过去([零一二三四五六七八九十百千万]+|\\d+)年)|((\\d+)个星期)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)(数|多|多少|好几|几|差不多|近|前|后|上|左右)天)|(执政期间)|([当前昨今明后春夏秋冬]+天)|(去年(\\d+)月份)|(今(数|多|多少|好几|几|差不多|近|前|后|上|左右))|((\\d+)周)|(两星期)|(([零一二三四五六七八九十百千万]+|\\d+)年代)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)天)|(昔日)|(两个半月)|([印尼|北京|美国]?当地时间)|(连日)|(本月(\\d+)日)|(第([零一二三四五六七八九十百千万]+|\\d+)天)|((\\d+)点(\\d+)分)|([长近多]年)|((\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午(\\d+)时)|(那时)|(冷战时代)|(([零一二三四五六七八九十百千万]+|\\d+)天)|(这个星期)|(去年)|(昨天傍晚)|(近期)|(星期([零一二三四五六七八九十百千万]+|\\d+)早些时候)|((\\d+)([零一二三四五六七八九十百千万]+|\\d+)年)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)两个月)|((\\d+)个小时)|(([零一二三四五六七八九十百千万]+|\\d+)个月)|(当年)|(本月)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)([零一二三四五六七八九十百千万]+|\\d+)个月)|((\\d+)点(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(目前)|(去年([零一二三四五六七八九十百千万]+|\\d+)月)|((\\d+)时(\\d+)分)|(每月)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)段时间)|((\\d+)日晚)|(早(数|多|多少|好几|几|差不多|近|前|后|上|左右)(\\d+)点(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(下旬)|((\\d+)月份)|(逐年)|(稍(数|多|多少|好几|几|差不多|近|前|后|上|左右))|((\\d+)年)|(月底)|(这个月)|((\\d+)年(\\d+)个月)|(\\d+大寿)|(周([零一二三四五六七八九十百千万]+|\\d+)早(数|多|多少|好几|几|差不多|近|前|后|上|左右))|(半年)|(今日)|(末日)|(昨天深夜)|(今年(\\d+)月)|((\\d+)月(\\d+)号)|((\\d+)日夜)|((早些|某个|晚间|本星期早些|前些)+时候)|(同年)|((北京|那个|更长的|最终冲突的)时间)|(每个月)|(一早)|((\\d+)来?[岁年])|((数|多|多少|好几|几|差不多|近|前|后|上|左右)个月)|([鼠牛虎兔龙蛇马羊猴鸡狗猪]年)|(季度)|(早些时候)|(今天)|(每天)|(年半)|(下(个)?月)|(午后)|((\\d+)日(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|((数|多|多少|好几|几|差不多|近|前|后|上|左右)个星期)|(今天(数|多|多少|好几|几|差不多|近|前|后|上|左右)午)|(同[一二三四五六七八九十][年|月|天])|(T\\d+:\\d+:\\d+)|(\\d+/\\d+/\\d+:\\d+:\\d+.\\d+)|(\\?\\?\\?\\?-\\?\\?-\\?\\?T\\d+:\\d+:\\d+)|(\\d+-\\d+-\\d+T\\d+:\\d+:\\d+)|(\\d+/\\d+/\\d+ \\d+:\\d+:\\d+.\\d+)|(\\d+-\\d+-\\d+|[0-9]{8})";
    private Pattern patterns = Pattern.compile(PATTERN_STR);

    private Map<String, String> solarFestivals;

    private Map<String, String> chineseFestivals;

    private List<NlpPeriodTimeConfig> nlpPeriodTimeConfigs;

    private List<NlpPeriodClockConfig> nlpPeriodClockConfigs;

    private Map<String, String[]> periodClockMap;

    public Pattern getPatterns() {
        return patterns;
    }

    public void setPatterns(Pattern patterns) {
        this.patterns = patterns;
    }

    public TimeNormalizer() {
    }

    public TimeNormalizer(Map<String, String> solarFestivalMap, Map<String, String> chineseFestivals,
                          List<NlpPeriodTimeConfig> nlpPeriodTimeConfigs, List<NlpPeriodClockConfig> nlpPeriodClockConfigs) {
        this.solarFestivals = solarFestivalMap;
        this.chineseFestivals = chineseFestivals;
        this.nlpPeriodTimeConfigs = nlpPeriodTimeConfigs;
        this.nlpPeriodClockConfigs = nlpPeriodClockConfigs;
    }

    public TimeNormalizer(String path) {
        if (patterns == null) {
            try {
                patterns = readModel(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TimeNormalizer的构造方法，根据提供的待分析字符串和timeBase进行时间表达式提取 在构造方法中已完成对待分析字符串的表达式提取工作
     *
     * @param target   待分析字符串
     * @param timeBase 给定的timeBase
     * @return 返回值
     */
    public TimeUnit[] parse(String target, String timeBase) {
        target = preHandling(target);
        return timeEx(target, timeBase);
    }

    /**
     * 同上的TimeNormalizer的构造方法，timeBase取默认的系统当前时间
     *
     * @param target 待分析字符串
     * @return 时间单元数组
     */
    public TimeUnit[] parse(String target) {
        if (checkNumeralCountIsLarge(target, 12)) {
            return new TimeUnit[0];
        }
        String currentDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
        String chineseFestival = null;
        String solarFestival = null;
        try {
            TimeUnit timeUnit = new TimeUnit(target, currentDate);
            TimeUnit particularTime = particularDetection(target, timeUnit);
            if (particularTime != null) {
                TimeUnit[] particularTimes = new TimeUnit[1];
                particularTimes[0] = particularTime;
                return particularTimes;
            }
            int year = timeUnit._tp.tunit[0];
            chineseFestival = chineseFestivalDetection(target, year);
            solarFestival = solarFestivalDetection(target, year);

            if (StringUtils.isBlank(chineseFestival) && StringUtils.isBlank(solarFestival)) {
                if (!timeUnit.Time_Initial.equals(currentDate)) {
                    return parse(target, timeUnit.Time_Initial);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(chineseFestival)) {
            target = chineseFestival;
        }

        if (StringUtils.isNotBlank(solarFestival)) {
            target = solarFestival;
        }


        return parse(target, currentDate);
    }

    private boolean checkNumeralCountIsLarge(String target, int size) {
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(target);
        boolean result = m.find();
        int count = 0;
        while (result) {
            count++;
            result = m.find();
        }
        if (count > size) {
            return true;
        }
        return false;
    }

    public TimeUnit[] timeShift(String target, String currentTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(currentTime);
            String currentDateStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
            return parse(target, currentDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new TimeUnit[0];
    }


    /**
     * 待匹配字符串的清理空白符和语气助词以及大写数字转化的预处理
     */
    public String preHandling(String target) {
        Pattern pattern = Pattern.compile(".*([\\d]{1,2}月[\\d]{1,2}+)[^日].*");
        Matcher matcher = pattern.matcher(target);
        if (matcher.find()) {
            String date = matcher.group(1);
            target = target.replace(date, date + "日");
        }

        target = delKeyword(target, "\\s+"); // 清理空白符
        target = numberTranslator(target);// 大写数字转化
        return target;
    }

    /**
     * 有基准时间输入的时间表达式识别
     * <p>
     * 这是时间表达式识别的主方法， 通过已经构建的正则表达式对字符串进行识别，并按照预先定义的基准时间进行规范化 将所有别识别并进行规范化的时间表达式进行返回，
     * 时间表达式通过TimeUnit类进行定义
     *
     * @param tar      输入文本字符串
     * @param timebase 输入基准时间
     * @return TimeUnit[] 时间表达式类型数组
     */
    private TimeUnit[] timeEx(String tar, String timebase) {
        Matcher match;
        int startline = -1, endline = -1;

        String[] temp = new String[99];
        int rpointer = 0;
        TimeUnit[] Time_Result = null;

        match = patterns.matcher(tar);
        boolean startmark = true;
        while (match.find()) {
            startline = match.start();
            if (endline == startline) {
                rpointer--;
                temp[rpointer] = temp[rpointer] + match.group();
            } else if (endline > 1 && endline == startline - 1 && "的".equals(tar.substring(startline - 1, startline))) {
                rpointer--;
                temp[rpointer] = temp[rpointer] + "的" + match.group();
            } else {
                if (!startmark) {
                    rpointer--;
                    rpointer++;
                }
                startmark = false;
                temp[rpointer] = match.group();
            }
            endline = match.end();
            rpointer++;
        }
        if (rpointer > 0) {
            rpointer--;
            rpointer++;
        }
        Time_Result = new TimeUnit[rpointer];
        for (int j = 0; j < rpointer; j++) {
            Time_Result[j] = new TimeUnit(temp[j], timebase);
            timebase = Time_Result[j].getTime_Initial();
        }

        return Time_Result;
    }

    @SuppressWarnings("unused")
    private Pattern readModel(InputStream is) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(is)));
        return readModel(in);
    }

    private Pattern readModel(String file) throws Exception {
        ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
        return readModel(in);
    }

    private Pattern readModel(ObjectInputStream in) throws Exception {

        Pattern p = (Pattern) in.readObject();
        return p = Pattern.compile(p.pattern());
    }

    /**
     * 该方法删除一字符串中所有匹配某一规则字串 可用于清理一个字符串中的空白符和语气助词
     *
     * @param target 待处理字符串
     * @param rules  删除规则
     * @return 清理工作完成后的字符串
     */
    public static String delKeyword(String target, String rules) {
        Pattern p = Pattern.compile(rules);
        Matcher m = p.matcher(target);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            m.appendReplacement(sb, "");
            result = m.find();
        }
        m.appendTail(sb);
        String s = sb.toString();
        return s;
    }

    /**
     * 该方法可以将字符串中所有的用汉字表示的数字转化为用阿拉伯数字表示的数字 如"这里有一千两百个人，六百零五个来自中国"可以转化为
     * "这里有1200个人，605个来自中国" 此外添加支持了部分不规则表达方法 如两万零六百五可转化为20650 两百一十四和两百十四都可以转化为214
     * 一六零加一五八可以转化为160+158 该方法目前支持的正确转化范围是0-99999999 该功能模块具有良好的复用性
     *
     * @param target 待转化的字符串
     * @return 转化完毕后的字符串
     */
    public static String numberTranslator(String target) {
        Pattern p = Pattern.compile("[一二两三四五六七八九123456789]万[一二两三四五六七八九123456789](?!(千|百|十))");
        Matcher m = p.matcher(target);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("万");
            int num = 0;
            if (s.length == 2) {
                num += wordToNumber(s[0]) * 10000 + wordToNumber(s[1]) * 1000;
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("[一二两三四五六七八九123456789]千[一二两三四五六七八九123456789](?!(百|十))");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("千");
            int num = 0;
            if (s.length == 2) {
                num += wordToNumber(s[0]) * 1000 + wordToNumber(s[1]) * 100;
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("[一二两三四五六七八九123456789]百[一二两三四五六七八九123456789](?!十)");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("百");
            int num = 0;
            if (s.length == 2) {
                num += wordToNumber(s[0]) * 100 + wordToNumber(s[1]) * 10;
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("[零一二两三四五六七八九]");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            m.appendReplacement(sb, Integer.toString(wordToNumber(m.group())));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("(?<=(周|星期|礼拜))[天日]");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            m.appendReplacement(sb, Integer.toString(wordToNumber(m.group())));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("(?<!(周|星期|礼拜))0?[0-9]?十[0-9]?");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("十");
            int num = 0;
            if (s.length == 0) {
                num += 10;
            } else if (s.length == 1) {
                int ten = Integer.parseInt(s[0]);
                if (ten == 0)
                    num += 10;
                else
                    num += ten * 10;
            } else if (s.length == 2) {
                if (s[0].equals(""))
                    num += 10;
                else {
                    int ten = Integer.parseInt(s[0]);
                    if (ten == 0)
                        num += 10;
                    else
                        num += ten * 10;
                }
                num += Integer.parseInt(s[1]);
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("0?[1-9]百[0-9]?[0-9]?");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("百");
            int num = 0;
            if (s.length == 1) {
                int hundred = Integer.parseInt(s[0]);
                num += hundred * 100;
            } else if (s.length == 2) {
                int hundred = Integer.parseInt(s[0]);
                num += hundred * 100;
                num += Integer.parseInt(s[1]);
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("0?[1-9]千[0-9]?[0-9]?[0-9]?");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("千");
            int num = 0;
            if (s.length == 1) {
                int thousand = Integer.parseInt(s[0]);
                num += thousand * 1000;
            } else if (s.length == 2) {
                int thousand = Integer.parseInt(s[0]);
                num += thousand * 1000;
                num += Integer.parseInt(s[1]);
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        p = Pattern.compile("[0-9]+万[0-9]?[0-9]?[0-9]?[0-9]?");
        m = p.matcher(target);
        sb = new StringBuffer();
        result = m.find();
        while (result) {
            String group = m.group();
            String[] s = group.split("万");
            int num = 0;
            if (s.length == 1) {
                int tenthousand = Integer.parseInt(s[0]);
                num += tenthousand * 10000;
            } else if (s.length == 2) {
                int tenthousand = Integer.parseInt(s[0]);
                num += tenthousand * 10000;
                num += Integer.parseInt(s[1]);
            }
            m.appendReplacement(sb, Integer.toString(num));
            result = m.find();
        }
        m.appendTail(sb);
        target = sb.toString();

        return target;
    }

    /**
     * 方法numberTranslator的辅助方法，可将[零-九]正确翻译为[0-9]
     *
     * @param s 大写数字
     * @return 对应的整形数，如果不是大写数字返回-1
     */
    private static int wordToNumber(String s) {
        if (s.equals("零") || s.equals("0"))
            return 0;
        else if (s.equals("一") || s.equals("1"))
            return 1;
        else if (s.equals("二") || s.equals("两") || s.equals("2"))
            return 2;
        else if (s.equals("三") || s.equals("3"))
            return 3;
        else if (s.equals("四") || s.equals("4"))
            return 4;
        else if (s.equals("五") || s.equals("5"))
            return 5;
        else if (s.equals("六") || s.equals("6"))
            return 6;
        else if (s.equals("七") || s.equals("天") || s.equals("日") || s.equals("7"))
            return 7;
        else if (s.equals("八") || s.equals("8"))
            return 8;
        else if (s.equals("九") || s.equals("9"))
            return 9;
        else
            return -1;
    }

    private TimeUnit particularDetection(String text, TimeUnit timeUnit) {


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        for (NlpPeriodTimeConfig nlpPeriodTimeConfig : nlpPeriodTimeConfigs) {
            String word = nlpPeriodTimeConfig.getWord();
            String type = nlpPeriodTimeConfig.getType();
            String startDate = nlpPeriodTimeConfig.getStartMonthDate();
            String endDate = nlpPeriodTimeConfig.getEndMonthDate();
            int calculateNumber = nlpPeriodTimeConfig.getCalculateNumber();

            Pattern pattern = Pattern.compile(word);
            Matcher match = pattern.matcher(text);

            switch (type) {
                case "0":
                    if (match.find()) {
                        String present = CalendarUtils.date2Str(calendar.getTime(), "yyyyMMdd");
                        calendar.add(Calendar.DATE, calculateNumber);
                        String start = CalendarUtils.date2Str(calendar.getTime(), "yyyyMMdd");


                        timeUnit.Time_Norm = start + "|" + present;
                        timeUnit.Time_Expression = match.group();
                        return timeUnit;
                    }
                case "1":
                    if (match.find()) {
                        if (month >= 3) {
                            year += 1;
                        }
                        timeUnit.Time_Norm = year + startDate + "|" + year + endDate;
                        timeUnit.Time_Expression = match.group();
                        return timeUnit;
                    }
            }
        }

        return null;
    }

    private String chineseFestivalDetection(String text, int year) throws ParseException {
        if (chineseFestivals == null) {
            chineseFestivals = new HashMap<>();
            chineseFestivals.put("春节", "0101");
            chineseFestivals.put("元宵", "0115");
            chineseFestivals.put("龙抬头节", "0202");
            chineseFestivals.put("妈祖生辰", "0323");
            chineseFestivals.put("端午节", "0505");
            chineseFestivals.put("七夕", "0707");
            chineseFestivals.put("中元节", "0715");
            chineseFestivals.put("中秋节", "0815");
            chineseFestivals.put("重阳节", "0909");
            chineseFestivals.put("腊八节", "1208");
            chineseFestivals.put("小年", "1223");
            chineseFestivals.put("除夕", "1230");
        }

        String festivals = String.join("|", chineseFestivals.keySet());
        String rule = "(" + festivals + ")";

        Pattern pattern = Pattern.compile(rule);
        Matcher match = pattern.matcher(text);

        String month = "";
        String day = "";
        if (match.find()) {
            String festival = match.group(1);
            String date = chineseFestivals.get(festival);
            month = date.substring(0, 2);
            day = date.substring(2, 4);
        }

        if (StringUtils.isBlank(month) || StringUtils.isBlank(day)) {
            if (text.matches(".*农历.+月.+")) {
                Pattern lunarPattern = Pattern.compile("([一|二|三|四|五|六|七|八|九|十|[0-9]]{1,2})月");
                Matcher lunarMatcher = lunarPattern.matcher(text);
                String monthStr = "";
                if (lunarMatcher.find()) {
                    monthStr = lunarMatcher.group(1);
                }

                lunarPattern = Pattern.compile("月([一|二|三|四|五|六|七|八|九|十|[0-9]]{1,3})");
                lunarMatcher = lunarPattern.matcher(text);
                String dayStr = "";
                if (lunarMatcher.find()) {
                    dayStr = lunarMatcher.group(1);
                }

                if (StringUtils.isNotBlank(monthStr) && StringUtils.isNotBlank(dayStr)) {

                    monthStr = preHandling(monthStr);
                    dayStr = preHandling(dayStr);
                    int montInt = Integer.parseInt(monthStr);
                    int dayInt = Integer.parseInt(dayStr);

                    if (montInt > 0 && montInt < 13 && dayInt > 0 && dayInt < 32) {
                        month = monthStr;
                        day = dayStr;
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(month) && StringUtils.isNotBlank(day)) {
            int festivalMonthInt = Integer.parseInt(month);
            int festivalDayInt = Integer.parseInt(day);

            if (year != 0 && year != -1) {
                Solar solar = LunarSolarConverter.LunarToSolar(new Lunar(festivalDayInt, festivalMonthInt, year));
                return solar.solarYear + "年" + solar.solarMonth + "月" + solar.solarDay + "日";
            }

            Date currentDate = Calendar.getInstance().getTime();
            String currentYearStr = new SimpleDateFormat("yyyy").format(currentDate);
            String currentMonthStr = new SimpleDateFormat("MM").format(currentDate);
            String currentDayStr = new SimpleDateFormat("dd").format(currentDate);
            int currentYearInt = Integer.parseInt(currentYearStr);
            int currentMonthInt = Integer.parseInt(currentMonthStr);
            int currentDayInt = Integer.parseInt(currentDayStr);
            Lunar currentLunar = LunarSolarConverter
                    .SolarToLunar(new Solar(currentDayInt, currentMonthInt, currentYearInt));

            Lunar festivalLunar = new Lunar(festivalDayInt, festivalMonthInt, currentLunar.lunarYear);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date festivalLunarDate = sdf.parse(festivalLunar.toString());
            Date currentLunarDate = sdf.parse(currentLunar.toString());

            Solar solar = new Solar();
            int festivalYearInt = currentLunar.lunarYear;
            if (festivalLunarDate.before(currentLunarDate)) {
                festivalYearInt += 1;
            }
            solar = LunarSolarConverter.LunarToSolar(new Lunar(festivalDayInt, festivalMonthInt, festivalYearInt));

            return solar.solarYear + "年" + solar.solarMonth + "月" + solar.solarDay + "日";
        }


        return null;
    }

    private String solarFestivalDetection(String text, int year) throws ParseException {
        if (solarFestivals == null) {
            solarFestivals = new HashMap<>();
            solarFestivals.put("元旦", "0101");
            solarFestivals.put("国庆", "1001");
        }

        String festivals = String.join("|", solarFestivals.keySet());
        String rule = "(" + festivals + ")";

        Pattern pattern = Pattern.compile(rule);
        Matcher match = pattern.matcher(text);
        if (match.find()) {
            String festival = match.group(1);
            String date = solarFestivals.get(festival);
            String month = date.substring(0, 2);
            String day = date.substring(2, 4);

            if (year != 0 && year != -1) {
                return year + "年" + month + "月" + day + "日";
            }

            Date currentDate = Calendar.getInstance().getTime();
            String yearStr = new SimpleDateFormat("yyyy").format(currentDate);
            int yearInt = Integer.parseInt(yearStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date festivalDate = sdf.parse(yearStr + month + day);

            if (festivalDate.before(currentDate)) {
                yearInt += 1;
            }

            return yearInt + "年" + month + "月" + day + "日";
        }
        return null;
    }

    public Map<String, String> checkClock(String word) {
        initPeriodClock();
        for (Map.Entry<String, String[]> entry : periodClockMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (word.contains(key)) {
                String rangeStr = values[0] + "|" + values[1];
                Map<String, String> result = new HashMap<>();
                result.put(key, rangeStr);
                return result;
            }
        }
        return null;
    }

    private void initPeriodClock() {
        if (periodClockMap == null) {
            synchronized (TimeNormalizer.class) {
                if (periodClockMap == null) {
                    periodClockMap = new HashMap<>();
                    for (NlpPeriodClockConfig nlpPeriodClockConfig : nlpPeriodClockConfigs) {
                        String[] range = new String[2];
                        range[0] = nlpPeriodClockConfig.getStart();
                        range[1] = nlpPeriodClockConfig.getEnd();
                        periodClockMap.put(nlpPeriodClockConfig.getWord(), range);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Map<String, String> chineseFestivals = new HashMap<>();
        chineseFestivals = new HashMap<>();
        chineseFestivals.put("春节", "0101");
        chineseFestivals.put("元宵", "0115");
        chineseFestivals.put("龙抬头节", "0202");
        chineseFestivals.put("妈祖生辰", "0323");
        chineseFestivals.put("端午节", "0505");
        chineseFestivals.put("七夕", "0707");
        chineseFestivals.put("中元节", "0715");
        chineseFestivals.put("中秋节", "0815");
        chineseFestivals.put("重阳节", "0909");
        chineseFestivals.put("腊八节", "1208");
        chineseFestivals.put("小年", "1223");
        chineseFestivals.put("除夕", "1230");

        Map<String, String> solarFestivals = new HashMap<>();
        solarFestivals.put("元旦", "0101");
        solarFestivals.put("国庆", "1001");

        List<NlpPeriodTimeConfig> nlpPeriodTimeConfigs = new ArrayList<>();
        TimeNormalizer timeNormalizer = new TimeNormalizer(solarFestivals, chineseFestivals, nlpPeriodTimeConfigs, new ArrayList<>());
        // 本后昨当新后明今去前那这
        // 本当那这
        long start = System.currentTimeMillis();
        TimeUnit[] timeUnits = timeNormalizer.parse("上周礼拜一天气怎么样");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(Arrays.toString(timeNormalizer.parse("上周礼拜一天气怎么样")));

        System.out.println(Arrays.toString(timeNormalizer.parse("上个礼拜一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("上上礼拜一天气怎么样")));

        System.out.println(Arrays.toString(timeNormalizer.parse("上周一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("上周星期一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("上个星期五天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("这周礼拜一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("下个礼拜一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("下周礼拜一天气怎么样")));
        System.out.println(Arrays.toString(timeNormalizer.parse("下个周礼拜一天气怎么样")));
        for (TimeUnit timeUnit : timeUnits) {
            System.out.println(timeUnit);
        }


        TimeUnit[] timeUnits1 = timeNormalizer.timeShift("前一个月", "20180901");
        System.out.println(timeUnits1);
        for (TimeUnit timeUnit : timeUnits1) {
            System.out.println(timeUnit);
        }

    }

}
