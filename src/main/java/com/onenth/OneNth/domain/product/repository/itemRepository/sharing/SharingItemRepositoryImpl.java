package com.onenth.OneNth.domain.product.repository.itemRepository.sharing;

import com.onenth.OneNth.domain.product.entity.*;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.region.entity.QRegion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class SharingItemRepositoryImpl implements SharingItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SharingItem> findByRegionAndTag(List<Integer> regionIds, String tag) {
        QSharingItem item = QSharingItem.sharingItem;
        QTag qTag = QTag.tag;
        QItemImage image = QItemImage.itemImage;
        QRegion region = QRegion.region;

        return queryFactory
                .selectFrom(item)
                .distinct()
                .join(item.region, region).fetchJoin()
                .leftJoin(item.itemImages, image).fetchJoin()
                .join(item.tags, qTag)
                .where(
                        item.region.id.in(regionIds),
                        qTag.name.eq(tag),
                        item.status.in(Status.DEFAULT, Status.IN_PROGRESS)
                )
                .fetch();
    }

    @Override
    public List<SharingItem> findByRegionAndCategory(List<Integer> regionIds, String category) {
        QSharingItem item = QSharingItem.sharingItem;
        QItemImage image = QItemImage.itemImage;
        QRegion region = QRegion.region;

        return queryFactory
                .selectFrom(item)
                .distinct()
                .join(item.region, region).fetchJoin()
                .leftJoin(item.itemImages, image).fetchJoin()
                .where(
                        item.region.id.in(regionIds),
                        item.itemCategory.eq(ItemCategory.valueOf(category.toUpperCase())),
                        item.status.in(Status.DEFAULT, Status.IN_PROGRESS)
                )
                .fetch();
    }

    @Override
    public List<SharingItem> findByRegionName(String regionName) {
        QSharingItem item = QSharingItem.sharingItem;
        QRegion region = QRegion.region;

        String cleanKeyword = regionName.trim();

        return queryFactory
                .selectFrom(item)
                .join(item.region, region).fetchJoin()
                .where(
                        region.regionName.like("%" + cleanKeyword + "%")
                                .and(item.status.in(Status.DEFAULT, Status.IN_PROGRESS))
                )
                .fetch();
    }
}