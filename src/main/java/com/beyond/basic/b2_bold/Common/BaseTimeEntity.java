package com.beyond.basic.b2_bold.Common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;



// 기본적으로 Entity는 상속이 불가 MpppedSuperclass 사용시 상속 가능
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    // 컬럼명에 캐멀케이스 사용시, db에는  created_time으로 컬럼 생성
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
