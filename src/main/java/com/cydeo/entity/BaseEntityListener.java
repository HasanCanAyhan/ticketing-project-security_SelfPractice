package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    //in the db, user , project , when it create , update, we keeping track, who did it , to show details


    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")){ //spring understands this name
            Object principal =authentication.getPrincipal();
            baseEntity.setInsertUserId( ( (UserPrincipal) principal ).getId()    ); // security giving id
            //in th DB, user has id, we should give this id to Security, bcs Security's user does not have id.go to UserPrincipal
            baseEntity.setLastUpdateUserId( ( (UserPrincipal) principal ).getId()  );
        }

    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal =authentication.getPrincipal();
            baseEntity.setLastUpdateUserId( ( (UserPrincipal) principal ).getId()  );
        }

    }


}
