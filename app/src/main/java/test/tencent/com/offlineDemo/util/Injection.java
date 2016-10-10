/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.tencent.com.offlineDemo.util;


import android.content.Context;

import test.tencent.com.offlineDemo.App;
import test.tencent.com.offlineDemo.util.schedulers.BaseSchedulerProvider;
import test.tencent.com.offlineDemo.util.schedulers.SchedulerProvider;
import test.tencent.com.offlineDemo.vo.DaoSession;

public class Injection {


    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    public static DaoSession daoSessionProvider(Context context){
        return ((App)context).getDaoSession();
    }
}
