package com.happyheal.happyhealapp.modules

import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.notifications.impl.NotificationServicesComponentImpl
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait ComponentRegistryImpl
  extends ComponentRegistry
    with ContextWrapperProvider
    with NotificationServicesComponentImpl
    with PersistenceServicesComponentImpl

