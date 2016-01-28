package com.happyheal.happyhealapp.modules

import com.happyheal.happyhealapp.modules.notifications.NotificationServicesComponent
import com.happyheal.happyhealapp.modules.persistence.PersistenceServicesComponent

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait ComponentRegistry
  extends NotificationServicesComponent
    with PersistenceServicesComponent
