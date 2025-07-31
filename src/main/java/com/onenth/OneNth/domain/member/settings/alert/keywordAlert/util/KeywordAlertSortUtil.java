package com.onenth.OneNth.domain.member.settings.alert.keywordAlert.util;

import com.onenth.OneNth.domain.member.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.member.entity.RegionKeywordAlert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KeywordAlertSortUtil {
    public static List<Object> mergeAndSortAlerts(List<ProductKeywordAlert> productKeywordAlertList, List<RegionKeywordAlert> regionKeywordAlertList) {
        List<Object> mergedAlerts = new ArrayList<>();
        mergedAlerts.addAll(productKeywordAlertList);
        mergedAlerts.addAll(regionKeywordAlertList);

        mergedAlerts.sort(Comparator.comparing(alert ->
                        (alert instanceof ProductKeywordAlert)
                                ? ((ProductKeywordAlert) alert).getCreatedAt()
                                : ((RegionKeywordAlert) alert).getCreatedAt(),
                Comparator.reverseOrder())
        );

        return mergedAlerts;
    }
}
