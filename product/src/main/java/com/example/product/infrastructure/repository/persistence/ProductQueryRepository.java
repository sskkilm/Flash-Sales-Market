package com.example.product.infrastructure.repository.persistence;

import com.example.product.infrastructure.repository.persistence.entity.EventProductEntity;
import com.example.product.infrastructure.repository.persistence.entity.NormalProductEntity;
import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;
import com.example.product.infrastructure.repository.persistence.entity.QEventProductEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.product.infrastructure.repository.persistence.entity.QProductEntity.productEntity;
import static com.querydsl.jpa.JPAExpressions.treat;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProductEntity> findAllSellableProduct(LocalDateTime now) {
        return queryFactory
                .selectFrom(productEntity)
                .where(
                        isNormalProduct().or(isOpenedLimitedProduct(now))
                )
                .fetch();
    }

    private BooleanExpression isNormalProduct() {
        return productEntity.instanceOf(NormalProductEntity.class);
    }

    private BooleanExpression isOpenedLimitedProduct(LocalDateTime now) {
        return productEntity.instanceOf(EventProductEntity.class)
                .and(treat(productEntity, QEventProductEntity.class).openTime.loe(now));
    }

}
